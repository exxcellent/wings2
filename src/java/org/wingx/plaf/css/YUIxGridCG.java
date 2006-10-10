/*
 * TableCG.java
 *
 * Created on 12. Juni 2006, 09:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wingx.plaf.css;


import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Link;
import org.wings.header.Script;
import org.wings.io.StringBuilderDevice;
import org.wings.resource.ClassPathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.session.SessionManager;
import org.wings.session.Session;
import org.wings.style.CSSProperty;
import org.wingx.YUIxGrid;

import javax.swing.table.TableModel;
import org.wings.SButton;
import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SForm;
import org.wings.SIcon;
import org.wings.SResourceIcon;
import org.wings.event.SParentFrameEvent;
import org.wings.plaf.css.AbstractComponentCG;
import org.wings.plaf.css.FrameCG;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.util.SessionLocal;

/**
 *
 *  * @author <a href="mailto:e.habicht@thiesen.com">Erik Habicht</a>
 */
public class YUIxGridCG
        extends AbstractComponentCG implements SParentFrameListener {
    
    private SessionLocal callableTable      = new SessionLocal();
    private String       callableScriptName = "xtable";
    
    private Link    linkYUIExtGrid;
    
    private Script  scriptYUIYahoo;
    private Script  scriptYUIEvent;
    private Script  scriptYUIDom;
    private Script  scriptYUIDragDrop;
    private Script  scriptYUIAnimation;
    private Script  scriptYUIExt;
    
    // These gif's are needed for grid.css
    static {
        String[] images = new String [] { "org/wingx/grid/images/pick-button.gif",
                            "org/wingx/grid/images/invalid_line.gif",
                            "org/wingx/grid/images/drop-no.gif",
                            "org/wingx/grid/images/drop-yes.gif",
                            "org/wingx/grid/images/page-first.gif",
                            "org/wingx/grid/images/done.gif",   
                            "org/wingx/grid/images/page-last.gif",
                            "org/wingx/grid/images/page-next.gif",  
                            "org/wingx/grid/images/page-prev.gif",
                            "org/wingx/grid/images/loading.gif",
                            "org/wingx/grid/images/page-first-disabled.gif",
                            "org/wingx/grid/images/page-last-disabled.gif",
                            "org/wingx/grid/images/page-next-disabled.gif",
                            "org/wingx/grid/images/page-prev-disabled.gif",
                            "org/wingx/grid/images/mso-hd.gif",  
                            "org/wingx/grid/images/page-prev.gif",
                            "org/wingx/grid/images/loading.gif",      
                            "org/wingx/grid/images/sort_desc.gif",
                            "org/wingx/grid/images/sort_asc.gif",
                            "org/wingx/grid/images/grid-vista-hd.gif",
                            "org/wingx/grid/images/grid-split.gif",
                            "org/wingx/grid/images/grid-blue-split.gif",
                            "org/wingx/grid/images/grid-blue-hd.gif"        
                          };
    
        for ( int x = 0, y = images.length ; x < y ; x++ ) {
            SIcon icon = new SResourceIcon(images[x]);
            icon.getURL();
        }
     
    }
        
    public YUIxGridCG() {
        Session session = SessionManager.getSession();
        
        ClassPathResource res = new ClassPathResource("org/wingx/grid/grid.css", "text/css");
        String url = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        linkYUIExtGrid = new Link("stylesheet", null, "text/css", null, new DefaultURLResource(url));
        
        scriptYUIYahoo      = createExternalizedScriptHeader( session, "org/wingx/grid/yahoo.js",      "text/javascript" );
        scriptYUIEvent      = createExternalizedScriptHeader( session, "org/wingx/grid/event.js",      "text/javascript" );
        scriptYUIDom        = createExternalizedScriptHeader( session, "org/wingx/grid/dom.js",        "text/javascript" );
        scriptYUIDragDrop   = createExternalizedScriptHeader( session, "org/wingx/grid/dragdrop.js",   "text/javascript" );
        scriptYUIAnimation  = createExternalizedScriptHeader( session, "org/wingx/grid/animation.js",  "text/javascript" );
        scriptYUIExt        = createExternalizedScriptHeader( session, "org/wingx/grid/yui-ext.js",    "text/javascript" );
    }
    
    public void installCG(final SComponent comp) {
        super.installCG(comp);
        comp.addParentFrameListener(this);
    }
    
    public void parentFrameAdded(SParentFrameEvent e) {
        
        if (!CallableManager.getInstance().containsCallable(callableScriptName))
            CallableManager.getInstance().registerCallable(callableScriptName, getCallableTable());
        
        if (!FrameCG.HEADERS.contains(scriptYUIYahoo)) {
            FrameCG.HEADERS.add(scriptYUIYahoo);
            FrameCG.HEADERS.add(scriptYUIEvent);
            FrameCG.HEADERS.add(scriptYUIDom);
            FrameCG.HEADERS.add(scriptYUIDragDrop);
            FrameCG.HEADERS.add(scriptYUIAnimation);
            FrameCG.HEADERS.add(scriptYUIExt);
            FrameCG.HEADERS.add(linkYUIExtGrid);
        }
    }
    
    public void parentFrameRemoved(SParentFrameEvent e) {
        
        CallableTable ct = getCallableTable();
        
        SComponent comp = e.getComponent();
        if ( comp != null && comp instanceof YUIxGrid ) {
            ct.unregisterTable( (YUIxGrid)comp );
        }
        
        if ( getCallableTable().getCallableCount() <= 0 ) {
            
            CallableManager.getInstance().unregisterCallable( callableScriptName );
            
            if ( FrameCG.HEADERS.contains(scriptYUIYahoo) ) {
                FrameCG.HEADERS.remove(scriptYUIYahoo);
                FrameCG.HEADERS.remove(scriptYUIEvent);
                FrameCG.HEADERS.remove(scriptYUIDom);
                FrameCG.HEADERS.remove(scriptYUIDragDrop);
                FrameCG.HEADERS.remove(scriptYUIAnimation);
                FrameCG.HEADERS.remove(scriptYUIExt);
                FrameCG.HEADERS.remove(linkYUIExtGrid);
            }
        }
    }
    
    private Script createExternalizedScriptHeader(Session session, String ClassPath, String mimeType) {
        ClassPathResource res = new ClassPathResource(ClassPath, mimeType);
        String jScriptUrl = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        return new Script(mimeType, new DefaultURLResource(jScriptUrl));
    }
    
    
    private void quoteNewLine( org.wings.io.Device device, String s )
    throws java.io.IOException {
        if (s == null) {
            return;
        }
        char[] chars = s.toCharArray();
        char c;
        int last = 0;
        for (int pos = 0; pos < chars.length; ++pos) {
            c = chars[pos];
            // write special characters as code ..
            if (c < 32 || c > 127) {
                device.print(chars, last, (pos - last));
                if ( ( c == '\n' || ( c == '\r' && (pos < chars.length && chars[pos+1] == '\n') ) ) ) {
                    device.print("<br>");
                    if ( c == '\r' ) pos++;
                } else {
                    device.print("&#");
                    device.print((int) c);
                    device.print(";");
                } // end of if ()
                last = pos + 1;
            } else {
                switch (c) {
                    case '\'':
                        device.print(chars, last, (pos - last));
                        device.print("\\'");
                        last = pos + 1;
                        break;
                        
                }
            }
        }
        device.print(chars, last, chars.length - last);
    }
    
    private void addParentFrame( SComponent component, YUIxGrid table ) {
        if ( component != null && table != null  ) {
            component.setParent( table.getParent() );
        }
    }
    
    
    
    private void writeModel( org.wings.io.Device device, String varName, YUIxGrid table )
    throws java.io.IOException {
        
        TableModel model = table.getModel();
        
        String varNameArray = varName + "A";

        device.print( "    var ").print( varNameArray ).print( " = [\n" );
        for ( int row = 0, y = model.getRowCount() ; row < y ; row++ ) {
            device.print( "      [" );
            for ( int col = 0, j = model.getColumnCount() ; col < j ; col++ ) {
                Object value = model.getValueAt( row, col );
                device.print( "'" );
                if ( value != null && value instanceof SButton ) {
                    SButton button = (SButton)value;
                    addParentFrame( button, table );
                    
                    StringBuilderDevice sbDevice = new StringBuilderDevice();
                    button.write( sbDevice );
                    quoteNewLine( device, sbDevice.toString() );
                } else if ( value != null ) {
                    quoteNewLine(device, value.toString().replaceAll("'","\\'") );
                } else {
                    device.print( "null" );
                }
                device.print( "',");
            }
            device.print( "'"+row+"'" );
            
            device.print( "]");
            if ( row < y - 1 ) {
                device.print( "," );
            }
            device.print("\n" );
        }
        device.print( "    ];\n" );
        
        device.print( "    var ").print( varName ).print( " = new YAHOO.ext.grid.DefaultDataModel( ").print( varNameArray ).print( ");\n" );
        
    }
    
    private void writeColumnModel( org.wings.io.Device device, String varName, TableColumnModel tableColumnModel )
    throws java.io.IOException {
        
        String varNameArray = varName + "A";
        
        device.print( "    var " ).print( varNameArray ).print( " = [\n" );
        for ( int x = 0, y = tableColumnModel.getColumnCount() ; x < y ; x++ ) {
            javax.swing.table.TableColumn tableColumn = tableColumnModel.getColumn( x );
            device.print( "       {" );
            device.print( "header:\"" ).print( tableColumn.getHeaderValue() ).print("\"");
            device.print( ",");
            device.print( "width:").print( tableColumn.getPreferredWidth() );
            device.print( ",sortable:true},\n");
        }
        // Ein extra Column, welches nicht angezeigt wird, um auch nach dem Sortieren noch zu wissen welchen Datensatz wir vor uns haben.
        device.print( "       {header:\"uid\",width:0,hidden:true}\n");
        
        device.print( "    ];\n" );
        device.print( "    var ").print( varName ).print( " = new YAHOO.ext.grid.DefaultColumnModel( " ).print( varNameArray ).print( " );\n" );
        
    }
    
    private void writeSelectedRows( org.wings.io.Device device, String selModel, YUIxGrid table )
    throws java.io.IOException {
        
        int[] selectedRows = table.getSelectedRows();
        
        if ( selectedRows.length > 0 ) {
            if ( selectedRows.length == 1 ) {
                device.print( "    " ).print( selModel ).print( ".selectRow( " );
                device.print( selectedRows[0] );
                device.print( ", false);\n" );
            } else {
                device.print( "    " ).print( selModel ).print( ".selectRows( new Array ( " );
                for ( int x = 0, y = selectedRows.length ; x < y ; x++ ) {
                    device.print( selectedRows[x] );
                    if ( x < y -1 ) {
                        device.print( "," );
                    }
                }
                device.print( " ), false);\n" );
            }
        }
        
    }
    
    public void writeInternal(org.wings.io.Device device, org.wings.SComponent _c )
    throws java.io.IOException {
        
        YUIxGrid      table = (YUIxGrid)_c;
        TableModel  model = table.getModel();
        
        String key = getCallableTable().registerTable( table );
        
        table.setAttribute( CSSProperty.OVERFLOW, "hidden" );
        
        device.print( "<div " );
        writeAllAttributes( device, table);
        device.print( "></div>\n" );
        
        device.print( "<input id=\""+table.getName()+"_hidden\" type=\"hidden\">" );
        
        device.print( "<script type=\"text/javascript\">\n" );
        
        String varName      = table.getName() + "Table";
        String varColModel  = table.getName() + "_cM";
        String varDataModel = table.getName() + "_dM";
        String varSelModel  = table.getName() + "_sM";
        String varGrid      = table.getName() + "_g";
        
        device.print( "var ").print(varName).print( " = {\n" );
        device.print( "  init : function() {\n" );
        
        writeColumnModel( device, varColModel, table.getColumnModel() );
        
        writeModel( device, varDataModel, table );
        
        ListSelectionModel selectionModel = table.getSelectionModel();
        if ( selectionModel.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION ) {
            device.print( "    var ").print( varSelModel ).print( " = new YAHOO.ext.grid.SingleSelectionModel();\n" );
        } else {
            device.print( "    var ").print( varSelModel ).print( " = new YAHOO.ext.grid.DefaultSelectionModel();\n" );
        }
        
        device.print( "    this.").print( varGrid ).print(" = new YAHOO.ext.grid.Grid('").print( table.getName() ).print("', "+varDataModel+", "+varColModel+", "+varSelModel+" );\n" );
        
        device.print( "    this.").print( varGrid ).print(".render();\n" );

        writeSelectedRows( device, varSelModel, table );
        
        device.print( "    ").print( varSelModel ).print( ".addListener('rowselect', this.onRowSelect);\n");
        device.print( "    ").print( varDataModel ).print( ".addListener('rowssorted', this.onRowsSorted);\n");

        device.print( "  },\n" );
        
        device.print( "  onRowSelect : function (selModel, row, selected ) { \n" );
        device.print( "    var rowArray = selModel.grid.dataModel.getRow(row.rowIndex);\n ");
        device.print( "    var uid = rowArray[rowArray.length-1];\n" );
        ListSelectionModel lsm = table.getSelectionModel();
        if ( lsm != null && lsm instanceof DefaultListSelectionModel && ((DefaultListSelectionModel)lsm).getListSelectionListeners().length > 0 ) {
            device.print( "    xtable.setSelected('").print(key).print("',uid,selected, function cb() { document.getElementById( '"+table.getName() +"_hidden').form.submit(); } );\n" );
        } else {
            device.print( "    xtable.setSelected('").print(key).print("',uid,selected, function cb() {} );\n" );
        }
        device.print( "  },\n" );
        
        
//        device.print( "  inform2 : function(selModel, selectedRows, selectedRowIds){\n" );
//        device.print( "    var rowArray;\n" );
//        device.print( "    var uidArray = new Array();\n" );
//        device.print( "    for ( var x = 0, y = selectedRows.length ; x < y ; x++ ) {\n" );
//        device.print( "      var rowDataArray = this.grid.dataModel.data[selectedRows[x].rowIndex];\n" );
//        device.print( "      uidArray.push( rowDataArray[rowDataArray.length-1] );\n" );
//        device.print( "    }\n" );
//        device.print( "      xtable.setSelected2('").print(key).print("',selectedRows, selectedRowIds, function cb() {} );\n" );
//        device.print( "  },\n" );
        
//        device.print( "  inform2 : function(selModel, selectedRows, selectedRowIds){\n" );
//        device.print( "    var rowArray;\n" );
//        device.print( "    var uidArray = new Array();\n" );
//        device.print( "    for ( var x = 0, y = selectedRows.length ; x < y ; x++ ) {\n" );
//        device.print( "      var rowDataArray = this.grid.dataModel.data[selectedRows[x].rowIndex];\n" );
//        device.print( "      uidArray.push( rowDataArray[rowDataArray.length-1] );\n" );
//        device.print( "    }\n" );
//        device.print( "    var rowIdString = uidArray.join(\",\");\n" );
//        device.print( "      xtable.setSelected2('").print(key).print("',rowIdString, function cb() {} );\n" );
//        device.print( "  },\n" );
        
        // Nach dem Sortieren einer Tabelle mit unterschiedlichen Zeilenhöhen kam es zu ungewollte Überlappungen.
        // Die nachfolgende Funktion ist nur ein Workaround.
        device.print( "  onRowsSorted : function(dataModel, sortColumnIndex, sortDirection ) {\n" );
        device.print( "    window.resizeBy(-1,-1);\n" );
        device.print( "    window.resizeBy( 1, 1);\n" );
        device.print( "  }\n");
        
        device.print( "} \n" );
        
        device.print( "YAHOO.util.Event.on(window, 'load', ").print(varName).print(".init, ").print(varName).print(", true); \n" );
        
        device.print( "</script>\n" );
        
        writeStyle( device, table );
    }
    
    private void writeStyle ( org.wings.io.Device device, YUIxGrid table ) 
    throws java.io.IOException {
        TableColumnModel columnModel = table.getColumnModel();   
        device.print( "<style type=\"text/css\">\n");
        int x = 0;
        for (  int y = columnModel.getColumnCount() ; x < y ; x++ ) {
            device.print( "#").print( table.getName() ).print( " .ygrid-col-" ).print( x ).print( " { text-align:left; }\n");
        }
        device.print( "#").print( table.getName() ).print( " .ygrid-col-" ).print( x++ ).print( " {}\n" );
        device.print( "</style>\n");
    }
    
    protected CallableTable getCallableTable() {
        CallableTable callableTable = (CallableTable)this.callableTable.get();
        if (callableTable == null) {
            callableTable = new CallableTable();
            this.callableTable.set(callableTable);
        }
        return callableTable;
    }
    
    public final static class CallableTable {
        Map tables = new WeakHashMap();
        
        public void setSelected( String key, String row, boolean selected ) {
            System.out.println( "Key : " + key + ", Row : " + row + ", Selected : " + selected );
            
            YUIxGrid table = tableByKey( key );
            if ( table != null ) {
                ListSelectionModel selectionModel = table.getSelectionModel();
                int irow = Integer.parseInt( row );
                if ( selected == true ) {
                    selectionModel.addSelectionInterval( irow, irow );
                } else {
                    selectionModel.removeSelectionInterval( irow, irow );
                }
//                table.setColumnSelectionInterval( Integer.parseInt( row ), Integer.parseInt( row ) );
//                table.setSelectedColumn( Integer.parseInt( col ) );
            }
        }
        
//        public void setSelected2( String key, String rowIds ) {
//
//            System.out.println("rowIds : " + rowIds );
//
//        }
        
        protected YUIxGrid tableByKey(String key ) {
            for (Iterator iterator = tables.keySet().iterator(); iterator.hasNext();) {
                YUIxGrid table = (YUIxGrid)iterator.next();
                if (key.equals("" + System.identityHashCode(table)))
                    return table;
            }
            return null;
        }
        
        public int getCallableCount() {
            return tables.size();
        }
        
        public String registerTable(YUIxGrid table) {
            tables.put(table, table);
            return "" + System.identityHashCode(table);
        }
        public void unregisterTable(YUIxGrid table) {
            tables.remove( table );
        }
        
    }
    
}