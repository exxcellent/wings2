package org.wingx;

import org.wings.*;
import org.wings.border.SLineBorder;
import org.wingx.plaf.css.XPageScrollerCG;

import java.awt.*;
import java.awt.event.AdjustmentListener;


public class XPageScroller
        extends SContainer
        implements Adjustable {

    SPageScroller pageScroller = new SPageScroller(Adjustable.VERTICAL);
    private GridBagConstraints c = new GridBagConstraints();

    public XPageScroller() {
        super(new SGridBagLayout());
        SLineBorder border = new SLineBorder(new Color(200, 200, 200), 0);
        border.setThickness(1, SConstants.TOP);
        setBorder(border);
        setPreferredSize(SDimension.FULLWIDTH);

        c.insets = new Insets(2, 5, 0, 5);
        c.anchor = GridBagConstraints.EAST;
        add(pageScroller);

        pageScroller.setCG(new XPageScrollerCG());
        pageScroller.setLayoutMode(Adjustable.HORIZONTAL);
        pageScroller.setDirectPages(5);
        pageScroller.setShowAsFormComponent(false);
    }

    public SComponent add(SComponent component) {
        c.weightx = 0d;
        return super.addComponent(component, c);
    }

    public SComponent add(SComponent component, double weight) {
        c.weightx = weight;
        return super.addComponent(component, c);
    }

    public void reset(int total) {
        while (getComponentCount() > 1) {
            remove(1);
        }
        normalize(total);
    }

    public void normalize(int total) {
        int value = pageScroller.getValue();
        int visible = pageScroller.getVisibleAmount();
        if (value + visible > total) {
            value = Math.max(0, total - visible);
            pageScroller.setValue(value);
        }
    }

    public void setUnitIncrement(int i) {
        pageScroller.setUnitIncrement(i);
    }

    public void setBlockIncrement(int i) {
        pageScroller.setBlockIncrement(i);
    }

    public int getUnitIncrement() {
        return pageScroller.getUnitIncrement();
    }

    public int getBlockIncrement() {
        return pageScroller.getBlockIncrement();
    }

    public int getValue() {
        return pageScroller.getValue();
    }

    public void setExtent(int value) {
        pageScroller.setExtent(value);
    }

    public int getVisibleAmount() {
        return pageScroller.getVisibleAmount();
    }

    public void setVisibleAmount(int i) {
        pageScroller.setVisibleAmount(i);
    }

    public int getMinimum() {
        return pageScroller.getMinimum();
    }

    public void setMinimum(int i) {
        pageScroller.setMinimum(i);
    }

    public int getMaximum() {
        return pageScroller.getMaximum();
    }

    public void setMaximum(int i) {
        pageScroller.setMaximum(i);
    }

    public int getOrientation() {
        return pageScroller.getOrientation();
    }

    public void setValue(int i) {
        pageScroller.setValue(i);
    }

    public void addAdjustmentListener(AdjustmentListener adjustmentListener) {
        pageScroller.addAdjustmentListener(adjustmentListener);
    }

    public void removeAdjustmentListener(AdjustmentListener adjustmentListener) {
        pageScroller.removeAdjustmentListener(adjustmentListener);
    }

    public SPageScroller getPageScroller() {
        return pageScroller;
    }

    public SBoundedRangeModel getModel() {
    	return pageScroller.getModel();
    }

    public void setModel(SBoundedRangeModel newModel) {
		pageScroller.setModel(newModel);
	}
}
