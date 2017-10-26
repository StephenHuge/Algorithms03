import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
    private final Point[] points;

    private final LineSegment[] lineSegments;

    private final int length;

    public FastCollinearPoints(Point[] ps)     // finds all line segments containing 4 or more points
    {
        validate(ps);
        this.points = new Point[ps.length];
        for (int i = 0; i < ps.length; i++) {
            points[i] = ps[i];
        }
        Arrays.sort(points);
        lineSegments = getSegments();
        length = lineSegments.length;
    }
    public int numberOfSegments()                  // the number of line segments
    {
        return length;
    }
    public LineSegment[] segments()                // the line segments
    {
        LineSegment[] newSegments = new LineSegment[lineSegments.length];
        for (int i = 0; i < lineSegments.length; i++) {
            newSegments[i] = lineSegments[i];
        }
        return newSegments;
    }
    /**
     * validate method, check whether array points is null, members of points are null
     * or contain repeated points. If so, throw a java.lang.IllegalArgumentException
     */
    private void validate(Point[] ps) {
        if (ps == null || (ps.length == 1 && ps[0] == null))     
            throw new java.lang.IllegalArgumentException();
        for (int i = 0; i < ps.length; i++) {   // check whether null entry or repeated ones exist
            for (int j = i + 1; j < ps.length; j++) {
                if (ps[i] == null || ps[j] == null || 
                        ps[i].compareTo(ps[j]) == 0) {
                    throw new java.lang.IllegalArgumentException();
                }  
            }
        }
    }

    private LineSegment[] getSegments() {
        Line[] lines = getLines(points);
        LineSegment[] lineSegments = getSegmentsByLines(lines);
        return lineSegments;
    }

    private Line[] getLines(Point[] ps) {
        Line[] lines = new Line[ps.length * (ps.length + 1) / 2];
        int length = generateLines(ps, lines);
        lines = trim(lines, length);      // there may be some repeated lines in array lines, clear the repeated
        return lines;
    }
    /**
     * generate all the lines suitable, return lines's length
     */
    private int generateLines(Point[] ps, Line[] lines) {
        int index = 0;
        for (int i = 0; i < ps.length - 3; i++) {   // no need to iterate last 3 points, at least 3 points in one line 
            Point origin = ps[i];
            Comparator<Point> sortOrder = origin.slopeOrder();

            Point[] mPoints = new Point[ps.length - i - 1]; // copy to sort and store operation
            for (int j = i + 1; j < ps.length; j++) {
                mPoints[j - i - 1] = ps[j];
            }
            // sort with order origin.slopeOrder(), this sort is stable, 
            // so don't worry about whether points with same slope is ordered 
            Arrays.sort(mPoints, sortOrder);  

            int left = 0;       // pivots for getting maximum lines 
            int right = 0;
            while (left < mPoints.length && right < mPoints.length) {
                while (right < mPoints.length && sortOrder.compare(mPoints[left], mPoints[right]) == 0) { // same slope
                    right++;
                } 
                if (right - left >= 3) {   // if there are any 3 (or more) adjacent points, add a new line
                    lines[index++] = new Line(origin, mPoints[right - 1]);
                    //                    System.out.println(String.format("new Line : %s", lines[index - 1].toString()));
                }
                left = right;
            }
        }
        return index;
    }
    /**
     * after sort, lines got larger slope or smaller min point when same slope is latter
     */
    private Line[] trim(Line[] lines, int index) {
        Line[] newLines = new Line[index];
        for (int i = 0; i < index; i++) {
            newLines[i] = lines[i];
        }
        lines = newLines;
        Arrays.sort(lines);         // lines is sorted

        //        System.out.println("-----------sorted Lines---------------");
        //        for (Line l : lines)
        //            System.out.println(l.toString());
        //        System.out.println("-----------sorted Lines---------------");
        int left = 0;
        int right = 0;
        int pivot = 0;
        while (left < lines.length && right < lines.length) {
            while (right < lines.length  && new LineComparator().compare(lines[left], lines[right])== 0) {
                right++;
            }
            lines[pivot++] = lines[right - 1];
            left = right;
        }
        Line[] newLine = new Line[pivot];
        for (int i = 0; i < pivot; i++)
            newLine[i] = lines[i];
        return newLine;
    }
    private LineSegment[] getSegmentsByLines(Line[] lines) {
        LineSegment[] mSegments = new LineSegment[lines.length];
        for (int i = 0; i < lines.length; i++) {
            mSegments[i] = new LineSegment(lines[i].getMin(), lines[i].getMax());
        }
        return mSegments;
    }
    /**
     * the difference from {@link Line#compareTo(Line)} is this comparator doesn't compare min points of two lines,
     * this class just compare lines by slopes and max points.
     */
    private class LineComparator implements Comparator<Line> {

        @Override
        public int compare(Line l1, Line l2) {
            if (Double.compare(l1.getSlope(), l2.getSlope()) < 0)    return -1;
            if (Double.compare(l1.getSlope(), l2.getSlope()) > 0)    return 1;
            else {
                if (l1.getMax().compareTo(l2.getMax()) > 0)          return 1;
                else if (l1.getMax().compareTo(l2.getMax()) < 0)     return -1;
                return 0;
            }
        }

    }
    private class Line implements Comparable<Line> {
        private final Point min;
        private final Point max;
        private double slope;
        public Line(Point mMin, Point mMax) {
            this.min = mMin;
            this.max = mMax;
            this.slope = mMin.slopeTo(mMax);
        }
        public Point getMin() {
            return min;
        }
        public Point getMax() {
            return max;
        }
        public double getSlope() {
            slope = min.slopeTo(max);
            return slope;
        }
        @Override
        public String toString() {
            return "Line " + min + " -> " + max + " slope=" + slope;
        }
        @Override
        public int compareTo(Line mLine) {
            if (Double.compare(getSlope(), mLine.getSlope()) < 0)    return -1;
            if (Double.compare(getSlope(), mLine.getSlope()) > 0)    return 1;
            else {
                if (getMax().compareTo(mLine.getMax()) > 0)
                    return 1;
                else if (getMax().compareTo(mLine.getMax()) < 0)
                    return -1;
                else {
                    if (getMin().compareTo(mLine.getMin()) < 0)     // this line has smaller min point than mLine, so it's **bigger**
                        return 1;
                    else if (getMin().compareTo(mLine.getMin()) > 0) // this line has bigger min point than mLine, so it's **smaller**
                        return -1;
                    else
                        return 0;
                }
            }
        }
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        FastCollinearPoints fcp = new FastCollinearPoints(points);
        LineSegment[] ls = fcp.segments();
        System.out.println("----------------------------------------");
        for (int i = 0; i < ls.length; i++) 
            System.out.println(ls[i]);
        System.out.println("count of slopes is " + ls.length);
    }
}