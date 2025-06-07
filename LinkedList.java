public class LinkedList<Type> implements Iterable<Type> {
    private class ListNode {
        Type data;
        ListNode next;
        ListNode prev;

        public ListNode(Type data) {
            this.data = data;
        }
    } //list node class

    private ListNode head;
    private ListNode tail;
    private int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    } //constructor

    public void add(Type data) {
        ListNode newNode = new ListNode(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    } //adds the given item to the end.

    public int size() {
        return size;
    } //gets the size

    public boolean contains(Type data) {
        ListNode current = head;
        while (current != null) {
            if ((data == null && current.data == null) || (data != null && data.equals(current.data))) {
                return true; // Item found
            }
            current = current.next;
        }
        return false; // Item not found
    } // checks if the given item is in the list



    // iterator for the list
    public java.util.Iterator<Type> iterator() {
        return new ListIterator();
    }

    //inner class for the iterator so that foreach is applicable
    private class ListIterator implements java.util.Iterator<Type> {
        private ListNode nextNode = head;
        private ListNode lastReturned = null;

        public boolean hasNext() {
            return nextNode != null;
        }

        public Type next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException();

            lastReturned = nextNode;
            nextNode = nextNode.next;
            return lastReturned.data;
        }
    }
}