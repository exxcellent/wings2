// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/ResetButton.plaf'
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SResetButton;
import org.wings.io.Device;

import java.io.IOException;

public class ResetButtonCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.ResetButtonCG {

//--- byte array converted template snippets.


    public void writeContent(final Device device,
                             final SComponent _c)
        throws IOException {
        final SResetButton component = (SResetButton)_c;

        device.write("<input type=\"reset\"".getBytes());
        Utils.innerPreferredSize(device, component.getPreferredSize());
        org.wings.plaf.Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        org.wings.plaf.Utils.optAttribute(device, "value", component.getText());
        Utils.writeEvents(device, component);
        device.write("/>".getBytes());
    }
}
