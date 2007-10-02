package org.wingx.treetable;

import javax.swing.tree.TreeModel;

import org.wingx.tree.XTreeNode;

/**
 * basic node class
 */
public abstract class DefaultTreeTableNode extends XTreeNode
    implements XTreeTableNode
{
    public DefaultTreeTableNode(Object userObject) {
        super(userObject);
    }

    public DefaultTreeTableNode(TreeModel treeModel, Object userObject, boolean allowsChildren) {
        super(treeModel, userObject, allowsChildren);
    }

    public Object getValueAt(int column) {
        return null;
    }

    public void setValueAt(Object value, int column) {
    }

    public Class getNodeClass() {
        return null;
    }

    public String toString() {
        return getUserObject().toString();
    }
}
