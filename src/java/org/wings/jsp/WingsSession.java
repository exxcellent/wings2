/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.jsp;

import org.wings.Renderable;
import org.wings.RequestURL;
import org.wings.SComponent;
import org.wings.SForm;
import org.wings.SFrame;
import org.wings.event.SRequestEvent;
import org.wings.event.SRequestListener;
import org.wings.externalizer.ExternalizedResource;
import org.wings.io.StringBuilderDevice;
import org.wings.plaf.css.Utils;
import org.wings.session.Session;
import org.wings.session.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author hengels
 */
public class WingsSession
        extends Session
{
    public static WingsSession getSession(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        synchronized (request.getSession()) {
            String key = "Session:" + request.getSession().getServletContext().getServletContextName();
            WingsSession wingsSession = (WingsSession)request.getSession().getAttribute(key);
            if (wingsSession == null) {
                wingsSession = new WingsSession();
                request.getSession().setAttribute(key, wingsSession);
                SessionManager.setSession(wingsSession);

                wingsSession.init(request);
                RequestURL requestURL = new RequestURL("", response.encodeURL("foo").substring(3));
                wingsSession.setProperty("request.url", requestURL);
            }

            SessionManager.setSession(wingsSession);
            wingsSession.setServletRequest(request);
            wingsSession.setServletResponse(response);

            return wingsSession;
        }
    }

    public static void removeSession() {
        SessionManager.removeSession();
    }

    public static SFrame getFrame(WingsSession wingsSession) throws ServletException {
        String path = wingsSession.getServletRequest().getServletPath();
        int pos = path.lastIndexOf('/');
        path = path.substring(pos + 1);

        synchronized (wingsSession.getServletRequest().getSession()) {
            Map frames = getFrames(wingsSession);
            SFrame frame = (SFrame)frames.get(path);
            if (frame == null) {
                frame = new SFrame(path);
                frame.setTargetResource(path);
                frame.show();
                frames.put(path, frame);
            }
            return frame;
        }
    }

    private static Map getFrames(WingsSession wingsSession) {
        Map frames = (Map) wingsSession.getProperty("frames");
        if (frames == null) {
            frames = new HashMap();
            wingsSession.setProperty("frames", frames);
        }
        return frames;
    }

    public static void dispatchEvents(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        synchronized (request.getSession()) {
            WingsSession wingsSession = getSession(request, response);
            try {
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

                wingsSession.getReloadManager().invalidateFrames();
                wingsSession.getReloadManager().notifyCGs();
            }
            finally {
                wingsSession.getReloadManager().clear();
                SessionManager.removeSession();
                SForm.clearArmedComponents();
            }
        }
    }

    public void addComponent(String name, SComponent component) throws ServletException {
        SFrame frame = getFrame(this);
        frame.getContentPane().add(component, name);
        setProperty(name, component);
    }

    public SComponent getComponent(String name) {
        return (SComponent)getProperty(name);
    }

    public SComponent removeComponent(String name) throws ServletException {
        SFrame frame = getFrame(this);
        SComponent component = (SComponent) removeProperty(name);
        frame.getContentPane().remove(component);
        return component;
    }

    public static void writeHeaders(HttpServletRequest request, HttpServletResponse response, JspWriter out) throws IOException, ServletException {
        synchronized (request.getSession()) {
            WingsSession wingsSession = getSession(request, response);
            SFrame frame = getFrame(wingsSession);

            StringBuilderDevice headerdev = new StringBuilderDevice();
            for (Iterator iterator = frame.getHeaders().iterator(); iterator.hasNext();) {
                Object next = iterator.next();
                if (next instanceof Renderable) {
                    ((Renderable) next).write(headerdev);
                } else {
                    Utils.write(headerdev, next.toString());
                }
                headerdev.write("\n".getBytes());
            }
            out.print(headerdev);

            SessionManager.removeSession();
        }
    }

    public static void writeComponent(HttpServletRequest request, HttpServletResponse response, JspWriter out, SComponent component) throws IOException, ServletException {
        synchronized (request.getSession()) {
            StringBuilderDevice outdev = new StringBuilderDevice();
            component.write(outdev);
            out.print(outdev);
            SessionManager.removeSession();
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
