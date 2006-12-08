package org.wings.plaf;

import java.util.Iterator;

import org.wings.SComponent;

public interface Update {

    public static final int STANDARD_UPDATE = 0;
    public static final int MULTIPLE_UPDATE = 1;

    public SComponent getComponent();

    public long getPositioning();

    public int getProperty();

    public Handler getHandler();

    public interface Handler {

        public String getName();

        public void addParameter(Object param);

        public Iterator getParameters();

    }

}