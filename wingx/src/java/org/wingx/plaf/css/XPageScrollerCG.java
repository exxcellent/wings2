package org.wingx.plaf.css;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SIcon;
import org.wings.SPageScroller;
import org.wings.SResourceIcon;
import org.wings.io.Device;
import org.wings.plaf.css.AbstractComponentCG;
import org.wings.plaf.css.Utils;

/**
 * CG for a pagescroller.
 *
 * @author holger
 */
public final class XPageScrollerCG extends AbstractComponentCG implements org.wings.plaf.PageScrollerCG {
    private static final long serialVersionUID = 1L;

    private final static transient Log log = LogFactory.getLog(org.wingx.plaf.css.XPageScrollerCG.class);

    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int FORWARD_BLOCK = 2;
    public static final int BACKWARD_BLOCK = 3;
    public static final int FIRST = 4;
    public static final int LAST = 5;
    private final static SIcon[][][] DEFAULT_ICONS = new SIcon[2][6][2];
    public static SIcon TRANSPARENT_ICON = new SResourceIcon("org/wings/icons/transdot.gif");

    static {
        String[] postfixes = new String[6];
        String[] prefixes = new String[6];
        for (int orientation = 0; orientation < 2; orientation++) {
            prefixes[org.wingx.plaf.css.XPageScrollerCG.BACKWARD] = "navigate_";
            prefixes[org.wingx.plaf.css.XPageScrollerCG.FORWARD] = "navigate_";
            prefixes[org.wingx.plaf.css.XPageScrollerCG.FIRST] = "navigate_";
            prefixes[org.wingx.plaf.css.XPageScrollerCG.LAST] = "navigate_";
            prefixes[org.wingx.plaf.css.XPageScrollerCG.FORWARD_BLOCK] = "navigate_";
            prefixes[org.wingx.plaf.css.XPageScrollerCG.BACKWARD_BLOCK] = "navigate_";
            if (orientation == SConstants.VERTICAL) {
                postfixes[org.wingx.plaf.css.XPageScrollerCG.BACKWARD] = "up";
                postfixes[org.wingx.plaf.css.XPageScrollerCG.FORWARD] = "down";
                postfixes[org.wingx.plaf.css.XPageScrollerCG.FIRST] = "top";
                postfixes[org.wingx.plaf.css.XPageScrollerCG.LAST] = "bottom";
                postfixes[org.wingx.plaf.css.XPageScrollerCG.BACKWARD_BLOCK] = "up2";
                postfixes[org.wingx.plaf.css.XPageScrollerCG.FORWARD_BLOCK] = "down2";
            } else {
                postfixes[org.wingx.plaf.css.XPageScrollerCG.BACKWARD] = "left";
                postfixes[org.wingx.plaf.css.XPageScrollerCG.FORWARD] = "right";
                postfixes[org.wingx.plaf.css.XPageScrollerCG.FIRST] = "beginning";
                postfixes[org.wingx.plaf.css.XPageScrollerCG.LAST] = "end";
                postfixes[org.wingx.plaf.css.XPageScrollerCG.BACKWARD_BLOCK] = "left2";
                postfixes[org.wingx.plaf.css.XPageScrollerCG.FORWARD_BLOCK] = "right2";
            }

            for (int direction = 0; direction < postfixes.length; direction++) {
                org.wingx.plaf.css.XPageScrollerCG.DEFAULT_ICONS[orientation][direction][0] =
                    new SResourceIcon("org/wingx/table/images/"
                        + prefixes[direction]
                        + postfixes[direction] + ".png");
                org.wingx.plaf.css.XPageScrollerCG.DEFAULT_ICONS[orientation][direction][1] =
                    new SResourceIcon("org/wingx/table/images/"
                        + prefixes[direction]
                        + "disabled_"
                        + postfixes[direction] + ".png");
            }
        }
    }

