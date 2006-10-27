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
import org.wings.resource.ResourceManager;
import org.wings.border.SLineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class OptionPaneExample
        extends WingSetPane {
    protected SComponent createExample() {
        SToolBar toolBar = new SToolBar();
        SLineBorder border = new SLineBorder(Color.LIGHT_GRAY, 0);
        border.setThickness(1, SConstants.BOTTOM);
        toolBar.setBorder(border);
        toolBar.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        ((SBoxLayout) toolBar.getLayout()).setHgap(6);
        ((SBoxLayout) toolBar.getLayout()).setVgap(4);

        final SComboBox combo = new SComboBox(new String[]{"Erstens", "Zweitens", "Drittens"});

        SButton msg = new SButton("show Message");
        msg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showPlainMessageDialog(null, "This is a simple message", "A Message");
            }
        });
        toolBar.add(msg);

        SButton question = new SButton("show Question");
        final ActionListener comment = new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(SOptionPane.OK_ACTION)) {
                    SOptionPane.showPlainMessageDialog(null, "Fine !");
                }
                else {
                    SOptionPane.showPlainMessageDialog(null, "No Problem, just look at another site");
                }
            }
        };

        question.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showQuestionDialog(null, "Continue this example?", "A Question", comment);
            }
        });
        toolBar.add(question);

        SButton yesno = new SButton("show Yes No");
        final ActionListener feedback = new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(SOptionPane.NO_ACTION)) {
                    SPanel p = new SPanel(new SFlowDownLayout());
                    p.add(new SLabel("That's sad!"));
                    SAnchor sendMail = new SAnchor("mailto:haaf@mercatis.de");
                    sendMail.add(new SLabel("Please send my why!"));
                    p.add(sendMail);
                    SOptionPane.showPlainMessageDialog(null, p);
                }
                else {
                    SOptionPane.showPlainMessageDialog(null, "Fine, so do we!");
                }
            }
        };

        yesno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showYesNoDialog(null, "Do you like wingS", "A Yes No Question", feedback);
            }
        });

        toolBar.add(yesno);

        SButton information = new SButton("show Information");
        information.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showMessageDialog(null, "wingS2 WingSet", "Information");
            }
        });
        toolBar.add(information);

        final SLabel label = new SLabel();
        final ActionListener inputListener = new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(SOptionPane.OK_ACTION)) {
                    final SOptionPane optionPane = (SOptionPane) e.getSource();
                    STextField inputValue = (STextField) optionPane.getInputValue();

                    if ("".equals(inputValue.getText().trim())) {
                        SOptionPane.showMessageDialog(null, "The profession field is empty.", "Empty profession field", SOptionPane.ERROR_MESSAGE, new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                optionPane.show(null);
                            }
                        });
                    }
                    else {
                        label.setText("" + inputValue.getText());
                    }
                }
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
        c.add(combo, SBorderLayout.SOUTH);
        return c;
    }
}
