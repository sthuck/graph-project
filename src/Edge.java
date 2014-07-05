import java.io.Serializable;

public class Edge implements Comparable<Edge>,Serializable {

    private static final long serialVersionUID = -92339852774952104L;
	public final String id;
	public final Vertex source;
	public final Vertex destination;
	public final double weight;

	public Edge(String id, Vertex source, Vertex destination, double weight) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	public String getId() {
		return id;
	}

	public Vertex getDestination() {
		return destination;
	}

	public Vertex getSource() {
		return source;
	}

	public double getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return source + "->" + destination;
	}

	@Override
	public int compareTo(Edge o) {
		return Double.compare(this.weight, o.weight);
	}

}