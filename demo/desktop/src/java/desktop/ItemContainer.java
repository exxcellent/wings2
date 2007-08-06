package desktop;

public interface ItemContainer
{
    public void setTitle(String title);

    public void setIcon(org.wings.SURLIcon icon);

    public void destroy();

    public DesktopItem getItem();

    public void setItem(DesktopItem item);

    public String getName();

}
