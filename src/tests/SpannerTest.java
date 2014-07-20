package tests;

import java.io.FileWriter;

import algo.FloydWarshall;
import dstructures.Graph;


public class SpannerTest {

	public static final int graphSize = 1000; 
	public static final double prob = 0.05;
	public static void main(String[] args) throws Exception {
		double[][] dist=null;
		FileWriter wo = new FileWriter("output.txt");
		wo.write("r-spanner,Edges,TotalWeight,AvrageDistance\n");
		
		Graph g = Graph.createRandomGraph(graphSize, prob, 30);
		dist = algo.FloydWarshall.Do(g);
		wo.write("orig,"+g.edges.size()+","+g.getWeight()+","+FloydWarshall.avergeShortestDistance(dist)+"\n");
		for (int i=1;i<10;i++) {
			Graph spanner = algo.spannerMaker.MakeRSpanner(g, i);
			dist = algo.FloydWarshall.Do(spanner);
			wo.write(i+","+spanner.edges.size()+","+spanner.getWeight()+","+FloydWarshall.avergeShortestDistance(dist)+"\n");
		}
		Graph mst = algo.mstMaker.MST_Prim(g);
		dist = algo.FloydWarshall.Do(mst);
		wo.write("mst,"+mst.edges.size()+","+mst.getWeight()+","+FloydWarshall.avergeShortestDistance(dist)+"\n");
		wo.close();
	}
	
}
