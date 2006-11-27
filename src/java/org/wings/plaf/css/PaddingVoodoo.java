// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.border.SBorder;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;
import org.wings.util.SStringBuilder;

import java.io.IOException;
import java.awt.*;

/**
 * This class collect various rendering workaround methods needed to fix issues with the Microsoft Internet Explorer.
 * It is primarily kept as separate utility class for <b>documentational and reference</b> purpose!
 * <p>
 * The methods call here should extensively document what they do and why to be maintainable.
 *
 * @author Benjamin Schmid <B.Schmid@exxcellent.de>
 */
public final class PaddingVoodoo
{
    private PaddingVoodoo() {
    }

    //
    //  MSIE TABLE Padding Voodooo section --------------------------------------------------------------------------------------
    //

    /**
     * @param component The component to inspect
     * @return <code>true</code> if the componentn has any padding insets requiring workaround
     */
    public static boolean hasPaddingInsets(final SComponent component) {
        if (component == null || component.getBorder() == null)
            return false;
        final Insets insets = component.getBorder().getInsets();
        return insets != null && (insets.top > 0 || insets.left >0 || insets.right > 0 || insets.bottom > 0);
    }

    /**
     * Utility method to add selected border's insets to the <code>targetInsets</code>.
     * @param border border containing insets
     * @param targetInsets Target inset to modify
     * @param firstRow if <code>true</code>, add top to top
     * @param firstCol if <code>true</code>, add left to left
     * @param lastCol if <code>true</code>, add right to right
     * @param lastRow if <code>true</code>, add bottom to bottom
     */
    public static void doBorderPaddingsWorkaround(final SBorder border, final Insets targetInsets,
                                            boolean firstRow, boolean firstCol, boolean lastCol, boolean lastRow) {
        if (border != null && border.getInsets() != null) {
            final Insets paddingInset = border.getInsets();
            if (firstRow)
                targetInsets.top += paddingInset.top;
            if (firstCol)
                targetInsets.left += paddingInset.left;
            if (lastCol)
                targetInsets.right+= paddingInset.right;
            if (lastRow)
                targetInsets.bottom+= paddingInset.bottom;
        }
    }


    /**
     * MSIE does not support PADDING on table elements.
     * For borders we re-render their insets (which are paddings) on the inner TD elements.
     * <p>
     * Call this method after <code>&lt;TD</code> and before <code>&gt;</code>
     * @param d target device
     * @param component source component
     */
    public static void doSimpleTablePaddingWorkaround(final Device d, final SComponent component) throws IOException {
        if (component == null)
            return;
        if (component.getBorder() != null && Utils.hasInsets(component.getBorder().getInsets())) {
            final SStringBuilder stringBuilder = new SStringBuilder();
            Utils.createInlineStylesForInsets(stringBuilder, component.getBorder().getInsets());
            Utils.optAttribute(d, "style", stringBuilder);
        }
    }

    //
    //  MSIE ????????????????? section --------------------------------------------------------------------------------------
    //

}
