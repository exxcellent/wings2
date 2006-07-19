/*
 * SInternationalFormatter.java
 *
 * Created on 18. Juli 2006, 12:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wings.text;

/**
 *
 * @author erik
 */
public class SInternationalFormatter extends SAbstractFormatter {
    
    java.text.Format format = null;
    
    /** Creates a new instance of SInternationalFormatter */
    public SInternationalFormatter( java.text.Format format ) {
        setFormat( format );
    }
    
    public void setFormat( java.text.Format format ) {
        this.format = format;
    }
    
    public java.text.Format getFormat() {
        return this.format;
    }
    
    /**
     * @param text String to convert
     * @return Object representation of text
     */
    public Object stringToValue(String text) throws java.text.ParseException {
        Object value = null;
        if ( format == null ) {
            value = text;
        } else {
            if ( text == null ) text = "";
            value = format.parseObject( text );
        }
        return value;
    }

    /**
     * @param value Value to convert
     * @return String representation of value
     */
    public String valueToString(Object value) throws java.text.ParseException {
        String string = "";
        if ( format != null ) {
            string = format.format( value );
        }
        return string;
    }
    
    public void setMinimum( Comparable min ) {
    }
    
    public void setMaximum( Comparable max ) {
    }
    
}
