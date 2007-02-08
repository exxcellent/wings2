package org.wings.header;

import org.wings.SFrame;
import org.wings.session.SessionManager;
import org.wings.util.SessionLocal;

import java.util.*;

/**
 * A class to manage headers which should be appended to all frames within a wingS session.
 */
public final class SessionHeaders {

    protected static final SessionLocal<SessionHeaders> INSTANCE = new SessionLocal<SessionHeaders>() {
        protected SessionHeaders initialValue() {
            return new SessionHeaders();
        }
    };

    protected final List<Header> headerList = new ArrayList<Header>();
    protected final Map<Header, Integer> linkCounts = new HashMap<Header, Integer>();

    /**
     * Session local manager class.
     */
    private SessionHeaders() {}

    /**
     * @return The session local instance of this class.
     */
    public static SessionHeaders getInstance() {
        return INSTANCE.get();
    }

    /**
     * @return The headers currently attached to all frames inside this wingS session.
     */
    public List<Header> getHeaders() {
        return Collections.unmodifiableList(headerList);
    }

    public void registerHeaders(List<? extends Header> headers) {
        for (Header header : headers) {
            registerHeader(header);
        }
    }

    public void registerHeader(Header header) {
        if (header == null)
            throw new IllegalArgumentException("Header must not be null!");

        if (getLinkCount(header) == 0) {
            Set<SFrame> frames = SessionManager.getSession().getFrames();
            for (SFrame frame : frames) {
                (frame).addHeader(header);
            }
            headerList.add(header);
        }
        incrementLinkCount(header);
    }

    public void deregisterHeaders(List<? extends Header> headers) {
        for (Header header : headers) {
            deregisterHeader(header);
        }
    }

    public void deregisterHeader(Header header) {
        if (header == null)
            throw new IllegalArgumentException("Header must not be null!");

        decrementLinkCount(header);

        if (getLinkCount(header) == 0) {
            Set<SFrame> frames = SessionManager.getSession().getFrames();
            for (SFrame frame : frames) {
                (frame).removeHeader(header);
            }
            headerList.remove(header);
        }
    }

    protected int getLinkCount(Header header) {
        Integer linkCount = linkCounts.get(header);
        if (linkCount == null) {
            return 0;
        } else {
            return linkCount.intValue();
        }
    }

    protected void incrementLinkCount(Header header) {
        linkCounts.put(header, new Integer(getLinkCount(header) + 1));
    }

    protected void decrementLinkCount(Header header) {
        linkCounts.put(header, new Integer(getLinkCount(header) - 1));
    }

}
