/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SDefaultListCellRenderer
        extends SLabel
        implements SListCellRenderer {

    /**
     * Style class to use for the foreground for selected nodes.
     */
    protected String selectionStyle = null;

    /**
     * Style class to use for the foreground for non-selected nodes.
     */
    protected String nonSelectionStyle = null;

    /**
     * Create a SDefaultListCellRenderer with default properties.
     */
    public SDefaultListCellRenderer() {}


    /**
     * Sets the style the cell is drawn with when the cell is selected.
     */
    public void setSelectionStyle(String newStyle) {
        selectionStyle = newStyle;
    }

    /**
     * Returns the style the cell is drawn with when the cell is selected.
     */
    public String getSelectionStyle() {
        return selectionStyle;
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


    public SComponent getListCellRendererComponent(SComponent list,
                                                   Object value,
                                                   boolean selected,
                                                   int index) {
        setText(null);
        setIcon(null);

        if (value == null)
            setText("");
        else if (value instanceof SIcon)
            setIcon((SIcon) value);
        else if (value instanceof SComponent) {
            SComponent result = (SComponent) value;

            if (selected && selectionStyle != null) {
                result.setStyle(selectionStyle);
            } else {
                result.setStyle(nonSelectionStyle);
            }

            return result;
        } else {
            setText(value.toString());
        }

        if (selected && selectionStyle != null) {
            setStyle(selectionStyle);
        } else {
            setStyle(nonSelectionStyle);
        }

        return this;
    }
}


