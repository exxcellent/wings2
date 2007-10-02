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

import javax.swing.table.TableModel;

/**
 * @author hengels
 */
public interface SortableTableModel
    extends TableModel, RefreshableModel
{
    public static final int SORT_NONE = 0;
    public static final int SORT_ASCENDING = 1;
    public static final int SORT_DESCENDING = 2;

    int getSort(int col);

    void setSort(int col, int order);
}
