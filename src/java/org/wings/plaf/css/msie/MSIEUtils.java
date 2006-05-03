// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.plaf.css.msie;

import org.wings.SComponent;
import org.wings.SimpleURL;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;

import java.io.IOException;

/**
 * Utility mehtods for the MSIE based PLAF rendering.
 */
final class MSIEUtils {

    /**
     * Render the start of an <code>A</code> element which submits a form and an
     * event value for the passed component.
     *
     * @param component Event target
     * @param value     Event value
     */
    static void writeSubmitAnchorStart(final Device device, final SComponent component, final String value) throws IOException {
        device.print("<a href=\"").print(component.getRequestURL());
        device.print("?").print(Utils.event(component)).print("=").print(value).print("\" ");
        device.print("onclick=\"sendEvent(event,'");
        device.print(value);
        device.print("','");
        device.print(Utils.event(component));
        device.print("')\"");
    }

    /** Writes an <code>A HREF</code> element for an RequestURL. */
    static void writeSubmitAnchorStart(final Device device, final SimpleURL selectionAddr) throws IOException {
        device.print("<a href=\"").print(selectionAddr.toString()).print("\" ");
        /*device.print("<a href=\"#\" onclick=\"location.href='");
        Utils.write(device, selectionAddr.toString());
        device.print("';\"");*/
    }

    /** Contrary to {@link #writeSubmitAnchorStart(org.wings.io.Device, org.wings.SComponent, String)} */
    static void writeSubmitAnchorEnd(final Device device) throws IOException {
        device.print("</a>");
    }

    /**
     * Render the start of an submit <code>INPUT</code> element which submits a form and an
     * event value for the passed component.
     *
     * @param component Event target
     * @param value     Event value
     */
    static void writeSubmitInputStart(final Device device, final SComponent component, final String value) throws IOException {
        device.print("<input type=\"submit\" name=\"").print(component.getLowLevelEventId()).print("\"");
        device.print(" value=\"").print(value).print("\" ");
        device.print("onclick=\"sendEvent(event,'");
        device.print(value);
        device.print("','");
        device.print(Utils.event(component));
        device.print("')\"");
    }

        /** Contrary to {@link #writeSubmitInputStart(org.wings.io.Device, org.wings.SComponent, String)}  */
   static void writeSubmitInputEnd(final Device device) throws IOException {
       device.print("</input>");
   }
}
