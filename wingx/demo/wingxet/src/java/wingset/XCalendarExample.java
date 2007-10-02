package wingset;

import org.wings.*;
import org.wings.text.SDateFormatter;
import org.wingx.XCalendar;

import javax.swing.*;
import java.util.*;
import java.text.DateFormat;

public class XCalendarExample
    extends WingSetPane
{
    protected SComponent createControls() {
        return null;
    }

    public SComponent createExample() {
        SGridLayout layout = new SGridLayout( 2 );
        SPanel panel = new SPanel( layout );

        Calendar calendar = new GregorianCalendar();
        Date initDate = calendar.getTime();

        calendar.add(Calendar.YEAR, -50);
        Date earliestDate = calendar.getTime();

        calendar.add(Calendar.YEAR, 100);
        Date latestDate = calendar.getTime();

        SSpinner spinner = new SSpinner( new SpinnerDateModel( initDate, earliestDate, latestDate, Calendar.MONTH) );
        SDateFormatter dateFormatter = new SDateFormatter( DateFormat.getDateInstance() );

        spinner.setEditor( new CalendarEditor( spinner, dateFormatter ) );

        panel.add( new SLabel( "Calendar: ", SConstants.RIGHT_ALIGN ) );
        panel.add( new XCalendar(dateFormatter) );

        panel.add( new SLabel( "Spinner: ", SConstants.RIGHT_ALIGN ) );
        panel.add( spinner );

        return panel;

    }

    public static class CalendarEditor extends SSpinner.DefaultEditor {

        XCalendar calendar = null;

        public CalendarEditor ( SSpinner spinner, SDateFormatter formatter ) {
            super( spinner );

            removeAll();

            calendar = new XCalendar( formatter );
            calendar.getFormattedTextField().setColumns( 15 );

            add( calendar );

        }

        public SFormattedTextField getTextField() {
            return calendar.getFormattedTextField();
        }

    }
}
