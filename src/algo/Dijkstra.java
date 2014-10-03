package algo;
import java.util.Comparator;
import java.util.PriorityQueue;

import dstructures.Edge;
import dstructures.Graph;
import dstructures.Vertex;

//TODO ����� �� ��� ���� ����� ������� ��� ��� �����

public class Dijkstra {

	private class CompareByMindist implements Comparator<Vertex> {
		private Double dist[];

		public CompareByMindist(Double[] dist) {
			super();
			this.dist = dist;
		}

		@Override
		public int compare(Vertex arg0, Vertex arg1) {
			int res = dist[arg0.id].compareTo(dist[arg1.id]);
			if (res==0) return -1;
			return res; 
			//why the -1?
			//Answer: it's an ugly hack to solve adding an existing element to the priority queue
		}

	}

	Graph g;
	Vertex source;
	public Double dist[];
	public Vertex previous[];
	CompareByMindist mindist;
	PriorityQueue<Vertex> vertexQueue;

	public Dijkstra(Graph g) {
		super();
		this.g = g;
		dist = new Double[g.vertexes.size()];
		previous = new Vertex[g.vertexes.size()];
		mindist = new CompareByMindist(dist);
		vertexQueue = new PriorityQueue<Vertex>(g.vertexes.size(), mindist);
	}

	public void Do(Vertex source) {
		vertexQueue.clear();
		Double alt;
		boolean visited[] = new boolean[this.g.getGraphSize()];
		dist[source.id] = 0D;
		for (Vertex v : g.vertexes) {
			if (v != source) {
				dist[v.id] = Double.POSITIVE_INFINITY;
				previous[v.id] = null;
			}
			else vertexQueue.add(v);
		}

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();
			if (visited[u.id])
				continue;
			visited[u.id]=true;
			for (Edge e : g.adjencies.get(u.id)) {
				Vertex v = (u!=e.destination) ? e.destination : e.source; //undirected graph, edges goes both ways
				alt =  dist[u.id] + e.weight;
				if (alt < dist[v.id]) {
					dist[v.id] = alt;
					previous[v.id] = u;
					vertexQueue.add(v);
				}
			}
		}
	}

}
