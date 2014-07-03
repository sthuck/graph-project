import java.util.Comparator;
import java.util.PriorityQueue;

//TODO ����� �� ��� ���� ����� ������� ��� ��� �����

public class Dijkstra {

    private class CompareByMindist implements Comparator<Vertex> {
	private Integer dist[];

	public CompareByMindist(Integer[] dist) {
	    super();
	    this.dist = dist;
	}

	@Override
	public int compare(Vertex arg0, Vertex arg1) {
	    return dist[arg0.id].compareTo(dist[arg1.id]);
	}

    }

    Graph g;
    Vertex source;
    public Integer dist[];
    public Vertex previous[];
    CompareByMindist mindist;
    PriorityQueue<Vertex> vertexQueue;

    public Dijkstra(Graph g) {
	super();
	this.g = g;
	dist = new Integer[g.vertexes.size()];
	previous = new Vertex[g.vertexes.size()];
	mindist = new CompareByMindist(dist);
	vertexQueue = new PriorityQueue<>(g.vertexes.size(), mindist);
    }

    public void Do(Vertex source) {
	vertexQueue.clear();
	int alt;
	dist[source.id] = 0;
	for (Vertex v : g.vertexes) {
	    if (v != source) {
		dist[v.id] = Integer.MAX_VALUE;
		previous[v.id] = null;
	    }
	    vertexQueue.add(v);
	}

	while (!vertexQueue.isEmpty()) {
	    Vertex u = vertexQueue.poll();
	    for (Edge e : u.adjencies) {
		Vertex v = e.destination;
		alt = (dist[u.id] == Integer.MAX_VALUE) ? Integer.MAX_VALUE : dist[u.id] + e.weight;
		if (alt < dist[v.id]) {
		    dist[v.id] = alt;
		    previous[v.id] = u;
		}
	    }
	}
    }

}
