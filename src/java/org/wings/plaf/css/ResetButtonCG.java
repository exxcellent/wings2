// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/ResetButton.plaf'
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SResetButton;
import org.wings.io.Device;
import org.wings.plaf.AbstractComponentCG;

import java.io.IOException;

public class ResetButtonCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.ResetButtonCG {

//--- byte array converted template snippets.
    private final static byte[] __input_type_res= "<input type=\"reset\"".getBytes();
    private final static byte[] __              = "/>".getBytes();


    public void writeContent(final Device device,
                      final SComponent _c)
        throws IOException {
        final SResetButton component = (SResetButton) _c;

//--- code from write-template.

        device.write(__input_type_res);
        Utils.writeEvents(device, component);
        org.wings.plaf.Utils.optAttribute( device, "class", org.wings.plaf.css.Utils.style(component));
        org.wings.plaf.Utils.optAttribute( device, "tabindex", component.getFocusTraversalIndex());
        org.wings.plaf.Utils.optAttribute( device, "value", component.getText());
        device.write(__);
//--- end code from write-template.
    }
}
