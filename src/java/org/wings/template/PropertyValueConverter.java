/*
  $Id$
  (c) Copyright 2002 mercatis information systems GmbH

  Part of e-lib 
 
  This file contains unpublished, proprietary trade secret information of
  mercatis information systems GmbH. Use, transcription, duplication and
  modification are strictly prohibited without prior written consent of
  mercatis information systems GmbH.
  See http://www.mercatis.de
*/

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
package org.wings.template;

/**
 * A PropertyValueConverter is able to convert a property value (a string) to an Java object.
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public interface PropertyValueConverter {

    /**
     * Describe <code>toObject</code> method here.
     *
     * @param value       a <code>String</code> value
     * @param targetClass a <code>Class</code> value
     * @return an <code>Object</code> value
     * @throws IllegalArgumentException if an error occurs
     */
    public Object convertPropertyValue(String value, Class targetClass)
            throws IllegalArgumentException;

}// PropertyValueConverter

