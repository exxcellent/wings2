package wingset;

import org.wings.*;
import org.wings.event.SRequestListener;
import org.wings.event.SRequestEvent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ErrorPageExample extends WingSetPane {

    protected SComponent createControls() {
        return null;
    }

    protected SComponent createExample() {
        SButton incremental = new SButton("Error during incremental request");
        incremental.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                throw new RuntimeException("Show error page!");
            }
        });
        SButton complete = new SButton("Error during complete request");
        complete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getParentFrame().reload();
                getSession().addRequestListener(new SRequestListener() {
                    int count = 0;
                    public void processRequest(SRequestEvent e) {
                        if (e.getType() == SRequestEvent.PROCESS_REQUEST) {
                            if (count == 0)
                                // omit the current request
                                count ++;
                            else {
                                // this is the reload
                                getSession().removeRequestListener(this);
                                throw new RuntimeException("Show error page!");
                            }
                        }
                    }
                });
            }
        });

        SPanel panel = new SPanel(new SGridLayout(1, 2, 10, 10));
        panel.addComponent(incremental);
        panel.addComponent(complete);

        return panel;
    }
}
