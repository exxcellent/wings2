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

import org.wings.border.SBorder;
import org.wings.border.SLineBorder;
import org.wings.io.Device;

import java.io.IOException;

public class LineBorderCG
    extends DefaultBorderCG
    implements org.wings.plaf.LineBorderCG
{
  public String getSpanAttributes( SBorder border ) {
    StringBuffer sb = new StringBuffer();
    SLineBorder b = ( SLineBorder ) border;
    java.awt.Color  color  = b.getColor();
    java.awt.Insets insets = b.getInsets();
    
    /* thickness & type */
    sb.append( "border: " );
    sb.append( b.getThickness() );
    sb.append( "px solid;" );
    
    /* color */
    sb.append( "border-color: #" );
    if ( color != null )
      sb.append( org.wings.plaf.xhtml.Utils.toColorString( color ) );
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
        throws IOException {
        
        /* thickness & type */
        d.print(getSpanAttributes(border));
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
