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

import java.util.EventListener;

/**
 * SRenderListener.java
 * <p/>
 * <p/>
 * Created: Wed Nov  6 10:17:41 2002
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface SRenderListener extends EventListener {

    public void startRendering(SRenderEvent e);

    public void doneRendering(SRenderEvent e);

}// SRenderListener

/*
   $Log$
   Revision 1.3  2004/12/01 07:54:07  hengels
   o wings is not j-wings
   o styles are not lower case (they're derived from the class name)
   o the gecko.css should be modified carefully, because the konqueror.css is following it
   o the css files should be as small as possible

   Revision 1.2  2004/11/24 18:12:54  blueshift
   TOTAL CLEANUP:
   - removed document me TODOs
   - updated/added java file headers
   - removed emacs stuff
   - removed deprecated methods

   Revision 1.1.1.1  2004/10/04 16:13:16  hengels
   o start development of wings 2

   Revision 1.2  2003/12/10 20:58:58  hzeller
   o some indentation stuff and adding source headers..

   Revision 1.1  2002/11/06 17:00:29  ahaaf
   o add support for render events

*/
