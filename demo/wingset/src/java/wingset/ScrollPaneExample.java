/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ScrollPaneExample
        extends WingSetPane {

    public SComponent createExample() {
        SForm p = new SForm();

        SLabel label = new SLabel("<html><h4>Table in a ScrollPane</h4>");
        p.add(label);

        STable table = new STable(new TableExample.ROTableModel(15, 15));
        table.setShowAsFormComponent(true);
        table.setDefaultRenderer(new TableExample.MyCellRenderer());

        final SScrollPane scroller = new SScrollPane(table);
        scroller.getHorizontalScrollBar().setBlockIncrement(3);
        scroller.getVerticalScrollBar().setBlockIncrement(3);

        final SCheckBox showAsFormComponent = new SCheckBox("Show as Form Component");
        showAsFormComponent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ((SScrollBar) scroller.getHorizontalScrollBar()).setShowAsFormComponent(showAsFormComponent.isSelected());
                ((SScrollBar) scroller.getVerticalScrollBar()).setShowAsFormComponent(showAsFormComponent.isSelected());
            }
        });
        showAsFormComponent.setShowAsFormComponent(false);
        ((SScrollBar) scroller.getHorizontalScrollBar()).setShowAsFormComponent(false);
        ((SScrollBar) scroller.getVerticalScrollBar()).setShowAsFormComponent(false);

        p.add(showAsFormComponent);
        p.add(scroller);
        return p;
    }

}


