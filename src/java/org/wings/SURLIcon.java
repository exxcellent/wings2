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
package org.wings;

import java.net.URL;

/**
 * SIcon which is backed by an URL to get the image.
 *
 * @author <a href="mailto:armin@hyperion.intranet.mercatis.de">Armin Haaf</a>
 */
public class SURLIcon extends SAbstractIcon {

    protected SimpleURL url;

    public SURLIcon(URL u) {
        this(u.toString());
    }

    public SURLIcon(SimpleURL u) {
        url = u;
    }

    public SURLIcon(String u) {
        this(new SimpleURL(u));
    }

    public SURLIcon(String u, int width, int height) {
        this(u);

        setIconWidth(width);
        setIconHeight(height);
    }

    public SimpleURL getURL() {
        return url;
    }

    public String toString() {
        return url.toString();
    }

}// SURLIcon


