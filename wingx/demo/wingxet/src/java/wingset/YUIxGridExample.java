package wingset;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import org.wings.*;
import org.wingx.YUIxGrid;

public class YUIxGridExample extends WingSetPane implements ActionListener {

    private YUIxGrid  table                 = null;
    private SButton buttonSelectionModel    = null;


    protected SComponent createControls() {
        SButton button = new SButton( "Get selected row(s)" );
            button.addActionListener( this );

        buttonSelectionModel = new SButton();
        buttonSelectionModel.addActionListener( this );

        SPanel panelButtons = new SPanel( new SFlowLayout( SConstants.LEFT ) );
        panelButtons.add( buttonSelectionModel );
        panelButtons.add( button );
        panelButtons.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        return panelButtons;
    }

    protected SComponent createExample() {
        
        table = new YUIxGrid();
        table.setPreferredSize( new SDimension( "700px", "150px" ) );
        
        java.util.List data = new java.util.LinkedList();
        
        data.add( new Library( "wings.jar", "yes", "The wingS core classes" ) );
        data.add( new Library( "css.jar", "yes", "development only" ) );
        data.add( new Library( "commons-logging.jar", "yes", "Apache Commons Logging API. Will delegate logging to Log4J or JDK 1.4 logging facility" ) );
        data.add( new Library( "bsh-core.jar", "yes", "BeanShell for scripting support in STemplateLayout" ) );
        data.add( new Library( "jakarta-regexp-x.x.jar", "yes", "Regular Expression support for JDK 1.3 (Browser identification)" ) );
        data.add( new Library( "kdeclassic-lfgr.jar", "optional", "Icons used in default wingS widget (i.e. graphical checkboxes, icons for table cell editors)" ) );
        data.add( new Library( "dwr.jar", "optional", "Direct Web Remoting libraries for AJAX support. Refer to Section 4.2, ?Client Side Script Listeners?" ) );
        data.add( new Library( "log4j-1.2.9.jar", "optional", "Deploy and configure Log4J with you application if you're using JDK 1.3 or you prefer Log4J in advance to the JDK logging facility" ) );
        data.add( new Library( "commons-httpclient-x.x.jar", "development only", "Apache Commons HTTP client used for Section 4.4, ?Session Recording and Playback?" ) );
        data.add( new Library( "servlet.jar", "development only", "Servlet API interface declaration. Only required for compiling as implementation is provided by the used servlet container" ) );
        
        table.setModel( new LibraryTableModel( data ) );
        
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn( 0 ).setPreferredWidth( 140 );
        columnModel.getColumn( 1 ).setPreferredWidth( 100 );
        columnModel.getColumn( 2 ).setPreferredWidth( 700 );
        
        switchSelectionMode();

        return table;
    }

    private void switchSelectionMode() {
        ListSelectionModel sModel = table.getSelectionModel();
        if ( sModel.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION ) {
            sModel.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
            buttonSelectionModel.setText( "Switch to Single Selection" );
        } else {
            sModel.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
            buttonSelectionModel.setText( "Switch to Multiple Interval Selection" );
        }
    }
    
    public void actionPerformed( ActionEvent event ) {
        
        Object source = event.getSource();
        if ( source != null && source.equals( buttonSelectionModel ) ) {
            switchSelectionMode();
        } else {
            
            int[] selRow = table.getSelectedRows();
            LibraryTableModel tableModel = (LibraryTableModel)table.getModel();
            StringBuffer sb = new StringBuffer();
            for ( int x = 0, y = selRow.length; x < y ; x++ ) {
                Library library = tableModel.getLibrary( selRow[x] );
                sb.append( library.getLibrary() ).append( "\n");
            }
            SOptionPane.showMessageDialog( this, sb.toString(), "Your selection" );
            
        }
    }
    
    private class LibraryTableModel extends AbstractTableModel {
        
        java.util.List data = null;
        
        private String[] columnNames = new String [] { "Library", "Required", "Description" };
        
        public LibraryTableModel ( java.util.List data ) {
            this.data = data;
        }
        
        public int getRowCount() {
            return this.data.size();
        }

        public int getColumnCount() {
            return this.columnNames.length;
        }
        
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }
       
        public void setData( java.util.List data ) {
            this.data = data;
        }
        
        public Library getLibrary( int rowIndex ) {
            Library library = null;
            Object object = data.get( rowIndex );
            if ( object != null && object instanceof Library ) {
                library = (Library)object;
            }
            return library;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            String value = "";
            
            Library lib = getLibrary( rowIndex );
            
            if ( lib != null ) {
                switch ( columnIndex ) {
                    case 0:
                        value = lib.getLibrary();
                        break;
                    case 1:
                        value = lib.getRequired();
                        break;
                    case 2:
                        value = lib.getDescription();
                        break;
                    default:
                        value = "";
                }
                
            }
            return value;
        }
        
    }
    
    private class Library {
    
        String library       = "";
        String required      = "";
        String description   = "";
        
        public Library ( String library, String required, String description ) {
            this.library = library;
            this.required = required;
            this.description = description;
        }
        
        public String getLibrary() {
            return this.library;
        }
        
        public String getRequired() {
            return this.required;
        }
        
        public String getDescription() {
            return this.description;
        }
        
    }
}
