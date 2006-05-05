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

import org.wings.SBorderLayout;
import org.wings.SButton;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SForm;
import org.wings.SLabel;
import org.wings.STemplateLayout;
import org.wings.STextArea;
import org.wings.SToolBar;
import org.wings.border.SBorder;
import org.wings.border.SLineBorder;
import org.wings.style.CSSProperty;
import org.wings.template.StringTemplateSource;
import org.wings.util.SStringBuilder;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class InteractiveTemplateExample
        extends WingSetPane
        implements SConstants {

    final static String TEMPLATE = "/templates/FallbackInteractiveTemplateExample.thtml";

    private String templateString;
    protected StringTemplateSource templateSource;
    protected STextArea templateInput;
    protected Controls controls;


    protected SComponent createExample() {
        controls = new Controls();

        try {
            java.net.URL templateURL = getSession().getServletContext().getResource(TEMPLATE);
            SStringBuilder buffer = new SStringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(templateURL.openStream()));

            String line = reader.readLine();
            while (line != null) {
                buffer.append(line).append('\n');
                line = reader.readLine();
            }
            templateString = buffer.toString();
        }
        catch (Exception ex) {
            templateString =
                    "A simple interactive example how to use template layouts:<br/>\n" +
                    "<input type=textarea column=\"100\" rows=\"20\" name=\"TemplateInput\"/> <br/>\n" +
                    "<input type=submit text=\"Apply\" name=\"Apply\"/>";
            ex.printStackTrace();
        }

        templateSource = new StringTemplateSource(templateString);
        templateInput = new STextArea(templateString);

        SButton applyButton = new SButton("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                templateSource.setTemplate(templateInput.getText());
            }
        });

        SLabel label = new SLabel("Simple Label");

        SForm form = new SForm();
        form.setLayout(new STemplateLayout(templateSource));
        form.add(templateInput, "TemplateInput");
        form.add(applyButton, "Apply");
        form.add(label, "Label");

        SForm panel = new SForm(new SBorderLayout());
        panel.add(controls, SBorderLayout.NORTH);
        panel.add(form, SBorderLayout.CENTER);
        return panel;
    }

    class Controls extends SToolBar {
        public Controls() {
            SBorder border = new SLineBorder(new Color(0xCC, 0xCC, 0xCC), 0);
            border.setThickness(1, SConstants.TOP);
            setBorder(border);
            SButton resetButton = new SButton("Reset");
            resetButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    templateSource.setTemplate(templateString);
                    templateInput.setText(templateString);
                }
            });

            add(resetButton, SBorderLayout.NORTH);
        }
    }
}
