// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Toolbar.plaf'
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SToolbar;
import org.wings.io.Device;
import org.wings.plaf.AbstractComponentCG;

import java.io.IOException;

public class ToolbarCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.ToolbarCG {

//--- byte array converted template snippets.
    private final static byte[] __div_class_tool= "<div class=\"toolbar\">".getBytes();
    private final static byte[] __table_cellspac= "<table cellspacing=\"0\" cellpadding=\"0\" vspace=\"0\" hspace=\"0\" width=\"100%\"".getBytes();
    private final static byte[] __              = ">".getBytes();
    private final static byte[] __tr_align_left = "<tr align=\"left\">".getBytes();
    private final static byte[] __td_width_100_t= "<td width=\"100%\"></td>".getBytes();
    private final static byte[] __td            = "<td>".getBytes();
    private final static byte[] __td_1          = "</td>".getBytes();
    private final static byte[] __tr_table      = "</tr></table>".getBytes();
    private final static byte[] __div           = "</div>".getBytes();

    public void writeContent(final Device device,
                      final SComponent _c)
        throws IOException {
        final SToolbar component = (SToolbar) _c;

        SToolbar toolbar = (SToolbar)component;
        int mcount = toolbar.getComponentCount();

        device.print("<table>");
        device.write(__tr_align_left);
        for (int i = 0; i < mcount; i++) {
            if ( toolbar.getComponent(i).isVisible() ) {
                if ( toolbar.getComponent(i).getHorizontalAlignment()==SConstants.RIGHT_ALIGN ) {
                    device.write(__td_width_100_t);
                }
                device.write(__td);
                toolbar.getComponent(i).write(device);
                device.write(__td_1);
            }
        }
        device.write(__tr_table);
//--- end code from write-template.
    }
}
