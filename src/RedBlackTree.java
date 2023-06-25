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
        insert(5);
        insert(3);
        insert(9);
        insert(2);
        insert(6);
        insert(11);
        insert(4);
        insert(1);
        insert(10);
        insert(8);
        insert(7);
        print(root, "", true);
        delete(3);
        print(root, "", true);
    }

    private static void insert(Integer data) {
        Node insertPosition = findInsertPosition(data);
        Node newNode = insertLeaf(insertPosition, data);
        // 삽입 과정의 조정은 조부모 노드까지 있어야 하기 때문에 조부모 노드가 nilNode이면 return
        if (newNode.parent.parent != nilNode) {
            insertBalance(newNode);
        }
    }

    private static Node findInsertPosition(Integer data) {
        Node node = root;
        Node destination = nilNode;
        while (node != nilNode) {
            destination = node;
            if (node.data < data) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return destination;
    }

    private static Node insertLeaf(Node insertPosition, Integer data) {
        Node newNode = new Node(data);
        newNode.left = nilNode;
        newNode.right = nilNode;
        newNode.parent = nilNode;

        newNode.parent = insertPosition;
        if (insertPosition == nilNode) {
            root = newNode;
            root.color = 'B';
        } else if (newNode.data <= insertPosition.data) {
            insertPosition.left = newNode;
        } else {
            insertPosition.right = newNode;
        }
        return newNode;
    }

    private static void insertBalance(Node node) {
        Node uncleNode;
        while (node.parent.color == 'R') {
            // 삽입된 노드의 부모가 조부모 노드의 오른쪽
            if (node.parent == node.parent.parent.right) {
                uncleNode = node.parent.parent.left;
                // Case 1: 삼촌 노드가 레드인 경우
                if (uncleNode.color == 'R') {
                    uncleNode.color = 'B';
                    node.parent.color = 'B';
                    node.parent.parent.color = 'R';
                    node = node.parent.parent;
                }
                // Case 2: 삼촌 노드가 블랙인 경우
                else {
                    // 삽입된 노드가 부모 노드의 왼쪽
                    // 즉, 트리가 / 또는 \ 형태가 돼야 하는데 엇갈린 경우 일자로 맞춰야 함
                    if (node == node.parent.left) {
                        // 부모를 자식간의 관계를 바꿔야 한다.
                        node = node.parent;
                        rightRotate(node);
                    }
                    node.parent.color = 'B';
                    node.parent.parent.color = 'R';
                    leftRotate(node.parent.parent);
                }
            }
            // 삽입된 노드의 부모가 조부모 노드의 왼쪽
            else {
                uncleNode = node.parent.parent.right;
                // Case 1: 삼촌 노드가 레드인 경우
                if (uncleNode.color == 'R') {
                    uncleNode.color = 'B';
                    node.parent.color = 'B';
                    node.parent.parent.color = 'R';
                    node = node.parent.parent;
                }
                // Case 2: 삼촌 노드가 블랙인 경우
                else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        leftRotate(node);
                    }
                    node.parent.color = 'B';
                    node.parent.parent.color = 'R';
                    rightRotate(node.parent.parent);
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

    private static void delete(Integer data) {
        Node delNode = findDelNode(data);
        findSwapNode(delNode);
    }

    private static Node findDelNode(Integer data) {
        Node node = root;
        while (node != nilNode) {
            if (node.data == data) {
                return node;
            }
            if (node.data <= data) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return node;
    }

    private static void findSwapNode(Node delNode) {
        Node swapNode;
        char delNodeOriginColor = delNode.color;
        // 자식이 하나만 있을 때 -> 오른쪽 자식만 가지는 경우
        if (delNode.left == nilNode) {
            swapNode = delNode.right;
            transPlant(delNode, delNode.right);
        }
        // 자식이 하나만 있을 때 -> 왼쪽 자식만 가지는 경우
        else if (delNode.right == nilNode) {
            swapNode = delNode.left;
            transPlant(delNode, delNode.left);
        }
        // 자식이 둘 다 있거나 없을 떄
        else {
            swapNode = findSuccessor(delNode.right);
            // 사실상 교체할 노드가 트리에서 사라지는 거기 때문에 교체할 노드의 색으로 갱신
            delNodeOriginColor = swapNode.color;
            Node swapNodeChild = swapNode.right;
            // 교체할 노드가 삭제할 노드의 직계 자식인 경우
            if (swapNode.parent == delNode) {
                swapNodeChild.parent = swapNode;
            }
            else {
                transPlant(swapNode, swapNode.right);
                swapNode.right = delNode.right;
                swapNode.right.parent = swapNode;
            }
            // 현재 교체할 노드의 왼쪽 서브트리는 비어있으므로, 삭제할 노드의 왼쪽 서브트리를 붙인다.
            // 비어있는 이유는 후임 노드를 찾기 위해선 최대한 왼쪽 서브트리로 가기 때문이다.
            transPlant(delNode, swapNode);
            swapNode.left = delNode.left;
            swapNode.left.parent = swapNode;
            swapNode.color = delNode.color;
        }
        // 교체할 노드가 블랙 노드면 불균형 발생
        if (delNodeOriginColor == 'B') {
            deleteBalance(swapNode);
        }
    }

    private static void transPlant(Node node, Node child) {
        if (node.parent == nilNode) {
            root = child;
        } else if (node.parent.left == node) {
            node.parent.left = child;
        } else {
            node.parent.right = child;
        }
        child.parent = node.parent;
    }

    private static Node findSuccessor(Node node) {
        if (node.left == nilNode) {
            return node;
        }
        return findSuccessor(node.left);
    }

    private static void deleteBalance(Node node) {
        Node brother;
        while (node != root && node.color == 'B') {
            // 노드가 부모의 왼쪽일 때
            if (node == node.parent.left) {
                brother = node.parent.right;
                // Case 1: 형제 노드가 레드 노드인 경우
                if (brother.color == 'R') {
                    brother.color = 'B';
                    node.parent.color = 'R';
                    leftRotate(node.parent);
                    brother = node.parent.right;
                }

                // Case 2: 형제 노드가 두 자식(Nil Node 포함)이 블랙 노드일 때
                if (brother.left.color == 'B' && brother.right.color == 'B') {
                    brother.color = 'R';
                    node = node.parent;
                }
                else {
                    // Case 3-1: 형제 노드의 자식 중 삭제할 노드와 가장 먼 쪽의 노드가 블랙 노드
                    // 형제 노드의 자식 중 삭제할 노드와 가장 먼쪽의 노드가 블랙 노드(왼쪽 <> 오른쪽)
                    if (brother.right.color == 'B') {
                        brother.left.color = 'B';
                        brother.color = 'R';
                        rightRotate(brother);
                        brother = node.parent.right;
                    }
                    // Case 3-2
                    brother.color = node.parent.color;
                    node.parent.color = 'B';
                    brother.right.color = 'B';
                    leftRotate(node.parent);
                    node = root;
                }
            }
            // 노드가 부모의 오른쪽일 때, 위에 조건문에서 left, right를 반전시키면 된다.
            else {
                brother = node.parent.left;
                // Case 1: 형제 노드가 레드 노드인 경우
                if (brother.color == 'R') {
                    brother.color = 'B';
                    node.parent.color = 'R';
                    rightRotate(node.parent);
                    brother = node.parent.left;
                }
                // Case 2: 형제 노드가 두 자식(Nil Node 포함)이 블랙 노드일 때
                if (brother.left.color == 'B' && brother.right.color == 'B') {
                    brother.color = 'R';
                    node = node.parent;
                }
                else {
                    // Case 3-1: 형제 노드의 자식 중 삭제할 노드와 가장 먼 쪽의 노드가 블랙 노드
                    // 형제 노드의 자식 중 삭제할 노드와 가장 먼쪽의 노드가 블랙 노드(오른쪽 <> 왼쪽)
                    if (brother.left.color == 'B') {
                        brother.right.color = 'B';
                        brother.color = 'R';
                        leftRotate(brother);
                        brother = node.parent.left;
                    }
                    // Case 3-2
                    brother.color = node.parent.color;
                    node.parent.color = 'B';
                    brother.left.color = 'B';
                    rightRotate(node.parent);
                    node = root;
                }
            }
        }
        node.color = 'B';
    }

    private static void print(Node root, String indent, boolean last) {
        if (root != nilNode) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "   ";
            } else {
                System.out.print("L----");
                indent += "|  ";
            }
            System.out.println(root.data + "(" + root.color + ")");
            print(root.left, indent, false);
            print(root.right, indent, true);
        }
    }
}
