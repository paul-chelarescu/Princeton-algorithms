import edu.princeton.cs.algs4.Digraph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class SAP {
    private Digraph digraph;
    private int lastCommonAncestor;
    
    // Constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph digraph) {
        this.digraph = digraph;
    }
    
    // Length of shortest ancestral path between v and w; -1 if no such path
    private int length(Set<Integer> vSet, Set<Integer> wSet, Queue<Integer> vQueue,
                       Queue<Integer> wQueue) {
        for (int v : vSet) {
            for (int w : wSet) {
                if (v == w) { return 0; }
            }
        }
        
        boolean[] vVisited = new boolean[digraph.V()];
        boolean[] wVisited = new boolean[digraph.V()];
        
        int[] vDistance = new int[digraph.V()];
        int[] wDistance = new int[digraph.V()];
        
        for (int v : vSet) {
            vVisited[v] = true;
        }
        for (int w : wSet) {
            wVisited[w] = true;
        }
        
        boolean connected = false;
        int distanceBetween = -1;
        
        while (!connected) {
            Integer vCurrent = vQueue.poll();
            Integer wCurrent = wQueue.poll();
            if (vCurrent != null) {
                for (int neighbour : digraph.adj(vCurrent)) {
                    if (vVisited[neighbour]) { continue; }
                    if (wVisited[neighbour]) {
                        connected = true;
                        distanceBetween = vDistance[vCurrent] + wDistance[neighbour] + 1;
                        lastCommonAncestor = neighbour;
                    }
                    vVisited[neighbour] = true;
                    vDistance[neighbour] = 1 + vDistance[vCurrent];
                    vQueue.add(neighbour);
                }
            }
            if (wCurrent != null) {
                for (int neighbour : digraph.adj(wCurrent)) {
                    if (wVisited[neighbour]) { continue; }
                    if (vVisited[neighbour]) {
                        connected = true;
                        distanceBetween = wDistance[wCurrent] + vDistance[neighbour] + 1;
                        lastCommonAncestor = neighbour;
                    }
                    wVisited[neighbour] = true;
                    wDistance[neighbour] = 1 + wDistance[wCurrent];
                    wQueue.add(neighbour);
                }
            }
        }
        return distanceBetween;
    }
    
    public int length(int v, int w) {
        Queue<Integer> vQueue = new LinkedList<>();
        Queue<Integer> wQueue = new LinkedList<>();
        vQueue.add(v);
        wQueue.add(w);
        
        Set<Integer> vSet = new HashSet<>();
        Set<Integer> wSet = new HashSet<>();
        
        vSet.add(v);
        wSet.add(w);
        return length(vSet, wSet, vQueue, wQueue);
    }
    
    // A common ancestor of v and w that participates in a shortest ancestral path; -1 if no such
    // path
    public int ancestor(int v, int w) {
        length(v, w);
        return lastCommonAncestor;
    }
    
    // Length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no
    // such path
    public int length(Iterable<Integer> vSet, Iterable<Integer> wSet) {
        Queue<Integer> vQueue = new LinkedList<>();
        Queue<Integer> wQueue = new LinkedList<>();
        for (int v : vSet) { vQueue.add(v); }
        for (int w : wSet) { wQueue.add(w); }
        
        Set<Integer> vSetSend = new HashSet<>();
        Set<Integer> wSetSend = new HashSet<>();
        
        for (int v : vSet) {
            vSetSend.add(v);
        }
        for (int w : wSet) {
            wSetSend.add(w);
        }
        return length(vSetSend, wSetSend, vQueue, wQueue);
    }

    // A common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        length(v, w);
        return lastCommonAncestor;
    }
}
