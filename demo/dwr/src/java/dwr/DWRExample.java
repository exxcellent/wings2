/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package dwr;

import org.wings.session.Session;
import org.wings.session.SessionManager;
import org.wings.externalizer.ExternalizedResource;
import org.wings.event.SRequestEvent;
import org.wings.event.SRequestListener;
import org.wings.*;
import org.wings.resource.DefaultURLResource;
import org.wings.header.Script;
import org.wings.script.JavaScriptListener;
import org.wings.text.SAbstractFormatter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.io.IOException;

/**
 * @author hengels
 * @version $Revision$
 */
public class DWRExample
{
    private SFormattedTextField dateTextField;
    private SFormattedTextField numberTextField;
    private SButton button;
    private SFrame frame;

    public DWRExample() throws IOException {
        dateTextField = new SFormattedTextField(new DateFormatter());
        dateTextField.setName("dateTextField");

        numberTextField = new SFormattedTextField(new NumberFormatter());
        numberTextField.setName("numberTextField");

        button = new SButton("submit");

        SForm form = new SForm();
        form.setLayout(new STemplateLayout(SessionManager.getSession().getServletContext().getRealPath("/template/content.thtml")));
        form.add(dateTextField, "dateTextField");
        form.add(numberTextField, "numberTextField");
        form.add(button, "submit");

        frame = new SFrame("DWRExample example");
        frame.getContentPane().add(form);
        frame.show();
        frame.addHeader(new Script("JavaScript", "text/javascript", new DefaultURLResource("../dwr/engine.js")));
    }

    public static class DateFormatter extends DWRFormatter {
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, SessionManager.getSession().getLocale());

        public Object stringToValue(String text) throws ParseException {
            System.out.println("text = '" + text + "'");
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

        public String validate(String text) {
            try {
                String s = valueToString(stringToValue(text));
                System.out.println("return = '" + s + "'");
                return s;
            }
            catch (ParseException e) {
                return null;
            }
        }
    }

    public static class NumberFormatter extends DWRFormatter {
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

        public String validate(String text) {
            try {
                return valueToString(stringToValue(text));
            }
            catch (ParseException e) {
                return null;
            }
        }
    }
}
