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

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.border.SBorder;
import org.wings.border.STitledBorder;
import org.wings.io.Device;
import org.wings.plaf.BorderCG;

import java.io.IOException;

/**
 * Write a titled border.
 * @author <a href="andre.lison@general-bytes.com">Andre Lison</a>
 * @version $Revision$
 * @see org.wings.border.STitledBorder
 */
public class TitledBorderCG implements BorderCG
{
    protected static class AttrContainer extends SComponent {
        public AttrContainer() {}
    }
    
    /**
     * @see BorderCG#writePrefix(Device, SBorder)
     */
    public void writePrefix(Device d, SBorder c) throws IOException
    {
        STitledBorder b = (STitledBorder) c;
        d.print("<fieldset>");
		if (b.getTitle() != null) {
		    d.print("<legend  class=\"titledbordertitle\" align=\"");
		    switch (b.getTitlePosition()) {
		        case SConstants.LEFT_ALIGN:
		        	d.print("left");
		        	break;
		        case SConstants.RIGHT_ALIGN:
		        	d.print("RIGHT");
		        	break;
		        case SConstants.CENTER_ALIGN:
		        	d.print("center");
		        	break;
		    }
		    d.print("\"");
		    AttrContainer comp = new AttrContainer();
		    comp.setForeground(b.getTitleColor());
		    comp.setFont(b.getTitleFont());
		    if (org.wings.plaf.xhtml.Utils.hasSpanAttributes(comp))
		    {
		        d.print(" style=\"");
		        org.wings.plaf.xhtml.Utils.writeSpanAttributes(d, comp);
		        d.print("\"");
		    }
		    d.print(">");
		    Utils.write(d, b.getTitle());
		    d.print("</legend>");
		}
    }

    /**
     * close fieldset
     * @see BorderCG#writePostfix(Device, SBorder)
     */
    public void writePostfix(Device d, SBorder c) throws IOException
    {
        d.print("</fieldset>");
    }

    public String getSpanAttributes(SBorder border) {
      return "";
    }
    
    /**
     * Does nothing
     * @see BorderCG#writeSpanAttributes(Device, SBorder)
     */
    public void writeSpanAttributes(Device d, SBorder border) throws IOException
    {
    }
}