    public void writeInternal(Device d, SComponent c)
            throws IOException {
        SPageScroller sb = (SPageScroller) c;

        String style = sb.getStyle();
        if (sb.getLayoutMode() == SConstants.VERTICAL) {
            sb.setStyle(style + " SPageScroller_vertical");
            writeVerticalPageScroller(d, sb);
        }
        else {
            sb.setStyle(style + " SPageScroller_horizontal");
            writeHorizontalPageScroller(d, sb);
        }
        sb.setStyle(style);
    }

    private void writeVerticalPageScroller(Device d, SPageScroller sb) throws IOException {
        int value = sb.getValue();
        int extent = sb.getExtent();
        int minimum = sb.getMinimum();
        int maximum = sb.getMaximum();
        boolean backEnabled = value > minimum;
        boolean forwardEnabled = value < maximum - extent;
        boolean firstPage = (value == minimum);
        boolean lastPage = (value >= (maximum - extent));
        boolean msie = isMSIE(sb);

        d.print("<table");
        writeAllAttributes(d, sb);
        Utils.writeEvents(d, sb, null);
        d.print("><tbody><tr height=\"1%\">")
            .print("<td height=\"1%\"><table class=\"buttons\"><tbody>");

        d.print("<tr><td");
        Utils.printClickability(d, sb, "" + minimum, !firstPage, sb.getShowAsFormComponent());
        d.print(">");
        writeIcon(d, XPageScrollerCG.DEFAULT_ICONS[SConstants.VERTICAL][XPageScrollerCG.FIRST][0], msie);
        d.print("</td></tr>");

        d.print("<tr><td");
        Utils.printClickability(d, sb, "" + (value - extent), backEnabled, sb.getShowAsFormComponent());
        d.print(">");
        writeIcon(d, XPageScrollerCG.DEFAULT_ICONS[SConstants.VERTICAL][XPageScrollerCG.BACKWARD][0], msie);
        d.print("</td></tr>");

        d.print("</tbody></table></td>")
                .print("</tr>")
                .print("<tr>")
                .print("<td><table class=\"pages\"><tbody>");

        int firstDirectPage = sb.getCurrentPage() - (sb.getDirectPages() - 1) / 2;
        firstDirectPage = Math.min(firstDirectPage, sb.getPageCount() - sb.getDirectPages());
        firstDirectPage = Math.max(firstDirectPage, 0);

        for (int i = 0; i < Math.min(sb.getDirectPages(), sb.getPageCount() - firstDirectPage); i++) {
            int page = firstDirectPage + i;
            d.print("<tr><td");
            boolean isCurrentPage = (sb.getCurrentPage() == page);
            Utils.optAttribute(d, "class", isCurrentPage ? "page_selected" : null);
            Utils.printClickability(d, sb, String.valueOf(page * sb.getExtent()), !isCurrentPage, sb.getShowAsFormComponent());
            d.print(">");
            d.print(Integer.toString(page + 1));
            d.print("</td></tr>");
        }

        d.print("</tbody></table></td>")
                .print("</tr>")
                .print("<tr height=\"1%\">")
                .print("<td height=\"1%\"><table class=\"buttons\"><tbody>");

        d.print("<tr><td");
        Utils.printClickability(d, sb, "" + (value + extent), forwardEnabled, sb.getShowAsFormComponent());
        d.print(">");
        writeIcon(d, XPageScrollerCG.DEFAULT_ICONS[SConstants.VERTICAL][XPageScrollerCG.FORWARD][0], msie);
        d.print("</td></tr>");

        d.print("<tr><td");
        Utils.printClickability(d, sb, "" + (maximum + 1 - extent), !lastPage, sb.getShowAsFormComponent());
        d.print(">");
        writeIcon(d, XPageScrollerCG.DEFAULT_ICONS[SConstants.VERTICAL][XPageScrollerCG.LAST][0], msie);
        d.print("</td></tr>");

        d.print("</tbody></table></td>")
                .print("</tr>")
                .print("</tbody></table>");
    }

