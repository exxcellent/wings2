package org.wingx;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.text.MessageFormat;

import org.wings.SAbstractAdjustable;
import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SDefaultBoundedRangeModel;
import org.wings.SDimension;
import org.wings.SLabel;
import org.wings.SScrollPane;
import org.wings.SScrollPaneLayout;
import org.wings.STable;


/**
 * XScrollPane
 *
 * @author jdenzel
 */
public class XScrollPane extends SScrollPane {

	protected SComboBox extentCombo = new SComboBox(new Integer[] {
			new Integer(8), new Integer(10), new Integer(12), new Integer(14),
			new Integer(16), new Integer(18), new Integer(20), new Integer(22),
			new Integer(24), new Integer(26), new Integer(28), new Integer(30),
			new Integer(32) });

	protected final XPageScroller pageScroller = new XPageScroller();

	protected final SLabel extentComboLabel = new SLabel();

	protected final SLabel totalLabel = new SLabel();

    protected STable tableComponent;

	protected String visibleSectionLabel = "{0} .. {1} von {2}";

	public XScrollPane() {
		this(null);
	}

	public XScrollPane(STable tableComponent) {
		this(tableComponent, 10);
	}

	public XScrollPane(STable tableComponent, int verticalExtent) {
		setPreferredSize(SDimension.FULLAREA);
		setVerticalAlignment(SConstants.TOP_ALIGN);

		extentCombo.addActionListener(new ExtentComboActionListener());
		extentCombo.setSelectedItem(new Integer(verticalExtent));

		pageScroller.add(totalLabel);
		pageScroller.add(extentComboLabel);
		pageScroller.add(extentCombo);
		pageScroller.add(new SLabel(" "), 1d);

		pageScroller.addAdjustmentListener(new PageAdjustmentListener());
		pageScroller.setExtent(verticalExtent);
		pageScroller.setHorizontalAlignment(SConstants.LEFT_ALIGN);

		setHorizontalScrollBar(pageScroller);
		setHorizontalScrollBarPolicy(SScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		setVerticalScrollBar(null);
		setVerticalScrollBarPolicy(SScrollPane.VERTICAL_SCROLLBAR_NEVER);
		setMode(SScrollPane.MODE_PAGING);

		setVerticalExtent(verticalExtent);

		if (tableComponent != null)
			setViewportView(tableComponent);
	}

    /**
     * Sets the horizontal scrollbar.
     *
     * @param sb         the scrollbar that controls the viewport's horizontal view position
     * @param constraint the constraint for the {@link LayoutManager} of this {@link SContainer}.
     *                   The {@link LayoutManager} is per default {@link SScrollPaneLayout}.
	 */
	public void setHorizontalScrollBar(Adjustable sb, String constraint) {
		if (horizontalScrollBar != null) {
			if (horizontalScrollBar instanceof SAbstractAdjustable)
                ((SAbstractAdjustable) horizontalScrollBar).setModel(new SDefaultBoundedRangeModel());
			else if (horizontalScrollBar instanceof XPageScroller)
                ((XPageScroller) horizontalScrollBar).setModel(new SDefaultBoundedRangeModel());

			if (horizontalScrollBar instanceof SComponent)
				remove((SComponent) horizontalScrollBar);
		}

		horizontalScrollBar = sb;

		if (horizontalScrollBar != null) {
			if (horizontalScrollBar instanceof SComponent)
                addComponent((SComponent) horizontalScrollBar, constraint, getComponentCount());

			if (horizontalScrollBar instanceof SAbstractAdjustable) {
				SAbstractAdjustable scrollbar = (SAbstractAdjustable) horizontalScrollBar;
				if (scrollbar.getOrientation() == SConstants.HORIZONTAL)
					scrollbar.setModel(horizontalModel);
				else
					scrollbar.setModel(verticalModel);
			} else if (horizontalScrollBar instanceof XPageScroller) {
				XPageScroller scrollbar = (XPageScroller) horizontalScrollBar;
				if (scrollbar.getOrientation() == SConstants.HORIZONTAL)
					scrollbar.setModel(horizontalModel);
				else
					scrollbar.setModel(verticalModel);
			}

            adoptScrollBarVisibility(horizontalScrollBar, horizontalScrollBarPolicy);
		}

		reload();
	}

	/**
	 * Sets the vertical scrollbar.
	 * 
     * @param sb         the scrollbar that controls the viewport's vertical view position
     * @param constraint the constraint for the {@link LayoutManager} of this {@link SContainer}.
     *                   The {@link LayoutManager} is per default {@link SScrollPaneLayout}.
	 */
	public void setVerticalScrollBar(Adjustable sb, String constraint) {
		if (verticalScrollBar != null) {
			if (verticalScrollBar instanceof SAbstractAdjustable)
                ((SAbstractAdjustable) verticalScrollBar).setModel(new SDefaultBoundedRangeModel());
			else if (verticalScrollBar instanceof XPageScroller)
                ((XPageScroller) verticalScrollBar).setModel(new SDefaultBoundedRangeModel());

			if (verticalScrollBar instanceof SComponent)
				remove((SComponent) verticalScrollBar);
		}

		verticalScrollBar = sb;

		if (verticalScrollBar != null) {
			if (verticalScrollBar instanceof SComponent)
                addComponent((SComponent) verticalScrollBar, constraint, getComponentCount());

			if (verticalScrollBar instanceof SAbstractAdjustable) {
				SAbstractAdjustable scrollbar = (SAbstractAdjustable) verticalScrollBar;
				if (scrollbar.getOrientation() == SConstants.HORIZONTAL)
					scrollbar.setModel(horizontalModel);
				else
					scrollbar.setModel(verticalModel);
			} else if (verticalScrollBar instanceof XPageScroller) {
				XPageScroller scrollbar = (XPageScroller) verticalScrollBar;
				if (scrollbar.getOrientation() == SConstants.HORIZONTAL)
					scrollbar.setModel(horizontalModel);
				else
					scrollbar.setModel(verticalModel);
			}

			adoptScrollBarVisibility(verticalScrollBar, verticalScrollBarPolicy);
		}

		reload();
	}

	public void setExtentLabel(String label) {
		extentComboLabel.setText(label);
	}

	public String getVisibleSectionLabel() {
		return visibleSectionLabel;
	}

	public void setVisibleSectionLabel(String visibleSectionLabel) {
		this.visibleSectionLabel = visibleSectionLabel;
	}

	/**
	 * @inheritDoc
	 */
	public void setViewportView(SComponent view) {
		if (view == null) {
			throw new NullPointerException();
		}
		if (getScrollable() != null) {
            throw new RuntimeException("error: this component is not reinitializable");
		}
		if (!(view instanceof XTable)) {
            throw new RuntimeException("the inner component must be of type XTable");
		}
		tableComponent = (XTable) view;
		tableComponent.setVerticalAlignment(SConstants.TOP_ALIGN);

		super.setViewportView(view);
		refresh();
	}

	public void setVerticalExtent(int ext) {
		super.setVerticalExtent(ext);
		extentCombo.setSelectedItem(new Integer(ext));
	}

	public void addAdjustmentListener(AdjustmentListener al) {
		pageScroller.addAdjustmentListener(al);
	}

	public void refresh() {
		scrollable.getViewportSize().height = verticalExtent;
		refreshTotalLabel();
	}

    protected void refreshTotalLabel() {
		if (tableComponent == null)
			return;

		Rectangle viewportSize = tableComponent.getViewportSize();
		if (viewportSize == null) {
			totalLabel.setText(null);
			return;
		}

		int startRow = viewportSize.y;
		int endRow = tableComponent.getRowCount();
		endRow = Math.min(startRow + viewportSize.height, endRow);

		String text;
		if (endRow > 0) {
			int rowCount = tableComponent.getModel().getRowCount();
			text = MessageFormat.format(visibleSectionLabel, new Object[] {new Integer(startRow + 1),
					new Integer(endRow), new Integer(rowCount)});

			boolean moreRecordsAvailable = endRow >= 100;
			if (moreRecordsAvailable)
				text += "  (+)";

			getPageScroller().setVisible(true);
		} else {
			text = null;
			getPageScroller().setVisible(false);
		}

		totalLabel.setText(text);
	}

	class PageAdjustmentListener implements AdjustmentListener {
		public void adjustmentValueChanged(AdjustmentEvent e) {
			refreshTotalLabel();
		}
	}

	class ExtentComboActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (tableComponent == null)
				return;
			Integer extent = (Integer) extentCombo.getSelectedItem();
            setVerticalExtent(extent.intValue());
		}
    }

	public XPageScroller getPageScroller() {
		return pageScroller;
	}
}
