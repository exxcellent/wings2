package desktop;

import org.wings.SURLIcon;

public class NewsFeedTool
    implements DesktopTool
{

    private SURLIcon icon;
    private String text = "";

    public DesktopItem getItem() {
        return new NewsFeedItem();
    }

    public DesktopItem getExistingItem(String name) {
        return new NewsFeedItem(name);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SURLIcon getIcon() {
        return icon;
    }

    public void setIcon(SURLIcon icon) {
        this.icon = icon;
    }
}
