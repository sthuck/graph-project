package drivers;

import dstructures.Graph;

public class CreateAndSave {

	public static void main(String[] args) {
		if (args.length < 4)
			usage();
		int n = Integer.parseInt(args[0]);
		Double prob = Double.parseDouble(args[1]);
		int max_weight = Integer.parseInt(args[2]) - 1;
		
		System.err.println("Creating the Graph");
		Graph graph = Graph.createRandomGraph(n, prob, max_weight);
		graph.MakeGraphConnected();
		System.err.println("Saving the Graph");
		graph.SaveGraph(args[3]);
		//graph.toCSV("orig-edges.csv", "orig-nodes.csv");
	}

	private static void usage() {
	    	System.out.println("Saves the file as java seralized object");
		System.out.println("Usage: run [n] [p] [max weight] [output]");
		System.exit(-1);
	}

}
