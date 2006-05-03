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
import org.wings.border.*;
import org.wings.style.CSSProperty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hengels
 * @version $Revision$
 */
public class ComponentControls
    extends SPanel
{
    protected final List components = new LinkedList();

    protected SToolBar globalControls = new SToolBar();
    protected SToolBar localControls = new SToolBar();
    protected final STextField widthTextField = new STextField();
    protected final STextField heightTextField = new STextField();
    protected final STextField insetsTextField = new STextField();
    protected final SComboBox borderComboBox = new SComboBox(new String[] {
        "none",
        "raised",
        "lowered",
        "line",
        "grooved",
        "ridged",
        "titled",
    });
    protected final STextField borderThicknessTextField = new STextField();
    protected final SComboBox backgroundComboBox = new SComboBox(new String[] {
        "translucent",
        "yellow",
        "red",
        "green",
        "blue",
    });
    protected final SButton applyButton;

    SBorder[] borders = new SBorder[] {
        null,
        new SBevelBorder(SBevelBorder.RAISED, new Insets(5, 5, 5, 5)),
        new SBevelBorder(SBevelBorder.LOWERED, new Insets(5, 5, 5, 5)),
        new SLineBorder(2, new Insets(5, 5, 5, 5)),
        new SEtchedBorder(SEtchedBorder.LOWERED, new Insets(5, 5, 5, 5)),
        new SEtchedBorder(SEtchedBorder.RAISED, new Insets(5, 5, 5, 5)),
        new STitledBorder(new SEtchedBorder(SEtchedBorder.LOWERED, new Insets(5, 5, 5, 5)), "Border Title"),
    };

    Color[] backgrounds = new Color[] {
        null,
        Color.YELLOW,
        Color.RED,
        Color.GREEN,
        Color.BLUE,
    };

    public ComponentControls() {
        super(new SGridBagLayout());
        setAttribute(CSSProperty.BORDER_BOTTOM, "1px solid #cccccc");

        applyButton = new SButton("apply");
        applyButton.setActionCommand("apply");
        globalControls.setAttribute(CSSProperty.BORDER_LEFT, "1px solid #cccccc");
        localControls.setAttribute(CSSProperty.BORDER_TOP, "1px solid #cccccc");
        localControls.setAttribute(CSSProperty.BORDER_LEFT, "1px solid #cccccc");
        localControls.setVisible(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.gridheight = 2;
        add(applyButton, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        add(globalControls, c);
        add(localControls, c);

        widthTextField.setColumns(5);
        widthTextField.setToolTipText("length with unit (200px)");
        heightTextField.setColumns(5);
        heightTextField.setToolTipText("length with unit (200px)");
        insetsTextField.setColumns(2);
        insetsTextField.setToolTipText("length only");
        borderThicknessTextField.setColumns(2);
        borderThicknessTextField.setToolTipText("length only");

        globalControls.add(new SLabel("<html>width&nbsp;"));
        globalControls.add(widthTextField);
        globalControls.add(new SLabel("<html>&nbsp;&nbsp;&nbsp;height&nbsp;"));
        globalControls.add(heightTextField);
        globalControls.add(new SLabel("<html>&nbsp;&nbsp;&nbsp;insets&nbsp;"));
        globalControls.add(insetsTextField);
        globalControls.add(new SLabel("<html>&nbsp;&nbsp;&nbsp;border&nbsp;"));
        globalControls.add(borderComboBox);
        globalControls.add(borderThicknessTextField);
        globalControls.add(new SLabel("<html>&nbsp;&nbsp;&nbsp;background&nbsp;"));
        globalControls.add(backgroundComboBox);

        addActionListener(new wingset.SerializableActionListener() {
            public void actionPerformed(ActionEvent event) {
                SDimension preferredSize = new SDimension();
                preferredSize.setWidth(widthTextField.getText());
                preferredSize.setHeight(heightTextField.getText());

                int insets = 0;
                try {
                    insets = Integer.parseInt(borderThicknessTextField.getText());
                }
                catch (NumberFormatException e) {}

                int borderThickness = 1;
                try {
                    borderThickness = Integer.parseInt(borderThicknessTextField.getText());
                }
                catch (NumberFormatException e) {}

                SBorder border = borders[borderComboBox.getSelectedIndex()];
                if (border != null)
                    border.setThickness(borderThickness);

                Color background = backgrounds[backgroundComboBox.getSelectedIndex()];

                for (Iterator iterator = components.iterator(); iterator.hasNext();) {
                    SComponent component = (SComponent) iterator.next();
                    component.setPreferredSize(preferredSize);
                    component.setInsets(new Insets(insets, insets, insets, insets));
                    component.setBorder(border);
                    component.setBackground(background);
                }
            }
        });
    }

    public void addControl(SComponent component) {
        localControls.add(component);
        localControls.setVisible(true);
    }

    public void addSizable(SComponent component) {
        components.add(component);
    }

    protected void addActionListener(ActionListener actionListener) {
        applyButton.addActionListener(actionListener);
    }
}
