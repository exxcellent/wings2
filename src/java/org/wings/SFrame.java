/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import org.wings.plaf.FrameCG;
import org.wings.resource.DynamicCodeResource;
import org.wings.resource.DynamicResource;
import org.wings.session.SessionManager;
import org.wings.style.StyleSheet;
import org.wings.util.ComponentVisitor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The frame is the root component in every component hierarchie.
 * A SessionServlet requires an instance of SFrame to render the page.
 * SFrame consists of some header informaton (meta, link, script)
 * and a stack of components. The bottommost component of the stack is always
 * the contentPane. When dialogs are to be shown, they are stacked on top of
 * it.
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>,
 *         <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SFrame
        extends SRootContainer
        implements PropertyChangeListener, LowLevelEventListener {

    /**
     * The Title of the Frame.
     */
    protected String title;

    /**
     * A Set containing additional tags for the html header.
     */
    protected List headers;

    /**
     * the style sheet used in certain look and feels.
     */
    protected StyleSheet styleSheet;  // IMPORTANT: initialization with null causes errors;
    // These: all properties, that are installed by the plaf, are installed during the initialization of
    // SComponent. The null initializations happen afterwards and overwrite the plaf installed values.
    // However: null is the default initialization value, so this is not a problem!
    // The same applies to all descendants of SComponent!

    protected String statusLine;

    private RequestURL requestURL = null;
    private String targetResource;

    private HashMap dynamicResources;

    private SComponent focusComponent = null;                //Component which requests the focus

    /**
     * Creates a new SFrame
     */
    public SFrame() {
        getSession().addPropertyChangeListener("lookAndFeel", this);
        getSession().addPropertyChangeListener("request.url", this);
    }

    /**
     * Creates a new SFrame
     *
     * @param title Title of this frame, rendered in browser window title
     */
    public SFrame(String title) {
        this();
        setTitle(title);
    }

    /**
     * Adds a dynamic ressoure.
     * @see #getDynamicResource(Class)
     */
    public void addDynamicResource(DynamicResource d) {
        if (dynamicResources == null) {
            dynamicResources = new HashMap();
        }
        dynamicResources.put(d.getClass(), d);
    }

    /**
     * Removes the instance of the dynamic ressource of the given class.
     * @see #getDynamicResource(Class)
     * @param dynamicResourceClass Class of dynamic ressource to remove
     */
    public void removeDynamicResource(Class dynamicResourceClass) {
        if (dynamicResources != null) {
            dynamicResources.remove(dynamicResourceClass);
        }
    }

    /**
     * Severeral Dynamic code Ressources are attached to a <code>SFrame</code>.
     * <br>See <code>Frame.plaf</code> for details, but in general you wil find attached
     * to every <code>SFrame</code> a
     * <ul><li>A {@link DynamicCodeResource} rendering the HTML-Code of all SComponents inside this frame.
     * <li>A {@link org.wings.script.DynamicScriptResource} rendering the attached (Java-)Scripts of all SComponents
     * into an external file and including them by a link tag into the rendered frame.
     * <li>A {@link org.wings.style.DynamicStyleSheetResource} rendering the CSS attributes
     * of all SComponents inside this frame into an external file with CSS classes.
     * </ul>
     */
    public DynamicResource getDynamicResource(Class c) {
        if (dynamicResources == null) {
            dynamicResources = new HashMap();
        }
        return (DynamicResource) dynamicResources.get(c);
    }

    /**
     * Return <code>this</code>.
     *
     * @return this.
     */
    public SFrame getParentFrame() {
        return this;
    }

    /**
     * A String with the current epoch of this SFrame. Provided by the
     * {@link DynamicCodeResource} rendering this frame.
     *
     * @return A String with current epoch. Increased on every invalidation.
     */
    public String getEventEpoch() {
        return getDynamicResource(DynamicCodeResource.class).getEpoch();
    }

    /**
     * Set server address.
     */
    public final void setRequestURL(RequestURL requestURL) {
        this.requestURL = requestURL;
    }

    /**
     * Returns the base URL for a request to the WingsServlet. This URL
     * is used to assemble an URL that trigger events. In order to be used
     * for this purpose, you've to add your parameters here.
     */
    public final RequestURL getRequestURL() {
        RequestURL result = null;
        // first time we are called, and we didn't get any change yet
        if (requestURL == null) {
            requestURL = (RequestURL) SessionManager.getSession().getProperty("request.url");
        }
        if (requestURL != null) {
            result = (RequestURL) requestURL.clone();
            result.setResource(getTargetResource());
        }
        return result;
    }

    /**
     * Set the target resource
     */
    public void setTargetResource(String targetResource) {
        this.targetResource = targetResource;
    }

    /**
     * Every externalized ressource has an id. A frame is a <code>DynamicCodeResource</code>.
     *
     * @return The id of this <code>DynamicCodeResource</code>
     */
    public String getTargetResource() {
        if (targetResource == null) {
            targetResource = getDynamicResource(DynamicCodeResource.class).getId();
        }
        return targetResource;
    }

    /**
     * Add an {@link Renderable}  into the header of the HTML page
     * @param m is typically a {@link org.wings.header.Link} or {@link DynamicResource}.
     * @see org.wings.header.Link
     * @see org.wings.script.DynamicScriptResource
     * @see DynamicCodeResource
     */
    public void addHeader(Object m) {
        if (!headers().contains(m))
            headers.add(m);
    }

    /** @see #addHeader(Object) */
    public void removeHeader(Object m) {
        headers.remove(m);
    }

    /**
     * Removes all headers. Be carful about what you do!
     * @see #addHeader(Object)
     */
    public void clearHeaders() {
        headers().clear();
    }

    /**
     * @see #addHeader(Object)
     */
    public List headers() {
        if (headers == null)
            headers = new ArrayList(2);
        return headers;
    }

    /**
     * Sets the title of this HTML page. Typically shown in the browsers window title.
     *
     * @param title The window title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Title of this HTML page. Typically shown in the browsers window title.
     *
     * @return Current page title
     */
    public String getTitle() {
        return title;
    }

    public void setStatusLine(String s) {
        statusLine = s;
    }

    public void show() {
        setVisible(true);
    }

    /**
     * Hides this frame. This means it gets removed at the session.
     * @see org.wings.session.Session#frames()
     */
    public void hide() {
        setVisible(false);
    }

    /**
     * Shows or hide this frame. This means it gets (un)registered at the session.
     * @see org.wings.session.Session#frames()
     */
    public void setVisible(boolean b) {
        if (b) {
            getSession().addFrame(this);
        } else {
            getSession().removeFrame(this);
        }
        super.setVisible(b);
    }

    public void propertyChange(PropertyChangeEvent pe) {
        if ("lookAndFeel".equals(pe.getPropertyName())) {
            updateComponentTreeCG(getContentPane());
        }
        if ("request.url".equals(pe.getPropertyName())) {
            setRequestURL((RequestURL) pe.getNewValue());
        }
    }

    private void updateComponentTreeCG(SComponent c) {
        c.updateCG();
        if (c instanceof SContainer) {
            SComponent[] children = ((SContainer) c).getComponents();
            for (int i = 0; i < children.length; i++) {
                updateComponentTreeCG(children[i]);
            }
        }
        updateCG();
    }

    public void setCG(FrameCG cg) {
        super.setCG(cg);
    }

    public void invite(ComponentVisitor visitor)
            throws Exception {
        visitor.visit(this);
    }

    /*
     * This function is called by SComponent.requestFocus().
     * @param c the component which requests the focus.
     */
    public void setFocus(SComponent c) {
        focusComponent = c;
    }

    public SComponent getFocus() {
        return focusComponent;
    }

    public void processLowLevelEvent(String name, String[] values) {
        if (values.length == 1) {
            String eventId = values[0];
            eventId = eventId.substring("focus_".length());
            System.out.println("eventId = " + eventId);
            SComponent component = (SComponent) getDispatcher().getLowLevelEventListener(eventId);
            System.out.println("component = " + component);
            component.requestFocus();
        }
    }

    public void fireIntermediateEvents() {
    }

    public boolean checkEpoch() {
        return true;
    }
}
