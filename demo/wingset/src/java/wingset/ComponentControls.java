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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hengels
 * @version $Revision$
 */
public class ComponentControls
        extends SPanel {
    protected List components = new LinkedList();
    protected STextField widthTextField;
    protected STextField heightTextField;
    private SButton button;

    public ComponentControls() {
        super(new SFlowLayout());

        button = new SButton("apply");
        widthTextField = new STextField();
        heightTextField = new STextField();

        add(button);
        add(new SLabel("<html>&nbsp;&nbsp;&nbsp;width&nbsp;"));
        add(widthTextField);
        add(new SLabel("<html>&nbsp;&nbsp;&nbsp;height&nbsp;"));
        add(heightTextField);

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SDimension preferredSize = new SDimension();
                String width = widthTextField.getText();
                if (width != null && width.length() > 0)
                    preferredSize.setWidth(width);
                String height = heightTextField.getText();
                if (height != null && height.length() > 0)
                    preferredSize.setHeight(height);

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
        button.addActionListener(actionListener);
    }
}
