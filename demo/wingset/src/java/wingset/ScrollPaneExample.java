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
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ScrollPaneExample
        extends WingSetPane
{
    private ScrollPaneControls controls;
    private STable table;
    private SScrollPane scroller;

    public SComponent createExample() {
        controls = new ScrollPaneControls();

        table = new STable(new TableExample.ROTableModel(15, 15));
        table.setShowAsFormComponent(true);
        table.setDefaultRenderer(new TableExample.MyCellRenderer());

        scroller = new SScrollPane(table);
        scroller.getHorizontalScrollBar().setBlockIncrement(3);
        scroller.getVerticalScrollBar().setBlockIncrement(3);

        ((SScrollBar) scroller.getHorizontalScrollBar()).setShowAsFormComponent(false);
        ((SScrollBar) scroller.getVerticalScrollBar()).setShowAsFormComponent(false);

        SForm p = new SForm(new SBorderLayout());
        p.add(controls, SBorderLayout.NORTH);
        p.add(scroller, SBorderLayout.CENTER);
        return p;
    }

    class ScrollPaneControls extends ComponentControls {
        public ScrollPaneControls () {
            final SCheckBox showAsFormComponent = new SCheckBox("<html>Show as Form Component&nbsp;&nbsp;&nbsp;");
            showAsFormComponent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean selected = showAsFormComponent.isSelected();
                    table.setShowAsFormComponent(selected);
                    ((SScrollBar)scroller.getHorizontalScrollBar()).setShowAsFormComponent( selected);
                    ((SScrollBar)scroller.getVerticalScrollBar()).setShowAsFormComponent(selected);
                }
            });

            add(showAsFormComponent);
        }
    }
}
