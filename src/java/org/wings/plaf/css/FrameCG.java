/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.css;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.plaf.Update;
import org.wings.Renderable;
import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.SToolTipManager;
import org.wings.Version;
import org.wings.script.JavaScriptDOMListener;
import org.wings.script.JavaScriptEvent;
import org.wings.dnd.DragAndDropManager;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.*;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.resource.ClassPathResource;
import org.wings.resource.ReloadResource;
import org.wings.resource.UpdateResource;
import org.wings.resource.ResourceManager;
import org.wings.script.ScriptListener;
import org.wings.session.*;

import javax.swing.*;

import java.io.IOException;
import java.util.*;

/**
 * PLAF renderer for SFrames.
 * Does quite many abritriray things i.e. registering diverse service scripts, etc.
 */
public final class FrameCG implements org.wings.plaf.FrameCG {

    private static final long serialVersionUID = 1L;

    private final static Log log = LogFactory.getLog(FrameCG.class);

    /**
     * The default DOCTYPE enforcing standard (non-quirks mode) in all current browsers. Please be aware, that
     * changing the DOCTYPE may change the way how browser renders the generate document i.e. esp. the CSS
     * attribute inheritance does not work correctly on <code>table</code> elements.
     * See i.e. http://www.ericmeyeroncss.com/bonus/render-mode.html
     */
    public final static String STRICT_DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" " +
            "\"http://www.w3.org/TR/REC-html40/strict.dtd\">";

    /**
     * The HTML DOCTYPE setting all browsers to Quirks mode. We need this to force IE to use the correct box
     * rendering model. It's the only browser you cannot reconfigure via a CSS tag.
     */
    public final static String QUIRKS_DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";

    /**
     * Lookup for a property Stylesheet.BROWSERNAME to know fitting stylesheets
     */
    private static final String PROPERTY_STYLESHEET = "Stylesheet.";
    private static final String BROWSER_DEFAULT = "default";

    private static final String WINGS_ALL = (String) ResourceManager.getObject("JS.wingsAll", String.class);
    private static final String YAHOO_GLOBAL = (String) ResourceManager.getObject("JS.yahooGlobal", String.class);
    private static final String YAHOO_DOM = (String) ResourceManager.getObject("JS.yahooDom", String.class);
    private static final String YAHOO_EVENT = (String) ResourceManager.getObject("JS.yahooEvent", String.class);
    private static final String YAHOO_CONTAINER = (String) ResourceManager.getObject("JS.yahooContainer", String.class);
    private static final String YAHOO_CONNECTION = (String) ResourceManager.getObject("JS.yahooConnection", String.class);
    private static final String YAHOO_SLIDER = (String) ResourceManager.getObject("JS.yahooSlider", String.class);
    private static final String YAHOO_DND = (String) ResourceManager.getObject("JS.yahooDnD", String.class);
    private static final String ETC_DND = (String) ResourceManager.getObject("JS.etcDnD", String.class);
    private static final String ETC_WZDND = (String) ResourceManager.getObject("JS.etcWzDnD", String.class);

    private ClassPathResource formbutton;

    private String documentType = STRICT_DOCTYPE;

    protected final List headers = new ArrayList();

    /**
     * Should the returned HTML page start with the &lt;?xml version="1.0" encoding="..."&gt;.
     * This has effects which rendering mode the browsers will choose (quirks/strict)
     */
    private Boolean renderXmlDeclaration = Boolean.FALSE;

    /**
     * Initialize properties from config
     */
    public FrameCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();
        final String userDocType = (String) manager.getObject("FrameCG.userDocType", String.class);
        final Boolean userRenderXmlDecl = (Boolean) manager.getObject("FrameCG.renderXmlDeclaration", Boolean.class);

        if (userDocType != null) {
            setDocumentType(userDocType);
        }

        if (userRenderXmlDecl != null) {
            setRenderXmlDeclaration(userRenderXmlDecl);
        }

