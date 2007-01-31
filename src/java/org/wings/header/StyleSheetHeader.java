package org.wings.header;

import org.wings.URLResource;
import org.wings.resource.DefaultURLResource;

/**
 * Use this to add a StyleSheet header to a frame.
 * <code>
 *   frame.addHeader(new StyleSheetHeader("../myStyleSheet.css"));
 * </code>
 */
public class StyleSheetHeader extends Link {


    public StyleSheetHeader(String url) {
        this(new DefaultURLResource(url));
    }

    public StyleSheetHeader(URLResource resource) {
        super("stylesheet", null, "text/css", null, resource);
    }

}