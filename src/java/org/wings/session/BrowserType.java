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
    public static final BrowserType UNKNOWN = new BrowserType(0, "Unknown");

    /**
     * Gecko based browser type.
     */
    public static final BrowserType GECKO = new BrowserType(1, "Gecko");

    /**
     * Old mozilla browser type.
     */
    public static final BrowserType MOZILLA = new BrowserType(2, "Mozilla (non-Gecko)");

    /**
     * Internet Explorere variant.
     */
    public static final BrowserType IE = new BrowserType(3, "Internet Exploder");

    /**
     * Opera browser type on Linux/KDE.
     */
    public static final BrowserType OPERA = new BrowserType(4, "Opera");

    /**
     * Konqueror browser type on Linux/KDE.
     */
    public static final BrowserType KONQUEROR = new BrowserType(5, "Konqueror");

    private int id;
    private String name;

    /** Typesafe enum constructor. */
    private BrowserType(int id, String name) {
        this.id = id;
        this.name = name;
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
    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }
}
