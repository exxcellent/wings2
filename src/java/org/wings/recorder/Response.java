package org.wings.recorder;

import java.util.regex.*;

/**
 * Completely useless at the moment! This should become a handy tool for
 * examination of result pages.
 */
public class Response {
    private String body;

    public Response(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getTextValue(String name) {
        Pattern p = Pattern.compile("name ?= ?\"" + name + "\" .* value ?= ?\"([^\"]*)\"");
        Matcher m = p.matcher(body);
        m.find();
        return m.group(1);
    }

    public boolean isCheckBoxSelected(String name) {
        Pattern p = Pattern.compile("name ?= ?\"" + name + "\" .* checked ?= ?\"([^\"]*)\"");
        Matcher m = p.matcher(body);
        m.find();
        String s = m.group(1);
        return "1".equals(s) || "on".equals(s) || "yes".equals(s);
    }
}
