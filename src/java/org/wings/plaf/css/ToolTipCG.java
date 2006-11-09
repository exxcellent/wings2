/*
 * $Id: TableCG.java 3016 2006-11-08 13:01:10Z stephanschuster $
 * Copyright 2006 wingS development team.
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

package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.SToolTipManager;
import org.wings.util.SStringBuilder;

/** 
 * PLAF renderer for Tooltips. 
 * @author Christian Schyma
 */
final class ToolTipCG {
    
    private ToolTipCG() {
        // nothing to do
    }
    
    /**
     * Generates and returns a JavaScript to initialize the tooltip of the given component.
     * @param component component of tooltip
     * @return JavaScript; returns null when the component has no tooltip
     */
    public static String generateTooltipInitScript(SComponent component) {            
        final String tooltip = component.getToolTipText();           
        if ((tooltip != null) && (tooltip.length() > 0)) {
            SToolTipManager ttManager = SToolTipManager.sharedInstance();            
            final SStringBuilder script = 
                    new SStringBuilder("new YAHOO.widget.Tooltip('");
                    script.append(component.getName())
                    .append("_tooltip', {")
                    .append("context: '").append(component.getName()).append("', ")
                    .append("text: \"").append(tooltip).append("\",")
                    .append("showdelay: ").append(ttManager.getInitialDelay())
                    .append(",").append("autodismissdelay: ").append(ttManager.getDismissDelay())
                    .append("});");                                        
            return script.toString();
        }
        else {
            return null;
        }                
    }
    
}
