/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */

package org.wings.plaf.css;

import org.wings.session.SessionManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LookAndFeel
    extends org.wings.plaf.LookAndFeel
{
    private static final String PROPERTIES_LOCATION = "WEB-INF/" + LookAndFeel.class.getPackage().getName() + ".properties";

    public LookAndFeel() throws IOException {
        super(loadProperties());
    }

    private static Properties loadProperties() throws IOException {
        try {
            Properties properties = new Properties();
            InputStream in = SessionManager.getSession().getServletContext().getResourceAsStream(PROPERTIES_LOCATION);
            properties.load(in);
            in.close();
            return properties;
        }
        catch (IOException e) {
            System.out.println("no " + PROPERTIES_LOCATION);
            throw e;
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
