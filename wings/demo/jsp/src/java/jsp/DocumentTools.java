/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package jsp;

import org.wings.SToolBar;
import org.wings.SButton;
import org.wings.SIcon;
import org.wings.SURLIcon;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author hengels
 */
public class DocumentTools
        extends SToolBar
{
    public DocumentTools() {
        add(new SButton(new Tool("cancel", "Cancel", new SURLIcon("../icons/cancel.png"))));
        add(new SButton(new Tool("revert", "Revert to Saved.", new SURLIcon("../icons/revert.png"))));
        add(new SButton(new Tool("save", "Save", new SURLIcon("../icons/save.png"))));
        add(new SButton(new Tool("finish", "Save and Finish", new SURLIcon("../icons/finish.png"))));
        add(new SButton(new Tool("delegate", "Delegate To", new SURLIcon("../icons/delegate.png"))));
        add(new SButton(new Tool("escalate", "Escalate", new SURLIcon("../icons/escalate.png"))));
        add(new SButton(new Tool("mail", "Mail Document", new SURLIcon("../icons/mail.png"))));
        add(new SButton(new Tool("print", "Print Document", new SURLIcon("../icons/print.png"))));
    }

    class Tool extends AbstractAction {
        public Tool(String name, String toolTip, SIcon icon) {
            putValue(AbstractAction.ACTION_COMMAND_KEY, name);
            if (icon == null)
                putValue(AbstractAction.NAME, toolTip);
            putValue(AbstractAction.SHORT_DESCRIPTION, toolTip);
            putValue(AbstractAction.SMALL_ICON, icon);
        }

        public void actionPerformed(ActionEvent e) {
        }
    }
}
