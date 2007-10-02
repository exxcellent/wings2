/*
 * SInternationalFormatter.java
 *
 * Created on 18. Juli 2006, 12:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wings.text;

import java.text.Format;
import java.text.ParseException;

/**
 * <code>SInternationalFormatter</code> extends <code>SDefaultFormatter</code>,
 * using an instance of <code>java.text.Format</code> to handle the
 * conversion to a String, and the conversion from a String.
 *
 * @author erik
 */
public class SInternationalFormatter extends SDefaultFormatter {
    
    java.text.Format format = null;
    
    /** Creates a new instance of SInternationalFormatter */
    public SInternationalFormatter( Format format ) {
        setFormat( format );
    }

    /**
     * Sets the format that dictates the legal values that can be edited and displayed.
     * @param format The format that dictates the legal values that can be edited and displayed.
     */
    public void setFormat( Format format ) {
        this.format = format;
    }

    /**
     *  Returns the format that dictates the legal values that can be edited and displayed.
     * @return The format that dictates the legal values that can be edited and displayed.
     */
    public Format getFormat() {
        return this.format;
    }
    
    /**
     * Object representation of text.
     * @param text String to convert
     * @return Object representation of text
     */
    public Object stringToValue(String text) throws ParseException {
        Object value;
        if ( format == null ) {
            value = text;
        } else {
            if ( text == null ) text = "";
            value = format.parseObject( text );
        }
        return value;
    }

    /**
     * String representation of value.
     * @param value Value to convert
     * @return String representation of value
     */
    public String valueToString(Object value) throws ParseException {
        if (value == null) {
            return "";
        }
        String string = "";
        if ( format != null ) {
            string = format.format( value );
        }
        return string;
    }

    /**
     * Not implemented yet.
     * @deprecated
     */
    public void setMinimum( Comparable min ) {
    }

    /**
     * Not implemented yet.
     * @deprecated
     */
    public void setMaximum( Comparable max ) {
    }

}
