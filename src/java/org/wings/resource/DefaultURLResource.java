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
package org.wings.resource;

import org.wings.SimpleURL;
import org.wings.URLResource;

/**
 * Default implementation of an URLResource
 *
 * @author armin
 *         created at 15.01.2004 17:58:29
 */
public class DefaultURLResource implements URLResource {

    private final SimpleURL url;

    public DefaultURLResource(String s) {
        url = new SimpleURL(s);
    }

    public SimpleURL getURL() {
        return url;
    }
}
