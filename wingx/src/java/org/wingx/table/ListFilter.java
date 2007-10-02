package org.wingx.table;

/**
 * ListFilter
 *
 * @author jde
 */
public class ListFilter extends Filter {

    private String operator;

    private boolean charField;

    private Object[] selection;

    private Object[] selectionValues;

    // set this value to "false" if you do not want the filter value applied as jdbc parameter
    private boolean applyValue = true;

    public ListFilter() {
    }

    /**
     * Creates a new ListFilter with name and value.
     *
     * @param field the field name
     */
    public ListFilter(String field, String operator) {
        setField(field);
        this.operator = operator;
    }

    public ListFilter(String field, String operator, Object[] selection) {
        this(field, operator);
        this.selection = selection;
    }

    /**
     * Creates a new ListFilter with name and value.
     *
     * @param field     the field name
     * @param charField if it is a character field
     */
    public ListFilter(String field, String operator, boolean charField) {
        this(field, operator);
        this.charField = charField;
    }

    public ListFilter(String field, String operator, boolean charField, Object[] selection) {
        this(field, operator, charField);
        this.selection = selection;
    }

    public ListFilter(String field, String operator, boolean charField, Object[] selection, Object[] selectionValues) {
        this(field, operator, charField, selection);
        this.selectionValues = selectionValues;
    }

    public Object getValue() {
        if (super.getValue() == null)
            return null;

        if (isCharField()) {
            return super.getValue().toString().length() > 0 ? "%" + super.getValue() + "%" : null;
        } else {
            return super.getValue();
        }
    }

    public String getOperator() {
        if (isCharField())
            return " like ";
        else
            return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public boolean isCharField() {
        return charField;
    }

    public void setCharField(boolean charField) {
        this.charField = charField;
    }

    public Object[] getSelection() {
        return selection;
    }

    public void setSelection(Object[] selection) {
        this.selection = selection;
    }

    public boolean isSelectionFilter() {
        return selection != null && selection.length > 0;
    }

    public Object[] getSelectionValues() {
        return selectionValues;
    }

    public void setSelectionValues(Object[] selectionValues) {
        this.selectionValues = selectionValues;
    }

    public void reset() {
        setValue(null);
    }

    public boolean isApplyValue() {
        return applyValue;
    }

    public void setApplyValue(boolean applyValue) {
        this.applyValue = applyValue;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isCharField()) {
            sb.append("UPPER(").
                    append(getField()).
                    append(")").
                    append(getOperator()).
                    append("UPPER(?)");
        } else
        {
            sb.append(getField()).
                    append(getOperator()).
                    append("?");
        }
        return sb.toString();
    }

    /**
     * Compares based on field name.
     */
    public boolean equals(Object rhs) {
        if (!(rhs instanceof ListFilter)) {
            return false;
        }
        return getField().equals(((ListFilter) rhs).getField());
    }
}
