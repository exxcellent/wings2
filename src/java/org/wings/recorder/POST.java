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

/**
 * @author hengels
 */
public class POST
        extends Request {
    public POST(String resource) {
        super("POST", resource);
    }

    public POST addEvent(String name, String[] values) {
        events.add(new Event(name, values));
        return this;
    }

    public POST addHeader(String name, String header) {
        headers.add(new Header(name, header));
        return this;
    }
}
