package Start;

import java.io.IOException;

import kmeans.KMean;

public class Start {
	//Entry of this application
	public static void main(String[] args) throws IOException
	{
		KMean start = new KMean(2,"G:\\data.txt");
		start.doCluster();
	}



}
