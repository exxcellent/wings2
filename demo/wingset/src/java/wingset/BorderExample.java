/*
 * BorderExample.java
 *
 * Created on 31. Mai 2007, 14:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wingset;

import java.awt.Color;
import javax.swing.ImageIcon;
import org.wings.*;
import org.wings.border.*;

/**
 *
 * @author pet
 */
public class BorderExample extends WingSetPane {
    

    protected SComponent createControls() {
        return null;
    }
/*
 * BorderDemo.java requires the following file:
 *    images/wavy.gif
 */
    public SComponent createExample() {
        
        SGridLayout layout = new SGridLayout(1, 0);
        SPanel panel = new SPanel( layout );

        //Keep references to the next few borders,
        //for use in titles and compound borders.
        SBorder blackline, raisedetched, loweredetched,
               raisedbevel, loweredbevel, empty;

        //A border that puts 10 extra pixels at the sides and
        //bottom of each pane.
        SBorder paneEdge = SBorderFactory.createSEmptyBorder(0,10,10,10);

        blackline = SBorderFactory.createSLineBorder(Color.black);
        raisedetched = SBorderFactory.createSEtchedBorder(SEtchedBorder.RAISED);
        loweredetched = SBorderFactory.createSEtchedBorder(SEtchedBorder.LOWERED);
        raisedbevel = SBorderFactory.createRaisedSBevelBorder();
        loweredbevel = SBorderFactory.createLoweredSBevelBorder();
        empty = SBorderFactory.createSEmptyBorder();
       

        //First pane: simple borders
        SPanel simpleBorders = new SPanel();
        simpleBorders.setBorder(paneEdge);
        simpleBorders.setLayout(new SBoxLayout(SBoxLayout.Y_AXIS));

        addCompForBorder(blackline, "line border",
                         simpleBorders);
        addCompForBorder(raisedetched, "raised etched border",
                         simpleBorders);
        addCompForBorder(loweredetched, "lowered etched border",
                         simpleBorders);
        addCompForBorder(raisedbevel, "raised bevel border",
                         simpleBorders);
        addCompForBorder(loweredbevel, "lowered bevel border",
                         simpleBorders);
        addCompForBorder(empty, "empty border",
                         simpleBorders);

        //Second pane: matte borders
        SPanel matteBorders = new SPanel();
        matteBorders.setBorder(paneEdge);
        matteBorders.setLayout(new SBoxLayout(SBoxLayout.Y_AXIS));

        ImageIcon icon = createImageIcon("images/wavy.gif",
                                         "wavy-line border icon"); //20x22
        //Third pane: titled borders
        SPanel titledBorders = new SPanel();
        titledBorders.setBorder(paneEdge);
        titledBorders.setLayout(new SBoxLayout(SBoxLayout.Y_AXIS));
        STitledBorder titled;

        titled = SBorderFactory.createSTitledBorder("title");
        addCompForBorder(titled,
                         "default titled border"
                         + " (default just., default pos.)",
                         titledBorders);

        titled = SBorderFactory.createSTitledBorder(
                              blackline, "title");
        addCompForTitledBorder(titled,
                               "titled line border"
                                   + " (centered, default pos.)",
                               STitledBorder.CENTER,
                               STitledBorder.DEFAULT_POSITION,
                               titledBorders);

        titled = SBorderFactory.createSTitledBorder("title");
        addCompForTitledBorder(titled,
                               "titled line border Bold"
                                   + " (centered, default pos.)",
                               STitledBorder.CENTER,
                               STitledBorder.DEFAULT_POSITION,
                               titledBorders);
        titled.setTitleFont(new SFont(SFont.BOLD));

        titled = SBorderFactory.createSTitledBorder("title");
        addCompForTitledBorder(titled,
                               "titled line border Italix RED"
                                   + " (centered, default pos.)",
                               STitledBorder.CENTER,
                               STitledBorder.DEFAULT_POSITION,
                               titledBorders);
        titled.setTitleFont(new SFont(SFont.ITALIC));
        titled.setTitleColor(java.awt.Color.RED);

        titled = SBorderFactory.createSTitledBorder(loweredetched, "title");
        titled.getBorder().setColor(java.awt.Color.GREEN);
        addCompForTitledBorder(titled,
                               "titled lowered Green etched border"
                                   + " (right just., default pos.)",
                               STitledBorder.RIGHT,
                               STitledBorder.DEFAULT_POSITION,
                               titledBorders);

        titled = SBorderFactory.createSTitledBorder(
                        loweredbevel, "title");
        addCompForTitledBorder(titled,
                               "titled lowered bevel border"
                                   + " (default just., above top)",
                               STitledBorder.DEFAULT_JUSTIFICATION,
                               STitledBorder.ABOVE_TOP,
                               titledBorders);

        titled = SBorderFactory.createSTitledBorder(
                        empty, "title");
        addCompForTitledBorder(titled, "titled empty border"
                               + " (default just., bottom)",
                               STitledBorder.DEFAULT_JUSTIFICATION,
                               STitledBorder.BOTTOM,
                               titledBorders);

        //Fourth pane: compound borders
        SPanel compoundBorders = new SPanel();
        compoundBorders.setBorder(paneEdge);
        compoundBorders.setLayout(new SBoxLayout(SBoxLayout.Y_AXIS));
        SBorder redline = SBorderFactory.createSLineBorder(Color.red);


        STabbedPane tabbedPane = new STabbedPane();
        tabbedPane.addTab("Simple", null, simpleBorders, null);
        // tabbedPane.addTab("Matte", null, matteBorders, null);
        tabbedPane.addTab("Titled", null, titledBorders, null);
        // tabbedPane.addTab("Compound", null, compoundBorders, null);
        tabbedPane.setSelectedIndex(0);
        String toolTip = new String("<html>Blue Wavy Line border art crew:<br>&nbsp;&nbsp;&nbsp;Bill Pauley<br>&nbsp;&nbsp;&nbsp;Cris St. Aubyn<br>&nbsp;&nbsp;&nbsp;Ben Wronsky<br>&nbsp;&nbsp;&nbsp;Nathan Walrath<br>&nbsp;&nbsp;&nbsp;Tommy Adams, special consultant</html>");
        tabbedPane.setToolTipTextAt(1, toolTip);

        panel.add(tabbedPane);
        
        return panel;
    }

    void addCompForTitledBorder(STitledBorder border,
                                String description,
                                int justification,
                                int position,
                                SContainer container) {
        border.setTitleJustification(justification);
        //order.setTitlePosition(position);
        addCompForBorder(border, description,
                         container);
    }

    void addCompForBorder(SBorder border,
                          String description,
                          SContainer container) {
        SPanel comp = new SPanel(new SGridLayout(1, 1));
        SLabel label = new SLabel(description, SConstants.CENTER);
        comp.add(label);
        comp.setBorder(border);

        // container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(comp);
    }


    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = BorderExample.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


}
