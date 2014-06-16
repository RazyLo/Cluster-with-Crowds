package kmeans;

import java.util.ArrayList;

/*
 * This Class is used to intialize the images features 
 * 
 * */
public class Image {
	private ArrayList<Double> feature;
	private String imageURL;
	private int id;
	
	public Image()
	{
		
	}
	
	//Give each image a unique id
	public void setImage(int id, String Url, ArrayList<Double> features)
	{
		this.id = id;
		this.imageURL = Url;
		this.feature = features;
	}
	
	public void setCenterImage(ArrayList<Double> image)
	{
		this.feature = image;
	}
	
	public ArrayList<Double> getImageFeature()
	{
		return this.feature;
	}
	
	public int getImageId()
	{
		return this.id;
	}
	
	public String getImageUrl()
	{
		return this.imageURL;
	}
	
	public int getImageFeatureSize(){
		int size = 0;
		if(feature!= null)
		{
			size = feature.size();
			return size;
		}
		else
		{
			return 0;
		}
	}
	
	public String ImageToString()
	{
		String ImageFeature = "";
		String seperator = "  ";
		for(int i = 0; i <  feature.size(); i++)
		{
			String temp = "";
			temp = feature.get(i).toString() + seperator;
			ImageFeature += temp ;
		}
		return "Image ID : " + this.id + " ;"
			  + " Image Feature : (" +ImageFeature + ")" + " ;"
			  + " Image Url : " + this.imageURL;
	}
	
	public String ImageFeatureToString()
	{
		String pointContent = "";
		String seperator = "  ";
		for(int j=0;j<feature.size();j++){
			String temp = "";
			temp = feature.get(j).toString() + seperator;
			pointContent += temp ;
		}
		return "( " + pointContent + ")";
	}
}
