/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package wingset;

import org.wings.*;
import org.wings.template.StringTemplateSource;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class InteractiveTemplateExample
        extends WingSetPane
        implements SConstants {

    final static String TEMPLATE = "/templates/InteractiveTemplateExample.thtml";
    final static String FALLBACK_TEMPLATE = "/templates/FallbackInteractiveTemplateExample.thtml";

    private String fallbackTemplateString;


    protected SComponent createExample() {
        SPanel examplePanel = new SPanel();

        try {
            java.net.URL templateURL = getSession().getServletContext().getResource(TEMPLATE);
            examplePanel.setLayout(new STemplateLayout(templateURL));
        } catch (Exception e) {
            // template not found ?
        }

        try {
            java.net.URL fallbackTemplateURL = getSession().getServletContext().getResource(FALLBACK_TEMPLATE);
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(fallbackTemplateURL.openStream()));

            String line = reader.readLine();
            while (line != null) {
                buffer.append(line).append('\n');
                line = reader.readLine();
            } // end of while ()

            fallbackTemplateString = buffer.toString();
        } catch (Exception ex) {
            fallbackTemplateString =
                    "A simple interactive example how to use template layouts:<br/>\n" +
                    "<input type=textarea column=\"80\" rows=\"20\" name=\"TemplateInput\"/> <br/>\n" +
                    "<input type=submit text=\"Apply\" name=\"Apply\"/>";
            ex.printStackTrace();
            // template not found ?
        }

        SForm form = new SForm();

        final StringTemplateSource templateSource =
                new StringTemplateSource(fallbackTemplateString);

        final STextArea templateInput = new STextArea();
        templateInput.setText(fallbackTemplateString);

        SButton applyButton = new SButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                templateSource.setTemplate(templateInput.getText());
            }
        });

        SButton resetButton = new SButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                templateSource.setTemplate(fallbackTemplateString);
                templateInput.setText(fallbackTemplateString);
            }
        });

        SLabel label = new SLabel("Simple Label");

        form.setLayout(new STemplateLayout(templateSource));

        form.add(templateInput, "TemplateInput");
        form.add(applyButton, "Apply");
        form.add(label, "Label");

        examplePanel.add(form, "DynamicTemplate");
        examplePanel.add(resetButton, "Reset");
        return examplePanel;
    }
}


