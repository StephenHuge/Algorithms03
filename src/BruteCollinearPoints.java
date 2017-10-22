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

    private Point[] points;
    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        validate(points);
        this.points = points;
    }
    /**
     * validate method, check whether array points is null, members of points are null
     * or contains repeated points. If so, throw a java.lang.IllegalArgumentException
     */
    private void validate(Point[] points) 
    {
        if (points == null || (repeated(points) < 0))
            throw new java.lang.IllegalArgumentException();
    }
    /**
     * check whether array points contains repeated points or members of points are null
     */
    private int repeated(Point[] points) 
    {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i] == null || points[j] == null
                        || points[i].compareTo(points[j]) == 0)   return -1;
            }
        }
        return 1;
    }
    public int numberOfSegments()        // the number of line segments
    {
        return segments().length;
    }
    public LineSegment[] segments()                // the line segments
    {
        int len = points.length * (points.length + 1) / 2;
        LineSegment[] lineSegments = new LineSegment[len];  // array for storing line segments
        Line[] lines = new Line[len];                  // array for storing lines with points and slopes

        int pivot = 0;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                Line l  = new Line(points[i], points[j]);
                System.out.println("check " + l);
                if (pivot == 0) {
                    lines[pivot] = new Line(points[i], points[j]);       // store this line 
                    lineSegments[pivot] = new LineSegment(points[i], points[j]);
                    System.out.println("add new line: " + lines[pivot]);
                    pivot++;
                    System.out.println("-------------------");
                } else {
                    int p = pivot;
                    boolean repeated = false;
                    for (int k = 0; k < p; k++) {   // check whether there is a collinear line in the array
                        if (l.equals(lines[k])) continue;
                        
                        if (l.collinear(lines[k])) {       // if 2 line Segments is in same line
                            System.out.println("change old line " + lines[k]);
                            lines[k] = l.getMaxLine(lines[k]);  // get "max" line for these two lines
                            lineSegments[k] = new LineSegment(lines[k].p, lines[k].q);
                            System.out.println("update to " + lines[k]);
                            System.out.println("--------------------");
                            repeated = true;
                        }
                    }
                    if (!repeated) {
                        lines[pivot] = new Line(points[i], points[j]);       // store this line 
                        lineSegments[pivot] = new LineSegment(points[i], points[j]);
                        System.out.println("add new line: " + lines[pivot]);
                        pivot++;
                        System.out.println("--------------------");
                    } 
                }
            }
        }
        LineSegment[] newSegments = new LineSegment[pivot];
        for (int i = 0; i < pivot; i++) {
            newSegments[i] = lineSegments[i];
        }
        return newSegments;
    }

    private static class Line {
        Point p;
        Point q;
        double slope;
        public Line(Point p, Point q) {
            super();
            this.p = p;
            this.q = q;
            this.slope = p.slopeTo(q);
        }
        public Line getMaxLine(Line line) {
            Point[] points = {p, q, line.p, line.q};
            Point min = points[0];
            Point max = points[0];
            for (Point p : points) {
                if (p.compareTo(min) < 0)         min = p;
                else if (p.compareTo(max) > 0)    max = p;
            }
            return new Line(min, max);
        }
        @Override
        public String toString() {
            return "Line: " + p + " -> " + q;
        }
        @Override
        public boolean equals(Object line) {
            Line l = (Line) line;
            if (this.slope == l.slope && 
                    this.p.equals(l.p) && 
                    this.q.compareTo(l.q) ==0) {
                return true; 
            }
            return false;
        }
        public boolean collinear(Line line) {
            if (this.slope == line.slope) {
                if (this.p.compareTo(line.p) == 0 ||
                        this.p.compareTo(line.q) == 0 ||
                        this.q.compareTo(line.p) == 0 ||
                        this.q.compareTo(line.q) == 0) {
                    return true;
                }
                return false;
            }   
            return false;
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
        for (int i = 0; i < ls.length; i++) 
            System.out.println("line segments: " + i + ls[i]);
        System.out.println("----------------------------------------");
        System.out.println("count of slopes is " + ls.length);
    }
}
