
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class tukey {
	
	
	/***** fonction biweight qui prends en entrée la difference d'intensité entre un pixel et son voisin*********/
	public static double biweight(double x,double sigma){
		
		if(Math.abs(x)<=sigma && sigma!=0) {
			
			double X= x *( 1 - (x/sigma)*(x/sigma))*( 1 - (x/sigma)*(x/sigma));
			
			return  X;
		} else if (sigma==0){
			System.out.println("ERREUR SIGMA 0");
			 return 0;
		}else{
			return 0;
		}
	}
	/***** calcul su sigmae ******/
	public static double sigmae(double[][] img) throws IOException{
		int l=img.length*img[0].length;
		
		double[] source =new double[l];
		img=tool.gradient(img);
               
		int k=0;
		for(int i=0;i<img.length;i++){
			for(int j=0;j<img[0].length;j++){
				
				source[k]=img[i][j];
				k++;
			}
		}
		
		
		Arrays.sort(source);
		double med;
		if(l%2==0) med= (source[l/2]+source[l/2-1])/2;
		else 	med=source[((l-1)/2)];
		
		
		
		for(int i=0;i<l;i++){
					source[i]=Math.abs(source[i]-med);
		}
		
		
		Arrays.sort(source);
		
		if(l%2==0) med= (source[l/2]+source[l/2-1])/2;
		else 	med=source[((l-1)/2)];
		double sigmae = 1.4826*med;
		return sigmae;


	}
		
	
	
	/***** realise la diffusion selon une intensité donnée *******/
	public static double[][] smooth(double[][] Source,int intensity,String PATH) throws IOException{
			double sigmae=sigmae(Source);
			double sigma=Math.sqrt(5)*sigmae;
			double lambda = 1/biweight(sigmae,sigma);
			int w =  Source[0].length;
			int h =  Source.length;
			double[][] Output = Source;
			
			for(int i=0;i<intensity-1;i++){
				for(int y = 1; y <w-2; y++){
					for(int x = 1; x <h-2; x++){
						//CALCUL DES BORDS ET COINS
						if(x==1 &&  y==1){
							Output[x][y]=(Source[x+1][y]+Source[x][y+1])/2;
						}else if(x==1 &&  y==(h-1)){
							Output[x][y]=(Source[x+1][y]+Source[x][y-1])/2;
							
						}else if(x==w-1 &&  y==1){
							Output[x][y]=(Source[x-1][y]+Source[x][y+1])/2;
						}else if(x==w-1 &&  y==h-1){
							Output[x][y]=(Source[x-1][y]+Source[x][y-1])/2;
						}else if(x==1){
							Output[x][y]=(Source[x+1][y]+Source[x][y-1]+Source[x][y+1])/3;
						}else if(x==w-1){
							Output[x][y]=(Source[x-1][y]+Source[x][y-1]+Source[x][y+1])/3;
						}else if(y==1){
							Output[x][y]=(Source[x+1][y]+Source[x-1][y]+Source[x][y+1])/3;
						}else if(y==h-1){
							
						
							Output[x][y]=(Source[x+1][y]+Source[x-1][y]+Source[x][y+-1])/3;
						}else {
							//GENERAL
							Output[x][y]= Output[x][y]+lambda*(biweight(Source[x-1][y]-Source[x][y],sigma)+
										biweight(Source[x+1][y]-Source[x][y],sigma)+
										biweight(Source[x][y+1]-Source[x][y],sigma)+
										biweight(Source[x][y-1]-Source[x][y],sigma))/4;
							}
					}
				}
			Source=Output;
		}
		tool.getImage(Output,"Smooth"+intensity+"_"+sigma+"_"+PATH);
		return Output;
	}	
	
	
	
	
	/****** determine les contours de l'image ********/
	
	public static double[][] border(double[][] Source,int intensity,String PATH) throws IOException{
		double sigma=sigmae(Source)*Math.sqrt(5);
		Source = smooth(Source,intensity,PATH);
		double [][] grad=tool.gradient(Source);
		int w =  Source[0].length;
		int h =  Source.length;
		double[][] Output = Source;
		
		
			for(int y = 1; y <w-1; y++){
				for(int x = 1; x <h-1; x++){
					double l=255*(2*sigma*sigma)/((2*sigma*sigma)+grad[x][y]*grad[x][y]);
					Output[x][y]=l;
					
					
				}
			}
		tool.getImage(Output,"Border"+intensity+"_"+sigma+PATH);
		return Output;
	}
}



