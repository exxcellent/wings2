package org.wings.header;

import org.wings.DefaultURLResource;
import org.wings.URLResource;

/**
 * Use this to add style sheets to a frame
 * <pre>
 *  frame.addHeader(new StyleSheetLink("../myStyleSheet.css"));
 * </pre>
 *
 * @author armin
 * created at 15.01.2004 17:55:28
 */
public class StyleSheetLink extends Link {

    public StyleSheetLink(URLResource resource) {
        super("stylesheet",null, "text/css", null, resource);
    }

    public StyleSheetLink(String url) {
        this(new DefaultURLResource(url));
    }

}
