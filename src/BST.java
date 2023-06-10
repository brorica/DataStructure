import java.util.*;

public class BST {

    private static class Node {
        int data;
        Node left, right;

        public Node(int data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private static Node root = null;

    public static void main(String[] args) {
        TestDeleteNoChildNode();
        TestDeleteOneChildNode();
        TestDeleteTwoChildNode();
    }

    public static void insert(int data) {
        if (root == null) {
            root = new Node(data);
            return;
        }

        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node here = q.poll();
            // 자신보다 작거나 같으면 왼쪽
            if (here.data >= data) {
                if (here.left == null) {
                    here.left = new Node(data);
                    break;
                }
                q.add(here.left);
            }
            if (here.data < data) {
                if (here.right == null) {
                    here.right = new Node(data);
                    break;
                }
                q.add(here.right);
            }
        }
    }

    private static Node delete(Node node, int data) {
        if (node == null) {
            return null;
        }
        if (node.data > data) {
            node.left = delete(node.left, data);
            return node;
        }
        if (node.data < data) {
            node.right = delete(node.right, data);
            return node;
        }
        // 자식 노드가 없는 경우(리프 노드)
        if (node.left == null && node.right == null) {
            node = null;
            return null;
        }
        // 자식 노드를 2개를 가지고, 오른쪽 노드(Successor)를 위로 올릴 때
        else if (node.left != null && node.right != null) {
            Node successorNode = findSuccessor(node, node, node.right);
            node.data = successorNode.data;

            /* 왼쪽 노드(Predecessor)를 위로 올릴 때
            Node predecessorNode = findPredecessor(node, node, node.left);
            node.data = predecessorNode.data;
            */
            return node;
        }
        // 자식 노드를 1개만 가지는 경우
        else {
            if (node.left != null) {
                node = node.left;
            } else if (node.right != null) {
                node = node.right;
            }
            return node;
        }
    }
    // 레벨 순회
    public static void preOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.printf("%d ", node.data);
        preOrder(node.left);
        preOrder(node.right);
    }

    private static Node findPredecessor(Node root, Node prev, Node node) {
        if (node.right != null) {
            return findPredecessor(root, node, node.right);
        }
        if (root == prev) {
            root.left = null;
        } else if (root != prev) {
            prev.right = null;
        }
        return node;
    }

    private static Node findSuccessor(Node root, Node prev, Node node) {
        if (node.left != null) {
            return findSuccessor(root, node, node.left);
        }
        if (root == prev) {
            root.right = null;
        } else if (root != prev) {
            prev.left = null;
        }
        return node;
    }

    private static void TestDeleteNoChildNode() {
        root = null;
        insert(5);
        insert(3);
        insert(7);
        insert(2);
        insert(4);
        insert(6);
        insert(9);
        insert(1);
        insert(8);
        insert(10);
        System.out.printf("PreOrder: ");
        preOrder(root);
        System.out.printf("\ndata %d delete", 4);
        root = delete(root,4);
        System.out.printf("\nPreOrder: ");
        preOrder(root);
        System.out.printf("\n\n");
    }

    private static void TestDeleteOneChildNode() {
        root = null;
        insert(5);
        insert(3);
        insert(7);
        insert(2);
        insert(4);
        insert(6);
        insert(9);
        insert(1);
        insert(8);
        insert(10);
        System.out.printf("PreOrder: ");
        preOrder(root);
        System.out.printf("\ndata %d delete", 2);
        root = delete(root,2);
        System.out.printf("\nPreOrder: ");
        preOrder(root);
        System.out.printf("\n\n");
    }

    private static void TestDeleteTwoChildNode() {
        root = null;
        insert(5);
        insert(3);
        insert(7);
        insert(2);
        insert(4);
        insert(6);
        insert(9);
        insert(1);
        insert(8);
        insert(10);
        System.out.printf("PreOrder: ");
        preOrder(root);
        System.out.printf("\ndata %d delete", 7);
        root = delete(root,7);
        System.out.printf("\nPreOrder: ");
        preOrder(root);
        System.out.printf("\n\n");
    }
}




