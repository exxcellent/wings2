/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package wingset;

import org.wings.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author hengels
 * @version $Revision$
 */
public class ComponentControls
    extends SPanel
{
    private List sizables = new LinkedList();
    private STextField widthTextField;
    private STextField heightTextField;
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

                for (Iterator iterator = sizables.iterator(); iterator.hasNext();) {
                    SComponent component = (SComponent)iterator.next();
                    component.setPreferredSize(preferredSize);
                }
            }
        });
    }

    public void addSizable(SComponent component) {
        sizables.add(component);
    }

    protected void addActionListener(ActionListener actionListener) {
        button.addActionListener(actionListener);
    }
}
