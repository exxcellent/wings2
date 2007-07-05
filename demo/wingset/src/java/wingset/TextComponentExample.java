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
import org.wings.border.SLineBorder;
import org.wings.event.SDocumentEvent;
import org.wings.event.SDocumentListener;
import org.wings.event.SRequestEvent;
import org.wings.event.SRequestListener;
import org.wings.session.SessionManager;
import org.wings.text.SAbstractFormatter;
import org.wings.text.SDateFormatter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 */
public class TextComponentExample extends WingSetPane {
    private final SLabel actionEvent = new SLabel("(no form or button event)");
    private final STextArea eventLog = new STextArea("(no document events fired)");
    private ComponentControls controls;
    private SDateFormatter dateFormatter = new SDateFormatter(DateFormat.getDateInstance(DateFormat.SHORT));
    private STextField textField;
    private SFormattedTextField numberTextFieldCoR;
    private SFormattedTextField dateTextFieldCoR;
    private SFormattedTextField numberTextFieldC;
    private SFormattedTextField dateTextFieldC;
    private SPasswordField passwordField;
    private STextArea textArea;


    protected SComponent createControls() {
        controls = new TextComponentControls();
        return controls;
    }

    public SComponent createExample() {
        SGridLayout gridLayout = new SGridLayout(2);
        gridLayout.setHgap(10);
        gridLayout.setVgap(4);
        SPanel panel = new SPanel(gridLayout);

        panel.add(new SLabel("STextField: "));
        textField = new STextField();
        textField.setName("textfield");
        textField.setToolTipText("Here you can enter any abritriary text.");
        textField.addDocumentListener(new MyDocumentListener(textField));
        textField.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        panel.add(textField);

        panel.add(new SLabel("SFormattedTextField (NumberFormat) :\n[COMMIT_OR_REVERT]"));
        SFormattedTextField numberTextFieldCoR = new SFormattedTextField(new NumberFormatter());
        this.numberTextFieldCoR = numberTextFieldCoR;
        this.numberTextFieldCoR.setName("numberfieldCoR");
        numberTextFieldCoR.setToolTipText("Text entered here will be formatted as number when you leave focus.<br>" +
                "If you entered an invalid number the text should become red.<br>" +
                "This uses code executed on server side in Java!");
        numberTextFieldCoR.addDocumentListener(new MyDocumentListener(numberTextFieldCoR));
        numberTextFieldCoR.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        panel.add(numberTextFieldCoR);

        panel.add(new SLabel("SFormattedTextField (DateFormat) :\n[COMMIT_OR_REVERT]"));
        SFormattedTextField dateTextFieldCoR = new SFormattedTextField(dateFormatter);
        this.dateTextFieldCoR = dateTextFieldCoR;
        this.dateTextFieldCoR.setName("datefieldCoR");
        dateTextFieldCoR.setToolTipText("Enter a valid/invalid date here.<br>" +
                "Dates will be parsed on server side and reformatted accordingly.");
        dateTextFieldCoR.addDocumentListener(new MyDocumentListener(dateTextFieldCoR));
        dateTextFieldCoR.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        panel.add(dateTextFieldCoR);
        
        panel.add(new SLabel("SFormattedTextField (NumberFormat) :\n[COMMIT]"));
        SFormattedTextField numberTextFieldC = new SFormattedTextField(new NumberFormatter());
        this.numberTextFieldC = numberTextFieldC;
        this.numberTextFieldC.setName("numberfieldC");
        this.numberTextFieldC.setFocusLostBehavior( SFormattedTextField.COMMIT );
        numberTextFieldC.setToolTipText("Text entered here will be formatted as number when you leave focus.<br>" +
                "If you entered an invalid number the text should become red.<br>" +
                "This uses code executed on server side in Java!");
        numberTextFieldC.addDocumentListener(new MyDocumentListener(numberTextFieldC));
        numberTextFieldC.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        panel.add(numberTextFieldC);

        panel.add(new SLabel("SFormattedTextField (DateFormat) :\n[COMMIT]: "));
        SFormattedTextField dateTextFieldC = new SFormattedTextField(dateFormatter);
        this.dateTextFieldC = dateTextFieldC;
        this.dateTextFieldC.setName("datefieldC");
        this.dateTextFieldC.setFocusLostBehavior( SFormattedTextField.COMMIT );
        dateTextFieldC.setToolTipText("Enter a valid/invalid date here.<br>" +
                "Dates will be parsed on server side and reformatted accordingly.");
        dateTextFieldC.addDocumentListener(new MyDocumentListener(dateTextFieldC));
        dateTextFieldC.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        panel.add(dateTextFieldC);        

        panel.add(new SLabel("SPasswordField: "));
        SPasswordField passwordField = new SPasswordField();
        this.passwordField = passwordField;
        this.passwordField.setName("passwordfield");
        passwordField.setToolTipText("Just a regular passsword input.");
        passwordField.addDocumentListener(new MyDocumentListener(passwordField));
        passwordField.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        panel.add(passwordField);

        panel.add(new SLabel("STextArea: "));
        STextArea textArea = new STextArea("");
        this.textArea = textArea;
        this.textArea.setName("textarea");
        textArea.setPreferredSize(new SDimension(250, 50));
        textArea.setToolTipText("Okay - but don't start writing books now ;-)");
        textArea.addDocumentListener(new MyDocumentListener(textArea));
        textArea.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        panel.add(textArea);

        panel.add(new SLabel("SDocumentEvents: "));
        panel.add(eventLog);

        SLabel actionEventsLabel = new SLabel("ActionEvents (try Submit vs. Enter):");
        panel.add(actionEventsLabel);
        panel.add(actionEvent);

        panel.add(new SLabel());
        SButton button = new SButton("Submit");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionEvent.setText("Button clicked + ");
            }
        });
        button.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        panel.add(button);

        // Styling of all components.
        for (int i = 0; i < panel.getComponents().length; i++) {
            SComponent component = panel.getComponents()[i];
            component.setVerticalAlignment(SConstants.TOP);
            if ((component instanceof STextComponent) /*&& (component != disabledTextArea)*/ && (component != textArea))
                component.setPreferredSize(new SDimension("250px", null));
        }

        eventLog.setEditable(false); // for multiline label
        eventLog.setBorder(new SLineBorder(1));
        eventLog.setBackground(new Color(240, 240, 240));
        eventLog.setHorizontalAlignment(SConstants.LEFT_ALIGN);

        actionEvent.setBorder(new SLineBorder(1));
        actionEvent.setBackground(new Color(240, 240, 240));
        actionEvent.setHorizontalAlignment(SConstants.LEFT_ALIGN);

        controls.addControllable(textField);
        controls.addControllable(textArea);
        controls.addControllable(passwordField);
        controls.addControllable(numberTextFieldCoR);
        controls.addControllable(dateTextFieldCoR);
        controls.addControllable(numberTextFieldC);
        controls.addControllable(dateTextFieldC);

        addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionEvent.setText(actionEvent.getText() + " Form event");
            }
        });

        // Clear event log for every request
        getSession().addRequestListener(new SRequestListener() {
            public void processRequest(SRequestEvent e) {
                if (e.getType() == SRequestEvent.DISPATCH_START) {
                    if (eventLog.getText() == null || eventLog.getText().length() == 0)
                        eventLog.setText("(no document events fired)");
                    else
                        eventLog.setText(null);
                    if (actionEvent.getText() == null || actionEvent.getText().length() == 0)
                        actionEvent.setText("(no form or button event)");
                    else
                        actionEvent.setText(null);
                }
            }
        });

        return panel;
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

    class TextComponentControls
        extends ComponentControls
    {
        public TextComponentControls() {
            widthTextField.setText("100%");
            formComponentCheckBox.setVisible(false);

            final SCheckBox editable = new SCheckBox("editable");
            editable.setSelected(true);
            editable.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    textField.setEditable(editable.isSelected());
                    numberTextFieldCoR.setEditable(editable.isSelected());
                    dateTextFieldCoR.setEditable(editable.isSelected());
                    passwordField.setEditable(editable.isSelected());
                    textArea.setEditable(editable.isSelected());
                }
            });
            addControl(editable);
        }
    }
}
