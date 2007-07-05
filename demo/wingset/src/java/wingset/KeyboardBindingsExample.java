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

import org.wings.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;

/**
 * @author Holger Engels
 */
public class KeyboardBindingsExample extends WingSetPane {
    private final SLabel actionEventLabel = new SLabel();
    private final STextField textField = new STextField();
    private final SPanel panel = new KeyboardPanel(new SFlowDownLayout());

    public KeyboardBindingsExample() {

        /**
         * Input/Action map locally for the textfield
         */
        final InputMap textfieldInputMap = new InputMap();
        textfieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0, false), "F1");
        textfieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0, false), "F2");
        textfieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0, false), "F3");
        textfieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0, false), "F4");
        textfieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0, false), "F5");
        textfieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0, false), "F6");
        textfieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0, false), "F7");
        textfieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0, false), "F8");
        textfieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0, false), "F9");
        textfieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0, false), "F10");

        /**
         * Input/Action map locally for the SForm element
         */
        final InputMap formInputMap = new InputMap();
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, KeyEvent.SHIFT_DOWN_MASK, false), "Shift F1");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, KeyEvent.SHIFT_DOWN_MASK, false), "Shift F2");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, KeyEvent.SHIFT_DOWN_MASK, false), "Shift F3");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.SHIFT_DOWN_MASK, false), "Shift F4");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, KeyEvent.SHIFT_DOWN_MASK, false), "Shift F5");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, KeyEvent.SHIFT_DOWN_MASK, false), "Shift F6");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, KeyEvent.SHIFT_DOWN_MASK, false), "Shift F7");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F8, KeyEvent.SHIFT_DOWN_MASK, false), "Shift F8");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, KeyEvent.SHIFT_DOWN_MASK, false), "Shift F9");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, KeyEvent.SHIFT_DOWN_MASK, false), "Shift F10");

        /**
         * Input/Action map globally on the whole dialog/page, but assigned via the textfield
         */
        final InputMap pageInputMap = new InputMap();
        pageInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, KeyEvent.ALT_DOWN_MASK, false), "Alt F1");
        pageInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, KeyEvent.ALT_DOWN_MASK, false), "Alt F2");
        pageInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, KeyEvent.ALT_DOWN_MASK, false), "Alt F3");
        pageInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK, false), "Alt F4");
        pageInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, KeyEvent.ALT_DOWN_MASK, false), "Alt F5");
        pageInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, KeyEvent.ALT_DOWN_MASK, false), "Alt F6");
        pageInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, KeyEvent.ALT_DOWN_MASK, false), "Alt F7");
        pageInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F8, KeyEvent.ALT_DOWN_MASK, false), "Alt F8");
        pageInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, KeyEvent.ALT_DOWN_MASK, false), "Alt F9");
        pageInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, KeyEvent.ALT_DOWN_MASK, false), "Alt F10");

        final Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                actionEventLabel.setText(e.getActionCommand());
            }
        };

        final ActionMap actionMap = new ActionMap();
        actionMap.put("F1", action);
        actionMap.put("F2", action);
        actionMap.put("F3", action);
        actionMap.put("F4", action);
        actionMap.put("F5", action);
        actionMap.put("F6", action);
        actionMap.put("F7", action);
        actionMap.put("F8", action);
        actionMap.put("F9", action);
        actionMap.put("F10", action);
        actionMap.put("Shift F1", action);
        actionMap.put("Shift F2", action);
        actionMap.put("Shift F3", action);
        actionMap.put("Shift F4", action);
        actionMap.put("Shift F5", action);
        actionMap.put("Shift F6", action);
        actionMap.put("Shift F7", action);
        actionMap.put("Shift F8", action);
        actionMap.put("Shift F9", action);
        actionMap.put("Shift F10", action);
        actionMap.put("Alt F1", action);
        actionMap.put("Alt F2", action);
        actionMap.put("Alt F3", action);
        actionMap.put("Alt F4", action);
        actionMap.put("Alt F5", action);
        actionMap.put("Alt F6", action);
        actionMap.put("Alt F7", action);
        actionMap.put("Alt F8", action);
        actionMap.put("Alt F9", action);
        actionMap.put("Alt F10", action);

        // Define key bindinings only if focus inside textfield
        textField.setInputMap(textfieldInputMap);
        textField.setActionMap(actionMap);

        // Define key bindings on the textfield, valid all over the page
        textField.setInputMap(WHEN_IN_FOCUSED_FRAME, pageInputMap);

        // Define key bindinings if focus inside the containing SForm
        panel.setInputMap(formInputMap);
             // equal to :  panel.setInputMap(formInputMap, WHEN_FOCUSED_OR_ANCESTOR_OF_FOCUSED_COMPONENT);
        panel.setActionMap(actionMap);

        final SLabel titleLabel = new SLabel("wingS key binding feature demonstration\n ");
        titleLabel.setFont(new SFont("sans-serif", SFont.PLAIN, 16));
        panel.add(titleLabel);

        final SLabel instructions = new SLabel(
                "This page demonstrates the feature of attaching key binding to specific components. " +
                        "Some components provide already default bindings like STabbedPane (try [Alt]-[LEFT] " +
                        "or [Alt]-[RIGHT].\n" +
                        "\n" +
                        "On this page The keys [F1] through [F10] are captured locally by the STextField. This means " +
                        "the keypress will only trigger an event for thesekeys if the focus is inside the textfield." +
                        "\n" +
                        "[Shift]-[F1] through [Shift]-[F10] are attached up to the containing SForm, so these keys should " +
                        "trigger events if the focus is inside the textfield or on the checkbox.\n" +
                        "\n" +
                        "[Alt]-[F1] through [Alt]-[F10] are also attached to the textfield but with global focus, hence " +
                        "captured by the whole page, then routed back to the STextField. These shortcuts will work " +
                        "everythere on the page.\n\n");
        instructions.setWordWrap(true);
        panel.add(instructions);

        panel.add(textField);
        panel.add(new SCheckBox("Checkbox: Use as alternative focus point"));

        panel.add(actionEventLabel);
        actionEventLabel.setForeground(Color.RED);

        panel.setHorizontalAlignment(CENTER);
        panel.setPreferredSize(new SDimension(550, SDimension.AUTO_INT));
    }


    protected SComponent createControls() {
        return null;
    }

    public SComponent createExample() {
        return panel;
    }

    private static class KeyboardPanel
        extends SPanel
        implements LowLevelEventListener
    {
        public KeyboardPanel(SLayoutManager l) {
            super(l);
        }

        public void processLowLevelEvent(String name, String[] values) {
            processKeyEvents(values);
        }

        public void fireIntermediateEvents() {
        }

        public boolean isEpochCheckEnabled() {
            return true;
        }
    }
}
