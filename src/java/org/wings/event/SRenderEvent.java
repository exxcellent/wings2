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
package org.wings.event;

import org.wings.SComponent;

import java.util.EventObject;


/**
 * SRenderEvent.java
 * <p/>
 * <p/>
 * Created: Wed Nov  6 10:06:57 2002
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SRenderEvent extends EventObject {


    public SRenderEvent(SComponent source) {
        super(source);
    }

}// SRenderEvent

/*
   $Log$
   Revision 1.4  2004/12/01 07:54:07  hengels
   o wings is not j-wings
   o styles are not lower case (they're derived from the class name)
   o the gecko.css should be modified carefully, because the konqueror.css is following it
   o the css files should be as small as possible

   Revision 1.3  2004/11/24 21:40:20  blueshift
   + commons logging
   + further empty javdoc removal

   Revision 1.2  2004/11/24 18:12:54  blueshift
   TOTAL CLEANUP:
   - removed document me TODOs
   - updated/added java file headers
   - removed emacs stuff
   - removed deprecated methods

   Revision 1.1.1.1  2004/10/04 16:13:16  hengels
   o start development of wings 2

   Revision 1.1  2002/11/06 17:00:29  ahaaf
   o add support for render events

*/
