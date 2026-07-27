import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class PV {

    private final Node NIL;
    private Node root;
    private int size;

    /**
     * Construtor padrão da PV. Inicializa o nó sentinela NIL e define a raiz como NIL.
     */
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

    /**
     * Implementação iterativa da adição de um elemento em uma PV.
     * 
     * @param element o valor a ser adicionado na árvore.
     */
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

    /**
     * Método auxiliar para rebalancear e restaurar as propriedades da PV após uma inserção.
     * 
     * @param node o nó a ser ajustado.
     */
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

    /**
     * Remove o nó cujo valor é igual ao passado como parâmetro.
     * 
     * @param value o valor do elemento a ser removido da PV.
     */
    public void remove(int value) {
        Node toRemove = search(value);
        if (toRemove != NIL) {
            remove(toRemove);
            this.size -= 1;
        }
    }

    /**
     * Método auxiliar para controle da remoção de um nó.
     * 
     * @param toRemove o nó a ser removido.
     */
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

    /**
     * Método auxiliar para rebalancear e restaurar as propriedades da PV após uma remoção.
     * 
     * @param node o nó a ser ajustado.
     * @param parent o pai do nó a ser ajustado.
     */
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
            
            brother = parent.right;

        } else if (!isLeft && brother.left.color == Color.BLACK) {
            brother.right.color = Color.BLACK;
            brother.color = Color.RED;
            rotateLeft(brother);
            
            brother = parent.left;
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

    /**
     * Rotaciona o nó à esquerda.
     * 
     * @param node nó a partir de onde a rotação ocorre
     */
    private void rotateLeft(Node node) {
        Node newRoot = node.right;
        newRoot.parent = node.parent;

        node.right = newRoot.left;
        
        if (newRoot.left != NIL) {
            newRoot.left.parent = node;
        }

        newRoot.left = node;
        node.parent = newRoot;

        if (newRoot.parent != NIL) {
            if (newRoot.parent.right == node) {
                newRoot.parent.right = newRoot;

            } else {
                newRoot.parent.left = newRoot;
            }

        } else {
            this.root = newRoot;
        }
    }

    /**
     * Rotaciona o nó à direita.
     * 
     * @param node nó a partir de onde a rotação ocorre
     */
    private void rotateRight(Node node) {
        Node newRoot = node.left;
        newRoot.parent = node.parent;

        node.left = newRoot.right;
        
        if (newRoot.right != NIL) {
            newRoot.right.parent = node;
        }

        newRoot.right = node;
        node.parent = newRoot;

        if (newRoot.parent != NIL) {
            if (newRoot.parent.left == node) {
                newRoot.parent.left = newRoot;

            } else {
                newRoot.parent.right = newRoot;
            }

        } else {
            this.root = newRoot;
        }
    }

    /**
     * Retorna o nó cujo valor é sucessor do valor passado como parâmetro. 
     * 
     * @param valor O valor para o qual deseja-se identificar o sucessor.
     * @return O nó contendo o sucessor do valor passado como parâmetro. O método retorna NIL
     * caso não haja sucessor.
     */
    public Node sucessor(Node node) {
        if (node == NIL) return NIL;
        
        if (node.right != NIL)
            return min(node.right);
        else {
            Node aux = node.parent;
            
            while (aux != NIL && aux.value < node.value)
                aux = aux.parent;
            
            return aux;
        }
    }

    /**
     * Retorna o nó que contém o valor mínimo da árvore cuja raiz é passada como parâmetro. Implementação iterativa.
     * 
     * @return o nó contendo o valor mínimo da árvore ou NIL se a árvore estiver vazia.
     */
    private Node min(Node node) {
        if (isEmpty()) return NIL;

        while (node.left != NIL) {
            node = node.left;
        }

        return node;
    }

    /**
     * Busca o nó cujo valor é igual ao passado como parâmetro. Implementação 
     * iterativa da busca binária em uma PV.
     * 
     * @param element O elemento a ser procurado.
     * @return O nó contendo o elemento procurado. O método retorna NIL caso
     * o elemento não esteja presente na árvore.
     */
    public Node search(int element) {
        Node current = this.root;

        while (current != NIL) {
            if (element == current.value) return current;
            if (element < current.value) current = current.left;
            else current = current.right;
        }

        return NIL;
    }

    /**
     * Valida a altura preta de toda a árvore a partir da raiz.
     * No cálculo da altura preta, a raiz não conta e o nó NIL conta.
     * 
     * @return a altura preta da árvore se estiver válida, -1 caso haja violação da propriedade de altura preta e 0 caso esteja vazia.
     */
    public int validateBlackHeight() {
        if (this.root == NIL) return 0;

        int blackHeight = validateBlackHeight(this.root);

        if (blackHeight == -1) return - 1;
        return blackHeight - 1;
    }

    /**
     * Método para auxiliar na validação da altura preta.
     * 
     * @param node a raiz da subárvore a ser validada.
     * @return a altura preta da subárvore se estiver válida ou -1 caso haja violação da propriedade de altura preta.
     */
    private int validateBlackHeight(Node node) {
        if (node == NIL) return 1;

        int leftHeight = validateBlackHeight(node.left);
        int rightHeight = validateBlackHeight(node.right);

        if (leftHeight == -1 || rightHeight == -1 || leftHeight != rightHeight) return -1;

        if (node.color == Color.BLACK) return leftHeight + 1;
        return leftHeight;
    }

    /**
     * Retorna a altura preta de um ramo.
     * No cálculo da altura preta, a raiz não conta e o nó NIL conta.
     * 
     * @return a altura preta acumulada a partir da raiz ou 0 se a árvore estiver vazia.
     */
    public int blackHeight(int valor) {
        Node no = search(valor);
        if (no == NIL) return 0;

        return blackHeightIncluding(no.left);
    }

    /**
     * Método para auxiliar o cálculo da altura preta de um ramo.
     * 
     * @param node a raiz da subárvore.
     * @return a altura preta acumulada no caminho a partir do nó fornecido.
     */
    private int blackHeightIncluding(Node node) {
        if (node == NIL) return 1;

        return (node.color == Color.BLACK ? 1 : 0) + blackHeightIncluding(node.left);
    }

    /**
     * Percorre a árvore em largura. 
     * 
     * @return Uma lista com a os elementos percorridos em largura.
     */
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

    /**
     * @return o tamanho da árvore.
     */
    public int size() {
        return this.size;
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