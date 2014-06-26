
public class run {

	public static void main(String[] args) {
		if (args.length < 4)
			usage();
		int n = Integer.parseInt(args[0]);
		Double prob = Double.parseDouble(args[1]);
		int max_weight = Integer.parseInt(args[2]) - 1;
		int r = Integer.parseInt(args[3]);

		
		System.out.println("Printing the Graph");
		Graph graph = Graph.createRandomGraph(n, prob, max_weight);
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
