package wingset;

import org.wings.*;
import org.wings.util.PropertyAccessor;
import org.wings.border.SLineBorder;
import org.wings.event.SMouseListener;
import org.wings.event.SMouseEvent;
import org.wings.plaf.css.TreeCG;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.*;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SplitPaneExample
    extends WingSetPane {
    private SplitPaneControls controls;
    private SSplitPane splitPane;

    protected SComponent createControls() {
        controls = new SplitPaneControls();
        return controls;
    }

    public SComponent createExample() {
        SLabel left = new SLabel("left");
        left.setPreferredSize(SDimension.FULLAREA);
        left.setBorder(new SLineBorder(1));
        SLabel right = new SLabel("right");
        right.setPreferredSize(SDimension.FULLAREA);
        right.setBorder(new SLineBorder(1));
        splitPane = new SSplitPane(SSplitPane.HORIZONTAL_SPLIT, left, right);
        splitPane.setPreferredSize(SDimension.FULLAREA);

        controls.addControllable(splitPane);
        return splitPane;
    }

    class SplitPaneControls
        extends ComponentControls {
        private final String[] SELECTION_MODES = new String[]{"single", "contiguous", "discontiguous"};
        private final Integer[] WIDTHS = new Integer[]{ new Integer(15), new Integer(20), new Integer(25), new Integer(30)};

        public SplitPaneControls() {
            /*
            widthTextField.setText("400px");
            borderColorComboBox.setSelectedItem(COLORS[1]);
            borderStyleComboBox.setSelectedItem(BORDERS[4]);
            borderThicknessTextField.setText("1");

            final SCheckBox consume = new SCheckBox("Consume events on leaves");
            consume.setToolTipText("<html>A SMouseListener will intercept the mouse clicks.<br>" +
                    "Consumed events will not be processed by the tree anymore");
            consume.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SplitPaneExample.this.consume = consume.isSelected();
                }
            });

            final SComboBox selectionMode = new SComboBox(SELECTION_MODES);
            //sync selectionMode with tree
            selectionMode.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (SELECTION_MODES[0].equals(selectionMode.getSelectedItem())) {
                        tree.getSelectionModel().setSelectionMode(STree.SINGLE_TREE_SELECTION);
                    }
                    else if (SELECTION_MODES[1].equals(selectionMode.getSelectedItem())) {
                        tree.getSelectionModel().setSelectionMode(STree.CONTIGUOUS_TREE_SELECTION);
                    }
                    else if (SELECTION_MODES[2].equals(selectionMode.getSelectedItem())) {
                        tree.getSelectionModel().setSelectionMode(STree.DISCONTIGUOUS_TREE_SELECTION);
                    }
                }
            });

            final SComboBox indentationWidth = new SComboBox(WIDTHS);
            // sync indentation width of tree with controller
            indentationWidth.setSelectedIndex(1); // set to 20px indent
            // now add the listener
            indentationWidth.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    tree.setNodeIndentDepth(((Integer) indentationWidth.getSelectedItem()).intValue());
                }
            });

            final SRadioButton plusButton = new SRadioButton("plus/minus");
            plusButton.setToolTipText("use [+] and [-] as expansion controls");

            final SRadioButton arrowButton = new SRadioButton("arrows");
            arrowButton.setToolTipText("use right-arrow and down-arrow as expansion controls");

            SButtonGroup group = new SButtonGroup();
            group.add(plusButton);
            group.add(arrowButton);
            plusButton.setSelected(true);

            group.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (plusButton.isSelected()) {
                        PropertyAccessor.setProperty(tree.getCG(), "collapseControlIcon", MINUS);
                        PropertyAccessor.setProperty(tree.getCG(), "expandControlIcon", PLUS);
                        PropertyAccessor.setProperty(tree.getCG(), "leafControlIcon", DOT);
                    } else {
                        PropertyAccessor.setProperty(tree.getCG(), "collapseControlIcon", ARROW_DOWN);
                        PropertyAccessor.setProperty(tree.getCG(), "expandControlIcon", ARROW_RIGHT);
                        PropertyAccessor.setProperty(tree.getCG(), "leafControlIcon", null);
                    }
                    tree.reload();
                }
            });

            final SCheckBox rootVisible = new SCheckBox("show root", true);
            rootVisible.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tree.setRootVisible(rootVisible.isSelected());
                }
            });

            addControl(consume);
            addControl(new SLabel(" selection mode"));
            addControl(selectionMode);
            addControl(new SLabel(" indentation width"));
            addControl(indentationWidth);
            addControl(new SLabel(" folding icons"));
            addControl(plusButton);
            addControl(arrowButton);
            addControl(new SLabel(" "));
            addControl(rootVisible);
            */
        }
    }
}
