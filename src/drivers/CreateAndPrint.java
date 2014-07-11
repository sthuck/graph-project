package drivers;

import dstructures.Graph;

public class CreateAndPrint {

	public static void main(String[] args) {
		if (args.length < 3)
			usage();
		int n = Integer.parseInt(args[0]);
		Double prob = Double.parseDouble(args[1]);
		int max_weight = Integer.parseInt(args[2]) - 1;
		
		System.err.println("Creating the Graph");
		Graph graph = Graph.createRandomGraph(n, prob, max_weight);
		graph.MakeGraphConnected();
		System.err.println("Printing the Graph");
		System.out.println(graph.toString());
		graph.toCSV("orig-edges.csv", "orig-nodes.csv");
	}

	private static void usage() {
		System.out.println("Usage: run [n] [p] [max weight]");
		System.exit(-1);
	}

}
