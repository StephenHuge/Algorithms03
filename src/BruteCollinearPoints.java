import java.util.Arrays;
import java.util.Comparator;

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

    private LineSegment[] lineSegments;

    private int length = 0;

    public BruteCollinearPoints(Point[] ps)    // finds all line segments containing 4 points
    {
        validate(ps);
        this.points = new Point[ps.length];
        for (int i = 0; i < ps.length; i++) {
            points[i] = ps[i];
        }
        Arrays.sort(points);

        lineSegments = new LineSegment[ps.length * (ps.length + 1) / 2];

        generateSegments(points);
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
    private void generateSegments(Point[] ps) {
        int pivot = 0;
        for (int i = 0; i < ps.length - 3; i++) {
            Comparator<Point> comparator = points[i].slopeOrder();
            for (int j = i + 1; j < ps.length - 2; j++) {
                if (ps[i] == ps[j]) continue;
                for (int k = j + 1; k < ps.length - 1; k++) {
                    if (ps[i] == ps[k]) continue;
                    if (ps[j] == ps[k]) continue;
                    if (comparator.compare(points[j], points[k]) == 0) {
//                        System.out.println(String.format("i = %d %s, j = %d %s, k = %d %s",
//                                i, ps[i].toString(), 
//                                j, ps[j].toString(),
//                                k, ps[k].toString()));
                        for (int m = k + 1; m < ps.length; m++) {
                            if (ps[i] == ps[m]) continue;
                            if (ps[j] == ps[m]) continue;
                            if (ps[k] == ps[m]) continue;
                            if (comparator.compare(ps[k], ps[m]) == 0) {
                                lineSegments[pivot++] = new LineSegment(ps[i], ps[m]);
//                                System.out.println(String.format("line segment: %s",
//                                        lineSegments[pivot - 1].toString()));
                            }
                        }
                    }
                }
            }
        }
        length = pivot;      // keep pivot empty
    }
    public int numberOfSegments()        // the number of line segments
    {
        return length;
    }
    public LineSegment[] segments()                // the line segments
    {
        lineSegments = trim(lineSegments);
        return lineSegments;
    } 
    /**
     * trim lineSegments to array with no null entry
     */
    private LineSegment[] trim(LineSegment[] lineSegments) {
        LineSegment[] segments = new LineSegment[length];
        for (int i = 0; i < length; i++) {
            segments[i] = lineSegments[i];
        }
        return segments;
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
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        LineSegment[] ls = bcp.segments();
        System.out.println("----------------------------------------");
        for (int i = 0; i < ls.length; i++) 
            System.out.println(ls[i]);
        System.out.println("count of slopes is " + ls.length);
    }
}
