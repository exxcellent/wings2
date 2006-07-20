/*
 * XCalendar.java
 *
 * Created on 9. Juni 2006, 12:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wingx;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.Format;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.wings.SContainer;
import org.wings.SDimension;
import org.wings.SFormattedTextField;
import org.wings.SIcon;
import org.wings.SResourceIcon;
import org.wings.text.SAbstractFormatter;
import org.wings.text.SDateFormatter;
import org.wings.text.SInternationalFormatter;

/**
 *
 *  * @author <a href="mailto:e.habicht@thiesen.com">Erik Habicht</a>
 */
public class XCalendar extends SContainer {
    
    public static final SIcon   DEFAULT_DATE_ICON   = new SResourceIcon("org/wingx/calendar/images/date.png");
    private             SIcon   icon                = DEFAULT_DATE_ICON;

    private SFormattedTextField fTextField = new SFormattedTextField();
    
    private TimeZone timeZone = TimeZone.getDefault();
    
    private ActionListener actionListener = null;
    
    private ActionListener getActionListener() {
        if ( actionListener == null ) {
            actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireActionEvents();
                }
            };
        }
        return actionListener;
    }
    
    public void setCG(org.wingx.plaf.CalendarCG cg) {
        super.setCG( cg );
    }
    
    public XCalendar() {
        this( new SDateFormatter() );
    }
    
    public XCalendar( SDateFormatter formatter ) {
        this( new GregorianCalendar().getTime(), formatter );
    }
    
    public XCalendar( Date date, SDateFormatter formatter ) {
        getFormattedTextField().setFocusLostBehavior( SFormattedTextField.COMMIT_OR_REVERT );
        this.setFormatter( formatter );
        this.setDate( date );
        this.add( getFormattedTextField() );
        
    }
    
    /**
     * 
     */
    public void setIcon( SIcon icon ) {
        this.icon = icon;
    }
    
    public SIcon getIcon() {
        return this.icon;
    }
    
    /**
     * Sets the SAbstractFormatter
     * @param formatter SAbstractFormatter
     */
    public void setFormatter( SDateFormatter formatter ) {
        getFormattedTextField().setFormatter( formatter );
    }
    
    /**
     * Sets the TimeZone
     * @param timeZone TimeZone
     */
    public void setTimeZone( TimeZone timeZone ) {
        this.timeZone = timeZone;
        if ( timeZone != null ) {
            SAbstractFormatter aFormatter = getFormattedTextField().getFormatter();
            if ( aFormatter != null && aFormatter instanceof SInternationalFormatter ) {
                SInternationalFormatter iFormatter = (SInternationalFormatter)aFormatter;
                Format format = iFormatter.getFormat();
                if ( format != null && format instanceof DateFormat ) {
                    ((DateFormat)format).setTimeZone( timeZone );
                }
            }
        }
    }
    /**
     * Returns the TimeZone
     * @return TimeZone
     */
    public TimeZone getTimeZone () {
        return this.timeZone;
    }
    
    /**
     * Returns the selected date.
     * @return the selected date
     */
    public Date getDate() {
        return (Date)getFormattedTextField().getValue();
    }
    
    /**
     * Set the selected date.
     * @param date date
     */
    public void setDate( Date date ) {
        if ( date != null ) {
            getFormattedTextField().setValue( date );
        }
    }
    
    /**
     * Set the preferred size of the component
     * @param dimension SDimension
     */
    public void setPreferredSize( SDimension dimension ) {
        if ( dimension != null ) {
            getFormattedTextField().setPreferredSize( dimension );
        }
    }
    /**
     * Returns the preferred size of the component
     * @return SDimension
     */
    public SDimension getPreferredSize() {
        return getFormattedTextField().getPreferredSize();
    }
    
    /**
     * Fire an ActionEvent at each registered listener.
     *
     * @param event supplied ActionEvent
     */
    protected void fireActionPerformed(ActionEvent event) {
        // Guaranteed to return a non-null array
        Object[] listeners = getListenerList();
        ActionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                if (e == null) {
                    e = new ActionEvent(XCalendar.this,
                            ActionEvent.ACTION_PERFORMED,
                            "",
                            event.getWhen(),
                            event.getModifiers());
                }
                ((ActionListener) listeners[i + 1]).actionPerformed(e);
            }
        }
    }
    
    private void fireActionEvents() {
        fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
    }
    
    /**
     * Adds an ActionListener to the button.
     *
     * @param listener the ActionListener to be added
     */
    public void addActionListener(ActionListener listener) {
        addEventListener(ActionListener.class, listener);
        if ( getActionListeners().length > 0 && getFormattedTextField().getActionListeners().length == 0 ) {
            getFormattedTextField().addActionListener( getActionListener() );
        }
    }
    
    /**
     * Removes the supplied Listener from the listener list
     * @param listener ActionListener
     */
    public void removeActionListener(ActionListener listener) {
        removeEventListener(ActionListener.class, listener);
        if ( getActionListeners().length == 0 ) {
            getFormattedTextField().removeActionListener( getActionListener() );
        }
    }
    
    /**
     * Returns an array of all the <code>ActionListener</code>s added
     * to this AbstractButton with addActionListener().
     *
     * @return all of the <code>ActionListener</code>s added or an empty
     *         array if no listeners have been added
     */
    public ActionListener[] getActionListeners() {
        return (ActionListener[]) (getListeners(ActionListener.class));
    }
    
    public SFormattedTextField getFormattedTextField() {
        return this.fTextField;
    }
    
}

