package org.wings.plaf.css.msie;

import org.wings.plaf.css.AbstractLabelCG;
import org.wings.plaf.css.IconTextCompound;
import org.wings.plaf.CGManager;
import org.wings.session.SessionManager;
import org.wings.SComponent;
import org.wings.SLabel;
import org.wings.SIcon;
import org.wings.SConstants;
import org.wings.io.Device;

import java.io.IOException;
import java.awt.*;

/**
 * CG for SLabel instances.
 *
 * @author <a href="mailto:B.Schmid@eXXcellent.de">Benjamin Schmid</a>
 */
public final class LabelCG extends org.wings.plaf.css.LabelCG implements org.wings.plaf.LabelCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device, final SComponent component)
            throws IOException {
        final SLabel label = (SLabel) component;
        final String text = label.getText();
        final SIcon icon = label.isEnabled() ? label.getIcon() : label.getDisabledIcon();
        final int horizontalTextPosition = label.getHorizontalTextPosition();
        final int verticalTextPosition = label.getVerticalTextPosition();
        final boolean wordWrap = label.isWordWrap();

        if (icon == null && text != null) {
            writeTablePrefix(device, component);
            writeText(device, text, wordWrap);
            writeTableSuffix(device, component);
        }
        else if (icon != null && text == null) {
            writeTablePrefix(device, component);
            writeIcon(device, icon);
            writeTableSuffix(device, component);
        }
        else if (icon != null && text != null) {
            new IconTextCompound() {
                protected void text(Device d) throws IOException {
                    writeText(d, text, wordWrap);
                }
                protected void icon(Device d) throws IOException {
                    writeIcon(d, icon);
                }

                protected void tableAttributes(Device d) throws IOException {
                    writeAllAttributes(d, label);
                }

                public void writeCompound(Device device, SComponent component, int horizontalTextPosition, int verticalTextPosition, boolean writeAllAttributes) throws IOException {
                    final Insets insets = component.getBorder().getInsets();
                    if (insets == null || insets.top == 0 && insets.left == 0 && insets.right == 0 && insets.bottom == 0)
                        super.writeCompound(device, component, horizontalTextPosition, verticalTextPosition, writeAllAttributes);
                    else {
                        if (horizontalTextPosition == SConstants.NO_ALIGN)
                            horizontalTextPosition = SConstants.RIGHT;
                        if (verticalTextPosition == SConstants.NO_ALIGN)
                            verticalTextPosition = SConstants.CENTER;
                        if (verticalTextPosition == SConstants.CENTER && horizontalTextPosition == SConstants.CENTER)
                            horizontalTextPosition = SConstants.RIGHT;

                        final boolean renderTextFirst = verticalTextPosition == SConstants.TOP ||
                            (verticalTextPosition == SConstants.CENTER && horizontalTextPosition == SConstants.LEFT);

                        device.print("<table");
                        tableAttributes(device);
                        device.print(">");

                        if (verticalTextPosition == SConstants.TOP && horizontalTextPosition == SConstants.LEFT ||
                            verticalTextPosition == SConstants.BOTTOM && horizontalTextPosition == SConstants.RIGHT) {
                            device.print("<tr><td align=\"left\" valign=\"top\" style=\"padding-left:");
                            device.print(insets.left);
                            device.print("px; padding-top:");
                            device.print(insets.top);
                            device.print("px\">");
                            first(device, renderTextFirst);
                            device.print("</td><td></td></tr>");
                            device.print("<tr><td></td><td align=\"right\" valign=\"bottom\">");
                            last(device, renderTextFirst);
                            device.print("</td></tr>");
                        } else if (verticalTextPosition == SConstants.TOP && horizontalTextPosition == SConstants.RIGHT ||
                            verticalTextPosition == SConstants.BOTTOM && horizontalTextPosition == SConstants.LEFT) {
                            device.print("<tr><td></td><td align=\"right\" valign=\"top\">");
                            first(device, renderTextFirst);
                            device.print("</td></tr><tr><td align=\"left\" valign=\"bottom\">");
                            last(device, renderTextFirst);
                            device.print("</td><td></td></tr>");
                        } else if (verticalTextPosition == SConstants.TOP && horizontalTextPosition == SConstants.CENTER ||
                            verticalTextPosition == SConstants.BOTTOM && horizontalTextPosition == SConstants.CENTER) {
                            device.print("<tr><td align=\"center\" valign=\"top\">");
                            first(device, renderTextFirst);
                            device.print("</td></tr><tr><td align=\"center\" valign=\"bottom\">");
                            last(device, renderTextFirst);
                            device.print("</td></tr>");
                        } else if (verticalTextPosition == SConstants.CENTER && horizontalTextPosition == SConstants.LEFT ||
                            verticalTextPosition == SConstants.CENTER && horizontalTextPosition == SConstants.RIGHT) {
                            device.print("<tr><td align=\"left\">");
                            first(device, renderTextFirst);
                            device.print("</td><td align=\"right\">");
                            last(device, renderTextFirst);
                            device.print("</td></tr>");
                        } else {
                            log.warn("horizontal = " + horizontalTextPosition);
                            log.warn("vertical = " + verticalTextPosition);
                        }
                        device.print("</table>");
                    }
                }
            }.writeCompound(device, component, horizontalTextPosition, verticalTextPosition, true);
        }
    }
}
