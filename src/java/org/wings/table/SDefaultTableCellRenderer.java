/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
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
import org.wings.io.SStringBuilder;

/**
 * @author <a href="mailto:holger.engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SDefaultTableCellRenderer
        extends SLabel
        implements STableCellRenderer
{
    SStringBuilder nameBuffer = new SStringBuilder();

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
        setNameRaw(name(table, row, col));
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

    /**
     * Generates the name (= id) of the editing component so that
     * the STable implementation knows to associate the input
     * value with the correct data row/columns
     *
     * @param component The edit component to rename
     * @param row Data row of this edit component
     * @param col Data column of this edit component
     * @return The unqique id/name for a component of the rows/column
     */
    protected String name(SComponent component, int row, int col) {
        nameBuffer.setLength(0);
        nameBuffer.append(component.getName()).append("_");
        if (row == -1)
            nameBuffer.append('h');
        else
            nameBuffer.append(row);
        nameBuffer.append("_").append(col);
        return nameBuffer.toString();
    }
}
