/*
 * Copyright 2000,2006 wingS development team.
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

import org.wings.plaf.css.PopupCG;
import org.wings.session.SessionManager;

/**
 * Popups are used to display a <code>SComponent</code> to the user, typically
 * on top of all the other <code>SComponent</code>s in a particular containment
 * hierarchy.
 * <p>
 * The general contract is that if you need to change the size of the
 * <code>SComponent</code>, or location of the <code>SPopup</code>, you should
 * obtain a new <code>Popup</code>.
 * <p>
 * <code>SPopup</code> does not descend from <code>SComponent</code>, rather
 * implementations of <code>SPopup</code> are responsible for creating
 * and maintaining their own <code>SComponent</code>s to render the
 * requested <code>SComponent</code> to the user.
 *
 * @author Christian Schyma
 */
public class SPopup extends SComponent {

    /**
     * The SComponent representing the Popup.
     */
    private SComponent contents;

    /**
     * Owner Frame.
     */
    private SComponent owner;

    /**
     * popup x position, origin is the top left of the browser viewport
     */
    private int x;

    /**
     * popup y position, origin is the top left of the browser viewport
     */
    private int y;

    /**
     * width of the popup which will be retrieved by using
     * contents.getPreferredSize()
     */
    private int width;

    /**
     * height of the popup which will be retrieved by using
     * contents.getPreferredSize()
     */
    private int height;

    // TODO: use constants from Swing
    public static final String TOP_RIGHT    = "tr";
    public static final String TOP_LEFT     = "tl";
    public static final String BOTTOM_RIGHT = "br";
    public static final String BOTTOM_LEFT  = "bl";

    private boolean anchored = false;
    private SComponent context;
    private String contentsCorner;
    private String contextCorner;

    /**
     * Creates a <code>SPopup</code>.
     * <code>x</code> and <code>y</code> specify the preferred initial location
     * to place the <code>SPopup</code> at. Based on screen size, or other
     * paramaters, the <code>SPopup</code> may not display at <code>x</code> and
     * <code>y</code>.
     * <p>
     * N.B.: check that the contents SComponent has a valid preferred size!
     * (<code>getPreferredSize()</code>)
     * <p>
     * @param owner    owner of component SPopup, if null the root frame will
     *                 be used
     * @param contents Contents of the Popup
     * @param x        Initial x screen coordinate
     * @param y        Initial y screen coordinate
     * @exception IllegalArgumentException if contents is null
     */
    public SPopup(SComponent owner, SComponent contents, int x, int y) {
        if (contents == null) {
            throw new IllegalArgumentException("Contents must be non-null");
        }

        if (owner == null) {
            this.owner = SessionManager.getSession().getRootFrame();;
        } else {
            this.owner = owner;
        }

        this.contents = contents;
        this.x        = x;
        this.y        = y;
        this.width    = contents.getPreferredSize().getWidthInt();
        this.height   = contents.getPreferredSize().getHeightInt();

        setCG(new PopupCG(this));
    }

    /**
     * Anchors the popup to the the given component <code>context</code>.
     * @param context        component to be anchored to
     * @param contentsCorner anchor point: e.g. SAnchoredPopup.TOP_LEFT
     * @param contextCorner  anchor point: e.g. SAnchoredPopup.BOTTOM_LEFT
     */
    public void setContext(SComponent context, String contentsCorner, String contextCorner) {
        this.anchored     = true;
        this.context        = context;
        this.contentsCorner = contentsCorner;
        this.contextCorner  = contextCorner;
        ((PopupCG)getCG()).attachJavaScript();
    }

    protected void finalize() throws Throwable {
        ((PopupCG)getCG()).tidyUp();
    }

    /**
     * Returns the <code>Component</code> returned from
     * <code>createComponent</code> that will hold the <code>Popup</code>.
     */
    public SComponent getComponent() {
        return this.contents;
    }

    public String getContentsCorner() {
        return contentsCorner;
    }

    public String getContextCorner() {
        return contextCorner;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public SComponent getOwner() {
        return owner;
    }

    public boolean isAnchored() {
        return anchored;
    }

    public SComponent getContext() {
        return context;
    }

}
