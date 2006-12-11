/*
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of the wingS demo (http://j-wings.org).
 *
 * The wingS demo is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package frameset;

import org.wings.SButton;
import org.wings.SCheckBox;
import org.wings.SForm;
import org.wings.SFrame;
import org.wings.SLabel;
import org.wings.event.SRequestEvent;
import org.wings.event.SRequestListener;
import org.wings.frames.SFrameSet;
import org.wings.frames.SFrameSetLayout;
import org.wings.frames.SReloadFrame;
import org.wings.resource.ReloadResource;
import org.wings.resource.DynamicResource;
import org.wings.session.Session;
import org.wings.session.SessionManager;
import org.wings.style.CSSProperty;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple application to demonstrate the features of the the experimental
 * frameset support in wingS.
 * <p/>
 * With SReloadFrame only the frames, whose code actually changed will get reloaded.
 *
 * @author Holger Engels
 */
public class FrameSet {
    private SFrameSet vertical;
    private SFrameSet horizontal;
    private SFrame toolbarFrame;
    private SFrame leftFrame;
    private SFrame rightFrame;
    private SReloadFrame reloadFrame;

    private SButton leftButton = new SButton("left frame: 0");
    private SButton rightButton = new SButton("right frame: 0");

    private int leftCount = 0;
    private int rightCount = 0;

    private static String VERTICAL_LAYOUT = "110,*";

    public FrameSet() {
        vertical = new SFrameSet(new SFrameSetLayout(null, VERTICAL_LAYOUT));
        toolbarFrame = new SFrame("toolbar");
        vertical.add(toolbarFrame);

        horizontal = new SFrameSet(new SFrameSetLayout("50%,50%", null));
        vertical.add(horizontal);
        leftFrame = new SFrame("left frame");
        horizontal.add(leftFrame);
        rightFrame = new SFrame("right frame");
        horizontal.add(rightFrame);

        SFrameSet.assignBaseTarget(vertical, "_top");
        vertical.show();

        buildFrames(toolbarFrame, leftFrame, rightFrame);

        installReloadManagerFrame();

        // Add a request handler that invalidates outdated frames on request.
        final Session session = SessionManager.getSession();
        session.addRequestListener(new SRequestListener() {
            public void processRequest(SRequestEvent e) {
                if (SRequestEvent.DISPATCH_DONE == e.getType() && reloadFrame != null) {
                    Set dirtyFrames = new HashSet(session.getReloadManager().getDirtyFrames());
                    reloadFrame.setDirtyFrames(dirtyFrames);
                    session.getReloadManager().clear();
                }
            }
        });
    }

