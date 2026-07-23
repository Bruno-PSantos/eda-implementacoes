import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class PV {

    private final Node NIL;
    private Node root;
    private int size;

    public PV() {
        this.NIL = new Node();
        this.NIL.color = Color.BLACK;
        this.NIL.left = NIL;
        this.NIL.right = NIL;
        this.NIL.parent = NIL;

        this.root = NIL;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.root == NIL;
    }

    public void add(int element) {
        this.size++;

        Node newNode = new Node(element);

        if (this.isEmpty()) {
            this.root = newNode;
            this.root.color = Color.BLACK;
        } else {
            Node aux = this.root;

            while (aux != NIL) {
                if (element < aux.value) {
                    if (aux.left == NIL) {
                        newNode.parent = aux;
                        aux.left = newNode;

                        break;
                    }

                    aux = aux.left;
                } else {
                    if (aux.right == NIL) {
                        newNode.parent = aux;
                        aux.right = newNode;

                        break;
                    }

                    aux = aux.right;
                }
            }

            fixUpInsert(newNode);
        }
    }

    private void fixUpInsert(Node node) {
        if (node == this.root) {
            node.color = Color.BLACK;
            return;
        }

        if (node.parent.color == Color.BLACK) return;

        Node parent = node.parent;
        Node grandfather = parent.parent;
        Node uncle = parent.isLeftChild() ? grandfather.right : grandfather.left;

        if (uncle.color == Color.RED) {
            parent.color = Color.BLACK;
            uncle.color = Color.BLACK;
            grandfather.color = Color.RED;

            fixUpInsert(grandfather);
        } else {
            if (node.isRightChild() && parent.isLeftChild()) {
                rotateLeft(parent);
                node = parent;
                parent = node.parent;

            } else if (node.isLeftChild() && parent.isRightChild()) {
                rotateRight(parent);
                node = parent;
                parent = node.parent;
            }

            parent.color = Color.BLACK;
            grandfather.color = Color.RED;

            if (node.isLeftChild()) rotateRight(grandfather);
            else rotateLeft(grandfather);

        }
    }

    public void remove(int value) {
        Node toRemove = search(value);
        if (toRemove != NIL) {
            remove(toRemove);
            this.size -= 1;
        }
    }

    private void remove(Node toRemove) {
        if (toRemove.left != NIL && toRemove.right != NIL) {
            Node sucessor = min(toRemove.right);
            toRemove.value = sucessor.value;
            toRemove = sucessor;
        }

        Node child = (toRemove.left != NIL) ? toRemove.left : toRemove.right;
        Node parent = toRemove.parent;
        Color originalColor = toRemove.color;

        if (toRemove == this.root) {
            this.root = child;
        } else if (toRemove == parent.left) {
            parent.left = child;
        } else {
            parent.right = child;
        }

        if (child != NIL) {
            child.parent = parent;
        }

        if (originalColor == Color.BLACK) {
            fixUpDelete(child, parent);
        }
    }

    private void fixUpDelete(Node node, Node parent) {
        if (node.color == Color.RED || node == this.root) {
            node.color = Color.BLACK;
            return;
        }

        boolean isLeft = (node == parent.left);
        Node brother = isLeft ? parent.right : parent.left;

        if (brother.color == Color.RED) {
            brother.color = Color.BLACK;
            parent.color = Color.RED;
            if (isLeft) rotateLeft(parent);
            else rotateRight(parent);
            
            fixUpDelete(node, parent);
            return;
        }

        if (brother.left.color == Color.BLACK && brother.right.color == Color.BLACK) {
            brother.color = Color.RED;
            fixUpDelete(parent, parent.parent);
            return;
        }

        if (isLeft && brother.right.color == Color.BLACK) {
            brother.left.color = Color.BLACK;
            brother.color = Color.RED;
            rotateRight(brother);
            
            fixUpDelete(node, parent);
            return;
        } else if (!isLeft && brother.left.color == Color.BLACK) {
            brother.right.color = Color.BLACK;
            brother.color = Color.RED;
            rotateLeft(brother);
            
            fixUpDelete(node, parent);
            return;
        }

        brother.color = parent.color;
        parent.color = Color.BLACK;
        
        if (isLeft) {
            brother.right.color = Color.BLACK;
            rotateLeft(parent);
        } else {
            brother.left.color = Color.BLACK;
            rotateRight(parent);
        }
    }

    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        rightChild.parent = node.parent;

        node.right = rightChild.left;
        
        if (rightChild.left != NIL) {
            rightChild.left.parent = node;
        }

        rightChild.left = node;
        node.parent = rightChild;

        if (rightChild.parent != NIL) {
            if (rightChild.parent.right == node) {
                rightChild.parent.right = rightChild;
            } else {
                rightChild.parent.left = rightChild;
            }
        } else {
            this.root = rightChild;
        }
    }

    private void rotateRight(Node node) {
        Node leftChild = node.left;
        leftChild.parent = node.parent;

        node.left = leftChild.right;
        
        if (leftChild.right != NIL) {
            leftChild.right.parent = node;
        }

        leftChild.right = node;
        node.parent = leftChild;

        if (leftChild.parent != NIL) {
            if (leftChild.parent.left == node) {
                leftChild.parent.left = leftChild;
            } else {
                leftChild.parent.right = leftChild;
            }
        } else {
            this.root = leftChild;
        }
    }

    public Node successor(Node node) {
        if (node == NIL) return NIL;
        
        if (node.right != NIL) {
            return min(node.right);
        }
        
        Node p = node.parent;
        while (p != NIL && node == p.right) {
            node = p;
            p = p.parent;
        }

        return p;
    }

    private Node min(Node node) {
        while (node.left != NIL) {
            node = node.left;
        }

        return node;
    }

    public Node search(int element) {
        Node current = this.root;

        while (current != NIL) {
            if (element == current.value) return current;
            if (element < current.value) current = current.left;
            else current = current.right;
        }

        return NIL;
    }

    public int validateBlackHeight() {
        return validateBlackHeight(this.root);
    }

    private int validateBlackHeight(Node node) {
        if (node == NIL) return 1;

        int leftHeight = validateBlackHeight(node.left);
        int rightHeight = validateBlackHeight(node.right);

        if (leftHeight == -1 || rightHeight == -1 || leftHeight != rightHeight) return -1;

        if (node.color == Color.BLACK) return leftHeight + 1;
        else return leftHeight;
    }

    public int blackHeight() {
        if (root == NIL) return 0;

        return blackHeight(this.root);
    }

    private int blackHeight(Node node) {
        if (node == NIL) return 1;

        int bh = blackHeight(node.left);

        return bh + (node.left.color == Color.BLACK ? 1 : 0);
    }

    public int size() {
        return this.size;
    }

    public ArrayList<Integer> bfs() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Deque<Node> queue = new LinkedList<Node>();

        if (!isEmpty()) {
            queue.addLast(this.root);
            while (!queue.isEmpty()) {
                Node current = queue.removeFirst();
                list.add(current.value);

                if (current.left != NIL)
                    queue.addLast(current.left);
                if (current.right != NIL)
                    queue.addLast(current.right);
            }
        }
        
        return list;
    }



    private enum Color {
        RED, BLACK
    }

    class Node {

        int value;
        Node left;
        Node right;
        Node parent;
        Color color;

        Node () {}

        Node(int v) {
            this.value = v;
            this.color = Color.RED;
            this.left = NIL;
            this.right = NIL;
            this.parent = NIL;
        }

        public boolean isLeftChild() {
            return parent.left == this;
        }

        public boolean isRightChild() {
            return parent.right == this;
        }
    }

}