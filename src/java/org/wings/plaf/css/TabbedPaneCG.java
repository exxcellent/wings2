/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SIcon;
import org.wings.STabbedPane;
import org.wings.io.Device;
import org.wings.plaf.AbstractComponentCG;
import org.wings.session.Browser;
import org.wings.session.SessionManager;

import java.io.IOException;
import java.util.Properties;

public class TabbedPaneCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.PanelCG {
    public void writeContent(final Device device, final SComponent component)
        throws java.io.IOException {
        STabbedPane tabbedPane = (STabbedPane)component;
        String style = component.getStyle();
        boolean childSelectorWorkaround = !component.getSession().getUserAgent().supportsChildSelector();
        int placement = tabbedPane.getTabPlacement();

        device
            .print("<table cellspacing=\"0\"");
        if (childSelectorWorkaround)
            Utils.childSelectorWorkaround(device, style);
        device
            .print(">");

        if (placement == STabbedPane.TOP)
            device
                .print("<tr><th placement=\"top\"");
        else if (placement == STabbedPane.LEFT)
            device
                .print("<tr><th placement=\"left\"");
        else if (placement == STabbedPane.RIGHT)
            device
                .print("<tr><td");
        else if (placement == STabbedPane.BOTTOM)
            device
                .print("<tr><td");

        if (childSelectorWorkaround) {
            if (placement == STabbedPane.TOP)
                Utils.childSelectorWorkaround(device, "top");
            else if (placement == STabbedPane.LEFT)
                Utils.childSelectorWorkaround(device, "left");
            else
                Utils.childSelectorWorkaround(device, "pane");
        }

        device.print(">");

        if (placement == STabbedPane.TOP
            || placement == STabbedPane.LEFT)
            writeTabs(device, tabbedPane);
        else
            writePane(device, tabbedPane);

        if (placement == STabbedPane.TOP)
            device
                .print("</th></tr><tr><td");
        else if (placement == STabbedPane.LEFT)
            device
                .print("</th><td");
        else if (placement == STabbedPane.RIGHT)
            device
                .print("</td><th placement=\"right\"");
        else if (placement == STabbedPane.BOTTOM)
            device
                .print("</td></tr><tr><th placement=\"bottom\"");

        if (childSelectorWorkaround) {
            if (placement == STabbedPane.RIGHT)
                Utils.childSelectorWorkaround(device, "right");
            else if (placement == STabbedPane.BOTTOM)
                Utils.childSelectorWorkaround(device, "bottom");
            else
                Utils.childSelectorWorkaround(device, "pane");
        }

        device.print(">");

        if (placement == STabbedPane.TOP
            || placement == STabbedPane.LEFT) {
            writePane(device, tabbedPane);
            device
                .print("</td></tr></table>");
        }
        else {
            writeTabs(device, tabbedPane);
            device
                .print("</th></tr></table>");
        }
    }

    private void writePane(Device device, STabbedPane tabbedPane) throws IOException {
        tabbedPane.getSelectedComponent().write(device);
    }

    private void writeTabs(Device device, STabbedPane tabbedPane) throws IOException {
        boolean childSelectorWorkaround = !tabbedPane.getSession().getUserAgent().supportsChildSelector();
        boolean showAsFormComponent = tabbedPane.getShowAsFormComponent();
        boolean konquerorWorkaround = "Konqueror".equals(tabbedPane.getSession().getUserAgent().getBrowserName());

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            SIcon icon = tabbedPane.getIconAt(i);
            String title = tabbedPane.getTitleAt(i);
            String tooltip = tabbedPane.getToolTipText();
            if (konquerorWorkaround)
                title = nonBreakingSpaces(title);

            if (showAsFormComponent)
                device
                    .print("<button type=\"submit\" name=\"")
                    .print(Utils.event(tabbedPane))
                    .print("\" value=\"")
                    .print(i)
                    .print("\"");
            else
                device
                    .print("<a href=\"")
                    .print(tabbedPane.getRequestURL()
                    .addParameter(Utils.event(tabbedPane) + "=" + i).toString())
                    .print("\"");

            if (i == tabbedPane.getSelectedIndex())
                device.print(" selected=\"true\"");

            if (!tabbedPane.isEnabledAt(i))
                device.print(" disabled=\"true\"");

            if (childSelectorWorkaround) {
                if (i == tabbedPane.getSelectedIndex())
                    Utils.childSelectorWorkaround(device, "selected");
                if (!tabbedPane.isEnabledAt(i))
                    Utils.childSelectorWorkaround(device, "disabled");
            }

            device.print(">");

            if (icon != null && tabbedPane.getTabPlacement() != STabbedPane.RIGHT) {
                device.print("<img");
                org.wings.plaf.Utils.optAttribute(device, "src", icon.getURL());
                org.wings.plaf.Utils.optAttribute(device, "alt", tooltip);
                org.wings.plaf.Utils.optAttribute(device, "width", icon.getIconWidth());
                org.wings.plaf.Utils.optAttribute(device, "height", icon.getIconHeight());
                device.print("/>&nbsp;");
            }

            org.wings.plaf.Utils.write(device, title);

            if (icon != null && tabbedPane.getTabPlacement() == STabbedPane.RIGHT) {
                device.print("&nbsp;<img");
                org.wings.plaf.Utils.optAttribute(device, "src", icon.getURL());
                org.wings.plaf.Utils.optAttribute(device, "alt", tooltip);
                org.wings.plaf.Utils.optAttribute(device, "width", icon.getIconWidth());
                org.wings.plaf.Utils.optAttribute(device, "height", icon.getIconHeight());
                device.print("/>");
            }

            if (showAsFormComponent)
                device.print("</button>\n");
            else
                device.print("</a>\n");
        }
    }

    private String nonBreakingSpaces(String title) {
        return title.replace(' ', '\u00A0');
    }

    public String mapSelector(String selector) {
        System.out.println("selector = " + selector);
        System.out.println("selector = " + geckoMappings.getProperty(selector));
        Browser browser = SessionManager.getSession().getUserAgent();
        if (browser.hasGecko())
            return geckoMappings.getProperty(selector, selector);
        else if ("MSIE".equals(browser.getBrowserName()))
            return msieMappings.getProperty(selector, selector);
        else
            return defaultMappings.getProperty(selector, selector);
    }


    private static final Properties msieMappings = new Properties();
    private static final Properties geckoMappings = new Properties();
    private static final Properties defaultMappings = new Properties();

    static {
        msieMappings.setProperty(STabbedPane.SELECTOR_SELECTION, " *.selected");
        msieMappings.setProperty(STabbedPane.SELECTOR_CONTENT, " td.content");
        msieMappings.setProperty(STabbedPane.SELECTOR_TABS, " th");
        geckoMappings.setProperty(STabbedPane.SELECTOR_SELECTION, " > table > tbody > tr > th > *[selected=\"true\"]");
        geckoMappings.setProperty(STabbedPane.SELECTOR_CONTENT, " > table > tbody > tr > td");
        geckoMappings.setProperty(STabbedPane.SELECTOR_TABS, " > table > tbody > tr > th");
        defaultMappings.setProperty(STabbedPane.SELECTOR_SELECTION, " > table > tr > th > *[selected=\"true\"]");
        defaultMappings.setProperty(STabbedPane.SELECTOR_CONTENT, " > table > tr > td");
        defaultMappings.setProperty(STabbedPane.SELECTOR_TABS, " > table > tr > th");
    }
}
