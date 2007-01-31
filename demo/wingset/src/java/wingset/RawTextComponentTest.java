package wingset;

import org.wings.SComponent;
import org.wings.SDimension;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SRawTextComponent;
import org.wings.STemplateLayout;

public class RawTextComponentTest
    extends WingSetPane
{
    protected SComponent createControls() {
        return null;
    }

    protected SComponent createExample() {
        final SPanel examplePanel = new SPanel();
        examplePanel.setPreferredSize(SDimension.FULLWIDTH);
        try {
            java.net.URL templateURL =
                    getSession().getServletContext().getResource("/templates/RawTextComponentExample.thtml");
            if (templateURL == null) {
                examplePanel.add(new SLabel("Sorry, can't find RawTextComponentExample.thtml. Are you using a JAR-File?"));
                return examplePanel;
            }
            // you can of course directly give files here.
            STemplateLayout layout = new STemplateLayout(templateURL);
            examplePanel.setLayout(layout);
        } catch (java.io.IOException except) {
            except.printStackTrace();
        }

        examplePanel.add(new SRawTextComponent("This is a raw text. It does not break and has no div's wrapping it."), "contentText");
        examplePanel.add(new SRawTextComponent("<a href=\"javascript:alert('Simple but useful...');\">" +
                "This link was made with a RawTextComponent.</a>"), "linkText");

        return examplePanel;

    }

}
