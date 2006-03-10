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

import org.wings.*;
import org.wings.event.SMouseListener;
import org.wings.event.SMouseEvent;
import org.wings.plaf.css.TableCG;
import org.wings.table.SDefaultTableCellRenderer;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class TableExample
        extends WingSetPane {
    static final SIcon image = new SResourceIcon("org/wings/icons/JavaCup.gif");

    public final MyCellRenderer cellRenderer = new MyCellRenderer();

    final static Color[] colors = {
            Color.black,
            Color.cyan,
            Color.yellow,
            Color.magenta,
            Color.orange,
            Color.pink,
            Color.red,
            Color.darkGray,
            Color.gray,
            Color.green,
            Color.lightGray,
            Color.white,
            Color.blue
    };

    private STable table;
    private SLabel clicks = new SLabel();
    private TableControls controls;
    private boolean consume = false;

    public SComponent createExample() {
        controls = new TableControls();

        table = new STable(new MyTableModel(7, 5));
        table.setName("table");
        table.setShowGrid(true);
        table.setSelectionMode(STable.NO_SELECTION);
        table.setDefaultRenderer(cellRenderer);
        table.setShowAsFormComponent(false);
        table.setEditable(false);
        controls.addSizable(table);

        table.getColumnModel().getColumn(0).setWidth("200px");
        table.getColumnModel().getColumn(1).setWidth("*");
        table.getColumnModel().getColumn(2).setWidth("100px");
        table.getColumnModel().getColumn(3).setWidth("50px");
        table.getColumnModel().getColumn(4).setWidth("50px");
        table.getColumnModel().getColumn(5).setWidth("50px");
        table.getColumnModel().getColumn(6).setWidth("100px");

        table.addMouseListener(new SMouseListener() {
            public void mouseClicked(SMouseEvent e) {
                if (consume && table.getColumnForLocation(e.getPoint()) == 1)
                    e.consume();
                clicks.setText("clicked " + e.getPoint().getCoordinates());
            }
        });

        SForm panel = new SForm(new SBorderLayout());
        panel.add(controls, SBorderLayout.NORTH);
        panel.add(table, SBorderLayout.CENTER);
        panel.add(clicks, SBorderLayout.SOUTH);
        return panel;
    }


    static class MyCellRenderer extends SDefaultTableCellRenderer {
        SCheckBox checkBox = new SCheckBox();

        public MyCellRenderer() {
            setEditIcon(getSession().getCGManager().getIcon("TableCG.editIcon"));
        }

        public SComponent getTableCellRendererComponent(STable table,
                                                        Object value,
                                                        boolean selected,
                                                        int row,
                                                        int col) {
            setHorizontalAlignment(SConstants.LEFT);
            name(table, row, col);
            if (value instanceof Color) {
                Color c = (Color) value;
                setText("<html><span style=\"color:" + colorToHex(c) + "\">" + colorToHex(c) + "</span>");
                setIcon(null);
                return this;
            }
            else if (value instanceof Boolean && row != -1) {
                setHorizontalAlignment(SConstants.CENTER);
                Boolean b = (Boolean) value;
                checkBox.setNameRaw(name(table, row, col));
                checkBox.setSelected(b.booleanValue());
                return checkBox;
            }
            else if (value instanceof Integer && row != -1) {
                setHorizontalAlignment(SConstants.RIGHT);
                return super.getTableCellRendererComponent(table, value, selected, row, col);
            }
            else
                return super.getTableCellRendererComponent(table, value, selected, row, col);
        }
    }

    static class MyTableModel extends AbstractTableModel {
        int cols, rows;

        Object[][] data;
        boolean asc[];

        MyTableModel(int cols, int rows) {
            this.cols = cols;
            this.rows = rows;

            data = new Object[rows][cols];
            asc = new boolean[cols];

            for (int c = 0; c < cols; c++) {
                for (int r = 0; r < rows; r++)
                    data[r][c] = (c == 1 ? "stretched cell " : "cell ") + r + ":" + c;
            }
            for (int r = 0; r < rows; r++)
                data[r][2] = createColor(r);
            for (int r = 0; r < rows; r++)
                data[r][3] = createImage(r);
            for (int r = 0; r < rows; r++)
                data[r][4] = createBoolean(r);
            for (int r = 0; r < rows; r++)
                data[r][5] = createInteger(r);
        }

        public int getColumnCount() {
            return cols;
        }

        public String getColumnName(int col) {
            return "col " + col;
        }

        public int getRowCount() {
            return rows;
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public void setValueAt(Object value, int row, int col) {
            if (value == null)
                data[row][col] = null;
            else if (getColumnClass(col).isAssignableFrom(String.class))
                data[row][col] = value.toString();
            else if (getColumnClass(col).isAssignableFrom(Boolean.class))
                data[row][col] = new Boolean(((Boolean) value).booleanValue());
        }

        public Class getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 2:
                    return Color.class;
                case 3:
                    return SIcon.class;
                case 4:
                    return Boolean.class;
                case 5:
                    return Integer.class;
            }
            return String.class;
        }

        public Color createColor(int row) {
            return colors[row % colors.length];
        }

        public SIcon createImage(int row) {
            return image;
        }

        public Boolean createBoolean(int row) {
            if (row % 2 == 1)
                return new Boolean(false);
            else
                return new Boolean(true);
        }

        public Integer createInteger(int row) {
            return new Integer(row);
        }

        public void sort(int col, boolean ascending) {
            log.debug("sort");
            if (col < asc.length)
                asc[col] = !ascending;
        }

        public boolean isCellEditable(int row, int col) {
            if (getColumnClass(col).isAssignableFrom(String.class) ||
                    getColumnClass(col).isAssignableFrom(Boolean.class))
                return true;
            else
                return false;
        }
    }

    static class ROTableModel extends MyTableModel {
        public ROTableModel(int cols, int rows) {
            super(cols, rows);
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

    static String colorToHex(Color color) {
        String colorstr = new String("#");

        // Red
        String str = Integer.toHexString(color.getRed());
        if (str.length() > 2)
            str = str.substring(0, 2);
        else if (str.length() < 2)
            colorstr += "0" + str;
        else
            colorstr += str;

        // Green
        str = Integer.toHexString(color.getGreen());
        if (str.length() > 2)
            str = str.substring(0, 2);
        else if (str.length() < 2)
            colorstr += "0" + str;
        else
            colorstr += str;

        // Blue
        str = Integer.toHexString(color.getBlue());
        if (str.length() > 2)
            str = str.substring(0, 2);
        else if (str.length() < 2)
            colorstr += "0" + str;
        else
            colorstr += str;

        return colorstr;
    }


    /**
     * Proof that we can do some really nice tables with j-wings.
     */
    public class MyTable extends STable {
        private final /*static*/ TableCG myTableCG = new TableCG();

        public MyTable(TableModel tm) {
            super(tm);
            myTableCG.setFixedTableBorderWidth("0");
            setCG(myTableCG);
        }

        /**
         * Returns the CSS style for a row (<td style="xxx")
         */
        public String getRowStyle(int row) {
            return isRowSelected(row) ? "table_selected_row" : (row % 2 == 0 ? "table_row1" : "table_row2");
        }
    }

    class TableControls extends ComponentControls {
        private final String[] SELECTION_MODES = new String[]{"no", "single", "multiple"};

        public TableControls() {
            final SCheckBox showAsFormComponent = new SCheckBox("<html>Show as Form Component&nbsp;&nbsp;&nbsp;");
            showAsFormComponent.addActionListener(new wingset.SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    table.setShowAsFormComponent(showAsFormComponent.isSelected());
                }
            });

            final SCheckBox editable = new SCheckBox("<html>Editable&nbsp;&nbsp;&nbsp;");
            editable.addActionListener(new wingset.SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    table.setEditable(editable.isSelected());
                }
            });

            final SCheckBox consume = new SCheckBox("<html>Consume events on 2nd col&nbsp;&nbsp;&nbsp;");
            consume.setToolTipText("<html>A SMouseListener will intercept the mouse clicks.<br>" +
                    "Consumed events will not be processed by the table anymore");
            consume.addActionListener(new wingset.SerializableActionListener() {
                public void actionPerformed(ActionEvent e) {
                    TableExample.this.consume = consume.isSelected();
                }
            });

            final SComboBox selectionMode = new SComboBox(SELECTION_MODES);
            selectionMode.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if ("no".equals(selectionMode.getSelectedItem()))
                        table.setSelectionMode(STable.NO_SELECTION);
                    else if ("single".equals(selectionMode.getSelectedItem()))
                        table.setSelectionMode(STable.SINGLE_SELECTION);
                    else if ("multiple".equals(selectionMode.getSelectedItem()))
                        table.setSelectionMode(STable.MULTIPLE_SELECTION);
                }
            });

            add(showAsFormComponent);
            add(editable);
            add(consume);
            add(new SLabel(" selection mode "));
            add(selectionMode);
        }
    }
}
