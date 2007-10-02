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
import org.wings.text.SDefaultFormatterFactory;
import org.wings.text.SAbstractFormatter;
import org.wings.text.SDateFormatter;
import org.wings.text.SInternationalFormatter;

/**
 * @author <a href="mailto:e.habich@@thiesen.com">Erik Habicht</a>
 */
public class XCalendar extends SContainer {
    
    /**
     * The <code>XCalendar</code> default <code>SIcon</code>.
     */
    public static final SIcon   DEFAULT_DATE_ICON   = new SResourceIcon("org/wingx/calendar/images/date.png");
    
    /**
     * The current <code>XCalendar</code>'s <code>SIcon</code>.
     */
    private             SIcon   icon                = DEFAULT_DATE_ICON;

    /** 
     * The current <code>SFormattedTextField</code>.
     */
    private SFormattedTextField fTextField;
    
    /**
     * The current <code>TimeZone</code>
     */
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
    
    /**
     * Set the look and feel delegate for this component
     * 
     * @see org.wings.SComponent#setCG
     * @param cg the component's cg
     */
    public void setCG(org.wingx.plaf.CalendarCG cg) {
        super.setCG( cg );
    }
    
    /**
     * Creates a <code>XCalendar</code> with the current Date and a default <code>SDateFormatter</code>.
     */
    public XCalendar() {
        this( new SDateFormatter() );
    }
    
    /**
     * Creates a <code>XCalendar</code> with the current Date and the specified <code>SDateFormatter</code>
     * instance.
     * @param formatter <code>SDateFormatter</code> to use for formatting.
     */
    public XCalendar( SDateFormatter formatter ) {
        this( new GregorianCalendar().getTime(), formatter );
    }
    
    /**
     * Creates a <code>XCalendar</code> with the given Date and the specified <code>SDateFormatter</code> 
     * instance.
     * @param date Date
     * @param formatter <code>SDateFormatter</code> to use for formatting.
     */
    public XCalendar( Date date, SDateFormatter formatter ) {
        add(getFormattedTextField());
        setFormatter( formatter );
        setDate( date );
    }
    
    /**
     * Sets the <code>XCalendar</code>'s default <code>SIcon</code>.
     * @param icon the icon used as the default image.
     */
    public void setIcon( SIcon icon ) {
        this.icon = icon;
    }
    
    /**
     * Returns the default <code>SIcon</code>.
     * @return the default <code>SIcon</code>
     */
    public SIcon getIcon() {
        return this.icon;
    }
    
    /**
     * Sets the current <code>SDateFormatter</code>.
     * @param formatter <code>SDateFormatter</code> to use for formatting
     */
    public void setFormatter( SDateFormatter formatter ) {
        getFormattedTextField().setFormatterFactory( new SDefaultFormatterFactory( formatter ) );
    }
    
    /**
     * Sets the current <code>TimeZone</code>.
     * @param timeZone <code>TimeZone</code>
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
     * Returns the current <code>TimeZone</code>.
     * @return <code>TimeZone</code>
     */
    public TimeZone getTimeZone () {
        return this.timeZone;
    }
    
    /**
     * Returns the current <code>Date</code>.
     * @return the current <code>Date</code>
     */
    public Date getDate() {
        return (Date)getFormattedTextField().getValue();
    }
    
    /**
     * Sets the current <code>Date</code>.
     * @param date the current <code>Date</code>
     */
    public void setDate( Date date ) {
        if ( date != null ) {
            getFormattedTextField().setValue( date );
        }
    }
    
    /**
     * Set the preferred size of the component
     * @param dimension <code>SDimension</code>
     */
    public void setPreferredSize( SDimension dimension ) {
        super.setPreferredSize(dimension);
        getFormattedTextField().setPreferredSize(dimension != null && dimension.getWidth() != null ? SDimension.FULLWIDTH : null);
    }

    /**
     * Fire an <code>ActionEvent</code> at each registered listener.
     *
     * @param event supplied <code>ActionEvent</code>
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
     * Adds an action listener to the <code>XCalendar</code>
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
    
    /**
     * Returns the current <code>SFormattedTextField</code>.
     * @return the current <code>SFormattedTextField</code>
     */
    public SFormattedTextField getFormattedTextField() {
        if (fTextField == null)
            fTextField = new SFormattedTextField();
        return fTextField;
    }
}
