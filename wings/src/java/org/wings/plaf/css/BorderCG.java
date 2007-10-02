// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.plaf.css;

import org.wings.io.Device;
import org.wings.SComponent;
import org.wings.border.STitledBorder;

import java.io.IOException;

/** This is not a 'real' CG class but a class collection rendering code for variois borders. */
public class BorderCG {
    private BorderCG() {
    }

    public static void writeComponentBorderPrefix(final Device device, final SComponent component) throws IOException {
        if (component != null && component.getBorder() instanceof STitledBorder) {
            device.print("<fieldset><legend>").print(((STitledBorder)component.getBorder()).getTitle()).print("</legend>");
        }
    }

    public static void writeComponentBorderSufix(final Device device, final SComponent component) throws IOException {
        if (component != null && component.getBorder() instanceof STitledBorder) {
            device.print("</fieldset>");
        }
    }

}
