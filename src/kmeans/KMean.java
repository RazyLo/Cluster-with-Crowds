package kmeans;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.amazonaws.mturk.requester.Assignment;
import mTurk.ImageSurvey;

/*
 *This Class is main class to achieve Clustering computation 
 * It has 98 percent to calculate the distance by computer 
 * and 2 persent possibility using Amazon Mechanical Turk service to ask workers to ask the questions
 * */
public class KMean {
	// k is the cluster number
	private int k;
	// path is the soucrFile
	private String path;
	// clusterContainer is used to store the clusters
	private ArrayList<Cluster> clusterContainer = new ArrayList<Cluster>();
	// getPoints is a container to store the data source
	private ArrayList<Image> getPoints = new ArrayList<Image>();
	// scanner is using for get user input
	private Scanner scanner;
	
	
	public KMean()
	{
		
	}

	// Seperate images into k clusters
	public KMean(int k, String filePath)
	{
		this.k = k;
		this.path = filePath;
		ReadImageData reader = new ReadImageData();
		reader.ReadImageDate(filePath);
		reader.initializeImage();;
		getPoints = reader.getImages();
	}
	
	/*
	 * Entry of clustering algorithm
	 * It will call two functions 
	 * */
	public void doCluster() throws IOException
	{
		int clusterId = 0;
		// RandomCenter is used to initialize the cluster center
		Random randomCenter = new Random();
		// store the cluster ID to avoid duplicated cluster ID
		ArrayList<Integer> sample = new ArrayList<Integer>();
		if(k < getPoints.size())
		{
			for(int i=0;i<k;i++)
			{
				while(sample.size() != k)
				{
					int centerPointIndex = randomCenter.nextInt(getPoints.size());
					if(!sample.contains(centerPointIndex))
					{
						Cluster tempCluster = new Cluster(clusterId, getPoints.get(centerPointIndex));
						clusterContainer.add(tempCluster);
						sample.add(centerPointIndex);
						clusterId++;
					}
				}
			}
			startCluster();
			clustering();
		}
		else
		{
			throw new IllegalStateException("Error! Too many clusters...");
		}		
	}
	
	// First time of clustering
	private ArrayList<Cluster> startCluster() throws IOException
	{
		// Calculate distance for first time
		int index = -2;
		// for loop one
		for(int i=0; i<getPoints.size();i++)
		{
			ArrayList<Double> disContainer = new ArrayList<Double>();
			// for loop two
			for(int j=0; j<clusterContainer.size(); j++)
			{
				Random rand = new Random();
				int possibility = rand.nextInt(10000);
				//System.out.println("Possibility is " + possibility);
				if(possibility < 10001)
				{
					Distance dis = new Distance(clusterContainer.get(j).getCenterPoint(),getPoints.get(i));
					dis.caculateDis();
					disContainer.add(dis.getDistance());
				}
				else
				{
					System.out.println("Tell me how similar between these two images: url 1: "
							+ clusterContainer.get(j).getCenterPoint().getImageUrl() + "; url 2: "
							+ getPoints.get(i).getImageUrl() + ";");
					try
					{
						scanner = new Scanner(System.in);
						double read = scanner.nextDouble();
						double distance = (double)read;
						disContainer.add(distance);
						//System.out.println(disContainer.size());
					}
					catch(Exception ex)
					{
						System.out.println(ex.getMessage());
					}
				}
				
			}// end two

			double flag = 9999999;
			// for loop 3
			for(int m=0; m<disContainer.size();m++)
			{
				if( flag > disContainer.get(m) || flag == disContainer.get(m))
				{
					flag = disContainer.get(m);
					index = disContainer.indexOf(flag);
				}
			}//end 3
			disContainer.clear();
			clusterContainer.get(index).addMember(getPoints.get(i));
			index = -2;
			
		} //end loop one
		
	//Get the result of cluster after add distance
		System.out.println("Initilise the cluter: " );
		for(int s=0; s<clusterContainer.size(); s++)
		{
			System.out.println(clusterContainer.get(s).clusterToString());
		}
		System.out.println();
		return clusterContainer;
	}// End of Start Cluster()
	
