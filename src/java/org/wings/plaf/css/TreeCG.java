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
package org.wings.plaf.css;


import org.wings.SCellRendererPane;
import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.STree;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.resource.ResourceManager;
import org.wings.tree.SDefaultTreeSelectionModel;
import org.wings.tree.STreeCellRenderer;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.IOException;

public final class TreeCG extends AbstractComponentCG implements
        org.wings.plaf.TreeCG {
    private static final long serialVersionUID = 1L;
    private SIcon collapseControlIcon;
    private SIcon emptyFillIcon;
    private SIcon expandControlIcon;
    private SIcon hashMark;
    private SIcon leafControlIcon;

    /**
     * Initialize properties from config
     */
    public TreeCG() {
        setCollapseControlIcon((SIcon) ResourceManager.getObject("TreeCG.collapseControlIcon", SIcon.class));
        setEmptyFillIcon((SIcon) ResourceManager.getObject("TreeCG.emptyFillIcon", SIcon.class));
        setExpandControlIcon((SIcon) ResourceManager.getObject("TreeCG.expandControlIcon", SIcon.class));
        setHashMark((SIcon) ResourceManager.getObject("TreeCG.hashMark", SIcon.class));
        setLeafControlIcon((SIcon) ResourceManager.getObject("TreeCG.leafControlIcon", SIcon.class));
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

    private boolean isLastChild(TreeModel model, TreePath path, int i) {
        if (i == 0) {
            return true;
        }
        Object node = path.getPathComponent(i);
        Object parent = path.getPathComponent(i - 1);
        return node.equals(model.getChild(parent, model.getChildCount(parent) - 1));
    }

    private void writeIcon(Device device, SIcon icon, boolean nullBorder) throws IOException {
        if (icon == null) {
            return;
        }

        device.print("<img");
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());
        device.print(" alt=\"");
        device.print(icon.getIconTitle());
        device.print("\"/>");
    }

    private void writeTreeNode(STree component, Device device, int row, int depth)
            throws IOException {
        final TreePath path = component.getPathForRow(row);

        final Object node = path.getLastPathComponent();
        final STreeCellRenderer cellRenderer = component.getCellRenderer();

        final boolean isLeaf = component.getModel().isLeaf(node);
        final boolean isExpanded = component.isExpanded(path);
        final boolean isSelected = component.isPathSelected(path);

        final boolean isSelectable = (component.getSelectionModel() != SDefaultTreeSelectionModel.NO_SELECTION_MODEL);

        SComponent cellComp = cellRenderer.getTreeCellRendererComponent(component, node,
                isSelected,
                isExpanded,
                isLeaf, row,
                false);

        /*
         * now, write the component.
         */
        device.print("<li");
        if (isSelected) {
            Utils.optAttribute(device, "class", "selected");
        } else {
            Utils.optAttribute(device, "class", "norm");
        }
        device.print(">");

        /*
        * in most applications, the is no need to render a control icon for a
        * leaf. So in that case, we can avoid writing the sourrounding
        * table, that will speed up things in browsers.
        */
        final boolean renderControlIcon = !(isLeaf && leafControlIcon == null);

        if (renderControlIcon) {
            /*
             * This table has to be here so that block level elements can be
             * nodes. I just can't think around it. So it won...
             */
            device.print("<table");
            if (isSelected) {
                Utils.optAttribute(device, "class", "selected");
            } else {
                Utils.optAttribute(device, "class", "norm");
            }
            device.print("><tr><td>");

            final String expansionParameter = component.getExpansionParameter(row, false);
            if (isLeaf) {
                // render a disabled button around this. firefox position bugfix (ol)
                Utils.printButtonStart(device, component, expansionParameter, true, component.getShowAsFormComponent());
                device.print(">");
                writeIcon(device, leafControlIcon, false);
                Utils.printButtonEnd(device, component, expansionParameter, true);
            } else {
                Utils.printButtonStart(device, component, expansionParameter, true, component.getShowAsFormComponent());
                device.print(">");

                if (isExpanded) {
                    if (collapseControlIcon == null) {
                        device.print("-");
                    } else {
                        writeIcon(device, collapseControlIcon, true);
                    }
                } else {
                    if (expandControlIcon == null) {
                        device.print("+");
                    } else {
                        writeIcon(device, expandControlIcon, true);
                    }
                }
                Utils.printButtonEnd(device, component, expansionParameter, true);
            }
            /*
             * closing layout td
             */
            device.print("</td><td");
            if (isSelected) {
                Utils.optAttribute(device, "class", "selected");
            } else {
                Utils.optAttribute(device, "class", "norm");
            }
            device.print(">");

        }

        SCellRendererPane rendererPane = component.getCellRendererPane();
        if (isSelectable) {
            final String selectionParameter = component.getSelectionParameter(row, false);
            Utils.printButtonStart(device, component, selectionParameter, true, component.getShowAsFormComponent());

            Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
            device.print(">");

            rendererPane.writeComponent(device, cellComp, component);

            Utils.printButtonEnd(device, component, selectionParameter, true);
        } else {
            rendererPane.writeComponent(device, cellComp, component);
        }

        if (renderControlIcon) {
            /*
             * we have to close the table
             */
            device.print("</td></tr></table>");
        }

        //handle the depth level of the tree
        int nextPathCount = 1;
        int pathCount = path.getPathCount();
        TreePath nextPath = component.getPathForRow(row + 1);
        // is there a next element? else use initialized level.
        if (nextPath != null) {
            nextPathCount = nextPath.getPathCount();
        }
        if (pathCount < nextPathCount) {
            indentOnce(device, component, path);
        } else if (pathCount > nextPathCount) {
            device.print("</li>");
            for (int i = nextPathCount; i < pathCount; i++) {
                device.print("</ul></div></li>");
            }
        } else if (path.getPathCount() == nextPathCount) {
            device.print("</li>");
        }
        Utils.printNewline(device, component);
    }

    public void writeInternal(final Device device, final SComponent _c)
            throws IOException
    {
        RenderHelper.getInstance(_c).forbidCaching();

        //try {             try finally are expensive. Rerender once after ex not
            final STree component = (STree) _c;
            int start = 0;
            int count = component.getRowCount();

            java.awt.Rectangle viewport = component.getViewportSize();
            if (viewport != null) {
                start = viewport.y;
                count = viewport.height;
            }

            final int depth = component.getMaximumExpandedDepth();

            writeTablePrefix(device, component);
            device.print("<ul class=\"STree\">");
            if (start != 0) {
                TreePath path = component.getPathForRow(start);
                indentRecursive(device, component, path.getParentPath());
            }

            for (int i = start; i < start + count; ++i) {
                writeTreeNode(component, device, i, depth);
            }
            device.print("</ul>");
            writeTableSuffix(device, component);
        //}
        //finally {
        RenderHelper.getInstance(_c).allowCaching();
        //}
    }

    /**
     * Recursively indents the Tree in case it isn't displayed from the root
     * node. reversely traverses the {@link TreePath} and renders afterwards.
     */
    private void indentRecursive(Device device, STree component, TreePath path) throws IOException {
        if (path == null) {
            return;
        }
        indentRecursive(device, component, path.getParentPath());
        device.print("<li>");
        indentOnce(device, component, path);
    }


    /**
     * Helper method for code reuse
     */
    private void indentOnce(Device device, STree component, TreePath path) throws IOException {
        device.print("<div");
        if (!isLastChild(component.getModel(), path, path.getPathCount() - 1)) {
            device.print(" class=\"SSubTree\"");
        }
        device.print("><ul class=\"STree\"");
        device.print(" style=\"margin-left:");
        device.print(component.getNodeIndentDepth());
        device.print("px;\">");
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

}
