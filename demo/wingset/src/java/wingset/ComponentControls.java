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
import org.wings.style.CSSProperty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hengels
 * @version $Revision$
 */
public class ComponentControls
        extends SToolbar {
    protected final List components = new LinkedList();
    protected final STextField widthTextField;
    protected final STextField heightTextField;
    protected final SButton applyButton;

    public ComponentControls() {
        setAttribute(CSSProperty.BORDER_BOTTOM,"1px solid #cccccc");
        applyButton = new SButton("apply");
        applyButton.setActionCommand("apply");
        widthTextField = new STextField();
        heightTextField = new STextField();

        add(applyButton);
        add(new SLabel("<html>&nbsp;&nbsp;&nbsp;width&nbsp;"));
        add(widthTextField);
        add(new SLabel("<html>&nbsp;&nbsp;&nbsp;height&nbsp;"));
        add(heightTextField);

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SDimension preferredSize = new SDimension();
                String width = widthTextField.getText();
                if (width != null && width.length() > 0) {
                    int widthInt;
                    try {
                        widthInt = Integer.parseInt(width);
                    } catch (NumberFormatException nfe) {
                        widthInt = Integer.MIN_VALUE;
                    }
                    if (widthInt == Integer.MIN_VALUE) {
                        preferredSize.setWidth(width);
                    } else {
                        preferredSize.setWidth(widthInt);
                    }
                }
                String height = heightTextField.getText();
                if (height != null && height.length() > 0) {
                    int heightInt;
                    try {
                        heightInt = Integer.parseInt(height);
                    } catch (NumberFormatException nfe) {
                        heightInt = Integer.MIN_VALUE;
                    }
                    if (heightInt == Integer.MIN_VALUE) {
                        preferredSize.setHeight(height);
                    } else {
                        preferredSize.setHeight(heightInt);
                    }
                }
                for (Iterator iterator = components.iterator(); iterator.hasNext();) {
                    SComponent component = (SComponent) iterator.next();
                    component.setPreferredSize(preferredSize);
                }
            }
        });
    }

    public void addSizable(SComponent component) {
        components.add(component);
    }

    protected void addActionListener(ActionListener actionListener) {
        applyButton.addActionListener(actionListener);
    }
}
