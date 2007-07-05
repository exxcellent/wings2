/*
 * $Id: LabelCG.java 3016 2006-11-08 13:01:10Z stephanschuster $
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

package org.wings.plaf.css;

import java.util.ArrayList;
import java.util.List;
import org.wings.SIcon;
import org.wings.SResourceIcon;
import org.wings.SSlider;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.plaf.css.script.OnHeadersLoadedScript;
import org.wings.resource.ResourceManager;
import org.wings.session.ScriptManager;
import org.wings.SComponent;
import org.wings.io.Device;
import java.io.IOException;
import org.wings.util.SStringBuilder;
import org.wings.header.SessionHeaders;

/**
 * CG for SSlider instances.
 * @author Christian Schyma
 */
public class SliderCG extends AbstractComponentCG implements org.wings.plaf.SliderCG,
        SParentFrameListener {

    protected final List headers = new ArrayList();

    private SIcon horizontalThumb;
    private SIcon verticalThumb;

    // used by .SSliderBgHoriz and SSliderBgVert in all.css
    static {
        String[] images = new String [] {
            "org/wings/icons/SliderHoriz.png",
            "org/wings/icons/SliderVert.png"
        };

        for ( int x = 0, y = images.length ; x < y ; x++ ) {
            SIcon icon = new SResourceIcon(images[x]);
            icon.getURL(); // hack to externalize
        }
    }

    /**
     * the maximum number of pixels the slider thumb can be moved
     */
    private final Integer defaultMaxPixelConstraint = new Integer(200); // slider bar width - slider thumb width

    public SliderCG() {
        setHorizontalThumbIcon((SIcon) ResourceManager.getObject("SliderCG.horizontalThumbIcon", SIcon.class));
        setVerticalThumbIcon((SIcon) ResourceManager.getObject("SliderCG.verticalThumbIcon", SIcon.class));

        headers.add(Utils.createExternalizedJSHeaderFromProperty(Utils.JS_YUI_SLIDER));
    }

    public void writeInternal(final Device device, final SComponent component) throws IOException {
        String id = component.getName();
        String thumbId = id + "_sliderthumb";
        SSlider c = (SSlider) component;
        Integer maxPixelConstraint = (Integer)component.getClientProperty("maxPixelConstraint");
        if (maxPixelConstraint == null) maxPixelConstraint = defaultMaxPixelConstraint;

        // scale factor for converting the pixel offset into a real value
        double factor = (c.getMaximum() - c.getMinimum()) / maxPixelConstraint.floatValue();

        // render HTML
        device.print("<div");
        Utils.optAttribute(device, "id", id);
        if (SSlider.HORIZONTAL == c.getOrientation()) {
            Utils.optAttribute(device, "class", "SSliderBgHoriz");
        } else if (SSlider.VERTICAL == c.getOrientation()) {
            Utils.optAttribute(device, "class", "SSliderBgVert");
        }

        device.print(">");

        device.print("<div");
        Utils.optAttribute(device, "id", thumbId);
        Utils.optAttribute(device, "class", "SSliderThumb"); // thumb origin to override table align="center"
        device.print(">");

        device.print("<img");
        if (SSlider.HORIZONTAL == c.getOrientation()) {
            Utils.optAttribute(device, "src", horizontalThumb.getURL());
        } else if (SSlider.VERTICAL == c.getOrientation()) {

            Utils.optAttribute(device, "src", verticalThumb.getURL());
        }
        device.print(" />");

        device.print("</div>");

        String valId = (new SStringBuilder(id).append("_val")).toString();
        device.print("<div");
        if (c.getOrientation() == SSlider.VERTICAL) {
            Utils.optAttribute(device, "class", "SSliderValueRight");
        } else if (c.getOrientation() == SSlider.HORIZONTAL) {
            Utils.optAttribute(device, "class", "SSliderValueBottom");
        }
        device.print("><input ");
        Utils.optAttribute(device, "autocomplete", "off");
        Utils.optAttribute(device, "name", valId);
        Utils.optAttribute(device, "id", valId);
        Utils.optAttribute(device, "value", 0);
        Utils.optAttribute(device, "size", 4);
        Utils.optAttribute(device, "type", "text");
        device.print(" readonly></div>");

        device.print("</div></div>");

        // prepare script
        String slider = (new SStringBuilder(id).append("_").append("slider")).toString();

        SStringBuilder code = new SStringBuilder("function() {");
        code.append("var ").append(slider).append(" = YAHOO.widget.Slider.");
        if (SSlider.HORIZONTAL == c.getOrientation()) {
            code.append("getHorizSlider(");
        } else if (SSlider.VERTICAL == c.getOrientation()) {
            code.append("getVertSlider(");
        }

        code.append("'"+ id +"', ")
        .append("'"+ thumbId +"', ")
        .append("0, ")
        .append(maxPixelConstraint.intValue());
        if (c.getSnapToTicks()) {
            code.append(", ").append(c.getMajorTickSpacing() / factor);
        }
        code.append("); ");

        code.append(slider).append(".setValue(").append((c.getValue() - c.getMinimum()) / factor).append(");")
        .append(slider).append(".onChange = function(offset) {document.getElementById('").append(valId).append("').value = offset * ").append(factor).append("+ ").append(c.getMinimum()).append("};")
        .append("}");

        ScriptManager.getInstance().addScriptListener(new OnHeadersLoadedScript(code.toString(), false));
    }

    public void installCG(final SComponent comp) {
        super.installCG(comp);
        comp.addParentFrameListener(this);
    }

    public void setHorizontalThumbIcon(SIcon icon) {
        this.horizontalThumb = icon;
    }

    public void setVerticalThumbIcon(SIcon icon) {
        this.verticalThumb = icon;
    }

    public void parentFrameAdded(SParentFrameEvent e) {
        SessionHeaders.getInstance().registerHeaders(headers);
    }

    public void parentFrameRemoved(SParentFrameEvent e) {
        SessionHeaders.getInstance().deregisterHeaders(headers);
    }

}
