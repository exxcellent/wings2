/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
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
public class KeyboardBindingsExample extends WingSetPane
{
    public SComponent createExample() {
        STextField textField = new STextField();
        InputMap inputMap = new InputMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "f10");
        textField.setInputMap(inputMap);

        final SLabel label = new SLabel();

        SForm form = new SForm();
        inputMap = new InputMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, KeyEvent.SHIFT_DOWN_MASK), "shift f10");
        form.setInputMap(inputMap);
        
        form.add(textField);
        form.add(label);

        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                label.setText(e.getActionCommand());
            }
        };

        ActionMap actionMap = new ActionMap();
        actionMap.put("f10", action);
        actionMap.put("shift f10", action);

        textField.setActionMap(actionMap);
        form.setActionMap(actionMap);

        return form;
    }
}
