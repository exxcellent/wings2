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
package org.wings.plaf.css;

import javax.swing.InputMap;
import javax.swing.KeyStroke;

/**
 * @author hengels
 */
public class VersionedInputMap extends InputMap {
    private static final long serialVersionUID = 1L;
    InputMap inputMap;
    int version = 0;

    public VersionedInputMap() {
    }

    public VersionedInputMap(InputMap inputMap) {
        this.inputMap = inputMap;
    }

    public int size() {
        return inputMap.size();
    }

    public void clear() {
        version++;
        inputMap.clear();
    }

    public InputMap getParent() {
        return inputMap.getParent();
    }

    public void setParent(InputMap map) {
        version++;
        inputMap.setParent(map);
    }

    public KeyStroke[] allKeys() {
        return inputMap.allKeys();
    }

    public KeyStroke[] keys() {
        return inputMap.keys();
    }

    public void remove(KeyStroke key) {
        version++;
        inputMap.remove(key);
    }

    public Object get(KeyStroke keyStroke) {
        return inputMap.get(keyStroke);
    }

    public void put(KeyStroke keyStroke, Object actionMapKey) {
        version++;
        inputMap.put(keyStroke, actionMapKey);
    }

    public int getVersion() {
        return version;
    }
}
