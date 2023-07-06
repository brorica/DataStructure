public class BTree {

    static class Node {
        Node[] child;
        int n; // key 배열이 가지고 있는 원소 개수
        int[] key;
        boolean leaf;

        public Node() {
            child = new Node[MAX_DEGREE + 1];
            key = new int[MAX_DEGREE];
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

    private static void insert(int key) {
        Node node = root;
        addKey(node, key);
        if (node.n == MAX_DEGREE) {
            Node newRoot = new Node();
            newRoot.leaf = false;
            newRoot.child[0] = root;
            root = newRoot;
            split(newRoot, 0, key);
        }
    }

    // split 기준은 2가지로 나뉜다.
    // 노드를 추가한 후 split
    // split 수행 후 노드 추가
    // 여기선 노드를 추가한 후 split 수행
    private static void split(Node node, int index, int key) {
        Node rightNode = new Node();
        Node leftNode = node.child[index];
        if (node.key[index] != 0) {
            int temp = node.n;
            for (int i = node.n - 1; i >= 0 && node.key[i] > key; i--) {
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
            leftNode.key[i + DEGREE] = 0;
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
        leftNode.key[DEGREE - 1] = 0;
        leftNode.n -= 1;
        // 노드의 자식 노드 마지막에 rightNode 추가
        node.child[index + 1] = rightNode;

    }

    private static void addKey(Node node, int key) {
        int index;
        if (node.leaf) {
            index = node.n;
            // 키의 순서를 오름차순으로 유지
            for (int i = node.n - 1; i >= 0 && node.key[i] > key; i--) {
                node.key[i + 1] = node.key[i];
                index -= 1;
            }
            node.key[index] = key;
            node.n += 1;
        }
        else {
            index = 0;
            while (node.n > index && node.key[index] < key) {
                index += 1;
            }
            Node there = node.child[index];
            addKey(there, key);
            if (there.n == MAX_DEGREE) {
                split(node, index, key);
            }
        }
    }

    private static void delete(Node node, int key) {
        Node delNode = find(root, key);
        if (delNode == null) {
            return;
        }
        int index;
        for (index = 0; index < delNode.n; index++) {
            if (delNode.key[index] == key) {
                break;
            }
        }
        // 삭제할 노드가 현 노드 안에 있는 경우
        if (index < node.n && node.key[index] == key) {
            // 리프 노드면 바로 삭제
            if (node.leaf) {
                for (index = 0; index < node.n && node.key[index] != key; index++) {
                    continue;
                }
                for (int i = index; i < node.n; i++) {
                    if (i < MAX_DEGREE - 1) {
                        node.key[i] = node.key[i + 1];
                    }
                }
                node.n -= 1;
                return;
            }
            else {
                // 왼쪽 자식에서 빌려옴
                Node leftChildNode = node.child[index];
                if (leftChildNode.n >= DEGREE) {
                    Node predecessor = getPredecessor(leftChildNode);
                    int predecessorKey = predecessor.key[predecessor.n - 1];
                    delete(predecessor, predecessorKey);
                    node.key[index] = predecessorKey;
                    return;
                }
                // 왼쪽이 안 되면 오른쪽 자식에서 빌려옴
                Node rightChildNode = node.child[index + 1];
                if (rightChildNode.n >= DEGREE) {
                    Node successor = getSuccessor(rightChildNode);
                    int successorKey = successor.key[0];
                    delete(successor, successorKey);
                    node.key[index] = successorKey;
                    return;
                }
                // 양옆에서 빌려오는게 안 되는 경우
                // 기존 왼쪽 자식 노드의 n 기억
                int tempLeftChildNodeN = leftChildNode.n;
                // 지우려는 키를 임시로 왼쪽 자식 노드에 붙임
                leftChildNode.key[leftChildNode.n] = node.key[index];
                leftChildNode.n += 1;
                // 오른쪽 자식 노드를 왼쪽 자식 노드에 붙임
                for (int i = 0, j = leftChildNode.n; i < rightChildNode.n; i++) {
                    leftChildNode.key[i + j] = rightChildNode.key[i];
                    leftChildNode.child[i + tempLeftChildNodeN] = rightChildNode.child[i];
                    leftChildNode.n += 1;
                }
                leftChildNode.child[leftChildNode.n] = rightChildNode.child[rightChildNode.n];
                // 부모 노드의 키 정리
                for (int i = index; i < node.n; i++) {
                    if (i + 1 < MAX_DEGREE) {
                        node.key[i] = node.key[i + 1];
                    }
                }
                // 부모 노드의 자식 노드 정리
                node.child[index] = leftChildNode;
                for (int i = index + 1; i <= node.n; i++) {
                    if (i + 1 < MAX_DEGREE + 1) {
                        node.child[i] = node.child[i + 1];
                    }
                }
                node.n -= 1;
                // 부모 노드가 0이 됐을 때
                if (node.n == 0) {
                    node = node.child[0];
                }
                // 왼쪽 자식노드로 이동한 키를 완전히 지움
                delete(leftChildNode, key);
            }
        }
    }

    private static Node find(Node node, int key) {
        int index = 0;
        for (index = 0; index < node.n; index++) {
            if (node.key[index] > key) {
                break;
            }
            if (node.key[index] == key) {
                return node;
            }
        }
        if (node.leaf) {
            return null;
        }
        return find(node.child[index], key);
    }

    private static Node getPredecessor(Node node) {
        if (node.leaf) {
            return node;
        }
        return getPredecessor(node.child[node.n]);
    }

    private static Node getSuccessor(Node node) {
        if (node.leaf) {
            return node;
        }
        return getPredecessor(node.child[0]);
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
