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
import org.wings.border.SBevelBorder;
import org.wings.border.SBorder;
import org.wings.border.SLineBorder;
import org.wings.border.STitledBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class BorderExample
    extends WingSetPane
{
    private static final SIcon WAIT_ICON = new SResourceIcon("org/wings/icons/Wait.gif");

    static final SBorder raised  = new SBevelBorder(SBevelBorder.RAISED);
    static final SBorder lowered = new SBevelBorder(SBevelBorder.LOWERED);
    static final SBorder line    = new SLineBorder(3);
    static final SBorder titled  = new STitledBorder("This is a title ...");

    public SComponent createExample() {
        SPanel p1 = new SPanel();
        try {
            SLayoutManager layout1 = createResourceTemplate("/templates/BorderExample.thtml");
            p1.setLayout(layout1);
        }
        catch ( Exception e ) {
            p1.add(new SLabel("Sorry, can't find " + "/templates/BorderExample.thtml"
                + " in resources"));
        }
        SPanel p = p1;

        /*
         * border layout allows to set the color
         */
        final SPanel c = new SPanel(new SBorderLayout());
        c.setBackground(new java.awt.Color(180, 180, 255));
        final SLabel borderLabel;
        borderLabel = new SLabel(WAIT_ICON);
        c.add(borderLabel);
        p.add(c, "BorderExample");

        SPanel buttons = new SPanel();
        buttons.setLayout(new SGridLayout(2, 2));
        SButtonGroup group = new SButtonGroup();
        final SRadioButton rb = new SRadioButton("Raised");
        group.add(rb); buttons.add(rb);
        final SRadioButton lb = new SRadioButton("Lowered");
        group.add(lb); buttons.add(lb);
        final SRadioButton lineb = new SRadioButton("Line");
        group.add(lineb); buttons.add(lineb);
        final SRadioButton eb = new SRadioButton("Title");
        group.add(eb); buttons.add(eb);
        
        group.addActionListener (new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SButtonGroup g = (SButtonGroup) e.getSource();
                    Object button = g.getSelection();
                    if (button == rb) {
                        borderLabel.setText("Raised Border");
                        c.setBorder(raised);
                    }
                    else if (button == lb) {
                        borderLabel.setText("Lowered Border");
                        c.setBorder(lowered);
                    }
                    else if (button == lineb) {
                        borderLabel.setText("Line Border");
                        c.setBorder(line);
                    }
                    else if (button == eb) {
                        borderLabel.setText("Titled Border");
                        c.setBorder(titled);
                    }
                }
            });

        rb.setSelected(true); // default: raised
        p.add(buttons, "Modifier");

        return p;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
