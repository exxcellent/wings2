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
public class ProgressBarExample extends WingSetPane {

    public SComponent createExample() {
        final SButton forward1Percent = new SButton("+1");
        final SButton forward10Percent = new SButton("+10");
        final SButton backward1Percent = new SButton("-1");
        final SButton backward10Percent = new SButton("-10");

        final SProgressBar progressBar = new SProgressBar(0, 100);

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int valueChange = 0;
                if (e.getSource() == forward1Percent) {
                    valueChange = 1;
                } else if (e.getSource() == forward10Percent) {
                    valueChange = 10;
                } else if (e.getSource() == backward1Percent) {
                    valueChange = -1;
                } else if (e.getSource() == backward10Percent) {
                    valueChange = -10;
                } // end of if ()

                progressBar.setValue(progressBar.getValue() + valueChange);
            }
        };
        forward1Percent.addActionListener(al);
        forward10Percent.addActionListener(al);
        backward1Percent.addActionListener(al);
        backward10Percent.addActionListener(al);

        progressBar.setUnfilledColor(java.awt.Color.gray);
        progressBar.setFilledColor(java.awt.Color.red);
        progressBar.setForeground(java.awt.Color.red);
        progressBar.setBorderColor(java.awt.Color.black);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new SDimension(200, 5));

        progressBar.setValue(20);

        SContainer panel = new SPanel();

        try {
            java.net.URL templateURL = getSession().getServletContext().getResource("/templates/ProgressBarExample.thtml");
            STemplateLayout layout = new STemplateLayout(templateURL);
            panel.setLayout(layout);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        panel.add(progressBar, "ProgressBar");
        panel.add(forward1Percent, "Forward1Percent");
        panel.add(forward10Percent, "Forward10Percent");
        panel.add(backward1Percent, "Backward1Percent");
        panel.add(backward10Percent, "Backward10Percent");


        return panel;
    }


}


