package wingset;

import org.wings.SComponent;
import org.wings.SLabel;
import org.wings.SPanel;

public class ErrorPageExample extends WingSetPane {

    protected SComponent createControls() {
        return null;
    }

    protected SComponent createExample() {
        SPanel panel = null; // produce an error
        panel.addComponent(new SLabel("This is a test"));
        return panel;
    }
}
