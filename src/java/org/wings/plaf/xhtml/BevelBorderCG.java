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

import org.wings.*; import org.wings.border.*;
import org.wings.io.*;
import org.wings.plaf.*;

/**
  * This handles border style for BevelBorderCG and EtchedBorderCG
  * because html-syntax is the same except the border-style
  * @author <a href="mailto:andre@lison.de">Andre Lison</a>
  * @see org.wings.plaf.xhtml.css1.EtchedBorderCG
  * @see #writeBorderStyle(Device,SBorder)
  */
public class BevelBorderCG
    extends DefaultBorderCG
{
  public String getSpanAttributes(SBorder border) {
    StringBuffer sb = new StringBuffer();
    SAbstractBorder b = ( SAbstractBorder ) border;
    java.awt.Insets insets = b.getInsets();
    java.awt.Color color = b.getColor();
    
    /* thickness & type */
    sb.append( "border: ");
    sb.append( b.getThickness() );
    sb.append("px " );
    sb.append(getBorderStyle(border));
    sb.append( ";" );
    
    /* color */
    sb.append( "border-color: #" );
    if ( color != null )
      sb.append( org.wings.plaf.xhtml.Utils.toColorString( b.getColor() ) );
    else
      sb.append( "000000" );
    sb.append( ";" );
    
    /* padding */
    if ( insets != null ) {
      sb.append( "padding-top: " );
      sb.append( insets.top );
      sb.append( "px;padding-right: " );
      sb.append( insets.right );
      sb.append( "px;padding-left: " );
      sb.append( insets.left );
      sb.append( "px;padding-bottom: " );
      sb.append( insets.bottom );
      sb.append( "px;" );
    }
    return sb.toString();
  }
	public void writeSpanAttributes( Device d, SBorder border )
    	throws IOException
     {
       d.print(getSpanAttributes(border));
     }

    protected String getBorderStyle(SBorder b) {
     return ( ( ((SBevelBorder) b).getBevelType() == SBevelBorder.RAISED) ? "outset" : "inset" );
    }
    
	protected void writeBorderStyle(Device d, SBorder b) 
    throws IOException {
      d.print(getBorderStyle(b));
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
