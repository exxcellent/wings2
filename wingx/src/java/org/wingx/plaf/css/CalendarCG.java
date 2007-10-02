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
import java.text.*;

import org.wings.*;
import org.wings.text.SAbstractFormatter;
import org.wings.util.SessionLocal;
import java.util.*;
import org.wings.plaf.css.*;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.*;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.resource.ClassPathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.session.SessionManager;
import org.wings.session.Session;
import org.wingx.XCalendar;

/**
 *
 *  * @author <a href="mailto:e.habicht@thiesen.com">Erik Habicht</a>
 */
public class CalendarCG
    extends AbstractComponentCG
    implements org.wingx.plaf.CalendarCG
{
    private SessionLocal callableCalendar = new SessionLocal();
    private HeaderUtil headerUtil = new HeaderUtil();

    private static HeaderUtil headerUtilYUI = null;
    
    public static void installYuiHeaders() {
        if ( headerUtilYUI == null ) {
            headerUtilYUI = new HeaderUtil();
            Session session = SessionManager.getSession();
            ClassPathResource res = new ClassPathResource("org/wingx/grid/utilities.js", "text/javascript");
            String jScriptUrl = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
            Script yuiHeader = new Script("text/javascript", new DefaultURLResource(jScriptUrl));
            headerUtilYUI.addHeader( yuiHeader );
        }
        headerUtilYUI.installHeaders();
    }
    
    public CalendarCG() {
        Session session = SessionManager.getSession();
        ClassPathResource res1 = new ClassPathResource("org/wingx/calendar/calendar.css", "text/css");
        String url1 = session.getExternalizeManager().externalize(res1, ExternalizeManager.GLOBAL);
        headerUtil.addHeader(new Link("stylesheet", null, "text/css", null, new DefaultURLResource(url1)));
        headerUtil.addHeader(createExternalizedScriptHeader( session, "org/wingx/calendar/calendar.js", "text/javascript" ));
        headerUtil.addHeader(createExternalizedScriptHeader( session, getLangScriptURL(), "text/javascript" ));
        headerUtil.addHeader(createExternalizedScriptHeader( session, "org/wingx/calendar/calendar-setup.js", "text/javascript" ));
        headerUtil.addHeader(createExternalizedScriptHeader( session, "org/wingx/calendar/xcalendar.js", "text/javascript" ));
    }

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        if (!CallableManager.getInstance().containsCallable("xcalendar"))
            CallableManager.getInstance().registerCallable("xcalendar", getCallableCalendar());

        installYuiHeaders();
        headerUtil.installHeaders();
    }

    private Script createExternalizedScriptHeader(Session session, String ClassPath, String mimeType) {
        ClassPathResource res = new ClassPathResource(ClassPath, mimeType);
        String jScriptUrl = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        return new Script(mimeType, new DefaultURLResource(jScriptUrl));
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
        Session session = SessionManager.getSession();
        return session.getLocale() != null ? session.getLocale() : Locale.getDefault();
    }

    public void writeInternal(org.wings.io.Device device, org.wings.SComponent _c )
    throws java.io.IOException {

        final XCalendar component = (org.wingx.XCalendar) _c;

        final String id_hidden = "hidden" + component.getName();
        final String id_button = "button" + component.getName();
        final String id_clear = "clear" + component.getName();
        
        SFormattedTextField fTextField = component.getFormattedTextField();
        String key = getCallableCalendar().registerFormatter(fTextField.getFormatter());

        SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy.MM.dd");
        dateFormat.setTimeZone( component.getTimeZone() );

        device.print("<table");
        writeAllAttributes(device, component);
        device.print("><tr><td class=\"tf\"");

        int oversizePadding = Utils.calculateHorizontalOversize(fTextField, true);
        //oversizePadding += RenderHelper.getInstance(component).getHorizontalLayoutPadding();

        if (oversizePadding != 0)
            Utils.optAttribute(device, "oversize", oversizePadding);
        device.print(">");

        fTextField.setEnabled( component.isEnabled() );
        fTextField.write(device);

        device.print("\n</td><td class=\"b\">\n");
        device.print("<input type=\"hidden\" id=\""+id_hidden+"\" name=\""+id_hidden+"\" formatter=\""+key+"\" value=\""+ format(dateFormat, component.getDate() )+"\">\n");
        device.print("<input class=\"calendar_button\" type=\"image\" id=\""+id_button+"\" src=\""+component.getEditIcon().getURL()+"\"");
        if ( !component.isEnabled() ) {
            device.print( " disabled");
        }
        device.print(">\n");

        device.print("</td><td class=\"cb\" width=\"0%\">\n");
        if (component.isNullable() && component.getClearIcon() != null) {
            device.print("<input class=\"calendar_clear_button\" type=\"image\" id=\""+id_clear+"\" src=\""+component.getClearIcon().getURL()+"\"");
            if ( !component.isEnabled() ) {
                device.print( " disabled");
            }
            device.print(">\n");
        }

        device.print("<script type=\"text/javascript\">\n");
        printCalendarSetup( device, id_hidden, fTextField.getName(), id_button, key, id_clear, component );
        device.print("</script>\n" );

        writeTableSuffix(device, component);
    }

    private String format(DateFormat dateFormat, Date date) {
        if (date == null)
            date = new Date();
        return date != null ? dateFormat.format( date ) : "";
    }

    private void printCalendarSetup( final org.wings.io.Device device, String hiddenName,
                                     String textFieldName, String buttonName, String formatterKey, String clearButton, XCalendar cal )
        throws IOException {
        
        String onUpdateCommit = "false";
        if ( cal.getActionListeners().length > 0 ) {
            onUpdateCommit = "true";
        }
        
        device.print("var xCalendarObj = new XCalendar(\"").print(formatterKey).print("\", \"")
                .print(hiddenName).print("\", \"").print(textFieldName)
                .print("\", \"").print(buttonName).print("\", \"").print(clearButton).print("\", \"").print( onUpdateCommit ).print("\"); ")
                .print("xCalendarObj.initXCal();");
    }

    protected CallableCalendar getCallableCalendar() {
        CallableCalendar callableCalendar = (CallableCalendar)this.callableCalendar.get();
        if (callableCalendar == null) {
            callableCalendar = new CallableCalendar();
            this.callableCalendar.set(callableCalendar);
        }
        return callableCalendar;
    }

    public final static class CallableCalendar {
        Map formatters = new WeakHashMap();

        /* Timestamt to Human readable */
        public List onCalUpdate(String key, String name, String value, boolean onUpdateCommit ) {
            List list = new LinkedList();
            SAbstractFormatter formatter = formatterByKey(key);
            if ( formatter != null ) {
                list.add( name );
                try {
                    Date newDate = new Date( Long.parseLong( value ) );
                    list.add( formatter.valueToString( newDate ) );
                } catch ( ParseException e ) {
                    list.add( "" );
                }
                list.add( String.valueOf( onUpdateCommit ) );
            }
            return list;
        }

        /* Human readabel to JSCalendar understandable */
        public List onFieldChange(String key, String viewName, String viewValue, String hiddenName) {
            List list = new LinkedList();
            SAbstractFormatter formatter = formatterByKey(key);
            // Format the view
            Date parsedDate = null;
            if ( formatter != null ) {
                list.add( viewName );
                try {
                    parsedDate = (Date)formatter.stringToValue( viewValue );
                    list.add( formatter.valueToString( parsedDate ) );
                } catch ( ParseException e ) {
                    list.add( ""  );
                }
            }

            // Format the hidden field
            final SimpleDateFormat dateFormatForHidden  = new SimpleDateFormat("yyyy.MM.dd");
            list.add( hiddenName );
            list.add( dateFormatForHidden.format( parsedDate != null ? parsedDate : new Date() ) );

            return list;
        }
        
        protected SAbstractFormatter formatterByKey(String key) {
            for (Iterator iterator = formatters.keySet().iterator(); iterator.hasNext();) {
                SAbstractFormatter formatter = (SAbstractFormatter)iterator.next();
                if (formatters.get( formatter ).equals( key ) )
                    return formatter;
            }
            return null;
        }

        public String registerFormatter(SAbstractFormatter formatter) {
            String value = "" + System.identityHashCode(formatter);
            formatters.put(formatter, value);
            return value;
        }
    }
}
