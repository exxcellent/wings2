/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
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
import org.wings.script.JavaScriptListener;
import org.wings.session.SessionManager;
import org.wings.style.StyleSheet;
import org.wings.util.ComponentVisitor;
import org.wings.resource.DynamicCodeResource;
import org.wings.resource.DynamicResource;

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
    implements PropertyChangeListener {

    /**
     *  The Title of the Frame.
     */
    protected String title;

    protected String baseTarget = null;

    /**
     * A Set containing additional tags for the html header.
     */
    protected List headers;

    protected boolean resizable = true;

    /** the style sheet used in certain look and feels. */
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

    private JavaScriptListener focus = null;          //Listener which sets the focus onload

    public SFrame() {
        getSession().addPropertyChangeListener("lookAndFeel", this);
        getSession().addPropertyChangeListener("request.url", this);
    }

    public SFrame(String title) {
        this();
        setTitle(title);
    }

    public void addDynamicResource(DynamicResource d) {
        if (dynamicResources == null) {
            dynamicResources = new HashMap();
        }
        dynamicResources.put(d.getClass(), d);
    }

    /**
     * Removes the instance of the dynamic ressource of the given class.
     */
    public void removeDynamicResource(Class dynamicResourceClass) {
        if (dynamicResources != null) {
            dynamicResources.remove(dynamicResourceClass);
        }
    }

    public DynamicResource getDynamicResource(Class c) {
        if (dynamicResources == null) {
            dynamicResources = new HashMap();
        }
        return (DynamicResource) dynamicResources.get(c);
    }

    public void setResizable(boolean b) {
        resizable = b;
    }

    public boolean isResizable() {
        return resizable;
    }

    /**
     * Return <code>this</code>.
     *
     * @return this.
     */
    public SFrame getParentFrame() {
        return this;
    }

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

    public String getTargetResource() {
        if (targetResource == null) {
            targetResource = getDynamicResource(DynamicCodeResource.class).getId();
        }
        return targetResource;
    }

    /**
     * Set the base target. This is the target of any link pressed.
     */
    public void setBaseTarget(String baseTarget) {
        this.baseTarget = baseTarget;
    }

    /**
     * set the base target frame. This frame will receive all klicks
     * in this frame. Usually you want to use this for the ReloadManager
     * frame.
     */
    public void setBaseTarget(SFrame otherFrame) {
        /*
         * this knows, that the frames are usually named "frame" + ID
         */
        setBaseTarget("frame" + otherFrame.getName());
    }

    public String getBaseTarget() {
        return baseTarget;
    }

    /**
     *
     * @param m is typically a {@link Renderable}.
     */
    public void addHeader(Object m) {
        if (!headers().contains(m))
            headers.add(m);
    }

    public void removeHeader(Object m) {
        headers.remove(m);
    }

    public void clearHeaders() {
        headers().clear();
    }

    public List headers() {
        if (headers == null)
            headers = new ArrayList(2);
        return headers;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setStatusLine(String s) {
        statusLine = s;
    }

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

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
}
