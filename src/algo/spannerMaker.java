package algo;

import java.util.Collections;
import dstructures.Edge;
import dstructures.Graph;
import algo.Dijkstra;

public class spannerMaker {
	
	public static Graph MakeRSpanner(Graph g, int r) {
		Graph res = g.newCopyEmptyGraph();
		Dijkstra d = new Dijkstra(res);
		double r2;
		if (r == -1)
			r2 = Double.POSITIVE_INFINITY;
		else r2 = r;
		int n = res.getGraphSize();
		
		Double cache[][] = new Double[n][n];
		boolean cache_valid[] = new boolean[n];
		
		int i = 0;
		int j = 0;
		String s = "Spanner: Edge %d/" + g.edges.size() + "\n";

		Collections.sort(g.edges);
		for (Edge e : g.edges) {
			if (++i % 1000 == 0)
				System.out.printf(s, i);
			int id = e.source.id;
			double shortestpath;
			
			if (cache_valid[id]) {
				shortestpath = cache[id][e.destination.id];
				//if (++j % 50 == 0)
					//System.out.println("Saved "+j+" func calls" );
			}
			else {
				d.Do(res.vertexes.get(id));
				shortestpath = d.dist[e.destination.id];
				System.arraycopy(d.dist, 0, cache[id], 0, n);
				cache_valid[id]=true;                   	   //next time we won't run dijkstra again
			}
			if ((r2 * e.weight) <= shortestpath) {
				res.addEdge(e);
				java.util.Arrays.fill(cache_valid,false);      //we added a new edge, all previous results are invalid
			}
		}
		return res;
	}

}
