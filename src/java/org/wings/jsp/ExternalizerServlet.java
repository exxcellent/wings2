/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.jsp;

import org.wings.externalizer.ExternalizedResource;
import org.wings.io.DeviceFactory;
import org.wings.io.Device;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author hengels
 * @version $Revision$
 */
public class ExternalizerServlet
        extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WingsSession wingsSession = WingsSession.getSession(request, response);

        synchronized (request.getSession()) {
            String pathInfo = request.getServletPath();
            if (pathInfo == null)
                return;

            int pos = pathInfo.lastIndexOf('/');
            pathInfo = pathInfo.substring(pos + 1);
            ExternalizedResource extInfo = wingsSession.getExternalizeManager().getExternalizedResource(pathInfo);
            Device outputDevice = DeviceFactory.createDevice(extInfo);
            wingsSession.getExternalizeManager().deliver(extInfo, response, outputDevice);
        }
    }
}
