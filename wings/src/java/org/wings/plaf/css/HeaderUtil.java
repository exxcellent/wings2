package org.wings.plaf.css;

import org.wings.header.Headers;

import java.util.*;

public class HeaderUtil
{
    private List headers;

    protected void addHeaders(List headers) {
        if (this.headers == null)
            this.headers = new LinkedList();

        this.headers.addAll(headers);
    }

    public void addHeader(Object header) {
        if (headers == null)
            headers = new LinkedList();
        this.headers.add(header);
    }

    public void installHeaders() {
        if (headers == null || headers.size() == 0)
            return;

        if (!Headers.INSTANCE.contains(headers.iterator().next()))
            Headers.INSTANCE.addAll(headers);
    }

    public void uninstallHeaders() {
        if (headers == null || headers.size() == 0)
            return;

        Headers.INSTANCE.removeAll(headers);
    }
}
