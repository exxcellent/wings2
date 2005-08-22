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
import org.wings.border.SLineBorder;
import org.wings.session.SessionManager;
import org.wings.text.SNumberFormatter;
import org.wings.text.SAbstractFormatter;
import org.wings.event.SDocumentListener;
import org.wings.event.SDocumentEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.text.NumberFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Iterator;

/**
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public class TextComponentExample
        extends WingSetPane
{
    private ComponentControls controls;
    private SLabel documentEvent = new SLabel(" ");
    private SLabel actionEvent = new SLabel(" ");

    SDocumentListener documentListener = new SDocumentListener() {
                public void insertUpdate(SDocumentEvent e) {
                    documentEvent.setText(((STextComponent) e.getSource()).getName());
                    actionEvent.setText("button not pressed");
                }

                public void removeUpdate(SDocumentEvent e) {
                    documentEvent.setText(((STextComponent) e.getSource()).getName());
                    actionEvent.setText("button not pressed");
                }

                public void changedUpdate(SDocumentEvent e) {
                    if (e.getSource() instanceof STextComponent) {
                        STextComponent component = (STextComponent) e.getSource();
                        documentEvent.setText(((STextComponent) e.getSource()).getName());
                    }
                    else {
                        log.debug("e.getSource().getClass() = " + e.getSource().getClass());
                    }
                    actionEvent.setText("button not pressed");
                }
            };

    public SComponent createExample() {
        controls = new ComponentControls();

        SGridLayout gridLayout = new SGridLayout(2);
        gridLayout.setHgap(4);
        gridLayout.setVgap(4);
        SPanel p = new SPanel(gridLayout);

        p.add(new SLabel("STextField: "));
        STextField textField = new STextField();
        textField.setName("textfield");
        textField.addDocumentListener(documentListener);
        p.add(textField);

        p.add(new SLabel("SFormattedTextField (NumberFormat): "));
        SFormattedTextField numberTextField = new SFormattedTextField(new NumberFormatter());
        numberTextField.setName("numberfield");
        numberTextField.addDocumentListener(documentListener);
        p.add(numberTextField);

        p.add(new SLabel("SFormattedTextField (DateFormat): "));
        SFormattedTextField dateTextField = new SFormattedTextField(new DateFormatter());
        dateTextField.setName("datefield");
        dateTextField.addDocumentListener(documentListener);
        p.add(dateTextField);

        p.add(new SLabel("STextArea: "));
        STextArea textArea = new STextArea();
        textArea.setName("textarea");
        textArea.addDocumentListener(documentListener);
        p.add(textArea);

        p.add(new SLabel("Multiline Label: "));
        //SPanel sp = new SPanel();
        STextArea disabledTextArea = new STextArea("A very simple multiline text\nonly seperated by \\n width and height is controlled by\nthe size of the text.\nThis can be achieved with not editable STextArea");
        disabledTextArea.setName("multilineArea");
        disabledTextArea.setEditable(false);
        disabledTextArea.setBorder(new org.wings.border.SLineBorder());
        p.add(disabledTextArea);
        //p.add(sp);

        
        p.add(new SLabel("DocumentEvent: "));
        p.add(documentEvent);

        SButton button = new SButton("Submit");
        p.add(button);
        p.add(actionEvent);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionEvent.setText("Button pressed");
            }
        });

        SForm f = new SForm(new SBorderLayout());
        f.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionEvent.setText(actionEvent.getText() + " + form event");
            }
        });

        for (int i = 0; i < p.getComponents().length; i++) {
            SComponent component = p.getComponents()[i];
            component.setVerticalAlignment(SConstants.TOP);
            if ((component instanceof STextComponent) && (component != disabledTextArea))
                component.setPreferredSize(new SDimension("200px", null));
        }
        documentEvent.setBorder(new SLineBorder(1));
        documentEvent.setBackground(Color.LIGHT_GRAY.brighter());
        documentEvent.setPreferredSize(new SDimension("200px", null));
        actionEvent.setBorder(new SLineBorder(1));
        actionEvent.setBackground(Color.LIGHT_GRAY.brighter());
        actionEvent.setPreferredSize(new SDimension("200px", null));

        controls.addSizable(textField);
        controls.addSizable(textArea);
        controls.addSizable(dateTextField);


        f.add(controls, SBorderLayout.NORTH);
        f.add(p, SBorderLayout.CENTER);
        return f;
    }

    public static class DateFormatter extends SAbstractFormatter {
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, SessionManager.getSession().getLocale());

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

    public static class NumberFormatter extends SAbstractFormatter {
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
}


