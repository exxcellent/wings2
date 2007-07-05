/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
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
 */
public class OptionPaneExample
        extends WingSetPane {

    private ComponentControls controls;

    protected SComponent createControls() {
        controls = new OptionPaneControls();
        return controls;
    }

    protected SComponent createExample() {
        SDesktopPane desktopPane = new SDesktopPane();

        final SInternalFrame iFrame1 = new SInternalFrame();
        iFrame1.getContentPane().setLayout(new SFlowDownLayout());

        SButton button1 = new SButton("Show OptionPane within this InternalFrame");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showMessageDialog(iFrame1, "Any message.", "Any title.");
            }
        });
        iFrame1.getContentPane().add(button1);

        for (int i = 0; i < 12; i++) {
            iFrame1.getContentPane().add(new SLabel("Label " + i));
        }

        desktopPane.add(iFrame1);

        final SInternalFrame iFrame2 = new SInternalFrame();
        iFrame2.getContentPane().setLayout(new SFlowDownLayout());

        SButton button2 = new SButton("Show Error Message within this InternalFrame");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showMessageDialog(iFrame2, "Oops! Don't you worry! ;)", "Error Message", SOptionPane.ERROR_MESSAGE);
            }
        });
        iFrame2.getContentPane().add(button2);

        for (int i = 0; i < 12; i++) {
            iFrame2.getContentPane().add(new SLabel("Label " + i));
        }

        desktopPane.add(iFrame2);

        return desktopPane;
    }

    class OptionPaneControls
        extends ComponentControls
    {
        public OptionPaneControls() {
            globalControls.setVisible(false);

            SButton msg = new SButton("show Message");
            msg.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SOptionPane.showPlainMessageDialog(null, "This is a simple message", "A Message");
                }
            });
            addControl(msg);

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
            addControl(question);

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

            addControl(yesno);

            SButton information = new SButton("show Information");
            information.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SOptionPane.showMessageDialog(null, "wingS2 WingSet", "Information");
                }
            });
            addControl(information);

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
            addControl(input);
            addControl(label);
        }
    }
}
