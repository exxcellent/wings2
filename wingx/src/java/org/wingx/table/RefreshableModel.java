package org.wingx.table;

import javax.swing.table.TableModel;

/**
 * Created by IntelliJ IDEA.
 * User: hengels
 * Date: Jul 16, 2006
 * Time: 10:44:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RefreshableModel extends TableModel
{
    void refresh();
}
