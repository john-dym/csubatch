package Model.datastructures;

public class Node {
    private Node next;
    private Node previous;
    private Object data;

    public Node(Object data) {
        this.data = data;
        this.next = null;
        this.previous = null;
    }

    public Node getNext() {
        return this.next;
    }

    public Node getPrevious() {
        return this.previous;
    }

    public Object getData() {
        return this.data;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
