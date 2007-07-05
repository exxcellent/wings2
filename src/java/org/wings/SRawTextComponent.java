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
package org.wings;

/**
 * Display area for a short text string.
 * 
 * This text is not interpreted.
 * Use for inserting raw text or even html snipplets.
 * 
 * @author <a href="mailto:ole@freiheit.com">Ole Langbehn</a>
 */
public class SRawTextComponent extends SComponent {

    /**
     * The text to be displayed
     */
    protected String text;

    /**
     * Creates a new <code>SRawTextComponent</code> instance with the specified text
     *
     * @param text The text to be displayed by the component.
     */
    public SRawTextComponent(String text) {
        this.text = text;
    }

    /**
     * Creates a new <code>SRawTextComponent</code> instance with no text.
     */
    public SRawTextComponent() {
        this((String) null);
    }

    /**
     * Returns the text of the component
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the component. If the value of text is null or an empty
     * string, nothing is displayed.
     *
     * @param t The new text
     */
    public void setText(String t) {
        reloadIfChange(text, t);
        text = t;
    }
}
