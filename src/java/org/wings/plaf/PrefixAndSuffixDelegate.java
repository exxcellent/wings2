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
 */package org.wings.plaf;

import org.wings.SComponent;
import org.wings.io.Device;

import java.io.IOException;
import java.io.Serializable;

/**
 * wingS renders a HTML code in front an behind every component (for alginment, colouring, font style, etc.).
 * Implemntors of this interface do this for special browsers.
 *
 * @author ole
 */
public interface PrefixAndSuffixDelegate extends Serializable {
    public void writePrefix(Device device, SComponent component) throws IOException;
    public void writeSuffix(Device device, SComponent component) throws IOException;
}