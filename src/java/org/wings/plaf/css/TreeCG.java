/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;


import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.AbstractComponentCG;
import org.wings.plaf.CGManager;
import org.wings.session.SessionManager;
import org.wings.tree.SDefaultTreeSelectionModel;
import org.wings.tree.STreeCellRenderer;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.IOException;

public class TreeCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.TreeCG {

//--- byte array converted template snippets.
    private final static byte[] __tr_td = "<tr><td".getBytes();
    private final static byte[] __td_tr_table = "></td></tr></table>".getBytes();
    private final static byte[] __img = "<img".getBytes();
    private final static byte[] ___1 = "/>".getBytes();
    private final static byte[] __border_0 = " border=\"0\"".getBytes();
    private final static byte[] __tr_height_1 = "<tr height=\"1\">".getBytes();
    private final static byte[] __nbsp = "&nbsp;".getBytes();
    private final static byte[] __td_1 = "</td>".getBytes();
    private final static byte[] __table_border_0_1 = "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td>".getBytes();
    private final static byte[] __a_href = "<a href=\"".getBytes();
    private final static byte[] ___4 = "-".getBytes();
    private final static byte[] ___5 = "+".getBytes();
    private final static byte[] __a = "</a>".getBytes();
    private final static byte[] __td_tr_table_1 = "</td></tr></table>".getBytes();
    private final static byte[] __td_td_width_10 = "</td><td width=\"100%\"></td></tr>\n".getBytes();

//--- properties of this plaf.
    private SIcon collapseControlIcon;
    private SIcon emptyFillIcon;
    private SIcon expandControlIcon;
    private SIcon hashMark;
    private SIcon leafControlIcon;

    /**
     * Initialize properties from config
     */
    public TreeCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();

        setCollapseControlIcon((SIcon)manager.getObject("TreeCG.collapseControlIcon", SIcon.class));
        setEmptyFillIcon((SIcon)manager.getObject("TreeCG.emptyFillIcon", SIcon.class));
        setExpandControlIcon((SIcon)manager.getObject("TreeCG.expandControlIcon", SIcon.class));
        setHashMark((SIcon)manager.getObject("TreeCG.hashMark", SIcon.class));
        setLeafControlIcon((SIcon)manager.getObject("TreeCG.leafControlIcon", SIcon.class));
    }


    public void installCG(final SComponent comp) {
        super.installCG(comp);
        final STree component = (STree)comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value;
        value = manager.getObject("STree.cellRenderer", STreeCellRenderer.class);
        if (value != null) {
            component.setCellRenderer((STreeCellRenderer)value);
        }
        value = manager.getObject("STree.nodeIndentDepth", Integer.class);
        if (value != null) {
            component.setNodeIndentDepth(((Integer)value).intValue());
        }
    }

