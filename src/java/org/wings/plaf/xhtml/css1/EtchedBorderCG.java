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

package org.wings.plaf.xhtml.css1;

import java.io.IOException;

import org.wings.border.SBorder;
import org.wings.border.SEtchedBorder;
import org.wings.io.Device;

/**
 * Write a etched border
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @see DefaultBorderCG#writeBorderStyle
 */
public class EtchedBorderCG
    extends DefaultBorderCG
{

  public String getSpanAttributes( SBorder b )
    {
      StringBuffer sb = new StringBuffer();
	SEtchedBorder border = (SEtchedBorder)b;

        sb.append(getBorderStyle(b, border.getEtchedType()==SEtchedBorder.RAISED ? 
                         "ridge" : "groove"));

        // write insets/padding
        sb.append(super.getSpanAttributes(b));
        return sb.toString();
    }

  public void writeSpanAttributes(Device d, SBorder b )
        throws IOException
    {
        d.print(getSpanAttributes(b));
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
