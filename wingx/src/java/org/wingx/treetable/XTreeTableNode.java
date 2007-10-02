package org.wingx.treetable;

import javax.swing.tree.MutableTreeNode;

/**
 * Created by IntelliJ IDEA.
 * User: hengels
 * Date: Aug 4, 2006
 * Time: 12:50:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface XTreeTableNode
    extends MutableTreeNode
{
    Object getValueAt(int column);
    void setValueAt(Object value, int column);

    Class getNodeClass();

    void setExpanded(boolean b);
    boolean isExpanded();

    int getDepth();
}
