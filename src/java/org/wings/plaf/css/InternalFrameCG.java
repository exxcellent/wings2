// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/InternalFrame.plaf'
package org.wings.plaf.css;


import org.wings.*;
import org.wings.event.SInternalFrameEvent;
import org.wings.io.Device;
import org.wings.plaf.AbstractComponentCG;
import org.wings.plaf.CGManager;
import org.wings.session.SessionManager;

import java.io.IOException;

public class InternalFrameCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.InternalFrameCG {

//--- byte array converted template snippets.
    private final static byte[] __img_border_0  = "<img border=\"0\"".getBytes();
    private final static byte[] __              = "/>".getBytes();
    private final static byte[] __td            = "<td".getBytes();
    private final static byte[] __class_framebut= " class=\"framebutton\"><a href=\"".getBytes();
    private final static byte[] ___1            = "\">".getBytes();
    private final static byte[] __a_td          = "</a></td>".getBytes();
    private final static byte[] __table_cellpadd= "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border: solid 1px\"><tr>".getBytes();
    private final static byte[] __class_framebut_1= " class=\"framebutton\">".getBytes();
    private final static byte[] __td_1          = "</td>".getBytes();
    private final static byte[] __td_width_480_c= "<td width=\"480\" class=\"frametitle\">&nbsp;".getBytes();
    private final static byte[] __tr            = "</tr>".getBytes();
    private final static byte[] __tr_td_colspan = "<tr><td colspan=\"".getBytes();
    private final static byte[] __class_framebor= "\" class=\"frameborder\">".getBytes();
    private final static byte[] __td_tr         = "</td></tr>".getBytes();
    private final static byte[] __table         = "</table>".getBytes();
    private final static byte[] ___2            = "\n".getBytes();

//--- properties of this plaf.
    private SIcon closeIcon;
    private SIcon deiconifyIcon;
    private SIcon iconifyIcon;
    private SIcon maximizeIcon;
    private SIcon unmaximizeIcon;

    /**
     * Initialize properties from config
     */
    public InternalFrameCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();

        setCloseIcon((SIcon) manager.getObject("InternalFrameCG.closeIcon", SIcon.class));
        setDeiconifyIcon((SIcon) manager.getObject("InternalFrameCG.deiconifyIcon", SIcon.class));
        setIconifyIcon((SIcon) manager.getObject("InternalFrameCG.iconifyIcon", SIcon.class));
        setMaximizeIcon((SIcon) manager.getObject("InternalFrameCG.maximizeIcon", SIcon.class));
        setUnmaximizeIcon((SIcon) manager.getObject("InternalFrameCG.unmaximizeIcon", SIcon.class));
    }


    public void installCG(final SComponent comp) {
        final SInternalFrame component = (SInternalFrame) comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value;
        value = manager.getObject("SInternalFrame.style", String.class);
        if (value != null) {
            component.setStyle((String) value);
        }
    }

//--- code from common area in template.
    private void writeIcon(Device device, SIcon icon) throws IOException {

        device.write(__img_border_0);
        org.wings.plaf.Utils.optAttribute( device, "src", icon.getURL());
        org.wings.plaf.Utils.optAttribute( device, "width", icon.getIconWidth());
        org.wings.plaf.Utils.optAttribute( device, "height", icon.getIconHeight());

        device.write(__);
    }

    private void writeWindowIcon(Device device, SInternalFrame frame,
        int event, SIcon icon) throws IOException {
        RequestURL addr = frame.getRequestURL();
        addr.addParameter(org.wings.plaf.css.Utils.event(frame), event);

        device.write(__td);
        org.wings.plaf.Utils.optAttribute( device, "width", icon.getIconWidth());

        device.write(__class_framebut);
        org.wings.plaf.Utils.write( device, addr);

        device.write(___1);
        writeIcon(device, icon);

        device.write(__a_td);
    }


//--- end code from common area in template.


    public void writeContent(final Device device,
                      final SComponent _c)
        throws IOException {
        final SInternalFrame component = (SInternalFrame) _c;

//--- code from write-template.
        SInternalFrame frame = component;
        String text = frame.getTitle();
        int columns = 0;
        if (text == null) {
            text = "wingS";
        }

        device.write(__table_cellpadd);
        // left icon
        if (frame.getIcon() != null) {
            SIcon icon = frame.getIcon();
            device.write(__td);
            org.wings.plaf.Utils.optAttribute( device, "width", icon.getIconWidth());
            device.write(__class_framebut_1);
            writeIcon(device, icon);
            device.write(__td_1);
            ++columns;
        }

        // window main bar (width=480: hack necessary for opera, netscape 4)
        device.write(__td_width_480_c);
        org.wings.plaf.Utils.write( device, text);
        device.write(__td_1);
        ++columns;

        // optional icons.
        if (frame.isIconifyable() && !frame.isIconified() && iconifyIcon != null) {
            writeWindowIcon(device, frame, 
                SInternalFrameEvent.INTERNAL_FRAME_ICONIFIED,
                iconifyIcon);
            ++columns;
        }

        if (frame.isIconified() && deiconifyIcon != null) {
            writeWindowIcon(device, frame, 
                SInternalFrameEvent.INTERNAL_FRAME_DEICONIFIED,
                deiconifyIcon);
            ++columns;
        }

        if (frame.isMaximizable() 
            && !frame.isMaximized() 
            && !frame.isIconified()
            && maximizeIcon != null)  {
            writeWindowIcon(device, frame, 
                SInternalFrameEvent.INTERNAL_FRAME_MAXIMIZED,
                maximizeIcon);
            ++columns;
        }

        if (frame.isClosable() && closeIcon != null) {
            writeWindowIcon(device, frame, 
                SInternalFrameEvent.INTERNAL_FRAME_CLOSED, closeIcon);
            ++columns;
        }
        device.write(__tr);
        // write the actual content
        if (!frame.isIconified()) {
            device.write(__tr_td_colspan);
            org.wings.plaf.Utils.write( device, columns);
            device.write(__class_framebor);
            Utils.renderContainer(device, frame);
            device.write(__td_tr);
        }
        device.write(__table);
        device.write(___2);

//--- end code from write-template.
    }

//--- setters and getters for the properties.
    public SIcon getCloseIcon() { return closeIcon; }
    public void setCloseIcon(SIcon closeIcon) { this.closeIcon = closeIcon; }

    public SIcon getDeiconifyIcon() { return deiconifyIcon; }
    public void setDeiconifyIcon(SIcon deiconifyIcon) { this.deiconifyIcon = deiconifyIcon; }

    public SIcon getIconifyIcon() { return iconifyIcon; }
    public void setIconifyIcon(SIcon iconifyIcon) { this.iconifyIcon = iconifyIcon; }

    public SIcon getMaximizeIcon() { return maximizeIcon; }
    public void setMaximizeIcon(SIcon maximizeIcon) { this.maximizeIcon = maximizeIcon; }

    public SIcon getUnmaximizeIcon() { return unmaximizeIcon; }
    public void setUnmaximizeIcon(SIcon unmaximizeIcon) { this.unmaximizeIcon = unmaximizeIcon; }

}
