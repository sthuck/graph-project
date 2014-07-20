package tests;

import dstructures.Graph;

public class cacheTest {
public static final double graphDenisty = 0.08;
public static final int graphWeights = 40;
public static final int graphSpanner = 2;
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		int graphSize[] = {100,200,300,400,500,600,700,800,900,1000,1100,1200,1300,1400,1500,2000,2500};
		int callsSaved[] = new int[graphSize.length];
		long timeDiff[] = new long[graphSize.length];
		long cacheTime[] = new long[graphSize.length];
		long noCacheTime[] = new long[graphSize.length];
		
		for (int i=0;i<graphSize.length;i++) {
			long withCacheTime,withoutCacheTime;
			System.out.println("Doing Test "+i+" - with cache");
			Graph g = Graph.createRandomGraphDensity(graphSize[i], graphDenisty, graphWeights);
			long startTime = System.nanoTime();
			Graph withCache = algo.spannerMaker.MakeRSpanner(g, graphSpanner);
			withCacheTime = System.nanoTime()-startTime;
			cacheTime[i]=withCacheTime;
			withCache=null;
			callsSaved[i]=testData.tData.callsSaved;
			
			System.out.println("Doing Test "+i+" - without cache");
			startTime = System.nanoTime();
			testData.tData.doCache=false;
			Graph noCache = algo.spannerMaker.MakeRSpanner(g, graphSpanner);
			withoutCacheTime = System.nanoTime()-startTime;
			noCacheTime[i]=withoutCacheTime;
			timeDiff[i]=withoutCacheTime-withCacheTime;
			testData.tData.doCache=true;
			noCache=null;
		}
		
		System.out.println("\n=====================\n");
		System.out.println("Size,callsSaved,TimeSaved,cacheTime,noCacheTime");
		for (int i=0;i<graphSize.length;i++) 
			System.out.println(graphSize[i]+","+callsSaved[i]+","+timeDiff[i]+","+cacheTime[i]+","+noCacheTime[i]);
	}

}
