/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package wingset;

import org.wings.SComponent;
import org.wings.SForm;
import org.wings.SLabel;
import org.wings.STextField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class KeyboardBindingsExample extends WingSetPane {
    private SLabel label = new SLabel();
    private STextField textField = new STextField();
    private SForm form = new SForm();

    public SComponent createExample() {
        InputMap inputMap = new InputMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0, false), "f1");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0, false), "f2");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0, false), "f3");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0, false), "f4");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0, false), "f5");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0, false), "f6");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0, false), "f7");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0, false), "f8");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0, false), "f9");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0, false), "f10");

        InputMap formInputMap = new InputMap();
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, KeyEvent.SHIFT_DOWN_MASK, false), "shift f1");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, KeyEvent.SHIFT_DOWN_MASK, false), "shift f2");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, KeyEvent.SHIFT_DOWN_MASK, false), "shift f3");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.SHIFT_DOWN_MASK, false), "shift f4");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, KeyEvent.SHIFT_DOWN_MASK, false), "shift f5");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, KeyEvent.SHIFT_DOWN_MASK, false), "shift f6");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, KeyEvent.SHIFT_DOWN_MASK, false), "shift f7");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F8, KeyEvent.SHIFT_DOWN_MASK, false), "shift f8");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, KeyEvent.SHIFT_DOWN_MASK, false), "shift f9");
        formInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, KeyEvent.SHIFT_DOWN_MASK, false), "shift f10");


        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                label.setText(e.getActionCommand());
            }
        };

        ActionMap actionMap = new ActionMap();
        actionMap.put("f1", action);
        actionMap.put("f2", action);
        actionMap.put("f3", action);
        actionMap.put("f4", action);
        actionMap.put("f5", action);
        actionMap.put("f6", action);
        actionMap.put("f7", action);
        actionMap.put("f8", action);
        actionMap.put("f9", action);
        actionMap.put("f10", action);
        actionMap.put("shift f1", action);
        actionMap.put("shift f2", action);
        actionMap.put("shift f3", action);
        actionMap.put("shift f4", action);
        actionMap.put("shift f5", action);
        actionMap.put("shift f6", action);
        actionMap.put("shift f7", action);
        actionMap.put("shift f8", action);
        actionMap.put("shift f9", action);
        actionMap.put("shift f10", action);

        textField.setInputMap(inputMap);
        textField.setActionMap(actionMap);

        form.setInputMap(formInputMap);
        form.setActionMap(actionMap);

        form.add(new SLabel("<html>f1 through f10 are captured by the STextField<br/>" +
                "shift f1 through shift f10 are bubbling up to the containing SForm"));
        form.add(textField);
        form.add(label);

        return form;
    }
}
