package kmeans;


import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

/*
 * This class will get the source data
 * The image data source has url and a data features corresponds to this url
 * */
public class ReadImageData {
	private ArrayList<Image> Image_Feature_Container;
	private Scanner reader;
		
	public void ReadImageDate(String filePath)
	{
		try 
		{
            		reader = new Scanner(new File(filePath));
        	}
		catch (Exception e) 
        	{
	            System.out.println("Could not locate the data file!");
        	}
	}
	
	//Get the images data source and store them into a container
	public void initializeImage()
	{
		Image_Feature_Container = new ArrayList<Image>();
		int id = 0;
			
		while(reader.hasNext()) 
        	{
			String url = "";
        		Image tempImage = new Image();
        		String[] tempStr = null;
        		tempStr = reader.nextLine().split(" ");
        		ArrayList<Double> featureData = new ArrayList<Double>();
	        	
		    	url = tempStr[0].toString();
		 		for(int i=1;i<tempStr.length;i++)
		  		{
		   			featureData.add(Double.parseDouble(tempStr[i]));
		 		}
		 	tempImage.setImage(id, url, featureData);
		 	Image_Feature_Container.add(tempImage);
		 	id++;
	    	}// END while loop
        		reader.close();
		}
		
	public ArrayList<Image> getImages()
	{
		return this.Image_Feature_Container;
	}
		
	public void showAllImges()
	{
		for(int j=0;j<Image_Feature_Container.size();j++)
		{
			System.out.println(this.Image_Feature_Container.get(j).ImageToString());
		}
	}
}
