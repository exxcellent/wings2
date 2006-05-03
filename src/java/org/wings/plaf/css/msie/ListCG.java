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
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.resource.ResourceManager;
import org.wings.session.SessionManager;

import java.io.IOException;

public final class ListCG extends org.wings.plaf.css.ListCG implements SParentFrameListener {
    private static final long serialVersionUID = 1L;
    private static final String FORMS_JS = (String) ResourceManager.getObject("JScripts.form", String.class);


    /* (non-Javadoc)
     * @see org.wings.plaf.css.ListCG#writeLinkStart(org.wings.io.Device, org.wings.RequestURL)
     */
    protected void writeLinkStart(Device device, RequestURL selectionAddr) throws IOException {
        MSIEUtils.writeSubmitAnchorStart(device, selectionAddr);
        /*device.print(" onclick=\"location.href='");
        Utils.write(device, selectionAddr.toString());
        device.print("';\"");*/
    }

    protected void writeButtonStart(Device device, SComponent component, String value) throws IOException {
        MSIEUtils.writeSubmitAnchorStart(device, component, value);
    }

    protected void writeButtonEnd(Device device) throws IOException {
        MSIEUtils.writeSubmitAnchorEnd(device);
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

}
