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
package org.wings;

import org.wings.session.Session;
import org.wings.session.SessionManager;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

/*
 * Diese Klasse ist nur ein Wrapper, um Eingabestroeme von Grafiken mit dem
 * ExternalizeManager mit der richtigen Endung und ohne Umweg einer neuen
 * Codierung (die z.B. keine Transparenz unterstuetzt) uebers WWW zugreifbar zu
 * machen. Zugleich muss diese Klasse aber auch zu der API der Componenten
 * passen, also ein Image bzw. ImageIcon sein. ImageIcon ist einfacher zu
 * benutzen und implementiert schon alles was benoetigt wird...
 */

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public abstract class Resource implements Serializable, URLResource, Renderable {
    private final static Logger logger = Logger.getLogger("org.wings");

    /**
     *
     */
    protected String id;

    protected String extension;

    protected String mimeType;

    protected Collection headers;

    protected Resource(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    protected Resource() {
    }


    public int getLength() {
        return -1;
    }


    public String getExtension() {
        return extension;
    }

    /**
     * Returns the mime type of this resource.
     *
     * @return
     */
    public String getMimeType() {
        return mimeType;
    }

    public void setHeaders(Collection/*<Map.Entry>*/ headers) {
        this.headers = headers;
    }

    public Collection getHeaders() {
        return headers;
    }

    /**
     *
     */
    public String getId() {
        return id;
    }

    public abstract SimpleURL getURL();


    public String toString() {
        return getId();
    }

    public Session getSession() {
        return SessionManager.getSession();
    }

    public static final class HeaderEntry implements Map.Entry {
        final Object key;
        Object value;

        /**
         * Create new entry.
         */
        public HeaderEntry(Object k, Object v) {
            key = k;
            value = v;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object newValue) {
            Object oldValue = value;
            value = newValue;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry e = (Map.Entry) o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                Object v1 = getValue();
                Object v2 = e.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2)))
                    return true;
            }
            return false;
        }

        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^
                    (value == null ? 0 : value.hashCode());
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }

    }

}


