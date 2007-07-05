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
package org.wings.header;

import org.wings.io.Device;

import java.io.IOException;
import java.io.Serializable;

/**
 * Include a <code>&lt;META&gt;</code>-element inside the HTML header of rendered page.
 *
 * @author Holger Engels
 */
public class Meta implements Header, Serializable {
    protected String httpEquiv;
    protected String name;
    protected String lang;
    protected String content;

    public Meta(String httpEquiv, String name, String lang, String content) {
        this.httpEquiv = httpEquiv;
        this.name = name;
        this.lang = lang;
        this.content = content;
    }

    public Meta(String name, String lang, String content) {
        this(null, name, lang, content);

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getHttpEquiv() {
        return httpEquiv;
    }

    public void setHttpEquiv(String pHttpEquiv) {
        httpEquiv = pHttpEquiv;
    }

    public void write(Device d)
            throws IOException {
        d.print("<meta");
        if (httpEquiv != null)
            d.print(" http-equiv=\"" + httpEquiv + "\"");
        if (name != null)
            d.print(" name=\"" + name + "\"");
        if (lang != null)
            d.print(" lang=\"" + lang + "\"");
        if (content != null)
            d.print(" content=\"" + content + "\"");
        d.print("/>");
    }
}


