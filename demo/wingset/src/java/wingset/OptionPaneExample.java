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

import org.wings.SAnchor;
import org.wings.SBorderLayout;
import org.wings.SButton;
import org.wings.SComponent;
import org.wings.SFlowDownLayout;
import org.wings.SForm;
import org.wings.SLabel;
import org.wings.SOptionPane;
import org.wings.SPanel;
import org.wings.STextField;
import org.wings.SToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class OptionPaneExample
        extends WingSetPane
{
    protected SComponent createExample() {
        SToolBar toolBar = new SToolBar();

        SButton msg = new SButton("show Message");
        msg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showMessageDialog(null, "This is a simple message", "A Message");
            }
        });
        toolBar.add(msg);

        SButton question = new SButton("show Question");
        final ActionListener comment = new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == SOptionPane.OK_ACTION)
                    SOptionPane.showMessageDialog(null, "Fine !");
                else
                    SOptionPane.showMessageDialog(null, "No Problem, just look at another site");
            }
        };

        question.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showQuestionDialog(null, "Continue this example?",
                        "A Question", comment);
            }
        });
        toolBar.add(question);

        SButton yesno = new SButton("show Yes No");
        final ActionListener feedback = new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == SOptionPane.NO_ACTION) {
                    SPanel p = new SPanel(new SFlowDownLayout());
                    p.add(new SLabel("That's sad!"));
                    SAnchor sendMail = new SAnchor("mailto:haaf@mercatis.de");
                    sendMail.add(new SLabel("Please send my why!"));
                    p.add(sendMail);
                    SOptionPane.showMessageDialog(null, p);
                } else
                    SOptionPane.showMessageDialog(null, "Fine, so do we!");
            }
        };

        yesno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showYesNoDialog(null,
                        "Do you like wingS",
                        "A Yes No Question", feedback);
            }
        });

        toolBar.add(yesno);

        final SLabel label = new SLabel();
        final ActionListener inputListener = new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane optionPane = (SOptionPane) e.getSource();
                STextField inputValue = (STextField) optionPane.getInputValue();
                label.setText("" + inputValue.getText());
            }
        };

        SButton input = new SButton("show Input");
        input.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showInputDialog(null, "What's your profession?", "A Message", new STextField(), inputListener);
            }
        });
        toolBar.add(input);
        toolBar.add(label);

        SForm c = new SForm(new SBorderLayout());
        c.add(toolBar, SBorderLayout.NORTH);
        return c;
    }
}
