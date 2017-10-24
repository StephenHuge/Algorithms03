import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class TestClient {
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        System.out.println("Start showing " + args[0]);
        
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        LineSegment[] ls;
        if (args[1] == "1") {
            BruteCollinearPoints collinear = new BruteCollinearPoints(points);
            ls = collinear.segments();
        } else {
            FastCollinearPoints collinear = new FastCollinearPoints(points);
            ls = collinear.segments();
        }
        
        for (LineSegment segment : ls) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        System.out.println("count of slopes is " + ls.length);
        System.out.println("End showing " + args[0]);
        while (true) {
            if (StdDraw.isKeyPressed(0x20)) {
                StdDraw.pause(500);
                StdDraw.clear();    
                break;
            }
        }
            
    }
}
