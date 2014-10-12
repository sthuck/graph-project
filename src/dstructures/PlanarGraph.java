package dstructures;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import megamu.mesh.Voronoi;

import org.w3c.dom.Element;




public class PlanarGraph extends Graph {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7324691511039253181L;
	private List<Pair<Float>> vertex_pos;
	
	public PlanarGraph(List<Vertex> vertexes, List<Edge> edges) {
		super(vertexes, edges);
		int n = vertexes.size();
		vertex_pos = new ArrayList<Pair<Float>>(n);
		for (int i=0;i<n;i++)
			vertex_pos.add(null);
	}
	
	static public PlanarGraph newEmptyGraph(int n) {
		List<Vertex> vertices = new ArrayList<Vertex>(n);
		for (int i = 0; i < n; i++)
			vertices.add(new Vertex(i));
		List<Edge> edges = new ArrayList<Edge>();
		return new PlanarGraph(vertices, edges);
	}
	
	public Graph newCopyEmptyGraph() {
		PlanarGraph res = new PlanarGraph(this.vertexes, new ArrayList<Edge>());
		res.vertex_pos = this.vertex_pos;
		return res;
	}
	
	
	public void updateVertexPosition(Pair<Float> v, int id) {
		vertex_pos.set(id, v);
	}
	
	protected void nodesToCSV(String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write("Id,Label,X,Y\n");
			int i=0;
			for (Vertex v : this.vertexes) {
				writer.write("" + v.id);
				writer.write("," + v.id);
				Pair<Float> v_pos = vertex_pos.get(i++); 
				writer.write("," + v_pos.getX());
				writer.write("," + v_pos.getY());
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PlanarGraph Generate(int n, int weight) {
		Voronoi myVoronoi = null;
		Random r = new Random(System.currentTimeMillis());
		float[][] points = new float[n][2];

		for (int i = 0; i < n; i++) {
			points[i][0] = (1000F * r.nextFloat() + r.nextInt(1000)) / 1;
			points[i][1] = (1000F * r.nextFloat() + r.nextInt(2000)) / 1;
		}
		///
		myVoronoi = new Voronoi(points);
		///
		
		//finding out how many vertices we have
		float[][] edges = myVoronoi.getEdges();
		HashMap<Pair<Float>, Integer> vertexMap = new HashMap<Pair<Float>, Integer>();

		
		int id=0;
		for (int j=0;j<edges.length;j++) {
			if (edges[j][0] > 2000 || edges[j][1] > 3000 || edges[j][2] > 2000
					|| edges[j][3] > 3000 || edges[j][0] < 0 || edges[j][1] < 0
					|| edges[j][2] < 0 || edges[j][3] < 0)
				continue;
			
			Pair<Float> v1 = new Pair<Float>(edges[j][0], edges[j][1]);
			if (!vertexMap.containsKey(v1))
				vertexMap.put(v1, id++);
			Pair<Float> v2 = new Pair<Float>(edges[j][2], edges[j][3]);
			if (!vertexMap.containsKey(v2))
				vertexMap.put(v2, id++);
		}
		PlanarGraph res = PlanarGraph.newEmptyGraph(vertexMap.size());
		
		//updates locations
		for ( Entry<Pair<Float>,Integer> entry : vertexMap.entrySet()) 
			res.updateVertexPosition(entry.getKey(),entry.getValue());
		
		
		for (int j = 0; j < edges.length; j++) {
			int id1,id2;
			Pair<Float> v1 = new Pair<Float>(edges[j][0], edges[j][1]);
			if (vertexMap.containsKey(v1))
				id1 = vertexMap.get(v1);
			else continue;
			Pair<Float> v2 = new Pair<Float>(edges[j][2], edges[j][3]);
			if (vertexMap.containsKey(v2))
				id2 = vertexMap.get(v2);
			else continue;
			res.addEdge(id1, id2, r.nextInt(weight)+1);
		}
		
		return res;
	}


	public static PlanarGraph importYED(InputStream is,int randrange) {
		Random rand = new Random();
		PlanarGraph res = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("node");
			NodeList nList2 = doc.getElementsByTagName("edge");
			res = PlanarGraph.newEmptyGraph(nList.getLength());
			for (int i=0;i<nList.getLength();i++) {
				Element eElement = (Element) nList.item(i);
				Float x = Float.parseFloat(eElement.getElementsByTagName("y:Geometry").item(0).getAttributes().getNamedItem("x").getNodeValue());
				Float y = Float.parseFloat(eElement.getElementsByTagName("y:Geometry").item(0).getAttributes().getNamedItem("y").getNodeValue());
				res.updateVertexPosition(new Pair<Float>(x,y), i);
			}
			
			for (int i=0;i<nList2.getLength();i++) {
				Element eElement = (Element) nList2.item(i);
				int source = Integer.parseInt(eElement.getAttribute("source").substring(1));
				int target = Integer.parseInt(eElement.getAttribute("target").substring(1));
				res.addEdge(source, target, rand.nextInt(randrange)+1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return res;
	}
	
	public void makeEucleadWeights() {
		for (Edge e : edges) {
			Pair<Float>v1pos = vertex_pos.get(e.destination.id);
			Pair<Float>v2pos = vertex_pos.get(e.source.id);
			double weight = Math.sqrt(Math.pow((v1pos.x-v2pos.x),2)+Math.pow((v1pos.y-v2pos.y),2));
			e.weight=weight;
		}
	}
}
