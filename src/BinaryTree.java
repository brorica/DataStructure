import java.util.*;

public class BinaryTree {

    private static class Node {
        int data;
        Node left, right;
        Node(int val) {
            this.data = val;
        }

        public Node(int val, Node left, Node right) {
            this.data = val;
            this.left = left;
            this.right = right;
        }
    }

    private static Node root;

    public static void main(String[] args) {
        System.out.println("1 ~ 10까지 삽입");
        for (int i = 1; i <= 10; i++) {
            insert(i);
        }
        System.out.println("----------------------------------");

        System.out.printf("전위 순회(preorder) : ");
        preOrder(root);
        System.out.printf("\n중위 순회(inorder) : ");
        inorder(root);
        System.out.printf("\n후위 순회(postorder) : ");
        postorder(root);
        System.out.printf("\n레벨 순회(postorder) : ");
        levelOrder(root);

        print();
        delete(5);
        print();
    }

    private static void insert(int data) {
        Node newNode = new Node(data);
        if (root == null) {
            root = newNode;
            return;
        }
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node here = q.poll();
            if (here.left == null) {
                here.left = newNode;
                break;
            } else if (here.right == null) {
                here.right = newNode;
                return;
            }
            else {
                q.add(here.left);
                q.add(here.right);
            }
        }
    }

    private static void delete(int data) {
        System.out.printf("%d 삭제\n", data);
        Node deleteNode = null;     // 값을 지울 노드
        Node deepestNode = null;    // 가장 나중에 생성된 노드
        Node parentNode = null;     // 가장 나중에 생성된 노드의 부모 노드, 루트는 이 값이 null
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node here = q.poll();
            deepestNode = here;
            if (here.data == data) {
                deleteNode = here;
            }
            if (here.left != null) {
                parentNode = here;
                q.add(here.left);
            }
            if (here.right != null) {
                parentNode = here;
                q.add(here.right);
            }
        }

        if (deleteNode != null) {
            deleteNode.data = deepestNode.data;
            if (parentNode != null) {
                if(parentNode.left != null && parentNode.left.data == deepestNode.data) {
                    parentNode.left = null;
                } else if (parentNode.right != null && parentNode.right.data == deepestNode.data) {
                    parentNode.right = null;
                }
            }
            deepestNode = null;
        }
        System.out.println("----------------------------------");
    }

    private static void print() {
        System.out.println("출력");
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            int len = q.size();
            for (int i = 0; i < len; i++) {
                Node here = q.poll();
                System.out.printf("%d ", here.data);
                if (here.left != null) {
                    q.add(here.left);
                }
                if (here.right != null) {
                    q.add(here.right);
                }
            }
            System.out.println();
        }
        System.out.println("----------------------------------");
    }

    private static void preOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.printf("%d ", node.data);
        if (node.left != null) {
            preOrder(node.left);
        }
        if (node.right != null) {
            preOrder(node.right);
        }
    }

    private static void inorder(Node node) {
        if (node == null) {
            return;
        }
        if (node.left != null) {
            inorder(node.left);
        }
        System.out.printf("%d ", node.data);
        if (node.right != null) {
            inorder(node.right);
        }
    }

    private static void postorder(Node node) {
        if (node == null) {
            return;
        }
        if (node.left != null) {
            postorder(node.left);
        }
        if (node.right != null) {
            postorder(node.right);
        }
        System.out.printf("%d ", node.data);
    }

    private static void levelOrder(Node node) {
        if (node == null) {
            return;
        }
        Queue<Node> q = new LinkedList<>();
        q.add(node);
        while (!q.isEmpty()) {
            Node here = q.poll();
            System.out.printf("%d ", here.data);
            if (here.left != null) {
                q.add(here.left);
            }
            if (here.right != null) {
                q.add(here.right);
            }
        }
    }
}

