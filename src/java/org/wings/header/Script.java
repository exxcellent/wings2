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
package org.wings.header;

import org.wings.Renderable;
import org.wings.SimpleURL;
import org.wings.URLResource;
import org.wings.io.Device;

import java.io.IOException;

/**
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class Script implements Renderable {
    protected String language = null;
    protected String type = null;
    protected URLResource urlSource = null;

    public Script(String language, String type, URLResource urlSource) {
        this.language = language;
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

    public SimpleURL getURL() { return urlSource.getURL(); }

    public void write(Device d)
            throws IOException {
        d.print("<script");
        if (language != null)
            d.print(" language=\"" + language + "\"");
        if (type != null)
            d.print(" type=\"" + type + "\"");

        if (urlSource != null && urlSource.getURL() != null) {
            d.print(" src=\"");
            urlSource.getURL().write(d);
            d.print("\"");
        }
        d.print("></script>");
    }
}


