package org.wings.event;

/**
 * Created by IntelliJ IDEA.
 * User: hengels
 * Date: Jul 14, 2006
 * Time: 12:10:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class STableColumnModelEvent
    extends java.util.EventObject
{
    int from;
    int to;

    public STableColumnModelEvent(Object source, int from, int to) {
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
