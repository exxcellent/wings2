package org.wings.plaf.css.msie;

import org.wings.SBorderLayout;
import org.wings.SCardLayout;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SDimension;
import org.wings.SGridBagLayout;
import org.wings.SLayoutManager;
import org.wings.STemplateLayout;
import org.wings.border.SAbstractBorder;
import org.wings.io.Device;
import org.wings.plaf.css.RenderHelper;
import org.wings.plaf.css.Utils;

public final class ContainerCG extends org.wings.plaf.css.ContainerCG {
    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device, final SComponent component) throws java.io.IOException {
        SContainer container = (SContainer) component;
        SLayoutManager layout = container.getLayout();

        boolean requiresFillBehaviour = false;
        SDimension preferredSize = null;
        String height = null;
        if (layout instanceof SBorderLayout || layout instanceof SGridBagLayout) {
            preferredSize = container.getPreferredSize();
            if (preferredSize != null) {
                height = preferredSize.getHeight();
                if (height != null && !"auto".equals(height))
                    requiresFillBehaviour = true;
            }
        }

        if (requiresFillBehaviour) {
            int borderHeight = 0;
            SAbstractBorder border = (SAbstractBorder) container.getBorder();
            if (border != null) {
                borderHeight += border.getThickness(SConstants.TOP);
                borderHeight += border.getThickness(SConstants.BOTTOM);
            }

            RenderHelper.getInstance(component).addScript("wingS.layout.layoutAvailableSpaceIE('" + component.getName() + "');");
            device.print("<table ");
            Utils.optAttribute(device, "layoutHeight", height);
            Utils.optAttribute(device, "borderHeight", borderHeight);
            preferredSize.setHeight(null);
        }
        else
            device.print("<table");

        writeAllAttributes(device, component);
        if (requiresFillBehaviour)
            preferredSize.setHeight(height);

        Utils.writeEvents(device, component, null);
        device.print(">");

        // special case templateLayout, open cell
        boolean writeTableData = container.getLayout() instanceof STemplateLayout
            || container.getLayout() instanceof SCardLayout;
        if (writeTableData) {
            device.print("<tr><td>");
        }

        Utils.renderContainer(device, container);

        if (writeTableData) {
            device.print("</td></tr>");
        }
        device.print("</table>");
    }
}
