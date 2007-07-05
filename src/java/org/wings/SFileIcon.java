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

import org.wings.resource.FileResource;
import org.wings.util.ImageInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * SIcon implementation that is externalized globally
 * and is not bound to a session.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SFileIcon extends FileResource implements SIcon {

    /**
     * The width to display the icon. This overwrites the real width of the icon. Ignored, if &lt;0
     */
    private int width = -1;

    /**
     * The height to display the icon. This overwrites the real height of the icon. Ignored, if &lt;0
     */
    private int height = -1;

    /**
     * Title of icon, <code>""</code> if not set.
     */
    private String title = null;


    /**
     * Create a new SFileIcon from the File. This constructor extracts
     * the extension from the file to be appended to the externalized resource
     * name.
     */
    public SFileIcon(String fileName) throws FileNotFoundException {
        this(new File(fileName));
    }

    /**
     * crates a new SFileIcon from the given file. The extension and
     * mimetype are taken from the parameters given.
     *
     * @param file      the file to construct a SFileIcon from
     * @param extension user provided extension. The original extension of
     *                  the file is ignored, unless this paramter is
     *                  'null'.
     * @param mimetype  the user provided mimetype. If this is 'null', then
     *                  the mimetype is guessed from the extension.
     */
    public SFileIcon(File file, String extension, String mimetype) throws FileNotFoundException {
        super(file, extension, mimetype);

        ImageInfo tImageInfo = new ImageInfo();
        FileInputStream tImageInput = new FileInputStream(file);
        tImageInfo.setInput(tImageInput);

        if (tImageInfo.check()) {
            // if either of the extension or mimetype is missing, try to guess it.
            if (this.mimeType == null || this.mimeType.length() == 0) {
                this.mimeType = tImageInfo.getMimeType();
            } else if (this.extension == null || this.extension.length() == 0) {
                this.extension = tImageInfo.getFormatName();
            }

            width = tImageInfo.getWidth();
            height = tImageInfo.getHeight();
        }

        try {
            tImageInput.close();
        } catch (IOException ex) {
            // ignore close exception, we don't need it anymore
        }
    }

    public SFileIcon(File file) throws FileNotFoundException {
        this(file, null, null);
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

    public void setIconWidth(int width) {
        this.width = width;
    }

    public void setIconHeight(int height) {
        this.height = height;
    }

    public String getIconTitle() {
        return (title!=null) ? title : "";
    }
    
    public void setIconTitle(String title) {
        this.title = title;
    }
}


