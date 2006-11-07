package org.wings.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.io.Device;
import org.wings.plaf.css.RenderHelper;
import org.wings.session.SessionManager;

/**
 * This resource is responsible for incremental page updates using AJAX. While the {@link CompleteUpdateResource}
 * always writes the complete component hierarchy of a frame, the {@link IncrementalUpdateResource} writes code
 * only for components that have been changed since the last request. Because this resource is typically requested
 * from an XMLHttpRequest object, it returns or respectively writes a well-formed XML document with MIME type
 * "text/xml". The data contained within this document can then be processed by according JavaScript functions.
 *
 * Depending on the two possible update modes ("complete" or "incremental") the structure of the returned XML
 * document looks as follows:
 *
 * <pre>
 * <?xml version="1.0" encoding="[encoding]"?>
 * <update mode="complete">
 *   <redirect>[URL of CompleteUpdateResource]</redirect>
 * </update>
 *
 * <?xml version="1.0" encoding="[encoding]" standalone="yes"?>
 * <update mode="incremental">
 *   <component id="[1st component's ID]">[1st component's HTML code (inside a CDATA section)]</component>
 *   <component id="[2nd component's ID]">[2nd component's HTML code (inside a CDATA section)]</component>
 *   ...
 *   <component id="[Nth component's ID]">[Nth component's HTML code (inside a CDATA section)]</component>
 *   <script>[1st block of script code necessary for one of the updates (inside a CDATA section)]</script>
 *   <script>[2nd block of script code necessary for one of the updates (inside a CDATA section)]</script>
 *   ...
 *   <script>[Nth block of script code necessary for one of the updates (inside a CDATA section)]</script>
 *   <event_epoch>[updated event epoch]</event_epoch>
 * </update>
 * </pre>
 *
 * @author Stephan Schuster
 */
public class IncrementalUpdateResource extends DynamicResource {

    private static final transient Log log = LogFactory.getLog(CompleteUpdateResource.class);
    private static final boolean DEBUGGING = true;

    private String encoding = SessionManager.getSession().getCharacterEncoding();
    private String xmlHeader = "<?xml version=\"1.0\" encoding=\"" + encoding + "\" standalone=\"yes\"?>";

    public IncrementalUpdateResource(final SFrame f) {
        super(f);
        this.extension = "xml";
        this.mimeType = "text/xml; charset=" + encoding;
    }

    public void write(Device out) throws IOException {
        try {
            SComponent component;
            SFrame frame = getFrame();
            Collection componentsToUpdate = new ArrayList();
            RenderHelper helper = RenderHelper.getInstance(frame);

            // Enable incremental updates.
            String updateMode = "incremental";
            helper.setIncrementalUpdateMode(true);

            // ---> COLLECT COMPONENTS THAT NEED AN UPDATE

            Set dirtyComponents = SessionManager.getSession().getReloadManager().getDirtyComponents();

            // In case our frame has become dirty, every component should
            // be updated. Therefore we redirect the browser to the URL of
            // the CompleteUpdateResource which then renders the whole page.
            if (dirtyComponents.contains(getFrame())) {
                updateMode = "complete";
                componentsToUpdate.add(getFrame());
            }

            if (updateMode.equals("incremental")) {
                // Store dirty components and their path to the parent frame
                // in a HashMap (using a HashMap and sorting keys afterwards
                // seems to be faster than inserting everything in a TreeMap).
                Map candidatesToUpdate = new HashMap(dirtyComponents.size());
                for (Iterator i = dirtyComponents.iterator(); i.hasNext();) {
                    component = (SComponent) i.next();
                    // If a component is dirty but has no parent frame it has
                    // typically been changed and removed afterwards. In such
                    // cases we do not need to update the mentioned component.
                    if (component.getParentFrame() == null) continue;
                    candidatesToUpdate.put(component.getPathToParentFrame(), component);
                }
                // Sort paths in list according to their depth in the component hierarchy.
                ArrayList pathsToParentFrame = new ArrayList(candidatesToUpdate.keySet());
                Collections.sort(pathsToParentFrame, new Comparator() {
                    public int compare(Object path1, Object path2) {
                        int depthPath1 = ((String) path1).split("/").length;
                        int depthPath2 = ((String) path2).split("/").length;
                        return depthPath1 - depthPath2;
                    }
                });
                // If a dirty component contains one or more other dirty components
                // we don't have to update the latter ones since they are implicitly
                // updated during the update process of their 'container component'.
                for (int i = 0; i < pathsToParentFrame.size(); ++i) {
                    String prefix = (String) pathsToParentFrame.get(i);
                    for (int j = i + 1; j < pathsToParentFrame.size(); ++j) {
                        String path = (String) pathsToParentFrame.get(j);
                        if (path.startsWith(prefix)) {
                            candidatesToUpdate.remove(path);
                            pathsToParentFrame.remove(j--);
                        }
                    }
                }
                componentsToUpdate = candidatesToUpdate.values();
            }

            // ---> WRITE COMPONENTS THAT NEED AN UPDATE

            helper.reset();
            out.print(xmlHeader);
            // open root element
            out.print("\n<update mode=\"" + updateMode + "\">");
            if (updateMode.equals("incremental")) {
                if (componentsToUpdate.size() > 0) {
                    // debug info of updates
                    if (DEBUGGING) {
                        out.print("\nComponents to update: " + componentsToUpdate.size() + " -->");
                        for (Iterator i = componentsToUpdate.iterator(); i.hasNext();) {
                            component = (SComponent) i.next();
                            out.print(" " + component + (i.hasNext() ? "," : ""));
                        }
                    }
                    // updates of components
                    for (Iterator i = componentsToUpdate.iterator(); i.hasNext();) {
                        component = (SComponent) i.next();
                        out.print("\n<component id=\"" + component.getName() + "\"><![CDATA[");
                        component.write(out);
                        out.print("]]></component>");
                    }
                    // updates of scripts
                    for (Iterator i = helper.getCollectedScripts().iterator(); i.hasNext();) {
                        out.print("\n<script><![CDATA[" + i.next() + "]]></script>");
                    }
                    // update of event epoch
                    out.print("\n<event_epoch>" + getFrame().getEventEpoch() + "</event_epoch>");
                } else {
                    out.print("\nThere are no updates available!");
                }
            } else if (updateMode.equals("complete")) {
                out.print("\n<redirect>");
                out.print(getFrame().getDynamicResource(CompleteUpdateResource.class).getURL());
                out.print("</redirect>");
            }
            // close root element
            out.print("\n</update>");
            helper.reset();

        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            log.fatal("resource: " + getId(), e);
            throw new IOException(e.getMessage());
        }
    }

}
