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
package org.wings.plaf.xhtml;

import org.wings.SCardLayout;
import org.wings.SComponent;
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;

import java.io.IOException;

public class CardLayoutCG implements LayoutCG {
    /**
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
            throws IOException {
        SCardLayout layout = (SCardLayout) l;
        SComponent c = layout.getVisibleComponent();

        if (c == null) return;
        /*
if ( Utils.hasSpanAttributes( component ) )
{
                 d.print("<span style=\"");
                Utils.writeSpanAttributes( d, component );
d.print("\">");
             }
*/
        c.write(d);
        /*
        if ( Utils.hasSpanAttributes( component ) )
         {
         	d.print("</span>");
         }

        */
    }
}


