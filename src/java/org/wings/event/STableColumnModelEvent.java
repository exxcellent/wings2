package org.wings.event;

import org.wings.table.STableColumnModel;

public class STableColumnModelEvent extends java.util.EventObject {
    protected int from;
    protected int to;

    public STableColumnModelEvent(STableColumnModel source, int from, int to) {
        super(source);
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}
