/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package jsp;

import org.wings.tree.SDefaultTreeCellRenderer;
import org.wings.SComponent;
import org.wings.STree;
import org.wings.style.CSSProperty;

/**
 * @author hengels
 */
public class NavigationTreeCellRenderer
        extends SDefaultTreeCellRenderer
{
    public SComponent getTreeCellRendererComponent(STree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        setIcon(null);

        if (tree.getPathForRow(row).getPathCount() == 2) {
            String text = getText();
            if (text.startsWith("<html>"))
                text = "<html><b>" + text.substring("<html>".length()) + "</b>";
            else
                text = "<html><b>" + text + "</b>";
            setText(text);
        }

        return this;
    }
}
