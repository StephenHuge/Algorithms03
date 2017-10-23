import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;

public class FastCollinearPoints {

    private final Point[] points;

    private int length = 0;

    public FastCollinearPoints(Point[] ps)     // finds all line segments containing 4 or more points
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
     * or contains repeated points. If so, throw a java.lang.IllegalArgumentException
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
    public int numberOfSegments()                  // the number of line segments
    {
        if (length == 0)    length = segments().length;
        return length;
    }
    public LineSegment[] segments()                // the line segments
    {
        int len = points.length * (points.length + 1) / 2;
        LineSegment[] lineSegments = new LineSegment[len];
        Line[] max = new Line[points.length];
        int index = 0;      // index for lineSegments 

        for (int i = 0; i < points.length; i++) {
            Line[] lines = new Line[points.length - 1];
            {
                int p = 0;
                for (int j = 0; j < points.length; j++) {
                    if (i != j)     lines[p++] = new Line(points[i], points[j]); 
                }
            }
            Merge.sort(lines);      // sort array slopes

//            System.out.println("------------");
//            System.out.println("Lines: ");
//            for (Line l : lines)
//                System.out.println(l + " ");
//            System.out.println("------------");

            int pivotL = 0;      // pivot left to multiple array slopes
            int pivotR = 1;      // pivot right to multiple array slopes
            
            while (pivotL < lines.length && pivotR < lines.length) {
                while (pivotR < lines.length && 
                        Double.compare(lines[pivotL].getSlope(), lines[pivotR].getSlope()) == 0)
                    pivotR++;
                if (pivotR - pivotL >= 3) {
                    Line line = new Line(lines[pivotL].start, lines[pivotR - 1].end);
                    if (notRepeated(max, line)) {
                        max[index] = line;
                        lineSegments[index] = new LineSegment(lines[pivotL].start, lines[pivotR - 1].end);
                        index++;
                        
//                        System.out.println("Max Line: ");
//                        System.out.println(l.start + " -> " + l.end);
                    }
                }
                pivotL = pivotR;
                pivotR = pivotR + 1;
            }
        }
        lineSegments = trim(lineSegments, index);
        return lineSegments;
    }
    private boolean notRepeated(Line[] max, Line l) {
        if (max[0] == null) return true;
        for (int i = 0; i < max.length; i++) {
            if (max[i] == null)     break;
            
//            System.out.println("max " + max[i].start + " " + max[i].end);
//            System.out.println("l " + l.start + " " + l.end);
            
            if (Double.compare(max[i].getSlope(), l.getSlope()) == 0 && l.hasSameEndPoint(max[i])) {
                if (l.start.compareTo(max[i].start) >= 0 &&
                        l.end.compareTo(max[i].end) <= 0) {
//                    System.out.println("repeated" + l.start + " -> " + l.end);
                    return false;
                }
            }
        }
        return true;
    }
    private LineSegment[] trim(LineSegment[] lineSegments, int pivot) {
        LineSegment[] newSegments = new LineSegment[pivot];
        for (int i = 0; i < pivot; i++) {
            newSegments[i] = lineSegments[i];
        }
        return newSegments;
    }
    private class Line implements Comparable<Line> {
        Point start;
        Point end;
        public Line(Point p1, Point p2) {
            if (p1.compareTo(p2) > 0) {
                this.start = p2;
                this.end = p1;
            } else {
                this.start = p1;
                this.end = p2;
            }
        }

        public boolean hasSameEndPoint(Line line) {
            if (this.start.compareTo(line.start) == 0 ||
                    this.start.compareTo(line.end) == 0 ||
                    this.end.compareTo(line.start) == 0 ||
                    this.end.compareTo(line.end) == 0) {
                return true;
            }
            return false;
        }

        public double getSlope() {
            return start.slopeTo(end);
        }

        @Override
        public String toString() {
            return "Line " + start + " -> " + end + " " + getSlope();
        }

        @Override
        public int compareTo(Line line) {
            int ans = Double.compare(this.getSlope(), line.getSlope());
            if (ans < 0)  return -1;
            if (ans > 0)  return 1;
            else {
                if (this.end.compareTo(line.end) < 0)   return -1;
                if (this.end.compareTo(line.end) > 0)   return 1;
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
//        System.out.println("----------------------------------------");
        for (int i = 0; i < ls.length; i++) 
            System.out.println(ls[i]);
//        System.out.println("count of slopes is " + ls.length);
    }
}