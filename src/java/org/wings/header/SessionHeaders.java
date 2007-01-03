package org.wings.header;

import org.wings.SFrame;
import org.wings.session.SessionManager;
import org.wings.util.SessionLocal;

import java.util.*;

public class SessionHeaders {

    private static SessionLocal headerList = new SessionLocal() {
        protected Object initialValue() {
            return new ArrayList();
        }
    };

    private static SessionLocal linkCounts = new SessionLocal() {
        protected Object initialValue() {
            return new HashMap();
        }
    };

    private static final SessionHeaders INSTANCE = new SessionHeaders();

    private SessionHeaders() {}

    public static SessionHeaders getInstance() {
        return INSTANCE;
    }

    public List getHeaders() {
        return new ArrayList(getHeaderList());
    }

    public void registerHeaders(List headers) {
        for (Iterator i = headers.iterator(); i.hasNext();) {
            registerHeader(i.next());
        }
    }

    public void registerHeader(Object header) {
        if (header == null)
            throw new IllegalArgumentException("Header must not be null!");

        if (getLinkCount(header) == 0) {
            Set frames = SessionManager.getSession().getFrames();
            for (Iterator i = frames.iterator(); i.hasNext();) {
                ((SFrame) i.next()).addHeader(header);
            }
            getHeaderList().add(header);
        }
        incrementLinkCount(header);
    }

    public void deregisterHeaders(List headers) {
        for (Iterator i = headers.iterator(); i.hasNext();) {
            deregisterHeader(i.next());
        }
    }

    public void deregisterHeader(Object header) {
        if (header == null)
            throw new IllegalArgumentException("Header must not be null!");

        decrementLinkCount(header);

        if (getLinkCount(header) == 0) {
            Set frames = SessionManager.getSession().getFrames();
            for (Iterator i = frames.iterator(); i.hasNext();) {
                ((SFrame) i.next()).removeHeader(header);
            }
            getHeaderList().remove(header);
        }
    }

    private List getHeaderList() {
        return (List) headerList.get();
    }

    private Map getLinkCounts() {
        return (Map) linkCounts.get();
    }

    private int getLinkCount(Object header) {
        Integer linkCount = (Integer) getLinkCounts().get(header);
        if (linkCount == null) {
            return 0;
        } else {
            return linkCount.intValue();
        }
    }

    private void incrementLinkCount(Object header) {
        getLinkCounts().put(header, new Integer(getLinkCount(header) + 1));
    }

    private void decrementLinkCount(Object header) {
        getLinkCounts().put(header, new Integer(getLinkCount(header) - 1));
    }

}
