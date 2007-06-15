/*
 * @(#)SBorderFactory.java	1.0 01/01/02
 *
 * Factory inspired  by javax.swing.BorderFactory
 */
package org.wings;

import java.awt.Color;
import java.awt.Font;
import org.wings.SComponent;
import org.wings.border.*;

/**
 * Factory class for standard <code>SBorder</code> elements.
 */
public class SBorderFactory 
{

    /** We are static */
    private SBorderFactory() {
    }

    //// LineBorder ///////////////////////////////////////////////////////////////

    /**
     * Creates a line SBorder withe the specified color.
     *
     * @param color  a <code>Color</code> to use for the line
     * @return the <code>SBorder</code> object
     */
    public static SBorder createSLineBorder(Color color) {
        return new SLineBorder(color, 1);
    }

    /**
     * Creates a line SBorder with the specified color
     * and width. The width applies to all four sides of the
     * SBorder. To specify widths individually for the top,
     * bottom, left, and right, use 
     * {@link #createSEmptyBorder(int, int, int, int)}. 
     *
     * @param color  a <code>Color</code> to use for the line
     * @param thickness  an integer specifying the width in pixels
     * @return the <code>SBorder</code> object
     */
    public static SBorder createSLineBorder(Color color, int thickness)  {
        return new SLineBorder(color, thickness);
    }
    

    //// BevelBorder /////////////////////////////////////////////////////////////

    /**
     * Creates a SBorder with a raised beveled edge, using
     * brighter shades of the component's current background color
     * for highlighting, and darker shading for shadows.
     * (In a raised SBorder, highlights are on top and shadows
     *  are underneath.)
     *
     * @return the <code>SBorder</code> object
     */
    public static SBorder createRaisedSBevelBorder() {
        return new SBevelBorder(SBevelBorder.RAISED);
    }

    /**
     * Creates a SBorder with a lowered beveled edge, using
     * brighter shades of the component's current background color
     * for highlighting, and darker shading for shadows.
     * (In a lowered SBorder, shadows are on top and highlights
     *  are underneath.)
     *
     * @return the <code>SBorder</code> object
     */
    public static SBorder createLoweredSBevelBorder() {
        return new SBevelBorder(SBevelBorder.LOWERED);
    }

    /**
     * Creates a beveled SBorder of the specified type, using
     * brighter shades of the component's current background color
     * for highlighting, and darker shading for shadows.
     * (In a lowered SBorder, shadows are on top and highlights
     *  are underneath.)
     *
     * @param type  an integer specifying either
     *			<code>BevelBorder.LOWERED</code> or
     *              	<code>BevelBorder.RAISED</code>
     * @return the <code>SBorder</code> object
     */
    public static SBorder createSBevelBorder(int type) {
	return new SBevelBorder(type);
    }
	
    /**
     * Creates a beveled SBorder of the specified type, using
     * the specified highlighting and shadowing. The outer 
     * edge of the highlighted area uses a brighter shade of
     * the highlight color. The inner edge of the shadow area
     * uses a brighter shade of the shadow color.
     * 
     * @param type  an integer specifying either 
     *			<code>BevelBorder.LOWERED</code> or
     *              	<code>BevelBorder.RAISED</code>
     * @param highlight  a <code>Color</code> object for highlights
     * @param shadow     a <code>Color</code> object for shadows
     * @return the <code>SBorder</code> object
     */
/*    public static SBorder createSBevelBorder(int type, Color highlight, Color shadow) {
        return new SBevelBorder(type, highlight, shadow);
    } */

    /**
     * Creates a beveled SBorder of the specified type, using
     * the specified colors for the inner and outer highlight
     * and shadow areas. 
     * <p>
     * Note: The shadow inner and outer colors are
     * switched for a lowered bevel SBorder.
     * 
     * @param type  an integer specifying either 
     *		<code>BevelBorder.LOWERED</code> or
     *          <code>BevelBorder.RAISED</code>
     * @param highlightOuter  a <code>Color</code> object for the
     *			outer edge of the highlight area
     * @param highlightInner  a <code>Color</code> object for the
     *			inner edge of the highlight area
     * @param shadowOuter     a <code>Color</code> object for the
     *			outer edge of the shadow area
     * @param shadowInner     a <code>Color</code> object for the
     *			inner edge of the shadow area
     * @return the <code>SBorder</code> object
     */
/*    public static SBorder createSBevelBorder(int type,
                        Color highlightOuter, Color highlightInner,
                        Color shadowOuter, Color shadowInner) {
        return new SBevelBorder(type, highlightOuter, highlightInner, 
					shadowOuter, shadowInner);
    }
*/

    //// EtchedBorder ///////////////////////////////////////////////////////////

    /**
     * Creates a SBorder with an "etched" look using
     * the component's current background color for 
     * highlighting and shading.
     *
     * @return the <code>SBorder</code> object
     */
    public static SBorder createSEtchedBorder()    {
	return new SEtchedBorder();
    }

    /**
     * Creates a SBorder with an "etched" look using
     * the specified highlighting and shading colors.
     *
     * @param etchedType  Raised or Lowered
     * @return the <code>SBorder</code> object 
     */
    public static SBorder createSEtchedBorder(int etchedType)    {
        return new SEtchedBorder(etchedType);
    }

 
    /**
     * Creates a SBorder with an "etched" look using
     * the specified highlighting and shading colors.
     *
     * @param type    	one of <code>EtchedBorder.RAISED</code>, or
     *			<code>EtchedBorder.LOWERED</code>
     * @param highlight  a <code>Color</code> object for the SBorder highlights
     * @param shadow     a <code>Color</code> object for the SBorder shadows
     * @return the <code>SBorder</code> object 
     * @since 1.3
     */
  /*  public static SBorder createSEtchedBorder(int type, Color highlight,
					    Color shadow)    {
        return new SEtchedBorder(type, highlight, shadow);
    }
*/
    //// STitledBorder ////////////////////////////////////////////////////////////

    /**
     * Creates a new STitled SBorder with the specified title,
     * the default SBorder type (determined by the current look and feel),
     * the default text position (sitting on the top line),
     * the default justification (leading), and the default
     * font and text color (determined by the current look and feel).
     *
     * @param title      a <code>String</code> containing the text of the title
     * @return the <code>STitledBorder</code> object
     */
    public static STitledBorder createSTitledBorder(String title)     {
        return new STitledBorder(title);
    }

    /**
     * Creates a new STitled SBorder with an empty title,
     * the specified SBorder object,
     * the default text position (sitting on the top line),
     * the default justification (leading), and the default
     * font and text color (determined by the current look and feel).
     *
     * @param SBorder     the <code>SBorder</code> object to add the title to; if
     *                   <code>null</code> the <code>SBorder</code> is determined
     *                   by the current look and feel.
     * @return the <code>STitledBorder</code> object
     */
    public static STitledBorder createSTitledBorder(SBorder border)       {
        return new STitledBorder(border);
    }

    /**
     * Adds a title to an existing SBorder,
     * with default positioning (sitting on the top line),
     * default justification (leading) and the default
     * font and text color (determined by the current look and feel).
     *
     * @param SBorder     the <code>SBorder</code> object to add the title to
     * @param title      a <code>String</code> containing the text of the title
     * @return the <code>STitledBorder</code> object
     */
    public static STitledBorder createSTitledBorder(SBorder border, 
						   String title) {
        return new STitledBorder(border, title);
    }

    /**
     * Adds a title to an existing SBorder, with the specified
     * positioning and using the default
     * font and text color (determined by the current look and feel).
     *
     * @param SBorder      the <code>SBorder</code> object to add the title to
     * @param title       a <code>String</code> containing the text of the title
     * @param titleJustification  an integer specifying the justification 
     *        of the title -- one of the following:
     *<ul>
     *<li><code>STitledBorder.LEFT</code>
     *<li><code>STitledBorder.CENTER</code>
     *<li><code>STitledBorder.RIGHT</code>
     *<li><code>STitledBorder.LEADING</code>
     *<li><code>STitledBorder.TRAILING</code>
     *<li><code>STitledBorder.DEFAULT_JUSTIFICATION</code> (leading)
     *</ul>
     * @param titlePosition       an integer specifying the vertical position of
     *        the text in relation to the SBorder -- one of the following:
     *<ul>
     *<li><code>STitledBorder.ABOVE_TOP</code>
     *<li><code>STitledBorder.TOP</code> (sitting on the top line)
     *<li><code>STitledBorder.BELOW_TOP</code>
     *<li><code>STitledBorder.ABOVE_BOTTOM</code>
     *<li><code>STitledBorder.BOTTOM</code> (sitting on the bottom line)
     *<li><code>STitledBorder.BELOW_BOTTOM</code>
     *<li><code>STitledBorder.DEFAULT_POSITION</code> (top)
     *</ul>
     * @return the <code>STitledBorder</code> object
     */
    public static STitledBorder createSTitledBorder(SBorder border, 
                        String title,
                        int titleJustification,
                        int titlePosition)      {
        return new STitledBorder(border, title, titleJustification,
                        titlePosition);
    }

    /**
     * Adds a title to an existing SBorder, with the specified
     * positioning and font, and using the default text color
     * (determined by the current look and feel).
     *
     * @param border      the <code>SBorder</code> object to add the title to
     * @param title       a <code>String</code> containing the text of the title
     * @param titleJustification  an integer specifying the justification
     *        of the title -- one of the following:
     *<ul>
     *<li><code>STitledBorder.LEFT</code>
     *<li><code>STitledBorder.CENTER</code>
     *<li><code>STitledBorder.RIGHT</code>
     *<li><code>STitledBorder.LEADING</code>
     *<li><code>STitledBorder.TRAILING</code>
     *<li><code>STitledBorder.DEFAULT_JUSTIFICATION</code> (leading)
     *</ul>
     * @param titlePosition       an integer specifying the vertical position of
     *        the text in relation to the SBorder -- one of the following:
     *<ul>
     *<li><code> STitledBorder.ABOVE_TOP</code>
     *<li><code>STitledBorder.TOP</code> (sitting on the top line)
     *<li><code>STitledBorder.BELOW_TOP</code>
     *<li><code>STitledBorder.ABOVE_BOTTOM</code>
     *<li><code>STitledBorder.BOTTOM</code> (sitting on the bottom line)
     *<li><code>STitledBorder.BELOW_BOTTOM</code>
     *<li><code>STitledBorder.DEFAULT_POSITION</code> (top)
     *</ul>
     * @param titleFont           a Font object specifying the title font
     * @return the STitledBorder object
     */
    public static STitledBorder createSTitledBorder(SBorder border,       	
                        String title,
                        int titleJustification,
                        int titlePosition,
                        SFont titleFont) {
        return new STitledBorder(border, title, titleJustification,
                        titlePosition, titleFont);
    }

    /**
     * Adds a title to an existing SBorder, with the specified
     * positioning, font and color.
     *
     * @param border      the <code>SBorder</code> object to add the title to
     * @param title       a <code>String</code> containing the text of the title
     * @param titleJustification  an integer specifying the justification
     *        of the title -- one of the following:
     *<ul>
     *<li><code>STitledBorder.LEFT</code>
     *<li><code>STitledBorder.CENTER</code>
     *<li><code>STitledBorder.RIGHT</code>
     *<li><code>STitledBorder.LEADING</code>
     *<li><code>STitledBorder.TRAILING</code>
     *<li><code>STitledBorder.DEFAULT_JUSTIFICATION</code> (leading)
     *</ul>
     * @param titlePosition       an integer specifying the vertical position of
     *        the text in relation to the SBorder -- one of the following:
     *<ul>
     *<li><code> STitledBorder.ABOVE_TOP</code>
     *<li><code>STitledBorder.TOP</code> (sitting on the top line)
     *<li><code>STitledBorder.BELOW_TOP</code>
     *<li><code>STitledBorder.ABOVE_BOTTOM</code>
     *<li><code>STitledBorder.BOTTOM</code> (sitting on the bottom line)
     *<li><code>STitledBorder.BELOW_BOTTOM</code>
     *<li><code>STitledBorder.DEFAULT_POSITION</code> (top)
     *</ul>
     * @param titleFont   a <code>Font</code> object specifying the title font
     * @param titleColor  a <code>Color</code> object specifying the title color
     * @return the <code>STitledBorder</code> object
     */
    public static STitledBorder createTitledBorder(SBorder border,                     
                        String title,
                        int titleJustification,
                        int titlePosition,
                        SFont titleFont,
                        Color titleColor)       {
        return new STitledBorder(border, title, titleJustification,
                        titlePosition, titleFont, titleColor);
    }

//// EmptyBorder ///////////////////////////////////////////////////////////	

    /**
     * Creates an empty SBorder that takes up no space. (The width
     * of the top, bottom, left, and right sides are all zero.)
     *
     * @return the <code>SBorder</code> object
     */
    public static SBorder createSEmptyBorder() {
	return new SEmptyBorder(0, 0, 0, 0);
    }

    /**
     * Creates an empty SBorder that takes up space but which does
     * no drawing, specifying the width of the top, left, bottom, and
     * right sides.
     *
     * @param top     an integer specifying the width of the top,
     *			in pixels
     * @param left    an integer specifying the width of the left side,
     *			in pixels
     * @param bottom  an integer specifying the width of the bottom,
     *			in pixels
     * @param right   an integer specifying the width of the right side,
     *			in pixels
     * @return the <code>SBorder</code> object
     */
    public static SBorder createSEmptyBorder(int top, int left, 
						int bottom, int right) {
	return new SEmptyBorder(top, left, bottom, right);
    }
}
