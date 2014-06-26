import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Graph {
	public final List<Vertex> vertexes;
	public final List<Edge> edges;

	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public List<Vertex> getVertexes() {
		return vertexes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public int[][] toMatrix() {
		int[][] ans = new int[vertexes.size()][vertexes.size()];
		int n = edges.size();
		for (int i = 0; i < n; i++) {
			Edge e = edges.get(i);
			ans[e.source.id][e.destination.id] = e.weight;
		}
		return ans;
	}

	public static Graph FromMatrix(int size,String filename) {
		Graph res = new Graph(new ArrayList<Vertex>(),new ArrayList<Edge>());
		for (int i=0;i<size;i++)
			res.vertexes.add(new Vertex(i));
		
		try {
			BufferedReader  reader =new BufferedReader (new FileReader(filename));
			for (int i = 0; i < size ; i++) {
				String line[] = reader.readLine().split("  ");
				assert(line.length==size);
				for (int j = 0; j<size;j++ ) {
					int weight = Integer.parseInt(line[j]);
					if (weight!=0) {
						Edge e = new Edge(i+"->"+j, res.vertexes.get(i), res.vertexes.get(j), weight);
						res.edges.add(e);
						res.vertexes.get(i).adjencies.add(e);
					}
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
	
	public Graph toUndirectedUnweighted() {
		Graph res = new Graph(new ArrayList<Vertex>(),new ArrayList<Edge>());
		int[][] oldMatrix = this.toMatrix();
		int len = oldMatrix.length;
		for (int i=0;i<len;i++)
			res.vertexes.add(new Vertex(i));
		
		int[][] newMatrix = new int[len][len];
		for (int i=0;i<len;i++)
			for (int j=0;j<len;j++) 
				if (oldMatrix[i][j]>0) {
					newMatrix[i][j]=1;
					newMatrix[j][i]=1;
				}
		for (int i=0;i<len;i++)
			for (int j=0;j<len;j++) 
				if (newMatrix[i][j]>0) {
					Edge e = new Edge(i+"->"+j, res.vertexes.get(i), res.vertexes.get(j), 1);
					res.edges.add(e);
					res.vertexes.get(i).adjencies.add(e);
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
				for (Edge e : t.adjencies) {
					Vertex u = e.destination;
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
			int i=0;
			for (Edge e : this.edges) {
				writer.write(""+e.source.id);
				writer.write(","+e.destination.id);
				writer.write(",Directed");
				writer.write(","+i++);
				writer.write(","+e.id+" w:"+e.weight);  //label
				writer.write(","+e.weight);
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename_nodes));
			writer.write("Id,Label\n");
			for (Vertex v : this.vertexes) {
				writer.write(""+v.id);
				writer.write(","+v.id);
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Finding connected sets 
	 * New edge between each set
	 */
	public void MakeGraphConnected() {
		Graph graph2 = this.toUndirectedUnweighted();
		List<Set<Vertex>> elements = graph2.getConnectedElements();
		if (elements.size()>1)
			System.out.println("Graph isn't connected. Adding Edges...");
		for (int i=0;i<elements.size()-1;i++) {
			int id1 = elements.get(i).iterator().next().id;
			int id2 = elements.get(i+1).iterator().next().id;
			Vertex v1 = this.vertexes.get(id1);
			Vertex v2 = this.vertexes.get(id2);
			Edge e = new Edge(id1+"->"+id2, v1, v2, 1);
			this.edges.add(e);
			v1.adjencies.add(e);
		}
	}
	
	public Graph MakeRSpanner(int r) {
		Graph res = new Graph(new ArrayList<Vertex>(),new ArrayList<Edge>());
		int len = this.vertexes.size();
		for (int i=0;i<len;i++)
			res.vertexes.add(new Vertex(i));
		Collections.sort(this.edges);
		for (Edge e : this.edges) {
			Dijkstra d = new Dijkstra(res, res.vertexes.get(e.source.id));
			d.Do();
			int shortestpath = d.dist[e.destination.id];
			if ((r*e.weight)<shortestpath) {
				int id1 = e.source.id;
				int id2 = e.destination.id;
				Edge e1 = new Edge(id1+"->"+id2, res.vertexes.get(id1), res.vertexes.get(id2), e.weight);
				res.edges.add(e1);
				res.vertexes.get(id1).adjencies.add(e1);
			}
		}
		return res;
	}
	
	@Override
	public String toString() {
		int[][] mat = this.toMatrix();
		StringBuilder res = new StringBuilder();
		int n = mat.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				res.append(mat[i][j]).append("  ");
			}
			res.append("\n");
		}
		return res.toString();
	}

}