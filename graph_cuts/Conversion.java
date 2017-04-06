import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import javax.imageio.*;
import java.io.File;
import java.awt.Color;
import java.util.*;
// http://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
// http://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values

public class MinCut {
	
	public static int[] lambda={45,23};
	public static int height,width,nbPixels;
	public static int beta=25;
	
	public static void main (String[] args) throws IOException {
		int[][] img1;
		long startTime=System.currentTimeMillis();
		
		
		File fichImg=new File("img.png");
		BufferedImage img = ImageIO.read(fichImg);
		img1=imgToArray(img);
		height=img1.length;
		width=img1[0].length;
		nbPixels=height*width;
		System.out.println("Image 1, height "+height+" width "+width);

		BufferedImage img1bis = arrayToImg(img1);
		File res;
		
		// Adding noise to the original image
		/*
		res = new File("ImgTraitee.png");
		//ImageIO.write(img1bis,"png",res);
		
		noise25_Percent(img1);
		
		img1bis = arrayToImg(img1);
		res = new File("ImgTraitee1.png");
		ImageIO.write(img1bis,"png",res);
		*/
		
		int[][] adjMatrix=imgToAdjMatrix(img1,lambda);
		int[][] afterCut = minCut(adjMatrix,nbPixels,nbPixels+1); //afterCut is the new image
		BufferedImage imgAfterCut = arrayToImg(afterCut);
		res = new File("ImgDebruitee.png");
		ImageIO.write(imgAfterCut,"png",res);
		long endTime=System.currentTimeMillis();
		long time=endTime-startTime;
		System.out.println("Running time of the cut: "+time);

	}
	
	
	public static int[][] minCut(int[][] graph,int source, int sink){

		//Initialization of the residual graph
		int[][] residualGraph=new int[graph.length][];
		for(int i=0;i<residualGraph.length;i++){
			int length=6;
			if(i>=nbPixels)
				length=nbPixels;
			residualGraph[i]=new int[length];
			for(int j=0;j<length;j++){
				residualGraph[i][j]=graph[i][j];
			}
		}
		
		System.out.println("Computing the max-flow");
		
		int[] path=new int[nbPixels+2];
		for(int i=0;i<nbPixels+2;i++)
			path[i]=-1;
		
		//While there is an augmenting path from source to sink, update the residual graph
		while(breadth_First_Search(residualGraph,nbPixels,nbPixels+1,path,nbPixels+2)){
			//System.out.println("Chemin");
			int pathFlow=Integer.MAX_VALUE;
			//Find the flow of the augmenting path
			int j=path[sink];
			for(int i=sink; i!=source;i=path[i]){  
				int edgeWeight=0;
				if(j==source){
					edgeWeight=residualGraph[source][i];
				} else if(i==sink) {
					edgeWeight=residualGraph[j][5];
				} else {              //i=xI*width+yI;
					int yI=i%width;
					int xI=(i-yI)/width;
					int yJ=j%width;
					int xJ=(j-yJ)/width;
					if(yI>yJ){  	//i=right neighbour of j
						edgeWeight=residualGraph[j][1];
					}else if(yI<yJ){
						edgeWeight=residualGraph[j][0];
					}else if(xI>xJ){
						edgeWeight=residualGraph[j][3];
					}else if(xI<xJ) {
						edgeWeight=residualGraph[j][2];
					}
				}
				//System.out.println("Arc de "+j+" a "+i+" de poids " +edgeWeight);
				pathFlow=Math.min(pathFlow,edgeWeight);
				j=path[j];
			}
			//System.out.println("PathFlow de "+pathFlow);
			//Update the residual graph
			j=path[sink];
			for(int i=sink; i!=source;i=path[i]){  
				if(j==source){
					residualGraph[i][4]+=pathFlow;
					residualGraph[source][i]-=pathFlow;
				} else if(i==sink) {
					residualGraph[sink][j]+=pathFlow;
					residualGraph[j][5]-=pathFlow;
				} else {              //i=xI*width+yI;
					int yI=i%width;
					int xI=(i-yI)/width;
					int yJ=j%width;
					int xJ=(j-yJ)/width;
					
					if(yI>yJ){  	//i=right neighbour of j
						residualGraph[i][0]+=pathFlow;
						residualGraph[j][1]-=pathFlow;
					}else if(yI<yJ){
						residualGraph[i][1]+=pathFlow;
						residualGraph[j][0]-=pathFlow;
					}else if(xI>xJ){
						residualGraph[i][2]+=pathFlow;
						residualGraph[j][3]-=pathFlow;
					}else if(xI<xJ) {
						residualGraph[i][3]+=pathFlow;
						residualGraph[j][2]-=pathFlow;
					}
				}
				j=path[j];
			}
		}
		
		
		System.out.println("Computing the corresponding cut");
		//Find the cut thanks to the residual graph
		int[] cut=new int[nbPixels+2];
		for(int i=0;i<nbPixels+2;i++)
			cut[i]=0;
			
		residualGraphExporation(residualGraph,nbPixels, nbPixels+1,cut,nbPixels+2);
		
		//Creating the image thanks to the cut
		int[][] img=new int[height][width];
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				if(cut[i*width+j]==1){
					img[i][j]=1;
				} else {
					img[i][j]=0;
				}
			}
		}
		return img;
	}
	
	public static void residualGraphExporation(int residualGraph[][], int source, int sink, int cut[], int nbNodes)
    {

        boolean marked[] = new boolean[nbNodes];
        for(int i=0; i<nbNodes; i++)
            marked[i]=false;
 
        LinkedList<Integer> ll = new LinkedList<Integer>();
        ll.addFirst(source);
        marked[source] = true; 
        // BFS
        while (ll.size()!=0)
        {
            int node = ll.removeFirst();
            cut[node]=1;
            int yNode=node%width;
			int xNode=(node-yNode)/width;
            int nbNeighbours=residualGraph[node].length;
            for (int neighbour=0; neighbour<nbNeighbours; neighbour++)
            {
                if(residualGraph[node][neighbour] > 0){
					int indiceN=0;
					if(node==source || node==sink){
						indiceN=neighbour;
					} else {
						switch(neighbour){
							case 0:
								indiceN=(xNode)*width+(yNode-1);
								break;
							case 1:
								indiceN=(xNode)*width+(yNode+1);
								break;
							case 2:
								indiceN=(xNode-1)*width+yNode;
								break;
							case 3:
								indiceN=(xNode+1)*width+yNode;
								break;
							case 4:
								indiceN=nbNodes-2;
								break;
								
							case 5:
								indiceN=nbNodes-1;
								break;
						}
					}
					if (marked[indiceN]==false)
					{
						ll.add(indiceN);
						marked[indiceN] = true;
					}
				}
            }
        }
    }
	
	public static boolean breadth_First_Search(int residualGraph[][], int source, int sink, int path[], int nbNodes)
    {

        boolean marked[] = new boolean[nbNodes];
        for(int i=0; i<nbNodes; i++)
            marked[i]=false;
 
        LinkedList<Integer> ll = new LinkedList<Integer>();
        ll.addFirst(source);
        marked[source] = true;
        path[source]=-1;
 
        // BFS
        while (ll.size()!=0)
        {
            int node = ll.removeFirst();
            int nbNeighbours=residualGraph[node].length;
            if(node==sink)
				return true;
            for (int neighbour=0; neighbour<nbNeighbours; neighbour++)
            {
                if(residualGraph[node][neighbour] > 0){
					int indiceN=0;
					int yNode=node%width;
					int xNode=(node-yNode)/width;
					if(node==source || node==sink){
						indiceN=neighbour;
					} else {
						switch(neighbour){
							case 0:
								indiceN=(xNode)*width+(yNode-1);
								break;
							case 1:
								indiceN=(xNode)*width+(yNode+1);
								break;
							case 2:
								indiceN=(xNode-1)*width+yNode;
								break;
							case 3:
								indiceN=(xNode+1)*width+yNode;
								break;
							case 4:
								indiceN=nbNodes-2;
								break;
							case 5:
								indiceN=nbNodes-1;
								break;
						}
					}
					if (marked[indiceN]==false)
					{
						ll.add(indiceN);
						path[indiceN] = node;
						marked[indiceN] = true;
					}
				}
            }
        }
        return false;
    }
	
	
	 public static int[][] imgToAdjMatrix(int[][] binaryImg, int[] lambda){
		int height=binaryImg.length;
		int width=binaryImg[0].length;
		int nbPixels=height*width;
		
		
		//Adjacency matrix initialization
		int adjMatrix[][]=new int[nbPixels+2][];
		for(int i=0;i<nbPixels;i++){
			adjMatrix[i]=new int[6]; //0-3:Left/Right/Above/Below; 4-5:source/sink
			for(int j=0;j<6;j++)
				adjMatrix[i][j]=0;
		}
		adjMatrix[nbPixels]=new int[nbPixels];  //The sink has max nbPixels neighbours
		adjMatrix[nbPixels+1]=new int[nbPixels];
		for(int i=0;i<nbPixels;i++){
			adjMatrix[nbPixels][i]=0;
			adjMatrix[nbPixels+1][i]=0;			//Will be useful for the residual graph
		}
		
		//Construction of the graph
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				int indice=i*width+j;
				if(binaryImg[i][j]==0){   //Edge from this pixel to the sink
					adjMatrix[indice][5]=lambda[0];
				} else {				  //Edge from the source to this pixel
					adjMatrix[nbPixels][indice]=lambda[1];
				}
				if(i>0){				//Neighbour above
					adjMatrix[indice][2]=beta;
				}
				if(i<height-1){			//Neighbour below
					adjMatrix[indice][3]=beta;
				}
				if(j>0){				//Neighbour to the left
					adjMatrix[indice][0]=beta;
				}
				if(j<width-1){			//Neighbour to the right
					adjMatrix[indice][1]=beta;
				}
			}
		}
		return adjMatrix;
	}
	 
	 public static int[][] imgToArray(BufferedImage image) {
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
   
   public static BufferedImage arrayToImg(int[][] array){
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
	
	public static void noise25_Percent(int[][] array){
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

   private static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

      final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
      final int width = image.getWidth();
      final int height = image.getHeight();

      int[][] result = new int[height][width];
      
      if (image.getAlphaRaster() != null) {  //Un pixel->4 composantes (RGB + Alpha)
         final int pixelLength = 4;
         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
            int argb = 0;
           // argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
            argb += ((int) pixels[pixel + 1] & 0xff); // blue
            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
            if(argb/3>128)
				result[row][col] = 1;
            else
				result[row][col] = 0;
            col++;
            if (col == width) {
               col = 0;
               row++;
            }
         }
      } else {					//Un pixel -> 3 Composantes(RGB)
         final int pixelLength = 3;
         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
            int blue,red,green;
            blue = ((int) pixels[pixel] & 0xff); // blue
            green = (((int) pixels[pixel + 1] & 0xff) << 8); // green
            red = (((int) pixels[pixel + 2] & 0xff) << 16); // red
            if((blue+red+green)/3>128){
				result[row][col] = 1;
			} else {
				result[row][col] = 0;
			}
            col++;
            if (col == width) {
               col = 0;
               row++;
            }
         }
      }

      return result;
   }
}


