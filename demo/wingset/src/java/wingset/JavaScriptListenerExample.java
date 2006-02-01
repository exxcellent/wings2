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

import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SComponent;
import org.wings.SForm;
import org.wings.SGridLayout;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.STextField;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;
import org.wings.style.CSSProperty;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Create some text fields and add two listeners: a standard server side
 * listener, that does a numerical calculation on the fields and a client
 * side JavaScript Listener that allows to calculate these fields as well.
 *
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public class JavaScriptListenerExample
        extends WingSetPane {
    /**
     * The JavaScript code that is executed on any change of input fields.
     * The curly braces with a number in it are replaced by the numbered
     * SComponent argument.
     */
    private final static String JS_ADD_SCRIPT =
            "document.getElementById('{2}').getElementsByTagName('input')[0].value" +
            " = ((1.0 * document.getElementById('{0}').getElementsByTagName('input')[0].value)" +
            " + (1.0 * document.getElementById('{1}').getElementsByTagName('input')[0].value));";

    private final static DecimalFormatSymbols DSYM
            = new DecimalFormatSymbols(Locale.US); // '.' as fraction separator

    public SComponent createExample() {
        SPanel p = new SPanel(new SBoxLayout(SBoxLayout.VERTICAL));
        p.add(new SLabel("The client side can handle simple events by JavaScript listeners. In this example, numbers are added locally."));

        final STextField firstField = createNumberField();
        final STextField secondField = createNumberField();
        final STextField sumField = createNumberField();
        SButton serverCalcButton = new SButton("sum");

        firstField.setFocusTraversalIndex(1);
        secondField.setFocusTraversalIndex(2);

        SGridLayout gridLayout = new SGridLayout(2);
        gridLayout.setHgap(4);
        gridLayout.setVgap(4);

        SForm form = new SForm(gridLayout);
        form.add(new SLabel("Value #1"));
        form.add(firstField);
        form.add(new SLabel("Value #2"));
        form.add(secondField);
        form.add(new SLabel("Sum"));
        form.add(sumField);
        form.add(new SLabel("Server calculation"));
        form.add(serverCalcButton);

        /*
         * The server side listener
         */
        serverCalcButton.addActionListener(new wingset.SerializableActionListener() {
            public void actionPerformed(ActionEvent ev) {
                doCalculation(firstField, secondField, sumField);
            }
        });

        /*
         * add the client side script listener. The variables 
         * in curly braces are replaced by the actual IDs of the components.
         */
        SComponent[] jsParams = new SComponent[]{firstField, secondField,
                                                 sumField};
        JavaScriptListener jsListener;
        jsListener = new JavaScriptListener(JavaScriptEvent.ON_CHANGE,
                JS_ADD_SCRIPT,
                jsParams);

        firstField.addScriptListener(jsListener);
        secondField.addScriptListener(jsListener);

        // any change to the sum field: no way, recalculate from source fields
        sumField.addScriptListener(jsListener);

        p.add(form);
        return p;
    }

    /**
     * do the calculation and normalize the output fields.
     */
    private void doCalculation(STextField a, STextField b, STextField sum) {
        double aNum = parseNumber(a);
        double bNum = parseNumber(b);
        if (Double.isNaN(aNum) || Double.isNaN(bNum)) {
            sum.setBackground(Color.RED);
            sum.setText("?");
        } else {
            sum.setBackground(null);
            /*
             * normalize the output: set the same number of decimal
             * digits for all fields.
             */
            int decimalsNeeded = Math.max(fractionDecimals(aNum),
                    fractionDecimals(bNum));
            NumberFormat fmt;
            fmt = new DecimalFormat("#.#", DSYM);
            fmt.setMinimumFractionDigits(decimalsNeeded);

            a.setText(fmt.format(aNum));
            b.setText(fmt.format(bNum));
            sum.setText(fmt.format(aNum + bNum));
        }
    }

    /**
     * returns the number of decimals needed to display the given
     * number
     */
    private int fractionDecimals(double number) {
        // is there a simple and more efficient way ?
        NumberFormat fmt = new DecimalFormat("#.########");
        String fractionStr = fmt.format(Math.IEEEremainder(number, 1.0));
        return fractionStr.length() - 2;
    }

    /**
     * parse a number in a text field. Assume an empty text field
     * to be '0', non-parseable values are NaN.
     */
    private double parseNumber(STextField field) {
        String text = field.getText().trim();
        if (text.length() == 0) {
            text = "0";
        }
        double result = Double.NaN;
        try {
            result = Double.parseDouble(text);
            field.setBackground(null);
        } catch (Exception e) {
            field.setBackground(Color.RED);
        }
        return result;
    }

    private STextField createNumberField() {
        STextField field = new STextField();
        field.setAttribute(CSSProperty.TEXT_ALIGN, "right");
        return field;
    }
}


