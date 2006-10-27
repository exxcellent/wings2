/*
 * SDateFormatter.java
 *
 * Created on 18. Juli 2006, 10:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wings.text;

import java.text.DateFormat;
import org.wings.session.SessionManager;

/**
 *
 * @author erik
 */
public class SDateFormatter extends SInternationalFormatter {
    
    DateFormat format = null;
    
    /** Creates a new instance of SDateFormatter */
    public SDateFormatter() {
        this( SessionManager.getSession() != null ? DateFormat.getDateInstance(DateFormat.DEFAULT, SessionManager.getSession().getLocale() ) : DateFormat.getDateInstance() );
    }
    
    public SDateFormatter( DateFormat dateFormat ) {
        super( dateFormat );
    }
    
}
