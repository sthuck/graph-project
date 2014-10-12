package tests;

import java.io.FileWriter;

import dstructures.Edge;
import dstructures.Graph;
import dstructures.PlanarGraph;


public class Test3 {

	public static final String[] graphNames = {"planar/100nodes.graph","planar/1000nodes.graph","planar/5000nodes.graph"}; 
	public static void main(String[] args) throws Exception {
		FileWriter wo = new FileWriter("output.txt");
		wo.write("|V|,stretch-factor,Size(G'),Weight(G'),Weight(mst)\n");
		
		for (String filename : graphNames) {
				PlanarGraph g = (PlanarGraph) Graph.ReadGraph(filename);
				g.makeEucleadWeights();
				double mst_weight = algo.mstMaker.MST_Prim(g).getWeight();
				Graph oldspanner = g;
				for (int i=3;i<8;i=i+2) {
					Graph spanner = algo.spannerMaker.MakeRSpanner(oldspanner, i);
					wo.write(g.getGraphSize()+","+i+","+spanner.edges.size()+","+spanner.getWeight()+","+mst_weight+"\n");
					oldspanner=spanner;
			}
		}
		wo.close();
	}
	
}
