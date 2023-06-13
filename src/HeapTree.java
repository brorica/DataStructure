public class HeapTree {

    private static Heap heap = new Heap(256);

    public static void main(String[] args) {
        heap.insert(5);
        heap.insert(1);
        heap.insert(2);
        heap.insert(4);
        heap.insert(3);
        heap.insert(6);
        heap.insert(10);
        heap.insert(9);
        heap.insert(8);
        heap.insert(7);
        // descend와 priorityPrint는 따로 실행해야 한다.
        heap.descend();
//        heap.priorityPrint();
    }
}

class Heap {
    private int[] heapArray;
    private int tail = 0;
    private int capacity;

    public Heap(int capacity) {
        this.heapArray = new int[capacity];
        this.capacity = capacity;
    }

    public void insert(int data) {
        // 한계에 도달하면 삽입 취소
        if (tail >= capacity) {
            return;
        }
        heapArray[tail] = data;
        int index = tail;
        tail += 1;
        upRecursive(getParentIndex(index), index);
    }

    private void upRecursive(int parent, int child) {
        // 자식이 루트 인덱스이거나, 자식이 부모보다 크면 return
        if (child <= 0 || heapArray[child] >= heapArray[parent]) {
            return;
        }
        swap(child, parent);
        upRecursive(getParentIndex(parent), parent);
    }

    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }

    private int getLeftChildIndex(int index) {
        return index * 2 + 1;
    }

    private int getRightChildIndex(int index) {
        return index * 2 + 2;
    }

    private void swap(int childIndex, int parentIndex) {
        int temp = heapArray[parentIndex];
        heapArray[parentIndex] = heapArray[childIndex];
        heapArray[childIndex] = temp;
    }

    public int getMin() {
        return heapArray[0];
    }

    public int extractMin() {
        int ret = getMin();
        heapArray[0] = heapArray[tail - 1];
        heapArray[tail - 1] = 0;
        tail -= 1;
        downRecursive(0);
        return ret;
    }

    private void downRecursive(int parent) {
        int child = parent;
        int leftIndex = getLeftChildIndex(parent);
        if (leftIndex < tail && heapArray[parent] > heapArray[leftIndex]) {
            child = leftIndex;
        }
        int rightIndex = getRightChildIndex(parent);
        if (rightIndex < tail && heapArray[child] > heapArray[rightIndex]) {
            child = rightIndex;
        }
        if (child != parent) {
            swap(parent, child);
            downRecursive(child);
        }
    }

    public void updateKey(int key, int newValue) {
        if (key >= tail) {
            return;
        }
        heapArray[key] = newValue;
        upRecursive(getParentIndex(key), key);
    }

    public void descend() {
        System.out.print("내림차순 정렬: ");
        for (int i = tail - 1; i >= 0; i--) {
            int temp = heapArray[0];
            heapArray[0] = heapArray[i];
            heapArray[i] = temp;

            descendRecursive(0, i);
        }
        // 출력
        for (int i = 0; i < tail; i++) {
            System.out.printf("%d ", heapArray[i]);
        }
        System.out.println();
    }

    private void descendRecursive(int parent, int limit) {
        int child = parent;
        int leftIndex = getLeftChildIndex(parent);
        if (leftIndex < limit && heapArray[parent] > heapArray[leftIndex]) {
            child = leftIndex;
        }
        int rightIndex = getRightChildIndex(parent);
        if (rightIndex < limit && heapArray[child] > heapArray[rightIndex]) {
            child = rightIndex;
        }
        if (child != parent) {
            swap(parent, child);
            descendRecursive(child, limit);
        }
    }

    public void priorityPrint() {
        System.out.print("오름차순 우선순위 큐: ");
        int N = tail;
        for (int i = 0; i < N; i++) {
            System.out.printf("%d ", extractMin());
        }
        System.out.println();
    }
}