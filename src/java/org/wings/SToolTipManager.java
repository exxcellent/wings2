/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import org.wings.session.SessionManager;

/**
 * @author hengels
 * @version $Revision$
 */
public class SToolTipManager {
    private int initialDelay = 1000;
    private int dismissDelay = 3000;
    private boolean followMouse = true;

    public int getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    public int getDismissDelay() {
        return dismissDelay;
    }

    public void setDismissDelay(int dismissDelay) {
        this.dismissDelay = dismissDelay;
    }

    public boolean isFollowMouse() {
        return followMouse;
    }

    public void setFollowMouse(boolean followMouse) {
        this.followMouse = followMouse;
    }

    static SToolTipManager sharedInstance() {
        return SessionManager.getSession().getToolTipManager();
    }
}
