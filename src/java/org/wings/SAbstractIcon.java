package org.wings;

/**
 * SAbstractIcon.java
 *
 *
 * Created: Tue Nov 19 09:17:25 2002
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class SAbstractIcon implements SIcon {
    
    /**
     * The width of the icon. This is the width it is rendered, not
     * the real width of the icon. A value <0 means, no width is rendered
     */
    protected int width = -1;

    /**
     * The height of the icon. This is the height it is rendered, not
     * the real width of the icon. A value <0 means, no height is rendered
     */
    protected int height = -1;

    /**
     * 
     */
    protected SAbstractIcon() {
    }

    /**
     * 
     */
    protected SAbstractIcon(int width, int height) {
        setIconWidth(width);
        setIconHeight(height);
    }

    public int getIconWidth() {
        return width;
    }
  
    public int getIconHeight() {
        return height;
    }

    public void setIconWidth(int w) {
        width = w;
    }
  
    public void setIconHeight(int h) {
        height = h;
    }

    
}// SAbstractIcon

/*
   $Log$
   Revision 1.1  2004/10/04 16:13:06  hengels
   Initial revision

   Revision 1.1  2002/11/19 14:57:47  ahaaf
   o make icon dimensions modifiable (they are used now as render dimension)

*/
