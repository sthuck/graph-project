package drivers;

import java.util.Scanner;

import algo.FloydWarshall;
import algo.mstMaker;
import algo.spannerMaker;
import dstructures.Graph;

public class Run {

	public static void main(String[] args) {
		if (args.length < 3)
			usage();
		String filename = args[0];
		double r = Double.parseDouble(args[1]);
		boolean docheck = (Integer.parseInt(args[2])==0) ? false : true;

		System.out.print("Loading the Graph...");
		Graph graph = Graph.ReadGraph(filename);
		System.out.println("Done.");

		System.out.println("Generating Spanner...");
		Graph spanner = spannerMaker.MakeRSpanner(graph, r);
		System.out.println("Done.");
		Graph mst = null;
		double[][] dist=null,dist2=null,dist3=null;
		
		if (docheck) {
			System.out.print("Running Floyd-Marshell on original Graph...");
			dist = FloydWarshall.Do(graph);
			System.out.println("Done.");
			System.out.print("Running Floyd-Marshell on spanner...");
			dist2 = FloydWarshall.Do(spanner);
			System.out.println("Done.");
			System.out.println("Verifying Distances are no larger than parameter r=" + r);
			int n = dist.length;
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					if (dist[i][j] * r < dist2[i][j])
						System.out.println("Found ERROR! spanner doesnt hold the requirements! " + i + "->" + j);

			System.out.print("Finding MST...");
			mst = mstMaker.MST_Prim(graph);
			System.out.println("Done.");
			System.out.print("Running Floyd-Marshell on MST...");
			dist3 = FloydWarshall.Do(mst);
			System.out.println("Done.");

			System.out.print("Checking that MST is contained in r-spanner...");
			if (spanner.edges.containsAll(mst.edges))
				System.out.println("True!");
			else System.out.println("False!");
		}
		
		System.out.print("\nOriginal Graph==>\t\tEdges: " + graph.edges.size());
		System.out.print("\tVertices: "+graph.getGraphSize());
		System.out.println("\tTotal Weight: "+graph.getWeight());
		System.out.print("r-spanner Graph==>\t\tEdges: " + spanner.edges.size());
		System.out.print("\tVertices: "+spanner.getGraphSize());
		System.out.println("\tTotal Weight: "+spanner.getWeight());
		if (docheck) {
			System.out.print("Minimum Spaning Tree==>\t\tEdges: " + mst.edges.size());
			System.out.print("\tVertices: "+mst.getGraphSize());
			System.out.println("\tTotal Weight: "+mst.getWeight());
		}
		if (docheck) System.out.println("\nAverge Distance:\n \t\tOrig:"+FloydWarshall.avergeShortestDistance(dist) +
			"\n\t\tr-spanner:"+FloydWarshall.avergeShortestDistance(dist2) + 
			"\n\t\tMST:"+FloydWarshall.avergeShortestDistance(dist3));
		
		System.out.println("Finished!");
		System.out.println("==================\n");
		
		System.out.print("Enter graphname to output the graph. Empty string means no output.\ngraph name:");
		Scanner scanIn = new Scanner(System.in);
	    filename = scanIn.nextLine();
	    scanIn.close(); 
	    if (filename.length()>0) {
	    	graph.toCSV(filename +"_edges.csv", filename +"_nodes.csv");
	    	spanner.toCSV(filename +"_"+r +"spanner_edges.csv", null);
	    	if (docheck) mst.toCSV(filename +"_"+"MST_edges.csv", null);
	    }
		

	}

	private static void usage() {
		System.out.println("Usage: run [input filename] [r] [do check 0\\1]");
		System.exit(-1);
	}

}
