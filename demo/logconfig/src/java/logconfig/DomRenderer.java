package logconfig;

import org.dom4j.Node;
import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.STree;
import org.wings.SURLIcon;
import org.wings.tree.SDefaultTreeCellRenderer;

public class DomRenderer extends SDefaultTreeCellRenderer {

    private static final SURLIcon LEAF_ICON = new SURLIcon("../images/leaf_node.png");

    public SComponent getTreeCellRendererComponent(STree tree, Object value, boolean selected,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        Node domNode = (Node) value;
        // String type = domNode.getNodeTypeName().substring(0, 1);
        StringBuffer output = new StringBuffer(" ").append(domNode.getName());
        if (domNode.getNodeType() == Node.ATTRIBUTE_NODE) {
            output.append("=\"").append(domNode.getText()).append("\"");
        }
        setText(output.toString());
        setToolTipText(null);
        return this;
    }

    public SIcon getLeafIcon() {
        return LEAF_ICON;
    }

}