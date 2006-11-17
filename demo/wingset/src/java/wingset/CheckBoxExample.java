/*
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

import org.wings.ReloadManager;
import org.wings.SAbstractButton;
import org.wings.SBorderLayout;
import org.wings.SCheckBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SForm;
import org.wings.SGridLayout;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SURLIcon;
import org.wings.plaf.css.CheckBoxCG;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class CheckBoxExample
        extends WingSetPane
{
    final static int[] textHPos = new int[] {SConstants.LEFT, SConstants.CENTER, SConstants.RIGHT};
    final static int[] textVPos = new int[] {SConstants.TOP, SConstants.CENTER, SConstants.BOTTOM};

    static final SIcon sel = new SURLIcon("../icons/ComboBoxSelectedIcon.gif");
    static final SIcon nsel = new SURLIcon("../icons/ComboBoxIcon.gif");
    static final SIcon dissel = new SURLIcon("../icons/ComboBoxDisabledSelectedIcon.gif");
    static final SIcon disnsel = new SURLIcon("../icons/ComboBoxDisabledIcon.gif");
    static final SIcon rollsel = new SURLIcon("../icons/ComboBoxRolloverSelectedIcon.gif");
    static final SIcon rollnsel = new SURLIcon("../icons/ComboBoxRolloverIcon.gif");
    private ButtonControls controls;

    private final SLabel reportLabel = new SLabel("No button pressed");
    protected ActionListener action = new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            reportLabel.setText("<html>Button <b>'" + e.getActionCommand() + "'</b> pressed");
        }
    };

    protected SComponent createControls() {
        controls = new ButtonControls();
        return controls;
    }

    public SComponent createExample() {
        return createCheckBoxExample();
    }

    SContainer createCheckBoxExample() {
        SCheckBox[] buttons = new SCheckBox[9];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new SCheckBox("Text " + (i + 1));
            buttons[i].setActionCommand(buttons[i].getText());
            if (i != 4) {
                buttons[i].setIcon(nsel);
                buttons[i].setSelectedIcon(sel);
                buttons[i].setDisabledIcon(disnsel);
                buttons[i].setDisabledSelectedIcon(dissel);
                buttons[i].setRolloverIcon(rollnsel);
                buttons[i].setRolloverSelectedIcon(rollsel);
            }
            else {
                buttons[i].setIcon(null);
                buttons[i].setSelectedIcon(null);
                buttons[i].setDisabledIcon(null);
                buttons[i].setDisabledSelectedIcon(null);
                buttons[i].setRolloverIcon(null);
                buttons[i].setRolloverSelectedIcon(null);
            }
            buttons[i].setToolTipText("CheckBox " + (i+1));
            buttons[i].setName("button" + (i+1));
            buttons[i].setShowAsFormComponent(false);
            buttons[i].setVerticalTextPosition(textVPos[(i / 3)% 3]);
            buttons[i].setHorizontalTextPosition(textHPos[i % 3]);
            controls.addControllable(buttons[i]);
        }

        final SGridLayout grid = new SGridLayout(3);
        final SPanel buttonGrid = new SPanel(grid);
        grid.setBorder(1);
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(action);
            buttonGrid.add(buttons[i]);
        }

        SPanel panel = new SPanel(new SGridLayout(1));
        panel.add(buttonGrid);
        panel.add(reportLabel);

        return panel;
    }

    class ButtonControls extends ComponentControls {
        public ButtonControls() {
            final SCheckBox useImages = new SCheckBox("Show icons in form");
            final CheckBoxCG cg = (CheckBoxCG) getSession().getCGManager().getCG(SCheckBox.class);
            useImages.setSelected(cg.isUseIconsInForm());
            useImages.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cg.setUseIconsInForm(useImages.isSelected());
                    reload(ReloadManager.STATE);
                }
            });
            addControl(useImages);
        }
    }
}
