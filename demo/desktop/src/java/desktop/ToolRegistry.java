package desktop;

import org.wings.SURLIcon;

import java.util.HashMap;
import java.util.Map;

public class ToolRegistry
{

    private static ToolRegistry ref = null;
    private Map<String, DesktopTool> registeredTools = new HashMap<String, DesktopTool>();

    private ToolRegistry() {
        EditorTool ed = new EditorTool();
        ed.setText("Add new Editor");
        ed.setIcon(new SURLIcon("../icons/penguin.png"));
        registerTool("EditorTool", ed);

        FileOpenerTool op = new FileOpenerTool();
        op.setText("Open File in Editor");
        op.setIcon(new SURLIcon("../icons/penguin.png"));
        registerTool("FileOpenerTool", op);

        NewsFeedTool news = new NewsFeedTool();
        news.setText("New Newsfeed");
        registerTool("NewsfeedTool", news);
    }

    public static ToolRegistry getToolRegistry() {
        if (ref == null)
            ref = new ToolRegistry();

        return ref;
    }

    public Map<String, DesktopTool> getRegisteredTools() {
        return registeredTools;
    }

    public void registerTool(String toolName, DesktopTool tool) {
        registeredTools.put(toolName, tool);
    }
}
