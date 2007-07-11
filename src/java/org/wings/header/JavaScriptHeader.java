package org.wings.header;

import org.wings.URLResource;
import org.wings.resource.DefaultURLResource;

/**
 * Use this to add a JavaScript header to a frame.
 * <code>
 *   frame.addHeader(new JavaScriptHeader("../myJavaScript.js"));
 * </code>
 */
public class JavaScriptHeader
    extends Script
{
    public JavaScriptHeader(String url) {
        this(new DefaultURLResource(url));
    }

    public JavaScriptHeader(URLResource resource) {
        super("text/javascript", resource);
    }
}
