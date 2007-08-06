package desktop;

import org.wings.SURLIcon;

public class EditorTool
    implements DesktopTool
{

    private SURLIcon icon;
    private String text = "";

    public DesktopItem getItem() {

        return new EditorItem();
    }

    public DesktopItem getExistingItem(String name) {
        return new EditorItem(name);
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
