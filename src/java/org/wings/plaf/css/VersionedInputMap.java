/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;

import javax.swing.*;

/**
 * @author hengels
 * @version $Revision$
 */
class VersionedInputMap extends InputMap {
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
