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
        extends WingSetPane {
    private int i = 1;

    private ComponentControls controls;

    public SComponent createExample() {
        controls = new ComponentControls();

        SPanel p = new SPanel(new SGridLayout(2));
        STextField textField = new STextField();
        textField.setName("textfield");
        p.add(createTextComponentExample(textField));

        STextArea textArea = new STextArea();
        textArea.setName("textarea");
        p.add(createTextComponentExample(textArea));

        SPanel f = new SPanel(new SBorderLayout());
        f.add(controls, SBorderLayout.NORTH);
        f.add(p, SBorderLayout.CENTER);
        return f;
    }

    SForm createTextComponentExample(STextComponent textComp) {
        SForm f = new SForm();

        SGridLayout layout = new SGridLayout(2);
        layout.setCellPadding(10);
        f.setLayout(layout);

        SLabel desc = new SLabel("Enter some text: ");
        SLabel resultDesc = new SLabel("Entered text: ");
        final SLabel result = new SLabel("");
        final SLabel buttonDesc = new SLabel("");

        textComp.addDocumentListener(new SDocumentListener() {
            public void insertUpdate(SDocumentEvent e) {
                result.setText(((STextComponent) e.getSource()).getText());
                buttonDesc.setText("button not pressed");
            }

            public void removeUpdate(SDocumentEvent e) {
                result.setText(((STextComponent) e.getSource()).getText());
                buttonDesc.setText("button not pressed");
            }

            public void changedUpdate(SDocumentEvent e) {
                result.setText(((STextComponent) e.getSource()).getText());
                buttonDesc.setText("button not pressed");
            }
        });

        SButton button = new SButton("Submit");
        button.setName("submit" + (i++));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonDesc.setText("Button pressed");
            }
        });

        f.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonDesc.setText(buttonDesc.getText() + " + form event");
            }
        });

        f.add(desc);
        f.add(textComp);
        f.add(resultDesc);
        f.add(result);
        f.add(button);
        f.add(buttonDesc);

        return f;
    }
}


