package org.wings.plaf.css.msie;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SForm;
import org.wings.SLayoutManager;
import org.wings.border.SAbstractBorder;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;

import java.io.IOException;

public class FormCG extends org.wings.plaf.css.FormCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device, final SComponent c) throws IOException {
        final SForm component = (SForm) c;

        device.print("<form method=\"");
        if (component.isPostMethod()) {
            device.print("post");
        } else {
            device.print("get");
        }
        device.print("\"");
        writeAllAttributes(device, component);
        Utils.optAttribute(device, "name", component.getName());
        Utils.optAttribute(device, "enctype", component.getEncodingType());
        Utils.optAttribute(device, "action", component.getRequestURL());
        Utils.writeEvents(device, component, null);

        /*
        * we render two icons into the page that captures pressing simple 'return'
        * in the page. Why ? Depending on the Browser, the Browser sends the
        * first or the last submit-button it finds in the page as 'default'-Submit
        * when we simply press 'return' somewhere.
        *
        * However, we don't want to have this arbitrary behaviour in wingS.
        * So we add these two (invisible image-) submit-Buttons, either of it
        * gets triggered on simple 'return'. No real wingS-Button will then be
        * triggered but only the ActionListener added to the SForm. So we have
        * a way to distinguish between Forms that have been sent as default and
        * pressed buttons.
        *
        * Watchout: the style of these images once had been changed to display:none;
        * to prevent taking some pixel renderspace. However, display:none; made
        * the Internet Explorer not accept this as an input getting the default-focus,
        * so it fell back to the old behaviour. So changed that style to no-padding,
        * no-margin, no-whatever (HZ).
        */
        final String defaultButtonName = component.getDefaultButton() != null ? Utils.event(component.getDefaultButton()) : "capture_enter";
        device.print("><input type=\"image\" name=\"").print(defaultButtonName).print("\" border=\"0\" ");
        Utils.optAttribute(device, "src", getBlindIcon().getURL());
        device.print(" width=\"0\" height=\"0\" tabindex=\"\" style=\"border:none;padding:0px;margin:0px;position:absolute\"/>");

        // Not sure: Think this was for optionally expiring old GET views?!
        device.print("<input type=\"hidden\" name=\"");
        Utils.write(device, Utils.event(component));
        device.print("\" value=\"");
        Utils.write(device, component.getName());
        device.print(SConstants.UID_DIVIDER);
        device.print("\" />");

        boolean requiresFillBehaviour = false;
        SDimension preferredSize = null;
        String height = null;

        SLayoutManager layout = component.getLayout();
        if (layout instanceof org.wings.plaf.css.ScrollPaneLayoutCG || layout instanceof org.wings.plaf.css.BorderLayoutCG) {
            preferredSize = component.getPreferredSize();
            if (preferredSize != null) {
                height = preferredSize.getHeight();
                if (height != null && !"auto".equals(height))
                    requiresFillBehaviour = true;
            }
        }

        if (requiresFillBehaviour) {
            int borderHeight = 0;
            SAbstractBorder border = (SAbstractBorder) component.getBorder();
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

        Utils.printCSSInlineFullSize(device, component.getPreferredSize());
        if (requiresFillBehaviour)
            preferredSize.setHeight(height);

        device.print(">");

        // Render the container itself
        Utils.renderContainer(device, component);

        device.print("</table>");

        // Enter capture at end of form
        device.print("<input type=\"image\" name=\"").print(defaultButtonName).print("\" border=\"0\" ");
        Utils.optAttribute(device, "src", getBlindIcon().getURL());
        device.print(" width=\"0\" height=\"0\" tabindex=\"\" style=\"border:none;padding:0px;margin:0px;position:absolute\"/>");

        device.print("</form>");
    }
}
