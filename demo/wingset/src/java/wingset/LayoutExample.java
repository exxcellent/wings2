/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
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
public class LayoutExample
        extends SPanel
        implements SConstants {
    public LayoutExample() {
        add(createLayoutExample());

        add(new SSeparator());
        /*
        SHRef href =  new SHRef("View Source Code");
        href.setReference("/demo/wingset/" +
                          getClass().getName().substring(getClass().getName().indexOf('.') +1) + ".java");
        add(href);
        */
    }

    SPanel createLayoutExample() {
        SGridLayout layout = new SGridLayout(4);
        layout.setBorder(1);
        SPanel p = new SPanel(layout);

        p.add(createBorderLayoutExample());
        p.add(createGridLayoutExample());

        return p;
    }

    SPanel createBorderLayoutExample() {
        SPanel erg = new SPanel(new SFlowDownLayout());

        erg.add(new SLabel("SBorderLayout"));
        erg.add(new SSeparator());

        final SBorderLayout layout = new SBorderLayout();
        SPanel p = new SPanel(layout);

        SLabel south = new SLabel("SOUTH");
        south.setHorizontalAlignment(CENTER);
        p.add(south, "South");

        SLabel north = new SLabel("NORTH");
        north.setHorizontalAlignment(CENTER);
        p.add(north, "North");
        p.add(new SLabel("WEST"), "West");
        p.add(new SLabel("EAST"), "East");
        p.add(new SLabel("CENTER"), "Center");

        erg.add(p);
        erg.add(new SSeparator());

        final SCheckBox toggleBorder = new SCheckBox("Border");
        toggleBorder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layout.setBorder(toggleBorder.isSelected() == true ? 1 : 0);
            }
        });
        erg.add(toggleBorder);

        return erg;
    }

    SPanel createGridLayoutExample() {
        SPanel erg = new SPanel(new SFlowDownLayout());

        erg.add(new SLabel("SGridLayout"));
        erg.add(new SSeparator());

        final SGridLayout layout = new SGridLayout(3);
        SPanel p = new SPanel(layout);

        java.util.Random rand = new java.util.Random();
        p.add(new SLabel("Ein"));
        p.add(new SLabel("netter"));
        p.add(new SLabel("Spruch:"));
        p.add(new SLabel("Ein"));
        p.add(new SLabel("Anfaenger"));
        p.add(new SLabel("der"));
        p.add(new SLabel("Gitarre"));
        p.add(new SLabel("habe"));
        p.add(new SLabel("Eifer"));

        SPanel center = new SPanel(new SFlowLayout(CENTER));
        center.add(p);
        erg.add(center);

        erg.add(new SSeparator());

        final SCheckBox toggleBorder = new SCheckBox("Border");
        toggleBorder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                layout.setBorder(toggleBorder.isSelected() == true ? 1 : 0);
            }
        });
        erg.add(toggleBorder);

        return erg;
    }
}


