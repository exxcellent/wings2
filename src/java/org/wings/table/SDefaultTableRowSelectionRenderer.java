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
import org.wings.SLabel;
import org.wings.SResourceIcon;
import org.wings.STable;
import javax.swing.*;

/**
 * Renderer responsible for the row selection column in cases where the table
 * cannot distinguish clicks on cells as selection clicks or editing clicks.
 *
 * @author <a href="mailto:B.Schmid@eXXcellent.de">Benjamin Schmid</a>
 */
public class SDefaultTableRowSelectionRenderer extends SLabel implements STableCellRenderer {

    public static final SResourceIcon DEFAULT_MULTI_SELECTION_ICON =
            new SResourceIcon("org/wings/icons/SelectedCheckBox.gif");

    public static final SResourceIcon DEFAULT_MULTI_NOT_SELECTION_ICON =
            new SResourceIcon("org/wings/icons/NotSelectedCheckBox.gif");

    public static final SResourceIcon DEFAULT_SINGLE_SELECTION_ICON =
            new SResourceIcon("org/wings/icons/SelectedRadioButton.gif");

    public static final SResourceIcon DEFAULT_SINGLE_NOT_SELECTION_ICON =
            new SResourceIcon("org/wings/icons/NotSelectedRadioButton.gif");

    /**
     * Style to use for the foreground for non-selected nodes.
     */
    protected String nonSelectionStyle;

    /**
     * Style to use for the foreground for non-selected nodes.
     */
    protected String selectionStyle;

    protected SResourceIcon multiSelectionIcon = DEFAULT_MULTI_SELECTION_ICON;

    protected SResourceIcon multiNotSelectionIcon = DEFAULT_MULTI_NOT_SELECTION_ICON;

    protected SResourceIcon singleSelectionIcon = DEFAULT_SINGLE_SELECTION_ICON;

    protected SResourceIcon singleNotSelectionIcon = DEFAULT_SINGLE_NOT_SELECTION_ICON;

    protected boolean useIcons = false;

    public SDefaultTableRowSelectionRenderer() {
    }

    public SComponent getTableCellRendererComponent(STable table, Object value, boolean selected, int row, int col) {
        if (useIcons) {
            switch (table.getSelectionMode()) {
                case ListSelectionModel.SINGLE_SELECTION:
                    setIcon(selected ? singleSelectionIcon : singleNotSelectionIcon);
                    break;
                default:
                    setIcon(selected ? multiSelectionIcon : multiNotSelectionIcon);
                    break;
            }
        }
        else
            setText("" + (row + 1));
        // style
        if (selected) {
            setStyle(selectionStyle);
        } else {
            setStyle(nonSelectionStyle);
        }

        return this;
    }

    /**
     * Sets the style the cell is drawn with when the cell isn't selected.
     */
    public void setNonSelectionStyle(String newStyle) {
        nonSelectionStyle = newStyle;
    }

    /**
     * Returns the style the cell is drawn with when the cell isn't selected.
     */
    public String getNonSelectionStyle() {
        return nonSelectionStyle;
    }

    /**
     * Sets the style the cell is drawn with when the cell isn't selected.
     */
    public void setSelectionStyle(String newStyle) {
        selectionStyle = newStyle;
    }

    /**
     * Returns the style the cell is drawn with when the cell isn't selected.
     */
    public String getSelectionStyle() {
        return selectionStyle;
    }

    /**
     * @return Icon used for selected rows in multi-selection mode for selected lines
     */
    public SResourceIcon getMultiSelectionIcon() {
        return multiSelectionIcon;
    }

    /**
     * @param multiSelectionIcon Icon used for selected rows in multi-selection mode for selected lines
     */
    public void setMultiSelectionIcon(SResourceIcon multiSelectionIcon) {
        this.multiSelectionIcon = multiSelectionIcon;
    }

    /**
     * @return Icon used for selected rows in multi-selection mode for unselected lines
     */
    public SResourceIcon getMultiNotSelectionIcon() {
        return multiNotSelectionIcon;
    }

    /**
     * @param multiNotSelectionIcon Icon used for selected rows in multi-selection mode for unselected lines
     */
    public void setMultiNotSelectionIcon(SResourceIcon multiNotSelectionIcon) {
        this.multiNotSelectionIcon = multiNotSelectionIcon;
    }

    /**
     * @return Icon used for selected rows in single-selection mode tables for selected lines
     */
    public SResourceIcon getSingleSelectionIcon() {
        return singleSelectionIcon;
    }

    /**
     * @param singleSelectionIcon Icon used for selected rows in single-selection mode tables for selected lines
     */
    public void setSingleSelectionIcon(SResourceIcon singleSelectionIcon) {
        this.singleSelectionIcon = singleSelectionIcon;
    }

    /**
     * @return Icon used for selected rows in single-selection mode tables for unselected lines
     */
    public SResourceIcon getSingleNotSelectionIcon() {
        return singleNotSelectionIcon;
    }

    /**
     * @param singleNotSelectionIcon Icon used for selected rows in single-selection mode tables for unselected lines
     */
    public void setSingleNotSelectionIcon(SResourceIcon singleNotSelectionIcon) {
        this.singleNotSelectionIcon = singleNotSelectionIcon;
    }

}


