import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import javax.imageio.*;
import java.io.File;
import java.awt.Color;
import java.util.*;
// http://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
// http://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values

public class TestMapEstimation {
	
	public static int[] lambda={45,23};
	public static int height,width,nbPixels;
	public static int beta=11;
	
	public static void main (String[] args) throws IOException {
		int[][] img1,afterCut;
		ImgProcessing process=new ImgProcessing();
		long startTime,endTime;
		OptimizedAdjMatrix optAdjMat;
		
		File fichImg=new File("MITimg1.png");
		BufferedImage img = ImageIO.read(fichImg);
		
		
		img1=process.imgToArray(img);
		System.out.println("Image 1, height "+img1.length+" width "+img1[0].length);
		File res;
		
		// Adding noise to the original image
		/*
		res = new File("ImgTraitee.png");
		ImageIO.write(img1bis,"png",res);
		
		noise25_Percent(img1);
		
		img1bis = arrayToImg(img1);
		res = new File("ImgTraitee1.png");
		ImageIO.write(img1bis,"png",res);
		*/
		
		//Computing the min-cut
		
		/*
		height=2;
		width=2;
		nbPixels=4;
		int[][] adjMatrix={{0,12,0,14,0,0,0,0,0,0},{0,0,0,0,0,20,0,0,9,0},{0,14,10,0,0,0,0,0,0,0},{0,0,7,0,0,4,0,0,0,0},{16,0,13,0},{0,0,0,0}};
		*/
			
		optAdjMat=new OptimizedAdjMatrix(img1,lambda,beta);	
		boolean FordFulkerson=false;
		startTime=System.currentTimeMillis();
		if(FordFulkerson) {
			afterCut = optAdjMat.minCut();  //afterCut is the new image
		} else {	
			afterCut = optAdjMat.pushRelabelAlgo();
		}
		endTime=System.currentTimeMillis();
		
		BufferedImage imgAfterCut = process.arrayToImg(afterCut);
		res = new File("ImgDebruitee.png");
		ImageIO.write(imgAfterCut,"png",res);
		
		long time=endTime-startTime;
		System.out.println("Running time of the cut: "+time);
	}
	
	
}


