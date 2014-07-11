package algo;

import dstructures.Edge;
import dstructures.Graph;

public class FloydWarshall {

	public static double[][] Do(Graph g) {
		int n = g.vertexes.size();
		double[][] dist = new double[n][n];
		for (int i=0;i<n;i++) 
			for (int j=0;j<n;j++) {
				if (i==j)
					dist[i][j]=0;
				else
					dist[i][j]=Double.POSITIVE_INFINITY;
			}
		for (Edge e : g.edges) {
			dist[e.source.id][e.destination.id] = e.weight;
			dist[e.destination.id][e.source.id] = e.weight;
		}

		for (int k = 0; k < n; k++)
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					if (dist[i][j] > dist[i][k] + dist[k][j])
						dist[i][j] = dist[i][k] + dist[k][j];

		return dist;
	}
	public static double avergeShortestDistance(double[][] distanceMat) {
		double sum=0;
		int n = distanceMat.length;
		int total=0;
		for (int i=0;i<n;i++) {
			for (int j=0;j<i;j++) {
				sum+=distanceMat[i][j];
				total++;
			}
		}
		return sum/total;
	}
}
