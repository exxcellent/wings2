/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package wingset;

import org.wings.SBorderLayout;
import org.wings.SButton;
import org.wings.SCardLayout;
import org.wings.SComboBox;
import org.wings.SComponent;
import org.wings.SFileChooser;
import org.wings.SFileIcon;
import org.wings.SFlowDownLayout;
import org.wings.SForm;
import org.wings.SGridLayout;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.STextArea;
import org.wings.border.SEmptyBorder;
import org.wings.util.SStringBuilder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class FileChooserExample
        extends WingSetPane
{
    final static Color WARN_COLOR = new Color(255, 255, 127);

    /**
     * the file chooser that gets the files.
     */
    SFileChooser chooser;

    /**
     * three cards for different content to be previewd: images, text and
     * unknown.
     */
    SCardLayout contentSwitcher;

    /**
     * label that shows image content.
     */
    SLabel iconLabel;

    /**
     * text area to show text content.
     */
    STextArea textArea;

    /**
     * form, that contains the text-area
     */
    SPanel textForm;

    /**
     * label for unknown content.
     */
    SLabel unknownLabel;

    /**
     * remember the previous file to remove it.
     */
    File previousFile;
    private FileChooserControls controls;

    public SComponent createExample() {
        controls = new FileChooserControls();

        SForm p = new SForm(new SBorderLayout());

        p.add(controls, SBorderLayout.NORTH);
        p.add(createUpload(), SBorderLayout.WEST);
        p.add(createPreview(), SBorderLayout.CENTER);

        controls.addControllable(chooser);
        return p;
    }

    protected String getText(File f) {
        try {
            SStringBuilder buffer = new SStringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line = reader.readLine();
            while (line != null) {
                buffer.append(line).append("\n");
                line = reader.readLine();
            }

            return buffer.toString();
        } catch (Exception ex) {
            return "got exception " + ex.getMessage();
        }
    }

    protected void adaptPreview() {
        if (previousFile != null) {
            previousFile.delete();
            previousFile = null;
        }
        try {
            if (chooser.getFileType().startsWith("text/")) {
                textArea.setText(getText(chooser.getFile()));
                contentSwitcher.show(textForm);
            } else if (chooser.getFileType().startsWith("image/")) {
                iconLabel.setIcon(new SFileIcon(chooser.getFile(), null,
                        chooser.getFileType()));
                contentSwitcher.show(iconLabel);
            } else {
                contentSwitcher.show(unknownLabel);
            }
            previousFile = chooser.getFile();
        } catch (Exception ex) {
            contentSwitcher.show(unknownLabel);
        }
    }


    protected SComponent createPreview() {
        SPanel p = new SPanel(new SFlowDownLayout());
        p.setVerticalAlignment(TOP);

        SLabel previewLabel = new SLabel("Preview");
        previewLabel.setBorder(new SEmptyBorder(0, 20, 0, 0));
        p.add(previewLabel);

        contentSwitcher = new SCardLayout();

        SPanel contentPane = new SPanel(contentSwitcher);

        iconLabel = new SLabel();

        textForm = new SPanel();

        textArea = new STextArea();
        textArea.setColumns(50);
        textArea.setRows(20);
        textArea.setEditable(false);

        unknownLabel = new SLabel("Unknown Content");
        unknownLabel.setBackground(WARN_COLOR);

        contentPane.add(iconLabel, "ICON");

        textForm.add(textArea);
        contentPane.add(textForm, "TEXT");

        contentPane.add(unknownLabel, "UNKNOWN");

        contentSwitcher.show(unknownLabel);

        contentPane.setBorder(new SEmptyBorder(10, 20, 0, 0));
        p.add(contentPane);
        return p;
    }

    protected SComponent createUpload() {
        SPanel form = new SPanel(new SFlowDownLayout());
        form.setBorder(new SEmptyBorder(10, 5, 0, 0));

        chooser = new SFileChooser();
        form.add(chooser);

        SButton submit = new SButton("upload");
        submit.setVerticalAlignment(RIGHT);
        form.add(submit);

        SPanel p = new SPanel(new SGridLayout(2));
        p.setBorder(new SEmptyBorder(10, 5, 0, 0));
        p.add(new SLabel("message:"));
        final SLabel message = new SLabel("");
        p.add(message);

        p.add(new SLabel("filename:"));
        final SLabel filename = new SLabel("");
        p.add(filename);

        p.add(new SLabel("fileid:"));
        final SLabel fileid = new SLabel("");
        p.add(fileid);

        p.add(new SLabel("filetype:"));
        final SLabel filetype = new SLabel("");
        p.add(filetype);

        p.add(new SLabel("size:"));
        final SLabel size = new SLabel("");
        p.add(size);

        form.add(p);


        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (chooser.getFile() != null) {
                        message.setText("OK");
                        message.setBackground(null);
                        filename.setText(chooser.getFileName());
                        fileid.setText(chooser.getFileId());
                        filetype.setText(chooser.getFileType());
                        size.setText("" + chooser.getFile().length());
                        adaptPreview();
                    } else {
                        message.setText("No file chosen");
                        message.setBackground(WARN_COLOR);
                    }
                } catch (IOException ex) {
                    message.setText(ex.getMessage());
                    message.setBackground(WARN_COLOR);
                    filename.setText("");
                    fileid.setText("");
                    filetype.setText("");
                    size.setText("");
                    contentSwitcher.show(unknownLabel);
                }
                chooser.reset();
            }
        });

        form.setVerticalAlignment(TOP);

        return form;
    }

    class FileChooserControls extends ComponentControls {
        public FileChooserControls() {
            Object[] values = {new Integer(1), new Integer(2), new Integer(4),
                               new Integer(8), new Integer(16), new Integer(32),
                               new Integer(64)};

            final SComboBox comboBox = new SComboBox(values);
            comboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    getSession().setMaxContentLength(((Integer) comboBox.getSelectedItem()).intValue());
                }
            });
            comboBox.setSelectedItem(new Integer(getSession().getMaxContentLength()));

            addControl(new SLabel("Maximum Content Length [kB]: "));
            addControl(comboBox);
        }
    }
}
