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


import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.plaf.Update;
import org.wings.resource.ResourceManager;
import org.wings.tree.SDefaultTreeSelectionModel;
import org.wings.tree.STreeCellRenderer;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;

public final class TreeCG extends AbstractComponentCG implements org.wings.plaf.TreeCG {
    private static final long serialVersionUID = 1L;
    private SIcon collapseControlIcon;
    private SIcon emptyFillIcon;
    private SIcon expandControlIcon;
    private SIcon hashMark;
    private SIcon leafControlIcon;
    private SResourceIcon lineslash;
    private SResourceIcon lineplus;
    private SResourceIcon lineminus;
    private boolean renderSelectableCells = true;

    /**
     * Initialize properties from config
     */
    public TreeCG() {
        setCollapseControlIcon((SIcon) ResourceManager.getObject("TreeCG.collapseControlIcon", SIcon.class));
        setEmptyFillIcon((SIcon) ResourceManager.getObject("TreeCG.emptyFillIcon", SIcon.class));
        setExpandControlIcon((SIcon) ResourceManager.getObject("TreeCG.expandControlIcon", SIcon.class));
        setHashMark((SIcon) ResourceManager.getObject("TreeCG.hashMark", SIcon.class));
        setLeafControlIcon((SIcon) ResourceManager.getObject("TreeCG.leafControlIcon", SIcon.class));
        lineslash = new SResourceIcon("org/wings/icons/lineslash.gif");
        lineslash.getId();
        lineplus = new SResourceIcon("org/wings/icons/lineplus.gif");
        lineplus.getId();
        lineminus = new SResourceIcon("org/wings/icons/lineminus.gif");
        lineminus.getId();
    }


    public void installCG(final SComponent comp) {
        super.installCG(comp);
        final STree component = (STree) comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value;
        value = manager.getObject("STree.cellRenderer", STreeCellRenderer.class);
        if (value != null) {
            component.setCellRenderer((STreeCellRenderer) value);
        }
        value = manager.getObject("STree.nodeIndentDepth", Integer.class);
        if (value != null) {
            component.setNodeIndentDepth(((Integer) value).intValue());
        }
    }

    public boolean isRenderSelectableCells() {
        return renderSelectableCells;
    }

    public void setRenderSelectableCells(boolean renderSelectableCells) {
        this.renderSelectableCells = renderSelectableCells;
    }

