package org.wings.plaf.css.msie;

import org.wings.*;
import org.wings.border.SAbstractBorder;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;

import java.io.IOException;
import java.awt.*;

public final class ScrollPaneCG extends org.wings.plaf.css.ScrollPaneCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(Device device, SComponent component) throws IOException {
        boolean requiresFillBehaviour = false;
        SDimension preferredSize;
        String height = null;
        preferredSize = component.getPreferredSize();
        if (preferredSize != null) {
            height = preferredSize.getHeight();
            if (height != null)
                requiresFillBehaviour = true;
        }
        if (!requiresFillBehaviour) {
            super.writeInternal(device, component);
            return;
        }

        SScrollPane scrollPane = (SScrollPane) component;
        Scrollable scrollable = scrollPane.getScrollable();
        SScrollPaneLayout layout = (SScrollPaneLayout) scrollPane.getLayout();

        if (!layout.isPaging() && scrollable instanceof SComponent) {
            SComponent center = (SComponent) scrollable;
            Rectangle viewportSizeBackup = scrollable.getViewportSize();
            SDimension preferredSizeBackup = center.getPreferredSize();
            try {
                scrollable.setViewportSize(scrollable.getScrollableViewportSize());
                writeContent(device, component);
            } finally {
                //component.setPreferredSize(center.getPreferredSize());
                scrollable.setViewportSize(viewportSizeBackup);
                center.setPreferredSize(preferredSizeBackup);
            }
        }
        else {
            writeContent(device, component);
        }
    }

    public void writeContent(Device device, SComponent c)
            throws IOException {
        SScrollPane scrollPane = (SScrollPane) c;

        boolean requiresFillBehaviour = false;
        SDimension preferredSize;
        String height = null;
        preferredSize = scrollPane.getPreferredSize();
        if (preferredSize != null) {
            height = preferredSize.getHeight();
            if (height != null)
                requiresFillBehaviour = true;
        }

        if (requiresFillBehaviour) {
            int borderHeight = 0;
            SAbstractBorder border = (SAbstractBorder) scrollPane.getBorder();
            if (border != null) {
                borderHeight += border.getThickness(SConstants.TOP);
                borderHeight += border.getThickness(SConstants.BOTTOM);
            }

            device.print("<table style=\"behavior:url('-org/wings/plaf/css/layout.htc')\" rule=\"fill\"");
            Utils.optAttribute(device, "layoutHeight", height);
            Utils.optAttribute(device, "borderHeight", borderHeight);
            preferredSize.setHeight(null);
        }
        else
            device.print("<table");

        writeAllAttributes(device, scrollPane);
        if (requiresFillBehaviour)
            preferredSize.setHeight(height);

        device.print(">");
        Utils.renderContainer(device, scrollPane);
        device.print("</table>");
    }
}
