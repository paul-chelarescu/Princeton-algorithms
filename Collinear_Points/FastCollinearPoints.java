import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.util.*;

/**
 * Detect 4 or more collinear points.
 * Complexity O(n^2*log(n))
 */
public class FastCollinearPoints {
    private Point[] points;
    private LineSegment[] segments;
    
    // Finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException("No collinear " +
                "points without points, dummy!");
        }
        for (Point p : points) {
            if (p == null) {
                throw new NullPointerException("One of the points supplied " +
                    "was null");
            }
        }
        Point[] newPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            newPoints[i] = points[i];
        }
        Arrays.sort(newPoints);
        for (int i = 0; i < newPoints.length - 1; i++) {
            if (newPoints[i].slopeTo(newPoints[i + 1]) == Double
                .NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("Duplicate point found");
            }
        }
        this.points = newPoints;
    }
    
    // The number of line segments
    public int numberOfSegments() {
        if (segments == null) {
            return segments().length;
        }
        else {
            return segments.length;
        }
    }
    
    // The line segments
    public LineSegment[] segments() {
        if (segments == null) {
            ArrayList<LineSegment> seg = new ArrayList<>();
            for (int i = 0; i < points.length; i++) {
                Arrays.sort(points, points[i].slopeOrder());
                int j = 0;
                while (j < points.length - 2) {
                    boolean sameSlope;
                    boolean increasingOrder = true;
                    int k = j;
                    do {
                        j++;
                        sameSlope = (Double.compare(points[0].slopeTo(points[j]),
                            points[0].slopeTo(points[j + 1])) == 0);
                        if (sameSlope && ((points[0].compareTo(points[j]) > 0)
                            || (points[0].compareTo(points[j + 1]) > 0))) {
                            increasingOrder = false;
                        }
                    } while (sameSlope && j < points.length - 2);
                    if (j == points.length - 1 || sameSlope) {
                        j++;
                    }
                    if (j - k > 2 && increasingOrder) {
                        seg.add(new LineSegment(points[0], points[j]));
                    }
                    
                }
                Arrays.sort(points);
            }
            segments = new LineSegment[seg.size()];
            Object[] oo = seg.toArray();
            for (int i = 0; i < oo.length; i++) {
                segments[i] = (LineSegment) oo[i];
            }
            LineSegment[] newSegments = new LineSegment[segments.length];
            for (int i = 0; i < segments.length; i++) {
                newSegments[i] = segments[i];
            }
            return newSegments;
        }
        else {
            LineSegment[] newSegments = new LineSegment[segments.length];
            for (int i = 0; i < segments.length; i++) {
                newSegments[i] = segments[i];
            }
            return newSegments;
        }
    }
    
    public static void main(String[] args) throws Exception {
        // Read the n points from a file
        Scanner in = new Scanner(new File(args[0]));
        int n = in.nextInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            points[i] = new Point(x, y);
        }
        
        // Draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // Print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
