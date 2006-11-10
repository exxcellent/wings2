/*
 * $Id$
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
package wingset;

import org.wings.ReloadManager;
import org.wings.SAnchor;
import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SCheckBox;
import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDefaultListCellRenderer;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SFrame;
import org.wings.SGridBagLayout;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.STextField;
import org.wings.SToolBar;
import org.wings.border.*;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;
import org.wings.session.SessionManager;

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
 * @version $Revision$
 */
public class ComponentControls  extends SPanel {
    protected static final Object[] BORDERS = new Object[] {
        new Object[] { "none",    null },
        new Object[] { "raised",  new SBevelBorder(SBevelBorder.RAISED) },
        new Object[] { "lowered", new SBevelBorder(SBevelBorder.LOWERED) },
        new Object[] { "line",    new SLineBorder(2) },
        new Object[] { "grooved", new SEtchedBorder(SEtchedBorder.LOWERED) },
        new Object[] { "ridged",  new SEtchedBorder(SEtchedBorder.RAISED) },
        new Object[] { "titled",  new STitledBorder(new SEtchedBorder(SEtchedBorder.LOWERED), "Border Title") },
        new Object[] { "empty",   new SEmptyBorder(5,5,5,5)}
    };

    protected static final Object[] COLORS = new Object[] {
        new Object[] { "none",   null },
        new Object[] { "yellow", new Color(255, 255, 100) },
        new Object[] { "red",    new Color(255, 100, 100) },
        new Object[] { "green",  new Color(100, 255, 100) },
        new Object[] { "blue",   new Color(100, 100, 255) },
    };

    protected static final Object[] FONTS = new Object[] {
        new Object[] { "default font",   null },
        new Object[] { "16pt sans-serif bold & italic", new SFont("Arial,sans-serif",SFont.BOLD+SFont.ITALIC, 16)},
        new Object[] { "default serif plain",    new SFont("Times, Times New Roman,serif",SFont.DEFAULT_SIZE, SFont.PLAIN) },
        new Object[] { "24pt fantasy italic",    new SFont("Comic,Comic Sans MS,fantasy",SFont.ITALIC, 24) }
    };

    protected final List components = new LinkedList();

    protected final SToolBar globalControls = new SToolBar();
    protected final SToolBar ajaxControls = new SToolBar();
    protected final SToolBar localControls = new SToolBar();

    protected final SButton applyButton;

    protected final STextField widthTextField = new STextField();
    protected final STextField heightTextField = new STextField();
    protected final STextField insetsTextField = new STextField();

    protected final SComboBox borderStyleComboBox = new SComboBox(BORDERS);
    protected final SComboBox borderColorComboBox = new SComboBox(COLORS);
    protected final STextField borderThicknessTextField = new STextField();

    protected final SComboBox backgroundComboBox = new SComboBox(COLORS);
    protected final SComboBox foregroundComboBox = new SComboBox(COLORS);
    protected final SComboBox fontComboBox = new SComboBox(FONTS);
    protected final SCheckBox formComponentCheckBox = new SCheckBox("form components");

    protected final SCheckBox ajaxEnabledCheckBox = new SCheckBox("Enable incremental updates");
    protected final SCheckBox ajaxHighlightEnabledCheckBox = new SCheckBox("Enable update highlight");
    protected final STextField ajaxHighlightColorTextField = new STextField();
    protected final SCheckBox ajaxCursorEnabledCheckBox = new SCheckBox("Enable update cursor");
    protected final SButton ajaxForceCompleteUpdate = new SButton("Force complete update");
    protected final SAnchor ajaxDebuggingViewAnchor = new SAnchor();


    protected final SFrame frame = SessionManager.getSession().getRootFrame();

