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
package org.wings.template;

import org.wings.SFont;

import java.util.StringTokenizer;

/**
 * TemplateUtil.java
 * <p/>
 * <p/>
 * Created: Tue Aug  6 16:41:22 2002
 *
 * @author (c) mercatis information systems gmbh, 1999-2002
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class TemplateUtil {


    public TemplateUtil() {

    }

    public static final SFont parseFont(String value) {
        StringTokenizer s = new StringTokenizer(value, ",");
        String fontName = s.nextToken();
        String tmpFontType = s.nextToken().toUpperCase().trim();
        int fontType = SFont.PLAIN;
        if (tmpFontType.startsWith("B"))
            fontType = SFont.BOLD;
        else if (tmpFontType.startsWith("I"))
            fontType = SFont.ITALIC;

        int fontSize = 12;
        try {
            fontSize = Integer.parseInt(s.nextToken());
        } catch (Exception e) {}

        return new SFont(fontName, fontType, fontSize);
    }

}// TemplateUtil

/*
   $Log$
   Revision 1.4  2004/12/01 07:54:28  hengels
   o wings is not j-wings
   o styles are not lower case (they're derived from the class name)
   o the gecko.css should be modified carefully, because the konqueror.css is following it
   o the css files should be as small as possible

   Revision 1.3  2004/11/24 21:40:25  blueshift
   + commons logging
   + further empty javdoc removal

   Revision 1.2  2004/11/24 18:13:18  blueshift
   TOTAL CLEANUP:
   - removed document me TODOs
   - updated/added java file headers
   - removed emacs stuff
   - removed deprecated methods

   Revision 1.1.1.1  2004/10/04 16:13:30  hengels
   o start development of wings 2

   Revision 1.1  2002/08/06 16:45:55  ahaaf
   add DefaultPropertyManager using reflection and bean shell scripting support

*/
