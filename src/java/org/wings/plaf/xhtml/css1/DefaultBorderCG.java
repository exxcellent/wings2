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

import org.wings.border.SBorder;
import org.wings.io.Device;

import java.awt.*;
import java.io.IOException;

public class DefaultBorderCG
    implements org.wings.plaf.BorderCG
{
    protected final static Insets NO_BORDER = new Insets(0,0,0,0);


    public void writePrefix(Device d, SBorder b)
	throws IOException
    {
/*      System.out.println("div prefix defaultborder");
      new Exception().printStackTrace();
	d.print("<div style=\"");

        writeSpanAttributes(d, b);
        
        d.print("\">"); */
    }

    public void writePostfix(Device d, SBorder b)
	throws IOException
    {
/*      System.out.println("div postfix defaultborder");
      new Exception().printStackTrace();
	d.print("</div>"); */
    }

    public String getSpanAttributes( SBorder border ) {
    StringBuffer sb = new StringBuffer();
	Insets insets = border.getInsets();

        if (insets != null && !NO_BORDER.equals(insets)) {
            sb.append("padding:")
              .append(insets.top).append("px ")
              .append(insets.right).append("px ")
              .append(insets.bottom).append("px ")
              .append(insets.left).append("px;");
        }
    return sb.toString();
    }
    
    public void writeSpanAttributes( Device d, SBorder border )
        throws IOException
    {
	  d.print(getSpanAttributes(border));
    }

  protected String getBorderStyle( SBorder border, String style )  
    {
      StringBuffer sb = new StringBuffer();
      
        sb.append("border:")
            .append(style)
            .append(" ")
            .append(border.getThickness())
            .append("px;color: #")
            .append(Utils.toColorString(border.getColor()))
            .append(";");
        return sb.toString();
    }

  protected void writeBorderStyle( Device d, SBorder border, String style ) 
        throws IOException 
    {
        d.print(getBorderStyle(border, style));
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */


