package org.wings.event;

import org.wings.table.STableColumnModel;

/**
 * Created by IntelliJ IDEA.
 * User: hengels
 * Date: Jul 14, 2006
 * Time: 12:10:15 PM
 * To change this template use File | Settings | File Templates.
 */
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
