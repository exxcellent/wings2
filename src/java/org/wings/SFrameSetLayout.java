/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import org.wings.io.Device;
import org.wings.session.Session;
import org.wings.session.SessionManager;

import java.io.IOException;
import java.util.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SFrameSetLayout
    extends SAbstractLayoutManager
{
    private List components = new LinkedList();
    private List constraints = new LinkedList();

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
	StringBuffer buffer = new StringBuffer(c[0]);
	for (int i=1; i < c.length; i++) {
	    buffer.append(",");
	    buffer.append(c[i]);
	}
	setColumns(buffer.toString());
    }
    public void setColumns(String columns) {
	this.columns = columns;
	if (getContainer() != null)
	    getContainer().reload(ReloadManager.RELOAD_CODE);
    }

    public String getColumns() { return columns; }

    public void setRows(String[] r) {
	StringBuffer buffer = new StringBuffer(r[0]);
	for (int i=1; i < r.length; i++) {
	    buffer.append(",");
	    buffer.append(r[i]);
	}
	setRows(buffer.toString());
    }

    public void setRows(String rows) {
	this.rows = rows;
	if (getContainer() != null)
	    getContainer().reload(ReloadManager.RELOAD_CODE);
    }

    public String getRows() { return rows; }

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
	throws IOException
    {
	SFrameSet frameSet = (SFrameSet)getContainer();

	if (frameSet.getParent() == null) {
	    String language = "en"; // TODO: ???
	    String title = frameSet.getTitle();

	    d.print("<?xml version=\"1.0\" encoding=\"");
	    d.print(charSetFor(frameSet.getSession().getLocale()));
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
	    d.print(title);
	    d.print("</title>\n");
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

	d.print(">\n");

	Iterator iterator = components.iterator();
	int i=0;
	while (iterator.hasNext()) {
	    Object component = iterator.next();
	    if (component instanceof SFrameSet)
		((SFrameSet)component).write(d);
	    else if (component instanceof SFrame)
		writeFrame(d, (SFrame)component, (Properties)constraints.get(i));
	    else
		break;
	    i++;
	}

	d.print("</frameset>\n");
    }

    protected void writeFrame(Device d, SFrame frame, Properties properties)
	throws IOException
    {
	d.print("<frame src=\"");
        d.print(frame.getDynamicResource(DynamicCodeResource.class).getURL());
        d.print("\"");
	d.print(" name=\"frame")
	    .print(frame.getComponentId())
	    .print("\"");

	if (properties != null) {
	    Iterator iterator = properties.entrySet().iterator();
	    while (iterator.hasNext()) {
		Map.Entry entry = (Map.Entry)iterator.next();
		d.print(' ');
		d.print((String)entry.getKey());
		d.print("=\"");
		d.print((String)entry.getValue());
		d.print('"');
	    }
	}
	d.print("/>\n");
    }

    private String charSetFor(Locale locale) {
        final String language = locale.getLanguage();

        if(language.equals("pl"))
            return "iso-8859-2";

        return "iso-8859-1";
    }

    private transient Session session = null;

    protected Session getSession() {
	if (session == null)
	    session = SessionManager.getSession();
	return session;
    }

    public void updateCG() {}
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
