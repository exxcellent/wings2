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
package org.wings;

import org.wings.util.SStringBuilder;



/**
 * Default implementation of a {@link SListCellRenderer}.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SDefaultListCellRenderer extends SLabel implements SListCellRenderer {

    private SStringBuilder nameBuffer = new SStringBuilder();
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
    public SDefaultListCellRenderer() {
    }


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


    public SComponent getListCellRendererComponent(SComponent list, Object value, boolean selected, int row) {
        setNameRaw(name(list, row));
        setText(null);
        setIcon(null);

        SComponent rendererComponent = this;
        if (value == null)
            setText("");
        else if (value instanceof SIcon)
            setIcon((SIcon) value);
        else if (value instanceof SComponent)
            rendererComponent = (SComponent)value;
        else
            setText(value.toString());

        if (selected && selectionStyle != null) {
            rendererComponent.setStyle(selectionStyle);
        } else {
            rendererComponent.setStyle(nonSelectionStyle);
        }

        return rendererComponent;
    }

    protected String name(SComponent component, int row) {
        nameBuffer.setLength(0);
        nameBuffer.append(component.getName()).append(SConstants.UID_DIVIDER).append(row);
        return nameBuffer.toString();
    }
}
