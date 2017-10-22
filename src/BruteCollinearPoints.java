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
    private void validate(Point[] points) {
        if (points == null || (repeated(points) < 0))
            throw new java.lang.IllegalArgumentException();
    }
    /**
     * check whether array points contains repeated points or members of points are null
     */
    private int repeated(Point[] points) {
        for (int i = 0; i < points.length; i++)
        {
            for (int j = i + 1; j < points.length; j++)
            {
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

        LineSegment[] lineSegments = new LineSegment[points.length * (points.length + 1) / 2];
        double[] slopes = new double[lineSegments.length];  // array for storing slopes for segments

        double slope;       // a temporary variable
        boolean repeated = false;   // tell whether slope is repeated
        int pivot = 0;      // a pivot to array segments and slopes

        for (int i = 0; i < points.length; i++)
        {
            for (int j = i + 1; j < points.length; j++) 
            {
                slope = points[i].slopeTo(points[j]);    // current slope
                System.out.println("check: " + points[i].toString() +
                        " -> " + points[j].toString() +
                        "slop is " + slope);
                if (pivot != 0)
                {
                    for (int k = 0; k < pivot; k++)
                    {
                        if (slopes[k] == slope)     // repeated slope, ignore this one
                        {
                            System.out.println(lineSegments[k].toString() +
                                    " has same slope with " +
                                    points[i].toString() + "->" +
                                    points[j].toString() + "ignore");
                            repeated = true;
                            slope = 0;
                            break;
                        }
                    }
                }
                if (!repeated)
                {
                    slopes[pivot] = slope;    // no repeated slope, add this one
                    lineSegments[pivot] = new LineSegment(points[i], points[j]);
                    System.out.println("pivot: " + pivot);
                    pivot++;
                }
                slope = 0;
                repeated = false;
            }
        }
        if (pivot > 0) 
        {
            LineSegment[] newSegments = new LineSegment[pivot]; // generate a new array

            for (int i = 0; i < pivot; i++)
            {
                newSegments[i] = lineSegments[i];
            }
            return newSegments;
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
            System.out.println("line segments: " + i + ls[i]);
        System.out.println("----------------------------------------");
        System.out.println("count of slopes is " + ls.length);
    }
}
