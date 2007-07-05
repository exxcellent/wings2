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

import java.awt.Color;
import org.wings.SBorderLayout;
import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SDimension;
import org.wings.SGridLayout;
import org.wings.SIcon;
import org.wings.SPanel;
import org.wings.SPopup;
import org.wings.SLabel;
import org.wings.SURLIcon;
import org.wings.border.SLineBorder;
import org.wings.script.JavaScriptEvent;
import org.wings.script.PopupListener;

/**
 *
 * @author Christian Schyma
 */
public class PopupExample
        extends WingSetPane {

    private static final SIcon WINGS_IMAGE = new SURLIcon("../icons/wings-logo.png");
    private static final String SOME_TEXT =
            "What is wingS? In a nutshell, wingS is a web application\n" +
            "framework based on Java Servlets, resembling the Swing API with\n" +
            "its MVC paradigm and event oriented design principles. It utilizes\n" +
            "the models, events, and event listeners of Swing. Like in Swing,\n" +
            "components are arranged in a hierarchy of containers, whose root\n" +
            "container is hooked to a frame.";

    public SContainer createContent() {
        SContainer content = new SContainer(new SBorderLayout());
        content.setBorder(new SLineBorder(1));
        content.setBackground(Color.WHITE);
        content.setPreferredSize(new SDimension(200, 200));
        content.add(new SLabel(WINGS_IMAGE), SBorderLayout.NORTH);
        SLabel label = new SLabel(SOME_TEXT);
        content.add(new SLabel(SOME_TEXT), SBorderLayout.SOUTH);

        return content;
    }

    public SComponent createExample() {
        SPanel panel = new SPanel(new SGridLayout(2, 1, 50, 50));

        SLabel mouseSensitiveLabel = new SLabel("mouse sensitive label");
        SPopup popup = new SPopup(null, createContent(), 100, 100);

        mouseSensitiveLabel.addScriptListener(
                new PopupListener(JavaScriptEvent.ON_MOUSE_OVER, popup, PopupListener.SHOW, mouseSensitiveLabel));
        mouseSensitiveLabel.addScriptListener(
                new PopupListener(JavaScriptEvent.ON_MOUSE_OUT, popup, PopupListener.HIDE, mouseSensitiveLabel));

        panel.add(mouseSensitiveLabel);


        SLabel mouseSensitiveLabel2 = new SLabel("mouse sensitive label, popup gets aligned");
        SPopup popup2 = new SPopup(null, createContent(), 0, 0);
        popup2.setContext(mouseSensitiveLabel2, SPopup.TOP_LEFT, SPopup.BOTTOM_LEFT);

        mouseSensitiveLabel2.addScriptListener(
                new PopupListener(JavaScriptEvent.ON_MOUSE_OVER, popup2, PopupListener.SHOW, mouseSensitiveLabel2));
        mouseSensitiveLabel2.addScriptListener(
                new PopupListener(JavaScriptEvent.ON_MOUSE_OUT, popup2, PopupListener.HIDE, mouseSensitiveLabel2));

        panel.add(mouseSensitiveLabel2);


        return panel;
    }

    protected SComponent createControls() {
        return null;
    }

}
