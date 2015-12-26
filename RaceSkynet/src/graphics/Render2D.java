package graphics;

import game.Game;
import ai.NeuralIntelligence;
import race.Car;

public class Render2D extends Render{
	
	public Render2D(int width, int height) {
		super(width, height);
	}
	public void render(){
		for(int i=0;i<pixels.length;i++){
			pixels[i]=0xffffff;
		}
		Game.getTrack().render(this);
	}
	public static void drawLine(Render2D r,int depth,int color,double x1,double y1,double x2,double y2){
		double length=Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
		double slopeY=(y2-y1)/length,slopeX=(x2-x1)/length;
		
		for(int i=0;i<length;i++){
			int x=(int) (x1+slopeX*i);
			int y=(int) (y1+slopeY*i);
			r.pixels[x+y*r.width]=color;
		}
	}
}
