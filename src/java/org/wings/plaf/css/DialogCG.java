// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Dialog.plaf'
package org.wings.plaf.css;


import org.wings.*;
import org.wings.event.SInternalFrameEvent;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.session.SessionManager;

import java.io.IOException;

public class DialogCG extends org.wings.plaf.css.FormCG implements SConstants, org.wings.plaf.DialogCG {

//--- byte array converted template snippets.
    private final static byte[] __img_border_0 = "<img border=\"0\"".getBytes();
    private final static byte[] __ = "/>".getBytes();
    private final static byte[] __td = "<td".getBytes();
    private final static byte[] __class_framebut = " class=\"framebutton\"><a href=\"".getBytes();
    private final static byte[] ___1 = "\">".getBytes();
    private final static byte[] __a_td = "</a></td>".getBytes();
    private final static byte[] __table_cellpadd = "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">".getBytes();
    private final static byte[] __table_border_0 = "<table border=\"0\" width=\"100%\"><tr>".getBytes();
    private final static byte[] __td_align_cente = "<td align=\"center\" valign=\"middle\">".getBytes();
    private final static byte[] __table_cellpadd_1 = "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"".getBytes();
    private final static byte[] __tr = "><tr>".getBytes();
    private final static byte[] __class_framebut_1 = " class=\"framebutton\">".getBytes();
    private final static byte[] __nbsp_b = "\">&nbsp;<b>".getBytes();
    private final static byte[] __b_td = "</b></td>".getBytes();
    private final static byte[] __tr_1 = "</tr>".getBytes();
    private final static byte[] __tr_td_colspan = "<tr><td colspan=\"".getBytes();
    private final static byte[] __class = "\" class=\"".getBytes();
    private final static byte[] __td_tr_table = "</td></tr></table>".getBytes();
    private final static byte[] ___2 = "\n".getBytes();

//--- properties of this plaf.
    private SIcon closeIcon;

    /**
     * Initialize properties from config
     */
    public DialogCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();

        setCloseIcon((SIcon)manager.getObject("DialogCG.closeIcon", SIcon.class));
    }


    public void installCG(final SComponent comp) {
        final SDialog component = (SDialog)comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value;
        value = manager.getObject("SDialog.style", String.class);
        if (value != null) {
            component.setStyle((String)value);
        }
    }

//--- code from common area in template.
    private void writeIcon(Device device, SIcon icon) throws IOException {

        device.write(__img_border_0);
        org.wings.plaf.Utils.optAttribute(device, "src", icon.getURL());
        org.wings.plaf.Utils.optAttribute(device, "width", icon.getIconWidth());
        org.wings.plaf.Utils.optAttribute(device, "height", icon.getIconHeight());

        device.write(__);
    }

    private void writeWindowIcon(Device device, SDialog dialog,
                                 int event, SIcon icon) throws IOException {
        RequestURL addr = dialog.getRequestURL();
        addr.addParameter(org.wings.plaf.css.Utils.event(dialog), event);

        device.write(__td);
        org.wings.plaf.Utils.optAttribute(device, "width", icon.getIconWidth());

        device.write(__class_framebut);
        org.wings.plaf.Utils.write(device, addr);

        device.write(___1);
        writeIcon(device, icon);

        device.write(__a_td);
    }


//--- end code from common area in template.


    public void writeContent(final Device device,
                             final SComponent _c)
        throws IOException {
        final SDialog component = (SDialog)_c;

//--- code from write-template.
        final SDialog dialog = (SDialog)component;

        device.write(__table_cellpadd);
        int cols = 0;
        String text = dialog.getTitle();
        if (text == null)
            text = "Dialog";

        device.write(__table_border_0);
        device.write(__td_align_cente);
        device.write(__table_cellpadd_1);
        final SDimension dim = component.getPreferredSize();
        if (dim != null) {
            org.wings.plaf.Utils.optAttribute(device, "width", dim.width);
            org.wings.plaf.Utils.optAttribute(device, "height", dim.height);
        }
        device.write(__tr);
        // left icon
        if (dialog.getIcon() != null) {
            SIcon icon = dialog.getIcon();
            device.write(__td);
            org.wings.plaf.Utils.optAttribute(device, "width", icon.getIconWidth());
            device.write(__class_framebut_1);
            writeIcon(device, icon);
            cols++;
        }

        device.print("<th>");
        org.wings.plaf.Utils.write(device, text);
        device.print("</th>");
        cols++;

        if (dialog.isClosable() && closeIcon != null) {
            writeWindowIcon(device, dialog,
                SInternalFrameEvent.INTERNAL_FRAME_CLOSED, closeIcon);
            cols++;
        }

        device.write(__tr_1);
        device.write(__tr_td_colspan);
        org.wings.plaf.Utils.write(device, cols);
        device.write(___1);
        super.writeContent(device, component);
        device.write(__td_tr_table);
        device.write(__td_tr_table);
        device.write(___2);
    }

//--- setters and getters for the properties.
    public SIcon getCloseIcon() {
        return closeIcon;
    }

    public void setCloseIcon(SIcon closeIcon) {
        this.closeIcon = closeIcon;
    }

}
