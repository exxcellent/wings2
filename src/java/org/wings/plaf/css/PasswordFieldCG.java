// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/PasswordField.plaf'
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SPasswordField;
import org.wings.io.Device;
import org.wings.plaf.AbstractComponentCG;

import java.io.IOException;

public class PasswordFieldCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.PasswordFieldCG {

//--- byte array converted template snippets.
    private final static byte[] __input_type_pas= "<input type=\"password\"".getBytes();
    private final static byte[] __readonly_1    = " readonly=\"1\"".getBytes();
    private final static byte[] __name          = " name=\"".getBytes();
    private final static byte[] __              = "\"".getBytes();
    private final static byte[] __disabled_1    = " disabled=\"1\"".getBytes();
    private final static byte[] ___1            = "/>".getBytes();


    public void writeContent(final Device device,
                      final SComponent _c)
        throws IOException {
        final SPasswordField component = (SPasswordField) _c;

        device.write(__input_type_pas);
        org.wings.plaf.css.Utils.writeEvents(device, component);
        org.wings.plaf.Utils.optAttribute( device, "size", component.getColumns());
        org.wings.plaf.Utils.optAttribute( device, "tabindex", component.getFocusTraversalIndex());
        org.wings.plaf.Utils.optAttribute( device, "maxlength", component.getMaxColumns());
        org.wings.plaf.Utils.optAttribute( device, "id", component.getComponentId());
        if (!component.isEditable()) {
            device.write(__readonly_1);
        }
        if (component.isEnabled())   {
            device.write(__name);
            org.wings.plaf.Utils.write( device, Utils.event(component));
            device.write(__);
        } else 
            {
            device.write(__disabled_1);
        }
        org.wings.plaf.Utils.optAttribute( device, "value", component.getText());
        Utils.writeEvents(device, component);
        device.write(___1);
    }
}
