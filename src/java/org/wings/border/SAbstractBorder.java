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

package org.wings.border;

import java.awt.Insets;
import java.awt.Color;
import java.io.IOException;

import org.wings.io.Device;
import org.wings.plaf.*;
import org.wings.session.*;

/**
 * This is a an abstract implementation of the <code>SBorder</code>
 * interface.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public abstract class SAbstractBorder
    implements SBorder
{
    /**
     * The code generation delegate, which is responsible for
     * the visual representation of a border.
     */
    protected transient BorderCG cg;

    /**
     * the insets
     */
    private Insets insets;

    /**
     * border color
     */
    private Color color;

    /**
     * border thickness
     */
    private int thickness;

    /**
     * constructor
     */
    public SAbstractBorder() {
        this(Color.black, 1, new Insets(0, 0, 0, 0));
    }

    /**
     * constructor
     */
    public SAbstractBorder(Color c, int thickness, Insets insets) {
        setInsets(insets);
        setColor(c);
        setThickness(thickness);
	updateCG();
    }

    /**
     * constructor
     */
    public SAbstractBorder(Insets insets) {
	this(Color.black, 1, insets);
    }

    /**
     * constructor
     */
    public SAbstractBorder(Color c) {
	this(c, 1, new Insets(0, 0, 0, 0));
    }

    /**
     * constructor
     */
    public SAbstractBorder(int thickness) {
	this(Color.black, thickness, new Insets(0, 0, 0, 0));
    }

    /**
     * Sets the look and feel delegate.
     *
     * @param newGC the new componet's cg
     */
    protected void setCG(BorderCG newCG) {
        cg = newCG;
    }

    /**
     * Return the look and feel delegate.
     *
     * @return the componet's cg
     */
    public BorderCG getCG() {
        return cg;
    }

    public void updateCG() {
        setCG((BorderCG)SessionManager.getSession().getCGManager().getCG(this));
    }

    /**
     * set the insets of the border
     */
    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    /**
     * @return the insets of the border
     */
    public final Insets getInsets() { return insets; }

    public void writePrefix(Device d) throws IOException {
	cg.writePrefix(d, this);
    }

    public void writePostfix(Device d) throws IOException {
	cg.writePostfix(d, this);
    }

    public void writeSpanAttributes(Device d) throws IOException {
        cg.writeSpanAttributes( d, this );
    }

    public String getSpanAttributes() {
      return cg.getSpanAttributes(this);
    }
    
    /**
     * sets the foreground color of the border
     * @param color
     */
    public Color getColor() {
        return color;
    }

    /**
     * sets the foreground color of the border
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * set the thickness of the border 
     * thickness must be > 0
     *
     * @param thickness
     */
    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    /**
     * @return thickness in pixels
     */
    public final int getThickness() { return thickness; }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
