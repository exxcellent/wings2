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
package org.wings.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * Ein Klasse, die Zeitmessungen aufnimmt und diese in Relation zueineander
 * setzt. Zum Start der Zeitmessung Methode start kurzer Beschreibung was
 * gemessen wird als Parameter aufrufen. Das Ende der Zeitmessung wird durch
 * stop angezeigt. Es kann nur eine Zeitmessung gleichzeitig stattfinden. Die
 * Ausgabe ist nicht sortiert, gibt aber die relativen Unterschiede in der
 * Dauer der einzelnen Messungen an. Die Messung mit der laengsten Dauer ist
 * der Referenzwert (1.0).
 */

/**
 * Some simple stop watch. It allows to measure multiple time periods
 * and prints them. Usage: call start(comment) and stop() for
 * each period of time.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class TimeMeasure {
    protected final static double RESOLUTION = 100.0;

    /**
     * List of measurements.
     */
    protected final ArrayList measures;

    /**
     * Message formatter
     */
    protected final MessageFormat formatter;

    /**
     * the current time measurement.
     */
    protected Measure current;

    /**
     * Simple TimeMesaure with default format.
     */
    public TimeMeasure() {
        this(new MessageFormat("{0}\t: {1}\t {2}x\n"));
    }

    /**
     * A new TimeMeasure which reports in a specific format. The
     * format is a standard MessageFormat with the following variables:
     * <ul>
     * <li><code>{0}</code> the measurement comment</li>
     * <li><code>{1}</code> the time it took</li>
     * <li><code>{2}</code> how many times this is faster than the
     * slowest measurement</li>
     * </ul>
     */
    public TimeMeasure(MessageFormat formatter) {
        this.measures = new ArrayList();
        this.formatter = formatter;
    }

    /**
     * Reset of all Measurements.
     */
    public void reset() {
        measures.clear();
    }

    /*
     * Startet eine neue Messsung.
     * @param comment  Die Beschreibung der Messung.
     */
    public void start(String comment) {
        current = new Measure(comment);
    }

    /*
     * Startet eine neue Messsung.
     * @param comment  Die Beschreibung der Messung.
    public Object generate(String comment) {
        current = new Measure();
        actual.comment = comment;
        measures.add(actual);
        return actual;
    }
     */

    /*
     * Addiert eine Messsung zu einer bestehenden.
     * @param comment  Die Beschreibung der Messung.
    public void addToMeasure(Object measure) {
        int index = measures.indexOf(measure);
        if ( index<0 ) {
            System.err.println("Measure does not exists " + measure);
            actual = null;
            return;
        }

        actual = (Measure)measures.get(index);
        measures.remove(index);

        actual.start = System.currentTimeMillis();
    }
     */

    /**
     * stop current time measurement and store it.
     */
    public void stop() {
        if (current != null) {
            current.stop();
            measures.add(current);
            current = null;
        }
    }

    /**
     * determines the time duration of the longest or shortest time interval.
     *
     * @param findShortest boolean 'true', if we are looking for the shortest
     *                     time interval; 'false' if we are looking for the
     *                     longest.
     */
    private long findReferenceValue(boolean findShortest) {
        long result = findShortest ? Long.MAX_VALUE : -1;

        Iterator it = measures.iterator();
        while (it.hasNext()) {
            Measure m = (Measure) it.next();
            result = (findShortest
                    ? Math.min(result, m.getDuration())
                    : Math.max(result, m.getDuration()));
        }
        return result;
    }

    public String print() {
        return print(false);
    }

    /**
     * creates a formatted output (using the MessageFormat) of all
     * results. The output is sorted in the in the sequence the time
     * measurements took place.
     * Writes the relative time to either the shortest or the longest
     * time interval.
     *
     * @param shortestIsReference boolean true, if the shortest time interval
     *                            is the reference value (1.0). False, if the
     *                            longest is the reference.
     */
    public String print(boolean shortestIsReference) {
        SStringBuilder result = new SStringBuilder();
        long reference = findReferenceValue(shortestIsReference);
        Iterator it = measures.iterator();
        while (it.hasNext()) {
            Measure m = (Measure) it.next();
            String factor = " -- ";
            long duration = m.getDuration();
            if (reference > 0) {
                long tmp = (long) ((duration * RESOLUTION) / reference);
                factor = String.valueOf(tmp / RESOLUTION);
            }
            Object[] args = {m.getComment(), (duration + "ms"), factor};
            result.append(formatter.format(args));
        }
        return result.toString();
    }

    public String toString() {
        return print();
    }

    /**
     * A class to store one period of time.
     */
    private final static class Measure {
        /**
         * start time.
         */
        private final long start;

        /**
         * stop time.
         */
        private long stop;

        /**
         * Die Gesamtdauer der Messung
         */
        private long duration;

        /**
         * Description.
         */
        private String comment;

        public Measure(String comment) {
            start = System.currentTimeMillis();
            this.comment = comment;
        }

        public void stop() {
            stop = System.currentTimeMillis();
            duration = stop - start;
        }

        public long getDuration() { return duration; }

        public String getComment() { return comment; }
    }
}


