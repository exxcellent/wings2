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
import org.wings.event.SDocumentListener;
import org.wings.event.SDocumentEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public class TextComponentExample
        extends WingSetPane
{
    private ComponentControls controls;
    private SLabel documentEvent = new SLabel();
    private SLabel actionEvent = new SLabel();

    SDocumentListener documentListener = new SDocumentListener() {
                public void insertUpdate(SDocumentEvent e) {
                    documentEvent.setText(((STextComponent) e.getSource()).getName());
                    actionEvent.setText("button not pressed");
                }

                public void removeUpdate(SDocumentEvent e) {
                    documentEvent.setText(((STextComponent) e.getSource()).getName());
                    actionEvent.setText("button not pressed");
                }

                public void changedUpdate(SDocumentEvent e) {
                    documentEvent.setText(((STextComponent) e.getSource()).getName());
                    actionEvent.setText("button not pressed");
                }
            };

    public SComponent createExample() {
        controls = new ComponentControls();

        SPanel p = new SPanel(new SGridLayout(2));

        p.add(new SLabel("STextField: "));
        STextField textField = new STextField();
        textField.setName("textfield");
        textField.addDocumentListener(documentListener);
        p.add(textField);

        p.add(new SLabel("STextArea: "));
        STextArea textArea = new STextArea();
        textArea.setName("textarea");
        textArea.addDocumentListener(documentListener);
        p.add(textArea);

        p.add(new SLabel("DocumentEvent: "));
        p.add(documentEvent);

        SButton button = new SButton("Submit");
        p.add(button);
        p.add(actionEvent);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionEvent.setText("Button pressed");
            }
        });

        SForm f = new SForm(new SBorderLayout());
        f.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionEvent.setText(actionEvent.getText() + " + form event");
            }
        });

        controls.addSizable(textField);
        controls.addSizable(textArea);
        
        f.add(controls, SBorderLayout.NORTH);
        f.add(p, SBorderLayout.CENTER);
        return f;
    }
}


