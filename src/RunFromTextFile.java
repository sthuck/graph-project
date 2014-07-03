
public class RunFromTextFile {

    public static void main(String[] args) {
	if (args.length < 4)
	    usage();
	int n = Integer.parseInt(args[0]);
	String filename = args[1];
	int r = Integer.parseInt(args[2]);
	
	System.out.println("Loading the Graph");
	Graph graph = Graph.FromMatrix(n, filename);
	System.out.println("Pringing the Graph");
	System.out.println(graph.toString());
	
	Graph spanner = graph.MakeRSpanner(r);
	spanner.toCSV(args[3]+"-edges.csv", args[3]+"-nodes.csv");
	
    }
    
    
    private static void usage() {
	System.out.println("Usage: run [n] [filename] [r] [outputname]");
	System.exit(-1);
    }

}