    private void writeHorizontalPageScroller(Device d, SPageScroller sb) throws IOException {
        int value = sb.getValue();
        int extent = sb.getExtent();
        int minimum = sb.getMinimum();
        int maximum = sb.getMaximum();
        boolean backEnabled = value > minimum;
        boolean forwardEnabled = value < maximum - extent;
        boolean firstPage = (value == minimum);
        boolean lastPage = (value >= (maximum - extent));
        boolean msie = isMSIE(sb);

        d.print("<table");
        writeAllAttributes(d, sb);
        Utils.writeEvents(d, sb, null);
        d.print("><tbody><tr>")
            .print("<td><table class=\"buttons\"><tbody><tr>");

        d.print("<td valign=\"top\"");
        SIcon icon3 = XPageScrollerCG.DEFAULT_ICONS[SConstants.HORIZONTAL][XPageScrollerCG.FIRST][firstPage ? 1 : 0];
        Utils.printClickability(d, sb, "" + minimum, !firstPage, sb.getShowAsFormComponent());
        d.print(">");
        writeIcon(d, icon3, msie);
        d.print("</td>\n");

        d.print("<td valign=\"top\"");
        SIcon icon2 = XPageScrollerCG.DEFAULT_ICONS[SConstants.HORIZONTAL][XPageScrollerCG.BACKWARD][firstPage ? 1 : 0];
        Utils.printClickability(d, sb, "" + (value - extent), backEnabled, sb.getShowAsFormComponent());
        d.print(">");
        writeIcon(d, icon2, msie);
        d.print("</td>\n");

        d.print("</tr></tbody></table></td>")
                .print("<td><table class=\"pages\"><tbody><tr>");

        int firstDirectPage = sb.getCurrentPage() - (sb.getDirectPages() - 1) / 2;
        firstDirectPage = Math.min(firstDirectPage, sb.getPageCount() - sb.getDirectPages());
        firstDirectPage = Math.max(firstDirectPage, 0);

        int count = Math.min(sb.getDirectPages(), sb.getPageCount() - firstDirectPage);
        for (int i = 0; i < count; i++) {
            int page = firstDirectPage + i;
            d.print("<td valign=\"top\"");
            boolean isCurrentPage = (sb.getCurrentPage() == page);
            Utils.optAttribute(d, "class", isCurrentPage ? "page_selected": null);
            Utils.printClickability(d, sb, String.valueOf(page * sb.getExtent()), !isCurrentPage, sb.getShowAsFormComponent());
            d.print(">");
            d.print(Integer.toString(page + 1));
            d.print("</td>");
        }

        d.print("</tr></tbody></table></td>")
                .print("<td><table class=\"buttons\"><tbody><tr>");

        d.print("<td valign=\"top\"");
        SIcon icon1 = XPageScrollerCG.DEFAULT_ICONS[SConstants.HORIZONTAL][XPageScrollerCG.FORWARD][lastPage ? 1 : 0];
        Utils.printClickability(d, sb, "" + (value + extent), forwardEnabled, sb.getShowAsFormComponent());
        d.print(">");
        writeIcon(d, icon1, msie);
        d.print("</td>\n");

        d.print("<td valign=\"top\"");
        SIcon icon = XPageScrollerCG.DEFAULT_ICONS[SConstants.HORIZONTAL][XPageScrollerCG.LAST][lastPage ? 1 : 0];
        String event = "" + (sb.getPageCount() - 1) * extent;
        Utils.printClickability(d, sb, event, !lastPage, sb.getShowAsFormComponent());
        d.print(">");
        writeIcon(d, icon, msie);
        d.print("</td>\n");

        d.print("</tr></tbody></table></td>")
                .print("</tr></tbody></table>");
    }

    private void writeIcon(Device device, SIcon icon, boolean ie) throws IOException {
        device.print("<img");
        if (ie && icon.getURL().toString().endsWith(".png") && icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
            Utils.optAttribute(device, "style", "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + icon.getURL() + "', sizingMethod='scale')");
            Utils.optAttribute(device, "src", XPageScrollerCG.TRANSPARENT_ICON.getURL());
        }
        else
            Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());
        Utils.attribute(device, "alt", icon.getIconTitle());
        device.print("/>");
    }
}