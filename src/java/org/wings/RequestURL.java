/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import org.wings.io.Device;
import org.wings.util.SStringBuilder;
import org.wings.session.Session;
import org.wings.session.SessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Handles a HTTP GET Address that can be updated with additional parameters.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class RequestURL extends SimpleURL {
    private static final Log log = LogFactory.getLog(RequestURL.class);

    private static final String DEFAULT_RESOURCE_NAME = "_";

    private String baseParameters;

    private boolean hasQuestMark;

    private String eventEpoch;

    private String resource;

    private SStringBuilder parameters = null;

    /**
     * Current session encoding used for URLEncoder.
     */
    private final String characterEncoding;

    public RequestURL() {
        this.characterEncoding = determineCharacterEncoding();
    }

    /**
     * copy constructor.
     */
    private RequestURL(RequestURL other) {
        this.characterEncoding = determineCharacterEncoding();
        this.baseURL = other.baseURL;
        this.baseParameters = other.baseParameters;
        this.hasQuestMark = other.hasQuestMark;
        this.eventEpoch = other.eventEpoch;
        this.resource = other.resource;
        SStringBuilder params = other.parameters;
        parameters = (params == null) ? null : new SStringBuilder(params.toString());
    }


    public RequestURL(String baseURL, String encodedBaseURL) {
        this.characterEncoding = determineCharacterEncoding();
        setBaseURL(baseURL, encodedBaseURL);
    }


    public void setEventEpoch(String e) {
        eventEpoch = e;
    }


    public String getEventEpoch() {
        return eventEpoch;
    }


    public void setResource(String r) {
        resource = r;
    }


    public String getResource() {
        return resource;
    }


    public void setBaseURL(String b, String encoded) {
        baseURL = b;

        baseParameters = encoded.substring(b.length());
        if (baseParameters.length() == 0)
            baseParameters = null;

        if (baseParameters != null)
            hasQuestMark = baseParameters.indexOf('?') >= 0;
        else
            hasQuestMark = false;
    }


    /**
     * Add an additional parameter to be included in the GET paramter
     * list. Usually, this paramter will be in the form 'name=value'.
     *
     * @param parameter to be included in the GET parameters.
     * @return a reference to <code>this</code> to simplify 'call chaining'
     */
    public RequestURL addParameter(String parameter) {
        if (parameter != null) {
            if (parameters == null)
                parameters = new SStringBuilder();
            else
                parameters.append("&amp;");
            parameters.append(recode(parameter));
        }
        return this;
    }

    /**
     * Add an additional name/value pair to be included in the GET paramter
     * list. The added parameter will be 'name=value'
     *
     * @param name  the name of the parameter
     * @param value the value of the parameter
     * @return a reference to <code>this</code> to simplify 'call chaining'
     */
    public RequestURL addParameter(String name, String value) {
        addParameter(name);
        parameters.append("=").append(recode(value));
        return this;
    }

    /**
     * Add an additional name/value pair to be included in the GET paramter
     * list. The added name will be the LowLevelEventId of the LowLevelEventListener.
     *
     * @param value the value of the parameter
     * @return a reference to <code>this</code> to simplify 'call chaining'
     */
    public RequestURL addParameter(LowLevelEventListener comp, String value) {
        addParameter(comp.getLowLevelEventId(), value);
        return this;
    }

    /**
     * Add an additional name/value pair to be included in the GET paramter
     * list. The added parameter will be 'name=value'
     *
     * @param name  the name of the parameter
     * @param value the value of the parameter
     * @return a reference to <code>this</code> to simplify 'call chaining'
     */
    public RequestURL addParameter(String name, int value) {
        addParameter(name);
        parameters.append("=").append(value);
        return this;
    }

    /**
     * clear all additional paramters given in the {@link #addParameter(String)} call.
     */
    public void clear() {
        if (parameters != null) {
            parameters.setLength(0);
        }
        setEventEpoch(null);
        setResource(null);
    }

    /**
     * Writes the context Address to the output Device. Appends all
     * parameters given. Only the context URL is given, since all GET urls generated
     * by wings are relative to the WingS servlet.
     * Tries to avoid charset conversion as much as possible by precalculating the
     * byteArray representation of the non-parameter part.
     *
     * @param d the Device to write to
     */
    public void write(Device d) throws IOException {
        super.write(d);

        if (resource != null && eventEpoch != null) {
            d.print(eventEpoch);
            d.print(SConstants.UID_DIVIDER);
        }

        if (resource != null) {
            d.print(resource);
        } else {
            /*
             * The default resource name. Work around a bug in some
             * browsers that fail to assemble URLs.
             * (TODO: verify and give better explanation here).
             */
            d.print(DEFAULT_RESOURCE_NAME);
        }

        if (baseParameters != null) {
            d.print(baseParameters);
        }

        if (parameters != null && parameters.length() > 0) {
            d.print(hasQuestMark ? "&amp;" : "?");
            d.print(parameters.toString());
        }
    }

    /**
     * Returns the string representation of the context URL plus
     * all paramters given.
     */
    public String toString() {
        SStringBuilder erg = new SStringBuilder();

        if (baseURL != null) {
            erg.append(baseURL);
        }

        if (resource != null && eventEpoch != null) {
            erg.append(eventEpoch);
            erg.append(SConstants.UID_DIVIDER);
        }

        if (resource != null) {
            erg.append(resource);
        } else {
            erg.append(DEFAULT_RESOURCE_NAME);
        }

        if (baseParameters != null) {
            erg.append(baseParameters);
        }

        if (parameters != null && parameters.length() > 0) {
            erg.append(hasQuestMark ? "&" : "?");
            erg.append(parameters.toString());
        }

        return erg.toString();
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!super.equals(o)) return false;
        RequestURL other = (RequestURL) o;
        return (hasQuestMark == other.hasQuestMark
                && eq(baseParameters, other.baseParameters)
                && eq(eventEpoch, other.eventEpoch)
                && eq(resource, other.resource)
                && eq(parameters, other.parameters));
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return baseURL != null ? baseURL.hashCode() : 0;
    }

    /**
     * Deep copy.
     *
     * @return object with cloned contents
     */
    public Object clone() {
        return new RequestURL(this);
    }

    private final boolean eq(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    /**
     * Determine the current character encoding.
     * @return Character encoding string or <code>null</code>
     */
    private String determineCharacterEncoding() {
        String characterEncoding = null;
        final Session session = SessionManager.getSession();
        if (session != null) {
            characterEncoding = session.getCharacterEncoding();
        }
        return characterEncoding;
    }

    /**
     * Recoded passes string to URL with current encoding.
     * @param parameter String to recode
     * @return Encoded parameter or same if an error occured
     */
    private String recode(String parameter) {
        try {
            return URLEncoder.encode(parameter, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            log.warn("Unknown character encoding?! "+characterEncoding, e);
            return parameter;
        }
    }
}