	/*
	 * In this method, it has one options for user to get the result of similarity
	 * Option 1 will ask user to input the distance after showing the images in the url
	 * */
	private void clustering() throws IOException
	{

		int times = 1;
		boolean centerChanged = true;
		ArrayList<Image> centerContainer = new ArrayList<Image>();
		// add the center in cluster container to a specified container
		for(Cluster cluster : clusterContainer)
		{
			centerContainer.add(cluster.getCenterPoint());
		}

		while(centerChanged)
		{
			ArrayList<Image> virtualCentersContainer = new ArrayList<Image>();
			ArrayList<Image> pointContainer = new ArrayList<Image>();
			ArrayList<Image> tempCenterContainer = new ArrayList<Image>();
			for(int j = 0; j<clusterContainer.size();j++)
			{
				clusterContainer.get(j).updateCluterCenter();
				virtualCentersContainer.add(clusterContainer.get(j).getVirtualCenter());
				System.out.println("Cluter " + j + " center is :" + clusterContainer.get(j).getCenterPoint().ImageFeatureToString());
			}
		
			for(int test = 0 ; test<virtualCentersContainer.size(); test++)
			{
				System.out.println("Cluster " + test + " Centroid is : \n" + virtualCentersContainer.get(test).ImageToString());
			}
			// put all cluster members into container
			for(int i=0; i<clusterContainer.size();i++)
			{
				for(int e=0;e<clusterContainer.get(i).getClusterMembers().size();e++)
				{
					pointContainer.add(clusterContainer.get(i).getClusterMembers().get(e));
				}
			}
			// clear all members 
			for(int y=0;y<clusterContainer.size();y++)
			{
				clusterContainer.get(y).clearAllMember();
			}
		
		// start to calculate the distance 
			int index = -2;
		// for loop one
			for(int i=0; i<pointContainer.size();i++)
			{
				ArrayList<Double> disContainer = new ArrayList<Double>();
			// for loop two
				
				for(int j=0; j<clusterContainer.size(); j++)
				{
					Random rand = new Random();
					int possibility = rand.nextInt(10000);
					if(possibility < 9800)
					{
						Distance dis = new Distance(clusterContainer.get(j).getCenterPoint(),pointContainer.get(i));
						dis.caculateDis();
						disContainer.add(dis.getDistance());
					}
					else
					{
						
						// Trying to get result from the Turk 
						ImageSurvey survey = new ImageSurvey(virtualCentersContainer.get(j).getImageUrl(), pointContainer.get(i).getImageUrl());
						try
						{
							System.out.println("Please wait! Creating Survey.... ! \n\n");
							survey.createImageSurvey();
							System.out.println("Please wait! Getting the results from Turk.... ! \n\n");
							int timeLeft = 60;
							for(int time = 0; time < timeLeft; time++)
							{
								System.out.println(timeLeft + "s has left....");
								Thread.sleep(5000);
								int cons = 5;
								timeLeft = timeLeft - cons;
							}
						}
						catch(InterruptedException  ex)
						{
							System.out.println(ex.getMessage());
						}
						//get rusult from the turk 
						String result = survey.getHitResult();
						if(!result.isEmpty())
						{
							System.out.println("The results is " + result);
							double dis = Double.parseDouble(result);
							disContainer.add(dis);
						}
						//If no workers accept the hit, it will go on clustring in two options
						else
						{
						//Option 1: Ask user to input the distance if there is no answer
							survey.answerSurveyManually();
							System.out.println("Tell me how similar between those two images ");
							try
							{
								scanner = new Scanner(System.in);
								double read = scanner.nextDouble();
								double distance = (double)read;
								disContainer.add(distance);
							}
							catch(Exception e)
							{
								System.out.println(e.getMessage());
							}
						}
					}
				}// end two
				double flag = 9999999;
			// for loop 3
				for(int m=0; m<disContainer.size();m++)
				{
					if( flag > disContainer.get(m) || flag == disContainer.get(m))
					{
						flag = disContainer.get(m);
						index = disContainer.indexOf(flag);
					}
					
				}//end 3

				disContainer.clear();
				clusterContainer.get(index).addMember(pointContainer.get(i));
				index = -2;
			
			} //end loop one
		
			// give the new center from cluster to temp center container
			for(Cluster tempCluster : clusterContainer)
			{
				tempCenterContainer.add(tempCluster.getCenterPoint());
			}

			// judge the center has changed or not
			ArrayList<Double> test1 = new ArrayList<Double>();
			for(int t=0;t<centerContainer.size();t++)
			{
				Distance testChange = new Distance(centerContainer.get(t), tempCenterContainer.get(t));
				testChange.caculateDis();
				test1.add(testChange.getDistance());
				//System.out.println(t + " : " +test1.get(t));
			}
			
			int testFlag = 0;
			for(int z=0; z<test1.size();z++)
			{
				if(test1.get(z) == 0)
				{
					testFlag++;
				}
			}
			
			if(testFlag != test1.size())
			{
				
				centerChanged = true;
				centerContainer.clear();
				for(Image tempCenter : tempCenterContainer)
				{
					centerContainer.add(tempCenter);
				}
				
				System.out.println("\nClustering for " + times + " times; ");
				showCurrentCluster();
				System.out.println("Center changed: " + centerChanged);
			}
			else
			{
				
				centerChanged =false;
				System.out.println("\nClustering for " + times + " times; ");
				showCurrentCluster();
				System.out.println( "Center changed: " + centerChanged);
			}
				times++;		
				
		} // end while
		System.out.println();
		result();
	}// end clustering()
	
	public void showCurrentCluster()
	{
		System.out.println("The Current Cluster is : ");
		for(int index = 0; index < clusterContainer.size(); index++)
		{
			System.out.println("\t\t\t" + clusterContainer.get(index).clusterToString() + "\n");
		}
		
	}
	
	
	public void result()
	{
		if(clusterContainer.size()>0)
		{
			System.out.println("The final cluster is: ");
			for(int i=0; i<clusterContainer.size();i++)
			{
				System.out.println("\t\t\t" + clusterContainer.get(i).clusterToString());
			}
		}
		else
		{
			throw new IllegalStateException("cluster has not been initilized! ");
		}
	}
	
}
