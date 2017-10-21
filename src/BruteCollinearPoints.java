
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
    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        validate(points);
        
        
    }
    /**
     * validate method, check whether array points is null, members of points are null
     * or contains repeated points. If so, throw a java.lang.IllegalArgumentException 
     */
    private void validate(Point[] points) {
        if (points == null || (repeated(points) > 0))
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
        return -1;
    }
    public LineSegment[] segments()                // the line segments
    {
        return null;
    }
}
