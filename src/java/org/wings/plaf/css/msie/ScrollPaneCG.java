package org.wings.plaf.css.msie;

import org.wings.io.Device;
import org.wings.*;
import org.wings.plaf.css.*;

import java.io.IOException;
import java.awt.*;

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
            device.print("<table style=\"behavior:url(../fill.htc)\"");
            Utils.optAttribute(device, "intendedHeight", height);
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
