import java.util.LinkedList;
import java.util.Queue;

public class RedBlackTree {

    private static class Node {

        int data;
        char color;
        Node parent, left, right;

        public Node(int data) {
            this.data = data;
            this.color = 'R';
            this.parent = null;
            this.left = null;
            this.right = null;
        }

        public void changeColor() {
            if (color == 'R') {
                color = 'B';
            }
            else if (color == 'B') {
                color = 'R';
            }
        }
    }
    // 색을 반전시키기 전에 회전 수행 여부를 저장함
    private static class RotateStatus {
        boolean LL;
        boolean LR;
        boolean RR;
        boolean RL;
    }

    private static RotateStatus status = new RotateStatus();

    public static void main(String[] args) {
        Node node = null;
        node = insert(node, 10);
        node = insert(node, 5);
        node = insert(node, 15);
        node = insert(node, 20);
        node = insert(node, 25);
        node = insert(node, 23);
        node = insert(node, 24);
        print(node);
    }

    private static Node insert(Node root, int data) {
        if (root == null) {
            Node newNode = new Node(data);
            newNode.changeColor();
            newNode.parent = newNode;
            return newNode;
        }
        return insertLeaf(root, data);
    }

    private static Node insertLeaf(Node root, int data) {
        if (root == null) {
            return new Node(data);
        }
        if (root.data > data) {
            root.left = insertLeaf(root.left, data);
            root.left.parent = root;
            checkParentAndBrotherRedNode(root);
        }
        if (root.data < data) {
            root.right = insertLeaf(root.right, data);
            root.right.parent = root;
            checkParentAndBrotherRedNode(root);
        }
        root = activeStatus(root);
        setRotation(root);
        return root;
    }

    private static Node activeStatus(Node root) {
        if (status.LL) {
            status.LL = false;
            root = rightRotate(root);
            root.color = 'B';
            root.right.color= 'R';
        }
        if (status.LR) {
            status.LR = false;
            root.left = leftRotate(root.left);
            root = rightRotate(root);
            root.color = 'B';
            root.right.color= 'R';
        }
        if (status.RR) {
            status.RR = false;
            root = leftRotate(root);
            root.color = 'B';
            root.left.color= 'R';
        }
        if (status.RL) {
            status.RL = false;
            root.right = rightRotate(root.right);
            root = leftRotate(root);
            root.color = 'B';
            root.left.color= 'R';
        }
        return root;
    }

    private static void setRotation(Node root) {
        if (root.parent == null) {
            return;
        }
        Node brotherNode = getBrotherNode(root);
        // 부모가 레드 노드이면서 삼촌 노드가 null 또는 블랙 노드라면 회전 수행
        if (root.color == 'R' && (brotherNode == null || brotherNode.color == 'B')) {
            if (root.parent.left == root) {
                if (root.left != null && root.left.color == 'R') {
                    status.LL = true;
                }
                if (root.right != null && root.right.color == 'R') {
                    status.LR = true;
                }
            }
            if (root.parent.right == root) {
                if (root.left != null && root.left.color == 'R') {
                    status.RL = true;
                }
                if (root.right != null && root.right.color == 'R') {
                    status.RR = true;
                }
            }
        }
    }

    private static Node getBrotherNode(Node node) {
        if (node.parent == null) {
            return null;
        }
        if (node.parent.left == node) {
            return node.parent.right;
        }
        if (node.parent.right == node) {
            return node.parent.left;
        }
        return null;
    }
    /**
     * 자신과 형제 노드가 레드일 때 발생. 자신와 형제 노드를 블랙으로 변경
     * 부모 노드는 레드로 변경. 단, 부모가 루트 노드인 경우 블랙으로 다시 바꿔야 함.
     */
    private static void checkParentAndBrotherRedNode(Node node) {
        Node parent = node.parent;
        if (parent == node) {
            return;
        }
        if (parent.left == null || parent.right == null) {
            return;
        }
        if (parent.left.color == 'R' && parent.right.color == 'R') {
            parent.left.color = 'B';
            parent.right.color = 'B';
            if (parent.parent != parent) {
                parent.color = 'R';
            }
        }
    }

    private static Node rightRotate(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        leftChild.right = node;
        node.parent = leftChild;
        return leftChild;
    }

    private static Node leftRotate(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        rightChild.left = node;
        node.parent = rightChild;
        return rightChild;
    }

    private static void print(Node root) {
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            int len = q.size();
            for (int i = 0; i < len; i++) {
                Node here = q.poll();
                System.out.printf("%d(%c)\t", here.data, here.color);
                if (here.left != null) {
                    q.add(here.left);
                }
                if (here.right != null) {
                    q.add(here.right);
                }
            }
            System.out.println();
        }
    }
}
