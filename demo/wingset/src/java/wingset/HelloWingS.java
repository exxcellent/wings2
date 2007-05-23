package wingset;

import org.wings.*;

import javax.swing.*;
import java.awt.*;

public class HelloWingS
{
    public HelloWingS() {
        SGridBagLayout gridLayout = new SGridBagLayout();
        gridLayout.setBorder(1);
        SForm panel = new SForm(gridLayout);
        panel.setPreferredSize(new SDimension("600px", "400px"));

        DefaultListModel model = new DefaultListModel();
        for (int i = 1; i <= 10; i++) {
            model.addElement(i);
        }

        SList leftList = new SList(model);
        leftList.setName("l");
        leftList.setPreferredSize(SDimension.FULLWIDTH);
        SList rightList = new SList(model);
        rightList.setPreferredSize(SDimension.FULLWIDTH);
        rightList.setName("r");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridheight = 4;
        constraints.weightx = .4;
        constraints.gridy = 0;
        panel.add(leftList, constraints);
        constraints.gridx = 2;
        panel.add(rightList, constraints);

        constraints.gridheight = 1;
        constraints.gridx = 1;
        constraints.weightx = .2;
        constraints.insets = new Insets(0, 6, 6, 6);

        //constraints.weighty = .1;
        SButton eins = new SButton("eins");
        eins.setName("b1");
        SButton zwei = new SButton("zwei");
        zwei.setName("b2");
        SButton drei = new SButton("drei");
        drei.setName("b3");

        panel.add(eins, constraints);
        constraints.gridy ++;
        panel.add(zwei, constraints);
        constraints.gridy ++;
        panel.add(drei, constraints);

        //constraints.weighty = .7;
        constraints.gridy ++;
        panel.add(new SLabel(), constraints);

        SFrame rootFrame = new SFrame();
        rootFrame.getContentPane().add(panel);
        rootFrame.setVisible(true);
    }
}
