/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.jsp;

import org.wings.session.Session;
import org.wings.session.SessionManager;
import org.wings.externalizer.ExternalizedResource;
import org.wings.event.SRequestEvent;
import org.wings.event.SRequestListener;
import org.wings.RequestURL;
import org.wings.SFrame;
import org.wings.SForm;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;

/**
 * @author hengels
 * @version $Revision$
 */
public class WingsSession
        extends Session
{
    private final Map frames = new HashMap();

    public static WingsSession getSession(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String key = "Session:" + request.getSession().getServletContext().getServletContextName();
        WingsSession wingsSession = (WingsSession)request.getSession().getAttribute(key);
        if (wingsSession == null) {
            wingsSession = new WingsSession();
            request.getSession().setAttribute(key, wingsSession);
            SessionManager.setSession(wingsSession);

            wingsSession.init(request);
            //RequestURL requestURL = new RequestURL("", response.encodeURL("foo").substring(3));
            RequestURL requestURL = new RequestURL("TreeExample.jsp", response.encodeURL("TreeExample.jsp"));
            wingsSession.setProperty("request.url", requestURL);
        }

        SessionManager.setSession(wingsSession);
        wingsSession.setServletRequest(request);
        wingsSession.setServletResponse(response);

        return wingsSession;
    }

    public SFrame getFrame(String page) {
        SFrame frame = (SFrame)frames.get(page);
        if (frame == null) {
            frame = new SFrame(page);
            frame.setTargetResource("");
            //frame.setTargetResource("TreeExample.jsp");
            frame.show();
            frames.put(page, frame);
        }
        return frame;
    }

    public void dispatchEvents(HttpServletRequest request) {
        String key = "Session:" + request.getSession().getServletContext().getServletContextName();
        WingsSession wingsSession = (WingsSession)request.getSession().getAttribute(key);
        SForm.clearArmedComponents();

        Enumeration en = request.getParameterNames();
        if (en.hasMoreElements()) {
            wingsSession.fireRequestEvent(SRequestEvent.DISPATCH_START);

            while (en.hasMoreElements()) {
                String paramName = (String) en.nextElement();
                String[] value = request.getParameterValues(paramName);
                wingsSession.getDispatcher().dispatch(paramName, value);
            }
            SForm.fireEvents();

            wingsSession.fireRequestEvent(SRequestEvent.DISPATCH_DONE);
        }
    }

    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    public void fireRequestEvent(int type) {
        fireRequestEvent(type, null);
    }

    public void fireRequestEvent(int type, ExternalizedResource resource) {
        SRequestEvent event = null;

        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SRequestListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new SRequestEvent(this, type, resource);
                }
                ((SRequestListener) listeners[i + 1]).processRequest(event);
            }
        }
    }
}
