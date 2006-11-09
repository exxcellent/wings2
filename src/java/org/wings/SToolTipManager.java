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
package org.wings;

import org.wings.session.SessionManager;
import java.io.Serializable;

/**
 * Defines the behaviour of component tooltips.
 *
 * @author hengels
 * @version $Revision$
 */
public class SToolTipManager implements Serializable {
    private int initialDelay = 1000;
    private int dismissDelay = 3000;
    private boolean followMouse = false;

    /**
     * @return The initial delay in ms the mouse pointer has to rest over a component
     * before it's tooltip is shown
     */
    public int getInitialDelay() {
        return initialDelay;
    }

    /**
     * @param initialDelay The initial delay in ms the mouse pointer has to rest over a component
     * before it's tooltip is shown
     */
    public void setInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    /**
     * @return The delay in ms before a tooltip is hidden automatically
     */
    public int getDismissDelay() {
        return dismissDelay;
    }

    /**
     * @param dismissDelay The delay in ms before a tooltip is hidden automatically
     */
    public void setDismissDelay(int dismissDelay) {
        this.dismissDelay = dismissDelay;
    }

    /**
     * @return <code>true</code> if the tooltip popup should follow the mouse movements.
     * @deprecated no function (reason: JavaScript library was changed from domTT to Yahoo YUI, 
     * which does not have this functionality; also even Swing does not support it)
     */
    public boolean isFollowMouse() {
        return followMouse;
    }

    /**
     * @param followMouse <code>true</code> if the tooltip popup should follow the mouse movements.
     * @deprecated no function (reason: JavaScript library was changed from domTT to Yahoo YUI, 
     * which does not have this functionality; also even Swing does not support it)
     */
    public void setFollowMouse(boolean followMouse) {
        this.followMouse = followMouse;
    }
    
    public static SToolTipManager sharedInstance() {
        return SessionManager.getSession().getToolTipManager();
    }
}
