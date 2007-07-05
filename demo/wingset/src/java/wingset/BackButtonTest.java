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

import org.wings.SButton;
import org.wings.SButtonGroup;
import org.wings.SComponent;
import org.wings.SFlowDownLayout;
import org.wings.SFlowLayout;
import org.wings.SFont;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SRadioButton;
import org.wings.SDimension;
import org.wings.border.SEmptyBorder;
import org.wings.event.*;

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
public class BackButtonTest
    extends WingSetPane
{
    private final SPanel mainPanel = new SPanel();
    private final SLabel epochLabel = new SLabel();
    private final SButton newEpochButton = new SButton("After changing mode, click here SEVERAL times generate browser history entries");

    private final SButton regularButton = new SButton("Regular button");
    private final SLabel regularButtonSignal = new SLabel("Regular button pressed");

    private final SButton virtualBackButton = new SButton("Virtual back button");
    private final SLabel virtualBackButtonSignal = new SLabel("Virtual back button pressed");

    private final SButton nonEpochedButton = new SButton("Non epoch-checked button");
    private final SLabel nonEpochedButtonSignal = new SLabel("Non epoch-checked button accepted");


    protected SComponent createControls() {
        return null;
    }

    protected SComponent createExample() {
        mainPanel.setLayout(new SFlowDownLayout());
        mainPanel.setHorizontalAlignment(CENTER);
        mainPanel.setPreferredSize(new SDimension(550, SDimension.AUTO_INT));

        final SLabel titleLabel = new SLabel("wingS browser back-button detection\n ");
        titleLabel.setFont(new SFont("sans-serif", SFont.PLAIN, 16));
        mainPanel.add(titleLabel);

        final SLabel instructions = new SLabel("The epoch checking mechanism allows wingS " +
                "to tackle the browser back navigation in threee different ways demonstrated in this example.\n\n" +
                "To try this example just choose one of the described ways, click several time the buttons and try " +
                "what happens if you go back with your browser.");
        instructions.setWordWrap(true);
        mainPanel.add(instructions);

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

        virtualBackButton.addActionListener(new java.awt.event.ActionListener() {
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
        final SRadioButton postMode = new SRadioButton("<html><b>Default:</b> Drop and ignore requests from old views" +
                " and just redisplay the current view. This ensures that no activity is falsely triggered and the " +
                "user only starts actions he expects. Typicall web applications are operating with HTTP POST, so " +
                "most wrowsers will present a 'repost form' confirmation dialog on navigating back.<p> ");
        final SRadioButton getMode = new SRadioButton("<html><b>Extended default:</b> Same as the default case, " +
                "but you register a virtual \"back\" button. wingS will <i>immediately</i> notify and catch " +
                "back operations and and trigger your virtual back button by heuristics. " +
                "Probably you want to use this. event i.e. to display a information message or trigger applaciation backs/undos." +
                "This example switches into HTTP GET mode to avoid the anyoing 'repost form' confirmations.<p> ");
        final SRadioButton getMode2 = new SRadioButton("<html><b>Allow:</b> Allows and doesn't intercept back navigations. " +
                "The user will be able to see and click within 'old views'. Just as the default case" +
                "wingS will ignored by default clicks on any component, unless you marked it via <b>setEpochCheckEnabled()</b> " +
                "to be always valid. From the developers perspective your application logic must be aware that those " +
                "non-epoched components will be able to fire events at any time." +
                "In this example clicks on the <b>regular</b> buttons will be dropped and lead to the back button event, " +
                "while clicking on the <b>Not epoch-checked button</b> will also accepted in old views.<p> ");
        buttonGroup.add(postMode);
        buttonGroup.add(getMode);
        buttonGroup.add(getMode2);
        postMode.setSelected(true);
        postMode.setWordWrap(true);
        getMode.setWordWrap(true);
        getMode2.setWordWrap(true);
        mainPanel.add(postMode);
        mainPanel.add(getMode);
        mainPanel.add(getMode2);

        buttonGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                regularButton.setVisible(false);
                nonEpochedButton.setVisible(false);

                if (buttonGroup.getSelection() == postMode) {
                    setPostMethod(true);
                    mainPanel.getParentFrame().setNoCaching(true);
                } else if (buttonGroup.getSelection() == getMode) {
                    setPostMethod(false);
                    mainPanel.getParentFrame().setNoCaching(true);
                } else if (buttonGroup.getSelection() == getMode2) {
                    setPostMethod(false);
                    mainPanel.getParentFrame().setNoCaching(false);
                    // Allow events on this button from old views
                    nonEpochedButton.setEpochCheckEnabled(false);
                    // Turn of components included in every request.
                    // Otherwise we would receive irritiating back button events in this demo.
                    postMode.setEpochCheckEnabled(false);
                    getMode.setEpochCheckEnabled(false);
                    getMode2.setEpochCheckEnabled(false);
                    setEpochCheckEnabled(false);
                    regularButton.setVisible(true);
                    nonEpochedButton.setVisible(true);
                }
            }
        });

        mainPanel.add(epochLabel);

        newEpochButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainPanel.reload(); // Force invalidaton of epoch for demonstration purposes
            }
        });
        mainPanel.add(newEpochButton);


        regularButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                regularButtonSignal.setVisible(true);
            }
        });
        mainPanel.add(regularButton);

        nonEpochedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nonEpochedButtonSignal.setVisible(true);
            }
        });
        mainPanel.add(nonEpochedButton);

        mainPanel.addParentFrameListener(new SParentFrameListener()
        {
            public void parentFrameAdded(SParentFrameEvent e) {
                mainPanel.getParentFrame().setBackButton(virtualBackButton);

                mainPanel.getParentFrame().addInvalidLowLevelEventListener(new SInvalidLowLevelEventListener() {
                    public void invalidLowLevelEvent(InvalidLowLevelEvent e) {
                        log.info("Invalid Low-Level event detected on "+e.getSource());
                    }
                });
                mainPanel.removeParentFrameListener(this);
            }

            public void parentFrameRemoved(SParentFrameEvent e) {
            }
        });
        return mainPanel;
    }
}
