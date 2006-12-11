/*
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

import java.util.ArrayList;
import java.util.List;
import org.wings.session.SessionManager;
import java.io.Serializable;

/**
 * Defines the behaviour of component tooltips.
 *
 * @author hengels
 */
public class SToolTipManager implements Serializable {
    private int initialDelay = 1000;
    private int dismissDelay = 3000;
    private final List componentIds = new ArrayList();
    
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
    
    public static SToolTipManager sharedInstance() {
        return SessionManager.getSession().getToolTipManager();
    }
    
    /**
     * If a component has a tooltip, it will be stored in a list which is used
     * by ToolTipCG.generateTooltipInitScript() to initialize the tooltips.
     * @param component component to register if there is a tooltip
     */
    public void registerComponent(SComponent component) {
        String tooltip = component.getToolTipText();               
        if ((tooltip != null) && (tooltip.length() > 0)) {        
            componentIds.add(component.getName());            
        }        
    }
    
    /**
     * @see org.wings.SToolTipManager#registerComponent
     */
    public List getRegisteredComponents() {
        return componentIds;
    }
    
    /**
     * Clear list of registered components.
     * @see org.wings.SToolTipManager#registerComponent
     */
    public void clearRegisteredComponents() {
        componentIds.clear();
    }
}
