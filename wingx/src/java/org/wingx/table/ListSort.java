package org.wingx.table;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Arrays;

/**
 * ListSort
 * A simple class to hold the ordering information of one field in a list.
 *
 * @author jde
 */
public class ListSort implements Serializable {
    private boolean ascending = true;

    private String field;
    private String fields[];

    private Comparator comparator;

    public ListSort() {
    }

    /**
     * Creates a new ListSort on the given field.
     */
    public ListSort(String field) {
        this.field = field;
    }

    public ListSort(String[] fields) {
        this.fields = fields;
    }

    /**
     * Creates a new ListSort on the given field.
     */
    public ListSort(String field, boolean ascending) {
        this(field);
        this.ascending = ascending;
    }

    public ListSort(String field, boolean ascending, Comparator comparator) {
        this(field, ascending);
        this.comparator = comparator;
    }

    /**
     * Reverses the order.
     */
    public void switchSort() {
        ascending = !ascending;
    }

    /**
     * Returns true if the current field is marked "ascending".
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * Returns the string representation of the field.
     */
    public String getField() {
        return field;
    }

    public String[] getFields() {
        return fields;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public Comparator getComparator() {
        return comparator;
    }

    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ListSort listSort = (ListSort) o;

        if (field != null ? !field.equals(listSort.field) : listSort.field != null) return false;
        if (!Arrays.equals(fields, listSort.fields)) return false;

        return true;
    }

    public int hashCode() {
        return (field != null ? field.hashCode() : 0);
    }
}
