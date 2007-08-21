package org.wings;

import org.wings.plaf.SplitPaneCG;

public class SSplitPane
    extends SContainer
    implements LowLevelEventListener
{
    /**
     * Vertical split indicates the <code>SComponent</code>s are
     * split along the y axis.  For example the two
     * <code>SComponent</code>s will be split one on top of the other.
     */
    public final static int VERTICAL_SPLIT = 0;

    /**
     * Horizontal split indicates the <code>SComponent</code>s are
     * split along the x axis.  For example the two
     * <code>SComponent</code>s will be split one to the left of the
     * other.
     */
    public final static int HORIZONTAL_SPLIT = 1;

    /**
     * Used to add a <code>SComponent</code> to the left of the other
     * <code>SComponent</code>.
     */
    public final static String LEFT = "left";

    /**
     * Used to add a <code>SComponent</code> to the right of the other
     * <code>SComponent</code>.
     */
    public final static String RIGHT = "right";

    /**
     * Used to add a <code>SComponent</code> above the other
     * <code>SComponent</code>.
     */
    public final static String TOP = "top";

    /**
     * Used to add a <code>SComponent</code> below the other
     * <code>SComponent</code>.
     */
    public final static String BOTTOM = "bottom";

    /**
     * Used to add a <code>SComponent</code> that will represent the divider.
     */
    public final static String DIVIDER = "divider";

    /**
     * How the views are split.
     */
    protected int orientation;

    /**
     * Whether or not the views are continuously redisplayed while
     * resizing.
     */
    protected boolean continuousLayout;

    /**
     * The left or top component.
     */
    protected SComponent leftComponent;

    /**
     * The right or bottom component.
     */
    protected SComponent rightComponent;

    /**
     * Size of the divider.
     */
    protected int dividerSize = 4;

    /**
     * How to distribute extra space.
     */
    private double resizeWeight;

    /**
     * Location of the divider, at least the value that was set, the UI may
     * have a different value.
     */
    private int dividerLocation;
    private int newLocation = -1;


    /**
     * Creates a new <code>JSplitPane</code> configured to arrange the child
     * components side-by-side horizontally with no continuous
     * layout, using two buttons for the components.
     */
    public SSplitPane() {
        this(SSplitPane.HORIZONTAL_SPLIT, false);
    }


    /**
     * Creates a new <code>JSplitPane</code> configured with the
     * specified orientation and no continuous layout.
     *
     * @param newOrientation  <code>JSplitPane.HORIZONTAL_SPLIT</code> or
     *                        <code>JSplitPane.VERTICAL_SPLIT</code>
     * @exception IllegalArgumentException if <code>orientation</code>
     *		is not one of HORIZONTAL_SPLIT or VERTICAL_SPLIT.
     */
    public SSplitPane(int newOrientation) {
        this(newOrientation, false);
    }


    /**
     * Creates a new <code>JSplitPane</code> with the specified
     * orientation and redrawing style.
     *
     * @param newOrientation  <code>JSplitPane.HORIZONTAL_SPLIT</code> or
     *                        <code>JSplitPane.VERTICAL_SPLIT</code>
     * @param newContinuousLayout  a boolean, true for the components to
     *        redraw continuously as the divider changes position, false
     *        to wait until the divider position stops changing to redraw
     * @exception IllegalArgumentException if <code>orientation</code>
     *		is not one of HORIZONTAL_SPLIT or VERTICAL_SPLIT
     */
    public SSplitPane(int newOrientation, boolean newContinuousLayout) {
        this(newOrientation, newContinuousLayout, null, null);
    }


    /**
     * Creates a new <code>JSplitPane</code> with the specified
     * orientation and
     * with the specified components that do not do continuous
     * redrawing.
     *
     * @param newOrientation  <code>JSplitPane.HORIZONTAL_SPLIT</code> or
     *                        <code>JSplitPane.VERTICAL_SPLIT</code>
     * @param newLeftSComponent the <code>SComponent</code> that will
     *		appear on the left
     *        	of a horizontally-split pane, or at the top of a
     *        	vertically-split pane
     * @param newRightSComponent the <code>SComponent</code> that will
     *		appear on the right
     *        	of a horizontally-split pane, or at the bottom of a
     *        	vertically-split pane
     * @exception IllegalArgumentException if <code>orientation</code>
     *		is not one of: HORIZONTAL_SPLIT or VERTICAL_SPLIT
     */
    public SSplitPane(int newOrientation, SComponent newLeftSComponent, SComponent newRightSComponent){
        this(newOrientation, false, newLeftSComponent, newRightSComponent);
    }


    /**
     * Creates a new <code>JSplitPane</code> with the specified
     * orientation and
     * redrawing style, and with the specified components.
     *
     * @param newOrientation  <code>JSplitPane.HORIZONTAL_SPLIT</code> or
     *                        <code>JSplitPane.VERTICAL_SPLIT</code>
     * @param newContinuousLayout  a boolean, true for the components to
     *        redraw continuously as the divider changes position, false
     *        to wait until the divider position stops changing to redraw
     * @param newLeftSComponent the <code>SComponent</code> that will
     *		appear on the left
     *        	of a horizontally-split pane, or at the top of a
     *        	vertically-split pane
     * @param newRightSComponent the <code>SComponent</code> that will
     *		appear on the right
     *        	of a horizontally-split pane, or at the bottom of a
     *        	vertically-split pane
     * @exception IllegalArgumentException if <code>orientation</code>
     *		is not one of HORIZONTAL_SPLIT or VERTICAL_SPLIT
     */
    public SSplitPane(int newOrientation, boolean newContinuousLayout, SComponent newLeftSComponent, SComponent newRightSComponent){
        dividerLocation = -1;
        setLayout(null);

        orientation = newOrientation;
        if (orientation != HORIZONTAL_SPLIT && orientation != VERTICAL_SPLIT)
            throw new IllegalArgumentException("cannot create SSplitPane, " +
                                               "orientation must be one of " +
                                               "JSplitPane.HORIZONTAL_SPLIT " +
                                               "or JSplitPane.VERTICAL_SPLIT");
        continuousLayout = newContinuousLayout;
        if (newLeftSComponent != null)
            setLeftComponent(newLeftSComponent);
        if (newRightSComponent != null)
            setRightComponent(newRightSComponent);

    }

    /**
     * Sets the size of the divider.
     *
     * @param newSize an integer giving the size of the divider in pixels
     * @beaninfo
     *        bound: true
     *  description: The size of the divider.
     */
    public void setDividerSize(int newSize) {
        int oldSize = dividerSize;

        if (oldSize != newSize) {
            dividerSize = newSize;
            reloadIfChange(oldSize, newSize);
        }
    }

    /**
     * Returns the size of the divider.
     *
     * @return an integer giving the size of the divider in pixels
     */
    public int getDividerSize() {
        return dividerSize;
    }

    /**
     * Sets the component to the left (or above) the divider.
     *
     * @param comp the <code>SComponent</code> to display in that position
     */
    public void setLeftComponent(SComponent comp) {
        if (comp == null) {
            if (leftComponent != null) {
                remove(leftComponent);
                leftComponent = null;
            }
        } else {
            add(comp, SSplitPane.LEFT);
        }
    }

    /**
     * Returns the component to the left (or above) the divider.
     *
     * @return the <code>SComponent</code> displayed in that position
     * @beaninfo
     *    preferred: true
     *  description: The component to the left (or above) the divider.
     */
    public SComponent getLeftComponent() {
        return leftComponent;
    }

    /**
     * Sets the component above, or to the left of the divider.
     *
     * @param comp the <code>SComponent</code> to display in that position
     * @beaninfo
     *  description: The component above, or to the left of the divider.
     */
    public void setTopComponent(SComponent comp) {
        setLeftComponent(comp);
    }

    /**
     * Returns the component above, or to the left of the divider.
     *
     * @return the <code>SComponent</code> displayed in that position
     */
    public SComponent getTopComponent() {
        return leftComponent;
    }

    /**
     * Sets the component to the right (or below) the divider.
     *
     * @param comp the <code>SComponent</code> to display in that position
     * @beaninfo
     *    preferred: true
     *  description: The component to the right (or below) the divider.
     */
    public void setRightComponent(SComponent comp) {
        if (comp == null) {
            if (rightComponent != null) {
                remove(rightComponent);
                rightComponent = null;
            }
        } else {
            add(comp, SSplitPane.RIGHT);
        }
    }

    /**
     * Returns the component to the right (or below) the divider.
     *
     * @return the <code>SComponent</code> displayed in that position
     */
    public SComponent getRightComponent() {
        return rightComponent;
    }

    /**
     * Sets the component below, or to the right of the divider.
     *
     * @param comp the <code>SComponent</code> to display in that position
     * @beaninfo
     *  description: The component below, or to the right of the divider.
     */
    public void setBottomComponent(SComponent comp) {
        setRightComponent(comp);
    }

    /**
     * Returns the component below, or to the right of the divider.
     *
     * @return the <code>SComponent</code> displayed in that position
     */
    public SComponent getBottomComponent() {
        return rightComponent;
    }

    /**
     * Sets the orientation, or how the splitter is divided. The options
     * are:<ul>
     * <li>JSplitPane.VERTICAL_SPLIT  (above/below orientation of components)
     * <li>JSplitPane.HORIZONTAL_SPLIT  (left/right orientation of components)
     * </ul>
     *
     * @param orientation an integer specifying the orientation
     * @exception IllegalArgumentException if orientation is not one of:
     *        HORIZONTAL_SPLIT or VERTICAL_SPLIT.
     * @beaninfo
     *        bound: true
     *  description: The orientation, or how the splitter is divided.
     *         enum: HORIZONTAL_SPLIT JSplitPane.HORIZONTAL_SPLIT
     *               VERTICAL_SPLIT   JSplitPane.VERTICAL_SPLIT
     */
    public void setOrientation(int orientation) {
        if ((orientation != VERTICAL_SPLIT) &&
            (orientation != HORIZONTAL_SPLIT)) {
           throw new IllegalArgumentException("SSplitPane: orientation must " +
                                              "be one of " +
                                              "SSplitPane.VERTICAL_SPLIT or " +
                                              "SSplitPane.HORIZONTAL_SPLIT");
        }

        int oldOrientation = this.orientation;

        this.orientation = orientation;
        reloadIfChange(oldOrientation, orientation);
    }

    /**
     * Returns the orientation.
     *
     * @return an integer giving the orientation
     * @see #setOrientation
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * Sets the value of the <code>continuousLayout</code> property,
     * which must be <code>true</code> for the child components
     * to be continuously
     * redisplayed and laid out during user intervention.
     * The default value of this property is <code>false</code>.
     * Some look and feels might not support continuous layout;
     * they will ignore this property.
     *
     * @param newContinuousLayout  <code>true</code> if the components
     *        should continuously be redrawn as the divider changes position
     * @beaninfo
     *        bound: true
     *  description: Whether the child components are
     *               continuously redisplayed and laid out during
     *               user intervention.
     * @see #isContinuousLayout
     */
    public void setContinuousLayout(boolean newContinuousLayout) {
        boolean           oldCD = continuousLayout;

        continuousLayout = newContinuousLayout;
        reloadIfChange(oldCD, newContinuousLayout);
    }

    /**
     * Gets the <code>continuousLayout</code> property.
     *
     * @return the value of the <code>continuousLayout</code> property
     * @see #setContinuousLayout
     */
    public boolean isContinuousLayout() {
        return continuousLayout;
    }

    /**
     * Specifies how to distribute extra space when the size of the split pane
     * changes. A value of 0, the default,
     * indicates the right/bottom component gets all the extra space (the
     * left/top component acts fixed), where as a value of 1 specifies the
     * left/top component gets all the extra space (the right/bottom component
     * acts fixed). Specifically, the left/top component gets (weight * diff)
     * extra space and the right/bottom component gets (1 - weight) * diff
     * extra space.
     *
     * @param value as described above
     * @exception IllegalArgumentException if <code>value</code> is < 0 or > 1
     * @since 1.3
     * @beaninfo
     *        bound: true
     *  description: Specifies how to distribute extra space when the split pane
     *               resizes.
     */
    public void setResizeWeight(double value) {
	if (value < 0 || value > 1) {
	    throw new IllegalArgumentException("JSplitPane weight must be between 0 and 1");
	}
	double         oldWeight = resizeWeight;

	resizeWeight = value;
	reloadIfChange(oldWeight, value);
    }

    /**
     * Returns the number that determines how extra space is distributed.
     * @return how extra space is to be distributed on a resize of the
     *         split pane
     * @since 1.3
     */
    public double getResizeWeight() {
        return resizeWeight;
    }

    /**
     * Lays out the <code>JSplitPane</code> layout based on the preferred size
     * of the children components. This will likely result in changing
     * the divider location.
     */
    public void resetToPreferredSizes() {
        // TODO: what shall we do here?
    }

    /**
     * Sets the location of the divider. This is passed off to the
     * look and feel implementation, and then listeners are notified. A value
     * less than 0 implies the divider should be reset to a value that
     * attempts to honor the preferred size of the left/top component.
     * After notifying the listeners, the last divider location is updated,
     * via <code>setLastDividerLocation</code>.
     *
     * @param location an int specifying a UI-specific value (typically a
     *        pixel count)
     * @beaninfo
     *        bound: true
     *  description: The location of the divider.
     */
    public void setDividerLocation(int location) {
        int oldValue = dividerLocation;
        dividerLocation = location;
        reloadIfChange(oldValue, dividerLocation);
    }

    /**
     * Returns the last value passed to <code>setDividerLocation</code>.
     * The value returned from this method may differ from the actual
     * divider location (if <code>setDividerLocation</code> was passed a
     * value bigger than the curent size).
     *
     * @return an integer specifying the location of the divider
     */
    public int getDividerLocation() {
        return dividerLocation;
    }

    /**
     * Removes the child component, <code>component</code> from the
     * pane. Resets the <code>leftComponent</code> or
     * <code>rightComponent</code> instance variable, as necessary.
     *
     * @param component the <code>SComponent</code> to remove
     */
    public void remove(SComponent component) {
        if (component == leftComponent) {
            leftComponent = null;
        } else if (component == rightComponent) {
            rightComponent = null;
        }
        super.remove(component);
    }


    /**
     * Removes the <code>SComponent</code> at the specified index.
     * Updates the <code>leftComponent</code> and <code>rightComponent</code>
     * instance variables as necessary, and then messages super.
     *
     * @param index an integer specifying the component to remove, where
     *        1 specifies the left/top component and 2 specifies the
     *        bottom/right component
     */
    public void remove(int index) {
        SComponent    comp = getComponent(index);

        if (comp == leftComponent) {
            leftComponent = null;
        } else if (comp == rightComponent) {
            rightComponent = null;
        }
        super.remove(index);
    }


    /**
     * Removes all the child components from the split pane. Resets the
     * <code>leftComonent</code> and <code>rightComponent</code>
     * instance variables.
     */
    public void removeAll() {
        leftComponent = rightComponent = null;
        super.removeAll();
    }

    /**
     * Adds the specified component to this split pane.
     * If <code>constraints</code> identifies the left/top or
     * right/bottom child component, and a component with that identifier
     * was previously added, it will be removed and then <code>comp</code>
     * will be added in its place. If <code>constraints</code> is not
     * one of the known identifiers the layout manager may throw an
     * <code>IllegalArgumentException</code>.
     * <p>
     * The possible constraints objects (Strings) are:
     * <ul>
     * <li>JSplitPane.TOP
     * <li>JSplitPane.LEFT
     * <li>JSplitPane.BOTTOM
     * <li>JSplitPane.RIGHT
     * </ul>
     * If the <code>constraints</code> object is <code>null</code>,
     * the component is added in the
     * first available position (left/top if open, else right/bottom).
     *
     * @param comp        the component to add
     * @param constraints an <code>Object</code> specifying the
     *			  layout constraints
     *                    (position) for this component
     * @param index       an integer specifying the index in the container's
     *                    list.
     * @exception IllegalArgumentException  if the <code>constraints</code>
     *		object does not match an existing component
     */
    public SComponent addComponent(SComponent comp, Object constraints, int index)
    {
        SComponent             toRemove;

        if (constraints != null && !(constraints instanceof String)) {
            throw new IllegalArgumentException("cannot add to layout: " +
                                               "constraint must be a string " +
                                               "(or null)");
        }

        /* If the constraints are null and the left/right component is
           invalid, add it at the left/right component. */
        if (constraints == null) {
            if (getLeftComponent() == null) {
                constraints = SSplitPane.LEFT;
            } else if (getRightComponent() == null) {
                constraints = SSplitPane.RIGHT;
            }
        }

        /* Find the SComponent that already exists and remove it. */
        if (constraints != null && (constraints.equals(SSplitPane.LEFT) ||
                                   constraints.equals(SSplitPane.TOP))) {
            toRemove = getLeftComponent();
            if (toRemove != null) {
                remove(toRemove);
            }
            leftComponent = comp;
            index = -1;
        } else if (constraints != null &&
                   (constraints.equals(SSplitPane.RIGHT) ||
                    constraints.equals(SSplitPane.BOTTOM))) {
            toRemove = getRightComponent();
            if (toRemove != null) {
                remove(toRemove);
            }
            rightComponent = comp;
            index = -1;
        } else if (constraints != null &&
                constraints.equals(SSplitPane.DIVIDER)) {
            index = -1;
        }
        /* LayoutManager should raise for else condition here. */

        super.addComponent(comp, constraints, index);

        return comp;
    }


    /**
     * Returns a string representation of this <code>SSplitPane</code>.
     * This method
     * is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not
     * be <code>null</code>.
     *
     * @return  a string representation of this <code>SSplitPane</code>.
     */
    protected String paramString() {
        String orientationString = (orientation == HORIZONTAL_SPLIT ?
                                    "HORIZONTAL_SPLIT" : "VERTICAL_SPLIT");
        String continuousLayoutString = (continuousLayout ?
                                         "true" : "false");

        return super.paramString() +
        ",continuousLayout=" + continuousLayoutString +
        ",dividerSize=" + dividerSize +
        ",orientation=" + orientationString;
    }

    public void setCG(SplitPaneCG cg) {
        super.setCG(cg);
    }


    public void processLowLevelEvent(String name, String[] values) {
        this.newLocation = new Integer(values[0]);
        SForm.addArmedComponent(this);
    }

    public void fireIntermediateEvents() {
        if (newLocation != -1)
            dividerLocation = newLocation;
    }

    public boolean isEpochCheckEnabled() {
        return false;
    }
}
