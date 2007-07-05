/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.recorder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
