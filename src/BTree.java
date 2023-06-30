public class BTree {

    static class Node {
        Node[] child;
        int n; // key 배열이 가지고 있는 원소 개수
        Integer[] key;
        boolean leaf;

        public Node() {
            child = new Node[MAX_NODE_INDEX];
            key = new Integer[MAX_KEY_INDEX];
            n = 0;
            leaf = true;
        }
    }

    private static final int MAX_DEGREE = 3;
    private static final int MAX_NODE_INDEX = 2 * MAX_DEGREE;
    private static final int MAX_KEY_INDEX = 2 * MAX_DEGREE - 1;
    private static Node root = new Node();

    public static void main(String[] args) {

    }

    private static Node find(Node node, Integer value) {
        int index = 0;
        // 이분 탐색으로 찾는 방법이 있지만, 코드 간소화를 위해 순차 탐색을 이용
        while (node.n > index && node.key[index] < value) {
            index += 1;
        }
        // 값을 찾은 경우
        if (index < node.n && node.key[index].equals(value)) {
            return node;
        }
        // 여기까지 왔단 것은, 리프 노드에도 해당 value를 가지는 노드가 없단 뜻
        if (node.leaf) {
            return null;
        }
        // index < node.n 일때 node.key[index] < data < node.key[index + 1]
        // index >= node.n 일때 data >= node.key[index + 1]
        return find(node.child[index], value);
    }

    private static void insert(Integer value) {
        Node node = root;
        if (node.n == 2 * MAX_DEGREE - 1) {
            Node newRoot = new Node();
            newRoot.leaf = false;
            newRoot.child[0] = root;
            root = newRoot;
        }
        if (node.n < 2 * MAX_DEGREE - 1) {

        }
    }

    private static void split(Node node, int index) {
        Node rightNode = new Node();
        Node leftNode = node.child[0];
        // rightNode는 leftNode와 같은 레벨이기 때문에, leaf 값이 같다.
        rightNode.leaf = leftNode.leaf;
        // MAX_DEGREE - 3 위치에 있는 키 값은 위로 올라간다.
        int splitIndex = MAX_DEGREE - 2;
        rightNode.n = splitIndex;
        // key 배열 정리
        for (int i = 0; i < splitIndex; i++) {
            rightNode.key[i] = leftNode.key[splitIndex + i];
            leftNode.key[splitIndex + i] = 0;
            leftNode.n -= 1;
        }
        // child 배열 정리
        int splitChildIndex = MAX_DEGREE - 1;
        for (int i = 0; i < splitChildIndex; i++) {
            rightNode.child[i] = leftNode.child[i + splitChildIndex];
            leftNode.child[i + splitChildIndex] = null;
        }
        // 노즈의 자식 노드 마지막에 rightNode 추가
        node.child[index + 1] = rightNode;
    }
}
