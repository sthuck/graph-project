import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Math;

public class run {

	public static void main(String[] args) {
		if (args.length < 4)
			usage();
		int n = Integer.parseInt(args[0]);
		Double prob = Double.parseDouble(args[1]);
		int max_weight = Integer.parseInt(args[2]) - 1;
		int r = Integer.parseInt(args[3]);
		Random rand = new Random();

		List<Vertex> vertices = new ArrayList<Vertex>(n);
		for (int i = 0; i < n; i++)
			vertices.add(new Vertex(i));
		List<Edge> edges = new ArrayList<Edge>();
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				if (i != j && Math.random() < prob) {
					Edge e = new Edge(i + "->" + j, vertices.get(i), vertices.get(j), rand.nextInt(max_weight) + 1);
					edges.add(e);
					vertices.get(i).adjencies.add(e);
				}
			}
		System.out.println("Printing the Graph");
		Graph graph = new Graph(vertices, edges);
		System.out.println(graph.toString());
		graph.MakeGraphConnected();
		graph.toCSV("orig-edges.csv", "orig-nodes.csv");
		Graph spanner = graph.MakeRSpanner(r);
		spanner.toCSV("spanner-edges.csv", "spanner-nodes.csv");
	}

	private static void usage() {
		System.out.println("Usage: run [n] [p] [max weight] [r]");
		System.exit(-1);
	}

}
