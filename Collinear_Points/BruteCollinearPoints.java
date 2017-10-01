/***************************************************
 *  Brute force O(n^4) solution
 ***************************************************/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.util.*;

public class BruteCollinearPoints {
    private Point[] points;
    
    public BruteCollinearPoints(Point[] points) {
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
    
    private LineSegment[] segments;
    
    public int numberOfSegments() {
        if (segments == null) {
            return segments().length;
        }
        else {
            return segments.length;
        }
    }
    
    public LineSegment[] segments() {
        if (segments == null) {
            ArrayList<LineSegment> seg = new ArrayList<>();
            for (int i = 0; i < points.length; i++) {
                for (int j = i + 1; j < points.length; j++) {
                    for (int k = j + 1; k < points.length; k++) {
                        for (int ll = k + 1; ll < points.length; ll++) {
                            Point p = points[i];
                            Point q = points[j];
                            Point r = points[k];
                            Point s = points[ll];
                            if ((p.slopeTo(q) == p.slopeTo(r))
                                && (p.slopeTo(q) == p.slopeTo(s))) {
                                seg.add(new LineSegment(p, s));
                                // System.out.println(p.toString() + q
                                // .toString()
                                
                                // + r.toString()  + s.toString());
                            }
                        }
                    }
                }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