        // Externalize JavaScript headers
        headers.add(Utils.createExternalizedJavaScriptHeader(WINGS_ALL));
        headers.add(Utils.createExternalizedJavaScriptHeader(YAHOO_GLOBAL));
        headers.add(Utils.createExternalizedJavaScriptHeader(YAHOO_DOM));
        headers.add(Utils.createExternalizedJavaScriptHeader(YAHOO_EVENT));
        headers.add(Utils.createExternalizedJavaScriptHeader(YAHOO_CONTAINER));
        headers.add(Utils.createExternalizedJavaScriptHeader(YAHOO_CONNECTION));
        headers.add(Utils.createExternalizedJavaScriptHeader(YAHOO_DND));
        headers.add(Utils.createExternalizedJavaScriptHeader(YAHOO_SLIDER));

        headers.add(new JavaScriptHeader("../dwr/engine.js"));
        headers.add(new JavaScriptHeader("../dwr/util.js"));

        formbutton = new ClassPathResource("org/wings/plaf/css/formbutton.htc", "text/x-component");
        formbutton.getId(); // externalize
    }

    public void installCG(final SComponent comp) {
        final SFrame component = (SFrame) comp;

        // Add dynamic resources to the frame
        ReloadResource reloadResource = new ReloadResource(component);
        component.addDynamicResource(reloadResource);
        UpdateResource updateResource = new UpdateResource(component);
        component.addDynamicResource(updateResource);

        // Externalize update resource
        component.getDynamicResource(UpdateResource.class).getId();

        final JavaScriptDOMListener storeFocusFF = new JavaScriptDOMListener(
                JavaScriptEvent.ON_FOCUS,
                "wingS.util.storeFocus", comp);
        final JavaScriptDOMListener storeFocusIE = new JavaScriptDOMListener(
                JavaScriptEvent.ON_ACTIVATE,
                "wingS.util.storeFocus", comp);
        final JavaScriptDOMListener initializeAjaxFrame = new JavaScriptDOMListener(
                JavaScriptEvent.ON_LOAD,
                "wingS.ajax.initializeFrame", comp);

        // Add script listeners to the frame
        component.addScriptListener(Utils.isMSIE(component) ? storeFocusIE : storeFocusFF);
        component.addScriptListener(initializeAjaxFrame);

        CaptureDefaultBindingsScriptListener.install(component);

        Headers.getInstance().registerHeaderLinks(headers, component);
        Headers.getInstance().registerHeaderLinks(getBrowserStylesheets(component.getSession()), component);
    }

    /**
     * Externalizes the style sheet(s) for this session. Look up according style sheet file name in
     * org.wings.plaf.css.properties file under Stylesheet.BROWSERNAME. The style sheet is loaded from
     * the class path.
     *
     * @return a list of externalized browser specific stylesheet headers
     */
    private List getBrowserStylesheets(Session session) {
        final CGManager cgManager = session.getCGManager();
        final String browserName = session.getUserAgent().getBrowserType().getShortName();

        String cssClassPaths = (String) cgManager.getObject(PROPERTY_STYLESHEET + browserName, String.class);
        if (cssClassPaths == null) {
            cssClassPaths = (String) cgManager.getObject(PROPERTY_STYLESHEET + BROWSER_DEFAULT, String.class);
        }

        ArrayList browserStylesheets = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(cssClassPaths, ",");
        while (tokenizer.hasMoreTokens()) {
            browserStylesheets.add(Utils.createExternalizedSytleSheetHeader(tokenizer.nextToken()));
        }

        return browserStylesheets;
    }

    /**
     * Uninstall renderer (i.e. other to apply other renderer).
     */
    public void uninstallCG(final SComponent comp) {
        final SFrame component = (SFrame) comp;

        component.removeDynamicResource(ReloadResource.class);
        component.removeDynamicResource(UpdateResource.class);

        Headers.getInstance().deregisterHeaderLinks(headers, component);
    }

    public void componentChanged(SComponent c) {
        // The update of the input maps happens on every write, so here it's unnecessary.
        // updateGlobalInputMaps(frame);
    }

    private void updateGlobalInputMaps(SFrame frame) {
        // Here it goes, global input maps
        ScriptListener[] scriptListeners = frame.getScriptListeners();
        // First, delete all of them, they are from the last request...
        for (int i = 0; i < scriptListeners.length; i++) {
            ScriptListener scriptListener = scriptListeners[i];
            if (scriptListener instanceof InputMapScriptListener) {
                /*
                 * One could collect this as a list and only add/remove
                 * the changes. But the listeners are added as anonymous
                 * classes, which makes identifying them expensive. That
                 * would have to be changed.
                 */
                frame.removeScriptListener(scriptListener);
            }
        }
        // Then install the ones we need for the request going on...
        List inputMapComponents = frame.getGlobalInputMapComponents();
        if (inputMapComponents != null) {
            Iterator iter = inputMapComponents.iterator();
            while (iter.hasNext()) {
                SComponent comp = (SComponent)iter.next();
                if (comp.isRecursivelyVisible()) {
                    InputMap inputMap = comp.getInputMap(SComponent.WHEN_IN_FOCUSED_FRAME);
                    if (inputMap != null) {
                        InputMapScriptListener.installToFrame(frame, comp);
                    }
                }
            }
        }
    }

    public void write(final Device device, final SComponent component) throws IOException {
        final SFrame frame = (SFrame) component;

        /*
         * The input maps must be updated on every rendering of the SFrame, since
         * some components could be invisible in this request that registered an
         * input map before. To avoid too much code sent to the client, this update
         * is calles.
         */
        updateGlobalInputMaps(frame);

        RenderHelper.getInstance(frame).reset();

        if (!frame.isVisible())
            return;
        else
            frame.fireRenderEvent(SComponent.START_RENDERING);

        Session session = SessionManager.getSession();
        final String language = session.getLocale().getLanguage();
        final String title = frame.getTitle();
        final List headers = frame.headers();
        final String encoding = session.getCharacterEncoding();

        // <?xml version="1.0" encoding="...">
        if (renderXmlDeclaration == null || renderXmlDeclaration.booleanValue()) {
            device.print("<?xml version=\"1.0\" encoding=\"");
            Utils.write(device, encoding);
            device.print("\"?>\n");
        }

        // <!DOCTYPE HTML PUBLIC ... >
        Utils.writeRaw(device, documentType);
        device.print("\n");

        // <html> tag
        device.print("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"");
        Utils.write(device, language);
        device.print("\" lang=\"");
        Utils.write(device, language);
        device.print("\">\n");

        // <head> tag
        device.print("<head>");
        if (title != null) {
            device.print("<title>");
            Utils.write(device, title);
            device.print("</title>\n");
        }

        // Character set encoding, the default is typically utf-8.
        device.print("<meta http-equiv=\"Content-type\" content=\"text/html; charset=");
        Utils.write(device, encoding);
        device.print("\"/>\n");

        /* Insert version and compile time. Since the Version Class is generated on compile time,
         * build errors in SDK's are quite normal. Just run the Version.java ant task.
         */
        device.print("<meta http-equiv=\"Generator\" content=\"wingS (http://www.j-wings.org) v");
        device.print(Version.getVersion());
        device.print(" built on: ");
        device.print(Version.getCompileTime());
        device.print("\" />\n");

        // Register and render DWR callables
        Collection callableNames = CallableManager.getInstance().callableNames();

        Collection allHeaders = new ArrayList();
        allHeaders.addAll(Headers.getInstance().getHeaders(frame));
        allHeaders.addAll(headers);
        for (Iterator iterator = callableNames.iterator(); iterator.hasNext();) {
            allHeaders.add(new JavaScriptHeader("../dwr/interface/" + iterator.next() + ".js"));
        }

        for (Iterator iterator = allHeaders.iterator(); iterator.hasNext();) {
            Object next = iterator.next();
            if (next instanceof Renderable) {
                ((Renderable) next).write(device);
            } else {
                Utils.write(device, next.toString());
            }
            device.print("\n");
        }

        // Focus management. Put focus in selected object.
        final SComponent focus = frame.getFocus();
        FocusScriptListener gainFocusScript = (FocusScriptListener) frame.getClientProperty("gain-focus-script");
        if (gainFocusScript == null || gainFocusScript.getFocusComponent() != focus) {
            if (gainFocusScript != null) {
                // Drop old focus gain script
                frame.removeScriptListener(gainFocusScript);
                frame.putClientProperty("gain-focus-script", null);
            }
            if (focus != null) {
                // Add new focus gain script
                gainFocusScript = new FocusScriptListener(focus);
                frame.addScriptListener(gainFocusScript);
                frame.putClientProperty("gain-focus-script", gainFocusScript);
            }
        }

        device.print("</head>\n");
        device.print("<body");
        Utils.writeEvents(device, frame, null);
        AbstractComponentCG.writeAllAttributes(device, frame);
        device.print(">\n");

        // Write contents of the frame
        if (frame.isVisible()) {

            // Setup DnD
            DragAndDropManager dndManager = frame.getSession().getDragAndDropManager();
            List dragComponents = null;
            List dropComponents = null;
            Iterator dragIter = null;
            Iterator dropIter = null;
            // Add initial JS for DnD if neccessary
            if (dndManager.isVisible()) {
                dragComponents = dndManager.getDragSources();
                dropComponents = dndManager.getDropTargets();
                dragIter = dragComponents.iterator();
                dropIter = dropComponents.iterator();
                if (dragIter.hasNext()) {
                    // this needs to be added to the body, so use device.print()
                    // TODO: is caching by the VM enough or make this only initialize once?
                    ClassPathResource res = new ClassPathResource(ETC_WZDND, "text/javascript");
                    String jScriptUrl = frame.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
                    device.print("<script type=\"text/javascript\" src=\"");
                    device.print(jScriptUrl);
                    device.print("\"></script>\n");
                }
            }

            // Write components
            frame.getLayout().write(device);

            // Write menus
            device.print("\n\n<div id=\"wings_menues\">\n");
            Set menues = frame.getSession().getMenuManager().getMenues(frame);
            for (Iterator i = menues.iterator(); i.hasNext();) {
                SComponent menuItem = (SComponent) i.next();
                menuItem.putClientProperty("popup", Boolean.TRUE);
                menuItem.write(device);
                menuItem.putClientProperty("popup", null);
            }
            device.print("\n</div>\n\n");

            // Write final JS for DnD if neccessary
            if (dndManager.isVisible() && dragIter != null && dragIter.hasNext()) {
                // initialize only if dragSources are present
                device.print("<script type=\"text/javascript\">\n<!--\n");
                device.print("SET_DHTML();\n");
                while (dragIter.hasNext()) {
                    SComponent dragComp = (SComponent)dragIter.next();
                    if (dragComp.isVisible()) {
                        device.print("ADD_DHTML('");
                        device.print(dragComp.getName());
                        device.print("'+CLONE+TRANSPARENT);\n");
                        device.print("dd.elements['");
                        device.print(dragComp.getName());
                        device.print("'].dragsource=1;\n");
                    }
                }
                while (dropIter.hasNext()) {
                    SComponent dropComp = (SComponent)dropIter.next();
                    if (dropComp.isVisible()) {
                        if (dragComponents.contains(dropComp)) {
                            // This is a draggable and a dropTarget, it's already
                            // added.
                        } else {
                            device.print("ADD_DHTML('");
                            device.print(dropComp.getName());
                            device.print("'+NO_DRAG);\n");
                        }
                        device.print("dd.elements['");
                        device.print(dropComp.getName());
                        device.print("'].droptarget=1;\n");
                    }
                }
                device.print("var wdnd_managerId = '");
                device.print(dndManager.getEncodedLowLevelEventId());
                device.print("';\n");
                device.print("//-->\n</script>");
                // TODO: is caching by the VM enough or make this only initialize once?
                ClassPathResource res = new ClassPathResource(ETC_DND, "text/javascript");
                String jScriptUrl = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
                device.print("<script type=\"text/javascript\" src=\"");
                device.print(jScriptUrl);
                device.print("\"></script>\n");
            }
        }

        handleScripts(device, frame);

        device.print("</body>\n</html>\n");

        component.fireRenderEvent(SComponent.DONE_RENDERING);
        RenderHelper.getInstance(frame).reset();
    }

    protected void handleScripts(Device device, SComponent component) throws IOException {
        final SFrame frame = (SFrame) component;
        final ScriptManager scriptManager = frame.getSession().getScriptManager();
        final SToolTipManager tooltipManager = SToolTipManager.sharedInstance();

        device.print("<script type=\"text/javascript\">\n");

        final String eventEpoch = frame.getEventEpoch();
        final String reloadResource = frame.getDynamicResource(ReloadResource.class).getId();
        final String updateResource = frame.getDynamicResource(UpdateResource.class).getId();
        final boolean updateEnabled = frame.isUpdateEnabled();
        final Object[] updateCursor = frame.getUpdateCursor();

        device.print("// globally accessible script variables:\n" +
                "wingS.global.eventEpoch = '" + eventEpoch + "';\n" +
                "wingS.global.reloadResource = '" + reloadResource + "';\n" +
                "wingS.global.updateResource = '" + updateResource + "';\n" +
                "wingS.global.updateEnabled = " + updateEnabled + ";\n" +
                "wingS.global.updateCursor = " +
                	"{ 'enabled':" + updateCursor[0] + ", 'image':'" + updateCursor[1] +
                	"', 'dx':" + updateCursor[2] + ", 'dy':" + updateCursor[3] + " };\n");

        // hand script listeners of frame to script manager
        scriptManager.addScriptListeners(frame.getScriptListeners());

        // print all scripts
        ScriptListener[] scriptListeners = scriptManager.getScriptListeners();
        for (int i = 0; i < scriptListeners.length; ++i) {
            if (scriptListeners[i].getScript() != null) {
                device.print(scriptListeners[i].getScript()).print("\n");
            }
        }
        scriptManager.clearScriptListeners();

        // print all tooltips
        final List ttComponentIds = tooltipManager.getRegisteredComponents();
        if (ttComponentIds.size() != 0) {
            device.print(ToolTipCG.generateTooltipInitScript(ttComponentIds));
            tooltipManager.clearRegisteredComponents();
        }

        device.print("</script>\n");
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * @return The current rendered DOCTYPE of this document. {@link #STRICT_DOCTYPE}
     */
    public Boolean getRenderXmlDeclaration() {
        return renderXmlDeclaration;
    }

    /**
     * Sets should the returned HTML page start with the &lt;?xml version="1.0" encoding="..."&gt;.
     * This has effects which rendering mode the browsers will choose (quirks/strict)
     *
     * @param renderXmlDeclaration should the returned HTML page start with the &lt;?xml version="1.0" encoding="..."&gt;.
     */
    public void setRenderXmlDeclaration(Boolean renderXmlDeclaration) {
        this.renderXmlDeclaration = renderXmlDeclaration;
    }

	public Update getComponentUpdate(SComponent component) {
        return null;
	}

    public Update getAddHeaderUpdate(SFrame frame, int index, Object header) {
        if (header instanceof Script)
            return new HeaderScriptUpdate(frame, true, (Script) header, index);
        else if (header instanceof Link)
            return new HeaderLinkUpdate(frame, true, (Link) header, index);
        else
            return null;
    }

    public Update getAddHeaderUpdate(SFrame frame, Object header) {
        if (header instanceof Script)
            return new HeaderScriptUpdate(frame, true, (Script) header);
        else if (header instanceof Link)
            return new HeaderLinkUpdate(frame, true, (Link) header);
        else
            return null;
    }

    public Update getRemoveHeaderUpdate(SFrame frame, Object header) {
        if (header instanceof Link)
            return new HeaderLinkUpdate(frame, false, (Link) header);
        else
            // Removing script headers asynchronously would indeed
            // detach the according header, however, the functions
            // contained in the according file are not unloaded. So
            // we force a complete component update in this case.
            return null;
    }

    public Update getEpochUpdate(SFrame frame, String epoch) {
        return new EpochUpdate(frame, epoch);
    }

    public Update getUpdateEnabledUpdate(SFrame frame, boolean enabled) {
        return new UpdateEnabledUpdate(frame, enabled);
    }

    protected class HeaderScriptUpdate extends AbstractUpdate {

        private Boolean add;
        private Script script;
        private Integer index;

        public HeaderScriptUpdate(SComponent component, boolean add, Script script) {
            super(component);
            this.add = new Boolean(add);
            this.script = script;
        }

        public HeaderScriptUpdate(SComponent component, boolean add, Script script, int index) {
            this(component, add, script);
            this.index = new Integer(index);
        }

        public Handler getHandler() {
            UpdateHandler handler = new UpdateHandler("headerScript");
            handler.addParameter(add);
            handler.addParameter(script.getURL().toString());
            handler.addParameter(script.getType());
            if (index != null)
                handler.addParameter(index);
            return handler;
        }

        public boolean equals(Object object) {
            if (this == object)
                return true;
            if (!super.equals(object))
                return false;
            if (!script.equals(((HeaderScriptUpdate) object).script))
                return false;

            return true;
        }

    }

    protected class HeaderLinkUpdate extends AbstractUpdate {

        private Boolean add;
        private Link link;
        private Integer index;

        public HeaderLinkUpdate(SComponent component, boolean add, Link link) {
            super(component);
            this.add = new Boolean(add);
            this.link = link;
        }

        public HeaderLinkUpdate(SComponent component, boolean add, Link link, int index) {
            this(component, add, link);
            this.index = new Integer(index);
        }

        public Handler getHandler() {
            UpdateHandler handler = new UpdateHandler("headerLink");
            handler.addParameter(add);
            handler.addParameter(link.getURL().toString());
            handler.addParameter(link.getType());
            if (link.getRel() != null || link.getRev() != null || link.getTarget() != null || index != null)
                handler.addParameter(link.getRel());
            if (link.getRev() != null || link.getTarget() != null || index != null)
                handler.addParameter(link.getRev());
            if (link.getTarget() != null || index != null)
                handler.addParameter(link.getTarget());
            if (index != null)
                handler.addParameter(index);

            return handler;
        }

        public boolean equals(Object object) {
            if (this == object)
                return true;
            if (!super.equals(object))
                return false;
            if (!link.equals(((HeaderLinkUpdate) object).link))
                return false;

            return true;
        }

    }

    protected class EpochUpdate extends AbstractUpdate {

        private String epoch;

        public EpochUpdate(SComponent component, String epoch) {
            super(component);
            this.epoch = epoch;
        }

        public Handler getHandler() {
            UpdateHandler handler = new UpdateHandler("epoch");
            handler.addParameter(epoch);
            return handler;
        }

    }

    protected class UpdateEnabledUpdate extends AbstractUpdate {

        private Boolean enabled;

        public UpdateEnabledUpdate(SComponent component, boolean enabled) {
            super(component);
            this.enabled = new Boolean(enabled);
        }

        public Handler getHandler() {
            UpdateHandler handler = new UpdateHandler("updateEnabled");
            handler.addParameter(enabled);
            return handler;
        }

    }

}
