package org.wings.plaf.css;

import org.wings.io.Device;
import org.wings.*;
import org.wings.header.Header;
import org.wings.header.SessionHeaders;
import org.wings.plaf.css.script.OnHeadersLoadedScript;
import org.wings.plaf.css.script.OnPageRenderedScript;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class SplitPaneCG
    extends AbstractComponentCG
    implements org.wings.plaf.SplitPaneCG
{
    protected final List<Header> headers = new ArrayList<Header>();

    public SplitPaneCG() {
        headers.add(Utils.createExternalizedJSHeaderFromProperty(Utils.JS_EXT_DD_CORE));
        headers.add(Utils.createExternalizedJSHeaderFromProperty(Utils.JS_EXT_SPLITBAR));
    }

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        SessionHeaders.getInstance().registerHeaders(headers);
    }

    public void uninstallCG(SComponent component) {
        super.uninstallCG(component);
        SessionHeaders.getInstance().deregisterHeaders(headers);
    }

    public void writeInternal(final Device device, final SComponent component) throws java.io.IOException {
        final SSplitPane splitPane = (SSplitPane)component;
        String name = splitPane.getName();

        device.print("<table");
        writeAllAttributes(device, component);
        Utils.writeEvents(device, component, null);
        device.print(">");

        StringBuilder scriptBuilder = STRING_BUILDER.get();
        scriptBuilder.setLength(0);

        if (splitPane.getOrientation() == SSplitPane.VERTICAL_SPLIT) {
            String topPaneName = name + "_t";
            String bottomPaneName = name + "_b";
            String splitBarName = name + "_tb";

            writeSplitterScript(scriptBuilder, splitBarName, topPaneName, splitPane.getDividerLocation(), "Ext.SplitBar.VERTICAL", "Ext.SplitBar.TOP");
            splitPane.getSession().getScriptManager().addScriptListener(new OnPageRenderedScript(scriptBuilder.toString()));

            writeVertical(device, topPaneName, splitPane.getTopComponent());
            writeVerticalSplitter(device, splitBarName, splitPane.getDividerSize());
            writeVertical(device, bottomPaneName, splitPane.getBottomComponent());
        }
        else {
            String leftPaneName = name + "_l";
            String rightPaneName = name + "_r";
            String splitBarName = name + "_lr";

            writeSplitterScript(scriptBuilder, splitBarName, leftPaneName, splitPane.getDividerLocation(), "Ext.SplitBar.HORIZONTAL", "Ext.SplitBar.LEFT");
            splitPane.getSession().getScriptManager().addScriptListener(new OnPageRenderedScript(scriptBuilder.toString()));

            device.print("<tr>");
            writeHorizontal(device, leftPaneName, splitPane.getLeftComponent());
            writeHorizontalSplitter(device, splitBarName, splitPane.getDividerSize());
            writeHorizontal(device, rightPaneName, splitPane.getRightComponent());
            device.print("</tr>");
        }


        device.print("</table>");
    }

    protected void writeSplitterScript(StringBuilder scriptBuilder, String splitBarName, String paneName, int location, String orientation, String placement) {
        scriptBuilder.append("var ");
        scriptBuilder.append(splitBarName);
        scriptBuilder.append(" = new Ext.SplitBar('");
        scriptBuilder.append(splitBarName);
        scriptBuilder.append("', '");
        scriptBuilder.append(paneName);
        scriptBuilder.append("', ");
        scriptBuilder.append(orientation);
        scriptBuilder.append(", ");
        scriptBuilder.append(placement);
        scriptBuilder.append(");\n");
        if (location != -1) {
            scriptBuilder.append(splitBarName);
            scriptBuilder.append(".setCurrentSize(");
            scriptBuilder.append(location);
            scriptBuilder.append(");\n");
        }
        scriptBuilder.append(splitBarName);
        scriptBuilder.append(".on('moved', wingS.component.splitPaneResized);");
    }

    protected void writeVertical(Device device, String paneName, SComponent paneComponent) throws IOException {
        device.print("<tr><td id=\"");
        device.print(paneName);
        device.print("\">");
        paneComponent.write(device);
        device.print("</td></tr>");
    }

    protected void writeVerticalSplitter(Device device, String name, int dividerSize) throws IOException {
        device.print("<tr><td style=\"height: ");
        device.print(dividerSize);
        device.print("px\" class=\"splitter\" id=\"");
        device.print(name);
        device.print("\"></td></tr>");
    }

    protected void writeHorizontal(Device device, String paneName, SComponent paneComponent) throws IOException {
        device.print("<td id=\"");
        device.print(paneName);
        device.print("\">");
        paneComponent.write(device);
        device.print("</td>");
    }

    protected void writeHorizontalSplitter(Device device, String name, int dividerSize) throws IOException {
        device.print("<td style=\"width: ");
        device.print(dividerSize);
        device.print("px\" class=\"splitter\" id=\"");
        device.print(name);
        device.print("\"></td>");
    }
}
