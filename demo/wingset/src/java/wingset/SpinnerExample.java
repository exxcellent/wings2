/*
 * SpinnerExample.java
 *
 * Created on 6. September 2006, 08:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wingset;

import java.text.DateFormat;
import java.text.FieldPosition;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SForm;
import org.wings.SGridLayout;
import org.wings.SSpinner;
import org.wings.SLabel;
import org.wings.STextField;

import java.util.List;
import java.util.LinkedList;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.SpinnerListModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import org.wings.text.SDateFormatter;
import org.wingx.XCalendar;

import org.wings.SFormattedTextField;

/**
 *
 * @author erik
 */
public class SpinnerExample extends WingSetPane {
    
    /** Creates a new instance of SpinnerExample */
    public SpinnerExample() {
    }
    
    public SComponent createExample() {
             
        SGridLayout layout = new SGridLayout( 2 );
        SForm form = new SForm( layout );
        
        form.add( new SLabel( "Fruits : ",      SConstants.RIGHT_ALIGN ) ); form.add( getListSpinner() );
        form.add( new SLabel( "Month : ",       SConstants.RIGHT_ALIGN ) ); form.add( getDateSpinner() );
        form.add( new SLabel( "Percent : ",     SConstants.RIGHT_ALIGN ) ); form.add( getNumberSpinner() );
        form.add( new SLabel( "XCalendar : ",   SConstants.RIGHT_ALIGN ) );
        
        Calendar calendar = new GregorianCalendar();
        Date initDate = calendar.getTime();
        
        calendar.add(Calendar.YEAR, -50);
        Date earliestDate = calendar.getTime();
        
        calendar.add(Calendar.YEAR, 100);
        Date latestDate = calendar.getTime();
        
        SSpinner spinner = new SSpinner( new SpinnerDateModel( initDate, earliestDate, latestDate, Calendar.MONTH) );
        SDateFormatter dateFormatter = new SDateFormatter( DateFormat.getDateInstance() );
        
        spinner.setEditor( new CalendarEditor( spinner, dateFormatter ) );
        
        form.add( spinner );
        
        return form;
        
    }
    
    private SSpinner getListSpinner () {
        
        List fruits = new LinkedList();
        fruits.add( "Apple" );
        fruits.add( "Banana" );
        fruits.add( "Cherry" );
        fruits.add( "Litchi");
        fruits.add( "Pineapple" );
        
        SSpinner listSpinner = new SSpinner( new SpinnerListModel( fruits ) );
        listSpinner.setHorizontalAlignment( SConstants.RIGHT_ALIGN );
        
        getTextField( listSpinner ).setColumns( 10 );
        
        return listSpinner;
    }
    
    private SSpinner getDateSpinner () {
        
        Calendar calendar = new GregorianCalendar();
        Date initDate = calendar.getTime();
        
        calendar.add(Calendar.YEAR, -50);
        Date earliestDate = calendar.getTime();
        
        calendar.add(Calendar.YEAR, 100);
        Date latestDate = calendar.getTime();

        SSpinner dateSpinner = new SSpinner( new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.MONTH) );
        dateSpinner.setHorizontalAlignment( SConstants.RIGHT_ALIGN );
        dateSpinner.setEditor(new SSpinner.DateEditor(dateSpinner, "MM/yyyy"));       
        
        getTextField(dateSpinner).setColumns( 10 );
            
        return dateSpinner;
        
    }
    
    private SSpinner getNumberSpinner () {
        
        SSpinner numberSpinner = new SSpinner();
        SpinnerNumberModel numberSpinnerModel = new SpinnerNumberModel( 50, 0,100,5 );
        
        numberSpinner.setModel( numberSpinnerModel );
        
        SSpinner.NumberEditor numberEditor = new SSpinner.NumberEditor(numberSpinner);
        
        numberSpinner.setEditor( numberEditor );
                
        numberSpinner.setHorizontalAlignment( SConstants.RIGHT_ALIGN );
        
        getTextField(numberSpinner).setColumns( 10 );
        
        return numberSpinner;
        
    }
    
    private STextField getTextField ( SSpinner spinner ) {
        
        STextField textField = null;
        
        SComponent editor = spinner.getEditor();
        if ( editor instanceof SSpinner.DefaultEditor ) {
            textField = ((SSpinner.DefaultEditor)editor).getTextField();
        } else {
            System.err.println( "Error" );
        }
        return textField;  
        
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
