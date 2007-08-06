package desktop;

public interface DesktopTool
{
    public DesktopItem getItem();

    public DesktopItem getExistingItem(String name);

    public String getText();

    public void setText(String text);

    public org.wings.SURLIcon getIcon();

    public void setIcon(org.wings.SURLIcon icon);

}
