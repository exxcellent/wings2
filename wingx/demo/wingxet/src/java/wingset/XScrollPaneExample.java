package wingset;

import org.wingx.XTable;
import org.wingx.XScrollPane;
import org.wingx.table.*;
import org.wings.*;
import org.wings.border.SEmptyBorder;
import org.wings.table.STableColumnModel;
import org.wings.style.CSSProperty;
import org.wings.style.CSSStyleSheet;
import org.wings.plaf.css.TableCG;
import org.wings.event.SMouseListener;
import org.wings.event.SMouseEvent;

import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * @author Holger Engels
 */
public class XScrollPaneExample
        extends TableExample
{
    private XTable table;
    private SLabel clicks = new SLabel();
    private XScrollPaneExample.TableControls controls;
    private boolean consume = false;

    public SComponent createExample() {
        table = new XTable(new XScrollPaneExample.MyTableModel(7, 100));
        table.setName("xScrollPaneExample");
        table.setShowGrid(true);
        table.setSelectionMode(STable.NO_SELECTION);
        table.setDefaultRenderer(cellRenderer);
        table.setShowAsFormComponent(false);
        table.setEditable(false);

        ((XTableColumn)table.getColumnModel().getColumn(0)).setSortable(true);
        ((XTableColumn)table.getColumnModel().getColumn(1)).setSortable(true);
        ((XTableColumn)table.getColumnModel().getColumn(5)).setSortable(true);
        ((XTableColumn)table.getColumnModel().getColumn(6)).setSortable(true);

        ((XTableColumn)table.getColumnModel().getColumn(0)).setFilterRenderer(new XScrollPaneExample.StringEditableCellRenderer());
        ((XTableColumn)table.getColumnModel().getColumn(0)).setFilterable(true);

        ((XTableColumn)table.getColumnModel().getColumn(4)).setFilterRenderer(new XScrollPaneExample.BooleanFilterRenderer());
        ((XTableColumn)table.getColumnModel().getColumn(4)).setCellRenderer(new XScrollPaneExample.BooleanEditableCellRenderer());
        ((XTableColumn)table.getColumnModel().getColumn(4)).setFilterable(true);

        table.getColumnModel().getColumn(0).setWidth("200px");
        table.getColumnModel().getColumn(1).setWidth("*");
        table.getColumnModel().getColumn(2).setWidth("100px");
        table.getColumnModel().getColumn(3).setWidth("50px");
        table.getColumnModel().getColumn(4).setWidth("50px");
        table.getColumnModel().getColumn(5).setWidth("50px");
        table.getColumnModel().getColumn(6).setWidth("100px");

        table.addMouseListener(new SMouseListener() {
            public void mouseClicked(SMouseEvent e) {
                if (consume && table.columnAtPoint(e.getPoint()) == 1)
                    e.consume();
                clicks.setText("clicked " + e.getPoint());
            }
        });

        XScrollPane scrollPane = new XScrollPane(table);
        scrollPane.setVerticalAlignment(SConstants.TOP_ALIGN);

        SPanel panel = new SPanel(new SBorderLayout());
        panel.add(scrollPane, SBorderLayout.CENTER);
        panel.add(clicks, SBorderLayout.SOUTH);
        panel.setVerticalAlignment(SConstants.TOP_ALIGN);
        return panel;
    }


    static class MyTableModel extends XTableModel {
        int cols, rows;

        Object[][] origData;
        Object[][] data;

        MyTableModel(int pCols, int pRows) {
            this.cols = pCols > 6 ? pCols: 6;
            this.rows = pRows;

            origData = new Object[rows][cols];

            for (int c = 0; c < cols; c++) {
                for (int r = 0; r < rows; r++)
                    origData[r][c] = (c == 1 ? "stretched cell " : "cell ") + r + ":" + c;
            }
            for (int r = 0; r < rows; r++)
                origData[r][2] = createColor(r);
            for (int r = 0; r < rows; r++)
                origData[r][3] = createImage(r);
            for (int r = 0; r < rows; r++)
                origData[r][4] = createBoolean(r);
            for (int r = 0; r < rows; r++)
                origData[r][5] = createInteger(r);

            refresh();
        }

        public int getColumnCount() {
            return cols;
        }

        public String getColumnName(int col) {
            return "col " + col;
        }

        public int getRowCount() {
            return data.length;
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

        public boolean isCellEditable(int row, int col) {
            if (getColumnClass(col).isAssignableFrom(String.class) ||
                    getColumnClass(col).isAssignableFrom(Boolean.class))
                return true;
            else
                return false;
        }

        public void refresh() {
            java.util.List list = new ArrayList(Arrays.asList((Object[][])origData.clone()));
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                Object[] r = (Object[])iterator.next();
                for (int i=0; i < getColumnCount(); i++) {
                    Object filter = getFilter(i);
                    if (filter != null) {
                        if (getColumnClass(i) == String.class) {
                            if (r[i] == null || !((String)r[i]).contains((CharSequence)filter))
                                iterator.remove();
                        }
                        else if (getColumnClass(i) == Boolean.class) {
                            if (!filter.equals(r[i]))
                                iterator.remove();
                        }
                    }
                }
            }
            data = (Object[][])list.toArray(new Object[0][0]);

            Arrays.sort(data, new Comparator() {
                public int compare(Object o1, Object o2) {
                    for (int i=0; i < getColumnCount(); i++) {
                        Object[] r1 = (Object[])o1;
                        Object[] r2 = (Object[])o2;
                        int comparision;

                        switch (getSort(i)) {
                            case SORT_ASCENDING:
                            {
                                Comparable v1 = (Comparable)r1[i];
                                Comparable v2 = (Comparable)r2[i];
                                comparision = v1.compareTo(v2);
                                if (comparision != 0)
                                    return comparision;
                            }
                            case SORT_DESCENDING:
                            {
                                Comparable v1 = (Comparable)r1[i];
                                Comparable v2 = (Comparable)r2[i];
                                comparision = v2.compareTo(v1);
                                if (comparision != 0)
                                    return comparision;
                            }
                            default:
                                comparision = 0;
                        }
                    }
                    return 0;
                }
            });

            fireTableDataChanged();
        }
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
            final SCheckBox editable = new SCheckBox("editable");
            editable.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    table.setEditable(editable.isSelected());
                }
            });

            final SCheckBox consume = new SCheckBox("Consume events on 'col 1'");
            consume.setToolTipText("<html>A SMouseListener will intercept the mouse clicks.<br>" +
                    "Consumed events will not be processed by the table anymore");
            consume.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    XScrollPaneExample.this.consume = consume.isSelected();
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

            addControl(editable);
            addControl(new SLabel(""));
            addControl(consume);
            addControl(new SLabel(" selection mode"));
            addControl(selectionMode);

            final SComboBox headerColor = new SComboBox(COLORS);
            headerColor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = (Color) ((Object[]) headerColor.getSelectedItem())[1];
                    table.setAttribute(STable.SELECTOR_HEADER, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
                }
            });
            headerColor.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel(" header"));
            addControl(headerColor);

            final SComboBox oddColor = new SComboBox(COLORS);
            oddColor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = (Color) ((Object[]) oddColor.getSelectedItem())[1];
                    table.setAttribute(STable.SELECTOR_ODD_ROWS, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
                }
            });
            oddColor.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel(" odd row"));
            addControl(oddColor);

            final SComboBox evenColor = new SComboBox(COLORS);
            evenColor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = (Color) ((Object[]) evenColor.getSelectedItem())[1];
                    table.setAttribute(STable.SELECTOR_EVEN_ROWS, CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
                }
            });
            evenColor.setRenderer(new ObjectPairCellRenderer());
            addControl(new SLabel(" even row"));
            addControl(evenColor);

            final SCheckBox reverseColumnOrder = new SCheckBox("Reverse column order");
            reverseColumnOrder.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    STableColumnModel columnModel = table.getColumnModel();
                    Collections.reverse((java.util.List)columnModel.getColumns());
                }
            });
            addControl(reverseColumnOrder);

            final SCheckBox hideSomeColumns = new SCheckBox("Hide some Columns");
            hideSomeColumns.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean hide = hideSomeColumns.isSelected();
                    STableColumnModel columnModel = table.getColumnModel();
                    for (int i=0; i < columnModel.getColumnCount(); i++) {
                        columnModel.getColumn(i).setHidden(hide && i %3 == 0);
                    }
                }
            });
            addControl(hideSomeColumns);
        }
    }

    private static class StringEditableCellRenderer
        extends STextField
        implements EditableTableCellRenderer
    {
        public Object getValue() {
            return getText();
        }

        public SComponent getTableCellRendererComponent(STable table, Object value, boolean isSelected, int row, int column) {
            setText(value != null ? value.toString() : null);
            return this;
        }

        public LowLevelEventListener getLowLevelEventListener(STable table, int row, int column) {
            return this;
        }
    }

    private static class BooleanEditableCellRenderer
        extends SCheckBox
        implements EditableTableCellRenderer
    {
        public Object getValue() {
            return isSelected() ? Boolean.TRUE : Boolean.FALSE;
        }

        public SComponent getTableCellRendererComponent(STable table, Object value, boolean isSelected, int row, int column) {
            setSelected(Boolean.TRUE.equals(value));
            return this;
        }

        public LowLevelEventListener getLowLevelEventListener(STable table, int row, int column) {
            return this;
        }
    }

    private static class BooleanFilterRenderer
        extends SComboBox
        implements EditableTableCellRenderer
    {
        public BooleanFilterRenderer() {
            super(new Boolean[] { null, Boolean.TRUE, Boolean.FALSE });
        }

        public Object getValue() {
            return getSelectedItem();
        }

        public SComponent getTableCellRendererComponent(STable table, Object value, boolean isSelected, int row, int column) {
            setSelectedItem(value);
            return this;
        }

        public LowLevelEventListener getLowLevelEventListener(STable table, int row, int column) {
            return this;
        }
    }
}
