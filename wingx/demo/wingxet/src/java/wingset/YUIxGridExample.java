package wingset;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ListSelectionModel;
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
        table.setPreferredSize( new SDimension( "700px", "200px" ) );
        
        java.util.List data = new java.util.LinkedList();
        
        data.add( new Library( new Integer( 1 ), "wings.jar", new Boolean(true), "The wingS core classes" ) );
        data.add( new Library( new Integer( 2 ), "css.jar", new Boolean(true), "development only" ) );
        data.add( new Library( new Integer( 3 ), "commons-logging.jar", new Boolean(true), "Apache Commons Logging API. Will delegate logging to Log4J or JDK 1.4 logging facility" ) );
        data.add( new Library( new Integer( 4 ), "bsh-core.jar", new Boolean(true), "BeanShell for scripting support in STemplateLayout" ) );
        data.add( new Library( new Integer( 5 ), "jakarta-regexp-x.x.jar", new Boolean(true), "Regular Expression support for JDK 1.3 (Browser identification)" ) );
        data.add( new Library( new Integer( 6 ), "kdeclassic-lfgr.jar", new Boolean(false), "Icons used in default wingS widget (i.e. graphical checkboxes, icons for table cell editors)" ) );
        data.add( new Library( new Integer( 7 ), "dwr.jar", new Boolean(false), "Direct Web Remoting libraries for AJAX support. Refer to Section 4.2, ?Client Side Script Listeners?" ) );
        data.add( new Library( new Integer( 8 ), "log4j-1.2.9.jar", new Boolean(false), "Deploy and configure Log4J with you application if you're using JDK 1.3 or you prefer Log4J in advance to the JDK logging facility" ) );
        data.add( new Library( new Integer( 9 ), "commons-httpclient-x.x.jar", new Boolean(false), "Apache Commons HTTP client used for Section 4.4, ?Session Recording and Playback?" ) );
        data.add( new Library( new Integer( 10 ), "servlet.jar", new Boolean(false), "Servlet API interface declaration. Only required for compiling as implementation is provided by the used servlet container" ) );
        
        table.setModel( new LibraryTableModel( data ) );
        
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn( 0 ).setPreferredWidth( 30 );
        columnModel.getColumn( 1 ).setPreferredWidth( 140 );
        columnModel.getColumn( 2 ).setPreferredWidth( 70 );
        columnModel.getColumn( 3 ).setPreferredWidth( 700 );
        
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
                sb.append( library.toString() ).append( "\n");
            }
            SOptionPane.showMessageDialog( this, sb.toString(), "Your selection" );
            
        }
    }
    
    private class LibraryTableModel extends AbstractTableModel {
        
        java.util.List data = null;
        
        private String[] columnNames = new String [] { "Id", "Library", "Required", "Description" };
        
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
        
        public void setValueAt( Object value, int row, int col ) {
            Library lib = getLibrary( row );
            if ( lib != null ) {
                switch ( col ) {
                    case 0: 
                        lib.setId( (Integer)value );
                        break;
                    case 1:
                        lib.setLibrary( (String)value );
                        break;
                    case 2:
                        lib.setRequired( (Boolean)value );
                        break;
                    case 3:
                        lib.setDescription( (String)value );
                        break;
                }
            }
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object value = "";
            
            Library lib = getLibrary( rowIndex );
            
            if ( lib != null ) {
                switch ( columnIndex ) {
                    case 0:
                        value = lib.getId();
                        break;
                    case 1:
                        value = lib.getLibrary();
                        break;
                    case 2:
                        value = lib.getRequired();
                        break;
                    case 3:
                        value = lib.getDescription();
                        break;
                    default:
                        value = "";
                }
                
            }
            return value;
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
        
        public Class getColumnClass(int columnIndex) {
            return getValueAt(0,columnIndex).getClass();
        }
        
    }
    
    private class Library {
    
        Integer id            = null;
        String  library       = "";
        Boolean required      = new Boolean(true);
        String  description   = "";
        
        public Library ( Integer id, String library, Boolean required, String description ) {
            this.id = id;
            this.library = library;
            this.required = required;
            this.description = description;
        }
        
        public Integer getId() {
            return this.id;
        }
        
        public void setId( Integer id ) {
            this.id = id;
        }
        
        public String getLibrary() {
            return this.library;
        }
        
        public void setLibrary( String library ) {
            this.library = library;
        }
        
        public Boolean getRequired() {
            return this.required;
        }
        
        public void setRequired( Boolean required ) {
            this.required = required;
        }
        
        public String getDescription() {
            return this.description;
        }
        
        public void setDescription( String description ) {
            this.description = description;
        }
        
        public String toString() {
            return id + " " + library + " " + required + " " + description;
        }
        
    }
}
