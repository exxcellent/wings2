/*
 * $Id: wings.js 2790 2006-10-05 13:59:11Z cjschyma $
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

package org.wingx;

import org.wings.SConstants;
import org.wings.SFrame;
import org.wings.SLabel;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Link;
import org.wings.header.Script;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.resource.ClassPathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.resource.StringResource;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;
import org.wings.session.Session;
import org.wings.session.SessionManager;

/**
 *
 * @author <a href="mailto:C.Schyma@eXXcellent.de">Christian Schyma</a> 
 */
public class XInplaceEditor extends SLabel {
    
    private Link inplaceCss;
    private Link yahooContainerCss;
    
    private Script inplaceJS;
    private Script libMKBase;
    private Script libMKIter;
    private Script libMKDOM;    
    private Script libMKStyle;
    private Script libYahoo;
    private Script libYahooEvent;
    private Script libYahooDom;
    private Script libYahooContainer;
    
    private final String DWR_JS_OBJECT = this.getName() + "_data";
    
    /** Creates a new instance of XInplaceEditor */
    public XInplaceEditor() {
        prepareDWRAccess();
        importJavaScriptLibraries();
        importCSS();
    }
    
    /**
     * Creates a new <code>XInplaceEditor</code> instance with the specified text
     * (left aligned).
     *
     * @param text The text to be displayed by the label.
     */
    public XInplaceEditor(String text) {
        setText(text);
        setHorizontalAlignment(SConstants.LEFT);
        
        prepareDWRAccess();
        importJavaScriptLibraries();
        importCSS();
    }
    
    private void prepareDWRAccess() {
        CallableManager.getInstance().registerCallable(DWR_JS_OBJECT, new XInplaceEditorDataSource(this));
        
        addParentFrameListener(new SParentFrameListener() {
            public void parentFrameAdded(SParentFrameEvent e) {
                getParentFrame().addScriptListener(new JavaScriptListener(JavaScriptEvent.ON_LOAD,
                        "DWRUtil.useLoadingMessage(); new wingS.InplaceEditor(\""+ e.getComponent().getName() +"\", "+
                        ((XInplaceEditor)e.getSource()).DWR_JS_OBJECT +");"));
            }
            public void parentFrameRemoved(SParentFrameEvent e) {
                // nothing to do
            }
        });
    }
    
    private Script createExternalizedJavaScriptHeader(Session session, String classPath) {
        ClassPathResource res = new ClassPathResource(classPath, "text/javascript");
        String jScriptUrl = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        return new Script("text/javascript", new DefaultURLResource(jScriptUrl));
    }
    
    private Link createExternalizedCSSHeader(Session session, String classPath) {
        ClassPathResource res = new ClassPathResource(classPath, "text/css");
        String cssUrl = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        return new Link("stylesheet", null, "text/css", null, new DefaultURLResource(cssUrl));
    }
    
    private void importCSS() {
        Session session = SessionManager.getSession();
        
        inplaceCss = createExternalizedCSSHeader(session, "org/wingx/inplaceeditor/inplaceeditor.css");
        yahooContainerCss = createExternalizedCSSHeader(session, "org/wingx/lib/YahooUI/container/assets/container.css");
        
        addParentFrameListener(new SParentFrameListener() {
            public void parentFrameAdded(SParentFrameEvent e) {
                SFrame frame = getParentFrame();
                frame.addHeader(inplaceCss);
                frame.addHeader(yahooContainerCss);
            }
            public void parentFrameRemoved(SParentFrameEvent e) {
            }
        });
    }
    
    private void importJavaScriptLibraries() {
        
        Session session = SessionManager.getSession();
        
        inplaceJS = createExternalizedJavaScriptHeader(session, "org/wingx/inplaceeditor/inplaceeditor.js");
        
        libMKBase = createExternalizedJavaScriptHeader(session, "org/wingx/lib/MochiKit/Base.js");
        libMKIter = createExternalizedJavaScriptHeader(session, "org/wingx/lib/MochiKit/Iter.js");
        libMKDOM = createExternalizedJavaScriptHeader(session, "org/wingx/lib/MochiKit/DOM.js");
        libMKStyle = createExternalizedJavaScriptHeader(session, "org/wingx/lib/MochiKit/Style.js");
        
        libYahoo = createExternalizedJavaScriptHeader(session, "org/wingx/lib/YahooUI/yahoo/yahoo.js");
        libYahooEvent = createExternalizedJavaScriptHeader(session, "org/wingx/lib/YahooUI/event/event.js");
        libYahooDom = createExternalizedJavaScriptHeader(session, "org/wingx/lib/YahooUI/dom/dom.js");
        libYahooContainer = createExternalizedJavaScriptHeader(session, "org/wingx/lib/YahooUI/container/container.js");
        
        addParentFrameListener(new SParentFrameListener() {
            public void parentFrameAdded(SParentFrameEvent e) {
                SFrame frame = getParentFrame();
                
                frame.addHeader(libMKBase);
                frame.addHeader(libMKIter);
                frame.addHeader(libMKDOM);                
                frame.addHeader(libMKStyle);                
                
                frame.addHeader(libYahoo);
                frame.addHeader(libYahooEvent);
                frame.addHeader(libYahooDom);
                frame.addHeader(libYahooContainer);
                
                frame.addHeader(inplaceJS);
                
                
                
                
            }
            public void parentFrameRemoved(SParentFrameEvent e) {
                // TODO: ScriptManager to remove not used headers?
            }
            
        });
        
    }
    
    public class XInplaceEditorDataSource {
        
        private XInplaceEditor editor;
        
        /** Creates a new instance of XInplaceEditorDataSource */
        public XInplaceEditorDataSource(XInplaceEditor ed) {
            this.editor = ed;
        }
        
        public String setText(String text) {
            this.editor.setText(text);            
            return this.editor.getText();                        
        }
    }
    
    
}



