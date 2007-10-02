/*
 * XTable.java
 *
 * Created on 12. September 2006, 13:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wingx;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.wings.SComponent;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import org.wings.SDimension;

import org.wings.LowLevelEventListener;
import org.wings.SDefaultListSelectionModel;
import org.wings.SForm;
import org.wings.SListSelectionModel;

/**
 *
 * @author erik
 */
public class YUIxGrid extends SComponent implements TableModelListener, LowLevelEventListener {

    private   TableModel          tableModel          = null;
    private   TableColumnModel    tableColumnModel    = null;
    protected SListSelectionModel selectionModel      = null;
    
    private boolean autoCreateColumnsFromModel;
    
    protected TableCellRenderer defaultRenderer;
    
    /**
     * @see LowLevelEventListener#isEpochCheckEnabled()
     */
    protected boolean epochCheckEnabled = true;
    
    /**
      * <p>In this <code>Map</code>, the renderers for the different
      * classes of cell content are stored.</p><p>The class is treated
      * as key, the renderer as the value.</p>
    */
    protected final HashMap renderer = new HashMap();
    
    /** Creates a new instance of XTable */
    public YUIxGrid() {
        this( new DefaultTableModel() );
    }
    
    public YUIxGrid(int numRows, int numColumns) {
        this( new DefaultTableModel( numRows, numColumns ) );
    } 
    
    public YUIxGrid(Object[][] rowData, Object[] columnNames) {
       this( new DefaultTableModel( rowData, columnNames ) );
    }
    
    public YUIxGrid(TableModel dm) {
        this( dm, new DefaultTableColumnModel() );
    }
    
    public YUIxGrid(TableModel dm, TableColumnModel cm) {
        this( dm, cm, null );
    }
    
    public YUIxGrid(TableModel dm, TableColumnModel cm, SListSelectionModel sm) {
        
//        this.setStyle( "ygrid-vista" );
        this.setStyle( "ygrid-mso" );
//        this.setStyle( "ygrid" ); 
        
        if (this.tableColumnModel == null) {
             autoCreateColumnsFromModel = true;
        }
        
        if ( sm == null ) {
            sm = new SDefaultListSelectionModel();
        }
        setSelectionModel( sm );
        
        setColumnModel( cm );
        
        setModel( dm );
        
        setPreferredSize( new SDimension ( "600px", "200px" ) );
    }    
    
    private int[] toIntArray( String string, String regexp ) {
        String[] strArr = string.split( regexp );
        int[] intArrTmp = new int[strArr.length];
        int index = 0;
        for ( int x = 0, y = strArr.length ; x < y ; x++ ) {
            String str = strArr[x].trim();
            if ( str != null && !"".equals(str) ) {
                intArrTmp[index] = Integer.parseInt( str );
                index++;
            }
        }
        int[] intArr = new int[index];
        System.arraycopy( intArrTmp, 0, intArr, 0, index );
        Arrays.sort( intArr );
        return intArr;
    }
    
    private int[] getSelectionIndervalIndexes( int[] src, int[] dest ) {
        int index0 = -1;
        int index1 = -1;
        for ( int x = 0 , y = src.length ; x < y ; x++ ) {
            if ( Arrays.binarySearch( dest, src[x] ) < 0 ) {
                if ( index0 == -1 || index0 > src[x] ) {
                    index0 = src[x];
                }
                if ( index1 == -1 || index1 < src[x] ) {
                    index1 = src[x];
                }
            }
        }
        return new int[] { index0, index1 };
    }
    
    public class SimpleDataModel {
        
        HashMap rows = new HashMap();
        
        public HashMap getRows () {
            return rows;
        }
        
        public void setValueAt( Object value, int row, int col ) {
            
            HashMap cols = null;
            
            Object objCols = rows.get( String.valueOf(row) );
            if ( objCols != null && objCols instanceof HashMap ) {
                cols = (HashMap)objCols;
            } else {
                cols = new HashMap();
                rows.put( String.valueOf(row), cols );
            }
            cols.put( String.valueOf(col), value );
        }
        
    }
    
    private SimpleDataModel sdm = new SimpleDataModel();
        
    public SimpleDataModel getSimpleDataModel () {
        return sdm;
    }
    
