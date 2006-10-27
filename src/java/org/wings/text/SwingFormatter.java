package org.wings.text;

import javax.swing.*;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: hengels
 * Date: Jul 17, 2006
 * Time: 8:50:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class SwingFormatter
    extends SAbstractFormatter
{
    JFormattedTextField.AbstractFormatter formatter;

    public SwingFormatter(JFormattedTextField.AbstractFormatter formatter) {
        this.formatter = formatter;
    }

    public Object stringToValue(String text) throws ParseException {
        return formatter.stringToValue(text);
    }

    public String valueToString(Object value) throws ParseException {
        return formatter.valueToString(value);
    }
}
