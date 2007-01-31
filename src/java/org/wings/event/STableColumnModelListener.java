package org.wings.event;

import java.util.EventListener;

import javax.swing.event.*;

public interface STableColumnModelListener extends EventListener {
    /** Tells listeners that a column was added to the model. */
    void columnAdded(STableColumnModelEvent e);

    /** Tells listeners that a column was removed from the model. */
    void columnRemoved(STableColumnModelEvent e);

    /** Tells listeners that a column was repositioned. */
    void columnMoved(STableColumnModelEvent e);

    /** Tells listeners that a column was moved due to a margin change. */
    void columnMarginChanged(ChangeEvent e);

    /** Tells listeners that a column was shown */
    void columnShown(ChangeEvent e);

    /** Tells listeners that a column was hidden */
    void columnHidden(ChangeEvent e);
}
