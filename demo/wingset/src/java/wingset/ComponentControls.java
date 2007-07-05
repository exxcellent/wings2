/*
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
package wingset;

import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SCheckBox;
import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDefaultListCellRenderer;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SGridBagLayout;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.STextField;
import org.wings.SToolBar;
import org.wings.border.*;
import org.wings.session.SessionManager;
import org.wings.style.CSSProperty;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

/**
 * A visual control used in many WingSet demos.
 *
 * @author hengels
 */
public class ComponentControls  extends SPanel {
    protected static final Object[] BORDERS = new Object[] {
        new Object[] { "default", SDefaultBorder.INSTANCE },
        new Object[] { "none",    null },
        new Object[] { "raised",  new SBevelBorder(SBevelBorder.RAISED) },
        new Object[] { "lowered", new SBevelBorder(SBevelBorder.LOWERED) },
        new Object[] { "line",    new SLineBorder(2) },
        new Object[] { "grooved", new SEtchedBorder(SEtchedBorder.LOWERED) },
        new Object[] { "ridged",  new SEtchedBorder(SEtchedBorder.RAISED) },
        new Object[] { "titled",  new STitledBorder("Border Title") },
        new Object[] { "empty",   new SEmptyBorder(5,5,5,5)}
    };

    protected static final Object[] COLORS = new Object[] {
        new Object[] { "none",   null },
        new Object[] { "gray",   Color.GRAY},
        new Object[] { "yellow", new Color(255, 255, 100) },
        new Object[] { "red",    new Color(255, 100, 100) },
        new Object[] { "green",  new Color(100, 255, 100) },
        new Object[] { "blue",   new Color(100, 100, 255) },
    };

    protected static final Object[] FONTS = new Object[] {
        new Object[] { "default font",         null },
        new Object[] { "serif italic bold",    new SFont("Times,Times New Roman,serif", SFont.ITALIC | SFont.BOLD, 10)},
        new Object[] { "serif",                new SFont("Times,Times New Roman,serif", SFont.PLAIN, SFont.DEFAULT_SIZE) },
        new Object[] { "16 sans bold",         new SFont("Arial,sans-serif", SFont.BOLD, 16)},
        new Object[] { "24 fantasy italic",    new SFont("Comic,Comic Sans MS,fantasy", SFont.ITALIC, 24) }
    };

    protected final List components = new LinkedList();

    protected final SToolBar globalControls = new SToolBar();
    protected final SToolBar localControls = new SToolBar();

    protected final SCheckBox ajaxCheckBox = new SCheckBox("<html>AJAX&nbsp;");

    protected final STextField widthTextField = new STextField();
    protected final STextField heightTextField = new STextField();
    protected final STextField insetsTextField = new STextField();

    protected final SComboBox borderStyleComboBox = new SComboBox(BORDERS);
    protected final SComboBox borderColorComboBox = new SComboBox(COLORS);
    protected final STextField borderThicknessTextField = new STextField();

    protected final SComboBox backgroundComboBox = new SComboBox(COLORS);
    protected final SComboBox foregroundComboBox = new SComboBox(COLORS);
    protected final SComboBox fontComboBox = new SComboBox(FONTS);
    protected final SCheckBox formComponentCheckBox = new SCheckBox("as form");

    private SLabel placeHolder = new SLabel("<html>&nbsp;");

