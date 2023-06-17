
import java.util.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.stream.Collectors;
import java.util.function.Function;

/**
 * This is a class that can find paths in a given graph.
 * 
 * There are several methods for finding paths, 
 * and they all return a PathFinder.Result object.
 */
public class PathFinder<Node> {

    private DirectedGraph<Node> graph;
    private long startTimeMillis;

    /**
     * Creates a new pathfinder for the given graph.
     * @param graph  the graph to search
     */
    public PathFinder(DirectedGraph<Node> graph) {
        this.graph = graph;
    }

    /**
     * The main search method, taking the search algorithm as input.
     * @param  algorithm  "random", "ucs" or "astar"
     * @param  start  the start node
     * @param  goal   the goal node
     */
    public Result search(String algorithm, Node start, Node goal) {
        startTimeMillis = System.currentTimeMillis();
        switch (algorithm) {
        case "random": return searchRandom(start, goal);
        case "ucs":    return searchUCS(start, goal);
        case "astar":  return searchAstar(start, goal);
        }
        throw new IllegalArgumentException("Unknown search algorithm: " + algorithm);
    }

    /**
     * Perform a random walk in the graph, hoping to reach the goal.
     * Warning: this method will give up of the random walk
     * reaches a dead end or after one million iterations.
     * So a negative result does not mean there is no path.
     * @param start  the start node
     * @param goal   the goal node
     */
    public Result searchRandom(Node start, Node goal) {
        int iterations = 0;
        LinkedList<Node> path = new LinkedList<>();
        double cost = 0;
        Random random = new Random();

        Node current = start;
        while (iterations < 1e6) {
            iterations++;
            path.add(current);
            if (current.equals(goal))
                return new Result(true, start, current, cost, path, iterations);

            List<DirectedEdge<Node>> neighbours = graph.outgoingEdges(start);
            if (neighbours.size() == 0)
                break;

            DirectedEdge<Node> edge = neighbours.get(random.nextInt(neighbours.size()));
            cost += edge.weight();
            current = edge.to();
        }
        return new Result(false, start, goal, -1, null, iterations);
    }

    /**
     * Run the Uniform Cost Search algorithm for finding the shortest path.
     * @param start  the start node
     * @param goal   the goal node
     */
    public Result searchUCS(Node start, Node goal) {
        /******************************
         * TODO: Task 1a+c            *
         * Change below this comment  *
         ******************************/
        Set <Node> visited = new HashSet<>();
        int iterations = 0;
        Queue<PQEntry> pqueue = new PriorityQueue<>(Comparator.comparingDouble(entry -> entry.costToHere));
        PQEntry entry = new PQEntry(start, 0, null);
        pqueue.add(entry);

        while (!pqueue.isEmpty()){
            entry = pqueue.remove();
            iterations++;

            if (!visited.contains(entry.node)){
                visited.add(entry.node);
                if (entry.node.equals(goal)){
                    return new Result(true, start, goal, entry.costToHere, extractPath(entry),iterations);
                }

                for(DirectedEdge <Node> edge: graph.outgoingEdges(entry.node) ){
                    double costToNext = entry.costToHere + edge.weight();
                    pqueue.add(new PQEntry(edge.to(), costToNext, entry));
                }

            }
        }
        return new Result(false, start, goal, -1, null, iterations);
    }
    
    /**
     * Run the A* algorithm for finding the shortest path.
     * @param start  the start node
     * @param goal   the goal node
     */
    public Result searchAstar(Node start, Node goal) {
        /******************************
         * TODO: Task 3               *
         * Change below this comment  *
         ******************************/
        Set <Node> visited = new HashSet<>();
        int iterations = 0;
        Queue<PQEntry> pqueue = new PriorityQueue<>(Comparator.comparingDouble(entry -> entry.guessCost));
        PQEntry entry = new PQEntry(start, 0, null, graph.guessCost(start, goal));
        pqueue.add(entry);

        while (!pqueue.isEmpty()) {
            entry = pqueue.remove();
            iterations++;

            if (!visited.contains(entry.node)) {
                visited.add(entry.node);
                if (entry.node.equals(goal)) {
                    return new Result(true, start, goal, entry.costToHere, extractPath(entry), iterations);
                }

                for (DirectedEdge<Node> edge : graph.outgoingEdges(entry.node)) {
                    double costToNext = entry.costToHere + edge.weight();
                    double guessTotalCost = costToNext + graph.guessCost(edge.to(),goal);
                    pqueue.add(new PQEntry(edge.to(), costToNext, entry, guessTotalCost));
                }
            }
        }

        return new Result(false, start, goal, -1, null, iterations);
    }

    /**
     * Extract the final path from start to goal, from the final priority queue entry.
     * @param entry  the final priority queue entry
     * @return the path from start to goal as a list of nodes
     */
    private List<Node> extractPath(PQEntry entry) {
        /******************************
         * TODO: Task 1b              *
         * Change below this comment  *
         ******************************/
        LinkedList<Node> path = new LinkedList<>();
        path.add(entry.node);
        while (entry.backPointer != null) {
            path.addFirst(entry.backPointer.node);
            entry = entry.backPointer;
        }

        return path;
    }

    /**
     * Entries to put in the priority queues in {@code searchUCS} and {@code searchAstar}.
     */
    private class PQEntry {
        /***********************************
         * TODO: Task 3                    *
         * Change below this comment,      *
         * for example, to add new fields. *
         **********************************/
        public final Node node;
        public final double costToHere;
        public final PQEntry backPointer;
        public double guessCost;

        PQEntry(Node n, double c, PQEntry bp, double guessCost) {
            node = n;
            costToHere = c;
            backPointer = bp;
            this.guessCost = guessCost;
        }

        PQEntry(Node n, double c, PQEntry bp) {
            this(n,c,bp,c);
        }
    }

    /**
     * The internal class for search results.
     */
    public class Result {
        public final boolean success;
        public final Node start;
        public final Node goal;
        public final double cost;
        public final List<Node> path;
        public final int iterations;
        public final double elapsedTime;

        public Result(boolean success, Node start, Node goal, double cost, List<Node> path, int iterations) {
            this.success = success;
            this.start = start;
            this.goal = goal;
            this.cost = cost;
            this.path = path;
            this.iterations = iterations;
            this.elapsedTime = (System.currentTimeMillis() - startTimeMillis) / 1000.0;
        }

        @Override
        public String toString() {
            StringWriter buffer = new StringWriter();
            PrintWriter w = new PrintWriter(buffer);
            if (iterations <= 0)
                w.println("ERROR: You have to iterate over at least the starting node!");
            w.println("Loop iterations: " + iterations);
            w.println("Elapsed time: " + elapsedTime);
            if (success) {
                w.println("Total cost from " + start + " to " + goal + ": " + cost);
                int len = path.size();
                if (len == 0)
                    w.println("WARNING: you have not implemented extractPath!");
                else {
                    w.println("Total path length: " + (len - 1));
                    Function<List<Node>, String> joinPath = path ->
                        path.stream().map(Node::toString).collect(Collectors.joining(" -> "));
                    if (len < 10)
                        w.println("Path: " + joinPath.apply(path));
                    else
                        w.println("Path: " + joinPath.apply(path.subList(0, 5)) + " -> ... -> " + joinPath.apply(path.subList(len-5, len)));
                }
            } else
                w.println("No path found from " + start + " to " + goal);
            return buffer.toString();
        }
    }

}
