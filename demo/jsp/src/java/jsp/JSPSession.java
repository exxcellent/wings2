/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package jsp;

import org.wings.session.Session;
import org.wings.externalizer.ExternalizedResource;
import org.wings.event.SRequestEvent;
import org.wings.event.SRequestListener;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author hengels
 * @version $Revision$
 */
public class JSPSession
        extends Session
{
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
