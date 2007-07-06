/*
 * Copyright 2000,2005 wingS development team.
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
package org.wings.plaf.css;


import java.io.IOException;

import org.wings.SSpinner;
import org.wings.SComponent;
import org.wings.SResourceIcon;
import org.wings.SIcon;
import org.wings.io.Device;

public class SpinnerCG extends AbstractComponentCG implements org.wings.plaf.SpinnerCG {

    private static final SResourceIcon DEFAULT_ICON_NEXT = new SResourceIcon("org/wings/icons/SpinnerNext.gif");
    private static final SResourceIcon DEFAULT_ICON_PREV = new SResourceIcon("org/wings/icons/SpinnerPrev.gif");

    private static final long serialVersionUID = 1L;

    private SIcon nextIcon = DEFAULT_ICON_NEXT;
    private SIcon prevIcon = DEFAULT_ICON_PREV;

    public void installCG(SComponent component) {
        super.installCG(component);
    }

    public void uninstallCG(SComponent component) {
    }

    public void writeInternal(final Device device, final SComponent component)
            throws IOException {
        final SSpinner spinner = (SSpinner) component;

        device.print( "\n<table" );
        writeAllAttributes(device, component);
        device.print( "><tr><td>\n" );
        spinner.getEditor().write( device );
        device.print( "\n</td><td style=\"width:0px; font-size: 0px; line-height: 0\">\n" );
        
        device.print( "<img src=\"" + getNextIcon().getURL() + "\" style=\"display:block;vertical-align:bottom;\"");
        Utils.printClickability( device, spinner, "0", true, false );
        device.print( ">\n" );
        
        device.print( "<img src=\"" + getPrevIcon().getURL() + "\" style=\"display:block;vertical-align:bottom;\"");
        Utils.printClickability( device, spinner, "1", true, false);
        device.print( ">\n" );
        
        device.print( "</td></tr></table>\n" );
    }

    public SIcon getNextIcon() {
        return nextIcon;
    }

    public void setNextIcon(SIcon nextIcon) {
        this.nextIcon = nextIcon;
    }

    public SIcon getPrevIcon() {
        return prevIcon;
    }

    public void setPrevIcon(SIcon prevIcon) {
        this.prevIcon = prevIcon;
    }
}
