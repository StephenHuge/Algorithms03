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
        LineSegment[] lineSegments = new LineSegment[points.length];
        for (int i = 0; i < points.length - 3; i++) {
            Double[] slopes = new Double[4];
            for (int j = i+ 1; j < i + 4; j++) {
                slopes[j - 1] = points[i].slopeTo(points[j]);
            }
            // 1. checkRepeated(), if so, get max lineSegment
            // 2. 
        }
        return null;
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
            System.out.println(ls[i]);
        System.out.println("----------------------------------------");
        System.out.println("count of slopes is " + ls.length);
    }
}
