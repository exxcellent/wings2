package wingset;

import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SForm;
import org.wings.SGridLayout;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SDimension;
import org.wings.SButton;
import org.wings.SFlowDownLayout;
import org.wings.border.SEmptyBorder;
import org.wings.script.JavaScriptListener;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author <a href="mailto:B.Schmid@eXXcellent.de">Benjamin Schmid</a>
 */
public class TableNestingExample  extends WingSetPane{

    private final SComboBox selectComboBox = new SComboBox();
    private final SForm mainPanel = new SForm(new SGridLayout(1));
    private Color[] colors = new Color[] { Color.red, Color.green, Color.yellow};

    protected SComponent createExample() {
        selectComboBox.addScriptListener(JavaScriptListener.JS_ON_CHANGE_SUBMIT_FORM);
        for(int i = 5; i< 30; i++) {
            selectComboBox.addItem(new Integer(i));
        }
        selectComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        selectComboBox.setSelectedIndex(0);
        update();
        return mainPanel;
    }

    private void update() {
        mainPanel.removeAll();
        mainPanel.add(new SLabel("Below you will see panels with GridLayout nested into panels.\n" +
                "You will realize that the browsers stop rendering at a specific level of table nesting.\n" +
                "(in IE try 9 vs. 10; in FireFox 20 vs. 21"));
        mainPanel.add(selectComboBox);
        mainPanel.add(nestPanel(((Integer)selectComboBox.getSelectedItem()).intValue()));

        mainPanel.add(new SLabel("\nPadding behaviour test for MSIE. (Workaround for missing " +
                "PADDING support on TABLE elements.)"));

        SPanel buttonPanel = new SPanel(new SFlowDownLayout());
        buttonPanel.setBackground(new  Color(0xD0, 0xD0, 0xFF));
        buttonPanel.setPreferredSize(new SDimension(150,-1));
        buttonPanel.add(createButton(0, "Root"));
        buttonPanel.add(createButton(1, "Menu 1"));
        buttonPanel.add(createButton(1, "Menu 2"));
        buttonPanel.add(createButton(2, "Menu 2a"));
        buttonPanel.add(createButton(2, "Menu 2b"));
        buttonPanel.add(createButton(1, "Menu 3"));
        mainPanel.add(buttonPanel);


        SPanel labelPanel = new SPanel(new SFlowDownLayout());
        labelPanel.setBackground(new  Color(0xD0, 0xFF, 0xD0));
        labelPanel.setPreferredSize(new SDimension(150,-1));
        labelPanel.add(createLabel(0, "Root"));
        labelPanel.add(createLabel(1, "Menu 1"));
        labelPanel.add(createLabel(1, "Menu 2"));
        labelPanel.add(createLabel(2, "Menu 2a"));
        labelPanel.add(createLabel(2, "Menu 2b"));
        labelPanel.add(createLabel(1, "Menu 3"));
        mainPanel.add(labelPanel);

    }

    private SPanel nestPanel(int depth) {
        SPanel panel = new SPanel(new SGridLayout());
        panel.add(new SLabel ("Depth "+depth));
        panel.setBackground(colors[depth % colors.length]);
        panel.setBorder(new SEmptyBorder(20,20,20,20));
        if (depth > 1)
            panel.add(nestPanel(depth-1));
        return panel;
    }

    private SButton createButton(int level, String text) {
        SButton button = new SButton(text);
        //button.setShowAsFormComponent(false);
        button.setBorder(new SEmptyBorder(0, level*20, 0, 0));
        return button;
    }

    private SLabel createLabel(int level, String text) {
        SLabel button = new SLabel(text);
        button.setBorder(new SEmptyBorder(0, level*20, 0, 0));
        return button;
    }

}
