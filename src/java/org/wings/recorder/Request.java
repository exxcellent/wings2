/* $Id$ */
package org.wings.recorder;

import java.util.*;

/**
 * @author hengels
 */
class Request {
    private String method;
    private String resource;
    private long millis;
    List events = new LinkedList();
    List headers = new LinkedList();

    public Request(String method, String resource) {
        this.method = method;
        this.resource = resource;
        this.millis = System.currentTimeMillis();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public List getEvents() {
        return events;
    }

    public List getHeaders() {
        return headers;
    }

    public long getMillis() {
        return millis;
    }

    static class Header {
        private String name;
        private String value;

        public Header(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

    static class Event {
        private String name;
        private String[] values;

        public Event(String name, String[] values) {
            this.name = name;
            this.values = values;
        }

        public String getName() {
            return name;
        }

        public String[] getValues() {
            return values;
        }
    }
}
