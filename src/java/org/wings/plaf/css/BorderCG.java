// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.plaf.css;

import org.wings.io.Device;
import org.wings.SComponent;
import org.wings.border.*;
import org.wings.style.*;

import java.io.IOException;

/** This is not a 'real' CG class but a class collection rendering code for variois borders. */
public class BorderCG {
    private BorderCG() {
    }
    
    public static void writeComponentBorderPrefix(final Device device, final SComponent component) throws IOException {
        if (component != null && component.getBorder() instanceof STitledBorder) {
            STitledBorder titledBorder = (STitledBorder)component.getBorder();
            device.print("<fieldset ");
            Utils.optAttribute(device, "id", component.getName()); /* Gives the Fieldset an id to get update working */

            if (titledBorder.getBorder() != null && titledBorder.getBorder().getAttributes() != null) {
                Utils.optAttribute(device, "style", titledBorder.getBorder().getAttributes().toString());
            }

            device.print("><legend ");
            switch (titledBorder.getTitleJustification()) {
                case STitledBorder.DEFAULT_JUSTIFICATION:
                    break;
                case STitledBorder.LEFT:
                    break;
                case STitledBorder.CENTER:
                    device.print(" align=\"center\" ");
                    break;
                case STitledBorder.RIGHT:
                    device.print(" align=\"right\" ");
                    break;
            }
                        
            Utils.optAttribute( device, "style", getLegendAttributes(titledBorder) ); /* Write Legend Specifiec styles */
            device.print(">").print(((STitledBorder)component.getBorder()).getTitle()).print("</legend>");
        }
    }
    
    public static void writeComponentBorderSufix(final Device device, final SComponent component) throws IOException {
        if (component != null && component.getBorder() instanceof STitledBorder ) {
            device.print("</fieldset>");
        }
    }
    
    
    /**
     * getLegendAttributes
     * generates css Atrivbutes for the Legend Property 
     *
     * @param border with the Toitled Border Info
     **/
    
    private static  CSSAttributeSet getLegendAttributes(STitledBorder border) {
       CSSAttributeSet attributes = new CSSAttributeSet();

//            switch (border.getTitleJustification()) { /* Position of the Text */
//                case STitledBorder.DEFAULT_JUSTIFICATION:
//                    break;
//                case STitledBorder.LEFT:
//                    break;
//                case STitledBorder.CENTER:
//                    attributes.put(CSSProperty.TEXT_ALIGN,"center");
//                    break;
//                case STitledBorder.RIGHT:
//                    attributes.put(CSSProperty.TEXT_ALIGN, "right");
//                    break;
//            }
            if ( border.getTitleFont() != null ) {  /* Do we have some font information */
                attributes.putAll(CSSStyleSheet.getAttributes(border.getTitleFont()));
            }

            if ( border.getTitleColor() != null) { /* Are there any color requests */
                attributes.put(CSSProperty.COLOR, CSSStyleSheet.getAttribute(border.getTitleColor()));
            }

        return attributes;
    }
}
