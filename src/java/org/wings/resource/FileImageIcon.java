package org.wings.resource;

import org.wings.SFileIcon;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 
 * @author armin
 * created at 06.01.2004 13:32:57
 * @deprecated use {@link org.wings.SFileIcon} instead
 */
public class FileImageIcon extends SFileIcon {
    public FileImageIcon(String name) throws FileNotFoundException {
        super(name);
    }

    public FileImageIcon(File file) throws FileNotFoundException {
        super(file);
    }

    public FileImageIcon(File file, String ext, String mt) throws FileNotFoundException {
        super(file, ext, mt);
    }
}
