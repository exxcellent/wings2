package org.wings.resource;

import org.wings.URLResource;
import org.wings.SimpleURL;

/**
 * Default implementation of an URLResource
 * @author armin
 * created at 15.01.2004 17:58:29
 */
public class DefaultURLResource implements URLResource {

    private final SimpleURL url;

    public DefaultURLResource(String s) {
        url = new SimpleURL(s);
    }

    public SimpleURL getURL() {
        return url;
    }
}
