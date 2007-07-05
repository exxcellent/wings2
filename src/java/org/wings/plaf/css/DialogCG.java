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


import org.wings.SComponent;
import org.wings.SDialog;
import org.wings.SIcon;
import org.wings.event.SInternalFrameEvent;
import org.wings.io.Device;
import org.wings.plaf.css.script.OnHeadersLoadedScript;
import org.wings.plaf.css.script.OnPageRenderedScript;
import org.wings.resource.ResourceManager;

import java.io.IOException;
import org.wings.script.JavaScriptDOMListener;
import org.wings.script.JavaScriptEvent;

public class DialogCG extends FormCG implements org.wings.plaf.DialogCG {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected static final String WINDOWICON_CLASSNAME = "WindowIcon";
    protected static final String BUTTONICON_CLASSNAME = "WindowButton";

    private SIcon closeIcon;

    /**
     * Initialize properties from config
     */
    public DialogCG() {
        setCloseIcon((SIcon) ResourceManager.getObject("DialogCG.closeIcon", SIcon.class));
    }

    protected void writeIcon(Device device, SIcon icon, String cssClass) throws IOException {
        device.print("<img");
        if (cssClass != null) {
            device.print(" class=\"");
            device.print(cssClass);
            device.print("\"");
        }
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());
        Utils.attribute(device, "alt", icon.getIconTitle());
        device.print("/>");
    }

    protected void writeWindowIcon(Device device, SDialog frame,
                                   int event, SIcon icon, String cssClass) throws IOException {
        Utils.printButtonStart(device, frame, Integer.toString(event), true, frame.getShowAsFormComponent());
        device.print(">");
        writeIcon(device, icon, null);

        Utils.printButtonEnd(device, true);
    }

    public void writeInternal(final Device device, final SComponent _c) throws IOException {
        SDialog frame = (SDialog) _c;

        String modalId = _c.getName() + "_modal";
        String dialogId = _c.getName() + "_dialog";

        device.print("<div id=\"" + modalId + "\" class=\"modalDialog\" style=\"position:absolute;top:0px;left:0px;bottom:0px;right:0px;width:100%;height:100%;\"></div>");
        device.print("<div id=\"" + dialogId + "\" style=\"z-index:-1;position:absolute;\">");

        writeTablePrefix(device, frame);
        writeWindowBar(device, frame);

        device.print("<div class=\"WindowContent\">");
        super.writeInternal(device, _c);
        device.print("</div>");
        writeTableSuffix(device, frame);

        device.print("</div>");

        String function = "wingS.util.showModalDialog(\"" + dialogId + "\", \"" + modalId + "\");";
        _c.getSession().getScriptManager().addScriptListener(new OnPageRenderedScript(function));
        _c.addScriptListener(new JavaScriptDOMListener(JavaScriptEvent.ON_RESIZE, "function() { " + function + "}", _c));
    }


    protected void writeWindowBar(final Device device, SDialog frame) throws IOException {
        String text = frame.getTitle();
        if (text == null)
            text = "wingS";
        device.print("<div class=\"WindowBar\">");

        // frame is rendered in desktopPane
        // these following icons will be floated to the right by the style sheet...
        if (frame.isClosable() && closeIcon != null) {
            writeWindowIcon(device, frame,
                    SInternalFrameEvent.INTERNAL_FRAME_CLOSED, closeIcon, BUTTONICON_CLASSNAME);
        }
        device.print("<div class=\"WindowBar_title\">");
        // float right end
        if (frame.getIcon() != null) {
            writeIcon(device, frame.getIcon(), WINDOWICON_CLASSNAME);
        }
        device.print(text);
        device.print("</div>");

        device.print("</div>");
    }

    public SIcon getCloseIcon() {
        return closeIcon;
    }

    public void setCloseIcon(SIcon closeIcon) {
        this.closeIcon = closeIcon;
    }
}
