/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.table;

import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.STable;

/**
 * @author <a href="mailto:holger.engels@mercatis.de">Holger Engels</a>
 */
public class SDefaultTableCellRenderer
        extends SLabel
        implements STableCellRenderer
{
    SIcon editIcon;

    /**
     * The icon to display if the cell value is <code>null</code> .
     */
    public SIcon getEditIcon() {
        return editIcon;
    }

    /**
     * The icon to display if the cell value is <code>null</code> .
     * Can be any abritriary icon. <p/>Example:<br/>
     * <code>setEditIcon(getSession().getCGManager().getIcon("TableCG.editIcon"));</code>
     */
    public void setEditIcon(SIcon editIcon) {
        this.editIcon = editIcon;
    }

    public SComponent getTableCellRendererComponent(STable table,
                                                    Object value,
                                                    boolean selected,
                                                    int row,
                                                    int col) {
        setText(null);
        setIcon(null);

        if (value == null) {
            if (editIcon != null && table.isEditable() && table.isCellEditable(row, col))
                setIcon(editIcon);
        }
        else if (value instanceof SIcon)
            setIcon((SIcon)value);
        else if (value instanceof SComponent)
            return (SComponent)value;
        else
            setText(value.toString());

        return this;
    }
}
