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
 * SExitListener.java
 * <p/>
 * <p/>
 * Created: Fri Dec  6 11:08:26 2002
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */

public interface SExitListener extends EventListener {

    public void prepareExit(SExitEvent e) throws ExitVetoException;

}// SExitListener
