package edu.sdccd.cisc191;

import java.util.LinkedList;

public class GenericMatchQueue<T> {

    private final LinkedList<T> items = new LinkedList<>();

    // Add item to the back of the queue
    public void enqueue(T item) {
        items.addLast(item);
    }

    // Remove and return the front item (FIFO order)
    public T dequeue() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return items.removeFirst();
    }

    // Return the front item without removing it
    public T peek() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return items.getFirst();
    }

    // True if there are no items in the queue
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }
}