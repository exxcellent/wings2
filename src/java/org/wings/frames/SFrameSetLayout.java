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
package org.wings.frames;

import org.wings.Renderable;
import org.wings.SAbstractLayoutManager;
import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.io.Device;
import org.wings.resource.ReloadResource;
import org.wings.session.SessionManager;
import org.wings.session.Session;
import org.wings.util.SStringBuilder;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * A special layout manager for the root frame of a frameset layout.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public class SFrameSetLayout extends SAbstractLayoutManager {
    private final List<SComponent> components = new LinkedList<SComponent>();
    private final List<Object> constraints = new LinkedList<Object>();

    private String columns;
    private String rows;

    public SFrameSetLayout(String cols, String rows) {
        setColumns(cols);
        setRows(rows);
    }

    public SFrameSetLayout(String[] cols, String[] rows) {
        setColumns(cols);
        setRows(rows);
    }

    public void setColumns(String[] c) {
        SStringBuilder buffer = new SStringBuilder(c[0]);
        for (int i = 1; i < c.length; i++) {
            buffer.append(",");
            buffer.append(c[i]);
        }
        setColumns(buffer.toString());
    }

    public void setColumns(String columns) {
        this.columns = columns;
        if (getContainer() != null)
            getContainer().reload();
    }

    public String getColumns() {
        return columns;
    }

    public void setRows(String[] r) {
        SStringBuilder buffer = new SStringBuilder(r[0]);
        for (int i = 1; i < r.length; i++) {
            buffer.append(",");
            buffer.append(r[i]);
        }
        setRows(buffer.toString());
    }

    public void setRows(String rows) {
        this.rows = rows;
        if (getContainer() != null)
            getContainer().reload();
    }

    public String getRows() {
        return rows;
    }

    public void addComponent(SComponent c, Object constraint, int index) {
        components.add(index, c);
        constraints.add(constraint);
    }

    public void removeComponent(SComponent c) {
        if (c == null)
            return;

        int index = components.indexOf(c);
        components.remove(index);
        constraints.remove(index);
    }

    public void write(Device d)
            throws IOException {
        SFrameSet frameSet = (SFrameSet) getContainer();
        List headers = frameSet.getHeaders();

        if (frameSet.getParent() == null) {
            Session session = SessionManager.getSession();
            final String language = session.getLocale().getLanguage();
            final String title = frameSet.getTitle();
            final String encoding = session.getCharacterEncoding();

            d.print("<?xml version=\"1.0\" encoding=\"");
            d.print(encoding);
            d.print("\"?>\n");
            d.print("<!DOCTYPE html\n");
            d.print("   PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\"\n");
            d.print("   \"DTD/xhtml1-frameset.dtd\">\n");
            d.print("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"");
            d.print(language);
            d.print("\" lang=\"");
            d.print(language);
            d.print("\">\n");
            d.print("<head>\n<title>");
            org.wings.plaf.css.Utils.write(d, title);
            d.print("</title>\n");

            Iterator it = headers.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (next instanceof Renderable) {
                    ((Renderable) next).write(d);
                } else {
                    d.print(next.toString());
                }
                d.print("\n");
            }

            d.print("</head>\n");
        }

        d.print("<frameset");

        if (columns != null && columns.length() > 0) {
            d.print(" cols=\"");
            d.print(columns);
            d.print("\"");
        }

        if (rows != null && rows.length() > 0) {
            d.print(" rows=\"");
            d.print(rows);
            d.print("\"");
        }

        if (!frameSet.isFrameBorderVisible()) {
            d.print(" frameborder=\"0\" framespacing=\"0\" border=\"0\"");
        }

        d.print(">\n");

        Iterator iterator = components.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Object component = iterator.next();
            if (component instanceof SFrameSet)
                ((SFrameSet) component).write(d);
            else if (component instanceof SFrame)
                writeFrame(d, (SFrame) component, (Properties) constraints.get(i));
            else
                continue;
            i++;
        }

        d.print("</frameset>\n");
    }

    protected void writeFrame(Device device, SFrame frame, Properties properties)
            throws IOException {
        device.print("<frame src=\"");
        device.print(frame.getDynamicResource(ReloadResource.class).getURL());
        device.print("\"");
        device.print(" name=\"");
        device.print(SFrameSet.createBaseTargetName(frame));
        device.print("\"");

        if (properties != null) {
            Iterator iterator = properties.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                device.print(' ');
                device.print((String) entry.getKey());
                device.print("=\"");
                device.print((String) entry.getValue());
                device.print('"');
            }
        }
        /* I personally do not actually need this one, since it is
         frameset specific perhaps it should be a constraint of the layout
         if ( !frame.isResizable() ){
         device.print(" noresize");
         } */
        device.print("/>\n");
    }

    public void updateCG() {
    }

}