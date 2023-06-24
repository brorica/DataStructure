public class AVLTree {

    private static class Node {

        int key, height;
        Node left, right;

        public Node(int key) {
            this.key = key;
            this.height = 1;
            this.left = null;
            this.right = null;
        }
    }

    private static Node root;

    public static void main(String[] args) {
        testLeftLeftRotation();
        testLeftRightRotation();
        testRightRightRotation();
        testRightLeftRotation();
    }

    private static Node insert(Node node, int key) {
        if (node == null) {
            return new Node(key);
        }
        if (node.key > key) {
            node.left = insert(node.left, key);
        }
        if (node.key < key) {
            node.right = insert(node.right, key);
        }
        // 트리의 불균형을 확인하는 단계
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        int balanceFactor = calcBalanceFactor(node);
        // 왼쪽 서브트리 높이가 더 큰 경우 왼쪽 서브 노드를 위로 올려야 한다.
        if (balanceFactor > 1) {
            // Left-Left
            if (node.left.key > key) {
                return rightRotate(node);
            }
            // Left-Right
            else if (node.left.key < key) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }
        // 오른쪽 서브트리 높이가 더 큰 경우 오른쪽 서브 노드를 위로 올려야 한다.
        else if (balanceFactor < -1) {
            // Right-Left,
            if (node.right.key > key) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
            // Right-Right
            else if (node.right.key < key) {
                return leftRotate(node);
            }
        }
        return node;
    }

    private static int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private static int calcBalanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    private static Node rightRotate(Node root) {
        Node leftChild = root.left;
        root.left = leftChild.right;
        leftChild.right = root;
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        leftChild.height = 1 + Math.max(getHeight(leftChild.left), getHeight(leftChild.right));
        return leftChild;
    }

    private static Node leftRotate(Node root) {
        Node rightChild = root.right;
        root.right = rightChild.left;
        rightChild.left = root;
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        rightChild.height = 1 + Math.max(getHeight(rightChild.left), getHeight(rightChild.right));
        return rightChild;
    }

    private static Node delete(Node node, int key) {
        if (node == null) {
            return null;
        }
        // 탐색
        if (node.key > key) {
            node.left = delete(node.left, key);
        } else if (node.key < key) {
            node.right = delete(node.right, key);
        } else {
            // 삭제 과정
            // 자식 노드가 없는 경우(리프 노드)
            if (node.left == null && node.right == null) {
                node = null;
                return null;
            }
            // 자식 노드가 2개일 때
            else if (node.left != null && node.right != null) {
                Node predecessor = findPredecessor(node.left);
                node.key = predecessor.key;
                node.left = delete(node.left, predecessor.key);
            }
            // 자식 노드가 1개일 때
            else {
                if (node.left != null) {
                    node = node.left;
                }
                if (node.right != null) {
                    node = node.right;
                }
                return node;
            }
            // 높이 갱신
            if (node == null) {
                return null;
            }
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
            int balanceFactor = calcBalanceFactor(node);
            // 왼쪽 서브트리 높이가 더 큰 경우 왼쪽 서브 노드를 위로 올려야 한다.
            if (balanceFactor > 1) {
                // Left-Left
                if (node.left.key > key) {
                    return rightRotate(node);
                }
                // Left-Right
                else if (node.left.key < key) {
                    node.left = leftRotate(node.left);
                    return rightRotate(node);
                }
            }
            // 오른쪽 서브트리 높이가 더 큰 경우 오른쪽 서브 노드를 위로 올려야 한다.
            else if (balanceFactor < -1) {
                // Right-Left,
                if (node.right.key > key) {
                    node.right = rightRotate(node.right);
                    return leftRotate(node);
                }
                // Right-Right
                else if (node.right.key < key) {
                    return leftRotate(node);
                }
            }
        }
        return node;
    }

    private static Node findPredecessor(Node node) {
        if (node.right != null) {
            return findPredecessor(node.right);
        }
        return node;
    }

    private static void preOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.printf("%d ", node.key);
        preOrder(node.left);
        preOrder(node.right);
    }


    private static void testLeftLeftRotation() {
        System.out.println("-----LeftLeftRotation-------");
        root = null;
        root = insert(root, 4);
        root = insert(root, 3);
        root = insert(root, 5);
        root = insert(root, 2);
        print(root, "", true);
        root = insert(root, 1);
        print(root, "", true);
    }

    private static void testLeftRightRotation() {
        System.out.println("\n-----LeftRightRotation------");
        root = null;
        root = insert(root, 5);
        root = insert(root, 2);
        root = insert(root, 6);
        root = insert(root, 1);
        root = insert(root, 3);
        print(root, "", true);
        root = insert(root, 4);
        System.out.println();
        print(root, "", true);
    }

    private static void testRightRightRotation() {
        System.out.println("\n----RightRightRotation------");
        root = null;
        root = insert(root, 3);
        root = insert(root, 2);
        root = insert(root, 5);
        root = insert(root, 4);
        root = insert(root, 6);
        print(root, "", true);
        root = insert(root, 7);
        System.out.println();
        print(root, "", true);
    }

    private static void testRightLeftRotation() {
        System.out.println("\n-----RightLeftRotation------");
        root = null;
        root = insert(root, 2);
        root = insert(root, 1);
        root = insert(root, 5);
        root = insert(root, 4);
        root = insert(root, 6);
        print(root, "", true);
        root = insert(root, 3);
        System.out.println();
        print(root, "", true);
    }

    private static void print(Node root, String indent, boolean last) {
        if (root != null) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "   ";
            } else {
                System.out.print("L----");
                indent += "|  ";
            }
            System.out.println(root.key);
            print(root.left, indent, false);
            print(root.right, indent, true);
        }
    }
}
