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

import org.wings.*;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ListExample
    extends WingSetPane
{
    static final SResourceIcon javaCup = new SResourceIcon("org/wings/icons/JavaCup.gif");

    public SComponent createExample() {
        SForm form = new SForm(new SFlowDownLayout());

        SPanel p = new SPanel(new SGridLayout(2,2));
        p.add(createListSingleSelExample());
        p.add(createListMultSelExample());
        p.add(createComboBoxExample());
        p.add(createAnchorListExample());

        form.add(p);
        form.add(new SButton("SUBMIT"));
        return form;
    }

    public SContainer createListSingleSelExample() {
        SContainer cont = new SPanel(new SFlowDownLayout());
        cont.add(new SLabel("List with single selection"));
        SList list = new SList();
        list.setName("single");
        list.setSelectionMode(SINGLE_SELECTION);
        addListElements(list);
        cont.add(list);

        return cont;
    }

    public SContainer createListMultSelExample() {
        SContainer cont = new SPanel(new SFlowDownLayout());
        cont.add(new SLabel("List with multiple selection"));
        SList list = new SList();
        list.setName("multiple");
        list.setSelectionMode(MULTIPLE_SELECTION);
        addListElements(list);
        cont.add(list);

        return cont;
    }

    public SContainer createComboBoxExample() {
        SContainer cont = new SPanel(new SFlowDownLayout());
        cont.add(new SLabel("ComboBox"));
        SComboBox comboBox = new SComboBox();
        comboBox.setName("combo");
        addComboBoxElements(comboBox);
        cont.add(comboBox);
        
        return cont;
    }

    public SContainer createAnchorListExample() {
        SContainer cont = new SPanel(new SFlowDownLayout());
        cont.add(new SLabel("List with showAsFormComponent = false"));
        SList list = new SList();
        list.setName("noform");
        list.setShowAsFormComponent(false);
        list.setSelectionMode(SConstants.SINGLE_SELECTION);
        addAnchorElements(list);
        cont.add(list);

        return cont;
    }

    public void addListElements(SList list) {
        list.setListData(createElements());
    }

    public void addComboBoxElements(SComboBox comboBox) {
        comboBox.setModel(new DefaultComboBoxModel(createElements()));
    }

    public static  Object[] createElements() {
        SLabel color = new SLabel("");
        color.setForeground(Color.green);
        color.setText(Color.green.toString());

        Object[] values = {
            "element1",
            color,
            "element3",
            "element4"
        };

        return values;
    }

    public static  ListModel createListModel() {
        final SLabel img =
            new SLabel(javaCup);

        final SLabel color = new SLabel("");
        color.setForeground(Color.green);
        color.setText(Color.green.toString());

        ListModel listModel = new ListModel() {
            Object[] values = {
                "element1",
                color,
                img,
                "element4"
            };

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
        };

        return listModel;
    }

    private final ListModel listModel = createListModel();

    public void addAnchorElements(SList list) {
        final SLabel img =
            new SLabel(javaCup);

        final SLabel color = new SLabel("");
        color.setForeground(Color.green);
        color.setText(Color.green.toString());

        list.setModel(listModel);
        list.setType(ORDER_TYPE_NORMAL);
        // list.setStart(8);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