    private void writeIcon(Device device, SIcon icon) throws IOException {
        if (icon == null) {
            return;
        }

        device.print("<img");
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());
        Utils.attribute(device, "alt", icon.getIconTitle());
        device.print("/>");
    }

    private void writeTreeNode(STree component, Device device, int row, int depth)
            throws IOException {
        final TreePath path = component.getPathForRow(row);

        final Object node = path.getLastPathComponent();
        final STreeCellRenderer cellRenderer = component.getCellRenderer();

        TreeModel model = component.getModel();
        final boolean isLeaf = model.isLeaf(node);
        final boolean isExpanded = component.isExpanded(path);
        final boolean isSelected = component.isPathSelected(path);

        final boolean isSelectable = renderSelectableCells && (component.getSelectionModel() != SDefaultTreeSelectionModel.NO_SELECTION_MODEL);

        SComponent cellComp = cellRenderer.getTreeCellRendererComponent(component, node,
                isSelected,
                isExpanded,
                isLeaf, row,
                false);

        device.print("<tr>");

        String[] images = new String[] {
            " class=\"slash\"",
            " class=\"plus\"",
            "",
            " class=\"minus\"",
        };

        Object[] pathElements = path.getPath();
        Object parentElement = component.isRootVisible() ? null : model.getRoot();
        for (int i = component.isRootVisible() ? 0 : 1; i < pathElements.length; i++) {
            Object element = pathElements[i];
            boolean lastElement = i == pathElements.length - 1;
            boolean lastChild = lastChild(model, parentElement, element);
            int imageIndex = (lastElement ? 1 : 0) + (lastChild ? 2 : 0);

            device.print("<td");
            if (lastElement && !isLeaf) {
                final String expansionParameter = component.getExpansionParameter(row, false);
                Utils.printClickability(device, component, expansionParameter, true, component.getShowAsFormComponent());
            }

            device.print(images[imageIndex]);
            Utils.optAttribute(device, "width", component.getNodeIndentDepth());
            device.print(">");

            if (lastElement && !(isLeaf && leafControlIcon == null))
                renderControlIcon(device, isLeaf, isExpanded);

            device.print("</td>");
            parentElement = element;
        }

        /*
         * now, write the component.
         */
        device.print("<td colspan=\"");
        device.print(depth - path.getPathCount() + 1);
        device.print("\"");
        Utils.optAttribute(device, "row", "" + row);

        if (isSelected) {
            Utils.optAttribute(device, "class", "selected");
        } else {
            Utils.optAttribute(device, "class", "norm");
        }
        if (isSelectable) {
            final String selectionParameter = component.getSelectionParameter(row, false);
            Utils.printClickability(device, component, selectionParameter, true, component.getShowAsFormComponent());
            Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        }
        device.print(">");

        SCellRendererPane rendererPane = component.getCellRendererPane();
        rendererPane.writeComponent(device, cellComp, component);

        device.print("</td></tr>");
        Utils.printNewline(device, component);
    }

    private void renderControlIcon(Device device, boolean leaf, boolean expanded) throws IOException {
        if (leaf) {
            writeIcon(device, leafControlIcon);
        }
        else if (expanded) {
            if (collapseControlIcon == null) {
                device.print("-");
            }
            else {
                writeIcon(device, collapseControlIcon);
            }
        }
        else {
            if (expandControlIcon == null) {
                device.print("+");
            }
            else {
                writeIcon(device, expandControlIcon);
            }
        }
    }

    private boolean lastChild(TreeModel model, Object parentElement, Object element) {
        return parentElement == null || model.getIndexOfChild(parentElement, element) == model.getChildCount(parentElement) - 1;
    }

    public void writeInternal(final Device device, final SComponent _c) throws IOException {
        RenderHelper.getInstance(_c).forbidCaching();

        final STree tree = (STree) _c;

        Rectangle currentViewport = tree.getViewportSize();
        Rectangle maximalViewport = tree.getScrollableViewportSize();
        int start = 0;
        int end = tree.getRowCount();
        int empty = maximalViewport != null ? maximalViewport.height : end;

        if (currentViewport != null) {
            start = currentViewport.y;
            end = start + currentViewport.height;
        }

        final int depth = tree.getMaximumExpandedDepth();

        device.print("<table");
        writeAllAttributes(device, tree);
        device.print(">");

        for (int i = start; i < end; ++i) {
            if (i >= empty) {
                device.print("<tr><td class=\"empty\">&nbsp;</td></tr>");
                continue;
            }
            writeTreeNode(tree, device, i, depth);
        }

        device.print("</table>");

        RenderHelper.getInstance(_c).allowCaching();
    }

    //--- setters and getters for the properties.

    public SIcon getCollapseControlIcon() {
        return collapseControlIcon;
    }

    public void setCollapseControlIcon(SIcon collapseControlIcon) {
        this.collapseControlIcon = collapseControlIcon;
    }

    public SIcon getEmptyFillIcon() {
        return emptyFillIcon;
    }

    public void setEmptyFillIcon(SIcon emptyFillIcon) {
        this.emptyFillIcon = emptyFillIcon;
    }

    public SIcon getExpandControlIcon() {
        return expandControlIcon;
    }

    public void setExpandControlIcon(SIcon expandControlIcon) {
        this.expandControlIcon = expandControlIcon;
    }

    public SIcon getHashMark() {
        return hashMark;
    }

    public void setHashMark(SIcon hashMark) {
        this.hashMark = hashMark;
    }

    public SIcon getLeafControlIcon() {
        return leafControlIcon;
    }

    public void setLeafControlIcon(SIcon leafControlIcon) {
        this.leafControlIcon = leafControlIcon;
    }

    public Update getSelectionUpdate(STree tree, List deselectedRows, List selectedRows) {
        return new SelectionUpdate(tree, deselectedRows, selectedRows);
    }

    protected class SelectionUpdate extends AbstractUpdate {

        private List deselectedRows;
        private List selectedRows;

        public SelectionUpdate(SComponent component, List deselectedRows, List selectedRows) {
            super(component);
            this.deselectedRows = deselectedRows;
            this.selectedRows = selectedRows;
        }

        public Handler getHandler() {
            UpdateHandler handler = new UpdateHandler("selectionTree");
            handler.addParameter(component.getName());
            handler.addParameter(Utils.listToJsArray(deselectedRows));
            handler.addParameter(Utils.listToJsArray(selectedRows));
            return handler;
        }

    }

}
