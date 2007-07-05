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
package org.wings.session;

import java.io.Serializable;

/**
 * Typesafe enumeration class of operating systems on browsers client side.
 * 
 * <p>Example usage of how to get the browser type</p>
 * <code>SessionManager.getSession().getUserAgent().getBrowserType()</code>
 */
public class BrowserType implements Serializable {
    public static class BrowserID implements Serializable {
        public static final int UNKNOWN = 0;
        public static final int GECKO = 1;
        public static final int MOZILLA = 2;
        public static final int IE = 3;
        public static final int OPERA = 4;
        public static final int KONQUEROR = 5;
    }
    /**
     * Unknown browser type
     */
    public static final BrowserType UNKNOWN = new BrowserType(BrowserID.UNKNOWN, "default", "Unknown");

    /**
     * Gecko based browser type.
     */
    public static final BrowserType GECKO = new BrowserType(BrowserID.GECKO, "gecko", "Gecko");

    /**
     * Old mozilla browser type.
     */
    public static final BrowserType MOZILLA = new BrowserType(BrowserID.MOZILLA, "mozilla", "Mozilla (non-Gecko)");

    /**
     * Internet Explorere variant.
     */
    public static final BrowserType IE = new BrowserType(BrowserID.IE, "msie", "Internet Exploder");

    /**
     * Opera browser type on Linux/KDE.
     */
    public static final BrowserType OPERA = new BrowserType(BrowserID.OPERA, "opera", "Opera");

    /**
     * Konqueror browser type on Linux/KDE.
     */
    public static final BrowserType KONQUEROR = new BrowserType(BrowserID.KONQUEROR, "konqueror", "Konqueror");

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
