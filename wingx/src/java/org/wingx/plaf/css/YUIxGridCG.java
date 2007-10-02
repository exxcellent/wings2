/*
 * TableCG.java
 *
 * Created on 12. Juni 2006, 09:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.wingx.plaf.css;

import java.util.Arrays;
import java.util.WeakHashMap;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.*;
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
import org.wings.SIcon;
import org.wings.SResourceIcon;
import org.wings.event.SParentFrameEvent;
import org.wings.plaf.css.AbstractComponentCG;
import org.wings.plaf.css.HeaderUtil;

/**
 *
 *  * @author <a href="mailto:e.habicht@thiesen.com">Erik Habicht</a>
 */
public class YUIxGridCG
        extends AbstractComponentCG implements SParentFrameListener {

    private static final WeakHashMap COMPONENTS = new WeakHashMap();
    
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

    HeaderUtil headerUtil = new HeaderUtil();

    public YUIxGridCG() {
        Session session = SessionManager.getSession();

        headerUtil.addHeader(createExternalizedScriptHeader( session, "org/wingx/grid/yahoo.js",      "text/javascript" ));
        headerUtil.addHeader(createExternalizedScriptHeader( session, "org/wingx/grid/event.js",      "text/javascript" ));
        headerUtil.addHeader(createExternalizedScriptHeader( session, "org/wingx/grid/dom.js",        "text/javascript" ));
        headerUtil.addHeader(createExternalizedScriptHeader( session, "org/wingx/grid/dragdrop.js",   "text/javascript" ));
        headerUtil.addHeader(createExternalizedScriptHeader( session, "org/wingx/grid/animation.js",  "text/javascript" ));
        headerUtil.addHeader(createExternalizedScriptHeader( session, "org/wingx/grid/yui-ext.js",    "text/javascript" ));
        headerUtil.addHeader(createExternalizedScriptHeader( session, "org/wingx/grid/MySelectionModel.js",    "text/javascript" ));

        ClassPathResource res = new ClassPathResource("org/wingx/grid/grid.css", "text/css");
        String url = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        headerUtil.addHeader(new Link("stylesheet", null, "text/css", null, new DefaultURLResource(url)));
    }
    
    public void installCG(final SComponent comp) {
        super.installCG(comp);
        comp.addParentFrameListener(this);
    }
    
    public void parentFrameAdded(SParentFrameEvent e) {
        if ( !COMPONENTS.containsKey( e.getComponent() ) ) {
            COMPONENTS.put( e.getComponent(), e.getComponent() );
        }
        headerUtil.installHeaders();
    }
    
    public void parentFrameRemoved(SParentFrameEvent e) {
        COMPONENTS.remove( e.getComponent() );
        if ( COMPONENTS.size() == 0 ) {
            headerUtil.uninstallHeaders();
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
                device.print( arrayToString( selectedRows ) );
                device.print( " ), false);\n" );
            }
        }
        
    }
    
    private String arrayToString ( int[] array ) {
        StringBuffer sb = new StringBuffer();
        for ( int x = 0, y = array.length ; x < y ; x++ ) {
            sb.append( array[x] );
            if ( x < y-1 ) {
                sb.append( "," );
            }
        }
        return sb.toString();
    }
    
    public void writeInternal(org.wings.io.Device device, org.wings.SComponent _c )
    throws java.io.IOException {
        
        YUIxGrid      table = (YUIxGrid)_c;

        table.setAttribute( CSSProperty.OVERFLOW, "hidden" );
        
        device.print( "<div " );
        writeAllAttributes( device, table);
        device.print( "></div>\n" );
        
        Arrays.toString( table.getSelectedRows() );
        device.print( "<input value=\"" + arrayToString( table.getSelectedRows() ) + "\" name=\""+table.getName()+"\" id=\""+table.getName()+"_hidden\" eid=\""+table.getEncodedLowLevelEventId()+"\" type=\"hidden\">" );
        
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
        device.print( "        ").print( varDataModel ).print( ".addListener('rowssorted', this.onRowsSorted);\n");
        
        ListSelectionModel selectionModel = table.getSelectionModel();
        if ( selectionModel.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION ) {
            device.print( "    var ").print( varSelModel ).print( " = new YAHOO.ext.grid.MySingleSelectionModel();\n" );
        } else {
            device.print( "    var ").print( varSelModel ).print( " = new YAHOO.ext.grid.MyDefaultSelectionModel();\n" );
        }
        device.print( "        ").print( varSelModel ).print( ".addListener('selectionchange', this.onSelectionChange);\n");
        
        device.print( "    this.").print( varGrid ).print(" = new YAHOO.ext.grid.Grid('").print( table.getName() ).print("', "+varDataModel+", "+varColModel+", "+varSelModel+" );\n" );     
        device.print( "    this.").print( varGrid ).print(".render();\n" );

        writeSelectedRows( device, varSelModel, table );
        
        device.print( "  },\n" );

        device.print( "  onSelectionChange : function ( selmodel ) { \n" );
        device.print( "     var sR = selmodel.getSelectedRows();\n" );
        device.print( "     var rString = '';\n");
        device.print( "     for (var i = 0; i < sR.length; i++) {\n" );
        device.print( "         var rA = selmodel.grid.dataModel.getRow( sR[i].rowIndex );\n" );
        device.print( "         if ( i > 0 ) { rString += ','; }\n");
        device.print( "         rString += rA[rA.length-1];\n" );
        device.print( "     }\n" );
        device.print( "     var elem = document.getElementById( '"+table.getName() +"_hidden');\n" );
        device.print( "     elem.value = rString;\n");
        
        ListSelectionModel lsm = table.getSelectionModel();
        if ( lsm != null && lsm instanceof DefaultListSelectionModel && ((DefaultListSelectionModel)lsm).getListSelectionListeners().length > 0 ) {
            device.print( "     elem.form.submit();\n");
        }
        
        device.print( "  },\n" );    
        
        // Nach dem Sortieren einer Tabelle mit unterschiedlichen Zeilenhoehen kam es zu ungewollte Ueberlappungen.
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
    
}