public class BTree {

    static class Node {
        Node[] child;
        int n; // key 배열이 가지고 있는 원소 개수
        Integer[] key;
        boolean leaf;

        public Node() {
            child = new Node[MAX_DEGREE + 1];
            key = new Integer[MAX_DEGREE];
            n = 0;
            leaf = true;
        }
    }

    private static final int DEGREE = 2;
    private static final int MAX_DEGREE = 2 * DEGREE;
    private static Node root = new Node();

    public static void main(String[] args) {
        insert(18);
        insert(4);
        insert(9);
        insert(13);
        insert(1);
        insert(11);
        insert(5);
        insert(20);
        insert(7);
        insert(16);
        insert(10);
        insert(2);
        insert(8);
        insert(19);
        insert(6);
        insert(3);
        insert(15);
        insert(12);
        insert(17);
        insert(14);
        print(root);
    }

    private static void insert(Integer value) {
        Node node = root;
        addKey(node, value);
        if (node.n == MAX_DEGREE) {
            Node newRoot = new Node();
            newRoot.leaf = false;
            newRoot.child[0] = root;
            root = newRoot;
            split(newRoot, 0, value);
        }
    }

    // split 기준은 2가지로 나뉜다.
    // 노드를 추가한 후 split
    // split 수행 후 노드 추가
    // 여기선 노드를 추가한 후 split 수행
    private static void split(Node node, int index, Integer value) {
        Node rightNode = new Node();
        Node leftNode = node.child[index];
        if (node.key[index] != null) {
            int temp = node.n;
            for (int i = node.n - 1; i >= 0 && node.key[i] > value; i--) {
                node.key[i + 1] = node.key[i];
                node.child[i + 2] = node.child[i + 1];
                temp -= 1;
            }
            leftNode = node.child[temp];
        }
        // rightNode는 leftNode와 같은 레벨이기 때문에, leaf 값이 같다.
        rightNode.leaf = leftNode.leaf;
        // 오른쪽 노드를 오른쪽부터 DEGREE 수 만큼 분리된다.
        rightNode.n = DEGREE;
        // key 배열 정리
        for (int i = 0; i < DEGREE; i++) {
            rightNode.key[i] = leftNode.key[i + DEGREE];
            leftNode.key[i + DEGREE] = null;
            leftNode.n -= 1;
        }
        // child 배열 정리, leaf 노드면 수행하는 의미가 없다.
        // leaf 유무에 대해 분기를 타야하지만 단순화를 위해 적용하진 않음
        // 자식 노드는 키보다 1 더 많기 때문에 i <= DEGREE 했다.
        for (int i = 0; i <= DEGREE; i++) {
            rightNode.child[i] = leftNode.child[i + DEGREE];
            leftNode.child[i + DEGREE] = null;
        }
        // leftNode의 마지막 키를 위로 올림
        node.key[index] = leftNode.key[DEGREE - 1];
        node.n += 1;
        leftNode.key[DEGREE - 1] = null;
        leftNode.n -= 1;
        // 노드의 자식 노드 마지막에 rightNode 추가
        node.child[index + 1] = rightNode;

    }

    private static void addKey(Node node, Integer value) {
        int index;
        if (node.leaf) {
            index = node.n;
            for (int i = node.n - 1; i >= 0 && node.key[i] > value; i--) {
                node.key[i + 1] = node.key[i];
                index -= 1;
            }
            node.key[index] = value;
            node.n += 1;
        }
        else {
            index = 0;
            while (node.n > index && node.key[index] < value) {
                index += 1;
            }
            Node there = node.child[index];
            addKey(there, value);
            if (there.n == MAX_DEGREE) {
                split(node, index, value);
            }
        }
    }

    private static void print(Node node) {
        int N = node.n;
        for (int i = 0; i < N; i++) {
            System.out.print(node.key[i] + " ");
        }
        if (!node.leaf) {
            for (int i = 0; i < N + 1; i++) {
                print(node.child[i]);
            }
        }
    }

}
