package org.wingx.plaf.css;

import org.wings.plaf.css.AbstractComponentCG;
import org.wings.plaf.css.Utils;
import org.wings.plaf.CGManager;
import org.wings.io.Device;
import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.session.SessionManager;
import org.wingx.XDivision;

import java.io.IOException;

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
        device.print("><colgroup><col width=\"0*\"/><col width=\"1*\"></colgroup><tr><td class=\"DivisionControl\">");
        writeDivisionIcon(device, division, "t", division.isShaded() ? closedIcon : openIcon, null);
        device.print("</td><td class=\"DivisionTitle\">");
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
        device.print(" alt=\"");
        device.print(icon.getIconTitle());
        device.print("\"/>");
    }

    protected void writeDivisionIcon(Device device, XDivision division, String event, SIcon icon, String cssClass) throws IOException {
        boolean showAsFormComponent = division.getShowAsFormComponent();
        boolean enabled = division.isEnabled();

        Utils.printButtonStart(device, division, event, enabled, showAsFormComponent, cssClass);
        device.print(">");
        writeIcon(device, icon, null);
        Utils.printButtonEnd(device, division, event, enabled);
    }
}
