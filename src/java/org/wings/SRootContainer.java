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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A root container.
 * The classes derived from this class ({@link SFrame} and
 * {@link SInternalFrame}) render in the content pane of this RootContainer.
 *
 * <p>The RootContainer has a stack of components. Ususally, the stack
 * contains only <em>one</em> element, the content pane; this is the bottom-most
 * component. When dialogs are added to the RootContainer, then these dialogs
 * are stacked on top of this content pane and only <em>that</em> dialog is
 * visible then. This emulates the behaviour of modal dialogs in a windowing
 * system.
 *
 * @author Holger Engels
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @author <a href="mailto:Haaf@mercatis.de">Armin Haaf</a>
 */
public abstract class SRootContainer extends SContainer {
    private final static Log log = LogFactory.getLog(SRootContainer.class);

    /**
     * The container for the contentPane.
     */
    protected SContainer contentPane;

    /**
     * default constructor initializes the stack layout system of this
     * SRootContainer.
     */
    public SRootContainer() {
        super(new SRootLayout());
        setContentPane(new SPanel(new SBorderLayout()));
    }

    /**
     * Push a new dialog on top of the stack. If this RootContainer is
     * rendered, then only this dialog is shown.
     *
     * @param dialog the SDialog that is to be shown on top.
     */
    public void pushDialog(SDialog dialog) {
        log.debug("push dialog = " + dialog.getName());
        super.addComponent(dialog, null, getComponentCount());
        dialog.setFrame(this);
        reload();
    }

    /**
     * remove the dialog, that is on top of the stack.
     *
     * @return the dialog, that is popped from the stack.
     */
    public SDialog popDialog() {
        int count = getComponentCount();
        if (count <= 1)
            throw new IllegalStateException("there's no dialog left!");

        SDialog dialog = (SDialog) getComponent(count - 1);
        super.remove(dialog);
        dialog.setFrame(null);

        reload();
        log.debug("pop dialog = " + dialog.getName());
        return dialog;
    }

    public void removeDialog(SDialog dialog) {
        super.remove(dialog);
        dialog.setFrame((SFrame) null);
        reload();
    }

    /**
     * @return the number of dialogs that are on the stack currently.
     */
    public int getDialogCount() {
        return getComponentCount() - 1;
    }

    /**
     * returns the content pane of this RootContainer.
     */
    public SContainer getContentPane() {
        return contentPane;
    }

    /**
     * Sets the container for the contentPane.
     *
     * @param container the container for the contentPane.
     */
    public void setContentPane(SContainer container) {
        if (this.contentPane != null) {
            super.remove(contentPane);
        }
        this.contentPane = container;
        super.addComponent(this.contentPane, null, 0);
    }

    /**
     * Use getContentPane().addComponent(c) instead.
     */
    public SComponent addComponent(SComponent c, Object constraint, int index) {
        throw new IllegalArgumentException("use getContentPane().addComponent()");
    }

    /**
     * Use getContentPane().removeComponent(c) instead.
     */
    public void remove(SComponent c) {
        throw new IllegalArgumentException("use getContentPane().removeComponent()");
    }

    // allow frame.setBackground(Color.yellow);
    //    public void setAttribute(Selector selector, CSSProperty property, String propertyValue) {
    //        throw new IllegalArgumentException("use getContentPane().setAttribute()");
    //    }
}
