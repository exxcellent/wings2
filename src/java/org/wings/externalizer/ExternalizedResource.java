/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.externalizer;

import java.util.Collection;

import org.wings.session.Session;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ExternalizedResource
{
    /*
     * the following two members are the key elements,
     * used by hashCode() and equals();
     */
    private final Object        extObject;
    private final Externalizer  externalizer;

    private final String        mimeType;
    private final int           flags;
    private final long          lastModified;
    private final Collection    headers;
    private String              id;

    public ExternalizedResource(Object obj, Externalizer ext, 
                                String mt, Collection headers, int flags) {
        extObject   = obj;
        externalizer= ext;
        mimeType    = mt;
        this.flags  = flags;
        
        if ( externalizer==null || extObject==null ) {
            throw new IllegalArgumentException("no externalizer or null object");
        }
        
        lastModified = System.currentTimeMillis();
        this.headers = headers;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final String getMimeType() {
        return mimeType;
    }
    
    /**
     * TODO: documentation
     *
     * @return
     */
    public final Object getObject() {
        return extObject;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final Externalizer getExternalizer() {
        return externalizer;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final Collection getHeaders() {
        return headers;
    }
    

    /**
     * TODO: documentation
     *
     * @return
     */
    public final boolean deliverOnce() {
        return (flags & AbstractExternalizeManager.REQUEST) > 0;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final boolean isFinal() {
        return (( (flags & AbstractExternalizeManager.FINAL) > 0 
                  || externalizer.isFinal(extObject) ) 
                // if flags is request only, then object is not final!!
                && (flags & AbstractExternalizeManager.REQUEST) == 0);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final String getExtension() {
        return externalizer.getExtension(extObject);
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final int getFlags() {
        return flags;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public final long getLastModified() {
        if ( isFinal() ) 
            return lastModified;
        else
            return -1;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    void setId(String s) {
        id = s;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        return "Externalized Info";
    }

    /**
     * The HashCode of the externalized resource is
     * the hash code of the to be externalized object
     * (the externalized resource). Together with the implementation
     * of the {@link equals(Object)}-Method, this makes sure, that
     * the same resource gets the same ID.
     *
     * @return the has code of the externalized object.
     */
    public final int hashCode() {
        return extObject.hashCode();
    }

    /**
     * If the managed externalized resource and the
     * externalized manager are the same, then both
     * ExternalizedResource objects are regarded equal.
     *     
     * @return true, if the other externalized resource equals
     *         this resource regarding the key members.
     */
    public final boolean equals(ExternalizedResource e) {
        return (extObject.equals(e.extObject)
                && externalizer.equals(e.externalizer));
    }

    /**
     * @return true, if the other object is an ExternalizedResource
     *         and {@link equals(ExternalizedResource)} returns true.
     */
    public final boolean equals(Object o) {
        if ( o instanceof ExternalizedResource ) {
            return equals((ExternalizedResource)o);
        }
        return false;
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */




