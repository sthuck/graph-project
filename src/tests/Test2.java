package tests;

import dstructures.Graph;
import dstructures.PlanarGraph;


public class Test2 {

	public static final String[] graphNames = {"planar/k25.graph"}; 
	public static void main(String[] args) throws Exception {
		
		for (String filename : graphNames) {
				PlanarGraph g = (PlanarGraph) Graph.ReadGraph(filename);
				g.makeEucleadWeights();
				g.toCSV("k25_edges.csv", "k25_nodes.csv");
				PlanarGraph spanner = (PlanarGraph) algo.spannerMaker.MakeRSpanner(g, 3);
				spanner.toCSV("k25_spanner_edges.csv", "k25_spanner_nodes.csv");
		}
	}
	
}
