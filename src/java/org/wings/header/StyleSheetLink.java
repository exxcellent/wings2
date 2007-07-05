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

import org.wings.URLResource;
import org.wings.resource.DefaultURLResource;

/**
 * Use this to add style sheets to a frame.
 * <code>
 *   frame.addHeader(new StyleSheetLink("../myStyleSheet.css"));
 * </code>
 *
 * @author armin
 *         created at 15.01.2004 17:55:28
 * [bsc] why?: deprecated use StyleSheetHeader instead
 */
public class StyleSheetLink extends Link {

    public StyleSheetLink(URLResource resource) {
        super("stylesheet", null, "text/css", null, resource);
    }

    public StyleSheetLink(String url) {
        this(new DefaultURLResource(url));
    }

}
