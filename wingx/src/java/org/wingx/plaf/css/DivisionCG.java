package org.wingx.plaf.css;

import java.io.IOException;

import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.plaf.css.AbstractComponentCG;
import org.wings.plaf.css.Utils;
import org.wings.session.SessionManager;
import org.wings.style.Style;
import org.wingx.XDivision;

public class DivisionCG
    extends AbstractComponentCG
    implements org.wingx.plaf.DivisionCG
{
    private SIcon openIcon;
    private SIcon closedIcon;

    public DivisionCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();

        setOpenIcon((SIcon)manager.getObject("DivisionCG.openIcon", SIcon.class));
        setClosedIcon((SIcon)manager.getObject("DivisionCG.closedIcon", SIcon.class));
    }

    public void setOpenIcon(SIcon openIcon) {
        this.openIcon = openIcon;
    }

    public void setClosedIcon(SIcon closedIcon) {
        this.closedIcon = closedIcon;
    }

    public void writeInternal(Device device, SComponent component) throws IOException {
        XDivision division = (XDivision)component;

        device.print("<table");
        writeAllAttributes(device, component);
        Utils.writeEvents(device, component, null);
        device.print("><colgroup><col width=\"0*\"/><col width=\"1*\"></colgroup><tr ");
        if ( division.isTitleClickabel() == true ) {
            Utils.printClickability(device, division, "t", division.isEnabled(), true);
        }
        device.print("><td class=\"DivisionControl\"");
        SIcon icon = division.isShaded() ? closedIcon : openIcon;
        if ( division.isTitleClickabel() == false ) {
            Utils.printClickability(device, division, "t", division.isEnabled(), true);
        }
        device.print(">");
        writeIcon(device, icon, null);
        device.print("</td><td class=\"DivisionTitle\"");
        final Style style = component.getDynamicStyle(XDivision.SELECTOR_TITLE);
        if ( style != null ) {
            Utils.optAttribute( device, "style", style.toString() );
        }
        device.print(">");
        if (division.getIcon() != null) {
            writeIcon(device, division.getIcon(), null);
            device.print("&nbsp;");
        }
        if (division.getTitle() != null)
            writeTitle(device, division.getTitle());
        device.print("</td></tr>");

        if (!division.isShaded()) {
            device.print("<tr><td></td><td class=\"DivisionContent\"><table class=\"DivisionContent\">");
            Utils.renderContainer(device, division);
            device.print("</table></td></tr>");
        }
        device.print("</table>");
    }

    private void writeTitle(Device device, String text) throws IOException {
        if ((text.length() > 5) && (text.startsWith("<html>")))
            Utils.writeRaw(device, text.substring(6));
        else
            Utils.quote(device, text, true, true, false);
    }

    protected void writeIcon(Device device, SIcon icon, String cssClass) throws IOException {
        device.print("<img");
        if (cssClass != null) {
            device.print(" class=\"");
            device.print(cssClass);
            device.print("\"");
        }
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());
        Utils.attribute(device, "alt", icon.getIconTitle());
        device.print("/>");
    }
}
