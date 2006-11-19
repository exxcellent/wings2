/*
 * $Id: LabelCG.java 3016 2006-11-08 13:01:10Z stephanschuster $
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.plaf.css;

import org.wings.SIcon;
import org.wings.SResourceIcon;
import org.wings.SSlider;
import org.wings.plaf.CGManager;
import org.wings.script.JavaScriptDOMListener;
import org.wings.script.JavaScriptEvent;
import org.wings.session.SessionManager;
import org.wings.SComponent;

import org.wings.io.Device;
import java.io.IOException;
import org.wings.util.SStringBuilder;

/**
 * CG for SSlider instances.
 * @author Christian Schyma
 */
public class SliderCG  extends AbstractComponentCG implements org.wings.plaf.SliderCG {
    
    // these graphics are needed for hsvcolorpicker.css
    static {
        String[] images = new String [] {
            "org/wings/icons/SliderHorizThumb.png",
            "org/wings/icons/SliderHoriz.png",
            "org/wings/icons/SliderVertThumb.png",
            "org/wings/icons/SliderVert.png"
        };
        
        for ( int x = 0, y = images.length ; x < y ; x++ ) {
            SIcon icon = new SResourceIcon(images[x]);
            icon.getURL(); // hack to externalize
        }
    }
    
    private String horizontalSliderUrl = "-org/wings/icons/SliderHorizThumb.png";
    private String verticalSliderUrl = "-org/wings/icons/SliderVertThumb.png";
    
    public SliderCG() {
    }
    
    public void installCG(SComponent c) {
    }
    
    public void uninstallCG(SComponent c) {
    }
    
    public void componentChanged(SComponent c) {
    }
    
    public void writeInternal(final Device device, final SComponent component) throws IOException {
        String id = component.getName();
        String thumbId = id + "_sliderthumb";
        SSlider c = (SSlider) component;
        
        // render HTML
        device.print("<div");
        Utils.optAttribute(device, "id", id);
        if (SSlider.HORIZONTAL == c.getOrientation()) {
            Utils.optAttribute(device, "class", "SSliderBg");
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
            Utils.optAttribute(device, "src", horizontalSliderUrl);
        } else if (SSlider.VERTICAL == c.getOrientation()) {
            Utils.optAttribute(device, "src", verticalSliderUrl);
        }
        device.print(" />");
        
        device.print("</div></div>");
        
        String valId = (new SStringBuilder(id).append("_val")).toString();
        device.print("<input ");
        Utils.optAttribute(device, "autocomplete", "off");
        Utils.optAttribute(device, "name", valId);
        Utils.optAttribute(device, "id", valId);
        Utils.optAttribute(device, "value", 0);
        Utils.optAttribute(device, "size", 4);
        Utils.optAttribute(device, "type", "text");
        device.print(" readonly>");
        
        // prepare script
        String slider = (new SStringBuilder(id).append("_").append("slider")).toString();
        double factor = (c.getMaximum() - c.getMinimum()) / 200.;
        //int pixelsToTheRight = c.getMaximum() - c.getMinimum();
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
        .append("200");
        
        if (c.getSnapToTicks()) {
            code.append(", ").append(c.getMajorTickSpacing() / factor);
        }
        
        code.append("); ")
        //.append(slider).append(".setValue(").append(c.getValue() - c.getMinimum()).append(");")
        .append(slider).append(".setValue(").append((c.getValue() - c.getMinimum()) / factor).append(");")
        //.append(slider).append(".onChange = function(offset) {document.getElementById('").append(valId).append("').value = offset + ").append(c.getMinimum()).append("};")
        .append(slider).append(".onChange = function(offset) {document.getElementById('").append(valId).append("').value = offset * ").append(factor).append("+ ").append(c.getMinimum()).append("};")
        .append("}");
        
        
//        System.out.println("## factor = "+ factor);
//        System.out.println("## c.getValue = "+ c.getValue());
//        System.out.println("## c.getMinimum = "+ c.getMinimum());
//        System.out.println("## setValue = "+ (c.getValue() - c.getMinimum()) / factor);
        
        // attach script
        JavaScriptDOMListener listener = new JavaScriptDOMListener(JavaScriptEvent.ON_AVAILABLE, code.toString(), component);
        component.addScriptListener(listener);
    }
    
}



//public class LabelCG extends AbstractLabelCG implements org.wings.plaf.LabelCG {
//
//
//    public void installCG(SComponent component) {
//        super.installCG(component);
//        ((SLabel)component).setWordWrap(wordWrapDefault);
//    }



