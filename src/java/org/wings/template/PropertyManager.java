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
package org.wings.template;

import org.wings.SComponent;

/**
 * Property Manager responsible to handle inline attributes in template layouts for specific component classes.
 * <p/>
 * Besides the dyanmic layout managers components can also be layouted inside the {@link org.wings.STemplateLayout}s.
 * In your template files you can use the <p/><code>&lt;object name="aConstraintName"&gt;&lt;/object&gt;</code><p/>
 * to place specific components inside this template i.e. like <code>panel.add(new SLabel("a label"), "aConstraintName"));</code>.
 * <p/>
 * The template layout mechanism is able to inline set properties of the arranged component inside the template.
 * This feature can be very useful if i.e. a web developer is soley responsible for the whole application Look &amp; Feel.
 * Depending on the type of the arranged component he can modify different visual properties (i.e. background color,
 * display text, etc.) just by modifying the template file.
 * I.e. let's assume you are placing the mentioned label oject in you template file. The the web designer can
 * overwrite the components background, display text, etc. by modifying his object inclusion tag to
 * <p/><code>&lt;object name="aConstraintName" background="#ff0000" text="new text"&gt;&lt;/object&gt;</code><p/>
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public interface PropertyManager {

    /**
     * Describe <code>setProperty</code> method here.
     *
     * @param comp  a <code>SComponent</code> value
     * @param name  a <code>String</code> value
     * @param value a <code>String</code> value
     */
    public void setProperty(SComponent comp, String name, String value);

    /**
     * Describe <code>getSupportedClasses</code> method here.
     *
     * @return a <code>Class[]</code> value
     */
    public Class[] getSupportedClasses();
}


