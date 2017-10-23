import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {

    public FastCollinearPoints(Point[] ps)     // finds all line segments containing 4 or more points
    {
    }
    public int numberOfSegments()                  // the number of line segments
    {
       return -1;
    }
    public LineSegment[] segments()                // the line segments
    {
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
        }
        FastCollinearPoints fcp = new FastCollinearPoints(points);
        LineSegment[] ls = fcp.segments();
//        System.out.println("----------------------------------------");
        for (int i = 0; i < ls.length; i++) 
            System.out.println(ls[i]);
//        System.out.println("count of slopes is " + ls.length);
    }
}