/* $Id$ */
package org.wings.recorder;

/**
 * @author hengels
 */
public class GET
    extends Request {
    public GET(String resource) {
        super("GET", resource);
    }

    public GET addEvent(String name, String[] values) {
        events.add(new Event(name, values));
        return this;
    }

    public GET addHeader(String name, String header) {
        headers.add(new Header(name, header));
        return this;
    }
}
