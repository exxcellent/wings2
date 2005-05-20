package wingset;

import org.wings.SComponent;
import org.wings.SDesktopPane;
import org.wings.SDimension;
import org.wings.SIcon;
import org.wings.SInternalFrame;
import org.wings.SLabel;
import org.wings.STextField;
import org.wings.session.SessionManager;

public class DesktopPaneExample extends WingSetPane {
    
    private static SIcon windowIcon = (SIcon)SessionManager.getSession().getCGManager()
                                            .getObject("TableCG.editIcon", SIcon.class);

    private static final int FRAME_COUNT = 8;

    protected SComponent createExample() {
        SDesktopPane desktopPane = new SDesktopPane();
        for (int i = 0; i < FRAME_COUNT; i++) {
            SInternalFrame iFrame = new SInternalFrame();
            iFrame.setTitle("A Long Title of Frame " + (i+1));
            desktopPane.add(iFrame);
            fillFrame(iFrame);
            // set some special contents & icons
            if ((i % 2) == 0) {
                iFrame.setIcon(windowIcon);
                StringBuffer labelText = new StringBuffer("some extra label...");
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
