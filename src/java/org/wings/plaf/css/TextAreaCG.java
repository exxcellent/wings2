// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/TextArea.plaf'
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.STextArea;
import org.wings.io.Device;

import java.io.IOException;

public class TextAreaCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.TextAreaCG {

//--- byte array converted template snippets.
    private final static byte[] __textarea = "<textarea".getBytes();
    private final static byte[] __ = ";\" ".getBytes();
    private final static byte[] __readonly_1 = " readonly=\"1\"".getBytes();
    private final static byte[] __name = " name=\"".getBytes();
    private final static byte[] ___1 = "\"".getBytes();
    private final static byte[] __id = " id=\"".getBytes();
    private final static byte[] __disabled_1 = " disabled=\"1\"".getBytes();
    private final static byte[] __wrap_virtual = " wrap=\"virtual\"".getBytes();
    private final static byte[] __wrap_physical = " wrap=\"physical\"".getBytes();
    private final static byte[] ___2 = ">".getBytes();
    private final static byte[] __textarea_1 = "</textarea>".getBytes();
    private final static byte[] ___3 = "\n".getBytes();


    public void writeContent(final Device device,
                             final SComponent _c)
        throws IOException {
        final STextArea component = (STextArea)_c;

        device.write(__textarea);
        org.wings.plaf.Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
        org.wings.plaf.Utils.optAttribute(device, "cols", component.getColumns());
        org.wings.plaf.Utils.optAttribute(device, "rows", component.getRows());
        org.wings.plaf.Utils.optAttribute(device, "focus", component.getComponentId());

        if (!component.isEditable()) {
            device.write(__readonly_1);
        }
        if (component.isEnabled()) {
            device.write(__name);
            org.wings.plaf.Utils.write(device, Utils.event(component));
            device.write(___1);
        }
        else {
            device.write(__disabled_1);
        }

        switch (component.getLineWrap()) {
            case STextArea.VIRTUAL_WRAP:
                device.write(__wrap_virtual);
                break;
            case STextArea.PHYSICAL_WRAP:
                device.write(__wrap_physical);
                break;
        }
        Utils.writeEvents(device, component);
        device.write(___2);
        org.wings.plaf.Utils.writeRaw(device, component.getText());
        device.write(__textarea_1);
        device.write(___3);
    }
}
