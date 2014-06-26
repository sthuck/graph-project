import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class run2 {

    public static void main(String[] args) {
	if (args.length < 2)
	    usage();
	int n = Integer.parseInt(args[0]);
	String filename = args[1];
	
	System.out.println("Loading the Graph");
	Graph graph = Graph.FromMatrix(n, filename);
	System.out.println("Pringing the Graph");
	System.out.println(graph.toString());
	
	Dijkstra dijkstra = new Dijkstra(graph,graph.getVertexes().get(0));
	dijkstra.Do();
	System.out.println("Distance is"+Arrays.toString(dijkstra.dist));
	dijkstra = new Dijkstra(graph,graph.getVertexes().get(1));
	dijkstra.Do();
	System.out.println("Distance is"+Arrays.toString(dijkstra.dist));
	dijkstra = new Dijkstra(graph,graph.getVertexes().get(2));
	dijkstra.Do();
	System.out.println("Distance is"+Arrays.toString(dijkstra.dist));
	dijkstra = new Dijkstra(graph,graph.getVertexes().get(3));
	dijkstra.Do();
	System.out.println("Distance is"+Arrays.toString(dijkstra.dist));
	//graph.toCSV("new.csv");
	Graph graph2 = graph.toUndirectedUnweighted();
	List<Set<Vertex>> elements = graph2.getConnectedElements();
	System.out.println(elements);
	
    }
    
    
    private static void usage() {
	System.out.println("Usage: run [n] [filename]");
	System.exit(-1);
    }

}
