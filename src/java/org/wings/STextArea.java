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
package org.wings;

import org.wings.plaf.TextAreaCG;
import org.wings.plaf.TextAreaCG;

/**
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class STextArea
        extends STextComponent {
    private int rows = 5;

    private int columns = 20;

    /**
     * allowed parameters
     * are off(0), virtual(1), physical(2)
     * default value is off
     */
    private int lineWrap = SConstants.VIRTUAL_WRAP;


    public STextArea(String text) {
        super(text);
    }


    public STextArea() {
        super();
    }


    public void setRows(int r) {
        int oldRows = rows;
        rows = r;
        if (oldRows != rows)
            reload();
    }


    public int getRows() {
        return rows;
    }


    public void setColumns(int c) {
        int oldColumns = columns;
        columns = c;
        if (columns != oldColumns)
            reload();
    }


    public int getColumns() {
        return columns;
    }


    public void setLineWrap(int lw) {
        if (lw >= 0 && lw < 3)
            lineWrap = lw;
    }


    public int getLineWrap() {
        return lineWrap;
    }

    public void setCG(TextAreaCG cg) {
        super.setCG(cg);
    }
}


