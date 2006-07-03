package org.wings.plaf.css.msie;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SScrollPane;
import org.wings.border.SAbstractBorder;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;

import java.io.IOException;

public final class ScrollPaneCG extends org.wings.plaf.css.ScrollPaneCG {

    private static final long serialVersionUID = 1L;

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

            device.print("<table style=\"behavior:url(../fill.htc)\"");
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
