/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.header;

import org.wings.SimpleURL;
import org.wings.URLResource;
import org.wings.io.Device;
import java.io.IOException;
import java.io.Serializable;

/**
 * Include a <code>&lt;SCRIPT&gt;</code>-element inside the HTML header of rendered page.
 *
 * @author Holger Engels
 */
public class Script implements Header, Serializable {
    protected String language = null;
    protected String type = null;
    protected URLResource urlSource = null;

    /**
     * @deprecated language is deprecated, use Script(type, urlSource) instead
     */
    public Script(String language, String type, URLResource urlSource) {
        this(type,urlSource);
        this.language = language;
    }

    /**
     * creates a script object which can be added to the frame
     * @param type the type of the script
     * @param urlSource the url of the script
     */
    public Script(String type, URLResource urlSource) {
        this.type = type;
        this.urlSource = urlSource;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() { return language; }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() { return type; }

    public SimpleURL getURL() { return urlSource!=null?urlSource.getURL():null; }

    public void write(Device d)
            throws IOException {
        d.print("<script");
        if (type != null)
            d.print(" type=\"" + type + "\"");

        if (language != null)
            d.print(" language=\"" + language + "\"");

        if (urlSource != null && urlSource.getURL() != null) {
            d.print(" src=\"");
            urlSource.getURL().write(d);
            d.print("\"");
        }
        d.print("></script>");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Script))
            return false;

        Script testObj = (Script) obj;

        if (testObj.getLanguage() == null) {
            if (getLanguage() != null) {
                return false;
            }
        } else {
            if (!testObj.getLanguage().equals(getLanguage())) {
                return false;
            }
        }

        if (testObj.getType() == null) {
            if (getType() != null) {
                return false;
            }
        } else {
            if (!testObj.getType().equals(getType())) {
                return false;
            }
        }

        if (testObj.getURL() == null) {
            if (getURL() != null) {
                return false;
            }
        } else {
            if (!testObj.getURL().toString().equals(getURL().toString())) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hashCode = 17;
        int dispersionFactor = 37;

        hashCode = hashCode * dispersionFactor + ((getLanguage() == null) ? 0 : getLanguage().hashCode());
        hashCode = hashCode * dispersionFactor + ((getType() == null) ? 0 : getType().hashCode());
        hashCode = hashCode * dispersionFactor + ((getURL() == null) ? 0 : getURL().hashCode());

        return hashCode;
    }

    public String toString() {
        return urlSource.getURL().toString();
    }

}


