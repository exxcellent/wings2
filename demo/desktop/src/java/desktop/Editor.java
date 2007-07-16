/*
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of the wingS demo (http://j-wings.org).
 *
 * The wingS demo is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package desktop;

import org.wings.*;
import org.wings.dnd.DragSource;
import org.wings.dnd.DropTarget;
import org.wings.event.SComponentDropListener;
import org.wings.event.SInternalFrameEvent;
import org.wings.event.SInternalFrameListener;
import org.wings.externalizer.AbstractExternalizeManager;
import org.wings.externalizer.ExternalizeManager;
import org.wings.io.Device;
import org.wings.resource.DynamicResource;
import org.wings.resource.FileResource;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;
import org.wings.session.SessionManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Desktop example demonstrates the use of internal frames as well as
 * file upload and download.
 * SInternalFrames work very similar to their Swing pendants. The file upload
 * is left to the SFileChooser. Beware, that if you use one or more input
 * elements of type="file", you have to set the encoding of the surrounding form
 * to "multipart/form-data". This is a requirement of html. Download is a bit
 * tricky. The text has to be externalized for example by using the class
 * {@see org.wings.FileResource}. A JavaScriptListener, that is hooked to the
 * java script event "onload", is installed in the frame.
 * Look at the source, especially the method "save".
 * <p>
 * As of now, the menu item "save" in the "file" menu does not work as expected.
 * It is rendered as a href outside the form. Changes to text area don't take
 * effect. We could use javascript again, to trigger the required form submit.
 *
 * @author Holger Engels
 */
public class Editor
    extends SInternalFrame
    implements SInternalFrameListener, DragSource, DropTarget
{
	private ArrayList<SComponentDropListener> componentDropListeners = new ArrayList<SComponentDropListener>();
	private boolean dragEnabled = false;
	private DynamicResource saveResource;
    private SMenuBar menuBar;
    private SToolBar toolBar;
    private STextArea textArea;

    private String backup;
    private String clip;

    public Editor() {
    	this.setDragEnabled(true);
        componentDropListeners = new ArrayList<SComponentDropListener>();
         
        menuBar = createMenu();
        SContainer contentPane = getContentPane();
        contentPane.setLayout(new SBorderLayout());
        contentPane.add(menuBar, SBorderLayout.NORTH);
        toolBar = createToolBar();

        textArea = new STextArea();
        textArea.setColumns(500);
        textArea.setRows(12);
        textArea.setPreferredSize(SDimension.FULLWIDTH);
        textArea.setEditable(true);
        
        SForm form = new SForm(new SFlowDownLayout());
        form.add(toolBar);
        form.add(textArea);
        contentPane.add(form, SBorderLayout.CENTER);
        saveResource = new EditorDynamicResource();

        SIcon icon = new SURLIcon("../icons/penguin.png");
        setIcon(icon);
        addInternalFrameListener(this);
        
        
        addComponentDropListener(new SComponentDropListener() {
            public boolean handleDrop(SComponent dragSource) {
            	            	
            	SContainer cont = dragSource.getParent();
            	
            	if(Editor.this.getParent() != cont){
            		if(cont instanceof SDesktopPane){
            			
            			Editor.this.setMaximized(false);
            			if(dragSource instanceof SInternalFrame)
                    		((SInternalFrame)dragSource).setMaximized(false);
            			
            			SDesktopPane targetPane = (SDesktopPane)Editor.this.getParent();
            			SDesktopPane sourcePane = (SDesktopPane)cont;
            			sourcePane.remove(sourcePane.getIndexOf(dragSource));
            			targetPane.add(dragSource, targetPane.getIndexOf(Editor.this));
            			return true;
            		}
            	}
            	else if(cont instanceof SDesktopPane)
            	{
            		SDesktopPane pane = (SDesktopPane)cont;
            		int tindex = pane.getIndexOf(Editor.this);
            		int sindex = pane.getIndexOf(dragSource);
            		
            		//do nothing if drag on itself..
            		if(sindex == tindex)
            			return false;
            		
            		Editor.this.setMaximized(false);
            		if(dragSource instanceof SInternalFrame)
                		((SInternalFrame)dragSource).setMaximized(false);
            		
            		pane.remove(sindex);
            		
            		if(tindex > sindex)
                		pane.add(dragSource, tindex -1);
                	else               		
                		pane.add(dragSource, tindex);

                	return true;
            	}
            	return false;
            }
        });
    }
    
	public void addComponentDropListener(SComponentDropListener listener) {
    	componentDropListeners.add(listener);
        SessionManager.getSession().getDragAndDropManager().registerDropTarget(this);
	}

	public List<SComponentDropListener> getComponentDropListeners() {
		return this.componentDropListeners;
	}

	public boolean isDragEnabled() {
		return this.dragEnabled;
	}

	public void setDragEnabled(boolean dragEnabled) {
		this.dragEnabled = dragEnabled;
        if (dragEnabled) {
            SessionManager.getSession().getDragAndDropManager().registerDragSource(this);
        } else {
            SessionManager.getSession().getDragAndDropManager().deregisterDragSource(this);
        }
	}
 	
 
    protected SMenuBar createMenu() {
        SMenuItem saveItem = new SMenuItem("Save");
        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                save();
            }
        });
        SMenuItem revertItem = new SMenuItem("Revert");
        revertItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                revert();
            }
        });
        SMenuItem closeItem = new SMenuItem("Close");
        closeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                close();
            }
        });

        SMenu fileMenu = new SMenu("File");
        fileMenu.add(saveItem);
        fileMenu.add(revertItem);
        fileMenu.add(closeItem);

        SMenuItem cutItem = new SMenuItem("Cut");
        cutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cut();
            }
        });
        SMenuItem copyItem = new SMenuItem("Copy");
        copyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                copy();
            }
        });
        SMenuItem pasteItem = new SMenuItem("Paste");
        pasteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                paste();
            }
        });

        SMenu editMenu = new SMenu("Edit");
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        SMenuBar menuBar = new SMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        return menuBar;
    }

    protected SToolBar createToolBar() {
        try {
            SButton saveButton = new SButton(new SURLIcon("../icons/filesave.png"));
            saveButton.setToolTipText("save");
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    save();
                }
            });

            SButton revertButton = new SButton(new SURLIcon("../icons/filerevert.png"));
            revertButton.setToolTipText("revert");
            revertButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    revert();
                }
            });
            SButton closeButton = new SButton(new SURLIcon("../icons/fileclose.png"));
            closeButton.setToolTipText("close");
            closeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    close();
                }
            });

            SButton cutButton = new SButton(new SURLIcon("../icons/editcut.png"));
            cutButton.setToolTipText("cut");
            cutButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    cut();
                }
            });
            SButton copyButton = new SButton(new SURLIcon("../icons/editcopy.png"));
            copyButton.setToolTipText("copy");
            copyButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    copy();
                }
            });
            SButton pasteButton = new SButton(new SURLIcon("../icons/editpaste.png"));
            pasteButton.setToolTipText("paste");
            pasteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    paste();
                }
            });

            SToolBar toolBar = new SToolBar();
            toolBar.add(saveButton);
            toolBar.add(revertButton);
            toolBar.add(closeButton);
            toolBar.add(new SLabel("<html>&nbsp;"));
            toolBar.add(cutButton);
            toolBar.add(copyButton);
            toolBar.add(pasteButton);

            return toolBar;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
        return new SToolBar();
    }

    public void setText(String text) {
        textArea.setText(text);
    }
    public String getText() { return textArea.getText(); }

    public void setBackup(String backup) {
        this.backup = backup;
    }
    public String getBackup() { return backup; }

