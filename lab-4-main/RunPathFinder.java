
import java.util.Scanner;

/**
 * This is the main class for finding paths in graphs.
 * 
 * Depending on the command line arguments, 
 * it creates different graphs and runs different search algorithms.
 */
public class RunPathFinder {    

    private static void testGridGraph(GridGraph graph, String algorithm, String start, String goal, boolean showGrid) {
        PathFinder<GridGraph.Coord> finder = new PathFinder<>(graph);
        String[] startC = start.split(":");
        String[] goalC = goal.split(":");
        PathFinder<GridGraph.Coord>.Result result =
            finder.search(algorithm,
                          new GridGraph.Coord(Integer.valueOf(startC[0]), Integer.valueOf(startC[1])),
                          new GridGraph.Coord(Integer.valueOf(goalC[0]), Integer.valueOf(goalC[1])));
        if (showGrid && result.success && graph.width() < 250 && graph.height() < 250)
            System.out.println(graph.showGrid(result.path));
        System.out.println(result);
    }

    public static void main(String[] args) {
        /* // If you don't want to specify arguments on the command-line, just uncomment this block.
        if (args.length == 0) {
            args = new String[] { 
                "random",         // Algorithm = random | ucs | astar
                "AdjacencyGraph", // Graphtype = AdjacencyGraph | WordLadder | NPuzzle | GridGraph
                "graphs/AdjacencyGraph/citygraph-VGregion.txt",  // Graph
                "Vara",           // Start node
                "Skara"           // Goal node
            };
        }
        */

        // If no arguments are given, ask for them.
        if (args.length == 0) {
            args = new String[5];
            Scanner input = new Scanner(System.in);
            System.out.print("Algorithm (random | ucs | astar): ");
            System.out.flush(); args[0] = input.nextLine();
            System.out.print("Graph type (AdjacencyGraph | WordLadder | NPuzzle | GridGraph): ");
            System.out.flush(); args[1] = input.nextLine();
            System.out.print("Graph: ");
            System.out.flush(); args[2] = input.nextLine();
            System.out.print("Start node: ");
            System.out.flush(); args[3] = input.nextLine();
            System.out.print("Goal node: ");
            System.out.flush(); args[4] = input.nextLine();
        }

        try {
            if (args.length < 5 || args.length % 2 == 0)
                throw new IllegalArgumentException();
            String algorithm = args[0].toLowerCase();
            String graphType = args[1].toLowerCase();
            String filePath  = args[2];
            PathFinder<String> finder;
            for (int i = 3; i < args.length; i += 2) {
                String start = args[i], goal = args[i+1];
                System.out.println();
                switch (graphType) {

                case "adjacencygraph":
                    finder = new PathFinder<>(new AdjacencyGraph(filePath));
                    System.out.println(finder.search(algorithm, start, goal));
                    break;

                case "wordladder":
                    finder = new PathFinder<>(new WordLadder(filePath));
                    System.out.println(finder.search(algorithm, start, goal));
                    break;

                case "npuzzle":
                    finder = new PathFinder<>(new NPuzzle(Integer.valueOf(filePath)));
                    System.out.println(finder.search(algorithm, start, goal));
                    break;

                case "gridgraph":
                    testGridGraph(new GridGraph(filePath), algorithm, start, goal, true);
                    break;

                case "gridgraph-nogrid":
                    // Use this variant if you don't want to show the grid in the result
                    testGridGraph(new GridGraph(filePath), algorithm, start, goal, false);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown graph type: " + args[1]);
                }
                System.out.println();
            }
        } catch (Exception e) {
            // If there is an error, print it and a little command-line help
            e.printStackTrace();
            System.err.println();
            System.err.println("Usage: java RunPathFinder algorithm graphtype graph start goal");
            System.err.println("  where algorithm = random | ucs | astar");
            System.err.println("        graphtype = AdjacencyGraph | WordLadder | NPuzzle | GridGraph");
            System.exit(1);
        }
    }

}

