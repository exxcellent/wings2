/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;


import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.AbstractComponentCG;
import org.wings.util.AnchorProperties;
import org.wings.util.AnchorRenderStack;

import java.io.IOException;

public class ClickableCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.ClickableCG {

//--- byte array converted template snippets.
    private final static byte[] __name          = " name=\"".getBytes();
    private final static byte[] __              = "\"".getBytes();
    private final static byte[] __onMouseover_if= " onMouseover=\"if(document.images){this.src='".getBytes();
    private final static byte[] __onmouseout_if = "';}\" onmouseout=\"if(document.images){this.src='".getBytes();
    private final static byte[] ___1            = "';}\"".getBytes();
    private final static byte[] __onMousedown_if= " onMousedown=\"if(document.images){this.src='".getBytes();
    private final static byte[] __onmouseup_if_d= "';}\" onmouseup=\"if(document.images){this.src='".getBytes();
    private final static byte[] __a_href        = "<a href=\"".getBytes();
    private final static byte[] __span          = "<span".getBytes();
    private final static byte[] ___2            = ">".getBytes();
    private final static byte[] __nobr          = "<nobr>".getBytes();
    private final static byte[] __nobr_1        = "</nobr>".getBytes();
    private final static byte[] __a             = "</a>".getBytes();
    private final static byte[] __span_1        = "</span>".getBytes();
    private final static byte[] __img_border_0_s= "<img border=\"0\" src=\"".getBytes();
    private final static byte[] ___3            = " />".getBytes();
    private final static byte[] __table         = "<table>".getBytes();
    private final static byte[] __tr_td         = "<tr><td>".getBytes();
    private final static byte[] __td_td         = "</td><td>".getBytes();
    private final static byte[] __td_tr         = "</td></tr>".getBytes();
    private final static byte[] __tr_td_td_td   = "<tr><td></td><td>".getBytes();
    private final static byte[] __td_td_td_tr   = "</td><td></td></tr>".getBytes();
    private final static byte[] __table_1       = "</table>".getBytes();
    private final static byte[] ___4            = "\n".getBytes();

//--- code from common area in template.
    protected void writeDynamicIcons(final Device device, 
        SAbstractIconTextCompound iconTextCompound, SIcon origIcon,
        String iconName, boolean renderNameAttribute)
    throws IOException
        {
        if ( iconTextCompound.isEnabled() ) {
            // render rollover
            SIcon rolloverIcon = iconTextCompound.getRolloverIcon();
            SIcon pressedIcon = iconTextCompound.getPressedIcon();

            if ( rolloverIcon!=null || pressedIcon!=null ) {
                if ( renderNameAttribute) {

                    device.write(__name);
                    org.wings.plaf.Utils.write( device, iconName);

                    device.write(__);
                } // end of if ()


                if ( rolloverIcon!=null ) {

                    device.write(__onMouseover_if);
                    org.wings.plaf.Utils.write( device, rolloverIcon.getURL()
                    );

                    device.write(__onmouseout_if);
                    org.wings.plaf.Utils.write( device, origIcon.getURL()
                    );

                    device.write(___1);
                }

                if ( pressedIcon!=null ) {

                    device.write(__onMousedown_if);
                    org.wings.plaf.Utils.write( device, pressedIcon.getURL()
                    );

                    device.write(__onmouseup_if_d);
                    org.wings.plaf.Utils.write( device, rolloverIcon!=null ? rolloverIcon.getURL() : origIcon.getURL()
                    );

                    device.write(___1);
                }
            }
        }

    }

    protected SIcon getIcon(SAbstractIconTextCompound iconTextCompound) {
        if ( iconTextCompound.isSelected() ) {
            return iconTextCompound.isEnabled() 
            ? iconTextCompound.getSelectedIcon()
            : iconTextCompound.getDisabledSelectedIcon();
        } else {
            return iconTextCompound.isEnabled() 
            ? iconTextCompound.getIcon()
            : iconTextCompound.getDisabledIcon();
        }
    }



    void writeText(final Device device, SAbstractClickable clickable, String attr)
    throws IOException
        {
        String text = clickable.getText();
        if (text == null || text.length() == 0) {
            return;
        }

        SimpleURL url = getURL(clickable);

        boolean writeAnchor = clickable.isEnabled() && 
        url!=null;

        if ( writeAnchor ) {

            device.write(__a_href);
            org.wings.plaf.Utils.write( device, url);

            device.write(__);
            org.wings.plaf.css.Utils.writeEvents(device, clickable);
            org.wings.plaf.Utils.optAttribute( device, "title", clickable.getToolTipText());
        }
        else {

            device.write(__span);
            Utils.writeEvents(device, clickable);
        }
        org.wings.plaf.Utils.optAttribute( device, "class", attr);

        device.write(___2);
        org.wings.plaf.Utils.write( device, clickable.getText());

        if ( writeAnchor ) {

            device.write(__a);
        }
        else {

            device.write(__span_1);
        }
    }

