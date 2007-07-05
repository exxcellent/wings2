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
import org.wings.STable;

import javax.swing.*;
import java.io.Serializable;

/**
 * @author <a href="mailto:holger.engels@mercatis.de">Holger Engels</a>
 */
public interface STableCellEditor extends CellEditor, Serializable {
    SComponent getTableCellEditorComponent(STable table,
                                           Object value,
                                           boolean selected,
                                           int row,
                                           int column);
}


