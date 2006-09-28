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
import org.wings.event.SParentFrameListener;
import java.util.*;
import org.wings.plaf.css.*;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Link;
import org.wings.header.Script;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.resource.ClassPathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;
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
    private Link css;
    private Script calendar;
    private Script calendarLang;
    private Script calendarSetup;

    public CalendarCG() {
        Session session = SessionManager.getSession();
        ClassPathResource res = new ClassPathResource("org/wingx/calendar/calendar.css", "text/css");
        String url = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        css = new Link("stylesheet", null, "text/css", null, new DefaultURLResource(url));
        calendar = createExternalizedScriptHeader( session, "org/wingx/calendar/calendar.js", "text/javascript" );
        calendarLang = createExternalizedScriptHeader( session, getLangScriptURL(), "text/javascript" );
        calendarSetup = createExternalizedScriptHeader( session, "org/wingx/calendar/calendar-setup.js", "text/javascript" );
    }

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        if (!CallableManager.getInstance().containsCallable("xcalendar"))
            CallableManager.getInstance().registerCallable("xcalendar", getCallableCalendar());

        if (!FrameCG.HEADERS.contains(css)) {
            FrameCG.HEADERS.add(css);
            FrameCG.HEADERS.add(calendar);
            FrameCG.HEADERS.add(calendarLang);
            FrameCG.HEADERS.add(calendarSetup);
        }
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

        String id_hidden = component.getName() + "_hidden";
        String id_button = component.getName() + "_button"; 
        
        SFormattedTextField fTextField = component.getFormattedTextField();
        String key = getCallableCalendar().registerFormatter(fTextField.getFormatter());

        fTextField.setEnabled( component.isEnabled() );
        fTextField.addScriptListener( new JavaScriptListener( JavaScriptEvent.ON_CHANGE, "onFieldChange('"+key+"', '"+ id_hidden + "', this.value )" ) );
        
        SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy.MM.dd");
        dateFormat.setTimeZone( component.getTimeZone() );

        device.print("<table");
        writeAllAttributes(device, component);
        device.print("><tr><td class=\"tf\" width=\"100%\"");

        int oversizePadding = Utils.calculateHorizontalOversize(fTextField, true);
        //oversizePadding += RenderHelper.getInstance(component).getHorizontalLayoutPadding();

        if (oversizePadding != 0)
            Utils.optAttribute(device, "oversize", oversizePadding);
        device.print(">");

        fTextField.write(device);

        device.print("\n</td><td class=\"b\" width=\"0%\">\n");

        device.print("<input type=\"hidden\" id=\""+id_hidden+"\" name=\""+id_hidden+"\" formatter=\""+key+"\" value=\""+ format(dateFormat, component.getDate() )+"\">\n");
        device.print("<input class=\"calendar_button\" type=\"image\" id=\""+id_button+"\" src=\""+component.getIcon().getURL()+"\"");
        if ( !component.isEnabled() ) {
            device.print( " disabled");
        }
        device.print(">\n");
        device.print("<script type=\"text/javascript\">\n");
        printCalendarSetup( device, id_hidden, fTextField.getName(), id_button, key );
        device.print("</script>\n" );

        writeTableSuffix(device, component);
    }

    private String format(DateFormat dateFormat, Date date) {
        if (date == null)
            date = new Date();
        return date != null ? dateFormat.format( date ) : "";
    }

    private void printCalendarSetup( org.wings.io.Device device, String hiddenName, String textFieldName, String buttonName, String key )
    throws IOException {
        device.print(" Calendar.setup({" );
        device.print(" inputField  : \"" ).print(hiddenName).print("\"");
        device.print(", textField  : \"" ).print(textFieldName).print("\"");
        device.print(", ifFormat : \"%Y.%m.%d\"");
        device.print(", button : \"").print(buttonName).print("\"");
        device.print(", showOthers : true" );
        device.print(", electric : false" );
        device.print(", onUpdate : onCalUpdate" );
        device.print(", formatter : \"" + key + "\"");
        device.print(" });\n" );
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
        public List onCalUpdate(String key, String name, String value) {
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
            }
            return list;
        }

        /* Human readabel to JSCalendar understandable */
        public List onFieldChange(String key, String name, String value) {
            List list = new LinkedList();
            SAbstractFormatter formatter = formatterByKey(key);
            if ( formatter != null ) {
                SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy.MM.dd");
                list.add( name );
                try {
                    Date date = (Date)formatter.stringToValue( value );
                    list.add( dateFormat.format( date ) );
                } catch ( ParseException e ) {
                    System.err.println(e);
                    list.add( dateFormat.format( new Date() ) );
                }
            }
            return list;
        }

        protected SAbstractFormatter formatterByKey(String key) {
            for (Iterator iterator = formatters.keySet().iterator(); iterator.hasNext();) {
                SAbstractFormatter formatter = (SAbstractFormatter)iterator.next();
                if (key.equals("" + System.identityHashCode(formatter)))
                    return formatter;
            }
            return null;
        }

        public String registerFormatter(SAbstractFormatter formatter) {
            formatters.put(formatter, formatter);
            return "" + System.identityHashCode(formatter);
        }
    }
}