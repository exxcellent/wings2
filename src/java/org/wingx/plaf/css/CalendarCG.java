/*
 * CalendarCG.java
 *
 * Created on 12. Juni 2006, 09:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wingx.plaf.css;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.wings.*;
import org.wings.event.SParentFrameListener;
import java.util.*;
import org.wings.plaf.css.AbstractComponentCG;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Link;
import org.wings.header.Script;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;
import org.wings.session.SessionManager;
import org.wingx.XCalendar;

/**
 *
 *  * @author <a href="mailto:e.habicht@thiesen.com">Erik Habicht</a>
 */
public class CalendarCG extends AbstractComponentCG implements org.wingx.plaf.CalendarCG, SParentFrameListener {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    
    public void installCG(final SComponent comp) {
        super.installCG(comp);
        comp.addParentFrameListener( this );
    }
    
    private void addHeaders ( SFrame parentFrame ) {
        ClasspathResource res = new ClasspathResource("org/wingx/calendar/calendar.css", "text/css");
        String jScriptUrl = SessionManager.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        parentFrame.addHeader( new Link("stylesheet", null, "text/css", null, new DefaultURLResource(jScriptUrl)));
        
        addExternalizedScriptHeader( parentFrame, "org/wingx/calendar/calendar.js", "text/javascript" );
        addExternalizedScriptHeader( parentFrame, getLangScriptURL(), "text/javascript" );
        addExternalizedScriptHeader( parentFrame, "org/wingx/calendar/calendar-setup.js", "text/javascript" );
    }
    
    private void addExternalizedScriptHeader(SFrame parentFrame, String classPath, String mimeType) {
        ClasspathResource res = new ClasspathResource(classPath, mimeType);
        String jScriptUrl = SessionManager.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        parentFrame.addHeader(new Script(mimeType, new DefaultURLResource(jScriptUrl)));
    }
    
    /**
     * Returns the language file.
     *
     */
    private String getLangScriptURL() {
        String retVal = "org/wingx/calendar/lang/calendar-" + getLocale().getLanguage() + ".js";
        java.net.URL url = org.wings.plaf.MenuCG.class.getClassLoader().getResource( retVal );
        if ( url == null ) {
            retVal = "org/wingx/calendar/lang/calendar-en.js";
        }
        return retVal;
    }

    public Locale getLocale( ) {
        return SessionManager.getSession().getLocale() != null ? SessionManager.getSession().getLocale() : Locale.getDefault();
    }
    
    public void writeInternal(org.wings.io.Device device, org.wings.SComponent _c )
    throws java.io.IOException {
        if ( !_c.isVisible() ) return;
        _c.fireRenderEvent(SComponent.START_RENDERING);
        final XCalendar component = (org.wingx.XCalendar) _c;

        SFormattedTextField fTextField = component.getFormattedTextField();
          

        String textFieldId  = "document.getElementById('" + fTextField.getName() + "')";
        String funcPrefix   = component.getName();
        String callable     = (String)component.getClientProperty( "callable" );
        
        fTextField.setEnabled( component.isEnabled() );
        fTextField.addScriptListener( new JavaScriptListener( JavaScriptEvent.ON_CHANGE, funcPrefix+"_onFieldChange("+textFieldId+".value)" ) );
        
        dateFormat.setTimeZone( component.getTimeZone() );
        
        device.print("\n<input type=\"hidden\" id=\""+callable+"\" name=\""+callable+"\" value=\""+dateFormat.format( component.getDate() )+"\">\n");
        
        device.print("<table cellspacing=\"0\" cellpadding=\"0\"><tr><td>\n");
        
        fTextField.write(device);
        
        device.print("\n</td><td>\n");
       
        SLabel label = new SLabel();
        device.print("<input style=\"margin-left:2px;\" type=\"image\" id=\""+label.getName()+"\" src=\""+component.getIcon().getURL()+"\"");
        if ( !component.isEnabled() ) {
            device.print( " disabled");
        }
        device.print(">\n");
        
        device.print("</td></tr></table>\n");
        
        device.print("<script type=\"text/javascript\">\n");
            printJavaScriptFunctions( device, callable, textFieldId, component, funcPrefix );
            printCalendarSetup( device, callable, label.getName(), funcPrefix );
        device.print("</script>\n" );

        _c.fireRenderEvent(SComponent.DONE_RENDERING);
        
    }
    
