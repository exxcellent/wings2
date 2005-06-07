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
package org.wings.plaf.css.msie;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SFrame;
import org.wings.STabbedPane;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.session.SessionManager;
import org.wings.style.CSSSelector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ole
 *
 */
public class TabbedPaneCG extends org.wings.plaf.css.TabbedPaneCG implements SParentFrameListener {
    private static final String FORMS_JS = (String) SessionManager
    .getSession().getCGManager().getObject("JScripts.form",
            String.class);


    /* (non-Javadoc)
     * @see org.wings.plaf.css.TabbedPaneCG#writeButtonStart(org.wings.io.Device, org.wings.STabbedPane, java.lang.String)
     */
    protected void writeButtonStart(Device device, STabbedPane tabbedPane, String value) throws IOException {
        device.print("<button onclick=\"addHiddenField(this.form,'");
        device.print(tabbedPane.getParentFrame().getEventEpoch());
        device.print(SConstants.UID_DIVIDER);
        device.print(SConstants.IEFIX_BUTTONACTION);
        device.print("','");
        device.print(tabbedPane.getName());
        device.print(SConstants.UID_DIVIDER);
        device.print(value);
        device.print("')\"");
    }

    public void installCG(SComponent component) {
        super.installCG(component);
        component.addParentFrameListener(this);
    }

    public void parentFrameAdded(SParentFrameEvent e) {
        SFrame parentFrame = e.getParentFrame();
        ClasspathResource res = new ClasspathResource(FORMS_JS, "text/javascript");
        String jScriptUrl = SessionManager.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        parentFrame.addHeader(new Script("text/javascript", new DefaultURLResource(jScriptUrl)));
    }

    public void parentFrameRemoved(SParentFrameEvent e) {
    }

    /* (non-Javadoc)
     * @see org.wings.plaf.css.TabbedPaneCG#writeButtonEnd(org.wings.io.Device)
     */
    protected void writeButtonEnd(Device device) throws IOException {
        super.writeButtonEnd(device);
        device.print("<span> </span>");
    }

    public CSSSelector  mapSelector(SComponent addressedComponent, CSSSelector selector) {
        CSSSelector mappedSelector = (CSSSelector) msieMappings.get(selector);
        return mappedSelector != null ? mappedSelector : selector;
    }

    private static final Map msieMappings = new HashMap();
    static {
        msieMappings.put(STabbedPane.SELECTOR_SELECTED_TAB, new CSSSelector (" *.STabbedPane_Tab_selected"));
        msieMappings.put(STabbedPane.SELECTOR_UNSELECTED_TAB, new CSSSelector (" *.STabbedPane_Tab_unselected"));
        msieMappings.put(STabbedPane.SELECTOR_CONTENT, new CSSSelector (" td.STabbedPane_pane"));
        msieMappings.put(STabbedPane.SELECTOR_TAB_AREA, new CSSSelector (" table.STabbedPane th"));
    }


}
