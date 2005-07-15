/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf;

import org.wings.SComponent;
import org.wings.io.Device;

import java.io.IOException;
import java.io.Serializable;

/**
 * wingS renders some HTML code in front and afetr every component (for alginment, colouring, font style, etc.).
 * Implemntors of this interface do this rendering of prefix and suffix (html) code for special browsers.
 *
 * @author ole
 */
public interface PrefixAndSuffixDelegate extends Serializable {
    /**
     * Render prefix code to device
     *
     * @param device    Output device
     * @param component Component to decorate with prefix
     */
    void writePrefix(Device device, SComponent component)
            throws IOException;

    /**
     * Render suffix code to device
     *
     * @param device    Output device
     * @param component Component to decorate with prefix
     */
    void writeSuffix(Device device, SComponent component)
            throws IOException;
}