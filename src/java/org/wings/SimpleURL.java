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

import org.wings.io.Device;

import java.io.IOException;
import java.io.Serializable;

/**
 * Simple URL representation.
 *
 */
public class SimpleURL implements Serializable, Renderable {
    protected String baseURL;

    protected SimpleURL() {}

    public SimpleURL(String url) {
        if (url == null)
            throw new IllegalArgumentException("null not allowed");
        baseURL = url;
    }

    public void write(Device d) throws IOException {
        if (baseURL != null) {
            d.print(baseURL);
        }
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        SimpleURL other = (SimpleURL) o;
        return (baseURL == other.baseURL
                || (baseURL != null && baseURL.equals(other.baseURL)));
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return baseURL != null ? baseURL.hashCode() : 0;
    }

    public String toString() {
        return baseURL;
    }
}


