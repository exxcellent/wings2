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

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ToggleButtonExample
    extends WingSetPane
{
    static final ClassLoader cl = WingSet.class.getClassLoader();
    static final SIcon icon = 
        new SURLIcon("../icons/ButtonIcon.gif");
    static final SIcon disabledIcon = 
        new SURLIcon("../icons/ButtonDisabledIcon.gif");
    static final SIcon pressedIcon = 
        new SURLIcon("../icons/ButtonPressedIcon.gif");
    static final SIcon selectedIcon = 
        new SURLIcon("../icons/ButtonPressedIcon.gif");
    static final SIcon rolloverIcon = 
        new SURLIcon("../icons/ButtonRolloverIcon.gif");
    
    public SComponent createExample() {
        SPanel p = new SPanel(new SGridLayout(2));
        
        p.add(new SLabel("<html><h4>ToggleButtons outside forms in a button group</h4>"));
        p.add(new SLabel("<html><h4>Image toggleButtons outside forms</h4>"));
        p.add(createToggleButtonExample());
        p.add(createImageToggleButtonExample());
        
        SForm form = new SForm();
        form.add(new SLabel("<html><h4>ToggleButtons in a form in a button group</h4>"));
        form.add(createToggleButtonExample());
        p.add(form);
            
        form = new SForm();
        form.add(new SLabel("<html><h4>Image toggleButtons in a form</h4>"));
        form.add(createImageToggleButtonExample());
        p.add(form);
        return p;
    }

    SContainer createToggleButtonExample() {
        SPanel text = new SPanel();

        final SLabel pressed = new SLabel("no ToggleButton pressed");

        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pressed.setText("ToggleButton \"" + e.getActionCommand() + "\" pressed");
            }
        };

        SButtonGroup group = new SButtonGroup();
        for ( int i=0; i<3; i++ ) {
            SToggleButton b = new SToggleButton("text " + (i+1));
            b.setIcon(icon);
            b.setDisabledIcon(disabledIcon);
            b.setRolloverIcon(rolloverIcon);
            b.setPressedIcon(pressedIcon);
            b.setSelectedIcon(pressedIcon);
            b.setActionCommand(b.getText());
            // b.setBorder(new SLineBorder(1));
            b.addActionListener(action);
            group.add(b);
            text.add(b);
        }
        text.add(new SLabel("<html><br />"));
        text.add(pressed);

        return text;
    }

    SContainer createImageToggleButtonExample() {

        SButtonGroup group = new SButtonGroup();

        SToggleButton[] toggleButtons = new SToggleButton[9];
        toggleButtons[0] = new SToggleButton("testTL");
        toggleButtons[1] = new SToggleButton("testTC");
        toggleButtons[2] = new SToggleButton("testTR");
        toggleButtons[3] = new SToggleButton("testCL");
        toggleButtons[4] = new SToggleButton("testCC");
        toggleButtons[5] = new SToggleButton("testCR");
        toggleButtons[6] = new SToggleButton("testBL");
        toggleButtons[7] = new SToggleButton("testBC");
        toggleButtons[8] = new SToggleButton("testBR");

        for ( int i=0; i<toggleButtons.length; i++ ) {
            toggleButtons[i].setActionCommand(toggleButtons[i].getText());
            group.add(toggleButtons[i]);
            if ( i!=4 ) {
                toggleButtons[i].setIcon(icon);
                toggleButtons[i].setDisabledIcon(disabledIcon);
                toggleButtons[i].setRolloverIcon(rolloverIcon);
                toggleButtons[i].setPressedIcon(pressedIcon);
                toggleButtons[i].setSelectedIcon(pressedIcon);
                toggleButtons[i].setToolTipText("ToggleButton " + i);
            } 
            
        }


        toggleButtons[0].setVerticalTextPosition(SConstants.TOP);
        toggleButtons[0].setHorizontalTextPosition(SConstants.LEFT);

        toggleButtons[1].setVerticalTextPosition(SConstants.TOP);
        toggleButtons[1].setHorizontalTextPosition(SConstants.CENTER);

        toggleButtons[2].setVerticalTextPosition(SConstants.TOP);
        toggleButtons[2].setHorizontalTextPosition(SConstants.RIGHT);

        toggleButtons[3].setVerticalTextPosition(SConstants.CENTER);
        toggleButtons[3].setHorizontalTextPosition(SConstants.LEFT);

        /* Huh?
        toggleButtons[4].setVerticalTextPosition(SConstants.CENTER);
        toggleButtons[4].setHorizontalTextPosition(SConstants.CENTER);
        */
        toggleButtons[4].setIcon(null);

        toggleButtons[5].setVerticalTextPosition(SConstants.CENTER);
        toggleButtons[5].setHorizontalTextPosition(SConstants.RIGHT);

        toggleButtons[6].setVerticalTextPosition(SConstants.BOTTOM);
        toggleButtons[6].setHorizontalTextPosition(SConstants.LEFT);

        toggleButtons[7].setSelected(false);
        toggleButtons[7].setEnabled(false);
        toggleButtons[7].setVerticalTextPosition(SConstants.BOTTOM);
        toggleButtons[7].setHorizontalTextPosition(SConstants.CENTER);

        toggleButtons[8].setSelected(false);
        toggleButtons[8].setEnabled(false);
        toggleButtons[8].setVerticalTextPosition(SConstants.BOTTOM);
        toggleButtons[8].setHorizontalTextPosition(SConstants.RIGHT);

        SPanel erg = new SPanel();

        SGridLayout grid = new SGridLayout(3,3);
        grid.setBorder(1);
        SPanel b = new SPanel(grid);

        final SLabel pressed = new SLabel("no ToggleButton pressed");

        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pressed.setText("ToggleButton \"" + e.getActionCommand() + "\" pressed");
            }
        };

        for ( int i=0; i<toggleButtons.length; i++ ) {
            toggleButtons[i].addActionListener(action);
        }

        for ( int i=0; i<toggleButtons.length; i++ )
            b.add(toggleButtons[i]);

        erg.add(b);
        erg.add(new SLabel("<html><br />"));
        erg.add(pressed);

        return erg;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
