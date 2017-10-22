import edu.princeton.cs.algs4.Merge;

public class FastCollinearPoints {
    
    private Point[] points;
    
    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
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
    public int numberOfSegments()                  // the number of line segments
    {
        return segments().length;
    }
    public LineSegment[] segments()                // the line segments
    {
        int len = points.length * (points.length + 1) / 2;
        LineSegment[] lineSegments = new LineSegment[len];  // array for storing line segments
//        Double[] slopes = new Double[points.length];
//        int pivot = 0;
//        
//        Merge.sort(points);
//        judegeCollinear(slopes);
//        lineSegments[pivot++] = new LineSegment(points[i], points[j]); 
        
        return lineSegments;
    }
    private void judegeCollinear(Double[] slopes) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                slopes[i] = points[i].slopeTo(points[j]);
            }
            Merge.sort(slopes);
        }
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
}