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
 */
public class OSType implements Serializable {
    /**
     * Operating system information could not be found.
     */
    public static final OSType UNKNOWN = new OSType(0, "Unknown OS");

    /**
     * Browser os is of type Unix.
     */
    public static final OSType UNIX = new OSType(1, "Unix");

    /**
     * Browser os is of type Windows.
     */
    public static final OSType WINDOWS = new OSType(2, "Windows");

    /**
     * Browser os is of type MacOS
     */
    public static final OSType MACOS = new OSType(3, "Mac OS");

    /**
     * Browser os is of type IBM-os. f.e. os/2
     */
    public static final OSType IBMOS = new OSType(4, "IBM OS/2");

    private int id;
    private String name;

    /** Typesafe enum constructor. */
    private OSType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return A unique id for this operating system.
     */
    public int getId() {
        return id;
    }

    /**
     * @return Clear-Text browserName of this operating system
     */
    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }
}
