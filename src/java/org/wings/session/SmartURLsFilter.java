/* $Id$ */
/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.session;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hengels
 */
public class SmartURLsFilter
        implements Filter {
    private static Logger logger = Logger.getLogger(SmartURLsFilter.class.getPackage().getName());
    private String parameterSeparator = ";";
    private String nameValueSeparator = ",";
    private Pattern encodePattern;
    private Pattern decodePattern;

    public void init(FilterConfig filterConfig) throws ServletException {
        if (filterConfig.getInitParameter("wings.servlet.smarturls.parameterSeparator") != null)
            parameterSeparator = filterConfig.getInitParameter("wings.servlet.smarturls.parameterSeparator");

        if (filterConfig.getInitParameter("wings.servlet.smarturls.nameValueSeparator") != null)
            nameValueSeparator = filterConfig.getInitParameter("wings.servlet.smarturls.nameValueSeparator");

        logger.config("wings.servlet.smarturls.parameterSeparator " + parameterSeparator);
        logger.config("wings.servlet.smarturls.nameValueSeparator " + nameValueSeparator);

        encodePattern = Pattern.compile("(" + "\\?|&" + ")([a-zA-Z0-9%+.-[*]_]*)" +
                "(" + "=" + ")([a-zA-Z0-9%+.-[*]_=/:]*)");
        decodePattern = Pattern.compile("(" + parameterSeparator + ")([a-zA-Z0-9%+.-[*]_]*)" +
                "(" + nameValueSeparator + ")([a-zA-Z0-9%+.-[*]_=/:]*)");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        ServletRequest request = servletRequest;
        ServletResponse response = servletResponse;

        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

            MyHttpServletRequestWrapper requestWrapper = new MyHttpServletRequestWrapper(httpServletRequest);
            if (requestWrapper.getPathInfo() == null || requestWrapper.getPathInfo().indexOf('.') == -1) {
                response = new MyHttpServletResponseWrapper(servletResponse);

                if ("get".equalsIgnoreCase(httpServletRequest.getMethod()))
                    request = requestWrapper;

                logger.finer("wrap " + requestWrapper.getPathInfo());
            } else
                logger.finer("don't wrap " + requestWrapper.getPathInfo());
        }
        filterChain.doFilter(request, response);
    }

    public void destroy() {
    }

    public static final String replace(String s,
                                       String toFind, String replace) {
        StringBuffer erg = new StringBuffer();

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

    private class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private Map parameterMap;
        private String pathInfo;
        private String queryString;

        public MyHttpServletRequestWrapper(HttpServletRequest httpServletRequest) {
            super(httpServletRequest);

            pathInfo = httpServletRequest.getPathInfo();
            queryString = httpServletRequest.getQueryString();
            parameterMap = httpServletRequest.getParameterMap();
            logger.finer("pathInfo = " + pathInfo);
            logger.finer("queryString = " + queryString);
            logger.finer("parameterMap = " + parameterMap);

            if (pathInfo == null)
                return;

            int pos = pathInfo.indexOf(parameterSeparator);
            if (pos != -1) {
                queryString = pathInfo.substring(pos);
                pathInfo = pathInfo.substring(0, pos);

                parameterMap = new HashMap();
                StringBuffer buffer = new StringBuffer(queryString.length());
                Matcher matcher = decodePattern.matcher(queryString);
                while (matcher.find()) {
                    String param = matcher.group();
                    pos = param.indexOf(nameValueSeparator);
                    parameterMap.put(param.substring(1, pos), param.substring(pos + 1));
                    matcher.appendReplacement(buffer, "&$2=$4");
                }
                queryString = buffer.substring(1);
                logger.finer("modified pathInfo = " + pathInfo);
                logger.finer("modified queryString = " + queryString);
                logger.finer("modified parameterMap = " + parameterMap);
            }
        }

        public String getRequestURI() {
            System.out.println("*************** getRequestURI called");
            return super.getRequestURI();
        }

        public Map getParameterMap() {
            return parameterMap;
        }

        public String getPathInfo() {
            return pathInfo;
        }

        public String getQueryString() {
            return queryString;
        }

        public String getParameter(String string) {
            Object value = getParameterMap().get(string);
            if (value instanceof String)
                return (String) value;
            else
                return ((String[]) value)[0];
        }

        public Enumeration getParameterNames() {
            return Collections.enumeration(getParameterMap().keySet());
        }

        public String[] getParameterValues(String string) {
            Object value = getParameterMap().get(string);
            if (value instanceof String)
                return new String[]{(String) value};
            else
                return (String[]) value;
        }
    }

    private static class MyHttpServletResponseWrapper extends HttpServletResponseWrapper {
        public MyHttpServletResponseWrapper(ServletResponse servletResponse) {
            super((HttpServletResponse) servletResponse);
        }

        public ServletOutputStream getOutputStream() throws IOException {
            final ServletOutputStream superOut = super.getOutputStream();
            return new ServletOutputStream() {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream(1000);

                public void write(int b) throws IOException {
                    bytes.write(b);
                }

                public void close() throws IOException {
                    encode(bytes, superOut);
                    superOut.close();
                }
            };
        }
    }


    private static void encode(ByteArrayOutputStream bytes, ServletOutputStream out) throws IOException {
        String regex = "(href|src|action) *= *\"([^\"]*)\"";

        SmartURLsFilter smartURLsFilter = new SmartURLsFilter();
        String replacement =
                smartURLsFilter.parameterSeparator + "$2" +
                smartURLsFilter.nameValueSeparator + "$4";

        smartURLsFilter.encodePattern = Pattern.compile("(" + "\\?|&" + ")([a-zA-Z0-9%+.-[*]_]*)" +
                "(" + "=" + ")([a-zA-Z0-9%+.-[*]_=/]*)");

        CharBuffer chars = CharBuffer.wrap(bytes.toString());
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(chars);
        int pos = 0;
        while (matcher.find()) {
            out.print(chars.subSequence(pos, matcher.start()).toString());
            pos = matcher.end();
            Matcher matcher2 = smartURLsFilter.encodePattern.matcher(matcher.group(2));
            String group2 = matcher2.replaceAll(replacement);

            out.print(matcher.group(1) + "=\"" + group2 + "\"");
        }
        out.print(chars.subSequence(pos, bytes.size()).toString());
    }
}
