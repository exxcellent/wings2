/*
 * XTable.java
 *
 * Created on 12. September 2006, 13:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wingx;

import java.util.HashMap;
import java.util.Vector;
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

/**
 *
 * @author erik
 */
public class YUIxGrid extends SComponent implements TableModelListener {
 
    private int selRow = -1;
    private int selCol = -1;
    
    private   TableModel          tableModel          = null;
    private   TableColumnModel    tableColumnModel    = null;
    protected ListSelectionModel  selectionModel      = null;
    
    private boolean autoCreateColumnsFromModel;
    
    protected TableCellRenderer defaultRenderer;
    
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
    
    public YUIxGrid(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        
//        this.setStyle( "ygrid-vista" );
        this.setStyle( "ygrid-mso" );
//        this.setStyle( "ygrid" ); 
        
        if (this.tableColumnModel == null) {
             autoCreateColumnsFromModel = true;
        }
        
        if ( sm == null ) {
            sm = new DefaultListSelectionModel();
        }
        setSelectionModel( sm );
        
        setColumnModel( cm );
        
        setModel( dm );
        
        
        setPreferredSize( new SDimension ( "600px", "200px" ) );
    }
    
    public YUIxGrid(Vector rowData, Vector columnNames) {
    }
    
    public TableModel getModel() {
        return tableModel;
    }
    
    public void setModel( TableModel tableModel ) {
         if (tableModel == null)
             throw new IllegalArgumentException("Cannot set a null TableModel");

         if (this.tableModel != tableModel) {
             if (this.tableModel != null)
                 this.tableModel.removeTableModelListener(this);

             this.tableModel = tableModel;
             this.tableModel.addTableModelListener(this);

             tableChanged(new TableModelEvent(tableModel, TableModelEvent.HEADER_ROW));
         }
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
    
    public ListSelectionModel getSelectionModel() {
        return this.selectionModel;
    }
    
    public void setSelectionModel( ListSelectionModel selectionModel ) {
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
         // kill active editors
//         editingCanceled(null);

         if (e == null || e.getFirstRow() == TableModelEvent.HEADER_ROW) {
             // The whole thing changed
//             clearSelection();
             if (getAutoCreateColumnsFromModel())
                 createDefaultColumnsFromModel();
         } 
//         else {
//             switch (e.getType()) {
//                 case TableModelEvent.INSERT:
//                     if (e.getFirstRow() >= 0)
//                         getSelectionModel().insertIndexInterval(e.getFirstRow(),
//                                 e.getLastRow(), true);
//                     break;
//
//                 case TableModelEvent.DELETE:
//                     if (e.getFirstRow() >= 0)
//                         getSelectionModel().removeIndexInterval(e.getFirstRow(),
//                                 e.getLastRow());
//                     break;
//                case TableModelEvent.UPDATE:
//                    /* event fire on javax.swing.table.AbstractTableModel.fireTableDataChanged() */
//                    if (e.getLastRow() == Integer.MAX_VALUE)
//                        clearSelection();
//                    break;                 
//             }
//         }
         reload(org.wings.ReloadManager.STATE);
     }
     
     public TableCellRenderer getCellRenderer( int row, int col ) {
         TableColumnModel columnModel = getColumnModel();
         if (columnModel != null) {
             TableColumn column = columnModel.getColumn(col);
             if (column != null) {
                 TableCellRenderer renderer = column.getCellRenderer();
                 if (renderer != null)
                     return renderer;
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
    
    
}
