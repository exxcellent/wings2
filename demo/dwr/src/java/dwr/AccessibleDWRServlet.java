/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package dwr;

import uk.ltd.getahead.dwr.DWRServlet;
import uk.ltd.getahead.dwr.Configuration;
import uk.ltd.getahead.dwr.util.Log;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

/**
 * @author hengels
 * @version $Revision$
 */
public class AccessibleDWRServlet
    extends DWRServlet
{
    private ServletConfig config;

    public void init(ServletConfig config)
        throws ServletException
    {
        this.config = config;
        String logLevel = config.getInitParameter("logLevel");
        if (logLevel != null)
            Log.setLevel(logLevel);

        String debugStr = config.getInitParameter("debug");
        boolean debug = Boolean.valueOf(debugStr).booleanValue();

        configuration = new Configuration(debug);
        try {
            InputStream in = config.getServletContext().getResourceAsStream("/WEB-INF/dwr.xml");
            configuration.addConfig(in);
        }
        catch (SAXException ex) {
            throw new ServletException("Parse error reading from dwr.xml", ex);
        }

        converterManager = configuration.getConverterManager();
        creatorManager = new SessionCreatorManager();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        SessionCreatorManager.setSession(req.getSession());
        super.doGet(req, resp);
        SessionCreatorManager.removeSession();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        SessionCreatorManager.setSession(req.getSession());
        super.doPost(req, resp);
        SessionCreatorManager.removeSession();
    }

    public ServletConfig getServletConfig() {
        return config;
    }

    public String getServletName() {
        return getServletConfig().getServletName();
    }

    public void log(String s) {
        System.err.println(s);
    }

    public void log(String s, Throwable throwable) {
        if (throwable != null)
            throwable.printStackTrace(System.err);
        System.err.println(s);
    }
}
