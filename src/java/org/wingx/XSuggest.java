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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SFrame;
import org.wings.STextField;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Link;
import org.wings.header.Script;
import org.wings.header.StyleSheetLink;
import org.wings.plaf.FrameCG;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.resource.ClassPathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.resource.StringResource;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;
import org.wings.session.Session;
import org.wings.session.SessionManager;
import org.wingx.XSuggestDataSource;

/**
 * Enhanced STextField that supports the user input by displaying suggestions.
 *
 * @author Christian Schyma
 */
public class XSuggest extends STextField {
    
    private XSuggestDataSource dataSource = null;
    private final String DWR_JS_OBJECT = this.getName() + "_data";
    public final String POPULATOR_NAME = this.getName() + "_populator";
    private final String CALLBACK_FUNCTION = "callback:function(suggestions) {suggestWidget.updateSuggestions(suggestions);}, timeout: 6000, errorHandler: function(message) {alert(message);}";
    private final String POPULATOR_FUNCTION = "function "+ this.POPULATOR_NAME +"(suggestWidget, token) {"+
            DWR_JS_OBJECT +".getData(token, {"+ CALLBACK_FUNCTION  +"});}";
    
    private Script libMKBase;
    private Script suggestJS;    
    private Script libMKIter;    
    private Script libMKDOM;    
    private Script libMKLog;    
    private Script libMKStyle;    
    private Script libMKColor;    
    private Script libMKVisual;    
    private Script libYahoo;    
    private Script libYahooEvent;
    private Link suggestCss;
    
    private final transient static Log log = LogFactory.getLog(XSuggest.class);
    
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
    
    private void importJavaScriptLibraries() {
        
        Session session = SessionManager.getSession();
                
        suggestCss = createExternalizedCSSHeader(session, "org/wingx/suggest/suggest.css");
        
        suggestJS = createExternalizedJavaScriptHeader(session, "org/wingx/suggest/suggest.js");
        
        libMKBase = createExternalizedJavaScriptHeader(session, "org/wingx/lib/MochiKit/Base.js");
        libMKIter = createExternalizedJavaScriptHeader(session, "org/wingx/lib/MochiKit/Iter.js");
        libMKDOM = createExternalizedJavaScriptHeader(session, "org/wingx/lib/MochiKit/DOM.js");
        libMKLog = createExternalizedJavaScriptHeader(session, "org/wingx/lib/MochiKit/Logging.js");
        libMKStyle = createExternalizedJavaScriptHeader(session, "org/wingx/lib/MochiKit/Style.js");
        libMKColor = createExternalizedJavaScriptHeader(session, "org/wingx/lib/MochiKit/Color.js");
        libMKVisual = createExternalizedJavaScriptHeader(session, "org/wingx/lib/MochiKit/Visual.js");
        
        libYahoo = createExternalizedJavaScriptHeader(session, "org/wingx/lib/YahooUI/yahoo/yahoo.js");
        libYahooEvent = createExternalizedJavaScriptHeader(session, "org/wingx/lib/YahooUI/event/event.js");
                
        addParentFrameListener(new SParentFrameListener() {
            public void parentFrameAdded(SParentFrameEvent e) {
                SFrame frame = getParentFrame();
                frame.addHeader(libMKBase);                
                frame.addHeader(libMKIter);
                frame.addHeader(libMKDOM);
                frame.addHeader(libMKLog);
                frame.addHeader(libMKStyle);
                frame.addHeader(libMKColor);
                frame.addHeader(libMKVisual);
                
                frame.addHeader(libYahoo);
                frame.addHeader(libYahooEvent);
                
                frame.addHeader(suggestJS);
                
                frame.addHeader(suggestCss);
            }
            public void parentFrameRemoved(SParentFrameEvent e) {
                // TODO: ScriptManager to remove not used headers?
            }
        
        });
            
    }
    
    private void prepareDWRAccess() {
        
        log.debug("preparing access to DWR");
        
        CallableManager.getInstance().registerCallable(DWR_JS_OBJECT, dataSource);
        
        addParentFrameListener(new SParentFrameListener() {
            public void parentFrameAdded(SParentFrameEvent e) {
                StringResource stringResource = new StringResource(POPULATOR_FUNCTION, "js", "text/javascript");
                String url = SessionManager.getSession().getExternalizeManager().externalize(stringResource);
                getParentFrame().addHeader(new Script("text/javascript", new DefaultURLResource(url)));
                getParentFrame().addScriptListener(new JavaScriptListener(JavaScriptEvent.ON_LOAD,
                        "DWRUtil.useLoadingMessage(); new wingS.Suggest(\""+ e.getComponent().getName() +"\", "+
                        ((XSuggest)e.getSource()).POPULATOR_NAME +");"));
            }
            public void parentFrameRemoved(SParentFrameEvent e) {
                
            }
        });
    }
    
    /**
     * Creates a text field that displays suggestions based upon given source.
     * @param source source of suggestions
     */
    public XSuggest(XSuggestDataSource source) {
        this.dataSource = source;
        prepareDWRAccess();
        importJavaScriptLibraries();
    }
    
    /**
     * Creates a text field that displays suggestions based upon given source.
     * @param text initial content of the text field
     * @param source source of suggestions
     */
    public XSuggest(String text, XSuggestDataSource source) {
        super(text);
        this.dataSource = source;
        prepareDWRAccess();
        importJavaScriptLibraries();
    }
    
}
