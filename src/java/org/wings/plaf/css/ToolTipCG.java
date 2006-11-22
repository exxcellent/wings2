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

import java.util.List;
import org.wings.SComponent;
import org.wings.SToolTipManager;
import org.wings.util.SStringBuilder;

/** 
 * PLAF renderer for Tooltips. 
 * @author Christian Schyma
 */
public final class ToolTipCG {
    
    private ToolTipCG() {
        // nothing to do
    }
       
    /**
     * Generates and returns JavaScript to initialize the tooltip for the given components.
     * @param componentIds
     * @return JavaScript
     */
    public static String generateTooltipInitScript(List componentIds) {        
        SToolTipManager ttManager = SToolTipManager.sharedInstance();
        
        SStringBuilder ids = new SStringBuilder("[");
        for (int i = 0; i < componentIds.size(); i++) {
            ids.append("'").append(componentIds.get(i)).append("',");            
        }
        ids.append("]");        
                        
        final SStringBuilder tooltipsInitScript = new SStringBuilder("new YAHOO.widget.Tooltip('tt',");
        tooltipsInitScript
                .append("{context:").append(ids).append(", ")
                .append("showdelay: ").append(ttManager.getInitialDelay()).append(", ")
                .append("autodismissdelay: ").append(ttManager.getDismissDelay())
                .append("});");
        
        return tooltipsInitScript.toString();                
    }
    
}
