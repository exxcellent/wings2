package wingset;

import org.wings.SBoxLayout;
import org.wings.SComponent;
import org.wings.SDesktopPane;
import org.wings.SIcon;
import org.wings.SInternalFrame;
import org.wings.SLabel;
import org.wings.STextField;
import org.wings.session.SessionManager;
import org.wings.util.SStringBuilder;

public class DesktopPaneExample extends WingSetPane {
    
    private SIcon windowIcon;

    private static final int FRAME_COUNT = 8;

    protected SComponent createExample() {
        SDesktopPane desktopPane = new SDesktopPane();
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
        return desktopPane;
    }

    private void fillFrame(SInternalFrame frame) {
        frame.getContentPane().add(new STextField());
        frame.getContentPane().add(new SLabel("This is a label"));
    }

}
