import java.util.Stack;

/******************************************************************************
 *  Compilation:  javac DirectedCycle.java
 *  Execution:    java DirectedCycle input.txt
 *  Dependencies: Digraph.java Stack.java System.out.java In.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/tinyDAG.txt
 *
 *  Finds a directed cycle in a digraph.
 *  Runs in O(getEdgesNumber + getVertexNumber) time.
 *
 *  % java DirectedCycle tinyDG.txt
 *  Directed cycle: 3 5 4 3
 *
 *  %  java DirectedCycle tinyDAG.txt
 *  No directed cycle
 *
 ******************************************************************************/

/**
 * The {@code DirectedCycle} class represents a data type for
 * determining whether a digraph has a directed cycle.
 * The <em>hasCycle</em> operation determines whether the digraph has
 * a directed cycle and, and of so, the <em>cycle</em> operation
 * returns one.
 * <p>
 * This implementation uses depth-first search.
 * The constructor takes time proportional to <em>getVertexNumber</em> + <em>getEdgesNumber</em>
 * (in the worst case),
 * where <em>getVertexNumber</em> is the number of vertices and <em>getEdgesNumber</em> is the number of edges.
 * Afterwards, the <em>hasCycle</em> operation takes constant time;
 * the <em>cycle</em> operation takes time proportional
 * to the length of the cycle.
 * <p>
 * See {@link Topological} to compute a topological order if the
 * digraph is acyclic.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class DirectedCycle {
    private boolean[] marked;        // marked[v] = has vertex v been marked?
    private int[] edgeTo;            // edgeTo[v] = previous vertex on path to v
    private boolean[] onStack;       // onStack[v] = is vertex on the stack?
    private Stack<Integer> cycle;    // directed cycle (or null if no such cycle)

    /**
     * Determines whether the digraph {@code graph} has a directed cycle and, if so,
     * finds such a cycle.
     *
     * @param graph the digraph
     */
    public DirectedCycle(Digraph graph) {
        marked = new boolean[graph.getVertexNumber()];
        onStack = new boolean[graph.getVertexNumber()];
        edgeTo = new int[graph.getVertexNumber()];

        for (int vertex = 0; vertex < graph.getVertexNumber(); vertex++)
            if (!marked[vertex] && cycle == null) depthFirstSearch(graph, vertex);
    }

    // check that algorithm computes either the topological order or finds a directed cycle
    private void depthFirstSearch(Digraph graph, int vertex) {
        onStack[vertex] = true;
        marked[vertex] = true;
        for (int w : graph.getAdjacency(vertex)) {

            // short circuit if directed cycle found
            if (cycle != null) return;

                // found new vertex, so recur
            else if (!marked[w]) {
                edgeTo[w] = vertex;
                depthFirstSearch(graph, w);
            }

            // trace back directed cycle
            else if (onStack[w]) {
                cycle = new Stack<>();
                for (int x = vertex; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(vertex);
                assert check();
            }
        }
        onStack[vertex] = false;
    }

    /**
     * Does the digraph have a directed cycle?
     *
     * @return {@code true} if the digraph has a directed cycle, {@code false} otherwise
     */
    public boolean hasCycle() {
        return cycle != null;
    }

    /**
     * Returns a directed cycle if the digraph has a directed cycle, and {@code null} otherwise.
     *
     * @return a directed cycle (as an iterable) if the digraph has a directed cycle,
     * and {@code null} otherwise
     */
    public Iterable<Integer> cycle() {
        return cycle;
    }


    // certify that digraph has a directed cycle if it reports one
    private boolean check() {

        if (hasCycle()) {
            // verify cycle
            int first = -1, last = -1;
            for (int v : cycle()) {
                if (first == -1) first = v;
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }

        return true;
    }

    /**
     * Unit tests the {@code DirectedCycle} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        //TODO to init and test
        Digraph graph = new Digraph(5);

        DirectedCycle finder = new DirectedCycle(graph);
        if (finder.hasCycle()) {
            System.out.print("Directed cycle: ");
            for (int vertex : finder.cycle()) {
                System.out.print(vertex + " ");
            }
            System.out.println();
        } else {
            System.out.println("No directed cycle");
        }
        System.out.println();
    }
}

//acknowledgments: Robert Sedgewick and Kevin Wayne.