    public ComponentControls() {
        super(new SGridBagLayout());
        setPreferredSize(SDimension.FULLWIDTH);
        SLineBorder border = new SLineBorder(Color.LIGHT_GRAY, 0);
        border.setThickness(1, SConstants.BOTTOM);
        setBorder(border);

        applyButton = new SButton("Apply");
        applyButton.setName("apply");
        applyButton.setActionCommand("apply");
        applyButton.setHorizontalAlignment(SConstants.CENTER_ALIGN);
        applyButton.setVerticalAlignment(SConstants.CENTER_ALIGN);

        border = new SLineBorder(Color.LIGHT_GRAY, 0);
        border.setThickness(1, SConstants.LEFT);
        globalControls.setBorder(border);
        globalControls.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        ((SBoxLayout)globalControls.getLayout()).setHgap(6);
        ((SBoxLayout)globalControls.getLayout()).setVgap(4);

        border = new SLineBorder(Color.LIGHT_GRAY, 0);
        border.setThickness(1, SConstants.LEFT);
        border.setThickness(1, SConstants.TOP);
        ajaxControls.setBorder(border);
        ajaxControls.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        ((SBoxLayout)ajaxControls.getLayout()).setHgap(6);
        ((SBoxLayout)ajaxControls.getLayout()).setVgap(4);

        border = new SLineBorder(Color.LIGHT_GRAY, 0);
        border.setThickness(1, SConstants.LEFT);
        border.setThickness(1, SConstants.TOP);
        localControls.setBorder(border);
        localControls.setVisible(false);
        localControls.setHorizontalAlignment(SConstants.LEFT_ALIGN);
        ((SBoxLayout)localControls.getLayout()).setHgap(6);
        ((SBoxLayout)localControls.getLayout()).setVgap(4);

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.gridheight = 3;
        add(applyButton, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        add(globalControls, c);
        add(ajaxControls, c);
        add(localControls, c);

        widthTextField.setColumns(3);
        widthTextField.setToolTipText("length with unit (example: '200px')");
        heightTextField.setColumns(3);
        heightTextField.setToolTipText("length with unit (example: '200px')");
        insetsTextField.setColumns(1);
        insetsTextField.setToolTipText("length only (example: '8')");
        borderThicknessTextField.setColumns(1);
        borderThicknessTextField.setToolTipText("length only (example: '2')");
        borderStyleComboBox.setRenderer(new ObjectPairCellRenderer());
        borderColorComboBox.setRenderer(new ObjectPairCellRenderer());
        backgroundComboBox.setRenderer(new ObjectPairCellRenderer());
        foregroundComboBox.setRenderer(new ObjectPairCellRenderer());
        fontComboBox.setRenderer(new ObjectPairCellRenderer());

        globalControls.add(new SLabel("width"));
        globalControls.add(widthTextField);
        globalControls.add(new SLabel(" height"));
        globalControls.add(heightTextField);
        globalControls.add(new SLabel(" border"));
        globalControls.add(borderThicknessTextField);
        globalControls.add(borderStyleComboBox);
        globalControls.add(borderColorComboBox);
        globalControls.add(new SLabel(" border insets"));
        globalControls.add(insetsTextField);
        globalControls.add(new SLabel(" foreground"));
        globalControls.add(foregroundComboBox);
        globalControls.add(new SLabel(" background"));
        globalControls.add(backgroundComboBox);
        globalControls.add(new SLabel(" font"));
        globalControls.add(fontComboBox);
        globalControls.add(new SLabel(""));
        globalControls.add(formComponentCheckBox);

        ajaxControls.add(new SLabel("AJAX settings:"));
        ajaxControls.add(ajaxEnabledCheckBox);
        ajaxControls.add(new SLabel("  |  "));
        ajaxControls.add(ajaxHighlightEnabledCheckBox);
        ajaxControls.add(ajaxHighlightColorTextField);
        ajaxControls.add(new SLabel("  |  "));
        ajaxControls.add(ajaxCursorEnabledCheckBox);
        ajaxControls.add(new SLabel("  |  "));
        ajaxControls.add(ajaxForceCompleteUpdate);
        ajaxControls.add(new SLabel("  |  "));
        ajaxControls.add(ajaxDebuggingViewAnchor);
        initAjaxSettings();


        addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                SDimension preferredSize = new SDimension();
                preferredSize.setWidth(widthTextField.getText());
                preferredSize.setHeight(heightTextField.getText());

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
                if (border != null) {
                    border.setColor((Color)getSelectedObject(borderColorComboBox));
                    border.setInsets(new Insets(insets, insets, insets, insets));
                    border.setThickness(borderThickness);
                }

                for (Iterator iterator = components.iterator(); iterator.hasNext();) {
                    SComponent component = (SComponent) iterator.next();
                    if (widthTextField.isVisible())
                        component.setPreferredSize(preferredSize);
                    if (borderThicknessTextField.isVisible())
                        component.setBorder(border != null ? (SBorder)border.clone() : null);
                    if (backgroundComboBox.isVisible())
                        component.setBackground((Color)getSelectedObject(backgroundComboBox));
                    if (foregroundComboBox.isVisible())
                        component.setForeground((Color)getSelectedObject(foregroundComboBox));
                    if (formComponentCheckBox.isVisible())
                        component.setShowAsFormComponent(formComponentCheckBox.isSelected());
                    if (fontComboBox.isVisible())
                        component.setFont((SFont) getSelectedObject(fontComboBox));
                }

                applyAjaxSettings();
            }
        });
    }

    protected Object getSelectedObject(SComboBox combo) {
        return combo.getSelectedIndex() != -1 ? ((Object[])combo.getSelectedItem())[1] : null;
    }

    public void removeGlobalControl(SComponent control) {
        int index = Arrays.asList(globalControls.getComponents()).indexOf(control);
        if (index >= 0) {
            globalControls.remove(index);    // comp
            globalControls.remove(index-1);  // label
        }
    }

    public void addControl(SComponent component) {
        localControls.add(component);
        localControls.setVisible(true);
    }

    public void addControllable(SComponent component) {
        components.add(component);
    }

    public List getControllables() {
        return components;
    }

    protected void addActionListener(ActionListener actionListener) {
        applyButton.addActionListener(actionListener);
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

    private void initAjaxSettings() {
        ajaxEnabledCheckBox.setSelected(frame.isIncrementalUpdateEnabled());
        boolean highlightEnabled = ((Boolean) frame.getIncrementalUpdateHighlight()[0]).booleanValue();
        ajaxHighlightEnabledCheckBox.setSelected(highlightEnabled);
        String color = (String) frame.getIncrementalUpdateHighlight()[1];
        ajaxHighlightColorTextField.setText(color);
        ajaxHighlightColorTextField.setColumns(4);
        boolean cursorEnabled = ((Boolean) frame.getIncrementalUpdateCursor()[0]).booleanValue();
        ajaxCursorEnabledCheckBox.setSelected(cursorEnabled);
        ajaxDebuggingViewAnchor.addScriptListener(new JavaScriptListener(
                JavaScriptEvent.ON_CLICK,
                "var debug = document.getElementById('ajaxDebugging');" +
                "if (debug == null) alert('The AJAX debugging view has not been enabled yet!');" +
                "else {" +
                "  if (debug.style.display == 'block') wingS.ajax.hideAjaxDebugging();" +
                "  else wingS.ajax.showAjaxDebugging();" +
                "}" +
                "return false;"
        ));
        ajaxForceCompleteUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.reload(ReloadManager.STATE);
            }
        });
        ajaxForceCompleteUpdate.setShowAsFormComponent(false);
        ajaxDebuggingViewAnchor.add(new SLabel("Toggle debugging view"));
    }

    private void applyAjaxSettings() {
        frame.setIncrementalUpdateEnabled(ajaxEnabledCheckBox.isSelected());
        boolean highlightEnabled = ajaxHighlightEnabledCheckBox.isSelected();
        String highlightColor = ajaxHighlightColorTextField.getText();
        frame.setIncrementalUpdateHighlight(highlightEnabled, highlightColor, 300);
        boolean cursorEnabled = ajaxCursorEnabledCheckBox.isSelected();
        SIcon image = (SIcon) frame.getIncrementalUpdateCursor()[1];
        int dx = ((Integer) frame.getIncrementalUpdateCursor()[2]).intValue();
        int dy = ((Integer) frame.getIncrementalUpdateCursor()[3]).intValue();
        frame.setIncrementalUpdateCursor(cursorEnabled, image, dx, dy);
    }
}
