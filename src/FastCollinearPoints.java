import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
    private final Point[] points;

    private LineSegment[] lineSegments;

    private int length = 0;

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
    /**
     * validate method, check whether array points is null, members of points are null
     * or contain repeated points. If so, throw a java.lang.IllegalArgumentException
     */
    private void validate(Point[] ps) {
        if (ps == null)     throw new java.lang.IllegalArgumentException();

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
        return getSegmentsByLines(lines);
    }
    
    private Line[] getLines(Point[] ps) {
        Line[] lines = new Line[points.length * (points.length + 1) / 2];
        int index = 0;

        for (int i = 0; i < points.length - 3; i++) {
            Point origin = points[i];
            Comparator<Point> sortOrder = origin.slopeOrder();

            Point[] mPoints = new Point[points.length - i - 1]; // copy to sort and store operation
            for (int j = i + 1; j < points.length; j++) {
                mPoints[j - i - 1] = points[j];
            }
            Arrays.sort(mPoints, sortOrder);  // sort with order origin.slopeOrder()

            int left = 0;       // pivots for getting maximum lines 
            int right = 1;
            while (left < mPoints.length && right < mPoints.length) {
                while (right < mPoints.length &&  
                        sortOrder.compare(mPoints[left], mPoints[right]) == 0) {    // same slope
                    right++;
                } 
                if (right - left >= 3) {            // more than 3 points, add new line
                    lines[index++] = new Line(origin, mPoints[right - 1]);
//                    System.out.println(String.format("new Line : %s", lines[index - 1].toString()));
                }
                left = right;
                right = right + 1;
            }
        }
        Line[] newLines = new Line[index];
        for (int i = 0; i < index; i++) {
            newLines[i] = lines[i];
        }
        lines = trim(newLines, index);      // there may be some repeated lines here
        
        return lines;
    }
    /**
     * after sort, lines got larger slope or smaller min point when same slope is latter
     */
    private Line[] trim(Line[] lines, int index) {
        Arrays.sort(lines);
        
//        System.out.println("-----------sorted Lines---------------");
//        for (Line l : lines)
//            System.out.println(l.toString());
//        System.out.println("-----------sorted Lines---------------");
        int left = 0;
        int right = 1;
        int pivot = 0;
        while (left < lines.length && right < lines.length) {
            while (right < lines.length  && lines[left].getMax().compareTo(lines[right].getMax()) == 0 && 
                    Double.compare(lines[left].getSlope(), lines[right].getSlope()) == 0) {
                right++;
            }
            lines[pivot++] = lines[right - 1];
            left = right;
//            right = right + 1;
        }
        Line[] newLine = new Line[pivot];
        for (int i = 0; i < pivot; i++)
            newLine[i] = lines[i];
        return newLine;
    }
    private LineSegment[] getSegmentsByLines(Line[] lines) {
        LineSegment[] lineSegments = new LineSegment[lines.length];
        for (int i = 0; i < lines.length; i++) {
            lineSegments[i] = new LineSegment(lines[i].getMin(), lines[i].getMax());
        }
        return lineSegments;
    }
    
    private class Line implements Comparable<Line>{
        private Point min;
        private Point max;
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
        public int compareTo(Line o) {
            if (Double.compare(getSlope(), o.getSlope()) < 0)    return -1;
            if (Double.compare(getSlope(), o.getSlope()) > 0)    return 1;
            else {
                if (getMax().compareTo(o.getMax()) > 0)
                    return 1;
                else if (getMax().compareTo(o.getMax()) < 0)
                    return -1;
                else {
                    if (getMin().compareTo(o.getMin()) < 0)     // this line has smaller min point than o, so it's **bigger**
                        return 1;
                    else if (getMin().compareTo(o.getMin()) > 0) // this line has bigger min point than o, so it's **smaller**
                        return -1;
                    else
                        return 0;
                }
            }
        }
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