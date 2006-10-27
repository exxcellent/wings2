/*
 * SNumberFormatter.java
 *
 * Created on 18. Juli 2006, 10:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wings.text;

import java.text.Format;
import java.text.NumberFormat;
import org.wings.session.SessionManager;

/**
 *
 * @author erik
 */
public class SNumberFormatter extends SInternationalFormatter {
    
    Format format = null;
    
    /** Creates a new instance of SNumberFormatter */
    public SNumberFormatter( NumberFormat format ) {
        super( format );
    }
    
    public SNumberFormatter() {
        this( SessionManager.getSession() != null ? NumberFormat.getNumberInstance( SessionManager.getSession().getLocale() ) : NumberFormat.getNumberInstance() );
    }
    
}
