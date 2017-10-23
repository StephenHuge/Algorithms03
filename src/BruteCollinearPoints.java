import edu.princeton.cs.algs4.In;

/**
 * examines 4 points at a time and checks whether they all lie on the same line segment, 
 * returning all such line segments. To check whether the 4 points p, q, r, and s are 
 * collinear, check whether the three slopes between p and q, between p and r, and 
 * between p and s are all equal.
 * 
 * @author HJS
 * 
 * @date 2017-10-21
 * 
 */
public class BruteCollinearPoints {

    private final Point[] points;
    
    private int length = 0;

    public BruteCollinearPoints(Point[] ps)    // finds all line segments containing 4 points
    {
        validate(ps);
        this.points = copy(ps);
    }
    private Point[] copy(Point[] ps) {
        Point[] copy = new Point[ps.length];
        for (int i = 0; i < ps.length; i++) {
            copy[i] = ps[i];
        }
        return copy;
    }
    /**
     * validate method, check whether array points is null, members of points are null
     * or contain repeated points. If so, throw a java.lang.IllegalArgumentException
     */
    private void validate(Point[] ps) 
    {
        if (ps == null || (repeated(ps) < 0) || validateNullEntry(ps))
            throw new java.lang.IllegalArgumentException();
    }
    private boolean validateNullEntry(Point[] ps) {
        for (Point p : ps)
            if (p == null)  return true;
        return false;
    }
    /**
     * check whether array points contains repeated points or members of points are null
     */
    private int repeated(Point[] ps) 
    {
        for (int i = 0; i < ps.length; i++) {
            for (int j = i + 1; j < ps.length; j++) {
                if (ps[i] == null || ps[j] == null
                        || ps[i].compareTo(ps[j]) == 0)   return -1;
            }
        }
        return 1;
    }
    public int numberOfSegments()        // the number of line segments
    {
        if (length == 0)    length = segments().length;
        return length;
    }
    public LineSegment[] segments()                // the line segments
    {
        int len = points.length * (points.length + 1) / 2;
        LineSegment[] lineSegments = new LineSegment[len];
        Line[] lines = new Line[len];
        int pivot = 0;          // pivot for array lineSegments

        for (int i = 0; i < points.length - 3; i++) {
            double[] slopes = new double[3];
            for (int j = i+ 1; j < points.length - 2; j++) {
                slopes[0] = points[i].slopeTo(points[j]);           // first line
                for (int k = j + 1; k < points.length - 1; k++) {
                    slopes[1] = points[i].slopeTo(points[k]);       // second line
                    for (int m = k + 1; m < points.length; m++) {
                        slopes[2] = points[i].slopeTo(points[m]);   // third line
                        
                        if (collinear(slopes)) {        // three slope are collinear
                            Line line = getMaxLine(points, i, j, k, m);   // get one max line
                            int repeated = 0;
                            repeated = line.notRepeated(lines);
                            
                            if (repeated == -1) {   // line not repeated, just add a new line
                                lines[pivot] = line;
                                lineSegments[pivot] = new LineSegment(line.min, line.max);
                                pivot++;
                            } else if (repeated != -2) { // line repeated, replace the old repeated one with a new one
                                lines[repeated] = line;
                                lineSegments[repeated] = new LineSegment(lines[repeated].min, lines[repeated].max);
                            } 
                        } 
                    }
                }
            }
        }
        lineSegments = trim(lineSegments, pivot);    // trim array lineSegments with no null elements
        return lineSegments;
    } 
    private LineSegment[] trim(LineSegment[] lineSegments, int pivot) {
        LineSegment[] newSegments = new LineSegment[pivot];
        for (int i = 0; i < pivot; i++) {
            newSegments[i] = lineSegments[i];
        }
        return newSegments;
    }
    private Line getMaxLine(Point[] points, int start, int end1, int end2, int end3) {
        Point min = points[start];
        Point max = points[start];
        Point[] fourPoints = {points[start], points[end1], points[end2], points[end3]};
        for (int i = 0; i < fourPoints.length; i++) {
            if (fourPoints[i].compareTo(min) < 0)   min = fourPoints[i];
            if (fourPoints[i].compareTo(max) > 0)   max = fourPoints[i];
        }
        return new Line(min, max);
    }
    private boolean collinear(double[] slopes) {
        double start = slopes[0];
        for (int i = 1; i < slopes.length; i++) {
            if (Double.compare(start, slopes[i]) != 0)     return false;
        }
        return true;
    }
    private static class Line {
        Point min;
        Point max;
        public Line(Point min, Point max) {
            if (min.compareTo(max) > 0) {
                Point temp = min;
                max = min;
                min = temp;
            }
            this.min = min;
            this.max = max;
        }
        /**
         * check whether there is repeating line in array lines, if so, return pivot {@code i} 
         * of the array, else return {@code -1}.
         */
        public int notRepeated(Line[] lines) {
            for (int i = 0; i < lines.length; i++) {
                if (lines[i] == null)  break;
                if (Double.compare(getSlope(), lines[i].getSlope()) == 0
                        && hasSameEndPoint(lines[i])) {   // have same slope
                    if (this.min.compareTo(lines[i].min) <= 0 
                            && this.max.compareTo(lines[i].max) >= 0) {
                        return i;
                    }
                    return -2;  // same slope but "smaller", just ignore this line
                }
            }
            return -1;
        }
        public boolean hasSameEndPoint(Line line) {
            if (this.min.compareTo(line.min) == 0 ||
                    this.min.compareTo(line.max) == 0 ||
                    this.max.compareTo(line.min) == 0 ||
                    this.max.compareTo(line.max) == 0) {
                return true;
            }
            return false;
        }
        public double getSlope() {
            return max.slopeTo(min);
        }
        @Override
        public String toString() {
            return "Line " + min + " -> " + max;
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
            //            System.out.println(points[i].toString());
        }
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        LineSegment[] ls = bcp.segments();
//        System.out.println("----------------------------------------");
        for (int i = 0; i < ls.length; i++) 
            System.out.println(ls[i]);
//        System.out.println("count of slopes is " + ls.length);
    }
}