    public ComponentControls() {
        super(new SGridBagLayout());
        setBackground(new Color(240,240,240));
        setPreferredSize(SDimension.FULLWIDTH);

        SBorder border = new SLineBorder(1, new Insets(0, 3, 0, 6));
        border.setColor(new Color(255, 255, 255), SConstants.TOP);
        border.setColor(new Color(255, 255, 255), SConstants.LEFT);
        border.setColor(new Color(190, 190, 190), SConstants.RIGHT);
        border.setColor(new Color(190, 190, 190), SConstants.BOTTOM);
        globalControls.setBorder(border);
        globalControls.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        ((SBoxLayout)globalControls.getLayout()).setHgap(6);
        ((SBoxLayout)globalControls.getLayout()).setVgap(4);

        border = new SLineBorder(1, new Insets(0, 3, 0, 6));
        border.setColor(new Color(255, 255, 255), SConstants.TOP);
        border.setColor(new Color(255, 255, 255), SConstants.LEFT);
        border.setColor(new Color(190, 190, 190), SConstants.RIGHT);
        border.setColor(new Color(190, 190, 190), SConstants.BOTTOM);
        localControls.setBorder(border);
        localControls.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        ((SBoxLayout)localControls.getLayout()).setHgap(6);
        ((SBoxLayout)localControls.getLayout()).setVgap(4);

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        add(globalControls, c);
        add(localControls, c);

        ajaxCheckBox.setSelected(SessionManager.getSession().getRootFrame().isUpdateEnabled());
        widthTextField.setColumns(3);
        widthTextField.setToolTipText("length with unit (example: '200px')");
        heightTextField.setColumns(3);
        heightTextField.setToolTipText("length with unit (example: '200px')");
        insetsTextField.setColumns(1);
        insetsTextField.setToolTipText("length only (example: '8')\n(applies to the border!)");
        borderThicknessTextField.setColumns(1);
        borderThicknessTextField.setToolTipText("length only (example: '2')");
        borderStyleComboBox.setRenderer(new ObjectPairCellRenderer());
        borderColorComboBox.setRenderer(new ObjectPairCellRenderer());
        backgroundComboBox.setRenderer(new ObjectPairCellRenderer());
        foregroundComboBox.setRenderer(new ObjectPairCellRenderer());
        fontComboBox.setRenderer(new ObjectPairCellRenderer());
        formComponentCheckBox.setToolTipText("show as form component .. i.e. trigger form submission");

        globalControls.add(ajaxCheckBox);
        globalControls.add(new SLabel("width"));
        globalControls.add(widthTextField);
        globalControls.add(new SLabel(" height"));
        globalControls.add(heightTextField);
        globalControls.add(new SLabel(" border"));
        globalControls.add(borderThicknessTextField);
        globalControls.add(borderStyleComboBox);
        globalControls.add(borderColorComboBox);
        globalControls.add(new SLabel("insets"));
        globalControls.add(insetsTextField);
        globalControls.add(new SLabel(" color"));
        globalControls.add(foregroundComboBox);
        globalControls.add(backgroundComboBox);
        globalControls.add(new SLabel(" font"));
        globalControls.add(fontComboBox);
        globalControls.add(new SLabel(""));
        globalControls.add(formComponentCheckBox);

        localControls.add(placeHolder);

        ActionListener ajaxListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ajax();
            }
        };
        ajaxCheckBox.addActionListener(ajaxListener);

        ActionListener preferredSizeListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                preferredSize();
            }
        };
        widthTextField.addActionListener(preferredSizeListener);
        heightTextField.addActionListener(preferredSizeListener);

        ActionListener borderListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                border();
            }
        };
        borderThicknessTextField.addActionListener(borderListener);
        borderStyleComboBox.addActionListener(borderListener);
        borderColorComboBox.addActionListener(borderListener);
        insetsTextField.addActionListener(borderListener);

        ActionListener foregroundListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                foreground();
            }
        };
        foregroundComboBox.addActionListener(foregroundListener);

        ActionListener backgroundListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                background();
            }
        };
        backgroundComboBox.addActionListener(backgroundListener);

        ActionListener fontListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                font();
            }
        };
        fontComboBox.addActionListener(fontListener);

        ActionListener showAsFormComponentListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                showAsFormComponent();
            }
        };
        formComponentCheckBox.addActionListener(showAsFormComponentListener);
    }

    void ajax() {
        SessionManager.getSession().getRootFrame().setUpdateEnabled(ajaxCheckBox.isSelected());
    }

    void preferredSize() {
        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            SComponent component = (SComponent) iterator.next();

            SDimension preferredSize = new SDimension();
            preferredSize.setWidth(widthTextField.getText());
            preferredSize.setHeight(heightTextField.getText());

            component.setPreferredSize(preferredSize);
        }
    }

    void border() {
        int insets = 0;
        try {
            insets = Integer.parseInt(insetsTextField.getText());
        }
        catch (NumberFormatException e) {}

        int borderThickness = 1;
        try {
            borderThickness = Integer.parseInt(borderThicknessTextField.getText());
        }
        catch (NumberFormatException e) {}

        SAbstractBorder border = (SAbstractBorder) getSelectedObject(borderStyleComboBox);
        if (border != null && border != SDefaultBorder.INSTANCE) {
            border.setColor((Color)getSelectedObject(borderColorComboBox));
            border.setInsets(new Insets(insets, insets, insets, insets));
            border.setThickness(borderThickness);
        }

        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            SComponent component = (SComponent) iterator.next();
            component.setBorder(border != null ? (SBorder)border.clone() : null);
        }
    }

    void background() {
        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            SComponent component = (SComponent) iterator.next();
            component.setBackground((Color)getSelectedObject(backgroundComboBox));
        }
    }

    void foreground() {
        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            SComponent component = (SComponent) iterator.next();
            component.setForeground((Color)getSelectedObject(foregroundComboBox));
        }
    }

    void font() {
        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            SComponent component = (SComponent) iterator.next();
            component.setFont((SFont) getSelectedObject(fontComboBox));
        }
    }

    void showAsFormComponent() {
        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            SComponent component = (SComponent) iterator.next();
            component.setShowAsFormComponent(formComponentCheckBox.isSelected());
        }
    }

    protected Object getSelectedObject(SComboBox combo) {
        return combo.getSelectedIndex() != -1 ? ((Object[])combo.getSelectedItem())[1] : null;
    }

    public void removeGlobalControl(SComponent control) {
        int index = Arrays.asList(globalControls.getComponents()).indexOf(control);
        if (index >= 0) {
            globalControls.remove(index);    // comp
            if (globalControls.getComponent(index - 1) instanceof SLabel)
                globalControls.remove(index-1);  // label
        }
    }

    public void addControl(SComponent component) {
        if (localControls.getComponent(0) == placeHolder)
            localControls.removeAll();
        localControls.add(component);
    }

    public void addControllable(SComponent component) {
        components.add(component);
    }

    public List getControllables() {
        return components;
    }

    /**
     * Renderer which expects <code>Object[]</code> values and returns the first value
     */
    protected static class ObjectPairCellRenderer extends SDefaultListCellRenderer {
        public SComponent getListCellRendererComponent(SComponent list, Object value, boolean selected, int row) {
            Object[] objects = (Object[])value;
            value = objects[0];
            return super.getListCellRendererComponent(list, value, selected, row);
        }
    }
}

/*
        SButton switchDebugViewButton = new SButton("Toggle AJAX debug view");
        switchStyleButton.setShowAsFormComponent(false);
        switchDebugViewButton.addScriptListener(new JavaScriptListener(
                JavaScriptEvent.ON_CLICK,
                "wingS.ajax.toggleDebugView(); return false;"
        ));
*/