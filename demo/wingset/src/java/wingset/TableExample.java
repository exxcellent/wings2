/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */

package wingset;

import org.wings.*;
import org.wings.plaf.css.TableCG;
import org.wings.table.SDefaultTableCellRenderer;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class TableExample
    extends WingSetPane
{
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
    private static final String[] SELECTION_MODES = new String[] { "no", "single", "multiple" };

    public SComponent createExample() {
        SForm panel = new SForm(new SBorderLayout());

        final STable table = new STable(new MyTableModel(7, 5));
        table.setName("table");
        table.setShowGrid(true);
        table.setSelectionMode(NO_SELECTION);
        table.setDefaultRenderer(cellRenderer);
        table.setShowAsFormComponent(false);
        table.setEditable(false);
        panel.add(table, SBorderLayout.CENTER);

        final SCheckBox showAsFormComponent = new SCheckBox("<html>Show as Form Component&nbsp;&nbsp;&nbsp;");
        showAsFormComponent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                table.setShowAsFormComponent(showAsFormComponent.isSelected());
            }
        });
        showAsFormComponent.setShowAsFormComponent(false);

        final SCheckBox editable = new SCheckBox("<html>Editable&nbsp;&nbsp;&nbsp;");
        editable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                table.setEditable(editable.isSelected());
            }
        });
        editable.setShowAsFormComponent(false);

        final SComboBox selectionMode = new SComboBox(SELECTION_MODES);
        selectionMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("no".equals(selectionMode.getSelectedItem()))
                    table.setSelectionMode(NO_SELECTION);
                else if ("single".equals(selectionMode.getSelectedItem()))
                    table.setSelectionMode(SINGLE_SELECTION);
                else if ("multiple".equals(selectionMode.getSelectedItem()))
                    table.setSelectionMode(MULTIPLE_SELECTION);
            }
        });

        SPanel north = new SPanel(new SFlowLayout());
        north.add(showAsFormComponent);
        north.add(editable);
        north.add(selectionMode);

        panel.add(north, SBorderLayout.NORTH);
        return panel;
    }


    static class MyCellRenderer extends SDefaultTableCellRenderer {
        SLabel colorOut = new SLabel();

        public SComponent getTableCellRendererComponent(STable table,
                                                        Object value,
                                                        boolean selected,
                                                        int row,
                                                        int col) {
            if (value instanceof Color) {
                Color c = (Color) value;

                // unfortunately this does not work, because the style sheet visitor does
                // not visit cellrenderers
                // colorOut.setText("[" + c.getRed() + "," 
                //                  + c.getGreen() + "," + c.getBlue() + "]");
                // colorOut.setForeground(c);
                colorOut.setText("<html><span style=\"color:" + colorToHex(c) +
                                 "\">" +
                                 colorToHex(c) +
                                 "</span>");
                return colorOut;
            }
            else
                return super.getTableCellRendererComponent(table,value,
                                                           selected,row,col);
        }
    }

    static class MyTableModel extends AbstractTableModel
    {
        int cols, rows;

        Object[][] data;
        boolean asc[];

        MyTableModel(int cols, int rows) {
            this.cols = cols;
            this.rows = rows;

            data = new Object[rows][cols];
            asc = new boolean[cols];

            for (int c=0; c < cols; c++) {
                for (int r=0; r < rows; r++)
                    data[r][c] = "cell " + r + ":" + c;
            }
            for (int r=0; r < rows; r++)
                data[r][2] = createColor(r);
            for (int r=0; r < rows; r++)
                data[r][3] = createImage(r);
            for (int r=0; r < rows; r++)
                data[r][4] = createBoolean(r);
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
            if (getColumnClass(col).isAssignableFrom(String.class))
                data[row][col] = value.toString();
            else if (getColumnClass(col).isAssignableFrom(Boolean.class))
                data[row][col] = new Boolean(((Boolean)value).booleanValue());
        }

        public Class getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }

        public Color createColor(int row) {
            return colors[row % colors.length];
        }

        public Object createImage(int row) {
            return image;
        }

        public Boolean createBoolean(int row) {
            if (row % 2 == 1)
                return new Boolean(false);
            else
                return new Boolean(true);
        }

        public void sort(int col, boolean ascending) {
            System.out.println("sort");
            if ( col<asc.length )
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

    static class ROTableModel extends MyTableModel
    {
        public ROTableModel(int cols, int rows) {
            super(cols, rows);
        }

        public boolean isCellEditable(int row, int col) { return false; }
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
    
    
    /** Proof that we can do some really nice tables with j-wings.  */
    public class MyTable extends STable {
        private final /*static*/ TableCG myTableCG = new TableCG();

        public MyTable(TableModel tm) {
            super(tm);
            myTableCG.setFixedTableBorderWidth("0"); 
            setCG(myTableCG);
        }
    
        /** Returns the CSS style for a row (<td style="xxx") */
        public String getRowStyle(int row) {
            return isRowSelected(row) ? "table_selected_row":(row % 2 == 0 ? "table_row1" : "table_row2");
        }    
    }
    
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
