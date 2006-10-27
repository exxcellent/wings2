package org.wings.event;

import javax.swing.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: hengels
 * Date: Jul 14, 2006
 * Time: 12:08:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface STableColumnModelListener
{
    /** Tells listeners that a column was added to the model. */
    public void columnAdded(STableColumnModelEvent e);

    /** Tells listeners that a column was removed from the model. */
    public void columnRemoved(STableColumnModelEvent e);

    /** Tells listeners that a column was repositioned. */
    public void columnMoved(STableColumnModelEvent e);

    /** Tells listeners that a column was shown */
    public void columnShown(ChangeEvent e);

    /** Tells listeners that a column was hidden */
    public void columnHidden(ChangeEvent e);
}
