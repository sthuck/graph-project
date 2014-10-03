package algo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import dstructures.Edge;
import dstructures.Graph;
import dstructures.Vertex;

public class mstMaker {
	public static Graph MST_Prim(Graph g) {
		Graph res = g.newCopyEmptyGraph();

		Set<Vertex> set = new HashSet<Vertex>();
		set.add(g.vertexes.get(0));

		Collections.sort(g.edges);
		while (set.size() != res.getGraphSize()) {
			for (Edge e : g.edges) {
				if (set.contains(e.source) && !set.contains(e.destination)) {
					set.add(e.destination);
					res.addEdge(e);
					break;
				} else if (set.contains(e.destination) && !set.contains(e.source)) {   //undirected graph, edges can go both ways
					set.add(e.source);
					res.addEdge(e);
					break;
				}
			}
		}
		return res;
	}
}
