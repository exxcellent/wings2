/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.table;

import javax.swing.CellEditor;

import org.wings.SComponent;
import org.wings.STable;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:holger.engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public interface STableCellEditor
    extends CellEditor
{
    SComponent getTableCellEditorComponent(STable table,
                                           Object value,
                                           boolean isSelected,
                                           int row,
                                           int column);
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
