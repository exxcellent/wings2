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
package org.wings.event;

/**
 * For vetoing an application triggered session exit. 
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public class ExitVetoException extends Exception {


    public ExitVetoException(String message) {
        super(message);
    }


    public ExitVetoException(String message, Throwable cause) {
        super(message, cause);
    }

}

