/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package wingset;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.wings.SFileIcon;
import org.wings.SIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * FaceGenerator.java
 * <p/>
 * <p/>
 * Created: Mon Mar 18 15:36:32 2002
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class FaceGenerator {

    int width = 340;

    int height = 450;

    /**
     *
     */
    public FaceGenerator() {

    }


    Faces.Face generateFace(SIcon f, File dir, String name) throws IOException {
        ImageIcon i = new ImageIcon(f.getURL().toString());
        return generateFace(i, dir, name);
    }

    Faces.Face generateFace(ImageIcon f, File dir, String name) throws IOException {
        int width = f.getIconWidth();
        int height = f.getIconHeight();

        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D biContext = bi.createGraphics();
        biContext.drawImage(f.getImage(), 0, 0, null);

        return generateFace(bi, dir, name);
    }

    Faces.Face generateFace(BufferedImage f, File dir, String name) throws IOException {
        Faces.Face result = new Faces.Face();


        AffineTransformOp scaler =
                new AffineTransformOp(AffineTransform.getScaleInstance(width / (double) f.getWidth(),
                        height / (double) f.getHeight()),
                        null);

        f = scaler.filter(f, null);

        result.hair =
                new SFileIcon(storeImage(f.getSubimage(0, 0, width, height / 3),
                        dir,
                        name + "_hair"));

        result.eyes =
                new SFileIcon(storeImage(f.getSubimage(0, height / 3, width, height / 3),
                        dir,
                        name + "_eyes"));

        result.mouth =
                new SFileIcon(storeImage(f.getSubimage(0, 2 * height / 3, width, height / 3),
                        dir,
                        name + "_mouth"));

        return result;
    }

    public File storeImage(BufferedImage image, File dir, String name) throws IOException {

        if (!dir.exists()) {
            throw new IOException("Directory " + dir + " does not exist, cannot write file");
        }

        if (!dir.canWrite()) {
            throw new IOException("Directory " + dir + " is not writable, cannot write file");
        }

        FileOutputStream out = null;

        File file = new File(dir, name + ".jpeg");

        out = new FileOutputStream(file);
        JPEGImageEncoder encoder =
                JPEGCodec.createJPEGEncoder(out);
        encoder.encode(image);

        out.close();

        return file;
    }

    public static void main(String[] args) throws IOException {

        FaceGenerator gen = new FaceGenerator();


        gen.generateFace(new ImageIcon(args[0]), new File(args[1]), args[2]);
    }

}// FaceGenerator

/*
   $Log$
   Revision 1.2  2004/11/24 18:14:56  blueshift
   TOTAL CLEANUP:
   - removed document me TODOs
   - updated/added java file headers
   - removed emacs stuff
   - removed deprecated methods

   Revision 1.1.1.1  2004/10/04 16:12:58  hengels
   o start development of wings 2

   Revision 1.4  2004/03/02 16:20:40  arminhaaf
   o rename SFileImageIcon to SFileIcon

   Revision 1.3  2004/01/16 13:34:20  arminhaaf
   o use SFileIcon

   Revision 1.2  2002/05/15 12:19:09  hzeller
   o move pre1.0 branch to main

   Revision 1.1.2.1  2002/03/24 15:36:06  ahaaf
   Simple application to generate faces for the faces example

*/
