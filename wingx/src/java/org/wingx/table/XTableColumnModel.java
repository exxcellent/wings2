package org.wingx.table;

import org.wings.table.STableColumnModel;
import org.wings.table.STableColumn;

/**
 * Created by IntelliJ IDEA.
 * User: hengels
 * Date: Aug 22, 2006
 * Time: 3:52:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface XTableColumnModel extends STableColumnModel
{
    STableColumn getColumn(String identifier);
}
