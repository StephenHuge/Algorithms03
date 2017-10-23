/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (that == null)    throw new NullPointerException();

        double slope;
        double dX = that.x - x;     // distance of direction X
        double dY = that.y - y;     // distance of direction Y

        if (dX == 0)
        {
            if (dY == 0)     return Double.NEGATIVE_INFINITY;
            return Double.POSITIVE_INFINITY;
        }
        slope = dY / dX;
        return slope + 0.0;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (that == null)    throw new NullPointerException();

        int dY = y - that.y;     // distance of direction Y
        int dX = x - that.x;     // distance of direction X

        if (dY < 0)  return -1;
        if (dY > 0)  return 1;
        else {
            if (dX < 0)  return -1;        
            if (dX > 0)  return 1;
            return 0;
        }

    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new Comparator<Point>() {

            @Override
            public int compare(Point p1, Point p2) {
                if (p1 == null || p2 == null)    throw new NullPointerException();

                double slope1 = slopeTo(p1);
                double slope2 = slopeTo(p2);

                if (Double.compare(slope1, slope2) > 0)     return 1;
                if (Double.compare(slope1, slope2) < 0)     return -1;
                return 0;
            }
        };
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point[] points = new Point[4];
        points[0] = new Point(0, 0);
        points[1] = new Point(1, 1);
        points[2] = new Point(0, 1);
        points[3] = new Point(1, 0);

        for (int i = 0; i < points.length; i++)
        {
            for (int j = 0; j < points.length; j++)
            {
                //                printSlope(points[i], points[j]);
                printCompare(points[i], points[j]); 
                printOrder(points[0], points[i], points[j]);
                System.out.println("--------------------------");
            }
        }
    }

    private static void printOrder(Point p0, Point p1, Point p2) 
    {
        printSlope(p0, p1);
        printSlope(p0, p2);
        double ans = p0.slopeOrder().compare(p1, p2);
        if (ans > 0)        System.out.println("Slope from " + p0.toString() + " to " + 
                p1.toString() + " is bigger than " + p2.toString() + "'s");   
        else if (ans < 0)   System.out.println("Slope from " + p0.toString() + " to " + 
                p1.toString() + " is smaller than " + p2.toString() + "'s");
        else                System.out.println("Slope from " + p0.toString() + " to " + 
                p1.toString() + " is identical to " + p2.toString() + "'s");
    }
    private static void printCompare(Point p0, Point p1)
    {
        if (p0.compareTo(p1) < 0)       System.out.println(p0.toString() + " is larger than " + p1.toString());
        else if (p0.compareTo(p1) > 0)  System.out.println(p0.toString() + " is smaller than " + p1.toString());
        else                            System.out.println("You input two identical points");
    }
    private static void printSlope(Point p0, Point p1) 
    {
        System.out.println("Slope of " + p0.toString() + "-->" + p1.toString() + " is " + p0.slopeTo(p1));
    }
}
