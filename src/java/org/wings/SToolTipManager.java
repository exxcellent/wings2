/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings;

import org.wings.session.SessionManager;

/**
 * @author hengels
 * @version $Revision$
 */
public class SToolTipManager
{
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
