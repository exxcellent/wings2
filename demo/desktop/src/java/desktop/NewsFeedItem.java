package desktop;

import org.wings.*;
import org.wings.style.CSSProperty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class NewsFeedItem
    extends AbstractDesktopItem
{


    private NewsFeedPanel panel;

    public NewsFeedItem() {
        super();
        pref.put(TOOL, "NewsfeedTool");

        addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(FEED)) {
                    ((NewsFeedPanel)getComponent()).setFeed((String)e.getNewValue());
                }
            }
        });
    }

    public NewsFeedItem(String name) {
        super(name);

        addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(FEED)) {
                    ((NewsFeedPanel)getComponent()).setFeed((String)e.getNewValue());
                }
            }
        });
    }

    public SComponent getComponent() {
        if (panel == null) {
            panel = new NewsFeedPanel();

        }


        return panel;
    }

    public void activated() {
        if (!(attributes.containsKey(NAME) && getValue(NAME) != null
            && attributes.containsKey(FEED) && getValue(FEED) != null)) {

            final SPanel inputPanel = new SPanel();
            final STextField inputElement = new STextField("file:///home/hengels/IssueNavigator.jspa.xml");
            final STextField inputTitleElement = new STextField("custom");
            inputElement.setAttribute(CSSProperty.WIDTH, "300px");
            inputTitleElement.setAttribute(CSSProperty.WIDTH, "300px");

            inputPanel.add(new SLabel("Title   "));
            inputPanel.add(inputTitleElement);

            inputPanel.setLayout(new SGridLayout(2, 2));
            inputPanel.add(new SLabel("URL   "));
            inputPanel.add(inputElement);
            SOptionPane.showInputDialog(panel, "New News-Feed", "News-Feed", inputPanel, new ActionListener()
            {

                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals(SOptionPane.OK_ACTION)) {
                        putValue(NAME, inputTitleElement.getText());
                        putValue(FEED, inputElement.getText());
                    }
                    else
                        container.destroy();
                }
            });
        }

    }

    public void fixContent() {
    }

    public void destroyed() {
        super.destroyed();
        ((NewsFeedPanel)getComponent()).destroy();
    }

}
