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

import org.wings.RequestURL;
import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.STable;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.resource.ResourceManager;
import org.wings.session.SessionManager;
import org.wings.style.CSSSelector;
import java.io.IOException;

public class TableCG extends org.wings.plaf.css.TableCG implements SParentFrameListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String FORMS_JS = (String) ResourceManager.getObject("JScripts.form", String.class);

    protected void writeButtonStart(Device device, SComponent component, String value) throws IOException {
        device.print("<button class=\"borderless\" onclick=\"sendEvent(event,'");
        device.print(value);
        device.print("','");
        device.print(Utils.event(component));
        device.print("')\"");
    }

    public void installCG(SComponent component) {
        super.installCG(component);
        component.addParentFrameListener(this);
    }

    public void uninstallCG(SComponent component) {
        super.uninstallCG(component);
        component.removeParentFrameListener(this);
    }

    public void parentFrameAdded(SParentFrameEvent e) {
        SFrame parentFrame = e.getParentFrame();
        ClasspathResource res = new ClasspathResource(FORMS_JS, "text/javascript");
        String jScriptUrl = SessionManager.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        parentFrame.addHeader(new Script("text/javascript", new DefaultURLResource(jScriptUrl)));
    }

    public void parentFrameRemoved(SParentFrameEvent e) {
    }

    protected void writeLinkStart(Device device, RequestURL selectionAddr) throws IOException {
        device.print("<a href=\"#\" onclick=\"location.href='");
        Utils.write(device, selectionAddr.toString());
        device.print("';\"");
    }

    protected String getResolvedPseudoSelectorMapping(CSSSelector selector) {
        if (selector != null && selector.equals(STable.SELECTOR_SELECTION))
            // Special case for this one
            return "#compid TR.selected";
        else
            // rest: defualt
            return super.getResolvedPseudoSelectorMapping(selector);
    }
}
