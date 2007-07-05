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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class ProgressBarExample extends WingSetPane {

    protected SComponent createControls() {
        return null;
    }

    public SComponent createExample() {
        final SButton forward1Percent = new SButton("+1");
        final SButton forward10Percent = new SButton("+10");
        final SButton backward1Percent = new SButton("-1");
        final SButton backward10Percent = new SButton("-10");

        final SProgressBar progressBar = new SProgressBar(0, 100);

        final ActionListener al = new java.awt.event.ActionListener() {
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

        progressBar.setUnfilledColor(java.awt.Color.lightGray);
        progressBar.setFilledColor(java.awt.Color.orange);
        progressBar.setForeground(java.awt.Color.blue);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new SDimension(250,SDimension.AUTO_INT));
        progressBar.setProgressBarDimension(new SDimension(250,12));

        progressBar.setValue(20);

        final SGridBagLayout gridBagLayout = new SGridBagLayout();
        final SContainer panel = new SPanel(gridBagLayout);
        panel.setPreferredSize(new SDimension("250",null));

        GridBagConstraints c0 = new GridBagConstraints();
        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(new SLabel("<html>&nbsp;"), c1);
        panel.add(progressBar, c1);
        panel.add(backward10Percent, c0);
        backward10Percent.setHorizontalAlignment(LEFT_ALIGN);
        panel.add(forward10Percent, c1);
        forward10Percent.setHorizontalAlignment(RIGHT_ALIGN);
        panel.add(backward1Percent, c0);
        backward1Percent.setHorizontalAlignment(LEFT_ALIGN);
        panel.add(forward1Percent, c1);
        forward1Percent.setHorizontalAlignment(RIGHT_ALIGN);

        return panel;
    }
}


