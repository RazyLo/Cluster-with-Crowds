package kmeans;


import java.io.IOException;
import java.util.ArrayList;

import kmeans.Image;

public class Cluster {
	private int clusterID;
	private Image centerImage;
	private ArrayList<Image> clusterMembers;
	ArrayList<ArrayList<Double>> temp ;
	ArrayList<Double> tempDouble ;
	//private List<Point> members;
	
	public Cluster()
	{
		
	}
	
	public Cluster(int id, Image center)
	{
		this.clusterID = id;
		this.centerImage = center;
		clusterMembers = new ArrayList<Image>();
	}
	
	public Cluster(int id, Image center, ArrayList<Image> clusterMembers)
	{
		this.clusterID = id;
		this.centerImage = center;
		this.clusterMembers = clusterMembers;
		
	}
	
	public void setCluster(int id, Image center)
	{
		this.clusterID = id;
		this.centerImage = center;
	}
	
	
	
	public ArrayList<Image> getClusterMembers()
	{
		return clusterMembers;
	}
	
	public void addMember(Image member)
	{
		if(!clusterMembers.contains(member))
		//if(cluster != null)
		{
			clusterMembers.add(member);
		}
		else
		{
			throw new IllegalStateException("Try to add a same member!");
		}
		
	}
	
	public ArrayList<Image> clearAllMember()
	{
		if(clusterMembers != null)
		{
			clusterMembers.clear();
		}
		else
		{
			throw new IllegalStateException("Have not been initilised! ");
		}
		return clusterMembers;
	}
	
	public void setCenterImage(Image point)
	{
		this.centerImage = point;
	}
	
	public Image getCenterPoint()
	{
		return centerImage;
	}
	
	public int getClusterID()
	{
		return clusterID;
	}
	

	public void updateCluterCenter()
	{
		double tempSum;
		double sum;
		int index = 0;
		int flag;
		int sizeOfCluster = 0;
		int sizeOfPoint = 0;
		temp = new ArrayList<ArrayList<Double>>();
		
		if(clusterMembers.size()>0)
		{
			for(int i=0; i<clusterMembers.size();i++)
			{
				tempDouble	 = new ArrayList<Double>();
				for(int j=0;j<clusterMembers.get(i).getImageFeature().size();j++)
				{
				
					tempDouble.add(clusterMembers.get(i).getImageFeature().get(j));
				}
				temp.add(tempDouble);
		}
		
		//System.out.println("Cluster size : " + temp.size());

		sizeOfCluster = temp.size();
		sizeOfPoint = tempDouble.size();
		ArrayList<Double> newCenterPoint = new ArrayList<Double>();
		Image newPoint = new Image();;
		
		for(int a=0; a<sizeOfPoint;a++)
		{
			
			flag = 0;
			sum = 0;
			tempSum = 0;
			for(int j=0; j<sizeOfCluster;j++)
			{
				tempSum += temp.get(flag).get(index);
				sum = tempSum/sizeOfCluster;
				//System.out.print(temp.get(flag).get(index) + " ");
				flag++;
			}
				newCenterPoint.add(sum);
				index++;
		}
			newPoint.setCenterImage(newCenterPoint); 
			this.centerImage = newPoint;
			//System.out.println("newcenter is :" + center.pointToString());
			//return temp;
		}
	}
	
	public Image getVirtualCenter() throws IOException
	{
		Image virtualCenter = new Image();
		ArrayList<Double> disContainer = new ArrayList<Double>();
		double flag = 9999999.9;
		if(clusterMembers.size() > 0)
		{
			for(int i = 0; i<clusterMembers.size(); i++)
			{
				Distance dis = new Distance(centerImage, clusterMembers.get(i));
				dis.caculateDis();
				disContainer.add(dis.getDistance());
			}
			for(Double distance : disContainer)
			{
				if(distance < flag || distance == flag)
				{
					flag = distance;
				}
			}
				int index = disContainer.indexOf(flag);
				virtualCenter = clusterMembers.get(index);
		}
		else
		{
			throw new IllegalStateException("Error! One of the Cluster didn't have centroid...");
		}
		return virtualCenter;
	}
	
	public String clusterToString()
	{
		String temp = "";
		String members = " ";
		if(clusterMembers!= null)
		{
			if(clusterMembers.size() > 0)
			{
				for(int i=0;i<clusterMembers.size();i++)
				{
					members +="Image" + clusterMembers.get(i).getImageId() + clusterMembers.get(i).ImageFeatureToString() + "; ";
				}
					temp = "Cluster " + clusterID + ": " +"Center is " + centerImage.ImageFeatureToString()  +"\n\t\t\tMembers: " + members ; 
					//temp = "Cluster: " + clusterID + ", " +"Center "+ center.pointToString() +" Members size: " + cluster.size() ; 
			}
			if(clusterMembers.size() == 0)
			{
				temp = "Cluster: " + clusterID + ", " +"Center "+ centerImage.ImageFeatureToString()  +"\n\t\t\tMembers: " + "No members have been added" ; 
			}
		}
		else
		{
			throw new IllegalStateException("Try to add a same member!");
		}
			return temp;
	}
	
	
}
