package org.wings.plaf.css;

import org.wings.io.Device;
import org.wings.io.StringBuilderDevice;
import org.wings.SComponent;
import org.wings.header.SessionHeaders;
import org.wings.script.ScriptListener;
import org.wings.session.ScriptManager;
import org.wings.dnd.*;
import org.wings.plaf.Update;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class DragAndDropManagerCG
    implements org.wings.plaf.DragAndDropManagerCG
{
    protected final List headers = new ArrayList();

    public DragAndDropManagerCG() {
    }

    public void write(Device device, final SComponent component) throws IOException {
        final DragAndDropManager dragAndDropManager = (DragAndDropManager)component;

        ScriptManager.getInstance().addScriptListener(new ScriptListener() {

            public String getScript() {
                StringBuilder builder = AbstractComponentCG.STRING_BUILDER.get();
                builder.setLength(0);

                builder.append("wingS.dnd.manager = '");
                builder.append(dragAndDropManager.getName());
                builder.append("';");

                return builder.toString();
            }

            public String getEvent() {
                return null;
            }

            public String getCode() {
                return null;
            }

            public int getPriority() {
                return DEFAULT_PRIORITY;
            }
        });
    }

    public void installCG(SComponent c) {
        SessionHeaders.getInstance().registerHeaders(headers);
    }

    public void uninstallCG(SComponent c) {
        SessionHeaders.getInstance().deregisterHeaders(headers);
    }

    public void componentChanged(SComponent c) {
    }

    public Update getComponentUpdate(SComponent component) {
        return new ComponentUpdate(component);
    }

    protected class ComponentUpdate extends AbstractUpdate {

        public ComponentUpdate(SComponent component) {
            super(component);
        }

        public int getProperty() {
            return FULL_REPLACE_UPDATE;
        }

        public int getPriority() {
            return 4;
        }

        public Handler getHandler() {
            String htmlCode = "";
            String exception = null;

            try {
                StringBuilderDevice htmlDevice = new StringBuilderDevice();
                write(htmlDevice, component);
                htmlCode = htmlDevice.toString();
            } catch (Throwable t) {
                exception = t.getClass().getName();
            }

            UpdateHandler handler = new UpdateHandler("component");
            handler.addParameter(component.getName());
            handler.addParameter(htmlCode);
            if (exception != null) {
                handler.addParameter(exception);
            }
            return handler;
        }
    }
}
