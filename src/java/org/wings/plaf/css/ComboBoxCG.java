// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/ComboBox.plaf'
package org.wings.plaf.css;


import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;

import java.io.IOException;

public class ComboBoxCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.ComboBoxCG {

//--- byte array converted template snippets.
    private final static byte[] __select_size_1 = "<select size=\"1\"".getBytes();
    private final static byte[] __disabled_1 = " disabled=\"1\"".getBytes();
    private final static byte[] __ = ">".getBytes();
    private final static byte[] __renderer_null = "<!--renderer==null-->".getBytes();
    private final static byte[] __option = "\n<option".getBytes();
    private final static byte[] __selected_selec = " selected=\"selected\"".getBytes();
    private final static byte[] __title = " title=\"".getBytes();
    private final static byte[] ___1 = "\"".getBytes();
    private final static byte[] __style = " style=\"".getBytes();
    private final static byte[] __cellrenderer_n = "<!--cellrenderer==null, use toString-->".getBytes();
    private final static byte[] __option_1 = "</option>".getBytes();
    private final static byte[] __select = "\n</select>".getBytes();
    private final static byte[] __input_type_hid = "<input type=\"hidden\"".getBytes();
    private final static byte[] ___2 = "/>".getBytes();
    private final static byte[] ___3 = "\n".getBytes();

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        final SComboBox component = (SComboBox)comp;
        final CGManager manager = component.getSession().getCGManager();
        Object value;
        value = manager.getObject("SComboBox.renderer", SDefaultListCellRenderer.class);
        if (value != null) {
            component.setRenderer((SDefaultListCellRenderer)value);
        }
    }

//--- code from common area in template.
    private static final JavaScriptListener submitListener = new JavaScriptListener(JavaScriptEvent.ON_CHANGE, "submit()");

    protected void writeFormComboBox(Device device, SComboBox comboBox) throws IOException {

        device.write(__select_size_1);
        org.wings.plaf.Utils.optAttribute(device, "name", Utils.event(comboBox));
        org.wings.plaf.Utils.optAttribute(device, "tabindex", comboBox.getFocusTraversalIndex());
        org.wings.plaf.Utils.optAttribute(device, "focus", comboBox.getComponentId());

        if (!comboBox.isEnabled()) {
            device.write(__disabled_1);
        }

        comboBox.removeScriptListener(submitListener);
        if (comboBox.getActionListeners().length > 0) {
            comboBox.addScriptListener(submitListener);
        }

        Utils.writeEvents(device, comboBox);

        device.write(__);
        javax.swing.ComboBoxModel model = comboBox.getModel();
        int size = model.getSize();
        int selected = comboBox.getSelectedIndex();

        SListCellRenderer renderer = comboBox.getRenderer();

        for (int i = 0; i < size; i++) {
            SComponent cellRenderer = null;
            if (renderer != null) {
                cellRenderer = renderer.getListCellRendererComponent(comboBox, model.getElementAt(i), false, i);
            }
            else {

                device.write(__renderer_null);
            }


            device.write(__option);
            org.wings.plaf.Utils.optAttribute(device, "value", comboBox.getSelectionParameter(i));
            if (selected == i) {

                device.write(__selected_selec);
            }

            if (cellRenderer != null) {
                if (cellRenderer.getToolTipText() != null) {

                    device.write(__title);
                    org.wings.plaf.Utils.write(device, cellRenderer.getToolTipText());

                    device.write(___1);
                }

                org.wings.io.StringBufferDevice stringBufferDevice = getStringBufferDevice();
                org.wings.plaf.xhtml.Utils.writeAttributes(stringBufferDevice, cellRenderer);
                String styleString = stringBufferDevice.toString();
                if (styleString != null && styleString.length() > 0) {

                    device.write(__style);
                    org.wings.plaf.Utils.write(device, styleString);

                    device.write(___1);
                }
            }

            device.write(__);
            if (cellRenderer != null) {
                // Hack: remove all tags, because in form selections, looks ugly.
                org.wings.io.StringBufferDevice string = getStringBufferDevice();
                cellRenderer.write(string);
                char[] chars = string.toString().toCharArray();
                int pos = 0;
                for (int c = 0; c < chars.length; c++) {
                    switch (chars[c]) {
                        case '<':
                            device.print(chars, pos, c - pos);
                            break;
                        case '>':
                            pos = c + 1;
                    }
                }
                device.print(chars, pos, chars.length - pos);
            }
            else {

                device.write(__cellrenderer_n);
                device.print(model.getElementAt(i).toString());
            }

            device.write(__option_1);
        }


        device.write(__select);
        // util method

        device.write(__input_type_hid);
        org.wings.plaf.Utils.optAttribute(device, "name", Utils.event(comboBox));
        org.wings.plaf.Utils.optAttribute(device, "value", -1);

        device.write(___2);
    }

    private org.wings.io.StringBufferDevice stringBufferDevice = null;

    protected org.wings.io.StringBufferDevice getStringBufferDevice() {
        if (stringBufferDevice == null) {
            stringBufferDevice = new org.wings.io.StringBufferDevice();
        }
        stringBufferDevice.reset();
        return stringBufferDevice;
    }


//--- end code from common area in template.


    public void writeContent(final Device device,
                             final SComponent _c)
        throws IOException {
        final SComboBox component = (SComboBox)_c;

//--- code from write-template.
        SComboBox comboBox = (SComboBox)component;
        // TODO: implement anchor combobox
        //if (comboBox.getShowAsFormComponent()) {
        writeFormComboBox(device, comboBox);
        //} else {
        //    writeAnchorComboBox(device, comboBox);
        // }
        device.write(___3);

//--- end code from write-template.
    }
}