//--- code from common area in template.
    private final boolean isLastChild(TreeModel model, TreePath path, int i) {
        Object node = path.getPathComponent(i);
        if (i == 0)
            return true;
        Object parent = path.getPathComponent(i - 1);

        return node.equals(model.getChild(parent, model.getChildCount(parent) - 1));
    }

    private void writeIcon(Device device, SIcon icon, int width, int height) throws IOException {

        device.write(__img);
        org.wings.plaf.Utils.optAttribute(device, "src", icon.getURL());
        org.wings.plaf.Utils.optAttribute(device, "width", width);
        org.wings.plaf.Utils.optAttribute(device, "height", height);
        device.write(___1);
    }

    private void writeIcon(Device device, SIcon icon, boolean nullBorder) throws IOException {
        if (icon == null) return;

        device.write(__img);
        if (nullBorder) {
            device.write(__border_0);
        }
        org.wings.plaf.Utils.optAttribute(device, "src", icon.getURL());
        org.wings.plaf.Utils.optAttribute(device, "width", icon.getIconWidth());
        org.wings.plaf.Utils.optAttribute(device, "height", icon.getIconHeight());

        device.write(___1);
    }

    private void writeTreeNode(STree component, Device device, int row, int depth)
        throws IOException {
        boolean childSelectorWorkaround = !component.getSession().getUserAgent().supportsChildSelector();
        final TreePath path = component.getPathForRow(row);
        final int nodeIndentDepth = component.getNodeIndentDepth();

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

        device.write(__tr_height_1);

        /*
        * fill the indented area.
        */
        for (int i = ((component.isRootVisible()) ? 0 : 1); i < path.getPathCount() - 1; ++i) {
            device.write("<td indent=\"true\"".getBytes());
            org.wings.plaf.Utils.optAttribute(device, "width", nodeIndentDepth);
            if (hashMark != null && !isLastChild(component.getModel(), path, i)) {
                device.write(" style=\"background-image:url(".getBytes());
                org.wings.plaf.Utils.write(device, hashMark.getURL());
                device.write(")\"".getBytes());
            }
            if (childSelectorWorkaround)
                Utils.childSelectorWorkaround(device, "indent");

            device.write(">".getBytes());

            if (emptyFillIcon != null) {
                writeIcon(device, emptyFillIcon, nodeIndentDepth, 1);
            }
            else {
                for (int n = nodeIndentDepth; n > 0; --n) {
                    device.write(__nbsp);
                }
            }

            device.write(__td_1);
        }

        /*
        * now, write the component.
        */
        device.write("<td".getBytes());
        org.wings.plaf.Utils.optAttribute(device, "colspan", depth - (path.getPathCount() - 1));
        if (isSelected) {
            device.print(" selected=\"true\"");

            if (childSelectorWorkaround)
                Utils.childSelectorWorkaround(device, "selected");
        }
        device.write(">".getBytes());

        /*
        * in most applications, the is no need to render a control icon for a
        * leaf. So in that case, we can avoid writing the sourrounding 
        * table, that will speed up things in browsers.
        */
        final boolean renderControlIcon = !(isLeaf && leafControlIcon == null);

        if (renderControlIcon) {

            device.write(__table_border_0_1);
            if (isLeaf) {
                writeIcon(device, leafControlIcon, false);
            }
            else {
                if (component.getShowAsFormComponent()) {
                    device.print("<button type=\"submit\" name=\"");
                    org.wings.plaf.Utils.write(device, Utils.event(component));
                    device.print("\" value=\"");
                    org.wings.plaf.Utils.write(device, component.getExpansionParameter(row, false));
                    device.print("\"");
                }
                else {
                    RequestURL selectionAddr = component.getRequestURL();
                    selectionAddr.addParameter(org.wings.plaf.css.Utils.event(component),
                        component.getExpansionParameter(row, false));

                    device.write("<a href=\"".getBytes());
                    org.wings.plaf.Utils.write(device, selectionAddr.toString());
                    device.write("\"".getBytes());
                }
                device.print("\">");

                if (isExpanded) {
                    if (collapseControlIcon == null) {
                        device.write(___4);
                    }
                    else {
                        writeIcon(device, collapseControlIcon, true);
                    }
                }
                else {
                    if (expandControlIcon == null) {
                        device.write(___5);
                    }
                    else {
                        writeIcon(device, expandControlIcon, true);
                    }
                }
                if (component.getShowAsFormComponent())
                    device.print("</button>");
                else
                    device.print("</a>");
            }

            device.write("</td><td>".getBytes());
        }

        SCellRendererPane rendererPane = component.getCellRendererPane();
        if (isSelectable) {
            if (component.getShowAsFormComponent()) {
                device.print("<button type=\"submit\" name=\"");
                org.wings.plaf.Utils.write(device, Utils.event(component));
                device.print("\" value=\"");
                org.wings.plaf.Utils.write(device, component.getSelectionParameter(row, false));
                device.print("\"");
            }
            else {
                RequestURL selectionAddr = component.getRequestURL();
                selectionAddr.addParameter(org.wings.plaf.css.Utils.event(component),
                    component.getSelectionParameter(row, false));

                device.write("<a href=\"".getBytes());
                org.wings.plaf.Utils.write(device, selectionAddr.toString());
                device.write("\"".getBytes());
            }

            org.wings.plaf.Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
            Utils.writeEvents(device, component);
            device.print(">");

            rendererPane.writeComponent(device, cellComp, component);

            if (component.getShowAsFormComponent())
                device.print("</button>");
            else
                device.print("</a>");
        }
        else {
            rendererPane.writeComponent(device, cellComp, component);
        }

        if (renderControlIcon)
            device.write(__td_tr_table_1);

        device.write(__td_td_width_10);
    }


//--- end code from common area in template.


    public void writeContent(final Device device, final SComponent _c)
        throws IOException {
        final STree component = (STree)_c;

        int start = 0;
        int count = component.getRowCount();

        java.awt.Rectangle viewport = component.getViewportSize();
        if (viewport != null) {
            start = viewport.y;
            count = viewport.height;
        }

        final int depth = component.getMaximumExpandedDepth();

        device.print("<table>");
        for (int i = start; i < count; ++i) {
            writeTreeNode(component, device, i, depth);
        }

        // expandable last row to fit preferred table size on IE
        device.write(__tr_td);
        org.wings.plaf.Utils.optAttribute(device, "colspan", depth);
        device.write(__td_tr_table);
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
