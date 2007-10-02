package wingset;

import org.wings.*;
import org.wingx.XDivision;

import java.awt.*;

public class XDivisionExample extends WingSetPane {


    protected SComponent createControls() {
        return null;
    }

    protected SComponent createExample() {
        XDivision division = new XDivision(new SGridBagLayout());
        division.setTitle("Person");

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.weightx = .4;
        labelConstraints.gridwidth = GridBagConstraints.RELATIVE;
        labelConstraints.insets = new Insets(0, 0, 4, 10);

        GridBagConstraints editorConstraints = new GridBagConstraints();
        editorConstraints.weightx = .6;
        editorConstraints.gridwidth = GridBagConstraints.RELATIVE;

        division.add(new SLabel("First Name"), labelConstraints);
        division.add(new STextField(), editorConstraints);
        division.add(new SLabel("Last Name"), labelConstraints);
        division.add(new STextField(), editorConstraints);

        XDivision division2 = new XDivision(new SGridBagLayout());
        division2.setTitle("Address");

        division2.add(new SLabel("Street"), labelConstraints);
        division2.add(new STextField(), editorConstraints);
        division2.add(new SLabel("City"), labelConstraints);
        division2.add(new STextField(), editorConstraints);

        SPanel panel = new SPanel(new SGridLayout());
        panel.add(division);
        panel.add(division2);
        return panel;
    }

    class SLabel
        extends org.wings.SLabel
    {
        public SLabel(String text) {
            super(text);
            setPreferredSize(new SDimension("100px", null));
            setHorizontalAlignment(SConstants.RIGHT_ALIGN);
        }
    }
}
