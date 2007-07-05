/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package wingset;

import org.wings.*;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class ListExample
    extends WingSetPane
{
    private final static SResourceIcon javaCup = new SResourceIcon("org/wings/icons/JavaCup.gif");
    private final ListModel listModel = createListModel();
    private ComponentControls controls;
    private SList singleSelectionList;
    private SList multiSelectionList;
    private SComboBox comboBox;
    private SList anchorList;


    protected SComponent createControls() {
        controls = new ListControls();
        return controls;
    }

    public SComponent createExample() {
        SPanel panel = new SPanel(new SGridLayout(2, 2, 40, 40));
        panel.add(createListSingleSelExample());
        panel.add(createListMultSelExample());
        panel.add(createComboBoxExample());
        panel.add(createAnchorListExample());

        return panel;
    }

    public SContainer createListSingleSelExample() {
        SContainer cont = new SPanel(new SFlowDownLayout());
        cont.add(new SLabel("List with single selection"));
        cont.add(new SSpacer(1, 5));
        singleSelectionList = new SList();
        singleSelectionList.setName("single");
        singleSelectionList.setSelectionMode(SList.SINGLE_SELECTION);
        addListElements(singleSelectionList);
        cont.add(singleSelectionList);
        controls.addControllable(singleSelectionList);

        return cont;
    }

    public SContainer createListMultSelExample() {
        SContainer cont = new SPanel(new SFlowDownLayout());
        cont.add(new SLabel("List with multiple selection"));
        cont.add(new SSpacer(1, 5));
        SList multiSelectionList = new SList();
        this.multiSelectionList = multiSelectionList;
        this.multiSelectionList.setName("multiple");
        multiSelectionList.setSelectionMode(SList.MULTIPLE_SELECTION);
        addListElements(multiSelectionList);
        cont.add(multiSelectionList);
        controls.addControllable(multiSelectionList);

        return cont;
    }

    public SContainer createComboBoxExample() {
        SContainer cont = new SPanel(new SFlowDownLayout());
        cont.setVerticalAlignment(SConstants.TOP_ALIGN);
        cont.add(new SLabel("ComboBox"));
        cont.add(new SSpacer(1, 5));
        comboBox = new SComboBox();
        comboBox.setName("combo");
        addComboBoxElements(comboBox);
        cont.add(comboBox);
        controls.addControllable(comboBox);

        return cont;
    }

    public SContainer createAnchorListExample() {
        SContainer cont = new SPanel(new SFlowDownLayout());
        cont.add(new SLabel("AnchorList"));
        cont.add(new SSpacer(1, 5));
        SList anchorList = new SList();
        this.anchorList = anchorList;
        this.anchorList.setName("noform");
        anchorList.setShowAsFormComponent(false);
        anchorList.setSelectionMode(SList.SINGLE_SELECTION);
        addAnchorElements(anchorList);
        cont.add(anchorList);
        controls.addControllable(anchorList);

        return cont;
    }

    public void addListElements(SList list) {
        list.setListData(createElements());
    }

    public void addComboBoxElements(SComboBox comboBox) {
        comboBox.setModel(new DefaultComboBoxModel(createElements()));
    }

    public static Object[] createElements() {
        SLabel color = new SLabel("");
        color.setForeground(Color.green);
        color.setText(Color.green.toString());
        Object[] values = {
            "Element 1",
            color,
            "Element 3",
            new Date(),
            "Element 5"
        };

        return values;
    }

    public static ListModel createListModel() {
        final SLabel img = new SLabel("This element has an icon", javaCup);

        final SLabel color = new SLabel("");
        color.setForeground(Color.green);
        color.setText(Color.green.toString());

        ListModel listModel = new MyListModel(color, img);

        return listModel;
    }

    public void addAnchorElements(SList list) {
        list.setModel(listModel);
        list.setType(SList.ORDER_TYPE_NORMAL);
    }

    private final static class MyListModel implements ListModel, Serializable {
        private final Object[] values;

        public MyListModel(SLabel color, SLabel img) {
            values = new Object[] {
                "Element 1",
                color,
                "Element 3",
                new Date(),
                "Element 5",
                img
            };
        }

        public int getSize() {
            return values.length;
        }

        public Object getElementAt(int i) {
            return values[i];
        }

        public void addListDataListener(ListDataListener l) {
        }

        public void removeListDataListener(ListDataListener l) {
        }
    }

    class ListControls
        extends ComponentControls
    {
        public ListControls() {
            formComponentCheckBox.setVisible(false);

            final SCheckBox enabled = new SCheckBox("enabled");
            enabled.setSelected(true);
            enabled.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    singleSelectionList.setEnabled(enabled.isSelected());
                    multiSelectionList.setEnabled(enabled.isSelected());
                    comboBox.setEnabled(enabled.isSelected());
                    anchorList.setEnabled(enabled.isSelected());
                }
            });
            addControl(enabled);
        }
    }
}
