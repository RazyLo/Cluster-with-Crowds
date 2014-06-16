package Start;

import java.io.IOException;

import kmeans.KMean;

public class Start {
	//private static ArrayList<String> imageUrls;
	public static void main(String[] args) throws IOException
	{
		KMean start = new KMean(2,"G:\\data.txt");
		start.doCluster();
	}



}