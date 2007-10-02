package org.wingx.table;

import org.wings.LowLevelEventListener;
import org.wings.STable;
import org.wings.table.STableCellRenderer;
import org.wingx.XTable;

/**
 * A cell renderer, that renders some editable component that allows for immediate user interaction.
 * The table will catch the lowlevel events, forward them to this renderer and retrieve the value
 * in order to set it on the table model.
 *
 * This is a preliminary approach, that will most probably be replaced with the standard cell editor
 * mechanism as soon as ajax makes its usability acceptable.
 */
public interface EditableTableCellRenderer extends STableCellRenderer {

    public abstract Object getValue();

    public abstract LowLevelEventListener getLowLevelEventListener(STable table, int row, int column);
}