    protected void buildFrames(SFrame toolbar, SFrame left, SFrame right) {
        // Action listeners for all buttons
        ActionListener leftIncrement = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                leftCount++;
                leftButton.setText("left frame: " + leftCount);
            }
        };
        ActionListener rightIncrement = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                rightCount++;
                rightButton.setText("right frame: " + rightCount);
            }
        };
        ActionListener leftDecrement = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                leftCount--;
                leftButton.setText("left frame: " + leftCount);
            }
        };
        ActionListener rightDecrement = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                rightCount--;
                rightButton.setText("right frame: " + rightCount);
            }
        };

        // Style frames
        toolbarFrame.setBackground(Color.yellow);
        toolbarFrame.setAttribute(CSSProperty.MARGIN, "10px");
        leftFrame.setAttribute(CSSProperty.MARGIN, "10px");
        rightFrame.setAttribute(CSSProperty.MARGIN, "10px");

        // Form to increment all frames.
        SButton leftModifier = new SButton("< increment left frame counter");
        leftModifier.addActionListener(leftIncrement);

        SButton rightModifier = new SButton("increment right frame counter >");
        rightModifier.addActionListener(rightIncrement);

        SButton bothModifier = new SButton("< increment both frame counters >");
        bothModifier.addActionListener(leftIncrement);
        bothModifier.addActionListener(rightIncrement);

        toolbar.getContentPane().add(new SLabel("<html><b>Frame support demo for wingS 2</b><br>" +
                "When using the reload-manager frame, only frames whose " +
                "components actually changed, are refetched.<br>" +
                "In the default case all frame are reloaded on every request."));

        final SForm incForm = new SForm();
        incForm.add(leftModifier);
        incForm.add(bothModifier);
        incForm.add(rightModifier);
        toolbar.getContentPane().add(incForm);

        left.getContentPane().setLayout(null);
        left.getContentPane().add(leftButton);
        left.getContentPane().add(createDecrementerForm(leftDecrement,
                rightDecrement));
        left.getContentPane().add(new RenderTimeLabel()); // see below.

        right.getContentPane().setLayout(null);
        right.getContentPane().add(rightButton);
        right.getContentPane().add(createDecrementerForm(leftDecrement,
                rightDecrement));
        right.getContentPane().add(new RenderTimeLabel()); // see below.

        leftButton.addActionListener(leftDecrement);
        rightButton.addActionListener(rightDecrement);

        final SCheckBox toggleReloadManager = new SCheckBox("use reload manager frame");
        toggleReloadManager.setSelected(true);
        toggleReloadManager.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (toggleReloadManager.isSelected())
                    installReloadManagerFrame();
                else
                    uninstallReloadManagerFrame();
            }
        });
        toolbar.getContentPane().add(toggleReloadManager);

        toolbarFrame.show();
	    leftFrame.show();
	    rightFrame.show();
    }

    SForm createDecrementerForm(ActionListener left, ActionListener right) {
        SButton leftDecrementer = new SButton("< decrement left frame counter");
        leftDecrementer.addActionListener(left);

        SButton rightDecrementer = new SButton("decrement right frame counter >");
        rightDecrementer.addActionListener(right);

        SForm decForm = new SForm();
        decForm.add(leftDecrementer);
        decForm.add(rightDecrementer);
        return decForm;
    }

    void installReloadManagerFrame() {
        // add reload manager frame. Create a frame of size 10 for
        // demonstration purposes. In 'real' applications, this would obviously
        // be of size zero.
        vertical.setLayout(new SFrameSetLayout(null, VERTICAL_LAYOUT + ",0"));
        reloadFrame = new SReloadFrame();
        vertical.add(reloadFrame);

        // set base target
        SFrameSet.assignBaseTarget(toolbarFrame, SFrameSet.createBaseTargetName(reloadFrame));
        SFrameSet.assignBaseTarget(leftFrame, SFrameSet.createBaseTargetName(reloadFrame));
        SFrameSet.assignBaseTarget(rightFrame, SFrameSet.createBaseTargetName(reloadFrame));

        DynamicResource targetResource = reloadFrame.getDynamicResource(ReloadResource.class);
        // set target resource
        toolbarFrame.setTargetResource(targetResource.getId());
        leftFrame.setTargetResource(targetResource.getId());
        rightFrame.setTargetResource(targetResource.getId());
    }

    void uninstallReloadManagerFrame() {
        if (reloadFrame != null) {
            Set dirtyFrames = new HashSet();
            dirtyFrames.add(vertical.getDynamicResource(ReloadResource.class));
            reloadFrame.setDirtyFrames(dirtyFrames);

            vertical.remove(reloadFrame);
            vertical.setLayout(new SFrameSetLayout(null, VERTICAL_LAYOUT));
            reloadFrame = null;
        }

        // reset target
        SFrameSet.assignBaseTarget(toolbarFrame, "_top");
        SFrameSet.assignBaseTarget(leftFrame, "_top");
        SFrameSet.assignBaseTarget(rightFrame, "_top");

        DynamicResource targetResource = vertical.getDynamicResource(ReloadResource.class);
        toolbarFrame.setTargetResource(targetResource.getId());
        leftFrame.setTargetResource(targetResource.getId());
        rightFrame.setTargetResource(targetResource.getId());
    }

    /**
     * A Label that returns the current time every time it is rendered.
     * This is to demonstrate, that using the reload-frame does not cause
     * re-rendering of unaffected Frames. In real-live, you would never have
     * a component whose content changes every time it is rendered.
     */
    private static class RenderTimeLabel extends SLabel {
        long startTime = System.currentTimeMillis();

        public String getText() {
            return "(rendered frame at t=" + (System.currentTimeMillis() - startTime) / 1000.0 + ")";
        }
    }
}
