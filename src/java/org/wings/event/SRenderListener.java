/*
 * $Id$
 * (c) Copyright 2002 wingS development team.
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
package org.wings.event;

import java.util.EventListener;

/**
 * SRenderListener.java
 *
 *
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
   Revision 1.1  2004/10/04 16:13:16  hengels
   Initial revision

   Revision 1.2  2003/12/10 20:58:58  hzeller
   o some indentation stuff and adding source headers..

   Revision 1.1  2002/11/06 17:00:29  ahaaf
   o add support for render events

*/
