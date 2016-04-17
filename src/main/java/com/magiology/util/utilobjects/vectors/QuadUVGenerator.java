package com.magiology.util.utilobjects.vectors;


public class QuadUVGenerator{
	
	private float imagePixelWidth,imagePixelHeight;
	
	public QuadUVGenerator(int imageWidth,int imageHeight){
		setImageDimension(imageWidth, imageHeight);
	}
	
	public QuadUV create(int xStart, int yStart, int width, int height){
		
		float
			x=imagePixelWidth*xStart,
			y=imagePixelHeight*yStart,
			x1=imagePixelWidth*(xStart+width),
			y1=imagePixelHeight*(yStart+height);
		
//		1,1,
//		1,0,
//		0,0,
//		0,1
		return new QuadUV(
				x1, y1, 
				x1, y,
				x, y,
				x, y1);
	}
	
	
	public void setImageDimension(int imageWidth,int imageHeight){
		imagePixelWidth=1F/imageWidth;
		imagePixelHeight=1F/imageHeight;
	}
}
