package org.wings;

/**
 * 
 * @author armin
 * created at 02.03.2004 10:34:20
 * @deprecated use {@link SResourceIcon} instead
 */
public class ResourceImageIcon extends SResourceIcon {

     public ResourceImageIcon(String resourceFileName) {
         super(resourceFileName);
    }

    public ResourceImageIcon(ClassLoader classLoader, String resourceFileName) {
        super(classLoader, resourceFileName);
    }
}
