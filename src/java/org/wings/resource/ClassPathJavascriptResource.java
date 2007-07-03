package org.wings.resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;

public class ClassPathJavascriptResource extends ClassPathResource {

    private String callbackCode;

    public ClassPathJavascriptResource(String resourceFileName, String callbackCode) {
        this(resourceFileName, "text/javascript", callbackCode);
    }

    public ClassPathJavascriptResource(String resourceFileName, String mimeType, String callbackCode) {
        super(resourceFileName, mimeType);
        this.callbackCode = callbackCode;
    }

    public ClassPathJavascriptResource(ClassLoader classLoader, String resourceFileName, String callbackCode) {
        super(classLoader, resourceFileName);
        this.callbackCode = callbackCode;
    }

    public ClassPathJavascriptResource(ClassLoader classLoader, String resourceFileName, String mimeType, String callbackCode) {
        super(classLoader, resourceFileName, mimeType);
        this.callbackCode = callbackCode;
    }

    protected InputStream getResourceStream() {
        if (callbackCode == null) {
            return super.getResourceStream();
        }
        return new SequenceInputStream(super.getResourceStream(), new ByteArrayInputStream(("\n\n" + callbackCode).getBytes()));
    }

}
