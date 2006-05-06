package wingset;

import org.wings.*;
import org.wings.style.CSSProperty;
import org.wings.style.CSSStyleSheet;
import org.wings.session.SessionManager;
import org.wings.util.SStringBuilder;

import java.awt.event.ActionEvent;
import java.awt.*;

public class DesktopPaneExample extends WingSetPane {

    private SIcon windowIcon;

    private static final int FRAME_COUNT = 8;
    private ComponentControls controls;
    private SDesktopPane desktopPane = new SDesktopPane();

    protected SComponent createExample() {
        controls = new DesktopPaneControls();

        windowIcon = (SIcon)SessionManager.getSession().getCGManager().getObject("TableCG.editIcon", SIcon.class);
        for (int i = 0; i < FRAME_COUNT; i++) {
            SInternalFrame iFrame = new SInternalFrame();
            iFrame.getContentPane().setLayout(new SBoxLayout(SBoxLayout.VERTICAL));
            iFrame.setTitle("A Long Title of Frame " + (i+1));
            desktopPane.add(iFrame);
            fillFrame(iFrame);
            // set some special contents & icons
            if ((i % 2) == 0) {
                iFrame.setIcon(windowIcon);
                SStringBuilder labelText = new SStringBuilder("some extra label...");
                for (int j = 0; j <= i; j++) {
                    labelText.append("extra-");
                    iFrame.getContentPane().add(new SLabel(labelText.toString()));
                }
                labelText.append("long.");
                iFrame.getContentPane().add(new SLabel(labelText.toString()));
            }
        }

        SForm form = new SForm(new SBorderLayout());
        form.add(controls, SBorderLayout.NORTH);
        form.add(desktopPane, SBorderLayout.CENTER);

        return form;
    }

    private void fillFrame(SInternalFrame frame) {
        frame.getContentPane().add(new STextField());
        frame.getContentPane().add(new SLabel("This is a label"));
    }

    private class DesktopPaneControls extends ComponentControls {

        public DesktopPaneControls() {
            final SComboBox titleColor = new SComboBox(COLORS);
            titleColor.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = (Color) ((Object[]) titleColor.getSelectedItem())[1];
                    for (int i = 0; i < desktopPane.getComponents().length; i++) {
                        SComponent component = desktopPane.getComponents()[i];
                        component.setAttribute(SInternalFrame.SELECTOR_TITLE, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
                    }
                }
            });
            titleColor.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel(" title"));
            addControl(titleColor);

            final SComboBox contentColor = new SComboBox(COLORS);
            contentColor.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = (Color) ((Object[]) contentColor.getSelectedItem())[1];
                    for (int i = 0; i < desktopPane.getComponents().length; i++) {
                        SComponent component = desktopPane.getComponents()[i];
                        component.setAttribute(SInternalFrame.SELECTOR_CONTENT, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
                    }
                }
            });
            contentColor.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel(" content"));
            addControl(contentColor);
        }
    }
}
