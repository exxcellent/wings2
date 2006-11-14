/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.header;

import org.wings.Renderable;
import org.wings.SimpleURL;
import org.wings.URLResource;
import org.wings.io.Device;

import java.io.IOException;
import java.io.Serializable;

/**
 * Include a <code>&lt;LINK&gt;</code>-element inside the HTML header of rendered page.
 *
 * <p>Example usage to include a customer stylesheet.<br/>
 * <code>frame.addHeader(new Link("stylesheet", null, "text/css", null, new DefaultURLResource("../css/appstyles.css")));</code>
 *
 * @author Holger Engels
 */
public class Link implements Renderable, Serializable {
    protected String rel = null;
    protected String rev = null;
    protected String type = null;
    protected String target = null;
    protected URLResource urlSource = null;

    /**
     *  Example usage to include a customer stylesheet.<br/>
     * <code>frame.addHeader(new Link("stylesheet", null, "text/css", null, new DefaultURLResource("../css/appstyles.css")));</code>
     * @param rel
     * @param rev
     * @param type
     * @param target
     * @param urlSource
     */
    public Link(String rel, String rev, String type, String target, URLResource urlSource) {
        this.rel = rel;
        this.rev = rev;
        this.type = type;
        this.target = target;
        this.urlSource = urlSource;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getRel() { return rel; }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getRev() { return rev; }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() { return type; }

    public SimpleURL getURL() { return urlSource.getURL(); }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() { return target; }

    public void write(Device d) throws IOException {
        d.print("<link");
        if (rel != null)
            d.print(" rel=\"" + rel + "\"");
        if (rev != null)
            d.print(" rev=\"" + rev + "\"");
        if (type != null)
            d.print(" type=\"" + type + "\"");
        if (target != null)
            d.print(" target=\"" + target + "\"");

        if (urlSource != null && urlSource.getURL() != null) {
            d.print(" href=\"");
            urlSource.getURL().write(d);
            d.print("\"");
        }
        d.print("/>");
    }
}


