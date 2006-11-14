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
import org.wings.Renderable;
import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.SToolTipManager;
import org.wings.Version;
import org.wings.dnd.DragAndDropManager;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.*;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.resource.ClassPathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.resource.DynamicCodeResource;
import org.wings.resource.ResourceManager;
import org.wings.script.JavaScriptListener;
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
     * The default DOCTYPE enforcing standard (non-quirks mode) in all current browsers.
     * Please be aware, that changing the DOCTYPE may change the way how browser renders the generate
     * document i.e. esp. the CSS attribute inheritance does not work correctly on <code>table</code> elements.
     * See i.e. http://www.ericmeyeroncss.com/bonus/render-mode.html
     */
    public final static String STRICT_DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" " +
            "\"http://www.w3.org/TR/REC-html40/strict.dtd\">";

    /**
     * The HTML DOCTYPE setting all browsers to Quirks mode.
     * We need this to force IE to use the correct box rendering model. It's the only browser
     * you cannot reconfigure via a CSS tag.
     */
    public final static String QUIRKS_DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";

    /**
     * javascript needed for Drag and Drop support
     */
    private final String DND_JS = (String) ResourceManager.getObject("JScripts.dnd", String.class);

    /**
     * javascript needed for Drag and Drop support
     */
    private final String WZ_DND_JS = (String) ResourceManager.getObject("JScripts.wzdragdrop", String.class);

    private String documentType = STRICT_DOCTYPE;

    /**
     * Should the returned HTML page start with the &lt;?xml version="1.0" encoding="..."&gt;.
     * This has effects which rendering mode the browsers will choose (quirks/strict)
     */
    private Boolean renderXmlDeclaration = Boolean.FALSE;

    private ClassPathResource layout;
    private ClassPathResource formbutton;

    private HeaderUtil headerUtil = new HeaderUtil();

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

        // add JavaScript files to frame
        Session session = SessionManager.getSession();
        headerUtil.addHeader(createExternalizedHeader(session, FORM_SCRIPT, "text/javascript"));
        headerUtil.addHeader(createExternalizedHeader(session, DOMLIB_SCRIPT, "text/javascript"));
        headerUtil.addHeader(createExternalizedHeader(session, DOMTT_SCRIPT, "text/javascript"));

        headerUtil.addHeader(new Script("text/javascript", new DefaultURLResource("../dwr/engine.js")));
        headerUtil.addHeader(new Script("text/javascript", new DefaultURLResource("../dwr/util.js")));

        layout = new ClassPathResource("org/wings/plaf/css/layout.htc", "text/x-component");
        layout.getId(); // externalize ..
        formbutton = new ClassPathResource("org/wings/plaf/css/formbutton.htc", "text/x-component");
        formbutton.getId(); // externalize ..
    }

    /**
     * Lookup for a property Stylesheet.BROWSERNAME to know fitting stylesheets
     */
    private static final String PROPERTY_STYLESHEET = "Stylesheet.";
    private static final String BROWSER_DEFAULT = "default";

    public final String FORM_SCRIPT = (String) ResourceManager.getObject("JScripts.form", String.class);
    public final String DOMLIB_SCRIPT = (String) ResourceManager.getObject("JScripts.domlib", String.class);
    public final String DOMTT_SCRIPT = (String) ResourceManager.getObject("JScripts.domtt", String.class);

    public static final JavaScriptListener FOCUS_SCRIPT_MOZILLA = new JavaScriptListener("onload", "wu_registerEvent(document,'focus',storeFocus,true)");
    public static final JavaScriptListener FOCUS_SCRIPT_IE = new JavaScriptListener("onactivate", "storeFocus(event)");
    public static final JavaScriptListener SCROLL_POSITION_SCRIPT = new JavaScriptListener("onscroll", "storeScrollPosition(event)");
    public static final JavaScriptListener RESTORE_SCROLL_POSITION_SCRIPT = new JavaScriptListener("onload", "restoreScrollPosition()");
    public static final JavaScriptListener PERFORM_WINDOW_ONLOAD_SCRIPT = new JavaScriptListener("onload", "performWindowOnLoad()");

    /**
     * Externalizes the style sheet(s) for this session.
     * Look up according style sheet file name in org.wings.plaf.css.properties file under Stylesheet.BROWSERNAME.
     * The style sheet is loaded from the class path.
     * @return the URLs under which the css file(s) was externalized
     */
    private List externalizeBrowserStylesheets(Session session) {
        final ExternalizeManager extManager = session.getExternalizeManager();
        final CGManager manager = session.getCGManager();
        final String browserName = session.getUserAgent().getBrowserType().getShortName();
        final String cssResource = PROPERTY_STYLESHEET + browserName;
        String cssClassPaths = (String)manager.getObject(cssResource, String.class);
        // catch missing browser entry in properties file
        if (cssClassPaths == null) {
            cssClassPaths = (String)manager.getObject(PROPERTY_STYLESHEET + BROWSER_DEFAULT, String.class);
        }

        StringTokenizer tokenizer = new StringTokenizer(cssClassPaths,",");
        ArrayList cssUrls = new ArrayList();
        while (tokenizer.hasMoreTokens()) {
            String cssClassPath = tokenizer.nextToken();
            ClassPathResource res = new ClassPathResource(cssClassPath, "text/css");
            String cssUrl = extManager.externalize(res, ExternalizeManager.GLOBAL);
            if (cssUrl != null) {
                log.info("Attaching CSS Stylesheet "+cssClassPath+" found for browser "+browserName+" to frame. " +
                        "(See Stylesheet.xxx entries in default.properties)");
                cssUrls.add(cssUrl);
            } else {
                log.warn("Did not attach CSS Stylesheet "+cssClassPath+" for browser "+browserName+" to frame. " +
                        "(See Stylesheet.xxx entries in default.properties)");
            }
        }

        return cssUrls;
    }

    public void installCG(final SComponent comp) {
        final SFrame component = (SFrame) comp;

        // dynamic code resource.
        // This Resource externalized the HTML page
        DynamicCodeResource dynamicCodeRessource = new DynamicCodeResource(component);
        component.addDynamicResource(dynamicCodeRessource);
        component.addScriptListener(Utils.isMSIE(component) ? FOCUS_SCRIPT_IE : FOCUS_SCRIPT_MOZILLA);
        component.addScriptListener(SCROLL_POSITION_SCRIPT);
        component.addScriptListener(RESTORE_SCROLL_POSITION_SCRIPT);
        component.addScriptListener(PERFORM_WINDOW_ONLOAD_SCRIPT);
        CaptureDefaultBindingsScriptListener.install(component);

        headerUtil.installHeaders();

        // Retrieve list of static CSS files to be attached to this frame for this browser.
        final List externalizedBrowserCssUrls = externalizeBrowserStylesheets(component.getSession());
        for (int i = 0; i < externalizedBrowserCssUrls.size(); i++) {
            component.addHeader(new Link("stylesheet", null, "text/css", null, new DefaultURLResource((String) externalizedBrowserCssUrls.get(i))));
        }
    }

    /**
     * adds the file found at the classPath to the parentFrame header with
     * the specified mimeType
     * @param classPath the classPath to look in for the file
     * @param mimeType the mimetype of the file
     */
    private Script createExternalizedHeader(Session session, String classPath, String mimeType) {
        ClassPathResource res = new ClassPathResource(classPath, mimeType);
        String jScriptUrl = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        return new Script(mimeType, new DefaultURLResource(jScriptUrl));
    }

    /**
     * Uninstall renderer (i.e. other to apply other renderer).
     */
    public void uninstallCG(final SComponent comp) {
        final SFrame component = (SFrame) comp;

        component.removeDynamicResource(DynamicCodeResource.class);
        component.clearHeaders();
    }

    public void componentChanged(SComponent c) {
        /*
         * the update of the input maps happens on every write,
         * so here it's unnecessary.
         */
        //updateGlobalInputMaps(frame);
    }


    /**
     * @param frame
     */
    private void updateGlobalInputMaps(SFrame frame) {
        // here it goes, global input maps
        ScriptListener[] scriptListeners = frame.getScriptListeners();
        // first, delete all of them, they are from the last request...
        for (int i = 0; i < scriptListeners.length; i++) {
            ScriptListener scriptListener = scriptListeners[i];
            if (scriptListener instanceof InputMapScriptListener) {
                /*
                 * one could collect this as a list and only add/remove
                 * the changes. But the listeners are added as anonymous
                 * classes, which makes identifying them expensive. That
                 * would have to be changed.
                 */
                frame.removeScriptListener(scriptListener);
            }
        }
        // then install the ones we need for the request going on...
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

    public void write(final Device device, final SComponent pComp)
            throws IOException {
        final SFrame frame = (SFrame) pComp;
        /*
         * the input maps must be updated on every rendering of the SFrame, since
         * some components could be invisible in this request that registered an
         * input map before. To avoid too much code sent to the client, this update
         * is calles.
         */
        updateGlobalInputMaps(frame);
        RenderHelper.getInstance(frame).reset();
        if (!frame.isVisible()) {
            return;
        } else {
            frame.fireRenderEvent(SComponent.START_RENDERING);
        }

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

        device.print("<head>");
        if (title != null) {
            device.print("<title>");
            Utils.write(device, title);
            device.print("</title>\n");
        }

        // Character set encoding. default typically utf-8
        device.print("<meta http-equiv=\"Content-type\" content=\"text/html; charset=");
        Utils.write(device, encoding);
        device.print("\"/>\n");

        /* Insert version and compile time.
         * Since the Version Class is generated on compile time, build errors
         * in SDK's are quite normal. Just run the Version.java ant task.
         */
        device.print("<meta http-equiv=\"Generator\" content=\"wingS (http://www.j-wings.org) v");
        device.print(Version.getVersion());
        device.print(" built on: ");
        device.print(Version.getCompileTime());
        device.print("\" />\n");

        // Register and render DWR callables
        Collection callableNames = CallableManager.getInstance().callableNames();

        Collection allHeaders = new ArrayList(headers.size() + callableNames.size() + Headers.INSTANCE.size());
        allHeaders.addAll(Headers.INSTANCE);
        allHeaders.addAll(headers);
        for (Iterator iterator = callableNames.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            Script header = new Script("text/javascript", new DefaultURLResource("../dwr/interface/" + name + ".js"));
            allHeaders.add(header);
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
                // add new focus gain script
                gainFocusScript = new FocusScriptListener(focus);
                frame.addScriptListener(gainFocusScript);
                frame.putClientProperty("gain-focus-script", gainFocusScript);
            }
        }

        // TODO: move this to a dynamic script resource
        SToolTipManager toolTipManager = frame.getSession().getToolTipManager();
        device
                .print("<script type=\"text/javascript\">\n")
                .print("domTT_addPredefined('default', 'caption', false");
        if (toolTipManager.isFollowMouse()) {
            device.print(", 'trail', true");
        }
        device.print(", 'delay', ").print(toolTipManager.getInitialDelay());
        device.print(", 'lifetime', ").print(toolTipManager.getDismissDelay());
        device
                .print(");\n")
                .print("</script>\n");

        device.print("</head>\n");
        device.print("<body");
        Utils.writeEvents(device, frame, null);
        AbstractComponentCG.writeAllAttributes(device, frame);
        device.print(">\n");
        if (frame.isVisible()) {
            // now add JS for DnD if neccessary.
            DragAndDropManager dndManager = frame.getSession().getDragAndDropManager();
            List dragComponents = null;
            List dropComponents = null;
            Iterator dragIter = null;
            Iterator dropIter = null;
            if (dndManager.isVisible()) {
                dragComponents = dndManager.getDragSources();
                dropComponents = dndManager.getDropTargets();
                dragIter = dragComponents.iterator();
                dropIter = dropComponents.iterator();
                if (dragIter.hasNext()) {
                    // this needs to be added to the body, so use device.print()
                    // TODO: is caching by the VM enough or make this only initialize once?
                    ClassPathResource res = new ClassPathResource(WZ_DND_JS, "text/javascript");
                    String jScriptUrl = frame.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
                    device.print("<script type=\"text/javascript\" src=\"");
                    device.print(jScriptUrl);
                    device.print("\"></script>\n");
                }
            }

            frame.getLayout().write(device);
            device.print("\n");
            // now add all menus
            device.print(RenderHelper.getInstance(frame).getMenueRenderBuffer().toString());

            // now add final JS for DnD if neccessary.
            if (dndManager.isVisible() && dragIter != null && dragIter.hasNext()) { // initialize only if dragSources are present
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
                ClassPathResource res = new ClassPathResource(DND_JS, "text/javascript");
                String jScriptUrl = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
                device.print("<script type=\"text/javascript\" src=\"");
                device.print(jScriptUrl);
                device.print("\"></script>\n");
            }
        }
        writeInlineScripts(device, frame);
        device.print("\n</body>\n</html>\n");
        pComp.fireRenderEvent(SComponent.DONE_RENDERING);
        RenderHelper.getInstance(frame).reset();
    }

    protected void writeInlineScripts(Device device, SComponent component) throws IOException {
        boolean scriptTagOpen = false;
        ScriptListener[] scriptListeners = component.getScriptListeners();
        for (int i = 0; i < scriptListeners.length; i++) {
            ScriptListener scriptListener = scriptListeners[i];
            String script = scriptListener.getScript();
            if (script != null) {
                if (!scriptTagOpen) {
                    device.print("\n<script type=\"text/javascript\">");
                    scriptTagOpen = true;
                }
                device.print(script);
            }
        }

        ScriptManager scriptManager = component.getSession().getScriptManager();
        scriptListeners = scriptManager.getScriptListeners();

        for (int i = 0; i < scriptListeners.length; i++) {
            ScriptListener scriptListener = scriptListeners[i];
            String script = scriptListener.getScript();
            if (!scriptTagOpen) {
                device.print("\n<script type=\"text/javascript\">");
                scriptTagOpen = true;
            }
            device.print(script);
        }

        scriptManager.clearScriptListeners();
        
        if (scriptTagOpen) {
            device.print("</script>");
        }
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
}
