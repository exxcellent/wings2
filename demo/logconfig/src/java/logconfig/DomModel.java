package logconfig;

import java.util.*;

import javax.swing.event.*;
import javax.swing.tree.*;

import org.dom4j.Node;

public class DomModel implements TreeModel {

	private static final String xpath = "(./* | ./@*)";

	private Vector listeners;
	private Node root;

	public DomModel(Node root) {
		this.root = root;
		listeners = new Vector();
	}

	public Object getRoot() {
		return root;
	}

	public Object getChild(Object parent, int index) {
		return ((Node) parent).selectSingleNode(xpath + "[" + (index + 1) + "]");
	}

	public int getChildCount(Object parent) {
		return ((Node) parent).selectNodes(xpath).size();
	}

	public int getIndexOfChild(Object parent, Object child) {
		return ((Node) parent).selectNodes(xpath).indexOf((Node) child);
	}

	public boolean isLeaf(Object node) {
		if (getChildCount(node) > 0) return false;
		return true;
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		// do nothing!
	}

	public void addTreeModelListener(TreeModelListener tml) {
		if (tml != null && !listeners.contains(tml)) {
			listeners.add(tml);
		}
	}

	public void removeTreeModelListener(TreeModelListener tml) {
		if (tml != null) {
			listeners.remove(tml);
		}
	}

	public void fireTreeNodesChanged(TreeModelEvent tme) {
		Iterator i = listeners.iterator();
		while (i.hasNext()) {
			TreeModelListener tml = (TreeModelListener) i.next();
			tml.treeNodesChanged(tme);
		}
	}

	public void fireTreeNodesInserted(TreeModelEvent tme) {
		Iterator i = listeners.iterator();
		while (i.hasNext()) {
			TreeModelListener tml = (TreeModelListener) i.next();
			tml.treeNodesInserted(tme);
		}
	}

	public void fireTreeNodesRemoved(TreeModelEvent tme) {
		Iterator i = listeners.iterator();
		while (i.hasNext()) {
			TreeModelListener tml = (TreeModelListener) i.next();
			tml.treeNodesRemoved(tme);
		}
	}

	public void fireTreeStructureChanged(TreeModelEvent tme) {
		Iterator i = listeners.iterator();
		while (i.hasNext()) {
			TreeModelListener tml = (TreeModelListener) i.next();
			tml.treeStructureChanged(tme);
		}
	}

}