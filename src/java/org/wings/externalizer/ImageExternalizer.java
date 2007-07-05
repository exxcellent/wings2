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
package org.wings.externalizer;

import Acme.JPM.Encoders.GifEncoder;
import Acme.JPM.Encoders.JpegEncoder;

import com.keypoint.PngEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.io.Device;
import org.wings.io.DeviceOutputStream;
import org.wings.resource.HttpHeader;

import java.awt.*;
import java.util.Collection;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 */
public class ImageExternalizer implements Externalizer<Image> {

    private final static Log log = LogFactory.getLog(ImageExternalizer.class);

    public static final String FORMAT_PNG = "png";
    public static final String FORMAT_GIF = "gif";
    public static final String FORMAT_JPG = "jpeg";

    public static final String[] SUPPORTED_FORMATS = {FORMAT_PNG, FORMAT_GIF, FORMAT_JPG};
    private static final Class[] SUPPORTED_CLASSES = {Image.class};

    public static final ImageExternalizer SHARED_GIF_INSTANCE = new ImageExternalizer(FORMAT_GIF);
    public static final ImageExternalizer SHARED_PNG_INSTANCE = new ImageExternalizer(FORMAT_PNG);
    public static final ImageExternalizer SHARED_JPG_INSTANCE = new ImageExternalizer(FORMAT_JPG);

    protected String format;

    protected final String[] supportedMimeTypes = new String[1];

    public ImageExternalizer() {
        this(FORMAT_PNG);
    }

    public ImageExternalizer(String format) {
        this.format = format;
        checkFormat();

        supportedMimeTypes[0] = getMimeType(null);
    }

    protected void checkFormat() {
        for (String aSUPPORTED_FORMATS : SUPPORTED_FORMATS) {
            if (aSUPPORTED_FORMATS.equals(format)) {
                return;
            }
        }
        throw new IllegalArgumentException("Unsupported Format " + format);
    }

    public String getId(Image obj) {
        return null;
    }

    public String getExtension(Image obj) {
        return format;
    }

    public String getMimeType(Image obj) {
        return "image/" + format;
    }

    public int getLength(Image obj) {
        return -1;
    }

    public boolean isFinal(Image obj) {
        return false; // images may dynamically change (i.e. status charts)
                      // I guess the static case should cache the encoding results rather
                      // then rerunning it
    }

    public Class[] getSupportedClasses() {
        return SUPPORTED_CLASSES;
    }

    public String[] getSupportedMimeTypes() {
        return supportedMimeTypes;
    }

    public void write(Object obj, Device out)
            throws java.io.IOException {
        if (FORMAT_PNG.equals(format))
            writePNG((Image) obj, out);
        else if (FORMAT_JPG.equals(format))
            writeJPG((Image) obj, out);
        else
            writeGIF((Image) obj, out);
    }

    /**
     * writes a image as gif to the OutputStream
     */
    public void writeGIF(Image img, Device out)
            throws java.io.IOException {
        GifEncoder encoder = new GifEncoder(img, new DeviceOutputStream(out),
                true);
        encoder.encode();
    }

    /**
     * writes a image as jpeg to the OutputStream
     */
    public void writeJPG(Image img, Device out)
            throws java.io.IOException {
        JpegEncoder encoder = new JpegEncoder(img, new DeviceOutputStream(out));
        encoder.encode();
    }

    /**
     * writes a image as png to the OutputStream
     */
    public void writePNG(Image img, Device out)
            throws java.io.IOException {
        PngEncoder png = new PngEncoder(img, PngEncoder.ENCODE_ALPHA,
                PngEncoder.FILTER_NONE, 6);
        byte[] pngbytes = png.pngEncode();
        if (pngbytes == null) {
            log.fatal("null image");
        } else {
            out.write(pngbytes);
        }
        out.flush();
    }

    public Collection<HttpHeader> getHeaders(Image obj) {
        return null;
    }
}


