package kmeans;

import java.io.IOException;

/*
 * This class is used to calculate the distance between source image and 
 * destination image
 * */
public class Distance {
	
	private Image centerImage;
	private Image sourceImage;
	private double distance;
	
	public Distance()
	{
		
	}

	//Two images is required, the central image and the sourceImage
	public Distance(Image centerPoint,Image sourcePoint)
	{
		this.centerImage = centerPoint;
		this.sourceImage = sourcePoint;
	}
	

	//Calculate the distance between two images
	public void caculateDis() throws IOException{
		double tempDis = 0;
		for(int i=0;i<centerImage.getImageFeatureSize();i++)
		{
			double tempX = 0;
			tempX = (centerImage.getImageFeature().get(i) - sourceImage.getImageFeature().get(i));
			tempDis += (Math.pow(tempX, 2));
		}
			distance =Math.sqrt(tempDis);
	}
	
	public double getDistance()
	{
		return distance;
	}
	
	public String disToString()
	{
		String temp = String.valueOf(distance);
		return temp;
	}
}