    private final void printJavaScriptFunctions( org.wings.io.Device device, String callable, String textFieldId, XCalendar cal, String functionPrefix )
    throws IOException {
        device.print(" function "+functionPrefix+"_onFieldChange(dateString) {");
        device.print(" "+callable+".onFieldChange( "+functionPrefix+"_onFieldChangeCallback, dateString );");
        device.print(" }\n");
        device.print(" function "+functionPrefix+"_onFieldChangeCallback(data) {");
        device.print(" document.getElementById('"+callable+"').value = data;");
        device.print( "}\n");
        device.print(" function "+functionPrefix+"_onCalUpdate(cal) {");
        device.print(" "+callable+".onCalUpdate( "+functionPrefix+"_onCalUpdateCallback, cal.date );");
        device.print(" }\n");
        device.print(" function "+functionPrefix+"_onCalUpdateCallback(data) {");
        device.print(" "+textFieldId+".value = data;");
        if ( cal.getActionListeners().length > 0 ) {
            device.print("   "+textFieldId+".form.submit();");
        } 
        device.print(" }\n");        
    }
    
    private final void printCalendarSetup( org.wings.io.Device device, String callable, String buttonName, String functionPrefix )
    throws IOException {
        device.print(" Calendar.setup({" );
        device.print(" inputField  : \"" ).print(callable).print("\"");
        device.print(", ifFormat : \"%Y.%m.%d\"");
        device.print(", button : \"").print(buttonName).print("\"");
        device.print(", showOthers : true" );
        device.print(", electric : false" );
        device.print(", onUpdate : "+functionPrefix+"_onCalUpdate" );
        device.print(" });\n" );
    }
    
    public void parentFrameAdded(org.wings.event.SParentFrameEvent e ) {
        addHeaders( e.getParentFrame() );
        registerCallable(e.getComponent());
    }
    
    public void parentFrameRemoved(org.wings.event.SParentFrameEvent e ) {
        unregisterCallable(e.getComponent());
    }
    
    public final static class CallableCalendar {

        private SFormattedTextField fTextField  = null;
        private TimeZone            timeZone    = null;

        public CallableCalendar( SFormattedTextField fTextField, TimeZone timeZone ){
            this.fTextField = fTextField;
            this.timeZone = timeZone;
        }
        /* Timestamt to Human readable */
        public String onCalUpdate(String test) {
            String retVal = "";
            try {
                Date newDate = new Date( Long.parseLong( test ) );
                retVal = fTextField.getFormatter().valueToString( newDate );
            } catch ( ParseException e ) {
                retVal = "";
            }
            return retVal;
        }
        /* Human readabel to JSCalendar understandable */
        public String onFieldChange(String test) {
            String retVal = "";
            dateFormat.setTimeZone( this.timeZone );
            try {
                Date date = (Date)fTextField.getFormatter().stringToValue( test );
                retVal = dateFormat.format( date );
            } catch ( ParseException e ) {
                retVal = dateFormat.format( new GregorianCalendar().getTime() );
            }
            return retVal;
        }
        
    }   
    
    private void registerCallable(SComponent component) {
        XCalendar calendar = (XCalendar)component;
        CallableCalendar callableCalendar = new CallableCalendar( calendar.getFormattedTextField(), calendar.getTimeZone() );
        String name = "xcalendar_" + System.identityHashCode(callableCalendar);
        if (!CallableManager.getInstance().containsCallable(name)) {
            CallableManager.getInstance().registerCallable(name, callableCalendar);
            component.putClientProperty( "callable", name );
        }
    }
    
    private void unregisterCallable(SComponent component) {
        CallableManager.getInstance().unregisterCallable( (String)component.getClientProperty("callable") );
    }
}