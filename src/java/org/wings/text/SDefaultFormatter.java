/*
 * SDefaultFormatter.java
 *
 * Created on 9. September 2003, 14:16
 */

package org.wings.text;

import org.wings.script.ScriptListener;
import org.wings.script.JavaScriptListener;

import org.wings.SFormattedTextField;
import java.util.Vector;
import java.util.Iterator;
/**
 * SDefault Formatter simulates the normal behavior of a TextField.
 * (when an ActionListener is added, the Form will be submitted onchange)
 * @author  theresia
 */
public class SDefaultFormatter extends SAbstractFormatter{
    /** Creates a new instance of SDefaultFormatter */
    public SDefaultFormatter(){
    }
    
    public Object stringToValue(String text){
        return text;
    }
    
    public String valueToString ( Object value ) {
        return "";
    }
    
    /*
     * Creates the javaScriptFunction based on the given Format. Here isn't any
     * Format, so the SDefaultFormatter creates the code an ActionListener needs.
     */
    public JavaScriptListener generateJavaScript(SFormattedTextField field,boolean actionListener) {
        if(actionListener){
            JavaScriptListener javaScriptListener = new JavaScriptListener("onchange","submit()");
            return javaScriptListener;
        }
        else{
            return new JavaScriptListener("","");
        }
    }
    
}
