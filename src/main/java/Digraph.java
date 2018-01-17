/******************************************************************************
 *  Compilation:  javac Digraph.java
 *  Execution:    java Digraph filename.txt
 *  Dependencies: Bag.java In.java System.out.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 *  A graph, implemented using an array of lists.
 *  Parallel edges and self-loops are permitted.
 *
 *  % java Digraph tinyDG.txt
 *  13 vertices, 22 edges
 *  0: 5 1
 *  1:
 *  2: 0 3
 *  3: 5 2
 *  4: 3 2
 *  5: 4
 *  6: 9 4 8 0
 *  7: 6 9
 *  8: 6
 *  9: 11 10
 *  10: 12
 *  11: 4 12
 *  12: 9
 *
 ******************************************************************************/

import java.util.ArrayList;
import java.util.Stack;

/**
 * The {@code Digraph} class represents a directed graph of vertices
 * named 0 through <em>vertexNumber</em> - 1.
 * It supports the following two primary operations: add an edge to the digraph,
 * iterate over all of the vertices adjacent from a given vertex.
 * Parallel edges and self-loops are permitted.
 * <p>
 * This implementation uses an adjacency-lists representation, which
 * is a vertex-indexed array of {Bag} objects.
 * All operations take constant time (in the worst case) except
 * iterating over the vertices adjacent from a given vertex, which takes
 * time proportional to the number of such vertices.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */

public class Digraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int vertexNumber;           // number of vertices in this digraph
    private int edgesNumber;                 // number of edges in this digraph
    private ArrayList<Integer>[] adjacency;    // adjacency[v] = adjacency list for vertex v // RU: list smejnosti
    private int[] indegree;        // indegree[v] = indegree of vertex v

    /**
     * Initializes an empty digraph with <em>vertexNumber</em> vertices.
     *
     * @param vertexNumber the number of vertices
     * @throws IllegalArgumentException if {@code vertexNumber < 0}
     */
    public Digraph(int vertexNumber) {
        if (vertexNumber < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.vertexNumber = vertexNumber;
        this.edgesNumber = 0;
        indegree = new int[vertexNumber];
        //TODO to analyze and swap
        adjacency = (ArrayList<Integer>[]) new ArrayList[vertexNumber];
        for (int v = 0; v < vertexNumber; v++) {
            adjacency[v] = new ArrayList<>();
        }
    }

    /**
     * Initializes a new digraph that is a deep copy of the specified digraph.
     *
     * @param that the digraph to copy
     */
    public Digraph(Digraph that) {
        this(that.getVertexNumber());
        this.edgesNumber = that.getEdgesNumber();
        for (int v = 0; v < vertexNumber; v++)
            this.indegree[v] = that.indegree(v);
        for (int v = 0; v < that.getVertexNumber(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<Integer> reverse = new Stack<>();
            for (int w : that.adjacency[v]) {
                reverse.push(w);
            }
            for (int w : reverse) {
                //TODO to analyze and swap
                adjacency[v].add(w);
            }
        }
    }

    /**
     * Returns the number of vertices in this digraph.
     *
     * @return the number of vertices in this digraph
     */
    public int getVertexNumber() {
        return vertexNumber;
    }

    /**
     * Returns the number of edges in this digraph.
     *
     * @return the number of edges in this digraph
     */
    public int getEdgesNumber() {
        return edgesNumber;
    }


    // throw an IllegalArgumentException unless {@code 0 <= v < vertexNumber}
    private void requireVertexExists(int v) {
        if (v < 0 || v >= vertexNumber)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (vertexNumber - 1));
    }

    /**
     * Adds the directed edge v→w to this digraph.
     *
     * @param v the tail vertex
     * @param w the head vertex
     * @throws IllegalArgumentException unless both {@code 0 <= v < vertexNumber} and {@code 0 <= w < vertexNumber}
     */
    public void addEdge(int v, int w) {
        requireVertexExists(v);
        requireVertexExists(w);
        adjacency[v].add(w);
        indegree[w]++;
        edgesNumber++;
    }

    /**
     * Returns the vertices adjacent from vertex {@code v} in this digraph.
     *
     * @param v the vertex
     * @return the vertices adjacent from vertex {@code v} in this digraph, as an iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < vertexNumber}
     */
    public Iterable<Integer> getAdjacency(int v) {
        requireVertexExists(v);
        return adjacency[v];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param v the vertex
     * @return the outdegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < vertexNumber}
     */
    public int outdegree(int v) {
        requireVertexExists(v);
        //TODO to analyze and swap
        return adjacency[v].size();
    }

    /**
     * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}.
     *
     * @param v the vertex
     * @return the indegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < vertexNumber}
     */
    public int indegree(int v) {
        requireVertexExists(v);
        return indegree[v];
    }

    /**
     * Returns the reverse of the digraph.
     *
     * @return the reverse of the digraph
     */
    public Digraph reverse() {
        Digraph reverse = new Digraph(vertexNumber);
        for (int v = 0; v < vertexNumber; v++) {
            for (int w : getAdjacency(v)) {
                reverse.addEdge(w, v);
            }
        }
        return reverse;
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return the number of vertices <em>vertexNumber</em>, followed by the number of edges <em>edgesNumber</em>,
     * followed by the <em>vertexNumber</em> adjacency lists
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(vertexNumber + " vertices, " + edgesNumber + " edges " + NEWLINE);
        for (int v = 0; v < vertexNumber; v++) {
            s.append(String.format("%d: ", v));
            //TODO to analyze and swap
            for (int w : adjacency[v]) {
                s.append(String.format("%d ", w));
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * Unit tests the {@code Digraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        //TODO init and test
        Digraph G = new Digraph(5);
        System.out.println(G);
    }

}

//acknowledgments: Robert Sedgewick and Kevin Wayne.