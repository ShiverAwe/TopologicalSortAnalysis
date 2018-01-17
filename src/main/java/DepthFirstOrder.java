/******************************************************************************
 *  Compilation:  javac DepthFirstOrder.java
 *  Execution:    java DepthFirstOrder digraph.txt
 *  Dependencies: Digraph.java Queue.java Stack.java System.out.java
 *                EdgeWeightedDigraph.java DirectedEdge.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDAG.txt
 *                https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *
 *  Compute preorder and postorder for a digraph or edge-weighted digraph.
 *  Runs in O(getEdgesNumber + getVertexNumber) time.
 *
 *  % java DepthFirstOrder tinyDAG.txt
 *     v  getPreorder getPostorder
 *  --------------
 *     0    0    8
 *     1    3    2
 *     2    9   10
 *     3   10    9
 *     4    2    0
 *     5    1    1
 *     6    4    7
 *     7   11   11
 *     8   12   12
 *     9    5    6
 *    10    8    5
 *    11    6    4
 *    12    7    3
 *  Preorder:  0 5 4 1 6 9 11 12 10 2 3 7 8
 *  Postorder: 4 5 1 12 11 10 9 6 0 3 2 7 8
 *  Reverse postorder: 8 7 2 3 0 6 9 10 11 12 1 5 4
 *
 ******************************************************************************/

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

/**
 * The {@code DepthFirstOrder} class represents a data type for
 * determining depth-first search ordering of the vertices in a digraph
 * or edge-weighted digraph, including preorder, postorder, and reverse postorder.
 * <p>
 * This implementation uses depth-first search.
 * The constructor takes time proportional to <em>getVertexNumber</em> + <em>getEdgesNumber</em>
 * (in the worst case),
 * where <em>getVertexNumber</em> is the number of vertices and <em>getEdgesNumber</em> is the number of edges.
 * Afterwards, the <em>preorder</em>, <em>postorder</em>, and <em>reverse postorder</em>
 * operation takes take time proportional to <em>getVertexNumber</em>.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class DepthFirstOrder {
    private boolean[] marked;          // marked[v] = has v been marked in dfs?
    private int[] pre;                 // getPreorder[v]    = preorder  number of v
    private int[] post;                // getPostorder[v]   = postorder number of v
    private Queue<Integer> preorder;   // vertices in preorder
    private Queue<Integer> postorder;  // vertices in postorder
    private int preCounter;            // counter or preorder numbering
    private int postCounter;           // counter for postorder numbering

    /**
     * Determines a depth-first order for the digraph {@code that}.
     *
     * @param that the digraph
     */
    public DepthFirstOrder(Digraph that) {
        this.pre = new int[that.getVertexNumber()];
        this.post = new int[that.getVertexNumber()];
        this.postorder = new ArrayDeque<>();
        this.preorder = new ArrayDeque<>();
        this.marked = new boolean[that.getVertexNumber()];
        for (int v = 0; v < that.getVertexNumber(); v++)
            if (!marked[v]) depthFirstSearch(that, v);

        assert check();
    }


    // run DFS in digraph G from vertex v and compute preorder/postorder
    private void depthFirstSearch(Digraph G, int v) {
        marked[v] = true;
        pre[v] = preCounter++;
        //TODO to analyze
        preorder.add(v);
        for (int w : G.getAdjacency(v)) {
            if (!marked[w]) {
                depthFirstSearch(G, w);
            }
        }
        //TODO to analyze
        postorder.add(v);
        post[v] = postCounter++;
    }


    /**
     * Returns the preorder number of vertex {@code v}.
     *
     * @param v the vertex
     * @return the preorder number of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < getVertexNumber}
     */
    public int getPreorder(int v) {
        requireVertex(v);
        return pre[v];
    }

    /**
     * Returns the postorder number of vertex {@code v}.
     *
     * @param v the vertex
     * @return the postorder number of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < getVertexNumber}
     */
    public int getPostorder(int v) {
        requireVertex(v);
        return post[v];
    }

    /**
     * Returns the vertices in postorder.
     *
     * @return the vertices in postorder, as an iterable of vertices
     */
    public Iterable<Integer> getPostorder() {
        return postorder;
    }

    /**
     * Returns the vertices in preorder.
     *
     * @return the vertices in preorder, as an iterable of vertices
     */
    public Iterable<Integer> getPreorder() {
        return preorder;
    }

    /**
     * Returns the vertices in reverse postorder.
     *
     * @return the vertices in reverse postorder, as an iterable of vertices
     */
    public Iterable<Integer> getPostorderReversed() {
        Stack<Integer> reverse = new Stack<>();
        for (int v : postorder)
            reverse.push(v);
        return reverse;
    }



    private void requireVertex(int v) {
        int vertexCount = marked.length;
        if (v < 0 || v >= vertexCount)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (vertexCount - 1));
    }
    /**
     * Unit tests the {@code DepthFirstOrder} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        //TODO check init
        Digraph graph = new Digraph(5);

        DepthFirstOrder dfs = new DepthFirstOrder(graph);
        System.out.println("   v  getPreorder getPostorder");
        System.out.println("--------------");
        for (int v = 0; v < graph.getVertexNumber(); v++) {
            System.out.printf("%4d %4d %4d\n", v, dfs.getPreorder(v), dfs.getPostorder(v));
        }

        System.out.print("Preorder:  ");
        for (int v : dfs.getPreorder()) {
            System.out.print(v + " ");
        }
        System.out.println();

        System.out.print("Postorder: ");
        for (int v : dfs.getPostorder()) {
            System.out.print(v + " ");
        }
        System.out.println();

        System.out.print("Reverse postorder: ");
        for (int v : dfs.getPostorderReversed()) {
            System.out.print(v + " ");
        }
        System.out.println();


    }

    // TEST
    // check that getPreorder() and getPostorder() are consistent with getPreorder(v) and getPostorder(v)
    // throw an IllegalArgumentException unless {@code 0 <= v < getVertexNumber}
    private boolean check() {

        // check that getPostorder(v) is consistent with getPostorder()
        int r = 0;
        for (int vertex : getPostorder()) {
            if (getPostorder(vertex) != r) {
                System.out.println("getPostorder(vertex) and getPostorder() inconsistent");
                return false;
            }
            r++;
        }

        // check that getPreorder(v) is consistent with getPreorder()
        r = 0;
        for (int v : getPreorder()) {
            if (getPreorder(v) != r) {
                System.out.println("getPreorder(v) and getPreorder() inconsistent");
                return false;
            }
            r++;
        }

        return true;
    }

}

//acknowledgments: Robert Sedgewick and Kevin Wayne.