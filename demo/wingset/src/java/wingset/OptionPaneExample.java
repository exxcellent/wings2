/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of the wingS demo (http://wings.mercatis.de).
 *
 * The wingS demo is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package wingset;

import java.awt.event.*;

import org.wings.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class OptionPaneExample
    extends SForm
    implements SConstants
{
    public OptionPaneExample(SFrame f) {

        final SFrame frame = f;
        SButton msg = new SButton("show Message");
        msg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showMessageDialog(frame, "This is a simple message", "A Message");
            } });
        add(msg);

        SButton question = new SButton("show Question");
        final ActionListener comment = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( e.getActionCommand()==SOptionPane.OK_ACTION )
                    SOptionPane.showMessageDialog(frame, "Fine !");
                else
                    SOptionPane.showMessageDialog(frame, "No Problem, just look at another site");
            }
        };

        question.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showQuestionDialog(frame, "Continue this example?",
                                               "A Question", comment);
            }});
        add(question);

        SButton yesno = new SButton("show Yes No");
        final ActionListener feedback = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( e.getActionCommand()==SOptionPane.NO_ACTION ) {
                    SPanel p = new SPanel(new SFlowDownLayout());
                    p.add(new SLabel("That's sad !"));
                    SAnchor sendMail = new SAnchor("mailto:ahaaf@mercatis.de");
                    sendMail.add(new SLabel("Please send my why!")); 
                    p.add(sendMail);
                    SOptionPane.showMessageDialog(frame, p);
                }
                else
                    SOptionPane.showMessageDialog(frame, "Fine, so do we!");
            }
        };

        yesno.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SOptionPane.showYesNoDialog(frame,
                                            "Do you like the HTML package",
                                            "A Yes No Question", feedback);
            }});

        add(yesno);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
