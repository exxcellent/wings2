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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The <code>InternalFrameOptionPaneExample</code> shows the
 * facility of option pane usage within internal frames.
 * User: raedler
 * Date: 03.08.2006
 * Time: 08:50:48
 *
 * @author Roman R&auml;dle
 * @version $Revision$
 */
public class InternalFrameOptionPaneExample extends WingSetPane {

    /**
     * Creates the internal frame option pane content.
     */
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

        for (int i = 0; i < 20; i++) {
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

        for (int i = 0; i < 20; i++) {
            iFrame2.getContentPane().add(new SLabel("Label " + i));
        }

        desktopPane.add(iFrame2);

        return desktopPane;
    }
}
