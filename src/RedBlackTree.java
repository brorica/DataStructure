import java.util.LinkedList;
import java.util.Queue;

public class RedBlackTree {

    private static class Node {

        Integer data;
        char color;
        Node parent, left, right;

        public Node(Integer data) {
            this.data = data;
            this.color = 'R';
            this.parent = null;
            this.left = null;
            this.right = null;
        }

        public Node(Integer data, char color) {
            this.data = data;
            this.color = color;
            this.parent = null;
            this.left = null;
            this.right = null;
        }
    }

    private static final Node nilNode = new Node(null, 'B');
    private static Node root = nilNode;

    public static void main(String[] args) {
        insert(7);
        insert(3);
        insert(18);
        insert(10);
        insert(22);
        insert(8);
        insert(11);
        insert(26);
        insert(2);
        insert(6);
        print();
        System.out.println();
//        delete(18);
//        delete(11);
//        delete(3);
//        delete(10);
//        node = delete(node, 22);
        print();
    }

    private static void insert(int data) {
        insertLeaf(data);
    }

    private static void insertLeaf(int data) {
        Node newNode = new Node(data);
        newNode.left = nilNode;
        newNode.right = nilNode;
        // 새로운 노드를 삽입할 위치를 찾음
        Node x = root;
        Node y = null;
        while (x != nilNode) {
            y = x;
            if (x.data <= data) {
                x = x.right;
            } else {
                x = x.left;
            }
        }
        // 새로운 노드가 삽입될 부모 노드를 찾음
        newNode.parent = y;
        if (y == null) {
            root = newNode;
            root.color = 'B';
            return;
        } else if (newNode.data <= y.data) {
            y.left = newNode;
        } else {
            y.right = newNode;
        }
        // 삽입 과정의 조정은 조부모 노드까지 있어야 하기 때문에 조부모 노드가 null이면 return
        if (newNode.parent.parent == null) {
            return;
        }
        insertBalance(newNode);
    }

    private static void insertBalance(Node node) {
        Node siblingNode;
        while (node.parent.color == 'R') {
            Node parent = node.parent;          // 부모 노드
            Node grandParent = parent.parent;   // 조부모 노드
            if (parent == grandParent.right) {
                siblingNode = grandParent.left;
                if (siblingNode.color == 'R') {
                    siblingNode.color = 'B';
                    parent.color = 'B';
                    grandParent.color = 'R';
                    node = grandParent;
                } else {
                    if (node == parent.left) {
                        node = node.parent;
                        rightRotate(node);
                    }
                    parent.color = 'B';
                    grandParent.color = 'R';
                    leftRotate(grandParent);
                }
            } else {
                siblingNode = grandParent.right;
                if (siblingNode.color == 'R') {
                    siblingNode.color = 'B';
                    parent.color = 'B';
                    grandParent.color = 'R';
                    node = grandParent;
                } else {
                    if (node == parent.right) {
                        node = parent;
                        leftRotate(node);
                    }
                    parent.color = 'B';
                    grandParent.color = 'R';
                    rightRotate(grandParent);
                }
            }
            if (node == root) {
                break;
            }
        }
        root.color = 'B';
    }

    private static void rightRotate(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != nilNode) {
            leftChild.right.parent = node;
        }
        leftChild.parent = node.parent;
        if (node.parent == nilNode) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }
        leftChild.right = node;
        node.parent = leftChild;
    }

    private static void leftRotate(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != nilNode) {
            rightChild.left.parent = node;
        }
        rightChild.parent = node.parent;
        if (node.parent == nilNode) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }
        rightChild.left = node;
        node.parent = rightChild;
    }

    private static void print() {
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            int len = q.size();
            for (int i = 0; i < len; i++) {
                Node here = q.poll();
                System.out.printf("%d(%c)\t", here.data, here.color);
                if (here.left != nilNode) {
                    q.add(here.left);
                }
                if (here.right != nilNode) {
                    q.add(here.right);
                }
            }
            System.out.println();
        }
    }
}
