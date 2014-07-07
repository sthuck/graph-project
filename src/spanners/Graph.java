package spanners;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Graph implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = -5712624205004515044L;
	private final int n;
	public final List<Vertex> vertexes;
	public final List<Edge> edges;
	public List<List<Edge>> adjencies;

	static public Graph createRandomGraph(int n, double prob, int max_weight) {
		Random rand = new Random();
		Graph g = newEmptyGraph(n);
		for (int i = 0; i < n; i++)
			for (int j = i + 1; j < n; j++) {
				if (i != j && Math.random() < prob) {
					int weight = rand.nextInt(max_weight) + 1;
					g.addEdge(i, j, weight);
				}
			}
		return g;
	}

	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
		this.n = vertexes.size();
		this.adjencies = new ArrayList<List<Edge>>(this.n);
		for (int i = 0; i < n; i++) {
			List<Edge> adj = new LinkedList<Edge>();
			this.adjencies.add(adj);
		}
	}

	/**
	 * Returns a new empty graph
	 * 
	 * @param n
	 * @return
	 */
	static public Graph newEmptyGraph(int n) {
		List<Vertex> vertices = new ArrayList<Vertex>(n);
		for (int i = 0; i < n; i++)
			vertices.add(new Vertex(i));
		List<Edge> edges = new ArrayList<Edge>();
		return new Graph(vertices, edges);
	}

	public int getGraphSize() {
		return this.n;
	}

	public List<Vertex> getVertexes() {
		return vertexes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public double[][] toMatrix() {
		double[][] ans = new double[vertexes.size()][vertexes.size()];
		int n = edges.size();
		for (int i = 0; i < n; i++) {
			Edge e = edges.get(i);
			ans[e.source.id][e.destination.id] = e.weight;
			ans[e.destination.id][e.source.id] = e.weight;
		}
		return ans;
	}

	public static Graph FromMatrix(int size, String filename) {
		Graph res = new Graph(new ArrayList<Vertex>(), new ArrayList<Edge>());
		for (int i = 0; i < size; i++)
			res.vertexes.add(new Vertex(i));

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			for (int i = 0; i < size; i++) {
				String line[] = reader.readLine().split("  ");
				assert (line.length == size);
				for (int j = 0; j < i; j++) {
					int weight = Integer.parseInt(line[j]);
					if (weight != 0)
						res.addEdge(i, j, weight);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return res;
	}

	/**
	 * BFS search according to wikipedia
	 * 
	 */
	public List<Set<Vertex>> getConnectedElements() {
		List<Set<Vertex>> res = new LinkedList<>();
		Set<Vertex> all = new HashSet<>();
		for (Vertex v : this.vertexes)
			all.add(v);

		while (!all.isEmpty()) {
			Set<Vertex> V = new HashSet<>();
			Queue<Vertex> Q = new LinkedList<>();
			Vertex v = all.iterator().next();
			Q.add(v);
			V.add(v);
			all.remove(v);
			while (!Q.isEmpty()) {
				Vertex t = Q.poll();
				for (Edge e : this.adjencies.get(t.id)) {
					Vertex u = (t != e.destination) ? e.destination : e.source; // undirected
																				// graph,
																				// edges
																				// goes
																				// both
																				// ways
					if (!V.contains(u)) {
						V.add(u);
						all.remove(u);
						Q.add(u);
					}
				}
			}
			res.add(V);
		}

		return res;
	}

	public void toCSV(String filename, String filename_nodes) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write("Source,Target,Type,Id,Label,Weight\n");
			int i = 0;
			for (Edge e : this.edges) {
				if (e.destination.id > e.source.id) // undirected graph
					continue;
				writer.write("" + e.source.id);
				writer.write("," + e.destination.id);
				writer.write(",Undirected");
				writer.write("," + i++);
				writer.write("," + e.id + " w:" + e.weight); // label
				writer.write("," + e.weight);
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (filename_nodes!=null) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(filename_nodes));
				writer.write("Id,Label\n");
				for (Vertex v : this.vertexes) {
					writer.write("" + v.id);
					writer.write("," + v.id);
					writer.write("\n");
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Finding connected sets New edge between each set
	 */
	public void MakeGraphConnected() {
		List<Set<Vertex>> elements = this.getConnectedElements();
		if (elements.size() > 1) {
			System.out.println("Graph isn't connected. Adding Edges...");
			System.out.println(elements);
		}
		for (int i = 0; i < elements.size() - 1; i++) {
			int id1 = elements.get(i).iterator().next().id;
			int id2 = elements.get(i + 1).iterator().next().id;
			this.addEdge(id1, id2, 1);
		}
	}

	public Graph MakeRSpanner(int r) {
		Graph res = new Graph(this.vertexes, new ArrayList<Edge>());
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
		String s = "Spanner: Edge %d/" + this.edges.size() + "\n";

		Collections.sort(this.edges);
		for (Edge e : this.edges) {
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

	@Override
	public String toString() {
		double[][] mat = this.toMatrix();
		StringBuilder res = new StringBuilder(this.vertexes.size() * (this.vertexes.size() * 5 + 2));
		int n = mat.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				res.append(mat[i][j]).append("  ");
			}
			res.append("\n");
		}
		return res.toString();
	}

	public void SaveGraph(String filename) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			GZIPOutputStream gos = new GZIPOutputStream(fos);
			out = new ObjectOutputStream(gos);
			out.writeObject(this);

			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static public Graph ReadGraph(String filename) {
		Graph g = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filename);
			GZIPInputStream gs = new GZIPInputStream(fis);
			in = new ObjectInputStream(gs);
			g = (Graph) in.readObject();
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// g.fixAdjencies();
		return g;
	}

	/**
	 * We save Vertex without adjency list, to avoid infinite recursion
	 *
	 * private void fixAdjencies() { for (Vertex v : vertexes) v.adjencies = new
	 * ArrayList<Edge>(); for (Edge e : edges) { e.source.adjencies.add(e); } }
	 */
	public Graph MST_Prim() {
		Graph g = new Graph(this.vertexes, new ArrayList<Edge>());

		Set<Vertex> set = new HashSet<>();
		set.add(this.vertexes.get(0));

		Collections.sort(this.edges);
		while (set.size() != g.getGraphSize()) {
			for (Edge e : this.edges) {
				if (set.contains(e.source) && !set.contains(e.destination)) {
					set.add(e.destination);
					g.addEdge(e);
					break;
				} else if (set.contains(e.destination) && !set.contains(e.source)) {   //undirected graph, edges can go both ways
					set.add(e.source);
					g.addEdge(e);
					break;
				}
			}
		}
		return g;
	}

	public void addEdge(int source_id, int destination_id, double weight) {

		Edge e = new Edge(source_id + "->" + destination_id, this.vertexes.get(source_id), vertexes.get(destination_id), weight);
		edges.add(e);
		this.adjencies.get(source_id).add(e);
		this.adjencies.get(destination_id).add(e);
	}

	public void addEdge(Edge e) {
		this.edges.add(e);
		this.adjencies.get(e.source.id).add(e);
		this.adjencies.get(e.destination.id).add(e);
	}

	public double[][] Floyd_Warshall() {
		double[][] dist = new double[vertexes.size()][vertexes.size()];
		for (Edge e : edges)
			dist[e.source.id][e.destination.id] = e.weight;

		int n = this.vertexes.size();
		for (int k = 0; k < n; k++)
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					if (dist[i][j] > dist[i][k] + dist[k][j])
						dist[i][j] = dist[i][k] + dist[k][j];

		return dist;
	}
}