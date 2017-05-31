import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import javax.imageio.*;
import java.io.File;
import java.awt.Color;

public class ImgProcessing {
	
	
	public int[][] imgToArray(BufferedImage image) {
      int width = image.getWidth();
      int height = image.getHeight();
      int[][] result = new int[height][width];

      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j++) {
            int color = image.getRGB(j, i);
            int blue = color & 0xff;
			int green = (color & 0xff00) >> 8;
			int red = (color & 0xff0000) >> 16;
			if((blue+green+red)/3>128){
				result[i][j]=1;
			} else {
				result[i][j]=0;
			}
         }
      }

      return result;
   }
   
   public BufferedImage arrayToImg(int[][] array){
	   int height=array.length;
	   int width=array[0].length;
	   BufferedImage res = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				if(array[i][j]==0){
					res.setRGB(j,i,Color.black.getRGB());
				} else {
					res.setRGB(j,i,Color.white.getRGB());
				}
			}
		}
		
		return res;
	}
	
	public void noise25_Percent(int[][] array){
		for(int i=0;i<array.length;i++){
			for(int j=0; j<array[0].length;j++){
				int change=(int)(4*Math.random());
				if(change==0){		//1 chance sur 4
					if(array[i][j]==0){
						array[i][j]=1;
					} else {
						array[i][j]=0;
					}
				}
			}
		}	
	}
	
}
