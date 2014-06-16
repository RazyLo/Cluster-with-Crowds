package kmeans;

import java.io.IOException;
public class Distance {
	
	private Image centerImage;
	private Image sourceImage;
	private double distance;
	
	public Distance(){
		
	}

	public Distance(Image centerPoint,Image sourcePoint){
		this.centerImage = centerPoint;
		this.sourceImage = sourcePoint;
		//this.distance = distance;
	}
	

	
	public void caculateDis() throws IOException{
		double tempDis = 0;
		//double distance = 0;
			for(int i=0;i<centerImage.getImageFeatureSize();i++)
			{
				double tempX = 0;
				tempX = (centerImage.getImageFeature().get(i) - sourceImage.getImageFeature().get(i));
				tempDis += (Math.pow(tempX, 2));
			}
				distance =Math.sqrt(tempDis);
	}
	
	public double getDistance(){
		return distance;
	}
	
	public String disToString()
	{
		String temp = String.valueOf(distance);
		return temp;
	}
	


}
