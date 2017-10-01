import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] arr; // The array of items
    private int size;

    public RandomizedQueue() { 
        arr = (Item[]) new Object[2]; 
        size = 0;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size() == 0; }

    private void resize(int capacity) {
        if (capacity == 0) throw new IllegalArgumentException("Do not resize to size 0");
        Item[] newArr = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newArr[i] = arr[i];
        }
        arr = newArr;
    }

    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException("No null items in the queue");
        if (size == arr.length) resize(2 * size);
        arr[size++] = item;
    }
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Empty queue");
        int pos = StdRandom.uniform(size);
        Item item = arr[pos];
        arr[pos] = arr[size - 1];
        size--;
        arr[size] = null; // Avoid loitering

        if (size > 0 && size <= arr.length / 4) resize(arr.length / 2);
        return item;
    }
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Empty queue");
        int pos = StdRandom.uniform(size);
        Item item = arr[pos];
        return item;
    }
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        Item[] arr;
        int current;
        public RandomizedQueueIterator() {
            arr = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                this.arr[i] = RandomizedQueue.this.arr[i];
            }
            StdRandom.shuffle(this.arr);
            current = 0;
        }

        public boolean hasNext() { return current < arr.length; }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return arr[current++];
        }
    }
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        q.enqueue("A");
        q.enqueue("B");
        q.enqueue("C");
        System.out.print(q.dequeue() + " ");
        System.out.print(q.dequeue() + " ");
        System.out.print(q.dequeue() + " ");
        System.out.println(q.isEmpty() + " " + q.size() + "\n");
        q.enqueue("A");
        q.enqueue("B");
        q.enqueue("C");
        for (String s : q) System.out.print(s + " ");
        System.out.println();
    }
}
