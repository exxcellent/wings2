package org.wingx.table;

import org.wings.table.SDefaultTableRowSelectionRenderer;
import org.wings.SComponent;
import org.wings.STable;
import org.wings.SLabel;
import org.wings.SResourceIcon;

/**
 * This Renderer is meant to be used in the selection column.
 * It will show a down arrow for selected rows and a right arrow for others.
 */
public class XTableDrilldownSelectionRenderer extends SDefaultTableRowSelectionRenderer {

    private SResourceIcon downIcon = new SResourceIcon("org/wings/icons/ScrollDown.gif");
    private SResourceIcon rightIcon = new SResourceIcon("org/wings/icons/ScrollRight.gif");

    public SComponent getTableCellRendererComponent(STable table, Object value, boolean isSelected, int row, int column) {
        SLabel label = new SLabel();
        if (isSelected) {
            label.setIcon(downIcon);
        } else {
            label.setIcon(rightIcon);
        }
        return label;
    }
}
