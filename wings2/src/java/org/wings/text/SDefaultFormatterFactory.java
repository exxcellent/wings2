/*
 * DefaultFormatterFactory.java
 *
 * Created on 4. September 2006, 11:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wings.text;

import org.wings.SFormattedTextField;

/**
 *
 * @author erik
 */
public class SDefaultFormatterFactory extends SFormattedTextField.SAbstractFormatterFactory {
    
    private SAbstractFormatter defaultFormat;
    private SAbstractFormatter displayFormat;
    private SAbstractFormatter editFormat;
    private SAbstractFormatter nullFormat;
    
    /** Creates a new instance of DefaultFormatterFactory */
    public SDefaultFormatterFactory() {
    }
    
    public SDefaultFormatterFactory(SAbstractFormatter defaultFormat) {
        this(defaultFormat, null);
    }

    public SDefaultFormatterFactory( SAbstractFormatter defaultFormat, SAbstractFormatter displayFormat) {
        this(defaultFormat, displayFormat, null);
    }

    public SDefaultFormatterFactory( SAbstractFormatter defaultFormat, SAbstractFormatter displayFormat, SAbstractFormatter editFormat) {
        this(defaultFormat, displayFormat, editFormat, null);
    }

    public SDefaultFormatterFactory( SAbstractFormatter defaultFormat, SAbstractFormatter displayFormat, SAbstractFormatter editFormat, SAbstractFormatter nullFormat) {
        this.defaultFormat = defaultFormat;
        this.displayFormat = displayFormat;
        this.editFormat = editFormat;
        this.nullFormat = nullFormat;
    }
    
        public void setDefaultFormatter( SAbstractFormatter atf ){
        defaultFormat = atf;
    }


    public SAbstractFormatter getDefaultFormatter() {
        return defaultFormat;
    }

    public void setDisplayFormatter( SAbstractFormatter atf ){
        this.displayFormat = atf;
    }

    public SAbstractFormatter getDisplayFormatter() {
        return displayFormat;
    }

    public void setEditFormatter( SAbstractFormatter atf ) {
        this.editFormat = atf;
    }

     public SAbstractFormatter getEditFormatter() {
        return editFormat;
    }

    public void setNullFormatter( SAbstractFormatter atf ) {
        this.nullFormat = atf;
    }

    public SAbstractFormatter getNullFormatter() {
        return nullFormat;
    }

    public SAbstractFormatter getFormatter( SFormattedTextField source ) {
        SAbstractFormatter format = null;
        
        if (source != null) {
            
            Object value = source.getText();
            
            if (value == null) {
                format = getNullFormatter();
            }
            if (format == null) {
                format = getDisplayFormatter();
                if (format == null) {
                    format = getDefaultFormatter();
                }
            }
        }
        return format;
    }
   
}
