package logconfig;

import org.dom4j.Node;
import org.wings.*;
import org.wings.tree.SDefaultTreeCellRenderer;

public class DomRenderer extends SDefaultTreeCellRenderer {

	public SComponent getTreeCellRendererComponent(STree tree, Object value, boolean selected,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Node domNode = (Node) value;
		// String type = domNode.getNodeTypeName().substring(0, 1);
		StringBuffer output = new StringBuffer(" " + domNode.getName());
		if (domNode.getNodeType() == Node.ATTRIBUTE_NODE) {
			output.append("=\"" + domNode.getText() + "\"");
		}
		return new SLabel(output.toString());
	}

}