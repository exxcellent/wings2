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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class OptionPaneExample
        extends SForm
        implements SConstants {
    public OptionPaneExample(SFrame f) {

        final SFrame frame = f;
        SButton msg = new SButton("show Message");
        msg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showMessageDialog(frame, "This is a simple message", "A Message");
            }
        });
        add(msg);

        SButton question = new SButton("show Question");
        final ActionListener comment = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == SOptionPane.OK_ACTION)
                    SOptionPane.showMessageDialog(frame, "Fine !");
                else
                    SOptionPane.showMessageDialog(frame, "No Problem, just look at another site");
            }
        };

        question.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showQuestionDialog(frame, "Continue this example?",
                        "A Question", comment);
            }
        });
        add(question);

        SButton yesno = new SButton("show Yes No");
        final ActionListener feedback = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == SOptionPane.NO_ACTION) {
                    SPanel p = new SPanel(new SFlowDownLayout());
                    p.add(new SLabel("That's sad !"));
                    SAnchor sendMail = new SAnchor("mailto:ahaaf@mercatis.de");
                    sendMail.add(new SLabel("Please send my why!"));
                    p.add(sendMail);
                    SOptionPane.showMessageDialog(frame, p);
                } else
                    SOptionPane.showMessageDialog(frame, "Fine, so do we!");
            }
        };

        yesno.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showYesNoDialog(frame,
                        "Do you like the HTML package",
                        "A Yes No Question", feedback);
            }
        });

        add(yesno);
    }
}