    SimpleURL getURL(SAbstractClickable clickable) {
        SimpleURL url = clickable.getURL();

        if ( url==null ) {
            AnchorProperties anchor	= AnchorRenderStack.get();
            if ( anchor!=null ) {
                url = anchor.getURL();
            }
        }

        return url;
    }

    void writeIcon(final Device device, SAbstractClickable clickable, String attr)
    throws IOException
        {
        SIcon icon = getIcon(clickable);

        SimpleURL url = getURL(clickable);

        boolean writeAnchor = clickable.isEnabled() && 
        url!=null;

        if ( writeAnchor ) {

            device.write(__a_href);
            org.wings.plaf.Utils.write( device, url);

            device.write(__);
        }
        else {

            device.write(__span);
        }
        org.wings.plaf.Utils.optAttribute( device, "class", attr);

        device.write(___2);
        String tooltip = clickable.getToolTipText();
        if (tooltip == null)
        tooltip = clickable.getText();


        device.write(__img_border_0_s);
        org.wings.plaf.Utils.write( device, icon.getURL());

        device.write(__);
        writeDynamicIcons(device, clickable, icon, "Icon_" + clickable.getComponentId(),true);
        org.wings.plaf.Utils.optAttribute( device, "width", icon.getIconWidth());
        org.wings.plaf.Utils.optAttribute( device, "height", icon.getIconHeight());
        Utils.writeEvents(device, clickable);
        org.wings.plaf.Utils.optAttribute( device, "title", tooltip);
        org.wings.plaf.Utils.optAttribute( device, "alt", tooltip);

        device.write(___3);
        if ( writeAnchor ) {

            device.write(__a);
        }
        else {

            device.write(__span_1);
        }
    }


//--- end code from common area in template.


    public void writeContent(final Device device,
                      final SComponent _c)
        throws IOException {
        final SAbstractClickable component = (SAbstractClickable) _c;

//--- code from write-template.
        String style = component.isSelected() ? Utils.selectionStyle(component) : Utils.style(component);

        SIcon icon = getIcon(component);

        String  text = component.getText();
        int     horizontalTextPosition = component.getHorizontalTextPosition();
        int     verticalTextPosition   = component.getVerticalTextPosition();

        if (horizontalTextPosition == NO_ALIGN)
        horizontalTextPosition = RIGHT;
        if (verticalTextPosition == NO_ALIGN)
        verticalTextPosition = CENTER;

        if (icon == null) {
            writeText(device, component, style);
        } else if (text == null) {
            writeIcon(device, component, style);
        } else {
            device.write(__table);
            // if we are on the top, create a top row
            if (verticalTextPosition == TOP) {
                device.write(__tr_td);
                if (horizontalTextPosition == LEFT) { 
                    writeText(device, component, style);
                }
                device.write(__td_td);
                if (horizontalTextPosition == CENTER) { 
                    writeText(device, component, style);
                }
                device.write(__td_td);
                if (horizontalTextPosition == RIGHT) {
                    writeText(device, component, style);
                }
                device.write(__td_tr);
            }

            // we always have a center row, since we have to write the icon
            if (verticalTextPosition == CENTER) {
                device.write(__tr_td);
                if (horizontalTextPosition == LEFT) { 
                    writeText(device, component, style);
                }
                device.write(__td_td);
                if (horizontalTextPosition == CENTER) { 
                    writeText(device, component, style);
                }
                // the rendered icon in the center
                writeIcon(device, component, style); 
                device.write(__td_td);
                if (horizontalTextPosition == RIGHT) { 
                    writeText(device, component, style);
                }
                device.write(__td_tr);
            } else {
                device.write(__tr_td_td_td);
                writeIcon(device, component, style);
                device.write(__td_td_td_tr);
            }

            // ..
            if (verticalTextPosition == BOTTOM) {
                device.write(__tr_td);
                if (horizontalTextPosition == LEFT) {
                    writeText(device, component, style);
                }
                device.write(__td_td);
                if (horizontalTextPosition == CENTER) {
                    writeText(device, component, style);
                }
                device.write(__td_td);
                if (horizontalTextPosition == RIGHT) {
                    writeText(device, component, style);
                }
                device.write(__td_tr);
            } 
            device.write(__table_1);
        }
        device.write(___4);

//--- end code from write-template.
    }
}
