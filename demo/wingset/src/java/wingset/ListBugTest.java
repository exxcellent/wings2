// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package wingset;

import org.wings.SButton;
import org.wings.SComponent;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SForm;
import org.wings.SGridLayout;
import org.wings.SLabel;
import org.wings.SList;
import org.wings.SPanel;
import org.wings.STextField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListBugTest extends WingSetPane {
    public ListBugTest() {    }

    protected SComponent createExample() {
        SLabel title = new SLabel("Search");
        SButton populate = new SButton("Search");
        populate.setShowAsFormComponent(true);
        title.setFont(new SFont(null, SFont.BOLD, 18));
        final SList list = new SList(new DefaultListModel());
        list.setPreferredSize(SDimension.FULLWIDTH);
        final STextField textField = new STextField();

        SButton clear = new SButton("Clear");
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultListModel model = (DefaultListModel) list.getModel();
                model.clear();
            }
        });

        SGridLayout gridLayout = new SGridLayout(1);
        SForm panel = new SForm(gridLayout);
        panel.setPreferredSize(new SDimension(200, 100));
        gridLayout.setVgap(5);

        gridLayout = new SGridLayout(3);
        gridLayout.setHgap(5);
        SPanel searchPanel = new SPanel(gridLayout);
        searchPanel.add(textField);
        searchPanel.add(populate);
        searchPanel.add(clear);

        // arrange components using a grid layout
        panel.add(title);
        panel.add(searchPanel);
        panel.add(list);

        /*SFrame rootFrame = new SFrame();
        rootFrame.getContentPane().add(panel);
        rootFrame.setVisible(true);*/
        return panel;
    }

}
