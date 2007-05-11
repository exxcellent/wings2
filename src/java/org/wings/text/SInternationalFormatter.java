/*
 * SInternationalFormatter.java
 *
 * Created on 18. Juli 2006, 12:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wings.text;

import java.lang.reflect.Constructor;
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
    
    /**
     * Can be used to impose a maximum value.
     */
    private Comparable max;
    
    /**
     * Can be used to impose a minimum value.
     */
    private Comparable min;

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

        if ( getValueClass() != null && value != null && !getValueClass().isInstance(value) ) {
            Constructor constructor = null;
            try {
                constructor = getValueClass().getConstructor( new Class[] { String.class } );
                value = constructor.newInstance( new Object[] { value.toString() } );
            } catch ( Exception e ) {
                throw new ParseException( "Error", 0 );
            }
        }

        boolean isValid = true;
        try {
            if ( getMinimum() != null && getMinimum().compareTo( value ) > 0 ) {
                isValid = false;
            }
            if ( getMaximum() != null && getMaximum().compareTo( value ) < 0 ) {
                isValid = false;
            }
        } catch ( ClassCastException c ) {
            isValid = false;
        }
        if ( isValid == false ) {
            throw new ParseException("Value not within min/max range", 0);     
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
     * Sets the minimum valid value
     * @param min the minimum valid value
     */
    public void setMinimum( Comparable min ) {
        if ( getValueClass() == null && min != null ) {
            setValueClass( min.getClass() );
        }
        this.min = min;
    }
    
    /**
     * Return the minimum valid value
     * @return Comparable the minimum valid value
     */
    public Comparable getMinimum() {
        return min;
    }

    /**
     * Sets the maximum valid value
     * @param max the maximum valid value
     */
    public void setMaximum( Comparable max ) {
        if ( getValueClass() == null && max != null ) {
            setValueClass( max.getClass() );
        }
        this.max = max;
    }   
    
    /**
     * Returns the maximum valid value
     * @return Comparable the maximum valid value
     */
    public Comparable getMaximum() {
        return max;
    }
    

}
