package drivers;

import dstructures.PlanarGraph;

public class CreatePlanar {

	public static void main(String[] args) {
		if (args.length < 3)
			usage();
		int n = Integer.parseInt(args[0]);
		int max_weight = Integer.parseInt(args[1]) - 1;
		
		System.err.println("Creating the Graph");
		PlanarGraph graph = PlanarGraph.Generate(n, max_weight);
		//graph.MakeGraphConnected();
		System.err.println("Saving the Graph");
		graph.SaveGraph(args[2]);
		//graph.toCSV("orig-edges.csv", "orig-nodes.csv");
	}

	private static void usage() {
	    	System.out.println("Saves the file as java seralized object");
		System.out.println("Usage: run [n] [max weight] [output]");
		System.exit(-1);
	}

}
