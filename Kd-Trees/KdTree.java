import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Set;
import java.util.TreeSet;

public class KdTree {
    private Set<Point2D> inside;
    // a nearest neighbor in the set to point p; null if the set is empty
    private Point2D nearest;
    private double bestDist;
    private Node root;
    private int size;
    
    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }
    
    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }
    
    // number of points in the set
    public int size() {
        return size;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        root = insert(null, root, p, Orientation.VERTICAL, -1);
    }
    
    private Node insert(Node parent, Node node, Point2D p, Orientation orientation, int whichSide) {
        if (node == null) {
            size++;
            if (parent == null) { return new Node(p, new RectHV(0, 0, 1, 1)); }
            else if (!parent.p.equals(p)) {
                return new Node(p, split(parent.rect, parent.p, oppositeOrientation(orientation),
                    whichSide));
            }
            return null;
        }
        if (node.p.equals(p)) { return node; }
        if (orientation == Orientation.VERTICAL) {
            if (Double.compare(p.x(), node.p.x()) < 0) {
                node.leftBottom = insert(node, node.leftBottom, p, Orientation.HORIZONTAL, 0);
            }
            else if (Double.compare(p.x(), node.p.x()) >= 0) {
                node.rightTop = insert(node, node.rightTop, p, Orientation.HORIZONTAL, 1);
            }
            return node;
        }
        else {
            if (Double.compare(p.y(), node.p.y()) < 0) {
                node.leftBottom = insert(node, node.leftBottom, p, Orientation.VERTICAL, 0);
            }
            else if (Double.compare(p.y(), node.p.y()) >= 0) {
                node.rightTop = insert(node, node.rightTop, p, Orientation.VERTICAL, 1);
            }
            return node;
        }
    }
    
    private Orientation oppositeOrientation(Orientation orientation) {
        if (orientation == Orientation.VERTICAL) { return Orientation.HORIZONTAL; }
        return Orientation.VERTICAL;
    }
    
    private RectHV split(RectHV rect, Point2D p, Orientation orientation, int whichSide) {
        if (orientation == Orientation.VERTICAL) {
            // The left
            if (whichSide == 0) {
                return new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
            }
            // The right
            else {
                return new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
        }
        else {
            // The bottom
            if (whichSide == 0) {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
            }
            // The top
            else {
                return new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
            }
        }
    }
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        return get(root, p, Orientation.VERTICAL);
    }
    
    private boolean get(Node node, Point2D p, Orientation orientation) {
        if (node == null) { return false; }
        if (orientation == Orientation.VERTICAL) {
            if (p.equals(node.p)) { return true; }
            if (Double.compare(p.x(), node.p.x()) < 0) {
                return get(node.leftBottom, p, Orientation.HORIZONTAL);
            }
            else if (Double.compare(p.x(), node.p.x()) >= 0) {
                return get(node.rightTop, p, Orientation.HORIZONTAL);
            }
            return false;
        }
        else {
            if (p.equals(node.p)) { return true; }
            if (Double.compare(p.y(), node.p.y()) < 0) {
                return get(node.leftBottom, p, Orientation.VERTICAL);
            }
            else if (Double.compare(p.y(), node.p.y()) >= 0) {
                return get(node.rightTop, p, Orientation.VERTICAL);
            }
            return false;
        }
    }
    
    // draw all points to standard draw
    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.clear();
        draw(root, Orientation.VERTICAL);
    }
    
    private void draw(Node node, Orientation orientation) {
        if (node == null) { return; }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();
        StdDraw.show();
        if (orientation == Orientation.VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.005);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            StdDraw.show();
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.005);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            StdDraw.show();
        }
        draw(node.leftBottom, oppositeOrientation(orientation));
        draw(node.rightTop, oppositeOrientation(orientation));
    }
    
    //    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        inside = new TreeSet<>();
        range(rect, root, Orientation.VERTICAL);
        return inside;
    }
    
    private void range(RectHV rect, Node node, Orientation o) {
        if (node == null) { return; }
        if (rect.contains(node.p)) { inside.add(node.p); }
        if (intersectsRectPoint(rect, node.p, o)) {
            range(rect, node.leftBottom, oppositeOrientation(o));
            range(rect, node.rightTop, oppositeOrientation(o));
        }
        else {
            if (compRectPoint(rect, node.p, o) < 0) {
                range(rect, node.leftBottom, oppositeOrientation(o));
            }
            else {
                range(rect, node.rightTop, oppositeOrientation(o));
            }
        }
    }
    
    private boolean intersectsRectPoint(RectHV rect, Point2D p, Orientation o) {
        if (o == Orientation.VERTICAL) {
            return rect.xmin() <= p.x() && rect.xmax() >= p.x();
        }
        return rect.ymin() <= p.y() && rect.ymax() >= p.y();
    }

    private int compRectPoint(RectHV rect, Point2D p, Orientation o) {
        if (o == Orientation.VERTICAL) {
            if (Double.compare(rect.xmax(), p.x()) < 0) { return -1; }
            return 1;
        }
        if (Double.compare(rect.ymax(), p.y()) < 0) { return -1; }
        return 1;
    }
    
    public Point2D nearest(Point2D p) {
        nearest = null;
        bestDist = Integer.MAX_VALUE;
        nearest(p, root, Orientation.VERTICAL);
        return nearest;
    }
    
    private void nearest(Point2D p, Node node, Orientation o) {
        if (node == null) return;
        if (bestDist > node.p.distanceSquaredTo(p)) {
            bestDist = node.p.distanceSquaredTo(p);
            nearest = node.p;
        }
        if (comparePointNode(p, node, o) < 0) {
            nearest(p, node.leftBottom, oppositeOrientation(o));
            if (distPointToNode(p, node, o) < bestDist) {
                nearest(p, node.rightTop, oppositeOrientation(o));
            }
        }
        else {
            nearest(p, node.rightTop, oppositeOrientation(o));
            if (distPointToNode(p, node, o) < bestDist) {
                nearest(p, node.leftBottom, oppositeOrientation(o));
            }
        }
    }
    
    private int comparePointNode(Point2D p, Node node, Orientation o) {
        if (o == Orientation.VERTICAL) {
            return Double.compare(p.x(), node.p.x());
        }
        return Double.compare(p.y(), node.p.y());
    }
    
    private double distPointToNode(Point2D p, Node node, Orientation o) {
        if (o == Orientation.VERTICAL) {
            return Math.pow(p.x() - node.p.x(), 2);
        }
        return Math.pow(p.y() - node.p.y(), 2);
    }
    
    private enum Orientation {VERTICAL, HORIZONTAL}
    
    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node leftBottom;
        private Node rightTop;
        
        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }
}
