/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.plaf.xhtml;

import java.io.IOException;
import java.util.*;

import org.wings.*;
import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class CardLayoutCG implements LayoutCG {
    /**
     * TODO: documentation
     *
     * @param d the device to write the code to
     * @param l the layout manager
     * @throws IOException
     */
    public void write(Device d, SLayoutManager l)
        throws IOException {
        SCardLayout layout = (SCardLayout)l;
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

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
