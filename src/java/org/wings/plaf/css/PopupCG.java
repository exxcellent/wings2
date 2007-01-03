/*
 * Copyright 2000,2006 wingS development team.
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.wings.SComponent;
import org.wings.SPopup;
import org.wings.header.SessionHeaders;
import org.wings.io.Device;
import java.io.IOException;
import org.wings.io.StringBuilderDevice;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.script.JavaScriptDOMListener;
import org.wings.script.JavaScriptEvent;
import org.wings.util.SStringBuilder;

/**
 * CG for SPopup instances.
 *
 * @author Christian Schyma
 */
public final class PopupCG extends AbstractComponentCG implements org.wings.plaf.PopupCG {

    private static final long serialVersionUID = 1L;

    protected final List headers = new ArrayList();

    private SPopup popup;

    private String popupJSObject;
    private String dwrJsObject;

    private String showFunction;
    private String hideFunction;

    private JavaScriptDOMListener listener;

    private final String DWR_GETTER = "getRenderedContent";

    public PopupCG(SPopup popup) {
        this.popup = popup;

        headers.add(Utils.createExternalizedJavaScriptHeader("org/wings/js/etc/popup.js"));
        headers.add(Utils.createExternalizedSytleSheetHeader("org/wings/js/yui/container/assets/container.css"));
        SessionHeaders.getInstance().registerHeaders(headers);

        String name        = this.popup.getComponent().getName();
        this.popupJSObject = name + "_popup";
        this.dwrJsObject   = name + "_popup_dwr";
        this.showFunction  = "function() {"+ popupJSObject + ".show()}";
        this.hideFunction  = "function() {"+ popupJSObject + ".hide()}";

        // expose data source to java script by using DWR
        HashSet methodsToExpose = new HashSet();
        methodsToExpose.add(DWR_GETTER);
        CallableManager.getInstance().registerCallable(this.dwrJsObject, this, methodsToExpose);

        attachJavaScript();
    }

    public void attachJavaScript() {
        if (listener != null) {
            popup.getOwner().removeScriptListener(listener);
        }
        listener = new JavaScriptDOMListener(
                JavaScriptEvent.ON_LOAD, generateInitScript(), this.popup.getComponent());
        popup.getOwner().addScriptListener(listener);
    }

    public void tidyUp() {
        SessionHeaders.getInstance().deregisterHeaders(headers);
        CallableManager.getInstance().unregisterCallable(dwrJsObject);
    }

    private String generateInitScript() {
        SStringBuilder code = new SStringBuilder();

        if (this.popup.isAnchored()) {
            code
                    .append(popupJSObject)
                    .append(" = new wingS.Popup(")
                    .append("'").append(popupJSObject).append("', ")
                    .append(this.dwrJsObject).append(".").append(DWR_GETTER).append(", ")
                    .append("0, ")
                    .append("0, ")
                    .append(this.popup.getWidth()).append(", ")
                    .append(this.popup.getHeight()).append(", ")
                    .append("'").append(this.popup.getContext().getName()).append("', ")
                    .append("'").append(this.popup.getContentsCorner()).append("', ")
                    .append("'").append(this.popup.getContextCorner()).append("'")
                    .append(")");
        } else {
            code
                    .append(this.popupJSObject)
                    .append(" = new wingS.Popup(")
                    .append("'").append(popupJSObject).append("', ")
                    .append(this.dwrJsObject).append(".").append(DWR_GETTER).append(", ")
                    .append(this.popup.getX()).append(", ")
                    .append(this.popup.getY()).append(", ")
                    .append(this.popup.getWidth()).append(", ")
                    .append(this.popup.getHeight())
                    .append(")");
        }

        return code.toString();
    }

    /**
     * @return JavaScript function to use a the client side to show the popup
     */
    public String getJsShowFunction() {
        return this.showFunction;
    }

    /**
     * @return JavaScript function to use a the client side to hide the popup
     */
    public String getJsHideFunction() {
        return this.hideFunction;
    }

    /**
     * Returns the rendered HTML code of the contents component.
     */
    public String getRenderedContent() {
        StringBuilderDevice device = new StringBuilderDevice();

        if (this.popup.getComponent() != null) {
            try {
                this.popup.getComponent().write(device);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return device.toString();
    }

    public void writeInternal(Device device, SComponent component) throws IOException {
    }

}
