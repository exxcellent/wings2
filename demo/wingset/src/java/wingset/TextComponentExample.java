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
import org.wings.SDimension;
import org.wings.SForm;
import org.wings.SFormattedTextField;
import org.wings.SGridLayout;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SPasswordField;
import org.wings.STextArea;
import org.wings.STextComponent;
import org.wings.STextField;
import org.wings.border.SLineBorder;
import org.wings.event.SDocumentEvent;
import org.wings.event.SDocumentListener;
import org.wings.event.SRenderEvent;
import org.wings.event.SRenderListener;
import org.wings.session.SessionManager;
import org.wings.text.SAbstractFormatter;
import org.wings.text.SDateFormatter;
import org.wingx.XCalendar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public class TextComponentExample extends WingSetPane {
    private final SLabel actionEvent = new SLabel("(no form or button event)");
    private final STextArea eventLog = new STextArea();
    private ComponentControls controls;
    private SDateFormatter dateFormatter = new SDateFormatter(DateFormat.getDateInstance(DateFormat.SHORT));


    public SComponent createExample() {
        controls = new TextComponentControls();

        SGridLayout gridLayout = new SGridLayout(2);
        gridLayout.setHgap(10);
        gridLayout.setVgap(4);
        SPanel p = new SPanel(gridLayout);

        p.add(new SLabel("XCalendar: "));
        XCalendar calendar = new XCalendar(dateFormatter);
        calendar.setName("calendar");
        calendar.setToolTipText("A Date Picker.");
        calendar.getFormattedTextField().addDocumentListener(new MyDocumentListener(calendar.getFormattedTextField()));
        p.add(calendar);

        p.add(new SLabel("STextField: "));
        STextField textField = new STextField();
        textField.setName("textfield");
        textField.setToolTipText("Here you can enter any abritriary text.");
        textField.addDocumentListener(new MyDocumentListener(textField));
        p.add(textField);

        p.add(new SLabel("SFormattedTextField (NumberFormat): "));
        SFormattedTextField numberTextField = new SFormattedTextField(new NumberFormatter());
        numberTextField.setName("numberfield");
        numberTextField.setToolTipText("Text entered here will be formatted as number when you leave focus.\n" +
                "If you entered an invalid number the text should become red.\n" +
                "This uses code executed on server side in Java!");
        numberTextField.addDocumentListener(new MyDocumentListener(numberTextField));
        p.add(numberTextField);

        p.add(new SLabel("SFormattedTextField (DateFormat): "));
        SFormattedTextField dateTextField = new SFormattedTextField(dateFormatter);
        dateTextField.setName("datefield");
        dateTextField.setToolTipText("Enter a valid/invalid date here.\n" +
                "Dates will be parsed on server side and reformatted accordingly.");
        dateTextField.addDocumentListener(new MyDocumentListener(dateTextField));
        p.add(dateTextField);

        p.add(new SLabel("SPasswordField: "));
        SPasswordField passwordField = new SPasswordField();
        passwordField.setName("passwordfield");
        passwordField.setToolTipText("Just a regular passsword input.");
        passwordField.addDocumentListener(new MyDocumentListener(passwordField));
        p.add(passwordField);

        p.add(new SLabel("STextArea: "));
        STextArea textArea = new STextArea("");
        textArea.setName("textarea");
        textArea.setPreferredSize(new SDimension(250, 50));
        textArea.setToolTipText("Okay - but don't start writing books now ;-)");
        textArea.addDocumentListener(new MyDocumentListener(textArea));
        p.add(textArea);

        p.add(new SLabel("SDocumentEvents: "));
        p.add(eventLog);

        SLabel actionEventsLabel = new SLabel("ActionEvents (try Submit vs. Enter):");
        p.add(actionEventsLabel);
        p.add(actionEvent);

        SButton button = new SButton("Submit");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionEvent.setText("Button clicked + ");
            }
        });
        p.add(button);

        SForm frame = new SForm(new SBorderLayout());
        frame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionEvent.setText(actionEvent.getText() + " Form event");
            }
        });

        // Styling of all components.
        for (int i = 0; i < p.getComponents().length; i++) {
            SComponent component = p.getComponents()[i];
            component.setVerticalAlignment(SConstants.TOP);
            if ((component instanceof STextComponent) /*&& (component != disabledTextArea)*/ && (component != textArea))
                component.setPreferredSize(new SDimension("250px", null));
            if ((component instanceof XCalendar))
                component.setPreferredSize(new SDimension("250px", null));
        }

        eventLog.setEditable(false); // for multiline label
        eventLog.setBorder(new SLineBorder(1));
        eventLog.setBackground(Color.LIGHT_GRAY);

        actionEvent.setBorder(new SLineBorder(1));
        actionEvent.setBackground(Color.LIGHT_GRAY);

        controls.addControllable(calendar);
        controls.addControllable(textField);
        controls.addControllable(textArea);
        controls.addControllable(passwordField);
        controls.addControllable(numberTextField);
        controls.addControllable(dateTextField);

        frame.add(controls, SBorderLayout.NORTH);
        frame.add(p, SBorderLayout.CENTER);

        // Clear event log on rendering page
        frame.addRenderListener(new SRenderListener() {
            public void doneRendering(SRenderEvent e) {
                eventLog.setText("");
                actionEvent.setText("");
            }

            public void startRendering(SRenderEvent e) {
                if (eventLog.getText().length() == 0)
                    eventLog.setText("(no document events fired)");
            }
        });

        return frame;
    }

    /**
     * Listener updating the document event log label.
     */
    private final class MyDocumentListener implements SDocumentListener {
        private STextComponent eventSource;

        public MyDocumentListener(STextComponent eventSource) {
            this.eventSource = eventSource;
        }

        public void insertUpdate(SDocumentEvent e) {
            String eventString = "insertUpdate() for " + eventSource.getName()
                    + ": length: " + e.getLength() + " offset: " + e.getOffset() + "\n";
            eventLog.setText(eventLog.getText() + eventString);
        }

        public void removeUpdate(SDocumentEvent e) {
            String eventString = "removeUpdate() for " + eventSource.getName()
                    + ": length: " + e.getLength() + " offset: " + e.getOffset() + "\n";
            eventLog.setText(eventLog.getText() + eventString);
        }


        public void changedUpdate(SDocumentEvent e) {
        } // Never called for unstyled documents
    }

    private static class NumberFormatter extends SAbstractFormatter {
        NumberFormat format = NumberFormat.getNumberInstance(SessionManager.getSession().getLocale());

        public Object stringToValue(String text) throws ParseException {
            if (text == null || text.trim().length() == 0)
                return null;
            else
                return format.parse(text.trim());
        }

        public String valueToString(Object value) throws ParseException {
            if (value == null)
                return "";
            else
                return format.format(value);
        }
    }

    static class TextComponentControls
        extends ComponentControls
    {
        public TextComponentControls() {
            widthTextField.setText("100%");
            formComponentCheckBox.setVisible(false);
        }
    }
}
