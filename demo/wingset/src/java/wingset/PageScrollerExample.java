/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of the wingS demo (http://wings.mercatis.de).
 *
 * The wingS demo is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package wingset;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class PageScrollerExample
    extends WingSetPane
{

    SList list;
    SPageScroller scrollbar;
    SScrollPane scroller;

    public SComponent createExample() {
        SPanel p = new SPanel(new SBorderLayout());

        /*        SLabel label = new SLabel("<html><h4>List in a PageScroller</h4>");
        p.add(label, SBorderLayout.NORTH);
        */

        list = new SList(listData);
        list.setVisibleRowCount(8);

        scrollbar = new SPageScroller(Adjustable.VERTICAL);
        scrollbar.setName("scrollbar");

        // its a horizontal scrollbar, but scrolls vertical...
        scrollbar.setLayoutMode(Adjustable.HORIZONTAL);
        scrollbar.setDirectPages(10);

        scroller = new SScrollPane(list);
        scroller.setHorizontalScrollBar(null);
        scroller.setVerticalScrollBar(null);
        // maximum of 50 visible rows...
        scroller.setVerticalExtent(50);

        p.add(scroller, SBorderLayout.CENTER);

        p.add(createControlForm(), SBorderLayout.NORTH);

        return p;
    }

    private SForm createControlForm() {
        SForm controlForm = new SForm(new SFlowLayout());
        
        controlForm.add(new SLabel("Visible Rows: "));
        Object[] visRowsValues = {new Integer(4), new Integer(8), new Integer(12), 
                           new Integer(16), new Integer(20), new Integer(50)};
        final SComboBox visRows = new SComboBox(visRowsValues);
        visRows.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    list.setVisibleRowCount(((Integer)visRows.getSelectedItem()).intValue());
                }
            });
        visRows.setSelectedItem(new Integer(list.getVisibleRowCount()));
        controlForm.add(visRows);

        controlForm.add(new SLabel("Direct Pages: "));
        Object[] values = {new Integer(5), new Integer(10), new Integer(15), 
                           new Integer(20), new Integer(50)};
        final SComboBox comboBox = new SComboBox(values);
        comboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    scrollbar.setDirectPages(((Integer) comboBox.getSelectedItem()).intValue());
                }
            });
        comboBox.setSelectedItem(new Integer(scrollbar.getDirectPages()));
        controlForm.add(comboBox);


        controlForm.add(new SLabel("Layout: "));
        Object[] constraints = { "Top", "Left", "Bottom", "Right" };
        final SComboBox layout = new SComboBox(constraints);
        layout.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if ( "Top".equals(layout.getSelectedItem()) ) {
                        scrollbar.setLayoutMode(Adjustable.HORIZONTAL);
                        scroller.setHorizontalScrollBar(scrollbar, 
                                                        SBorderLayout.NORTH);
                    } else if ( "Bottom".equals(layout.getSelectedItem()) ) {
                        scrollbar.setLayoutMode(Adjustable.HORIZONTAL);
                        scroller.setHorizontalScrollBar(scrollbar, 
                                                        SBorderLayout.SOUTH);
                    } else if ( "Left".equals(layout.getSelectedItem()) ) {
                        scrollbar.setLayoutMode(Adjustable.VERTICAL);
                        scroller.setHorizontalScrollBar(scrollbar, 
                                                        SBorderLayout.WEST);
                    } else if ( "Right".equals(layout.getSelectedItem()) ) {
                        scrollbar.setLayoutMode(Adjustable.VERTICAL);
                        scroller.setHorizontalScrollBar(scrollbar, 
                                                        SBorderLayout.EAST);
                    } 
                }
            });
        layout.setSelectedItem("Bottom");
        controlForm.add(layout);


        final SCheckBox margin = new SCheckBox("Margin:");
        margin.setHorizontalTextPosition(SCheckBox.LEFT);
        margin.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    scrollbar.setMarginVisible(margin.isSelected());
                }
            });
        margin.setSelected(scrollbar.isMarginVisible());
        controlForm.add(margin);

        final SCheckBox step = new SCheckBox("Step:");
        step.setHorizontalTextPosition(SCheckBox.LEFT);
        step.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    scrollbar.setStepVisible(step.isSelected());
                }
            });
        step.setSelected(scrollbar.isStepVisible());
        controlForm.add(step);

        
        SButton submit = new SButton("OK");
        controlForm.add(submit);
        return controlForm;
    }

    static void addChildNodes(TreeNode node, ArrayList list, int indent) {
        if ( node!=null ) {
            StringBuffer name = new StringBuffer();
            for ( int i=0; i<indent; i++ ) {
                name.append(".");
            }
            name.append(node.toString());
            list.add(name.toString());
            for ( int i=0; i<node.getChildCount(); i++ ) {
                addChildNodes(node.getChildAt(i), list, indent+1);
            }
        }
    }

    static Object[] listData = createData();

    static Object[] createData() {
        TreeNode root = TreeExample.generateTree();

        ArrayList data = new ArrayList();
        
        addChildNodes(root, data, 0);

        return data.toArray();
    }


}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */


