// $Id$
package org.wings.style;

import org.wings.util.ComponentVisitor;
import org.wings.io.Device;
import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.session.BrowserType;
import org.wings.session.SessionManager;
import org.wings.plaf.ComponentCG;
import org.wings.border.SBorder;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;

/**
 * A dynamical CSS file generator. It traverses the component hierarchy
 * starting with the passed componentn and gathers the dynamic styles
 * (attributes) of all visited components in a style sheet written to
 * the output device.
 *
 * <p>Example output:<pre>#z5_i{border-style:groove; padding-bottom:0px; border-width:2px; padding-top:1em; padding-left:0px; padding-right:0px; border-color:#c0c0c0; }
#c6_i{border-style:groove; padding-bottom:0px; border-width:2px; padding-top:1em; padding-left:0px; padding-right:0px; border-color:#c0c0c0; }</pre>
 * @author <a href="mailto:B.Schmid@eXXcellent.de">Benjamin Schmid</a>
 */
public class CSSStyleSheetWriter implements ComponentVisitor {
    /**
     * These CSS properties will not be inherited over tables in IE.
     * Circumvents using quirks mode.
     */
    private final static List MSIE_TABLE_RESTINTANT_CSS_PROPS = Arrays.asList(new CSSProperty[] {
        CSSProperty.COLOR, CSSProperty.FONT, CSSProperty.FONT_FAMILY, CSSProperty.FONT_SIZE,
        CSSProperty.FONT_STYLE, CSSProperty.FONT_VARIANT, CSSProperty.FONT_WEIGHT,
        CSSProperty.TEXT_DECORATION, CSSProperty.TEXT_TRANSFORM, CSSProperty.LETTER_SPACING,
        CSSProperty.LINE_HEIGHT, CSSProperty.BACKGROUND_COLOR});

    /**
     * These CSS properties won't work on the TABLE element (wearing the #id).
     * They must be mapped to the inner TD element. (#id_td)
     */
    private final static List MSIE_TD_REQUIRING_CSS_PROPS = Arrays.asList(new CSSProperty[] {
        CSSProperty.PADDING, CSSProperty.PADDING_BOTTOM, CSSProperty.PADDING_LEFT,
        CSSProperty.PADDING_RIGHT, CSSProperty.PADDING_TOP});


    private final Device out;
    private final boolean isMSIE;

    public CSSStyleSheetWriter(Device out) {
        this.out = out;
        final BrowserType currentBrowser = SessionManager.getSession().getUserAgent().getBrowserType();
        isMSIE = BrowserType.IE.equals(currentBrowser);
    }

    private void writeAttributesFrom(final SComponent component)
            throws IOException {

        // grab component styles
        Collection dynamicStyles = component.getDynamicStyles();

        // append CSS styles reqired for border to component styles.
        // (also to avoid duplicate implementation of non-msie workaround!)
        final SBorder border = component.getBorder();
        if (border != null && border.getAttributes() != null) {
            if (dynamicStyles == null)
                dynamicStyles = new ArrayList(5);
            else
                dynamicStyles = new ArrayList(dynamicStyles); // copy unmodifiable collection

            final CSSAttributeSet borderAttributes = border.getAttributes();
            dynamicStyles.add(new CSSStyle(component.getComponentCssSelector(), borderAttributes));
            if (isMSIE) {
                final CSSAttributeSet movedAttributes = copyAttributesToTDelementWorkaround(borderAttributes);
                if (movedAttributes != null) {
                    final CSSSelector innerTDselector = new CSSSelector("#"+component.getName()+"_td");
                    dynamicStyles.add(new CSSStyle(innerTDselector, movedAttributes));
                }
            }
        }

        // Render dynamic styles to style sheet
        if (dynamicStyles != null) {
            final ComponentCG cg = component.getCG();

            for (Iterator iterator = dynamicStyles.iterator(); iterator.hasNext();) {
                final CSSStyle style = (CSSStyle) iterator.next();
                // Map pseudo css selectors to real selectors
                final CSSSelector selector = cg.mapSelector(component, (CSSSelector) style.getSelector());

                // Output selector string
                String selectorString = selector.getSelectorString();

                if (isMSIE) {
                    // IE Workaround: We need to operate IE in quirks mode.
                    // Hence we have to inherit some props manually over TABLE elements.
                    final Set tableBlockedProperties = new HashSet(style.properties());
                    tableBlockedProperties.retainAll(MSIE_TABLE_RESTINTANT_CSS_PROPS);
                    // If this style containes a table blocked CSS property then add table as additional selector.
                    if (tableBlockedProperties.size() > 0)
                        out.print(selectorString).print(" table ").print(", ");
                } else {
                    // Non IE Workaround: In all other browsers we surround each component with two DIV
                    // elements. The outer wears the component id, but the inner DIV is the one which
                    // surrounds the component without any space (i.e. for setting component background)
                    // If the ID of the outer DIV is e1 then the ID of the inner div is e1_i
                    // we need to apply our styles to the inner div, not the outer div!

                    // Selector string contains
                    if (selectorString.indexOf('#') >= 0) {
                        int pos = selectorString.indexOf('#')+1;
                        while (pos < selectorString.length() && Character.isLetterOrDigit(selectorString.charAt(pos)))
                            pos++;
                        // make id to id_i
                        selectorString = selectorString.substring(0, pos) + selectorString.substring(pos);
                    }
                }

                out.print(selectorString);
                out.print("{");
                style.write(out);
                out.print("}\n");
            }
        }
    }

    public void visit(SComponent component) throws Exception {
        writeAttributesFrom(component);
    }

    public void visit(SContainer container) throws Exception {
        writeAttributesFrom(container);
        container.inviteEachComponent(this);
    }

    /**
     * For this damn-crappy so 'called' browser the majestic queen of MSIE
     * we need another damn workaround: MSIE ignores some CSS properties
     * defined on the TABLE element. It expects those on the TD element.
     * (Esp. padding).
     * @return filtered subset of CSS attributes
     */
    private CSSAttributeSet copyAttributesToTDelementWorkaround(final CSSAttributeSet origAttributeSet) {
        CSSAttributeSet filteredSet = null;
        for (Iterator iterator = origAttributeSet.properties().iterator(); iterator.hasNext();) {
            final CSSProperty cssProperty = (CSSProperty) iterator.next();
            if (MSIE_TD_REQUIRING_CSS_PROPS.contains(cssProperty)) {
                if (filteredSet == null)
                    filteredSet = new CSSAttributeSet();
                filteredSet.put(cssProperty, origAttributeSet.get(cssProperty));
            }
        }
        return filteredSet;
    }
}
