package org.wings.plaf.css.msie;

import org.wings.*;
import org.wings.border.SAbstractBorder;
import org.wings.io.Device;
import org.wings.plaf.css.RenderHelper;
import org.wings.plaf.css.Utils;

import java.io.IOException;

public final class ScrollPaneCG extends org.wings.plaf.css.ScrollPaneCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(Device device, SComponent component) throws IOException {
        SScrollPane scrollpane = (SScrollPane) component;

        boolean requiresFillBehaviour = false;
        SDimension preferredSize = scrollpane.getPreferredSize();
        if (preferredSize != null) {
            String height = preferredSize.getHeight();
            if (height != null) requiresFillBehaviour = true;
        }
        if (!requiresFillBehaviour) {
            super.writeInternal(device, component);
            return;
        }

        if (scrollpane.getMode() == SScrollPane.MODE_COMPLETE) {
            if (preferredSize == null) {
                scrollpane.setPreferredSize(new SDimension(200, 400));
            } else {
                if (preferredSize.getWidthInt() < 0) preferredSize.setWidth(200);
                if (preferredSize.getHeightInt() < 0) preferredSize.setHeight(400);
            }

            writeContent(device, component);
        } else {
            writeContent(device, component);
        }
    }

    public void writeContent(Device device, SComponent c) throws IOException {
        SScrollPane scrollpane = (SScrollPane) c;

        boolean requiresFillBehaviour = false;
        SDimension preferredSize = scrollpane.getPreferredSize();
        String height = null;
        if (preferredSize != null) {
            height = preferredSize.getHeight();
            if (height != null) requiresFillBehaviour = true;
        }

        if (requiresFillBehaviour) {
            int borderHeight = 0;
            SAbstractBorder border = (SAbstractBorder) scrollpane.getBorder();
            if (border != null) {
                borderHeight += border.getThickness(SConstants.TOP);
                borderHeight += border.getThickness(SConstants.BOTTOM);
            }

            RenderHelper.getInstance(c).addScript("layoutAvailableSpaceIE('" + c.getName() + "');");

            device.print("<table ");
            Utils.optAttribute(device, "layoutHeight", height);
            Utils.optAttribute(device, "borderHeight", borderHeight);
            preferredSize.setHeight(null);
        } else {
            device.print("<table");
        }

        writeAllAttributes(device, scrollpane);
        if (requiresFillBehaviour)
            preferredSize.setHeight(height);
        device.print(">");
        Utils.renderContainer(device, scrollpane);
        device.print("</table>");
    }
}
