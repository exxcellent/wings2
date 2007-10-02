package org.wingx.tree;

import org.wingx.tree.LazyNode;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 * basic node class
 */
public abstract class XTreeNode
    extends DefaultMutableTreeNode
    implements LazyNode
{

    private TreeModel treeModel;

    /**
     * Creates a tree node with no parent, no children, initialized with
     * the specified user object. For default the XTreeTableNode doesn't
     * allow children.
     *
     * @param userObject     an Object provided by the user that constitutes
     *                       the node's data
     */
    protected XTreeNode(Object userObject) {
        super(userObject);
    }

    /**
     * construct the node with the tree model,
     * the tree model must always be passed in the constructor,
     * each node knows it's model from the start.
     *
     * @param treeModel
     */
    public XTreeNode(TreeModel treeModel, Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);

        if (treeModel == null) {
            throw new NullPointerException("no tree model");
        }

        this.treeModel = treeModel;
    }

    /**
     * lazy initialization
     */
    public void initialize() {
        if (!initialized) {
            doInitialize();
            initialized = true;
        }
    }

    private boolean initialized;

    /**
     * do the initialization
     */
    protected abstract void doInitialize();

    /**
     * reinitialize
     */
    public void reinitialize() {
        removeAllChildrenNotify();
        doInitialize();
        initialized = true;
    }
    
    /**
     * @return Returns the initialized.
     */
    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * return the tree model
     *
     * @return
     */
    public TreeModel getTreeModel() {
        XTreeNode ancestor = (XTreeNode) getParent();
        while (treeModel == null && ancestor != null) {
            treeModel = ancestor.getTreeModel();
        }
        return treeModel;
    }

    /**
     * Set the tree model as tree model of current
     * node.
     *
     * @param treeModel The tree model of the whole
     *                  tree.
     */
    public void setTreeModel(TreeModel treeModel) {
        this.treeModel = treeModel;
    }

    /**
     * type cast the child ot an onode
     *
     * @param index
     * @return
     */
    public XTreeNode getNodeChildAt(int index) {
        return (XTreeNode) getChildAt(index);
    }

    /**
     * get the distance of this node from the root node
     * the children of the root node have a depth of 1.
     *
     * @return Returns the depth.
     */
    public int getDepth() {
        if (depth < 0) {
            XTreeNode parent = (XTreeNode) getParent();
            depth = (parent != null) ? parent.getDepth() + 1 : 0;
        }
        return this.depth;
    }

    private int depth = -1;

    /**
     * return the expanded state.
     * the expanded state is held in the model
     * because certain tree implementations perform
     * expand / collapse at the model level.
     *
     * @return Returns the expanded.
     */
    public boolean isExpanded() {
        return this.expanded;
    }

    /**
     * @param expanded The expanded to set.
     */
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    private boolean expanded;

    /**
     * insert the child node,
     * and notify the model if it is a default tree model
     *
     * @param newChild
     * @param index
     */
    public void insertNotify(MutableTreeNode newChild, int index) {
        if (getTreeModel() instanceof DefaultTreeModel) {
            DefaultTreeModel model = (DefaultTreeModel) getTreeModel();
            model.insertNodeInto(newChild, this, index);
        }
        else {
            insert(newChild, index);
        }
    }

    /**
     * remove all children
     */
    public void removeAllChildrenNotify() {
        if (getTreeModel() instanceof DefaultTreeModel) {
            DefaultTreeModel model = (DefaultTreeModel) getTreeModel();
            int count = getChildCount();
            for (int i = count - 1; i >= 0; i--) {
                model.removeNodeFromParent((MutableTreeNode) getChildAt(i));
            }
        }
        else {
            removeAllChildren();
        }
    }

    /**
     * add the child node,
     * and notify the model if it is a default tree model
     *
     * @param newChild
     * @param parent
     * @param index
     */
    public void addNotify(MutableTreeNode newChild) {
        insertNotify(newChild, getChildCount());
    }

    /**
     * initialise the nodes when they are expanded
     */
    public static class InitializationListener implements TreeExpansionListener {
        public void treeExpanded(TreeExpansionEvent event) {
            if (event.getPath() != null && event.getPath().getLastPathComponent() != null) {
                ((XTreeNode) event.getPath().getLastPathComponent()).initialize();
            }
        }

        public void treeCollapsed(TreeExpansionEvent event) {
        }
    }

}
