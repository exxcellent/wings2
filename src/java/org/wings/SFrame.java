/*
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.event.InvalidLowLevelEvent;
import org.wings.event.SInvalidLowLevelEventListener;
import org.wings.event.SRenderListener;
import org.wings.event.SRequestListener;
import org.wings.event.SRequestEvent;
import org.wings.io.Device;
import org.wings.plaf.FrameCG;
import org.wings.resource.DynamicCodeResource;
import org.wings.resource.DynamicResource;
import org.wings.style.StyleSheet;
import org.wings.util.ComponentVisitor;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.*;

/**
 * The root component of every component hierarchy.
 * <p/>
 * A SessionServlet requires an instance of SFrame to render the page.
 * SFrame consists of some header informaton (meta, link, script)
 * and a stack of components. The bottommost component of the stack is always
 * the contentPane. When dialogs are to be shown, they are stacked on top of
 * it.
 *
 * @author Holger Engels,
 *         <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SFrame
        extends SRootContainer
        implements PropertyChangeListener, LowLevelEventListener {

    private final transient static Log log = LogFactory.getLog(SFrame.class);

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
     * @see #setBackButton(SButton)
     */
    private SButton backButton;

    /*
     * see fireDefaultBackButton()
     */
    private LastRequestListener backbuttonRequestListener;

    /**
     * @see #setNoCaching(boolean)
     */
    private boolean noCaching = true;

    /**
     * For performance reasons.
     *
     * @see #fireInvalidLowLevelEventListener
     */
    private boolean fireInvalidLowLevelEvents = false;

    /**
     * we need to overwrite the inherited input map from SComponent.
     */
    private InputMap myInputMap;

    /**
     * Global input maps
     */
    private ArrayList globalInputMapComponents;

    /**
     * Creates a new SFrame
     */
    public SFrame() {
        getSession().addPropertyChangeListener("lookAndFeel", this);
        getSession().addPropertyChangeListener("request.url", this);
        this.visible = false; // Frames are invisible originally.
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
     *
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
     *
     * @param dynamicResourceClass Class of dynamic ressource to remove
     * @see #getDynamicResource(Class)
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
     * </ul>
     */
    public DynamicResource getDynamicResource(Class c) {
        if (dynamicResources == null) {
            dynamicResources = new HashMap();
        }
        return (DynamicResource) dynamicResources.get(c);
    }

    /**
     * @return all dynamic script resources
     */
    public Collection getDynamicResources() {
        if (dynamicResources == null) {
            dynamicResources = new HashMap();
        }
        return dynamicResources.values();
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
            requestURL = (RequestURL) getSession().getProperty("request.url");
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
     *
     * @param headerElement is typically a {@link org.wings.header.Link} or {@link DynamicResource}.
     * @see org.wings.header.Link
     */
    public void addHeader(Object headerElement) {
        if (!headers().contains(headerElement) && headerElement != null) {
            headers.add(headerElement);
        }
    }

    /**
     * Add an {@link Renderable} into the header of the HTML page at the desired index position
     *
     * @param headerElement is typically a {@link org.wings.header.Link} or {@link DynamicResource}.
     * @param index index in header list to add this item
     * @see org.wings.header.Link
     * @see #headers()
     */
    public void addHeader(int index, Object headerElement) {
        if (!headers().contains(headerElement) && headerElement != null) {
            headers.add(index, headerElement);
        }
    }

    /**
     * @see #addHeader(Object)
     * @return <tt>true</tt> if this frame contained the specified header element.
     */
    public boolean removeHeader(Object m) {
        return headers.remove(m);
    }

    /**
     * Removes all headers. Be carful about what you do!
     *
     * @see #addHeader(Object)
     */
    public void clearHeaders() {
        headers().clear();
    }

    /**
     * @see #addHeader(Object)
     */
    public List headers() {
        if (headers == null) {
            headers = new ArrayList(2);
        }
        return Collections.unmodifiableList(headers);
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

    /**
     * @return <code>true</code> if the generated HTML code of this frame/page should
     *         not be cached by browser, <code>false</code> if no according HTTP/HTML headers
     *         should be rendered
     * @see #setNoCaching(boolean)
     */
    public boolean isNoCaching() {
        return noCaching;
    }

    public void write(Device s) throws IOException {
        if (isNoCaching()) {
            reload(ReloadManager.STATE); // invalidate frame on each rendering!
        }
        super.write(s);
    }

    /**
     * Typically you don't want any wings application to operate on old 'views' meaning
     * old pages. Hence all generated HTML pages (<code>SFrame</code> objects
     * rendered through {@link DynamicCodeResource} are marked as <b>do not cache</b>
     * inside the HTTP response header and the generated HTML frame code.
     * <p>If for any purpose (i.e. you a writing a read only application) you want
     * th user to be able to work on old views then set this to <code>false</code>
     * and Mark the according <code>SComponent</code>s to be not epoch checked
     * (i.e. {@link SAbstractButton#setEpochCheckEnabled(boolean)})
     *
     * @param noCaching The noCaching to set.
     * @see LowLevelEventListener#isEpochCheckEnabled()
     * @see org.wings.session.LowLevelEventDispatcher
     */
    public void setNoCaching(boolean noCaching) {
        this.noCaching = noCaching;
    }

    /**
     * Shows this frame. This means it gets registered at the session.
     *
     * @see org.wings.session.Session#getFrames()
     */
    public void show() {
        setVisible(true);
    }

    /**
     * Hides this frame. This means it gets removed at the session.
     *
     * @see org.wings.session.Session#getFrames()
     */
    public void hide() {
        setVisible(false);
    }

    /**
     * Shows or hide this frame. This means it gets (un)registered at the session.
     *
     * @see org.wings.session.Session#getFrames()
     */
    public void setVisible(boolean visible) {
        if (visible != isVisible()) {
            if (visible) {
                getSession().addFrame(this);
                register();
            } else {
                getSession().removeFrame(this);
                unregister();
            }
            super.setVisible(visible);
        }
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

    /**
     * Choose which component rendered inside this frame should gain the edit focus on next rendering
     * This function is called by {@link SComponent#requestFocus()}
     *
     * @param focusOnComponent the component which requests the focus.
     */
    public void setFocus(SComponent focusOnComponent) {
        focusComponent = focusOnComponent;
    }

    /**
     * @see #setFocus(SComponent)
     */
    public SComponent getFocus() {
        return focusComponent;
    }

    public void processLowLevelEvent(String name, String[] values) {
        focusComponent = null;
        if (values.length == 1 && name.endsWith("_focus")) {
            String eventId = values[0];
            List listeners = getSession().getDispatcher().getLowLevelEventListener(eventId);
            for (int i = 0; i < listeners.size() && focusComponent == null; i++) {
                Object listener = listeners.get(i);
                if (listener instanceof SComponent) {
                    focusComponent = (SComponent) listener;
                }
            }
        }
    }

    /**
     * Registers an {@link SInvalidLowLevelEventListener} in this frame.
     *
     * @param l The listener to notify about outdated reqests
     * @see org.wings.event.InvalidLowLevelEvent
     */
    public final void addInvalidLowLevelEventListener(SInvalidLowLevelEventListener l) {
        addEventListener(SInvalidLowLevelEventListener.class, l);
        fireInvalidLowLevelEvents = true;
    }

    /**
     * Removes the passed {@link SInvalidLowLevelEventListener} from this frame.
     *
     * @param l The listener to remove
     * @see org.wings.event.InvalidLowLevelEvent
     */

    public final void removeDispatchListener(SInvalidLowLevelEventListener l) {
        removeEventListener(SRenderListener.class, l);
    }

    /**
     * Notify all {@link SInvalidLowLevelEventListener} about an outdated request
     * on the passed component
     *
     * @param source The <code>SComponent</code> received an outdated event
     * @see org.wings.session.LowLevelEventDispatcher
     */
    public final void fireInvalidLowLevelEventListener(LowLevelEventListener source) {
        if (fireInvalidLowLevelEvents) {
            Object[] listeners = getListenerList();
            InvalidLowLevelEvent e = null;
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == SInvalidLowLevelEventListener.class) {
                    // Lazily create the event:
                    if (e == null) {
                        e = new InvalidLowLevelEvent(source);
                    }
                    ((SInvalidLowLevelEventListener) listeners[i + 1]).invalidLowLevelEvent(e);
                }
            }
        }
        fireDefaultBackButton();
    }


    /**
     * A button activated on detected browser back clicks.
     *
     * @return Returns the backButton.
     * @see #setBackButton(SButton)
     */
    public SButton getBackButton() {
        return backButton;
    }

    /**
     * This button allows you to programattically react on Back buttons pressed in the browser itselfs.
     * This is a convenience method in contrast to {@link #addInvalidLowLevelEventListener(SInvalidLowLevelEventListener)}.
     * While the listener throws an event on every detected component receiving an invalid
     * request, this button is only activated if
     * <ul>
     * <li>Maximum once per request
     * <li>Only if some time passed by to avoid double-clicks to be recognized as back button clicks.
     * </ul>
     * <b>Note:</b> To work correctly you should set use GET posting
     * {@link SForm#setPostMethod(boolean)} and use {@link SFrame#setNoCaching(boolean)} for
     * no caching. This will advise the browser to reload every back page.
     *
     * @param defaultBackButton A button to trigger upon detected invalid epochs.
     */
    public void setBackButton(SButton defaultBackButton) {
        if (backbuttonRequestListener == null) {
            backbuttonRequestListener  = new LastRequestListener();
            getSession().addRequestListener(backbuttonRequestListener);
        }
        this.backButton = defaultBackButton;
    }

    /**
     * Fire back button only once and if some time already passed by to avoid double clicks.
     */
    private void fireDefaultBackButton() {
        final int DISPATCHINGFINISH_BACKBUTTON_DEADTIME = 500;
        if (this.backButton != null && this.backbuttonRequestListener != null) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - backbuttonRequestListener.lastDispatchingFinished > DISPATCHINGFINISH_BACKBUTTON_DEADTIME) {
                // Simulate a button press
                backButton.processLowLevelEvent(null, new String[]{"1"});
            }
        }
    }

    public void fireIntermediateEvents() {
    }

    /**
     * @see LowLevelEventListener#isEpochCheckEnabled()
     */
    private boolean epochCheckEnabled = true;

    /**
     * @see LowLevelEventListener#isEpochCheckEnabled()
     */
    public boolean isEpochCheckEnabled() {
        return epochCheckEnabled;
    }

    /**
     * @see LowLevelEventListener#isEpochCheckEnabled()
     */
    public void setEpochCheckEnabled(boolean epochCheckEnabled) {
        this.epochCheckEnabled = epochCheckEnabled;
    }

    /**
     * custom error handling. If you want to catch application errors,
     * return true here. 
     * @param e The throwable causing this.
     * @return does this frame handle errors...
     */
    public boolean handleError(Throwable e) {
        return false;
    }

    public InputMap getInputMap(int condition) {
        // SFrame has only one inputMap
        if (myInputMap == null) {
            myInputMap = new InputMap();
        }
        return myInputMap;
    }

    public void setInputMap(int condition, InputMap inputMap) {
        this.myInputMap = inputMap;
    }

    public void registerGlobalInputMapComponent(SComponent comp) {
        if (globalInputMapComponents == null) {
            globalInputMapComponents = new ArrayList();
        }
        if (!globalInputMapComponents.contains(comp)) {
            // not yet registered
            globalInputMapComponents.add(comp);
        }
    }

    public void deregisterGlobalInputMapComponent(SComponent comp) {
        if (globalInputMapComponents != null) {
            globalInputMapComponents.remove(comp);
        }
    }


    public List getGlobalInputMapComponents() {
        return globalInputMapComponents;
    }

    /**
     * Private helper class to remember the end of the last dispatch.
     * @see org.wings.SFrame#fireDefaultBackButton()
     */
    private static class LastRequestListener implements SRequestListener {
        private long lastDispatchingFinished = -1;

        public void processRequest(SRequestEvent e) {
            if (e.getType() == SRequestEvent.DISPATCH_DONE)
                lastDispatchingFinished = System.currentTimeMillis();
        }
    }
}