    private Object convertType( Class vc, String value ) {
        
        Object object = value;
        
        if (vc != null) {
            Constructor cons;
            
            try {
                cons = vc.getConstructor(new Class[] { String.class });
            } catch (NoSuchMethodException nsme) {
                cons = null;
            }
            
            if (cons != null) {
                try {
                    object = cons.newInstance( new Object[] { value } );
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }
        return object;
    }
    
    public void processLowLevelEvent(String action, String[] values) {
        processKeyEvents(values);
        SForm.addArmedComponent(this);
        
        System.out.print( "YUIxGrid.processLowLevelEvent( "+ action + ", '");
        for ( int x = 0, y = values.length ; x < y ; x++ ) {
            System.out.print( values[x] );
            if ( x < y ) {
                System.out.print( "," );
            }
        }
        System.out.println( "');" );
        
        SimpleDataModel sdm = getSimpleDataModel();
        HashMap rows = sdm.getRows();
        Object[] keys = rows.keySet().toArray();
        
        for ( int x = 0, y = keys.length; x < y ; x++ ) {
            
            HashMap colsHash = (HashMap)rows.get( keys[x] );
            Object[] cols = (colsHash).keySet().toArray();
            int row = Integer.parseInt( (String)keys[x] );
            
            for ( int i = 0, j = cols.length; i < j ; i++ ) {
                
                String value = (String)colsHash.get( cols[i] );
                int col = Integer.parseInt( (String)cols[i] );
                
                System.out.println( "setValueAt( "+ value + ", "+ row+ ", " +col + ");");
                
                getModel().setValueAt( convertType( getModel().getColumnClass( col ), value ), row, col );
            }
        }
        
        rows.clear();
        
        if ( getModel().getRowCount() == 0 ) {
            clearSelection();
        } else {
            
            getSelectionModel().setDelayEvents(true);
            
            int[] newSelectedRows  = toIntArray( values[0], "," );
            int[] oldSelectedRows  = getSelectedRows();
            
            if ( newSelectedRows.length == 1 ) {
                getSelectionModel().setSelectionInterval( newSelectedRows[0], newSelectedRows[0] );
            } else {
                
                ListSelectionModel lsm = getSelectionModel();
                if ( lsm != null && lsm instanceof DefaultListSelectionModel && ((DefaultListSelectionModel)lsm).getListSelectionListeners().length > 0 ) {
                    if ( newSelectedRows.length > oldSelectedRows.length ) {
                        int[] indexes = getSelectionIndervalIndexes( newSelectedRows, oldSelectedRows );
                        getSelectionModel().addSelectionInterval( indexes[0], indexes[1] );
                    } else {
                        int[] indexes = getSelectionIndervalIndexes( oldSelectedRows, newSelectedRows );
                        getSelectionModel().removeSelectionInterval( indexes[0], indexes[1] );
                    }
                    
                } else {
                    clearSelection();
                    if ( newSelectedRows.length > 0 ) {
                        for ( int x = 0, y = newSelectedRows.length ; x < y ; x++ ) {
                            getSelectionModel().addSelectionInterval( newSelectedRows[x], newSelectedRows[x] );
                        }
                    }
                }
            }
        }
    }
    
    /**
     * @see LowLevelEventListener#isEpochCheckEnabled()
     */
    public boolean isEpochCheckEnabled() {
        return epochCheckEnabled;
    }
    
    /**
     * @see LowLevelEventListener#isEpochCheckEnabled()
     */
    public void setEpochCheckEnabled(boolean epochCheckEnabled) {
        this.epochCheckEnabled = epochCheckEnabled;
    }
    
    public void fireIntermediateEvents() {
        getSelectionModel().setDelayEvents(false);
        getSelectionModel().fireDelayedFinalEvents();
    }
    
    public TableModel getModel() {
        return tableModel;
    }
    
    public void setModel( TableModel tableModel ) {
         if (tableModel == null) {
             throw new IllegalArgumentException("Cannot set a null TableModel");
         }

         if (this.tableModel != tableModel) {
             if (this.tableModel != null) {
                 this.tableModel.removeTableModelListener(this);
             }

             this.tableModel = tableModel;
             this.tableModel.addTableModelListener(this);

             tableChanged(new TableModelEvent(tableModel, TableModelEvent.HEADER_ROW));
         }
     }
    
    /**
     * Returns true if one cell in <code>column</code> is editable.
     * @param row the row
     * @param column the column
     * @return true if one cell in <code>column</code> is editable
     */
    public boolean isCellEditable(int row, int column) {
        boolean isCellEditable = false;
        for ( int x = 0, y = getModel().getRowCount() ; x < y ; x++ ) {
            if ( getModel().isCellEditable( x, column ) == true ) {
                isCellEditable = true;
                break;
            }
        }
        return isCellEditable;
    }
    
    public boolean isRowSelected(int row) {
        return selectionModel.isSelectedIndex(row);
    }
    
    public void setColumnSelectionInterval(int index0, int index1) {
        this.getSelectionModel().setSelectionInterval( index0, index1 );
    }
    
    public TableColumnModel getColumnModel() {
        return tableColumnModel;
    }
    
    public void setColumnModel( TableColumnModel tableColumnModel ) {
        this.tableColumnModel = tableColumnModel;
    }
    
    public SListSelectionModel getSelectionModel() {
        return this.selectionModel;
    }
    
    public void setSelectionModel( SListSelectionModel selectionModel ) {
        this.selectionModel = selectionModel;
    }
    
    protected ListSelectionModel createDefaultSelectionModel() {
        return new DefaultListSelectionModel();
    }
    
    public void setSelectionMode( int selectionMode ) {
        getSelectionModel().setSelectionMode( selectionMode );
    }
    
    public int getSelectedRow () {
        return this.getSelectionModel().getMinSelectionIndex();
    }

    public int[] getSelectedRows() {
        
        ListSelectionModel selectionModel = getSelectionModel();
        
        int min = selectionModel.getMinSelectionIndex();
        int max = selectionModel.getMaxSelectionIndex();

        int[] selRows;
        
        if ((min == -1) || (max == -1)) {
            
            selRows = new int[0];
                    
        } else {
            
            int[] selRowsTmp = new int[(max - min) + 1];
            int j = 0;
            for(int x = min; x <= max; x++) {
                if (selectionModel.isSelectedIndex(x)) {
                    selRowsTmp[j++] = x;
                }
            }
            selRows = new int[j];
            System.arraycopy(selRowsTmp, 0, selRows, 0, j);
            
        }
        
        return selRows;
    }
    
      /**
      * Creates the default columns of the table from the table model.
      */
     public void createDefaultColumnsFromModel() {
         TableModel tm = getModel();

         if (tm != null) {
             TableColumnModel columnModel = getColumnModel();
             while (columnModel.getColumnCount() > 0)
                 columnModel.removeColumn(columnModel.getColumn(0));

             for ( int i = 0; i < tm.getColumnCount(); i++ ) {
                 TableColumn column = new TableColumn( i );
                 String columnName = tm.getColumnName( i );
                 column.setHeaderValue( columnName );
                 this.tableColumnModel.addColumn( column );
             }
         }
     }

     /**
      * Sets this table's <code>autoCreateColumnsFromModel</code> flag.
      * This method calls <code>createDefaultColumnsFromModel</code> if
      * <code>autoCreateColumnsFromModel</code> changes from false to true.
      *
      * @param   autoCreateColumnsFromModel   true if <code>JTable</code> should automatically create columns
      * @see     #getAutoCreateColumnsFromModel
      * @see     #createDefaultColumnsFromModel
      */
     public void setAutoCreateColumnsFromModel(boolean autoCreateColumnsFromModel) {
         if (this.autoCreateColumnsFromModel != autoCreateColumnsFromModel) {
             this.autoCreateColumnsFromModel = autoCreateColumnsFromModel;
             if (autoCreateColumnsFromModel) {
                 createDefaultColumnsFromModel();
             }
         }
     }     
     
     /**
      * Determines whether the table will create default columns from the model.
      * If true, <code>setModel</code> will clear any existing columns and
      * create new columns from the new model.  Also, if the event in
      * the <code>tableChanged</code> notification specifies gthat the
      * entire table changed, then the columns will be rebuilt.
      * The default is true.
      *
      * @return  the autoCreateColumnsFromModel of the table
      * @see     #setAutoCreateColumnsFromModel
      * @see     #createDefaultColumnsFromModel
      */
     public boolean getAutoCreateColumnsFromModel() {
         return autoCreateColumnsFromModel;
     }     
    
     public void tableChanged(TableModelEvent e) {
         if (e == null || e.getFirstRow() == TableModelEvent.HEADER_ROW) {
             // The whole thing changed
             clearSelection();
             if (getAutoCreateColumnsFromModel()) {
                 createDefaultColumnsFromModel();
             }
         } 
         reload();
     }
     
     public TableCellRenderer getCellRenderer( int row, int col ) {
         TableColumnModel columnModel = getColumnModel();
         if (columnModel != null) {
             TableColumn column = columnModel.getColumn(col);
             if (column != null) {
                 TableCellRenderer renderer = column.getCellRenderer();
                 if (renderer != null) {
                     return renderer;
                 }
             }
         }
         return getDefaultRenderer(getColumnClass(col));
     }  
     
     public TableCellRenderer getDefaultRenderer(Class columnClass) {
         if (columnClass == null) {
             return defaultRenderer;
         } else {
             Object r = renderer.get(columnClass);
             if (r != null) {
                 return (TableCellRenderer) r;
             } else {
                 return getDefaultRenderer(columnClass.getSuperclass());
             }
         }
     }
     
     public Class getColumnClass( int column ) {
         return tableModel.getColumnClass(convertColumnIndexToModel(column));
     }
     
     public int convertColumnIndexToModel(int viewColumnIndex) {
         if (viewColumnIndex < 0) {
             return viewColumnIndex;
         }
         return getColumnModel().getColumn(viewColumnIndex).getModelIndex();
     }
     
     
    /**
     * Deselects all selected columns and rows.
     */
     public void clearSelection() {
         getSelectionModel().clearSelection();
         getColumnModel().getSelectionModel().clearSelection();
     }
    
}
