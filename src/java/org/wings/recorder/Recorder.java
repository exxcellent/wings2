/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.recorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.util.SStringBuilder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author hengels
 */
public class Recorder
        implements Filter {
    private final transient static Log log = LogFactory.getLog(Recorder.class);
    public static final String RECORDER_START = "recorder_start";
    public static final String RECORDER_STOP = "recorder_stop";
    public static final String RECORDER_SCRIPT = "recorder_script";

    private File file;
    private List list;
    private String scriptName = "Recording";
    private String lookupName = "SessionServlet";

    public void init(FilterConfig filterConfig) throws ServletException {
        if (filterConfig.getInitParameter("wings.servlet.recorder.script") != null)
            scriptName = filterConfig.getInitParameter("wings.servlet.recorder.script");

        lookupName = filterConfig.getInitParameter("wings.servlet.lookupname");

        if (lookupName == null || lookupName.trim().length() == 0) {
            lookupName = "SessionServlet:" + filterConfig.getInitParameter("wings.mainclass");
        }

        log.info("wings.servlet.lookupname " + lookupName);
        log.info("wings.servlet.recorder.script " + scriptName);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            if (servletRequest instanceof HttpServletRequest) {
                HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
                Map map = servletRequest.getParameterMap();
                if (map.containsKey(RECORDER_SCRIPT)) {
                    log.info("recorder_script " + map.get(RECORDER_SCRIPT));
                    String[] values = (String[]) map.get(RECORDER_SCRIPT);
                    scriptName = values[0];
                }
                if (map.containsKey(RECORDER_START)) {
                    if (list != null)
                        return;
                    log.info(RECORDER_START);
                    list = new LinkedList();
                } else if (map.containsKey(RECORDER_STOP)) {
                    if (list == null)
                        return;
                    log.info(RECORDER_STOP);
                    writeCode();
                    list = null;
                } else if (list != null) {
                    String resource = httpServletRequest.getPathInfo();
                    log.debug("PATH_INFO: " + resource);

                    Request record;
                    if ("GET".equalsIgnoreCase(httpServletRequest.getMethod()))
                        record = new GET(resource);
                    else
                        record = new POST(resource);

                    Enumeration parameterNames = httpServletRequest.getParameterNames();
                    while (parameterNames.hasMoreElements()) {
                        String name = (String) parameterNames.nextElement();
                        String[] values = httpServletRequest.getParameterValues(name);
                        addEvent(record, name, values);
                    }
                    Enumeration headerNames = httpServletRequest.getHeaderNames();
                    while (headerNames.hasMoreElements()) {
                        String name = (String) headerNames.nextElement();
                        if (name.equalsIgnoreCase("cookie") ||
                                name.equalsIgnoreCase("referer"))
                            continue;
                        addHeader(record, name, httpServletRequest.getHeader(name));
                    }
                    list.add(record);
                }
            }
        } finally {
            if (servletResponse instanceof HttpServletResponse) {
                filterChain.doFilter(servletRequest, new HttpServletResponseWrapper((HttpServletResponse) servletResponse) {
                    public ServletOutputStream getOutputStream() throws IOException {
                        final ServletOutputStream out = super.getOutputStream();
                        return new ServletOutputStream() {
                            public void write(int b) throws IOException {
                                out.write(b);
                            }

                            public void close() throws IOException {
                                super.println("<hr/><div align=\"center\">");
                                super.println("<form method=\"get\" action=\"\">");
                                super.println("<input type=\"text\" name=\"recorder_script\" value=\"" + scriptName + "\">");
                                super.println("<input type=\"submit\" name=\"recorder_start\" value=\"start\">");
                                super.println("<input type=\"submit\" name=\"recorder_stop\" value=\"stop\">");
                                super.println("</div></form>");
                                super.close();
                            }
                        };
                    }
                });
            } else
                filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void addHeader(Request record, String name, String value) {
        if (record instanceof GET) {
            GET get = (GET) record;
            get.addHeader(name, value);
        } else if (record instanceof POST) {
            POST post = (POST) record;
            post.addHeader(name, value);
        }
    }

    private void addEvent(Request record, String name, String[] values) {
        if (record instanceof GET) {
            GET get = (GET) record;
            get.addEvent(name, values);
        } else if (record instanceof POST) {
            POST post = (POST) record;
            post.addEvent(name, values);
        }
    }

    public void destroy() {
        writeCode();
    }

    private void writeCode() {
        PrintWriter out = null;
        if (list == null || list.size() == 0)
            return;
        try {
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                Request record = (Request) iterator.next();
                if (record.getResource().indexOf(".") == -1)
                    record.setResource("");
            }

            file = new File(scriptName + ".java");
            out = new PrintWriter(new FileWriter(file));
            out.println("import org.wings.recorder.*;");
            out.println();
            out.println("public class " + scriptName);
            out.println("    extends Script");
            out.println("{");
            out.println("    public void execute()");
            out.println("        throws Exception");
            out.println("    {");

            long millis = ((Request) list.get(0)).getMillis();

            int index = 0;
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                Request record = (Request) iterator.next();
                if (index > 0)
                    out.println();
                out.println("        delay(" + (record.getMillis() - millis) + ");");
                out.print("        " + record.getMethod() + " request" + index +
                        " = new " + record.getMethod() + "(\"" + record.getResource() + "\")");

                for (Iterator iterator2 = record.getHeaders().iterator(); iterator2.hasNext();) {
                    Request.Header header = (Request.Header) iterator2.next();
                    out.println();
                    out.print("            .addHeader(\"");
                    out.print(header.getName());
                    out.print("\", \"");
                    out.print(replace(header.getValue(), "\"", "\\\""));
                    out.print("\")");
                }
                for (Iterator iterator2 = record.getEvents().iterator(); iterator2.hasNext();) {
                    Request.Event event = (Request.Event) iterator2.next();
                    out.println();
                    out.print("            .addEvent(\"");
                    out.print(event.getName());
                    out.print("\", new String[] { \"");
                    for (int i = 0; i < event.getValues().length; i++) {
                        String value = event.getValues()[i];
                        if (i > 0)
                            out.print("\", \"");
                        out.print(replace(value, "\"", "\\\""));
                    }
                    out.print("\" })");
                }
                out.println(";");
                out.println("        send(request" + index + ");");
                millis = record.getMillis();
                index++;
            }

            out.println("    }");
            out.println("}");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { out.close(); } catch (Exception ign) {/*ignored*/}
        }
    }

    public static final String replace(String s,
                                       String toFind, String replace) {
        SStringBuilder erg = new SStringBuilder();

        int lastindex = 0;
        int indexOf = s.indexOf(toFind);
        if (indexOf == -1) return s;
        while (indexOf != -1) {
            erg.append(s.substring(lastindex, indexOf)).append(replace);
            lastindex = indexOf + toFind.length();
            indexOf = s.indexOf(toFind, lastindex);
        }

        erg.append(s.substring(lastindex));

        return erg.toString();
    }
}
