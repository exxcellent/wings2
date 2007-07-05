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

import org.wings.template.*;
import org.wings.template.propertymanagers.SAbstractButtonPropertyManager;
import org.wings.template.propertymanagers.SAbstractIconTextCompoundPropertyManager;
import org.wings.template.propertymanagers.SComponentPropertyManager;
import org.wings.template.propertymanagers.SListPropertyManager;
import org.wings.template.propertymanagers.SLabelPropertyManager;
import org.wings.template.propertymanagers.STextFieldPropertyManager;
import org.wings.template.propertymanagers.STablePropertyManager;
import org.wings.template.propertymanagers.SFileChooserPropertyManager;
import org.wings.template.propertymanagers.STextAreaPropertyManager;
import org.wings.template.parser.PageParser;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Static layout manager that uses template files to arrange components.
 * <p/>
 * Like any other layout manager it allows to place arbitrary elements, but you can
 * write a simple HTML-page being the template for your container component.
 * Though we encourage the use of the dynamic layout managers, this layout manager can be
 * very useful in realising the main page layout of your web application.
 * <p/>
 * To use this layout manager you have to define a template file required by the STemplateLayout
 * instance. Inside this template file you can insert inside your custom HTML code desired
 * wingS objects using tags like
 * <p/>
 * <code>&lt;input name=&quot;compname&quot;&gt;</code>
 * <p/> or
 * <p/><code>&lt;object name=&quot;compname&quot;&gt;&lt;/object&gt;</code>
 * <p/>
 * The name attribute of these <code>input</code> or <code>object</code> tag is the name
 * you have to use as layout <em>constraint</em> when you add the desired component to the
 * template layout managed {@link SContainer}:
 * <p/><code>panel.add(new SLabel("a test label), "compname"));</code>
 * <p/>
 * Besides this simple inlining mechanism the STemplateLayout manager has also another
 * very powerful feature: <b>Specific components bean attributes can be overwritten</b> by
 * specific optional inline attributes attached to your <code>object</code> html tags i.e. like
 * <p/><code>&lt;object name="compname" background="#ff0000" text="new text"&gt;&lt;/object&gt;</code><p/>
 * Please refer to javadoc of {@link PropertyManager} for more information on this feature.
 * <p/>
 * <b>Sample template file:</b><br/>
 * <CODE>
 * &lt;HTML&gt;&lt;BODY&gt;
 * Name der Person: &lt;COMPONENT NAME=NAME&gt;&lt;BR&gt;
 * Vorname der Person: &lt;COMPONENT NAME=VORNAME ICON="vorname.gif"&gt;&lt;BR&gt;
 * Wohnort der Person: &lt;COMPONENT NAME=WOHNORT&gt;&lt;BR&gt;
 * &lt;/BODY&gt;&lt;/HTML&gt;
 * </CODE>
 * <BR>
 * <b>According java sample code:</b>:<BR>
 * <CODE>
 * templateContainer.setLayout(new STemplateLayout("templatefile"));
 * templateContainer.addComponent(new SLabel("Haaf"), "NAME");
 * templateContainer.addComponent(new SButton("Armin"), "VORNAME");
 * templateContainer.addComponent(new SLabel("Neu-Ulm"), "WOHNORT");
 * </CODE>
 *
 * @see  PropertyManager
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author Jochen Woehrle
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 */
public class STemplateLayout extends SAbstractLayoutManager {
    /*
     * Dieser PropertyManager behandelt alle Properties, zu denen kein eigener
     * PropertyManager gefunden wurde.
     */
    private static final PropertyManager defaultPropertyManager =
            new PropertyManager() {
                final Class[] empty = new Class[0];

                public void setProperty(SComponent c, String name,
                                        String value) {
                }

                public Class[] getSupportedClasses() {
                    return empty;
                }
            };

    /*
     * Alle Property Manager
     */
    private static final HashMap propertyManager = new HashMap();

    /*
     * some default property Managers. Sets properties of components
     * from parameters given in the template page.
     */
    static {
        addPropertyManager(new SComponentPropertyManager());
        addPropertyManager(new SAbstractIconTextCompoundPropertyManager());
        addPropertyManager(new SAbstractButtonPropertyManager());
        addPropertyManager(new SLabelPropertyManager());
        addPropertyManager(new STextFieldPropertyManager());
        addPropertyManager(new STextAreaPropertyManager());
        addPropertyManager(new STablePropertyManager());
        addPropertyManager(new SFileChooserPropertyManager());
        addPropertyManager(new SListPropertyManager());
    }

    /**
     * Abstraction of the template source (file, resource, ..)
     */
    protected HashMap components = new HashMap();

    private TemplateSource templateSource = null;

    /**
     * PageParser to use
     */
    protected transient PageParser pageParser;


    public STemplateLayout() {}

    /**
     * Create a TemplateLayout that reads its content from the generic
     * {@link TemplateSource}. The template source can be implemented
     * to be read from any source you want to, e.g. database BLOBs. Whenever
     * the source changes (i.e. lastModified() returns a different
     * modification time the last time this source has been parsed), the
     * template is reparsed.
     *
     * @param source the TemplateSource this template is to be read from.
     */
    public STemplateLayout(TemplateSource source) {
        setTemplate(source);
    }

    /**
     * Open a template from a file with the given name.
     * Whenever the file changes, the Template is reloaded.
     *
     * @param tmplFileName the filename to read the file from.
     * @throws java.io.IOException
     */
    public STemplateLayout(String tmplFileName) throws java.io.IOException {
        setTemplate(new File(tmplFileName));
    }

    /**
     * Read the template from a file.
     * Open a template from a file.
     * Whenever the file changes, the Template is reloaded.
     *
     * @param tmplFile the File to read the template from.
     * @throws java.io.IOException
     */
    public STemplateLayout(File tmplFile) throws java.io.IOException {
        setTemplate(tmplFile);
    }

    /**
     * Read the template from an URL.
     * The content is cached.
     *
     * @param url the URL to read the template from.
     * @throws java.io.IOException
     */
    public STemplateLayout(URL url) throws java.io.IOException {
        setTemplate(url);
    }

    /**
     * Determines appropriate property manager for the given SComponent or
     * derived class. Goes up the hierarchy.
     */
    public static final PropertyManager getPropertyManager(Class c) {
        if (c == null)
            return defaultPropertyManager;

        PropertyManager p = (PropertyManager) propertyManager.get(c);

        if (p == null)
            return getPropertyManager(c.getSuperclass());

        return p;
    }

    /**
     * Adds a PropertyManager.
     * A Property Manager provides a mapping from properties given in
     * template tags to properties of components.
     * PropertyManager are responsible for a number of component classes; if
     * there is already a mapping for a certain class, then this mapping
     * is <em>not</em> added.
     */
    public static final void addPropertyManager(PropertyManager p) {
        if (p == null)
            return;

        Class[] cl = p.getSupportedClasses();
        if (cl == null)
            return;

        for (int i = 0; i < cl.length; i++) {
            if (!propertyManager.containsKey(cl[i]))
                propertyManager.put(cl[i], p);
        }
    }

    /**
     * Deregisters a property manager for a given component class to be able to replace it.
     * @param supportedComponentClass Target class as in {@link org.wings.template.PropertyManager#getSupportedClasses()}
     */
    public static final PropertyManager removePropertyManager(Class supportedComponentClass) {
        if (supportedComponentClass == null)
            return null;
        return (PropertyManager) propertyManager.remove(supportedComponentClass);
    }

    /**
     * Set the template to the template given as file name.
     *
     * @throws java.io.IOException
     */
    public void setTemplate(String templateFileName)
            throws java.io.IOException {
        setTemplate(new File(templateFileName));
    }

    /**
     * Set the template to the template stored in the given file.
     *
     * @throws java.io.IOException
     */
    public void setTemplate(File templateFile) throws java.io.IOException {
        setTemplate(new CachedFileTemplateSource(templateFile));
    }

    /**
     * Set the template to the template which can be retrieved from the
     * given URL.
     *
     * @throws java.io.IOException
     */
    public void setTemplate(URL templateURL) throws java.io.IOException {
        if ("file".equals(templateURL.getProtocol())) {
            setTemplate(new File(templateURL.getFile()));
        } else {
            setTemplate(new CachedFileTemplateSource(templateURL));
        }
    }

    /**
     * Sets the template from the DataSource. Use this, if you hold your
     * templates in databases etc. and write your own DataSource.
     *
     * @param source the source this template is to be read.
     * @see org.wings.template.TemplateSource
     */
    public void setTemplate(TemplateSource source) {
        templateSource = source;
    }

    /**
     * add a component with the given constraint. The contstraint in the
     * TemplateLayout is the value of the name attribute of the object in
     * the HTML-template.
     *
     * @param c          the component to be added
     * @param constraint the string describing the
     */
    public void addComponent(SComponent c, Object constraint, int index) {
        if (constraint == null)
            throw new IllegalArgumentException("null constraint not allowed here");
        components.put(constraint.toString(), c);
    }

    /**
     * removes the given component.
     *
     * @param comp the component to be removed.
     */
    public void removeComponent(SComponent comp) {
        Iterator it = components.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            if (e.getValue() == comp)
                it.remove();
        }
    }

    /**
     * returns a map of the constraint/component.
     */
    public SComponent getComponent(String name) {
        return (SComponent) components.get(name);
    }

    /**
     * Returns the source of the template layout.
     * @see #setTemplate(TemplateSource)
     * @return the template source
     */
    public TemplateSource getTemplateSource() {
        return templateSource;
    }

    /**
     * Retrieve the PageParser of this instance
     *
     * @return the current PageParser
     */
    public PageParser getPageParser() {
        return pageParser != null ? pageParser :  PageParser.getInstance();
    }

    /**
     * Set the PageParser for this instance
     *
     * @param pageParser the new PageParser
     */
    public void setPageParser(PageParser pageParser) {
        this.pageParser = pageParser;
    }
}


