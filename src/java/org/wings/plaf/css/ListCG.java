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


import org.wings.SCellRendererPane;
import org.wings.SComponent;
import org.wings.SDefaultListCellRenderer;
import org.wings.SList;
import org.wings.SListCellRenderer;
import org.wings.util.SStringBuilder;
import org.wings.io.Device;
import org.wings.io.StringBuilderDevice;
import org.wings.plaf.CGManager;
import org.wings.plaf.Update;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;

public final class ListCG extends AbstractComponentCG implements  org.wings.plaf.ListCG {

    private static final long serialVersionUID = 1L;

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        final SList component = (SList) comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value;
        value = manager.getObject("SList.cellRenderer", SDefaultListCellRenderer.class);
        if (value != null) {
            component.setCellRenderer((SDefaultListCellRenderer) value);
        }
    }

    protected void writeFormList(final Device device, final SList list) throws IOException {
        Object clientProperty = list.getClientProperty("onChangeSubmitListener");
        // If the application developer attached any ListSelectionListeners to this
        // SList, the surrounding form gets submitted as soon as the state / the
        // selection of this SList changed.
        if (list.getListSelectionListeners().length > 0) {
            if (clientProperty == null) {
                String event = JavaScriptEvent.ON_CHANGE;
            	String code = "wingS.request.sendEvent(event, true, " + !list.isReloadForced() + ");";
                JavaScriptListener javaScriptListener = new JavaScriptListener(event, code);
                list.addScriptListener(javaScriptListener);
                list.putClientProperty("onChangeSubmitListener", javaScriptListener);
            }
        } else if (clientProperty != null && clientProperty instanceof JavaScriptListener) {
            list.removeScriptListener((JavaScriptListener) clientProperty);
            list.putClientProperty("onChangeSubmitListener", null);
        }

        device.print("<span");
        SStringBuilder builder = new SStringBuilder();
        Utils.appendCSSInlineSize(builder, list);
        if (builder.length() != 0) {
            device.print(" style=\"");
            device.print(builder.toString());
            device.print("\"");
        }
        device.print("><select wrapping=\"1\"");
        writeAllAttributes(device, list);

        Utils.optAttribute(device, "name", Utils.event(list));
        Utils.optAttribute(device, "tabindex", list.getFocusTraversalIndex());
        Utils.optAttribute(device, "size", list.getVisibleRowCount());
        Utils.optAttribute(device, "multiple", (list.getSelectionMode() == SList.MULTIPLE_SELECTION) ? "multiple" : null);
        Utils.writeEvents(device, list, null);

        if (!list.isEnabled())
            device.print(" disabled=\"true\"");
        if (list.isFocusOwner())
            Utils.optAttribute(device, "foc", list.getName());

        device.print(">");

        final javax.swing.ListModel model = list.getModel();
        final int size = model.getSize();
        final SListCellRenderer cellRenderer = list.getCellRenderer();

        for (int i = 0; i < size; i++) {
            SComponent renderer = null;
            if (cellRenderer != null) {
                renderer = cellRenderer.getListCellRendererComponent(list, model.getElementAt(i), false, i);
            }

            Utils.printNewline(device, list, 1);
            device.print("<option");
            Utils.optAttribute(device, "value", list.getSelectionParameter(i));
            if (list.isSelectedIndex(i)) {
                device.print(" selected");
                //Utils.optAttribute(device, "class", "selected");
            }

            if (renderer != null) {
                Utils.optAttribute(device, "style", Utils.generateCSSComponentInlineStyle(renderer));
            }
            device.print(">");

            if (renderer != null) {
                String tooltipText = renderer.getToolTipText();
                renderer.setToolTipText(null);

                // Hack: remove all tags, because in form selections, looks ugly.
                StringBuilderDevice string = getStringBuilderDevice();
                renderer.write(string);
                char[] chars = string.toString().toCharArray();
                int pos = 0;
                for (int c = 0; c < chars.length; c++) {
                    switch (chars[c]) {
                        case '<':
                            device.print(chars, pos, c - pos);
                            break;
                        case '>':
                            pos = c + 1;
                    }
                }
                device.print(chars, pos, chars.length - pos);

                renderer.setToolTipText( tooltipText );
            } else {
                device.print(model.getElementAt(i).toString());
            }

            device.print("</option>");
        }

        Utils.printNewline(device, list);
        device.print("</select>");
        Utils.printNewline(device, list);
        device.print("<input type=\"hidden\"");
        Utils.optAttribute(device, "name", Utils.event(list));
        Utils.optAttribute(device, "value", -1);
        device.print("/></span>");
    }

    private StringBuilderDevice stringBufferDevice = null;

    protected org.wings.io.StringBuilderDevice getStringBuilderDevice() {
        if (stringBufferDevice == null) {
            stringBufferDevice = new StringBuilderDevice();
        }
        stringBufferDevice.reset();
        return stringBufferDevice;
    }

    public void writeAnchorList(Device device, SList list) throws IOException {
        // Remove superfluous 'onChangeSubmitListener' (in case there is any).
        // This is because we don't want to render 'onclick' AND 'onchange'.
        Object clientProperty = list.getClientProperty("onChangeSubmitListener");
        if (clientProperty != null && clientProperty instanceof JavaScriptListener) {
            list.removeScriptListener((JavaScriptListener) clientProperty);
            list.putClientProperty("onChangeSubmitListener", null);
        }

        boolean renderSelection = list.getSelectionMode() != SList.NO_SELECTION;

        device.print("<");
        device.print(list.getType());
        Utils.optAttribute(device, "type", list.getOrderType());
        Utils.optAttribute(device, "start", list.getStart());
        writeAllAttributes(device, list);
        device.print(">");

        javax.swing.ListModel model = list.getModel();
        SListCellRenderer cellRenderer = list.getCellRenderer();
        SCellRendererPane rendererPane = list.getCellRendererPane();

        Rectangle currentViewport = list.getViewportSize();
        Rectangle maximalViewport = list.getScrollableViewportSize();
        int start = 0;
        int end = model.getSize();
        int empty = maximalViewport != null ? maximalViewport.height : end;

        if (currentViewport != null) {
            start = currentViewport.y;
            end = start + currentViewport.height;
        }

        for (int i = start; i < end; ++i) {
            if (i >= empty) {
                device.print("<li class=\"empty\">&nbsp;</li>");
                continue;
            }

            boolean selected = list.isSelectedIndex(i);

            if (renderSelection && selected)
                device.print("<li class=\"selected clickable\"");
            else
                device.print("<li class=\"clickable\"");

            SComponent renderer = cellRenderer.getListCellRendererComponent(list, model.getElementAt(i), selected, i);

            if (renderSelection) {
                Utils.printClickability(device, list, list.getToggleSelectionParameter(i), true, list.getShowAsFormComponent());
                Utils.optAttribute(device, "foc", renderer.getName());
            }
            device.print(">");
            rendererPane.writeComponent(device, renderer, list);
            device.print("</li>\n");
        }

        device.print("</");
        Utils.write(device, list.getType());
        device.print(">");
    }

    public void writeInternal(final Device device, final SComponent _c) throws IOException {
        RenderHelper.getInstance(_c).forbidCaching();

        SList list = (SList) _c;
        if (list.getShowAsFormComponent()) {
            writeFormList(device, list);
        } else {
            writeAnchorList(device, list);
        }

        RenderHelper.getInstance(_c).allowCaching();
    }

    public Update getSelectionUpdate(SList list, List deselectedIndices, List selectedIndices) {
        return new SelectionUpdate(list, deselectedIndices, selectedIndices);
    }

    protected class SelectionUpdate extends AbstractUpdate {

        private List deselectedIndices;
        private List selectedIndices;

        public SelectionUpdate(SComponent component, List deselectedIndices, List selectedIndices) {
            super(component);
            this.deselectedIndices = deselectedIndices;
            this.selectedIndices = selectedIndices;
        }

        public Handler getHandler() {
            UpdateHandler handler = new UpdateHandler("selectionList");
            handler.addParameter(component.getName());
            handler.addParameter(Utils.listToJsArray(deselectedIndices));
            handler.addParameter(Utils.listToJsArray(selectedIndices));
            return handler;
        }

    }
}
