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
        node = insert(node, 7);
        node = insert(node, 3);
        node = insert(node, 18);
        node = insert(node, 10);
        node = insert(node, 22);
        node = insert(node, 8);
        node = insert(node, 11);
        node = insert(node, 26);
        node = insert(node, 2);
        node = insert(node, 6);
        print(node);
        System.out.println();
        node = delete(node, 18);
        node = delete(node, 11);
//        node = delete(node, 3);
//        node = delete(node, 10);
//        node = delete(node, 22);
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
            return node;
        }
        if (node.parent.left == node) {
            return node.parent.right;
        }
        if (node.parent.right == node) {
            return node.parent.left;
        }
        return node;
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

    private static Node delete(Node node, int data) {
        if (node == null) {
            return node;
        }
        if (node.data > data) {
            node.left = delete(node.left, data);
        }
        if (node.data < data) {
            node.right = delete(node.right, data);
        }
        if (node.data == data) {
            if (node.color == 'R') {
                // 자식 노드 없음
                if (node.left == null && node.right == null) {
                    node.parent = null;
                    return null;
                }
                // 자식이 둘
                if (node.left != null && node.right != null) {
                    Node successorNode = findSuccessor(node.left);
                    // 후임 노드의 부모 노드 포인터 정리
                    if (successorNode.parent.left == successorNode) {
                        successorNode.parent.left = successorNode.left;
                    }
                    if (successorNode.parent.right == successorNode) {
                        successorNode.parent.right = null;
                    }
                    successorNode.parent = null;
                    node.data = successorNode.data;
                    // 삭제할 노드와 후임 노드 둘 다 레드 노드면 레드 노드
                    if (successorNode.color == 'R') {
                        node.color = 'R';
                    }
                    // 삭제할 노드가 레드고 후임 노드가 블랙이면 블랙 노드
                    if (successorNode.color == 'B') {
                        node.color = 'B';
                    }
                    return node;
                }
                // 자식이 하나
                if (node.left != null) {
                    node.left.parent = node.parent;
                    node = node.left;
                }
                else if (node.right != null) {
                    node.right.parent = node.parent;
                    node = node.right;
                }
                return node;
            }
            if (node.color == 'B') {
                Node successorNode = findSuccessor(node.left);
                // 후임 노드가 레드라면 값만 변경하고 종료
                if (successorNode.color == 'R') {
                    node.data = successorNode.data;
                    // 전임 노드의 부모 노드 포인터 정리
                    if (successorNode.parent.left == successorNode) {
                        successorNode.parent.left = null;
                    }
                    if (successorNode.parent.right == successorNode) {
                        successorNode.parent.right = null;
                    }
                    return node;
                }
                // 후임 노드가 블랙 노드라면 값을 교체하고, 삭제할 노드를 이중 블랙 노드로 변경
                if (successorNode.color == 'B') {
                    node.data = successorNode.data;
                    // 루트 노드를 삭제하는 거면 이중 블랙 노드로 하지 않음
                    if (node.parent == node) {
                        node.color = 'B';
                    }
                    else {
                        node.color = 'D';
                    }
                    if (successorNode.parent.left == successorNode) {
                        successorNode.parent.left = null;
                    }
                    if (successorNode.parent.right == successorNode) {
                        successorNode.parent.right = null;
                    }
                }
                // 삭제할 루트가 아니면서 이중 블랙 노드라면 형제 노드를 봐야 함
                Node siblingNode = getBrotherNode(node);
                Node siblingChildNode = getSiblingChildNode(siblingNode);
                // 형제 노드가 블랙 노드면서 형제 노드의 자식 중 하나가 레드 노드라면
                if (siblingNode.color == 'B' && siblingChildNode.color == 'R') {
                    Node parent = siblingChildNode.parent;
                    if(parent.left == siblingNode && siblingNode.left == siblingChildNode) {
                        // LL
                        siblingNode.left.color = parent.color;
                        parent = rightRotate(siblingNode.parent);
                        if (parent.right.data == null) {
                            parent.right = null;
                        }
                    }
                    if(parent.left == siblingNode && siblingNode.right == siblingChildNode) {
                        // LR
                        siblingNode.left.color = parent.color;
                        parent.left = leftRotate(siblingNode);
                        parent = rightRotate(parent);
                        if (parent.right.data == null) {
                            parent.right = null;
                        }
                    }
                    if(parent.right == siblingNode && siblingNode.right == siblingChildNode) {
                        // RR
                        siblingNode.right.color = parent.color;
                        parent = leftRotate((siblingNode.parent));
                        if (parent.left.data == null) {
                            parent.left = null;
                        }
                    }
                    if(parent.right == siblingNode && siblingNode.left == siblingChildNode) {
                        // RL
                        siblingNode.left.color = parent.color;
                        parent.right = rightRotate(siblingNode);
                        parent = leftRotate(parent);
                        if (parent.left.data == null) {
                            parent.left = null;
                        }
                    }
                    return parent;
                }
                if (siblingNode.color == 'R') {
                    Node parent = siblingChildNode.parent;
                    if (parent.left == siblingNode) {
                        parent = rightRotate(parent);
                        parent.color = 'B';
                        parent.left.color = 'R';
                        if (parent.right.color == 'D') {
                            parent.right = null;
                        }
                    }
                    else if (parent.right == siblingChildNode) {
                        parent = leftRotate(parent);
                        parent.color = 'B';
                        parent.right.color = 'R';
                        if (parent.left.color == 'D') {
                            parent.left = null;
                        }
                    }
                    return parent;
                }
            }
        }
        return node;
    }

    private static Node findSuccessor(Node node) {
        if (node == null) {
            Node nullNode = new Node(null, 'D');
            return nullNode;
        }
        if (node.right == null) {
            return node;
        }
        return findSuccessor(node.right);
    }
    /**
     * 형제 노드의 자식 노드를 가져옴
     * 자식 노드가 없는 경우 null leaf(블랙 노드) 반환
     * 레드 노드인 자식이 있다면 해당 노드 반환.
     * 만약, 두 자식이 레드 노드라면 우측 노드 우선 반환
     */
    private static Node getSiblingChildNode(Node siblingNode) {
        Node node = new Node(null, 'B');
        if (siblingNode.left != null && siblingNode.left.color == 'R') {
            node = siblingNode.left;
        }
        if (siblingNode.right != null && siblingNode.right.color == 'R') {
            node = siblingNode.right;
        }
        return node;
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
