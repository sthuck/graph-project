package spanners;

public class RunFromTextFile {

    public static void main(String[] args) {
	if (args.length < 4)
	    usage();
	int n = Integer.parseInt(args[0]);
	String filename = args[1];
	int r = Integer.parseInt(args[2]);
	
	System.out.println("Loading the Graph");
	Graph graph = Graph.FromMatrix(n, filename);
	System.out.println("Running Floyd-Marshell");
	double dist[][] = graph.Floyd_Warshall();
	System.out.println("Generating Spanner");
	Graph spanner = graph.MakeRSpanner(r);
	//spanner.toCSV(args[3]+"-edges.csv", args[3]+"-nodes.csv");
	System.out.println("Running FloydMarshell on spanner");
	double dist2[][] = spanner.Floyd_Warshall();
	System.out.println("Verifying Distances are no larger than parameter r="+r);
	int size = dist.length;
	for (int i=0;i<size;i++) 
	    for (int j=0;j<size;j++)
		if (dist[i][j]*r<dist2[i][j])
		    System.out.println("Found ERROR! spanner doesnt hold the requirements! "+i+"->"+j);
	System.out.println("Number of edges in original Graph:"+graph.edges.size()/2);
	System.out.println("Number of edges in r-spanner Graph:"+spanner.edges.size()/2);
	System.out.println("Finished!");
	
	
    }
    
    
    private static void usage() {
	System.out.println("Usage: run [n] [filename] [r] [outputname]");
	System.exit(-1);
    }

}
