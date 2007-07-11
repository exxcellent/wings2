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

import org.wings.externalizer.ImageExternalizer;
import org.wings.session.SessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.IndexColorModel;
import java.awt.image.PixelGrabber;

/**
 * SIcon implementation that is based on {@link ImageIcon}.
 */
public class SImageIcon extends SAbstractIcon {
    private final static Log log = LogFactory.getLog(SImageIcon.class);

    private final ImageIcon img;
    private final SimpleURL url;

    public SImageIcon(ImageIcon image) {
        this (image, determineMimeType(image));
    }

    public SImageIcon(ImageIcon image, String mimeType) {
        if (image == null)
            throw new IllegalArgumentException("SImageIcon needs an Argument that's not null");

        img = image;
        url = new SimpleURL(SessionManager.getSession().getExternalizeManager().externalize(image, mimeType));
        setIconWidth(image.getIconWidth());
        setIconHeight(image.getIconHeight());
    }

    private static String extractMimeTypeFromPath(String description) {
        if (description == null)
            return null;
        String[] supported = ImageExternalizer.SUPPORTED_FORMATS;
        for (int i = 0; i < supported.length; i++) {
            if (description.endsWith(supported[i])) return "image/" + supported[i]; 
        }
        // special case jpg for jpeg
        if (description.endsWith("jpg")) return "image/jpeg";
        else return null;
    }

    public SImageIcon(java.awt.Image image) {
        this(new ImageIcon(image));
    }

    public SImageIcon(java.awt.Image image, String mimeType) {
        this(new ImageIcon(image), mimeType);
    }

    public SImageIcon(String name) {
        this(new ImageIcon(name));
    }

    public SImageIcon(String name, String mimeType) {
        this(new ImageIcon(name), mimeType);
    }

    /**
     * returns the URL, the icon can be fetched from. This URL may
     * be relative, usually if generated from the externalizer.
     */
    public SimpleURL getURL() {
        RequestURL requestURL = (RequestURL)SessionManager.getSession().getProperty("request.url");
        if (requestURL != null) {
            requestURL = (RequestURL)requestURL.clone();
            requestURL.setResource(url.toString());
            return requestURL;
        }
        else
            return url;
    }

    // get the image e.g. if you want to grey it out
    public java.awt.Image getImage() {
        return img.getImage();
    }

    protected static String determineMimeType(ImageIcon imageIcon) {
        String mimeType = extractMimeTypeFromPath(imageIcon.getDescription());
        if (mimeType != null)
            return mimeType;
        PixelGrabber pg = new PixelGrabber(imageIcon.getImage(), 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            log.warn("interrupted waiting for pixels!");
        }

        mimeType = "image/";
        if (!(pg.getColorModel() instanceof IndexColorModel))
            mimeType += ImageExternalizer.FORMAT_PNG;
        else
            mimeType += ImageExternalizer.FORMAT_GIF;

        return mimeType;
    }
}