public void save() {
    	try {
			File file = File.createTempFile("wings", "txt");
			PrintWriter out = new PrintWriter(new FileOutputStream(file));
            out.print(textArea.getText());
            out.close();
			
			FileResource resource = new FileResource(file);
	    	resource.setExternalizerFlags(resource.getExternalizerFlags() | ExternalizeManager.REQUEST);
	    	 
	    	Map headers = new HashMap();
	    	headers.put("Content-Disposition", "attachment; filename=" + file.getName());
	    	resource.setHeaders(headers.entrySet());
	    	 
	    	final ScriptListener listener = new JavaScriptListener(null, null, "location.href='" + resource.getURL() + "'");SessionManager.getSession().getScriptManager().addScriptListener(listener);

		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
    }

    public void revert() {
        textArea.setText(backup);
    }

    public void close() {
        super.dispose();
    }

    public void cut() {
        clip = textArea.getText();
        textArea.setText("");
    }

    public void copy() {
        clip = textArea.getText();
    }

    public void paste() {
        if (clip != null) {
            textArea.setText(textArea.getText() + clip);
        }
    }

    public void internalFrameClosed(SInternalFrameEvent e) {
        close();
    }

    public void internalFrameOpened(SInternalFrameEvent e) {}
    public void internalFrameIconified(SInternalFrameEvent e) {}
    public void internalFrameDeiconified(SInternalFrameEvent e) {}
    public void internalFrameMaximized(SInternalFrameEvent e) {
        textArea.setRows(24);
    }
    public void internalFrameUnmaximized(SInternalFrameEvent e) {
        textArea.setRows(12);
    }

    private class EditorDynamicResource extends DynamicResource
    {
        String id;

        public EditorDynamicResource() {
            super(null, "txt", "text/unknown");
        }

        public void write(Device out)
            throws IOException
        {
            out.print(textArea.getText());
        }

        public String getId() {
            if (id == null) {
                ExternalizeManager ext = SessionManager.getSession().getExternalizeManager();
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Disposition", "attachment; filename=blub");
                id = ext.getId(ext.externalize(this, headers.entrySet(), AbstractExternalizeManager.REQUEST));
                //logger.debug("new " + this.getClass().getName() + " with id " + id);
            }
            return id;
        }
    }
}
