// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.plaf.css;

import org.wings.style.CSSAttributeSet;
import org.wings.SConstants;

import java.awt.*;
import java.io.Serializable;

/**
 * A class holding styling attributes for one or more cells of (layout) tables.
 */
public final class TableCellStyle implements Serializable, Cloneable {

    /**
     * desired insets for this table cell.
     * Use {@link #getInsets()} 
     */
    private Insets insets = null;

    /**
     * render cells of first line as <code>TH</code>  element
     */
    public boolean renderAsTH = false;

    /**
     * Optional CSS class for this <code>TD</code> or <code>TH</code> cell
     */
    public String optionalStyleClass = null;

    /**
     * A set holding additional CSS property values that should be appliied to the cell.
     * Use {@link #getAdditionalCellStyles()}. 
     */
    private CSSAttributeSet additionalCellStyles = null;

    /**
     * Optional <code>TD</code>  cell width
     */
    public String width = null;

    /**
     * colspan attribute for this <code>TD</code> or <code>TH</code> cell
     */
    public int colspan = -1;

    /**
     * rowspan attribute for this <code>TD</code> or <code>TH</code> cell
     */
    public int rowspan = -1;


    /**
     * default in-cell <b>horizontal</b> alignment of inner component if component does not wear a orientation
     */
    public int defaultLayoutCellHAlignment = SConstants.NO_ALIGN;
    
    /**
     * default in-cell <b>vertical</b> alignment of inner component if component does not wear a orientation
     */
    public int defaultLayoutCellVAlignment = SConstants.NO_ALIGN;


    public TableCellStyle() {
    }

    public TableCellStyle makeACopy() {
        try {
            return (TableCellStyle) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public boolean hasAdditionalCellStyles() {
        return additionalCellStyles != null && !additionalCellStyles.isEmpty();
    }

    public CSSAttributeSet getAdditionalCellStyles() {
        if (additionalCellStyles == null)
            additionalCellStyles = new CSSAttributeSet();
        return additionalCellStyles;
    }


    public void setAdditionalCellStyles(CSSAttributeSet additionalCellStyles) {
        this.additionalCellStyles = additionalCellStyles;
    }

    public Insets getInsets() {
         if (insets == null)
            insets= new Insets(0, 0, 0, 0);
        return insets;
    }

    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    public boolean hasInsets() {
        return insets != null && (insets.top > 0 || insets.left > 0 || insets.right > 0 || insets.bottom > 0);
    }
}
