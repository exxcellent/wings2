package org.wingx.tree;

import org.wings.table.STableCellRenderer;
import org.wings.LowLevelEventListener;

/**
 * A cell renderer, that renders some editable component that allows for immediate user interaction.
 * The tree will catch the lowlevel events, forward them to this renderer and retrieve the value
 * in order to set it on the tree model.
 */
public interface EditableTreeCellRenderer
    extends STableCellRenderer, LowLevelEventListener
{
    public Object getValue();
}
