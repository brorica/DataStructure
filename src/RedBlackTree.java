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
            if (color == 'B') {
                color = 'R';
            }
        }
    }

    public static void main(String[] args) {
        Node node = null;
        node = insert(node, 1);
    }

    private static Node insert(Node root, int data) {
        if (root == null) {
            Node newNode = new Node(data);
            newNode.changeColor();
            return newNode;
        }
        return insertLeaf(root, data);
    }

    /**
     * todo: 회전에 대한 부분 메소드로 빼기
     * todo: 재귀적으로 색을 조정하되, 루트 노드의 색은 검은색으로 보장하기
     */
    private static Node insertLeaf(Node root, int data) {
        if (root == null) {
            return new Node(data);
        }
        Node parent = root.parent;
        if (root.data > data) {
            root.left = insert(root.left, data);
            root.left.parent = root;
            // 부모 노드가 검은색이면 ㅇㅋ
            if (root.color == 'B') {
                // nothing
            }
            // 혼란하다 혼란해
            if (root.color == 'R') {
                // 부모가 좌측 노드면, 형제 노드는 우측 노드
                Node brother = getBrotherNode(root);
                if (brother == null || brother.color == 'B') {
                    // 부모 노드를 중심으로 right-rotation (LL)
                    // 부모 노드의 색을 블랙으로, 조부모 노드의 색을 레드로.
                }
                else if (brother.color == 'R') {
                    // 부모와 삼촌을 블랙 노드로 변경
                    // 조부모 노드를 레드로 변경
                }
            }
        }
        if (root.data < data) {
            root.right = insert(root.right, data);
            root.right.parent = root;
            // 부모 노드가 검은색이면 ㅇㅋ
            if (root.color == 'B') {
                // nothing
            }
            // 혼란하다 혼란해
            if (root.color == 'R') {
                Node brother = getBrotherNode(root);
                // 부모가 좌측 노드면, 형제 노드는 우측 노드
                if (brother == null || brother.color == 'B') {
                    // 부모 노드를 중심으로 left rotation (LR)
                    // 부모 노드의 색을 블랙으로, 조부모 노드의 색을 레드로.
                }
                else if (brother.color == 'R') {
                    // 부모와 삼촌을 블랙 노드로 변경
                    // 조부모 노드를 레드로 변경
                }
            }
        }
        return null;
    }

    private static Node getBrotherNode(Node node) {
        Node parent = node.parent;
        if (parent == null) {
            return null;
        }
        if (parent.left == node) {
            return parent.right;
        }
        if (parent.right == node) {
            return parent.left;
        }
        return null;
    }

}
