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
package org.wings.plaf;

import org.wings.border.SBorder;
import org.wings.io.Device;

import java.io.IOException;
import java.io.Serializable;

public interface BorderCG
        extends Serializable {
    void writePrefix(Device d, SBorder c) throws IOException;

    void writePostfix(Device d, SBorder c) throws IOException;

    String getSpanAttributes(SBorder border);

    void writeSpanAttributes(Device d, SBorder border) throws IOException;
}


