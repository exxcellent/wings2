package org.wings.header;

import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.session.SessionManager;
import org.wings.util.SessionLocal;

import java.util.*;

public class Headers extends SessionLocal {

    private static final Headers INSTANCE = new Headers();

    private int headerCount = 0;

    private Headers() {}

    public static Headers getInstance() {
        return INSTANCE;
    }

    protected Object initialValue() {
        return new HashMap();
    }

    protected Map getHeaderLinkMap() {
        return (Map) get();
    }

    public void registerHeaderLinks(List headers, SComponent component) {
        for (Iterator i = headers.iterator(); i.hasNext();) {
            registerHeaderLink(i.next(), component);
        }
    }

    public void registerHeaderLink(Object header, SComponent component) {
        if (header == null)
            throw new IllegalArgumentException("Header must not be null!");

        HeaderObject headerObject = new HeaderObject(header);
        Set components = getComponents(headerObject);

        handleFrameReload(component, components);
        components.add(component);

        getHeaderLinkMap().put(headerObject, components);
    }

    public void deregisterHeaderLinks(List headers, SComponent component) {
        for (Iterator i = headers.iterator(); i.hasNext();) {
            deregisterHeaderLink(i.next(), component);
        }
    }

    public void deregisterHeaderLink(Object header, SComponent component) {
        if (header == null)
            throw new IllegalArgumentException("Header must not be null!");

        HeaderObject headerObject = new HeaderObject(header);
        Set components = getComponents(headerObject);

        components.remove(component);
        handleFrameReload(component, components);

        if (components.isEmpty())
            getHeaderLinkMap().remove(headerObject);
        else
            getHeaderLinkMap().put(headerObject, components);
    }

    public boolean isHeaderLinked(Object header) {
        return !getComponents(new HeaderObject(header)).isEmpty();
    }

    public Set getHeaderLinks(Object header) {
        return getComponents(new HeaderObject(header));
    }

    public List getHeaders() {
        List headers = new ArrayList(getHeaderLinkMap().keySet().size());
        for (Iterator i = getSortedHeaderObjects().iterator(); i.hasNext();) {
            headers.add(((HeaderObject) i.next()).header);
        }
        return headers;
    }

    public List getHeaders(SFrame frame) {
        List headers = new ArrayList(getHeaderLinkMap().keySet().size());
        for (Iterator i = getSortedHeaderObjects().iterator(); i.hasNext();) {
            HeaderObject headerObject = (HeaderObject) i.next();
            if (containsLinkToFrame(getComponents(headerObject), frame) ||
                containsLinkToFrame(getComponents(headerObject), null))
                headers.add(headerObject.header);
        }
        return headers;
    }

    private Set getComponents(HeaderObject headerObject) {
        Set links = (Set) getHeaderLinkMap().get(headerObject);
        if (links == null) {
            links = new HashSet(2);
        }
        return links;
    }

    private boolean containsLinkToFrame(Set components, SFrame frame) {
        for (Iterator i = components.iterator(); i.hasNext();) {
            if (((SComponent) i.next()).getParentFrame() == frame)
                return true;
        }
        return false;
    }

    private void handleFrameReload(SComponent component, Set components) {
        SFrame frame = component.getParentFrame();
        if (!containsLinkToFrame(components, frame)) {
            if (frame == null) {
                Set frames = SessionManager.getSession().getFrames();
                for (Iterator i = frames.iterator(); i.hasNext();) {
                    ((SFrame) i.next()).reload();
                }
            } else {
                frame.reload();
            }
        }
    }

    private List getSortedHeaderObjects() {
        List headerObjects = new ArrayList(getHeaderLinkMap().keySet());
        Collections.sort(headerObjects, new Comparator() {
            public int compare(Object object1, Object object2) {
                HeaderObject headerObject1 = (HeaderObject) object1;
                HeaderObject headerObject2 = (HeaderObject) object2;

                if (headerObject1.equals(headerObject2)) return 0;
                else {
                    if (headerObject1.header instanceof Script &&
                        headerObject2.header instanceof Link) return -1;
                    if (headerObject1.header instanceof Link &&
                        headerObject2.header instanceof Script) return 1;
                    if (headerObject1.position < headerObject2.position) return -1;
                    if (headerObject1.position > headerObject2.position) return 1;
                    return 0;
                }
            }
        });
        return headerObjects;
    }

    private final class HeaderObject {

        public Object header;
        public int position;

        public HeaderObject(Object header) {
            this.header = header;
            this.position = headerCount++;
        }

        public boolean equals(Object object) {
            if (object == this)
                return true;
            if (object == null || object.getClass() != this.getClass())
                return false;

            HeaderObject other = (HeaderObject) object;

            return header.equals(other.header);
        }

        public int hashCode() {
            return header.hashCode();
        }

    }

}
