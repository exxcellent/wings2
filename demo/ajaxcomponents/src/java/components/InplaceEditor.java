/*
 * InplaceEditor.java
 *
 * Created on 4. Oktober 2006, 09:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package components;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.wings.SBorderLayout;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SFrame;
import org.wings.SGridLayout;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.border.SEmptyBorder;
import org.wings.header.Link;
import org.wings.resource.DefaultURLResource;
import org.wings.session.SessionManager;
import org.wings.session.SessionManager;
import org.wingx.XInplaceEditor;

/**
 *
 * @author cschyma
 */
public class InplaceEditor {

    private SFrame rootFrame;
    
    /** Creates a new instance of InplaceEditor */
    public InplaceEditor() {
        rootFrame = new SFrame("demo");
                                
        rootFrame.getContentPane().add(createHeader(), SBorderLayout.NORTH);
        rootFrame.getContentPane().add(createXInplaceEditorPanel(), SBorderLayout.CENTER);        
        
        rootFrame.addHeader(new Link("stylesheet", null, "text/css", null, new DefaultURLResource("../css/custom.css")));
        
        rootFrame.show();
    }
    
    private SPanel createXInplaceEditorPanel() {
        SPanel panel = new SPanel();
                        
        XInplaceEditor editor = new XInplaceEditor("Der Hauptunterschied zwischen etwas, was möglicherweise kaputtgehen könnte und etwas, was unmöglich kaputtgehen kann, besteht darin, dass sich bei allem, was unmöglich kaputtgehen kann, falls es doch kaputtgeht, normalerweise herausstellt, dass es unmöglich zerlegt oder repariert werden kann.");        
        editor.setWordWrap(true);      
        panel.add(editor);
        
        return panel;
    }
    
    private SPanel createHeader() {
        SPanel header = new SPanel(new SGridLayout());
        header.setBackground(new Color(60, 80, 100));
        header.setBorder(new SEmptyBorder(10, 15, 10, 0));
        header.setPreferredSize(SDimension.FULLWIDTH);
        header.setHorizontalAlignment(SConstants.LEFT);
        SLabel title = new SLabel("demonstration of the component XInplaceEditor");
        title.setStyle(title.getStyle() + " headerTitle");
        title.setFont(new SFont(SFont.BOLD));        
        title.setVerticalAlignment(SConstants.CENTER);
        title.setHorizontalAlignment(SConstants.LEFT);
        header.add(title);
        
        return header;
    }
}
