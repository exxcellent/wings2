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

import org.wings.ReloadManager;
import org.wings.SButton;
import org.wings.SButtonGroup;
import org.wings.SComponent;
import org.wings.SFlowDownLayout;
import org.wings.SFlowLayout;
import org.wings.SFont;
import org.wings.SForm;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SRadioButton;
import org.wings.border.SEmptyBorder;
import org.wings.event.InvalidLowLevelEvent;
import org.wings.event.SInvalidLowLevelEventListener;
import org.wings.event.SRenderEvent;
import org.wings.event.SRenderListener;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Example demonstrating the capabilities of wings regarding
 * <ul>
 * <li>Back button handling</li>
 * <li>Read-only mode with support for operations on old views</li>
 * </ul>
 *
 * @author bschmid
 */
public class BackButtonExample extends WingSetPane {
    private final SForm mainPanel = new SForm();
    private final SLabel epochLabel = new SLabel();
    private final SButton newEpochButton = new SButton("After changing mode, click here SEVERAL times generate browser history entries");

    private final SButton regularButton = new SButton("Regular button");
    private final SLabel regularButtonSignal = new SLabel("Regular button pressed");

    private final SButton virtualBackButton = new SButton("Virtual back button");
    private final SLabel virtualBackButtonSignal = new SLabel("Virtual back button pressed");

    private final SButton nonEpochedButton = new SButton("Non epoch-checked button");
    private final SLabel nonEpochedButtonSignal = new SLabel("Non epoch-checked button pressed");

    protected SComponent createExample() {
        mainPanel.setLayout(new SFlowDownLayout());
        // TODO: As labels don't wrap this dissorts layout!
        //mainPanel.setPreferredSize(new SDimension("500px", null));
        mainPanel.setHorizontalAlignment(CENTER);
        mainPanel.add(new SLabel("<html>wingS is able to handle browser back navigation in different ways<br><ul>" +
                "<li><b>Default: </b>Drop requests from old views and just redisplay page</li>" +
                "<li><b>Extended default: </b>Register a virtual \"back\" button and trigger this on back operations" +
                "You can use this. i.e. to display a information message or for navigation purpose.</li>" +
                "<li><b>Allow: </b>Allow & don't intercept back navigation. Modifications (i.e. clicks) on past pages will" +
                "be ignored by default, but can be enabled via setEpochCheckEnabled() for selected components</li>" +
                "</ul>" +
                "<p>Below is some example to experiment with these different modes to handle back navigation"));

        mainPanel.addRenderListener(new SRenderListener() {
            public void startRendering(SRenderEvent e) {
                epochLabel.setText("<html><p>Current epoch: <b>" + epochLabel.getParentFrame().getEventEpoch() + "</b><p>");
            }

            public void doneRendering(SRenderEvent e) {
                virtualBackButtonSignal.setVisible(false);
                regularButtonSignal.setVisible(false);
                nonEpochedButtonSignal.setVisible(false);
            }
        });

        virtualBackButton.addActionListener(new wingset.SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                virtualBackButtonSignal.setVisible(true);
            }
        });

        SPanel signalPanel = new SPanel(new SFlowLayout());
        signalPanel.add(virtualBackButtonSignal);
        virtualBackButtonSignal.setBorder(new SEmptyBorder(10, 10, 10, 10));
        virtualBackButtonSignal.setBackground(Color.YELLOW);
        virtualBackButtonSignal.setFont(new SFont(SFont.BOLD));
        virtualBackButtonSignal.setVisible(false);

        signalPanel.add(regularButtonSignal);
        regularButtonSignal.setBorder(new SEmptyBorder(10, 10, 10, 10));
        regularButtonSignal.setBackground(Color.GREEN);
        regularButtonSignal.setFont(new SFont(SFont.BOLD));
        regularButtonSignal.setVisible(false);

        signalPanel.add(nonEpochedButtonSignal);
        nonEpochedButtonSignal.setBorder(new SEmptyBorder(10, 10, 10, 10));
        nonEpochedButtonSignal.setBackground(Color.GREEN);
        nonEpochedButtonSignal.setFont(new SFont(SFont.BOLD));
        nonEpochedButtonSignal.setVisible(false);
        mainPanel.add(signalPanel);

        regularButton.setVisible(false);
        nonEpochedButton.setVisible(false);

        final SButtonGroup buttonGroup = new SButtonGroup();
        final SRadioButton postMode = new SRadioButton("<html><b>Default:</b> HTTP POST " +
                "(Browsers present a 'repost form' query after submits. wingS can optionally identify back operations");
        final SRadioButton getMode = new SRadioButton("<html><b>Extended default:</b> HTTP GET mode (Browsers <i>don't</i> present" +
                " a 'repost form' query. wingS <i>immediately</i> catches back operations and triggers virtual back button)");
        final SRadioButton getMode2 = new SRadioButton("<html><b>Allow:</b> HTTP GET mode and no page refresh on back navigation." +
                "wingS doesn't catch back navigation and may allow manipulation on selected buttons. " +
                "<br>Clicks on <b>regular</b> buttons lead to an <i>delayed</i> back button event." +
                "<br>Clicks on <b>Not epoch-checked button</b> will be accepted.");
        buttonGroup.add(postMode);
        buttonGroup.add(getMode);
        buttonGroup.add(getMode2);
        postMode.setSelected(true);
        mainPanel.add(postMode);
        mainPanel.add(getMode);
        mainPanel.add(getMode2);

        buttonGroup.addActionListener(new wingset.SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                regularButton.setVisible(false);
                nonEpochedButton.setVisible(false);

                if (buttonGroup.getSelection() == postMode) {
                    mainPanel.setPostMethod(true);
                    mainPanel.getParentFrame().setNoCaching(true);
                } else if (buttonGroup.getSelection() == getMode) {
                    mainPanel.setPostMethod(false);
                    mainPanel.getParentFrame().setNoCaching(true);
                } else if (buttonGroup.getSelection() == getMode2) {
                    mainPanel.setPostMethod(false);
                    mainPanel.getParentFrame().setNoCaching(false);
                    // Allow events on this button from old views
                    nonEpochedButton.setEpochCheckEnabled(false);
                    // Turn of components included in every request.
                    // Otherwise we would receive irritiating back button events in this demo.
                    postMode.setEpochCheckEnabled(false);
                    getMode.setEpochCheckEnabled(false);
                    getMode2.setEpochCheckEnabled(false);
                    mainPanel.setEpochCheckEnabled(false);
                    regularButton.setVisible(true);
                    nonEpochedButton.setVisible(true);
                }
            }
        });

        mainPanel.add(epochLabel);

        newEpochButton.addActionListener(new wingset.SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainPanel.reload(ReloadManager.STATE); // Force invalidaton of epoch for demonstration purposes
            }
        });
        mainPanel.add(newEpochButton);


        regularButton.addActionListener(new wingset.SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                regularButtonSignal.setVisible(true);
            }
        });
        mainPanel.add(regularButton);

        nonEpochedButton.addActionListener(new wingset.SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                nonEpochedButtonSignal.setVisible(true);
            }
        });
        mainPanel.add(nonEpochedButton);

        return mainPanel;
    }

    protected void initializePanel() {
        super.initializePanel(); // first add this panel to the frame.
        mainPanel.getParentFrame().setBackButton(virtualBackButton);

        mainPanel.getParentFrame().addInvalidLowLevelEventListener(new SInvalidLowLevelEventListener() {
            public void invalidLowLevelEvent(InvalidLowLevelEvent e) {
                log.info("Invalid Low-Level event detected on "+e.getSource());
            }
        });
    }

}
