package wingset;

import org.wings.*;
import org.wings.text.SDocument;
import org.wings.event.SDocumentListener;
import org.wings.event.SDocumentEvent;
import org.wings.util.PropertyAccessor;
import org.wings.border.SLineBorder;

import java.awt.event.*;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SplitPaneExample
    extends WingSetPane {
    private SplitPaneControls controls;
    private SSplitPane splitPane;

    protected SComponent createControls() {
        controls = new SplitPaneControls();
        return controls;
    }

    public SComponent createExample() {
        SLabel left = new SLabel("left");
        left.setPreferredSize(SDimension.FULLAREA);
        left.setBorder(new SLineBorder(1));
        SLabel right = new SLabel("right");
        right.setPreferredSize(SDimension.FULLAREA);
        right.setBorder(new SLineBorder(1));
        splitPane = new SSplitPane(SSplitPane.HORIZONTAL_SPLIT, left, right);
        splitPane.setPreferredSize(SDimension.FULLAREA);

        controls.addControllable(splitPane);
        return splitPane;
    }

    class SplitPaneControls
        extends ComponentControls {
        private final String[] ORIENTATIONS = new String[] { "horizontal", "vertical" };

        public SplitPaneControls() {
            widthTextField.setText("100%");
            heightTextField.setText("100%");

            final SComboBox orientationCombo = new SComboBox(ORIENTATIONS);
            orientationCombo.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (ORIENTATIONS[0].equals(orientationCombo.getSelectedItem())) {
                        splitPane.setOrientation(SSplitPane.HORIZONTAL_SPLIT);
                    }
                    else if (ORIENTATIONS[1].equals(orientationCombo.getSelectedItem())) {
                        splitPane.setOrientation(SSplitPane.VERTICAL_SPLIT);
                    }
                }
            });

            final STextField dividerSizeTextField = new STextField();
            dividerSizeTextField.setColumns(1);
            dividerSizeTextField.setToolTipText("length only (example: '4')");
            dividerSizeTextField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        int dividerSize = Integer.parseInt(dividerSizeTextField.getText());
                        splitPane.setDividerSize(dividerSize);
                    }
                    catch (NumberFormatException e) {}
                }
            });
            addControl(new SLabel(" orientation"));
            addControl(orientationCombo);
            addControl(new SLabel(" divider size"));
            addControl(dividerSizeTextField);
        }
    }
}
