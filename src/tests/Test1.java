package tests;

import java.io.FileWriter;

import algo.FloydWarshall;
import algo.mstMaker;
import dstructures.Graph;


public class Test1 {

	public static final int[] graphSize = {100,1000,2500,5000}; 
	public static final double[] prob = {0.03,0.25,0.85};
	public static void main(String[] args) throws Exception {
		FileWriter wo = new FileWriter("output.txt");
		wo.write("|V|,prob,stretch-factor,Size(G'),Weight(G'),Weight(mst)\n");
		
		for (double p : prob) {
			for (int size : graphSize) {
				Graph g = Graph.createRandomGraph(size, p, 100);
				g.MakeGraphConnected();
				//g.GaussianWeights(75, 10);
				//g.TotalyRandomWeights(100);
				double mst_weight = algo.mstMaker.MST_Prim(g).getWeight();
				Graph oldspanner = g;
				for (int i=2;i<7;i=i+2) {
					Graph spanner = algo.spannerMaker.MakeRSpanner(oldspanner, i);
					wo.write(size+","+p+","+i+","+spanner.edges.size()+","+spanner.getWeight()+","+mst_weight+"\n");
					oldspanner=spanner;
				}
			}
		}
		wo.close();
	}
	
}
