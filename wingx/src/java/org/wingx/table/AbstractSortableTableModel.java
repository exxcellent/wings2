/*
 *
 * (c) Copyright 2004 con:cern development team.
 *
 * This file is part of con:cern (http://concern.org).
 *
 * con:cern is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wingx.table;

import javax.swing.table.AbstractTableModel;
import java.util.*;

/**
 * @author hengels
 */
public abstract class AbstractSortableTableModel
    extends AbstractTableModel
    implements SortableTableModel
{
    Map sorts = new HashMap();

    public int getSort(int col) {
        Integer order = (Integer)sorts.get(new Integer(col));
        if (order == null) {
            order = new Integer(SORT_NONE);
            sorts.put(new Integer(col), order);
        }
        return order.intValue();
    }

    public void setSort(int col, int order) {
        sorts.put(new Integer(col), new Integer(order));
    }
}
