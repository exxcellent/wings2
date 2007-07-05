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

/**
 * For classes that can be rendered, i.e. can write itself to a Device.
 *
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 */
public interface Renderable {
    /**
     * Write this Renderable component to some output device.
     */
    void write(Device d) throws IOException;
}


