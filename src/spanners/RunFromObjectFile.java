package spanners;

import java.util.Scanner;

public class RunFromObjectFile {

	public static void main(String[] args) {
		if (args.length < 3)
			usage();
		String filename = args[0];
		int r = Integer.parseInt(args[1]);
		boolean docheck = (Integer.parseInt(args[2])==0) ? false : true;

		System.out.print("Loading the Graph...");
		Graph graph = Graph.ReadGraph(filename);
		System.out.println("Done.");

		System.out.println("Generating Spanner...");
		Graph spanner = graph.MakeRSpanner(r);
		System.out.println("Done.");
		
		if (docheck) {
			System.out.print("Running Floyd-Marshell on original Graph...");
			double dist[][] = graph.Floyd_Warshall();
			System.out.println("Done.");
			System.out.print("Running Floyd-Marshell on spanner...");
			double dist2[][] = spanner.Floyd_Warshall();
			System.out.println("Done.");
			System.out.println("Verifying Distances are no larger than parameter r=" + r);
			int n = dist.length;
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					if (dist[i][j] * r < dist2[i][j])
						System.out.println("Found ERROR! spanner doesnt hold the requirements! " + i + "->" + j);
		}
		
		System.out.println("Number of edges in original Graph:" + graph.edges.size());
		System.out.println("Number of edges in r-spanner Graph:" + spanner.edges.size());
		System.out.print("Finding MST...");
		Graph mst = graph.MST_Prim();
		System.out.println("Done.");
		
		System.out.println("Number of edges in Minimum Spaning Tree:" + mst.edges.size());
		System.out.println("Finished!");
		System.out.println("==================");
		
		System.out.println("Enter graphname to output the graph. Empty string means no output.\ngraph name:");
		Scanner scanIn = new Scanner(System.in);
	    filename = scanIn.nextLine();
	    scanIn.close(); 
	    if (filename.length()>0) {
	    	graph.toCSV(filename +"_edges.csv", filename +"_nodes.csv");
	    	spanner.toCSV(filename +"_"+r +"spanner_edges.csv", null);
	    	mst.toCSV(filename +"_"+"MST_edges.csv", null);
	    }
		

	}

	private static void usage() {
		System.out.println("Usage: run [input filename] [r] [do check 0\\1]");
		System.exit(-1);
	}

}
