package org.wings.template;

import org.wings.SAbstractIconTextCompound;
import org.wings.SComponent;
import org.wings.SURLIcon;

/**
 * 
 * @author armin
 * created at 05.03.2004 10:24:07
 */
public class SAbstractIconTextCompoundPropertyManager  extends SComponentPropertyManager
{
    static final Class[] classes = {SAbstractIconTextCompound.class};

    public SAbstractIconTextCompoundPropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        SAbstractIconTextCompound c = (SAbstractIconTextCompound) comp;
        if ( name.equals("TEXT") )
            c.setText(value);
        else if ( name.startsWith("ICON") ) {
            if ( name.equals("ICON") )
                c.setIcon(new SURLIcon(value));
            else if ( name.equals("ICONWIDTH") ) {
                try {
                    int width = Integer.parseInt(value);
                    if ( c.getIcon()!=null ) {
                        c.getIcon().setIconWidth(width);
                    } // end of if ()
                    if ( c.getDisabledIcon()!=null ) {
                        c.getDisabledIcon().setIconWidth(width);
                    } // end of if ()
                    if ( c.getSelectedIcon()!=null ) {
                        c.getSelectedIcon().setIconWidth(width);
                    } // end of if ()
                    if ( c.getRolloverIcon()!=null ) {
                        c.getRolloverIcon().setIconWidth(width);
                    } // end of if ()
                    if ( c.getRolloverSelectedIcon()!=null ) {
                        c.getRolloverSelectedIcon().setIconWidth(width);
                    } // end of if ()
                    if ( c.getPressedIcon()!=null ) {
                        c.getPressedIcon().setIconWidth(width);
                    } // end of if ()
                } catch ( NumberFormatException ex ) {
                } // end of try-catch
            } else if ( name.equals("ICONHEIGHT") ) {
                try {
                    int height = Integer.parseInt(value);
                    if ( c.getIcon()!=null ) {
                        c.getIcon().setIconHeight(height);
                    } // end of if ()
                    if ( c.getDisabledIcon()!=null ) {
                        c.getDisabledIcon().setIconHeight(height);
                    } // end of if ()
                    if ( c.getSelectedIcon()!=null ) {
                        c.getSelectedIcon().setIconHeight(height);
                    } // end of if ()
                    if ( c.getRolloverIcon()!=null ) {
                        c.getRolloverIcon().setIconHeight(height);
                    } // end of if ()
                    if ( c.getRolloverSelectedIcon()!=null ) {
                        c.getRolloverSelectedIcon().setIconHeight(height);
                    } // end of if ()
                    if ( c.getPressedIcon()!=null ) {
                        c.getPressedIcon().setIconHeight(height);
                    } // end of if ()
                } catch ( NumberFormatException ex ) {
                } // end of try-catch
            }
        } else if ( name.equals("DISABLEDICON") )
            c.setDisabledIcon(new SURLIcon(value));
        else if ( name.equals("SELECTEDICON") )
            c.setSelectedIcon(new SURLIcon(value));
        else if ( name.equals("ROLLOVERSELECTEDICON") )
            c.setRolloverSelectedIcon(new SURLIcon(value));
        else if ( name.equals("PRESSEDICON") )
            c.setPressedIcon(new SURLIcon(value));
        else
            super.setProperty(comp, name, value);
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}
