// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/FileChooser.plaf'
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SFileChooser;
import org.wings.io.Device;
import org.wings.plaf.AbstractComponentCG;

import java.io.IOException;

public class FileChooserCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.FileChooserCG {

//--- byte array converted template snippets.
    private final static byte[] __input_type_fil= "<input type=\"file\"".getBytes();
    private final static byte[] __name          = " name=\"".getBytes();
    private final static byte[] __              = "\"".getBytes();
    private final static byte[] __id            = " id=\"".getBytes();
    private final static byte[] __readonly_1    = " readonly=\"1\"".getBytes();
    private final static byte[] ___2            = "\n".getBytes();

    public void writeContent(final Device device,
                      final SComponent _c)
        throws IOException {
        final SFileChooser component = (SFileChooser) _c;

        int columns = component.getColumns();
        /*
        * for some wierd reason, the 'maxlength' column contains the
        * maximum content length .. see RFC1867.
        * .. anyway, all browsers seem to ignore it or worse, render the
        * file upload unusable (konqueror 2.2.2).
        */
        //int maxContent = component.getSession().getMaxContentLength()*1024;

        // maxLength = maxContent removed, since it does not work.
        device.write(__input_type_fil);
        org.wings.plaf.css.Utils.writeEvents(device, component);
        org.wings.plaf.Utils.optAttribute( device, "size", columns);
        org.wings.plaf.Utils.optAttribute( device, "accept", component.getFileNameFilter());
        if ( component.isEnabled() ) {
            device.write(__name);
            org.wings.plaf.Utils.write( device, Utils.event(component));
            device.write(__);
            device.write(__id);
            org.wings.plaf.Utils.write( device, component.getComponentId());
            device.write(__);
        } else {
            device.write(__readonly_1);
        }
        org.wings.plaf.Utils.optAttribute( device, "tabindex", component.getFocusTraversalIndex());
        org.wings.plaf.Utils.optAttribute( device, "class", Utils.style(component));

        Utils.writeEvents(device, component);
        device.write("/>".getBytes());
    }
}
