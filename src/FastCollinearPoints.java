import java.util.Arrays;


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

        lineSegments = new LineSegment[ps.length * (ps.length + 1) / 2];
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

    public int numberOfSegments()                  // the number of line segments
    {
        return length;
    }
    public LineSegment[] segments()                // the line segments
    {
        Line[] lines = new Line[points.length * (points.length + 1) / 2];
        int index = 0;

        for (int i = 0; i < points.length - 3; i++) {
            Point origin = points[i];
            Point[] mPoints = new Point[points.length - i - 1]; // copy to sort and store operation
            for (int j = i + 1; j < points.length; j++) {
                mPoints[j - i - 1] = points[j];
            }
            Arrays.sort(mPoints, origin.slopeOrder());  // sort with order slopeOrder

            int left = 0;
            int right = 1;

            while (left < mPoints.length && right < mPoints.length) {
//                System.out.println("origin -> mPoints[left] " + 
//                                origin.toString() + " -> " + mPoints[left].toString() + 
//                                ", slope: " +origin.slopeTo(mPoints[left])+ 
//                                " \norigin -> mPoints[right] " + 
//                                origin.toString() +" ->  " + mPoints[right].toString() + 
//                                ", slope: " + origin.slopeTo(mPoints[right]) +
//                                "\n------------------------------");
                while (right < mPoints.length &&  
                        origin.slopeOrder().
                        compare(mPoints[left], mPoints[right]) == 0) {
                    right++;
                } 
                if (right - left >= 3) {
                    lines[index++] = new Line(origin, mPoints[right - 1]);
                    System.out.println(String.format("new Line : %s", lines[index - 1].toString()));
                }
                left = right;
                right = right + 1;
            }
        }
//        lines = trim(lines);
        return getSegmentsByLines(lines, index);
    }
    private Line[] trim(Line[] lines) {
        return null;
    }
    private LineSegment[] getSegmentsByLines(Line[] lines, int index) {
        LineSegment[] lineSegments = new LineSegment[index];
        for (int i = 0; i < index; i++) {
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
        public void setMin(Point min) {
            this.min = min;
        }
        public Point getMax() {
            return max;
        }
        public void setMax(Point max) {
            this.max = max;
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
                if (getMin().compareTo(o.getMin()) < 0)     // this line has smaller min point than o, so it's **bigger**
                    return 1;
                else if (getMin().compareTo(o.getMin()) > 0) // this line has bigger min point than o, so it's **smaller**
                    return -1;
                else
                    return 0;
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