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

                        fixUpInsert(newNode);
                        return;
                    }

                    aux = aux.left;
                } else {
                    if (aux.right == NIL) {
                        newNode.parent = aux;
                        aux.right = newNode;

                        fixUpInsert(newNode);
                        return;
                    }

                    aux = aux.right;
                }
            }
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

    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        rightChild.parent = node.parent;

        node.right = rightChild.left;
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

    public Node search(int element) {
        Node current = this.root;

        while (current != NIL) {
            if (element == current.value) return current;
            if (element < current.value) current = current.left;
            else current = current.right;
        }

        return NIL;
    }

    public int blackHeight() {
        return blackHeight(this.root);
    }

    private int blackHeight(Node node) {
        if (node == NIL) return 0;

        int leftHeight = blackHeight(node.left);
        int rightHeight = blackHeight(node.right);

        if (leftHeight == -1 || rightHeight == -1 || leftHeight != rightHeight) return -1;

        if (node.color == Color.BLACK) return leftHeight + 1;
        else return leftHeight;
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