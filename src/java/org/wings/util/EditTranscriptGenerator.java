package org.wings.util;

import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 * This class takes two Strings an generates the shortes list of necessarry
 * change operations to transform the source string into the target string.
 * <p>For more information about the used algorithm refer to:
 * <ul>
 * <li>http://www.merriampark.com/ld.htm</li>
 * <li>http://www.msci.memphis.edu/~giri/compbio/f99/ningxu/NOTE10.html</li>
 * <li>http://www.itu.dk/courses/AVA/E2005/StringEditDistance.pdf</li>
 * </ul>
 * <p/>Original source extracted from Glazed Lists (http://publicobject.com/glazedlists/)
 *
 * Implementation of Eugene W. Myer's paper, "An O(ND) Difference Algorithm and Its
 * Variations", the same algorithm found in GNU diff.
 * <p/>
 * <p>Note that this is a cleanroom implementation of this popular algorithm that is
 * particularly suited for the Java programmer. The variable names are descriptive and the
 * approach is more object-oriented than Myer's sample algorithm.
 *
 * @author <a href="mailto:jesse@odel.on.ca">Jesse Wilson</a>
 */
public final class EditTranscriptGenerator {
    /**
     * The exact calculation of document events is very memory and cpu intesive.
     * For texts beeing longer than this limit we use a dumb approximation.
     * (OL) turning this off until we actually can do something useful with it.
     */
    private static final int MAX_LENGTH_FOR_TRANSCRIPT_GENERATION = 0;


    /**
     * Generates the shorted edit transcript needed to transform the source String into the target String.
     * The needed changes are noted down as {@link DocumentEvent}s.
     *
     * @return A list of {@link DocumentEvent}s either of type {@link javax.swing.event.DocumentEvent.EventType#INSERT}
     *         or {@link javax.swing.event.DocumentEvent.EventType#REMOVE} with correct sourceIndexes and lengths.
     */
    public static List generateEvents(String source, String target) {
        /* turning off complex handling until we need it */
//        if (((source == null) || source.length() < MAX_LENGTH_FOR_TRANSCRIPT_GENERATION) &&
//                ((target == null) || target.length() < MAX_LENGTH_FOR_TRANSCRIPT_GENERATION))
//            return calculateEventsByStringDistance(source, target);
//        else
            return calculateEventsByDumbApproximation(source, target);
    }

    /**
     * Generate the document events by using the algorithm.
     * @return A list of {@link DocumentEvent}s either of type {@link javax.swing.event.DocumentEvent.EventType#INSERT}
     *         or {@link javax.swing.event.DocumentEvent.EventType#REMOVE} with correct sourceIndexes and lengths.
     */
    private static List calculateEventsByStringDistance(String source, String target) {
        final List editScript = shortestEditScript(new StringDiffMatcher(source, target));

        final Vector actions = new Vector();

        // target is x axis. Changes in X mean advance target index
        // source is y axis. Changes to y mean advance source index
        int targetIndex = 0;
        int sourceIndex = 0;

        // walk through points, applying changes as they arrive
        Point previousPoint = null;
        for (Iterator i = editScript.iterator(); i.hasNext();) {
            Point currentPoint = (Point) i.next();

            // skip the first point
            if (previousPoint == null) {
                previousPoint = currentPoint;
                continue;
            }

            // figure out what the relationship in the values is
            int deltaX = currentPoint.getX() - previousPoint.getX();
            int deltaY = currentPoint.getY() - previousPoint.getY();


            if (deltaX == deltaY) {
                // handle an update
                targetIndex += deltaX;
                sourceIndex += deltaY;
            } else if (deltaX == 1 && deltaY == 0) {
                // handle a remove
                addOrUpdateChangeEvent(sourceIndex, actions, DocumentEvent.EventType.REMOVE);
            } else if (deltaX == 0 && deltaY == 1) {
                // handle an insert
                addOrUpdateChangeEvent(sourceIndex, actions, DocumentEvent.EventType.INSERT);
                sourceIndex++;
                targetIndex++;
            } else {
                // should never be reached
                throw new IllegalStateException();
            }

            // the next previous point is this current point
            previousPoint = currentPoint;
        }
        return actions;
    }

    /**
     * Insert next atomic change (1 character) into event queue. Either consolidate as continuation with
     * an existing event in the queue or create a new one.
     *
     * @param sourceIndex character index in source string
     * @param actions Current queue of document events
     * @param eventType what happens at the source index?
     */
    private static void addOrUpdateChangeEvent(int sourceIndex, Vector actions, DocumentEvent.EventType eventType) {
        int offset = sourceIndex;
        int length = 1;
        final Document sourceDocument = null; // dummy - we do not have a reference.

        // First change is always a new event
        if (actions.size() == 0) {
            SimpleDocumentEvent newEvent = new SimpleDocumentEvent(offset, length, sourceDocument, eventType);
            actions.add(newEvent);
        } else {
            // Is this a contiunuation of the last event type?
            if (((DocumentEvent) actions.lastElement()).getType().equals(eventType)) {

                SimpleDocumentEvent docEvent = (SimpleDocumentEvent) actions.lastElement();
                offset = docEvent.getOffset();
                length = docEvent.getLength();
                // Continuation break for an insert? New event
                if ((sourceIndex != (offset + length)) && eventType.equals(DocumentEvent.EventType.INSERT)) {
                    offset = sourceIndex;
                    length = 1;
                    DocumentEvent newEvent = new SimpleDocumentEvent(offset, length, sourceDocument, eventType);
                    actions.add(newEvent);
                }
                // New remove?
                else if ((sourceIndex != offset) && eventType.equals(DocumentEvent.EventType.REMOVE)) {
                    offset = sourceIndex;
                    length = 1;
                    DocumentEvent newEvent = new SimpleDocumentEvent(offset, length, sourceDocument, eventType);
                    actions.add(newEvent);
                }
                // It contiunation? So just consolidate with existing event by increasing lenght by one
                else {
                    docEvent.increaseLength();
                }
            }
            // Anderes Event folgt
            else {
                DocumentEvent newEvent = new SimpleDocumentEvent(offset, length, sourceDocument, eventType);
                actions.add(newEvent);
            }
        }

    }

    /**
     * Our simple implementation of {@link DocumentEvent}
     */
    private static class SimpleDocumentEvent implements DocumentEvent {
        private int offset;
        private int length;
        private Document document;
        private EventType type;

        public SimpleDocumentEvent(int offset, int length, Document document, EventType type) {
            this.offset = offset;
            this.length = length;
            this.document = document;
            this.type = type;
        }

        public ElementChange getChange(Element elem) {
            return null;
        }

        public int getOffset() {
            return offset;
        }

        public int getLength() {
            return length;
        }

        public Document getDocument() {
            return document;
        }

        public EventType getType() {
            return type;
        }

        void increaseLength() {
            length += 1;
        }

        public String toString() {
            return "SimpleDocumentEvent{" +
                    "offset=" + offset +
                    ", length=" + length +
                    ", document=" + document +
                    ", type=" + type +
                    "}";
        }
    }


    /**
     * Calculate the length of the longest common subsequence for the specified input.
     */
    private final static List shortestEditScript(StringDiffMatcher input) {
        // calculate limits based on the size of the input matcher
        int N = input.getAlphaLength();
        int M = input.getBetaLength();
        Point maxPoint = new Point(N, M);
        int maxSteps = N + M;
        
        // use previous round furthest reaching D-path to determine the 
        // new furthest reaching (D+1)-path
        Map furthestReachingPoints = new HashMap();

        // walk through in stages, each stage adding one non-diagonal.
        // D == count of non-diagonals in current stage
        for (int D = 0; D <= maxSteps; D++) {

            // exploit diagonals in order to save storing both X and Y
            // diagonal k means every point on k, (k = x - y)
            for (int k = -D; k <= D; k += 2) {
                // the furthest reaching D-path on the left and right diagonals
                // either of these may be null. The terms 'below left' and 'above right'
                // refer to the diagonals that the points are on and may not be
                // representative of the point positions
                Point belowLeft = (Point) furthestReachingPoints.get(new Integer(k - 1));
                Point aboveRight = (Point) furthestReachingPoints.get(new Integer(k + 1));
                
                // the new furthest reaching point to create
                Point point;
                
                // first round: we have matched zero in word X
                if (furthestReachingPoints.isEmpty()) {
                    point = new Point(0, 0);

                    // if this is the leftmost diagonal, or the left edge is further
                    // than the right edge, our new X is that value and our y is one greater
                    // (shift verically by one)
                } else if (k == -D || (k != D && belowLeft.getX() < aboveRight.getX())) {
                    point = aboveRight.createDeltaPoint(0, 1);

                    // if the right edge is further than the left edge, use that x
                    // and keep y the same (shift horizontally by one)
                } else {
                    point = belowLeft.createDeltaPoint(1, 0);
                }
                
                // match as much diagonal as possible from the previous endpoint
                while (point.isLessThan(maxPoint) && input.matchPair(point.getX(), point.getY())) {
                    point = point.incrementDiagonally();
                }

                // save this furthest reaching path
                furthestReachingPoints.put(new Integer(k), point);
                
                // if we're past the end, we have a solution!
                if (point.isEqualToOrGreaterThan(maxPoint)) {
                    return point.trail();
                }
            }
        }
        // no solution was found
        throw new IllegalStateException();
    }

    /**
     * Generate a very simple list of document events to avoid cost intensive distance calculation:
     * Remove all existing characters, add all new characters.
     * @return A list of {@link DocumentEvent}s either of type {@link javax.swing.event.DocumentEvent.EventType#INSERT}
     *         or {@link javax.swing.event.DocumentEvent.EventType#REMOVE} with correct sourceIndexes and lengths.
     */
    private static List calculateEventsByDumbApproximation(String source, String target) {
        List events = new ArrayList(2);
        if (source != null)
            events.add(new SimpleDocumentEvent(0, source.length(), null, DocumentEvent.EventType.REMOVE));
        if (target != null)
            events.add(new SimpleDocumentEvent(0, target.length(), null, DocumentEvent.EventType.INSERT));

        return events;
    }

    /**
     * Models an X and Y point in a path. The top-left corner of the axis is the point (0,
     * 0). This is the lowest point in both the x and y dimensions. Negative points are
     * not allowed.
     */
    private final static class Point {
        private int x = 0;
        private int y = 0;
        private Point predecessor = null;

        /**
         * Create a new point with the specified coordinates and no predecessor.
         */
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Creates a new point from this point by shifting its values as specified. The
         * new point keeps a reference to its source in order to create a path later.
         */
        public Point createDeltaPoint(int deltaX, int deltaY) {
            Point result = new Point(x + deltaX, y + deltaY);
            result.predecessor = this;
            return result;
        }

        /**
         * Shifts <code>x</code> and <code>y</code> values down and to the
         * right by one.
         */
        public Point incrementDiagonally() {
            Point result = createDeltaPoint(1, 1);

            // shortcut to the predecessor (to save memory!)
            if (predecessor != null) {
                int deltaX = result.x - predecessor.x;
                int deltaY = result.y - predecessor.y;

                if (deltaX == deltaY) {
                    result.predecessor = this.predecessor;
                }
            }

            return result;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean isLessThan(Point other) {
            return x < other.x && y < other.y;
        }

        public boolean isEqualToOrGreaterThan(Point other) {
            return x >= other.x && y >= other.y;
        }

        public String toString() {
            return "(" + x + "," + y + ")";
        }

        /**
         * Get a trail from the original point to this point. This is a list of all points
         * created via a series of {@link #createDeltaPoint(int,int)} calls.
         */
        public List trail() {
            List reverse = new ArrayList();
            Point current = this;
            while (current != null) {
                reverse.add(current);
                current = current.predecessor;
            }
            Collections.reverse(reverse);
            return reverse;
        }
    }

    /**
     * Determines if the values at the specified points match or not.
     *
     * <p>This class specifies that each element should specify a character value.
     * This is for testing and debugging only and it is safe for implementing
     * classes to throw {@link UnsupportedOperationException} for both the
     * {@link #alphaAt(int)} and {@link #betaAt(int)} methods.
     */
    private final static class StringDiffMatcher {
        private String alpha;
        private String beta;

        public StringDiffMatcher(String alpha, String beta) {
            this.alpha = alpha;
            this.beta = beta;
        }

        public int getAlphaLength() {
            return alpha.length();
        }

        public char alphaAt(int index) {
            return alpha.charAt(index);
        }

        public char betaAt(int index) {
            return beta.charAt(index);
        }

        public int getBetaLength() {
            return beta.length();
        }

        public boolean matchPair(int alphaIndex, int betaIndex) {
            return alpha.charAt(alphaIndex) == beta.charAt(betaIndex);
        }
    }
}