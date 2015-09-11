package graphics;

public class Render {
	public final int width;
	public final int height;
	public final int pixels[];
	public static final double vDisplacement=4/3.000,hDisplacement=Math.pow(vDisplacement,-1);
	
	public Render(int width,int height){
		this.width=width;
		this.height=height;
		this.pixels=new int[width*height];
		
		for(int x=0; x<width*height;x++){
			pixels[x]=0;
		}
	}
	public void draw(Render render,int xOffSet,int yOffSet){
		for(int y=0; y<render.height;y++){
			int yPix=y+yOffSet;
			if (yPix<0||yPix>=height) continue;
			for(int x=0; x<render.width;x++){
				int xPix=x+xOffSet;
				if (xPix<0||xPix>=width) continue;
				int alpha=render.pixels[x+y*render.width];
				if(alpha>=0)
				pixels[xPix+yPix*width]=alpha;
			}
		}
	}
}
