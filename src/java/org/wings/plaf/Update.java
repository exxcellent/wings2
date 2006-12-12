package org.wings.plaf;

import java.util.Iterator;

import org.wings.SComponent;

public interface Update {

    public static final int DEFAULT_INCREMENTAL_UPDATE = 0;
    public static final int AFFECTS_COMPLETE_COMPONENT = 1;
    public static final int ALLOWS_SEVERAL_OF_THE_SAME = 2;

    public SComponent getComponent();

    public int getProperty();

    public int getPriority();

    public Handler getHandler();

    public interface Handler {

        public String getName();

        public void addParameter(Object param);

        public Iterator getParameters();

    }

}