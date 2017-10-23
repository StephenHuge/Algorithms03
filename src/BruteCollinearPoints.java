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
        int len = points.length * (points.length + 1) / 2;
        LineSegment[] lineSegments = new LineSegment[len];
        Line[] lines = new Line[len];
        int pivot = 0;          // pivot for array lineSegments

        for (int i = 0; i < points.length - 3; i++) {
            Double[] slopes = new Double[3];
            for (int j = i+ 1; j < points.length - 2; j++) {
                slopes[0] = points[i].slopeTo(points[j]);           // first line
                for (int k = j + 1; k < points.length - 1; k++) {
                    slopes[1] = points[i].slopeTo(points[k]);       // second line
                    for (int m = k + 1; m < points.length; m++) {
                        slopes[2] = points[i].slopeTo(points[m]);   // third line

                        //                        Point[] fourPoints = {points[i], points[j], points[k], points[m]};
                        //                        System.out.println("Four points: ");
                        //                        for (Point p : fourPoints)
                        //                            System.out.print(p + "\t");
                        //                        System.out.println();
                        //                        System.out.println("Three slopes: ");
                        //                        for (Double s : slopes)
                        //                            System.out.print(s + " \t");
                        //                        System.out.println();
                        //                        System.out.println("--------------");

                        Line line = getMaxLine(points, i, j, k, m);   // get one max line
                        if (collinear(slopes)) {
                            int repeated = 0;
                            if ((repeated = line.notRepeated(lines)) == -1) {
                                lines[pivot] = line;
                                lineSegments[pivot] = new LineSegment(lines[pivot].min, lines[pivot].max);
                                pivot++;
                            } else {
                                lines[repeated] = line;
                                lineSegments[repeated] = new LineSegment(lines[repeated].min, lines[repeated].max);
                            }
                            
                        }
                    }
                }
            }
        }
        
//        System.out.println("Lines: ");
//        for (Line l : lines) {
//            if (l == null)  break;
//            System.out.print(l + " ");
//        }
//        System.out.println();
        
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
    private Line getMaxLine(Point[] points2, int start, int end1, int end2, int end3) {
        Point min = points[start];
        Point max = points[start];
        Point[] fourPoints = {points[start], points[end1], points[end2], points[end3]};

        //        System.out.println("Four points: ");
        //        for (Point p : fourPoints)
        //            System.out.print(p + "\t");
        //        System.out.println();

        for (int i = 0; i < fourPoints.length; i++) {
            if (fourPoints[i].compareTo(min) < 0)   min = fourPoints[i];
            if (fourPoints[i].compareTo(max) > 0)   max = fourPoints[i];
        }

        //        System.out.println("min: " + min + ", max: " + max);

        return new Line(min, max);
    }
    private boolean collinear(Double[] slopes) {
        double start = slopes[0];
        for (int i = 1; i < slopes.length; i++) {
            if (start != slopes[i])     return false;
        }
        return true;
    }
    static class Line {
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
            
            System.out.println("Lines---------");
            for (Line l : lines) {
                if (l == null)  break;
                System.out.print(l + " ");
            }
            System.out.println();
            
            for (int i = 0; i < lines.length; i++) {
                if (lines[i] == null)  break;
                if (getSlope() == lines[i].getSlope()) {   // have same slope
                    
                    System.out.println("check: " + this.toString() + " - " + lines[i].toString());
                    System.out.println(this.min.compareTo(lines[i].min) <= 0);
                    System.out.println(this.max.compareTo(lines[i].max) >= 0);
                    if (this.min.compareTo(lines[i].min) <= 0 
                            && this.max.compareTo(lines[i].max) >= 0) {
                        
                        System.out.println("checked!");
                        
                        return i;
                    }
                }
            }
            return -1;
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
        for (int i = 0; i < ls.length; i++) 
            System.out.println(ls[i]);
        //        System.out.println("----------------------------------------");
        //        System.out.println("count of slopes is " + ls.length);
    }
}
