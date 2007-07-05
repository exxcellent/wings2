/*
 * Copyright 2006 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.script;

import org.wings.SComponent;
import org.wings.SPopup;
import org.wings.plaf.css.PopupCG;
import org.wings.util.SStringBuilder;

/**
 * A JavaScript listener to handle SPopup.
 * @author Christian Schyma
 */
public class PopupListener extends JavaScriptDOMListener {
    
    public static final int SHOW = 0;
    public static final int HIDE = 1;
    
    /**
     * This one is needed because SPopup is not nested in the component 
     * hierarchy and would otherwise be collected by the GC.
     */
    private SPopup popup;
    
    /**
     * @param event one of JavaScriptEvent (e.g. JavaScriptEvent.ON_CLICK) to act on
     * @param popup SPopup component
     * @param action PopupListener.SHOW | PopupListener.HIDE
     * @param component the component this listener is associated with
     */
    public PopupListener(String event, SPopup popup, int action, SComponent component) {        
        super(event, "", component);
        this.popup = popup;        
        
        switch (action) {
            case SHOW: 
                setCode(((PopupCG)popup.getCG()).getJsShowFunction());
                break;
            case HIDE:
                setCode(((PopupCG)popup.getCG()).getJsHideFunction());
                break;
            default:
                setCode(((PopupCG)popup.getCG()).getJsHideFunction());
        }       
        
    }
    
}
