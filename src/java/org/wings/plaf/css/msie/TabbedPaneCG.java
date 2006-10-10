package org.wings.plaf.css.msie;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.STabbedPane;
import org.wings.SDimension;
import org.wings.border.SBorder;
import org.wings.io.Device;
import org.wings.plaf.css.RenderHelper;
import org.wings.plaf.css.Utils;
import org.wings.util.SStringBuilder;

public final class TabbedPaneCG extends org.wings.plaf.css.TabbedPaneCG {
    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device, final SComponent component)
            throws java.io.IOException {

        SDimension preferredSize = component.getPreferredSize();
        if (preferredSize == null) {
            super.writeInternal(device, component);
            return;
        }
        String height = preferredSize.getHeight();
        if (height == null || "auto".equals(height)) {
            super.writeInternal(device, component);
            return;
        }
        // special implementation with expressions is only required, if the tabbed pane has a
        // given preferred height

        final STabbedPane tabbedPane = (STabbedPane) component;
        if (tabbedPane.getTabCount() > 0) {
            final int placement = tabbedPane.getTabPlacement();

            SStringBuilder tabArea = Utils.inlineStyles(component.getDynamicStyle(STabbedPane.SELECTOR_TABS));
            SStringBuilder contentArea = Utils.inlineStyles(component.getDynamicStyle(STabbedPane.SELECTOR_CONTENT));

            RenderHelper.getInstance(component).addScript("layoutAvailableSpaceIE('" + component.getName() + "');");
            device.print("<table ");
            Utils.optAttribute(device, "layoutHeight", height);

            preferredSize.setHeight(null);
            writeAllAttributes(device, component);
            preferredSize.setHeight(height);

            Utils.writeEvents(device, tabbedPane, null);
            device.print(">");

            if (placement == SConstants.TOP) {
                device.print("<tr><th");
            } else if (placement == SConstants.LEFT) {
                device.print("<tr><th");
            } else if (placement == SConstants.RIGHT) {
                device.print("<tr><td");
                Utils.printTableCellAlignment(device, tabbedPane.getSelectedComponent(), SConstants.LEFT, SConstants.TOP);
            } else if (placement == SConstants.BOTTOM) {
                device.print("<tr yweight=\"100\" oversize=\"2\"");
                device.print("><td");
                Utils.printTableCellAlignment(device, tabbedPane.getSelectedComponent(), SConstants.LEFT, SConstants.TOP);
            }

            if (placement == SConstants.TOP) {
                Utils.optAttribute(device, "class", "STabbedPane_top");
                Utils.optAttribute(device, "style", tabArea);
            } else if (placement == SConstants.LEFT) {
                Utils.optAttribute(device, "class", "STabbedPane_left");
                Utils.optAttribute(device, "style", tabArea);
            } else {
                Utils.optAttribute(device, "class", "STabbedPane_pane");
                Utils.optAttribute(device, "style", contentArea);
            }
            device.print(">");

            if (placement == SConstants.TOP || placement == SConstants.LEFT) {
                writeTabs(device, tabbedPane);
            } else {
                writeSelectedPaneContent(device, tabbedPane);
            }

            if (placement == SConstants.TOP) {
                device.print("</th></tr><tr yweight=\"100\" oversize=\"2\"><td");
                Utils.printTableCellAlignment(device, tabbedPane.getSelectedComponent(), SConstants.LEFT, SConstants.TOP);
                Utils.optAttribute(device, "style", contentArea);
            } else if (placement == SConstants.LEFT) {
                device.print("</th><td");
                Utils.printTableCellAlignment(device, tabbedPane.getSelectedComponent(), SConstants.LEFT, SConstants.TOP);
                Utils.optAttribute(device, "style", contentArea);
            } else if (placement == SConstants.RIGHT) {
                device.print("</td><th");
                Utils.optAttribute(device, "style", tabArea);
            } else if (placement == SConstants.BOTTOM) {
                device.print("</td></tr><tr><th");
                Utils.optAttribute(device, "style", tabArea);
            }

            if (placement == SConstants.RIGHT) {
                Utils.optAttribute(device, "class", "STabbedPane_right");
            } else if (placement == SConstants.BOTTOM) {
                Utils.optAttribute(device, "class", "STabbedPane_bottom");
            } else {
                Utils.optAttribute(device, "class", "STabbedPane_pane");
            }
            device.print(">");

            if (placement == SConstants.TOP || placement == SConstants.LEFT) {
                writeSelectedPaneContent(device, tabbedPane);
                device.print("</td></tr></table>");
            } else {
                writeTabs(device, tabbedPane);
                device.print("</th></tr></table>");
            }
        } else {
            Utils.printDebug(device, "<!-- tabbed pane has no tabs -->");
        }
    }
}
