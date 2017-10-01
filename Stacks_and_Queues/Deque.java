// This implementation will use a doubly linked list
import java.util.NoSuchElementException;
import java.util.Iterator;
  
public class Deque<Item> implements Iterable<Item> {
    private Node first; // First element
    private Node last; // Last element
    private int size;
    private class Node {
        Item item;
        Node next;
        Node prev;
        
        public Node(Item item) {
            if (item == null) throw new NullPointerException("No null elements in the deque");
            this.item = item;
        }
        
        public void connectRight(Node other) {
            this.next = other;
            other.prev = this;
        }
    }
    
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }
    public boolean isEmpty() {
        return size() == 0 && first == null && last == null;
    }
    
    public int size() {
        return size;
    }
    
    public void addFirst(Item item) {
        if (first == null) {
            Node onlyNode = new Node(item);
            first = onlyNode;
            last = onlyNode;
            size = 1;
        }
        else {
            Node newNode = new Node(item);
            Node oldNode = first;
            newNode.connectRight(oldNode);
            first = newNode;
            size++;
        }
    }
        
    public void addLast(Item item) {
        if (last == null) {
            Node onlyNode = new Node(item);
            last = onlyNode;
            first = onlyNode;
            size = 1;
        }
        else {
            Node newNode = new Node(item);
            Node oldNode = last;
            oldNode.connectRight(newNode);
            last = newNode;
            size++;
        }
    }
        
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        // If only one element
        if (first == last) {
            Node onlyNode = first;
            first = null;
            last = null;
            size--;
            return onlyNode.item;
        }
        // More than one element
        else {
            Node oldFirst = first;
            first = first.next;
            first.prev = null;
            oldFirst.next = null;
            size--;
            return oldFirst.item;
        }
    }
    
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        // If only one element
        if (last == first) {
            Node onlyNode = last;
            last = null;
            first = null;
            size--;
            return onlyNode.item;
        }
        // More than one element
        else {
            Node oldLast = last;
            last = last.prev;
            last.next = null;
            oldLast.prev = null;
            size--;
            return oldLast.item;
        }
    }
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() { return current != null; }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item oldCurrent = current.item;
            current = current.next;
            return oldCurrent;
        }
    }
    
    public static void main(String[] args) {
        initTests();
        testAll4Operations();
        testAddFirstRemoveLast();
    }
    private static void testAddFirstRemoveLast() {
        System.out.println("Test Add First Remove Last");
        Deque<String> d = new Deque<String>();
        d.addFirst("0");
        System.out.println(d.removeLast());
        System.out.println(d.isEmpty());
        d.addFirst("3");
        System.out.println(d.removeLast());
    }
    private static void testAll4Operations() {
        System.out.println("Test 4 operations");
        Deque<String> d = new Deque<String>();
        d.addFirst("A");
        d.addLast("B");
        System.out.println(d.removeFirst());
        System.out.println(d.removeLast());
        
    }
        
    private static void initTests() {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("A");
        deque.addFirst("B");
        deque.addFirst("C");
        while (!deque.isEmpty()) {
            System.out.print(deque.removeFirst() + " ");
        }
        System.out.println("size " + deque.size());
 
        deque.addFirst("D");
        System.out.print(deque.removeLast());
        System.out.println(" size " + deque.size() + " empty " + deque.isEmpty());
        
        deque.addLast("A");
        deque.addLast("B");
        deque.addLast("C");
        while (!deque.isEmpty()) {
            System.out.print(deque.removeFirst() + " ");
        }
        System.out.print("size " + deque.size());
        System.out.println(" empty: " + deque.isEmpty());
        
        
        deque.addLast("A");
        deque.addLast("B");
        deque.addLast("C");
        
        for (String elem : deque) {
            System.out.print(elem + " ");
        }
        System.out.println("size " + deque.size() + " empty " + deque.isEmpty());
    }
        
}
