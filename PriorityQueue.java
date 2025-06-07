public class PriorityQueue<T extends Node> {
    private Node[] heap;
    private int size;

    // constructor
    public PriorityQueue(int capacity) {
        this.heap = new Node[capacity];
        this.size = 0;
    }

    // adds it to the priority queue and restores the heap property
    public void add(T element) {
        if (size >= heap.length) {
            throw new IllegalStateException("full");
        }
        heap[size] = element;
        heapifyUp(size);
        size++;
    }

    // removes and returns the element with the highest priority (the root of the heap)
    public T poll() {
        if (size == 0) {
            throw new IllegalStateException("empty");
        }
        T result = (T) heap[0];
        size--;
        heap[0] = heap[size];
        heapifyDown(0);
        return result;
    }

    // helper function to maintain the heap property by "bubbling up" an element
    private void heapifyUp(int index) {
        T element = (T) heap[index];
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            T parent = (T) heap[parentIndex];

            // Correct comparison for min-heap
            if (element.compareTo(parent) >= 0) {
                break;
            }

            heap[index] = parent;
            index = parentIndex;
        }
        heap[index] = element;
    }


    // helper func to maintain the heap property by "bubbling down" an element
    private void heapifyDown(int index) {
        T element = (T) heap[index];
        int half = size / 2;
        while (index < half) {
            int leftChildIndex = 2 * index + 1;
            int rightChildIndex = 2 * index + 2;

            int smallerChildIndex;
            if (rightChildIndex < size &&
                    ((T) heap[rightChildIndex]).compareTo((T) heap[leftChildIndex]) < 0) {
                smallerChildIndex = rightChildIndex;
            } else {
                smallerChildIndex = leftChildIndex;
            }

            T smallerChild = (T) heap[smallerChildIndex];

            if (element.compareTo(smallerChild) <= 0) {
                break;
            }

            // move the smaller child up the heap
            heap[index] = smallerChild;
            index = smallerChildIndex;
        }
        heap[index] = element;
    }


    // Check if the priority queue is empty
    public boolean isEmpty() {
        return size == 0;
    }
}