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

/**
 * Allows downloading of the passed Resource implementation.
 *
 * @author armin
 *         created at 24.02.2004 13:05:00
 */
public class SDownloadButton extends SClickable {

    Resource resource;

    public SDownloadButton(Resource pResource) {
        resource = pResource;
        setShowAsFormComponent(false);
    }

    public SDownloadButton(String text, Resource pResource) {
        super(text);
        resource = pResource;
        setShowAsFormComponent(false);
    }

    public SDownloadButton(SIcon icon, Resource pResource) {
        super(icon);
        resource = pResource;
        setShowAsFormComponent(false);
    }

    public boolean isEpochCheckEnabled() {
        return false;
    }

    public SimpleURL getURL() {
        return resource.getURL();
    }


}
