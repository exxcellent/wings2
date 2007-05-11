/*
 * SDefaultFormatter.java
 *
 * Created on 4. September 2006, 14:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wings.text;


/**
 *
 * @author erik
 */
public class SDefaultFormatter extends SAbstractFormatter {
    
    private Class valueClass = null;
    
    /** Creates a new instance of SDefaultFormatter */
    public SDefaultFormatter() {
    }
    
    /**
     * @param text String to convert
     * @return Object representation of text
     */
    public Object stringToValue(String text) throws java.text.ParseException {
        return text;
    }

    /**
     * @param value Value to convert
     * @return String representation of value
     */
    public String valueToString(Object value) throws java.text.ParseException {
        String string = "";
        if ( value != null )
            string = value.toString();
        return string;
    }
    
    /**
     * Sets the valueClass
     * @param valueClass the valueClass
     */
    public void setValueClass( Class<?> valueClass ) {
        this.valueClass = valueClass;
    }
    
    /**
     * Returns the valueClass
     * @return Class the valueClass
     */
    public Class<?> getValueClass() {
        return valueClass;
    }
    
    
    
}
