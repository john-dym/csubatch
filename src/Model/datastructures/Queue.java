package Model.datastructures;

import java.util.Iterator;

public class Queue implements Iterable<Object> {
    private Node head;
    private Node tail;
    private int size;

    public Queue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void enqueue(Object data) {
        Node newNode = new Node(data);
        if (tail != null) {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
        }
        tail = newNode;
        if (head == null) {
            head = newNode;
        }
        size++;
    }

    public Object dequeue() {
        if (size == 0) {
            return null;
        }
        Object data = head.getData();
        head = head.getNext();
        if (head != null) {
            head.setPrevious(null);
        } else {
            tail = null;
        }
        size--;
        return data;
    }

    public Object peek() {
        return (head != null) ? head.getData() : null;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Iterator<Object> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Object> {
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Object next() {
            Object data = current.getData();
            current = current.getNext();
            return data;
        }
    }
}