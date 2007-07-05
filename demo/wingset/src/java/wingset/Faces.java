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
package wingset;

import org.wings.SButton;
import org.wings.SButtonGroup;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SForm;
import org.wings.SGridLayout;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SRadioButton;
import org.wings.SURLIcon;
import org.wings.border.SEmptyBorder;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class Faces extends WingSetPane {
    static final SIcon sel = new SURLIcon("../icons/RadioButtonSelectedIcon.gif");
    static final SIcon nsel = new SURLIcon("../icons/RadioButtonIcon.gif");
    static final SIcon pressed = new SURLIcon("../icons/RadioButtonPressedIcon.gif");
    static final SIcon rollsel = new SURLIcon("../icons/RadioButtonRolloverSelectedIcon.gif");
    static final SIcon rollnsel = new SURLIcon("../icons/RadioButtonRolloverIcon.gif");

    static final Face henner = new Face("Henner");
    static final Face armin = new Face("Armin");
    static final Face holger = new Face("Holger");
    static final Face ole = new Face("Ole");

    static final Random random = new Random();

    static final int maxFaces = 10;

    ArrayList faces;

    SEmptyBorder nameBorder;

    SGridLayout layout;

    SPanel facePanel;

    SButtonGroup hairGroup;
    SButtonGroup eyeGroup;

    SButtonGroup mouthGroup;


    protected SComponent createControls() {
        return null;
    }

    public SComponent createExample() {
        SPanel panel = new SPanel();
        panel.add(createSwitcher());
        return panel;
    }

    public SComponent createSwitcher() {
        nameBorder = new SEmptyBorder(10, 10, 10, 10);
        faces = new ArrayList();
        layout = new SGridLayout(4, faces.size() + 1);
        facePanel = new SPanel(layout);

        final SLabel hair = new SLabel();
        final SLabel eye = new SLabel();
        final SLabel mouth = new SLabel();

        SPanel shuffleForm = new SPanel();
        SButton shuffleButton = new SButton("Shuffle");
        shuffleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shuffle();
            }
        });
        shuffleForm.add(shuffleButton);

        facePanel.add(shuffleForm);
        facePanel.add(hair);
        facePanel.add(eye);
        facePanel.add(mouth);

        hairGroup = new SButtonGroup();
        hairGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = Integer.parseInt(e.getActionCommand());
                hair.setIcon(getFace(index).hair);
            }
        });

        eyeGroup = new SButtonGroup();
        eyeGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = Integer.parseInt(e.getActionCommand());
                eye.setIcon(getFace(index).eyes);
            }
        });

        mouthGroup = new SButtonGroup();
        mouthGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = Integer.parseInt(e.getActionCommand());
                mouth.setIcon(getFace(index).mouth);
            }
        });

        addFace(henner);
        addFace(armin);
        addFace(holger);
        addFace(ole);

        shuffle();

        return facePanel;
    }

    protected void shuffle() {
        shuffle(hairGroup);
        shuffle(eyeGroup);
        shuffle(mouthGroup);
    }

    protected void shuffle(SButtonGroup g) {
        int selIndex = getRandomFaceIndex();
        for (Iterator iter = g.iterator(); iter.hasNext();) {
            if (selIndex == 0) {
                g.setSelected((SRadioButton) iter.next(), true);
                return;
            }
            iter.next();
            selIndex--;
        }
    }

    protected Face getFace(int index) {
        return (Face) faces.get(index);
    }

    public void addFace(Face f) {
        if (faces.size() > maxFaces)
            return;

        layout.setColumns(faces.size() + 2);

        SButton name = new SButton(f.name);
        name.setBorder(nameBorder);
        facePanel.add(name, faces.size() + 0 * (faces.size() + 2));

        final int faceNumber = faces.size();
        // hair
        final SRadioButton hair = new SRadioButton();
        decorateButton(hair);
        hair.setActionCommand("" + faceNumber);
        hairGroup.add(hair);
        facePanel.add(hair, faces.size() + 1 * (faces.size() + 2));

        // eye
        final SRadioButton eye = new SRadioButton();
        decorateButton(eye);
        eye.setActionCommand("" + faceNumber);
        eyeGroup.add(eye);
        facePanel.add(eye, faces.size() + 2 * (faces.size() + 2));
        
        // mouth
        final SRadioButton mouth = new SRadioButton();
        decorateButton(mouth);
        mouth.setActionCommand("" + faceNumber);
        mouthGroup.add(mouth);
        facePanel.add(mouth, faces.size() + 3 * (faces.size() + 2));

        faces.add(f);
        
        /*
         * click on the name selects all buttons belonging to
         * that Face.
         */
        name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hairGroup.setSelected(hair, true);
                eyeGroup.setSelected(eye, true);
                mouthGroup.setSelected(mouth, true);
            }
        });

    }

    void decorateButton(SRadioButton b) {
        b.setIcon(nsel);
        b.setSelectedIcon(sel);
        b.setRolloverIcon(rollnsel);
        b.setRolloverSelectedIcon(rollsel);
        b.setPressedIcon(pressed);
        b.setHorizontalAlignment(SConstants.CENTER);
        b.setVerticalAlignment(SConstants.CENTER);
    }

    int getRandomFaceIndex() {
        synchronized (random) {
            return random.nextInt(faces.size());
        }
    }


    static class Face implements Serializable {
        SIcon hair;
        SIcon eyes;
        SIcon mouth;
        String name;

        Face() {
        }

        Face(String name) {
            hair = new SURLIcon("../icons/" + name + "_hair.jpeg");
            eyes = new SURLIcon("../icons/" + name + "_eyes.jpeg");
            mouth = new SURLIcon("../icons/" + name + "_mouth.jpeg");

            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
}


