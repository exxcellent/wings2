package org.wings.session;

import org.wings.io.Device;

/**
 * Created by IntelliJ IDEA.
 * User: hengels
 * Date: Aug 23, 2006
 * Time: 12:49:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ExceptionHandler
{
    void handle(Device device, Throwable thrown);
}
