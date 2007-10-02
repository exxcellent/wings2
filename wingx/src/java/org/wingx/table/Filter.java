package org.wingx.table;

import java.math.BigDecimal;
import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;

import org.wings.session.SessionManager;

/**
 * Filter
 * <p/>
 */
public class Filter {

    private String field;
    private Object value;
    private boolean visible = true;

    public Filter() {
    }

    public Filter(String field) {
        this.field = field;
    }

    public Filter(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Compares the filter value with the property value of the row bean.
     * Overwrite this method if you have to manually compare the two values.
     * @param filterValue
     * @param propertyValue
     * @return boolean
     */
    public boolean equals(Object filterValue, Object propertyValue) {
        if (propertyValue instanceof String) {
            String filterValueString = filterValue.toString();
            String propertyValueString = propertyValue.toString();
            return propertyValueString.toUpperCase().indexOf(filterValueString.toUpperCase()) > -1;
        } else if (propertyValue instanceof Byte) {
            try {
                return new Byte(filterValue.toString()).equals(propertyValue);
            } catch (NumberFormatException nfex) {
                // ignored -> use default behaviour
            }
        } else if (propertyValue instanceof Short) {
            try {
                return new Short(filterValue.toString()).equals(propertyValue);
            } catch (NumberFormatException nfex) {
                // ignored -> use default behaviour
            }
        } else if (propertyValue instanceof Integer) {
            try {
                return new Integer(filterValue.toString()).equals(propertyValue);
            } catch (NumberFormatException nfex) {
                // ignored -> use default behaviour
            }
        } else if (propertyValue instanceof Long) {
            try {
                return new Long(filterValue.toString()).equals(propertyValue);
            } catch (NumberFormatException nfex) {
                // ignored -> use default behaviour
            }
        } else if (propertyValue instanceof Float) {
            try {
                return new Float(filterValue.toString()).equals(propertyValue);
            } catch (NumberFormatException nfex) {
                // ignored -> use default behaviour
            }
        } else if (propertyValue instanceof Double) {
            try {
                return new Double(filterValue.toString()).equals(propertyValue);
            } catch (NumberFormatException nfex) {
                // ignored -> use default behaviour
            }
        } else if (propertyValue instanceof BigDecimal) {
            try {
                return new BigDecimal(filterValue.toString()).equals(propertyValue);
            } catch (NumberFormatException nfex) {
                // ignored -> use default behaviour
            }
        } else if (propertyValue instanceof Date) {
            if (filterValue instanceof Date) {
                Date compareDate = dropTimeFromDate((Date) propertyValue);
                filterValue = dropTimeFromDate((Date) filterValue);
                return filterValue.equals(compareDate);
            }

            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, SessionManager.getSession().getLocale());
            try {
                Date date = df.parse(filterValue.toString());
                date = dropTimeFromDate(date);
                Date compareDate = dropTimeFromDate((Date) propertyValue);
                return date.equals(compareDate);
            } catch (ParseException ex) {
                // ignored
            }
        } else if (propertyValue instanceof Collection) {
            Collection col = (Collection) propertyValue;
            return col.contains(filterValue);
        }

        // default behaviour
        return filterValue.equals(propertyValue);
    }

    private Date dropTimeFromDate(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.AM_PM, Calendar.AM);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        return cal.getTime();
    }

}
