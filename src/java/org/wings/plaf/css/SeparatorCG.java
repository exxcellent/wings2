// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Separator.plaf'
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SSeparator;
import org.wings.io.Device;

import java.io.IOException;

public class SeparatorCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.SeparatorCG {

//--- byte array converted template snippets.
    private final static byte[] __hr = "<hr".getBytes();
    private final static byte[] __align_right = " align=\"right\"".getBytes();
    private final static byte[] __align_center = " align=\"center\"".getBytes();
    private final static byte[] __align_justify = " align=\"justify\"".getBytes();
    private final static byte[] __noshade_1 = " noshade=\"1\"".getBytes();
    private final static byte[] __ = "/>".getBytes();
    private final static byte[] ___1 = "\n".getBytes();

    public void installCG(final SComponent comp) {
    }

    public void uninstallCG(final SComponent comp) {
    }


    public void writeContent(final Device device,
                             final SComponent _c)
        throws IOException {
        final SSeparator component = (SSeparator)_c;

//--- code from write-template.

        device.write(__hr);
        org.wings.plaf.Utils.optAttribute(device, "class", org.wings.plaf.css.Utils.style(component));
        org.wings.plaf.Utils.optAttribute(device, "width", component.getWidth());
        org.wings.plaf.Utils.optAttribute(device, "size", component.getSize());

        switch (component.getAlignment()) {
            case SSeparator.RIGHT_ALIGN:
                device.write(__align_right);
                break;
            case SSeparator.CENTER_ALIGN:
                device.write(__align_center);
                break;
            case SSeparator.BLOCK_ALIGN:
                device.write(__align_justify);
                break;
        }
        ;
        if (!component.getShade()) {
            device.write(__noshade_1);
        }
        device.write(__);
        device.write(___1);

//--- end code from write-template.
    }
}
