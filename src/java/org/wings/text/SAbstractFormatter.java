/*
 * SAbstractFormatter.java
 *
 * Created on 11. September 2003, 11:29
 */

package org.wings.text;

import org.wings.script.JavaScriptListener;
import org.wings.SFormattedTextField;
import java.util.*;
/**
 *
 * @author  theresia
 */
public abstract class SAbstractFormatter {

    private Vector textFields = null;
    private Vector jScripts = null;
    
     
    public SAbstractFormatter(){
        textFields = new Vector();
        jScripts = new Vector();
    }
    
    
    /*
     * Installs a SFormattedTextField to the Formatter. This is important, because the
     * Formatter must create JavaScript for the FormattedTextField everytime the Formatter
     * is changed.
     **/
    public void install(SFormattedTextField textField) {
        boolean b = false;
        if (textField != null){
            for(Iterator i = textFields.iterator(); i.hasNext();){
                if(i.next() == textField){
                    return;
                }
            }//for
            textFields.add(textField);
            if(textField.getActionListeners().length > 0){
                b = true;
            }
            
            JavaScriptListener jScriptListener = generateJavaScript(textField,b);
            
            textField.addScriptListener(jScriptListener);
            
            jScripts.add(jScriptListener);
        }       
    }
    
    /*
     * removes the Formatter
     */
    public void uninstall(SFormattedTextField textField){
        if(textField != null){
            for(int i = 0; i < textFields.size();i++){
                if(textFields.get(i) == textField){
                    textField.removeScriptListener((JavaScriptListener)jScripts.get(i));
                    jScripts.remove(i);
                    textFields.remove(i);
                }
            }
        }
    }
    
    
    /* 
     * Generates the new JavaScript for all registered SFormattedTextFields.
     * New code must be created if the Format of the formatter changes or if
     * a new ActionListener is registered.
     */
    public void updateFormatter(){
        if(textFields.size() > 0){
            Vector tempJScripts = new Vector();
            for(int i = 0; i < textFields.size(); i++){
               SFormattedTextField tf = (SFormattedTextField)textFields.get(i);
               tf.removeScriptListener((JavaScriptListener)jScripts.get(i));
               boolean b = false;
               if(tf.getActionListeners().length > 0) {
                   b = true;
               }
               
               JavaScriptListener jl = generateJavaScript(tf, b);
               //JavaScriptListener jl = generateJavaScript(tf.getEncodedLowLevelEventId(), b);
               tf.addScriptListener(jl);
               tempJScripts.add(jl);

            }
           jScripts = tempJScripts;
        }
        
       /* if(javaScriptListener != null){
            textField.removeScriptListener(javaScriptListener);
        }
        textField.addScriptListener(generateJavaScript(textField.getEncodedLowLevelEventId()));*/
    }
    
    public abstract JavaScriptListener generateJavaScript(SFormattedTextField field, boolean actionListener);
    
    /**
     * @param text String to convert
     * @return Object representation of text
     */    
    public abstract Object stringToValue(String text);
    /**
     * @param value Value to convert
     * @return String representation of value
     */    
    public abstract String valueToString( Object value );
}
