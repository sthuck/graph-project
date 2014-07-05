public class RunFromObjectFile {

	public static void main(String[] args) {
		if (args.length < 3)
			usage();
		String filename = args[0];
		int r = Integer.parseInt(args[1]);

		System.out.println("Loading the Graph");
		Graph graph = Graph.ReadGraph(filename);
		// System.out.println("Pringing the Graph");
		// System.out.println(graph.toString());
		System.out.println("Running Floyd-Marshell");
		int dist[][] = graph.Floyd_Warshall();
		System.out.println("Generating Spanner");
		Graph spanner = graph.MakeRSpanner(r);
		spanner.toCSV(args[2] + "-edges.csv", args[2] + "-nodes.csv");
		System.out.println("Running FloydMarshell on spanner");
		int dist2[][] = spanner.Floyd_Warshall();
		System.out.println("Verifying Distances are no larger than parameter r=" + r);
		int n = dist.length;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (dist[i][j] * r < dist2[i][j])
					System.out.println("Found ERROR! spanner doesnt hold the requirements! " + i + "->" + j);
		
		System.out.println("Number of edges in original Graph:" + graph.edges.size() / 2);
		System.out.println("Number of edges in r-spanner Graph:" + spanner.edges.size() / 2);
		System.out.println("Finding MST");
		Graph mst = graph.MST_Prim();
		System.out.println("Number of edges in Minimum Spaning Tree:" + mst.edges.size() / 2);
		System.out.println("Finished!");

	}

	private static void usage() {
		System.out.println("Usage: run [input filename] [r] [outputname]");
		System.exit(-1);
	}

}
