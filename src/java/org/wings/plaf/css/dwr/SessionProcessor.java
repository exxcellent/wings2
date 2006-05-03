package org.wings.plaf.css.dwr;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wings.session.SessionManager;

import uk.ltd.getahead.dwr.impl.DefaultProcessor;
import uk.ltd.getahead.dwr.util.Logger;

public class SessionProcessor
    extends DefaultProcessor
{
    private static final Logger log = Logger.getLogger(SessionProcessor.class);

    public void handle(HttpServletRequest req, HttpServletResponse res)
        throws IOException
    {
        String pathinfo = req.getPathInfo();
        if (pathinfo != null &&
                (pathinfo.equalsIgnoreCase('/' + FILE_ENGINE)
                        || pathinfo.equalsIgnoreCase('/' + FILE_UTIL)
                        || pathinfo.equalsIgnoreCase('/' + FILE_DEPRECATED)
                        /*|| pathinfo.startsWith(PATH_INTERFACE)*/))
        {
            res.setDateHeader("Expires", -1);
        }

        boolean clearSession = SessionManager.getSession() == null;

        try {
            super.handle(req, res);
        }
        finally {
            if (clearSession)
                SessionManager.removeSession();
        }
    }
    
    /*
    protected void doExec(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        ExecuteQuery eq = new ExecuteQuery(req, creatorManager, converterManager, accessControl);

        if (eq.isFailingBrowser())
        {
            resp.setContentType(MIME_HTML);

            PrintWriter out = resp.getWriter();
            out.println("//<script type='text/javascript'>"); //$NON-NLS-1$
            out.println("alert('Your browser sent a request that could not be understood.\\nIf you understand how Javascript works in your browser, please help us fix the problem.\\nSee the mailing lists at http://www.getahead.ltd.uk/dwr/ for more information.');"); //$NON-NLS-1$
            out.println("//</script>"); //$NON-NLS-1$
            out.flush();
            return;
        }

        boolean clearSession = SessionManager.getSession() == null;
        Call[] calls;

        try {
            calls = eq.execute();
        }
        finally {
            if (clearSession)
                SessionManager.removeSession();
        }

        for (int i = 0; i < calls.length; i++)
        {
            Call call = calls[i];
            if (call.getThrowable() != null)
            {
                log.warn("Erroring: id[" + call.getId() + "] message[" + call.getThrowable().getMessage() + ']', call.getThrowable()); //$NON-NLS-1$ //$NON-NLS-2$
            }
            else
            {
                log.debug("Returning: id[" + call.getId() + "] init[" + call.getReply().getInitCode() + "] assign[" + call.getReply().getAssignCode() + "] xml[" + eq.isXmlMode() + ']'); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            }
        }

        // We build the answer up in a SStringBuilder because that makes is easier
        // to debug, and because that's only what the compiler does anyway.
        SStringBuilder buffer = new SStringBuilder();

        // if we are in html (iframe mode) we need to direct script to the parent
        String prefix = eq.isXmlMode() ? "" : "window.parent."; //$NON-NLS-1$ //$NON-NLS-2$

        // iframe mode starts as HTML, so get into script mode
        if (!eq.isXmlMode())
        {
            buffer.append("<script type='text/javascript'>\n"); //$NON-NLS-1$
        }

        for (int i = 0; i < calls.length; i++)
        {
            Call call = calls[i];
            if (call.getThrowable() != null)
            {
                String output = StringEscapeUtils.escapeJavaScript(call.getThrowable().getMessage());

                buffer.append(prefix);
                buffer.append("DWREngine._handleError('"); //$NON-NLS-1$
                buffer.append(call.getId());
                buffer.append("', '"); //$NON-NLS-1$
                buffer.append(output);
                buffer.append("');\n"); //$NON-NLS-1$
            }
            else
            {
                buffer.append(call.getReply().getInitCode());
                buffer.append('\n');

                buffer.append(prefix);
                buffer.append("DWREngine._handleResponse('"); //$NON-NLS-1$
                buffer.append(call.getId());
                buffer.append("', "); //$NON-NLS-1$
                buffer.append(call.getReply().getAssignCode());
                buffer.append(");\n"); //$NON-NLS-1$
            }
        }

        // iframe mode needs to get out of script mode
        if (!eq.isXmlMode())
        {
            buffer.append("</script>\n"); //$NON-NLS-1$
        }

        String reply = buffer.toString();
        log.debug(reply);

        // LocalUtil.addNoCacheHeaders(resp);
        resp.setContentType(eq.isXmlMode() ? MIME_XML : MIME_HTML);
        PrintWriter out = resp.getWriter();
        out.print(reply);
        out.flush();
    }
    */
}
