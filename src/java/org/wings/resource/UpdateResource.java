package org.wings.resource;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.ReloadManager;
import org.wings.SFrame;
import org.wings.SToolTipManager;
import org.wings.plaf.Update;
import org.wings.io.Device;
import org.wings.plaf.css.RenderHelper;
import org.wings.plaf.css.ToolTipCG;
import org.wings.script.ScriptListener;
import org.wings.session.ScriptManager;
import org.wings.session.SessionManager;

/**
 * This resource is responsible for incremental page updates using AJAX. While requests sent to the
 * {@link ReloadResource} always entail a full rewrite of the frame's complete component hierarchy,
 * requests received by the {@link UpdateResource} will result in preferably small data chunks being
 * sent back to the client. These so called "incremental updates" encapsulate all informations the
 * client needs in order to synchronize its page with the server's state. Because this resource is
 * typically requested from an XMLHttpRequest object, it returns a well-formed XML document with MIME
 * type "text/xml". The updates in this document will be processed by according JavaScript functions.
 *
 * The XML structure looks as follows:
 *
 * <pre>
 * <?xml version="1.0" encoding="[encoding]" standalone="yes"?>
 * <updates>
 *   <update><![CDATA[ my1stClientSideHandler(...) ]]></update>
 *   <update><![CDATA[ my2ndClientSideHandler(...) ]]></update>
 *   <update><![CDATA[ my3rdClientSideHandler(...) ]]></update>
 *   ...
 * </updates>
 * </pre>
 *
 * @author Stephan Schuster
 */
public class UpdateResource extends DynamicResource {

    private static final transient Log log = LogFactory.getLog(UpdateResource.class);

    private String encoding = SessionManager.getSession().getCharacterEncoding();
    private String xmlHeader = "<?xml version=\"1.0\" encoding=\"" + encoding + "\" standalone=\"yes\"?>";

    public UpdateResource(final SFrame f) {
        super(f);
        this.extension = "xml";
        this.mimeType = "text/xml; charset=" + encoding;
    }

    public void write(Device out) throws IOException {
        try {
            final SFrame frame = getFrame();
            final RenderHelper renderHelper = RenderHelper.getInstance(frame);
            final ReloadManager reloadManager = frame.getSession().getReloadManager();
            final ScriptManager scriptManager = frame.getSession().getScriptManager();
            final SToolTipManager tooltipManager = SToolTipManager.sharedInstance();

            renderHelper.reset();

            out.print(xmlHeader);
            out.print("\n<updates>");
            if (reloadManager.isReloadRequired(frame)) {
                writeUpdate(out, "wingS.request.reloadFrame()");
            } else {
            	Collection updates = reloadManager.getUpdates();
				if (updates.size() > 0) {
					// update components
					for (Iterator i = updates.iterator(); i.hasNext();) {
						writeUpdate(out, (Update) i.next());
					}
				} else {
					out.print("\nThere are no components to update!");
				}
                // update tooltips
                final List ttComponentIds = tooltipManager.getRegisteredComponents();
                if (ttComponentIds.size() != 0) {
                    writeUpdate(out, ToolTipCG.generateTooltipInitScript(ttComponentIds));
                    tooltipManager.clearRegisteredComponents();
                }
                // update scripts
                ScriptListener[] scriptListeners = scriptManager.getScriptListeners();
                for (int i = 0; i < scriptListeners.length; ++i) {
                    if (scriptListeners[i].getScript() != null) {
                        writeUpdate(out, scriptListeners[i].getScript());
                    }
                }
                scriptManager.clearScriptListeners();
			}
            out.print("\n</updates>");

            renderHelper.reset();

        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            log.fatal("resource: " + getId(), e);
            throw new IOException(e.getMessage());
        }
    }

    private void writeUpdate(Device out, Update update) throws IOException {
        Update.Handler handler = update.getHandler();

        writePrefix(out);
    	out.print(handler.getName()).print("(");
        Iterator parameters = handler.getParameters();
        if (parameters.hasNext())
            out.print(parameters.next());
        while (parameters.hasNext())
            out.print(",").print(parameters.next());
		out.print(")");
        writePostfix(out);
    }

    private void writeUpdate(Device out, String update) throws IOException {
        writePrefix(out);
        out.print(update);
        writePostfix(out);
    }

    private void writePrefix(Device out) throws IOException {
        out.print("\n  <update><![CDATA[");
    }

    private void writePostfix(Device out) throws IOException {
        out.print("]]></update>");
    }

}
