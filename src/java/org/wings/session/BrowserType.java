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
package org.wings.session;

/**
 * Typesafe enumeration class of operating systems on browsers client side.
 */
public class BrowserType {
    /**
     * Unknown browser type
     */
    public static final BrowserType UNKNOWN = new BrowserType(0, "default", "Unknown");

    /**
     * Gecko based browser type.
     */
    public static final BrowserType GECKO = new BrowserType(1, "gecko", "Gecko");

    /**
     * Old mozilla browser type.
     */
    public static final BrowserType MOZILLA = new BrowserType(2, "mozilla", "Mozilla (non-Gecko)");

    /**
     * Internet Explorere variant.
     */
    public static final BrowserType IE = new BrowserType(3, "msie", "Internet Exploder");

    /**
     * Opera browser type on Linux/KDE.
     */
    public static final BrowserType OPERA = new BrowserType(4, "opera", "Opera");

    /**
     * Konqueror browser type on Linux/KDE.
     */
    public static final BrowserType KONQUEROR = new BrowserType(5, "konqueror", "Konqueror");

    private int id;
    private String description;
    private String shortName;

    /**
     * Typesafe enum constructor.
     */
    private BrowserType(int id, String shortName, String description) {
        this.id = id;
        this.shortName = shortName;
        this.description = description;
    }

    /**
     * @return A unique id for this browser type.
     */
    public int getId() {
        return id;
    }

    /**
     * @return Clear-Text browserName of this browser type
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Short name, used also to assemble i.e. css. file names
     */
    public String getShortName() {
        return shortName;
    }

    public String toString() {
        return getDescription();
    }
}
