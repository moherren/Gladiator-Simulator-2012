package com.mime.evolve.level;

import java.util.ArrayList;

import com.mime.evolve.graphics.Drawable;
import com.mime.evolve.graphics.Render2D;

public class Level implements Drawable{
	public final int width;
	public final int height;
	public boolean wall[];
	private ArrayList<Wall> walls;
	public Level(int width,int height){
		this.width=width;
		this.height=height;
		wall=new boolean[width*height];
		walls=new ArrayList<Wall>();
	}
	public void addWall(int x,int y,int width,int height){
		for(int xa=0;xa<width;xa++){
			int xPix=x+xa;
			for(int ya=0;ya<height;ya++){
				int yPix=y+ya;
				if(xPix+yPix*this.width<this.width*this.height)
				wall[xPix+yPix*this.width]=true;
			}
		}
		walls.add(new Wall(x,y,width,height));
	}
	public void draw(Render2D r) {
		for(int i=0;i<width*height;i++){
			if(wall[i]==true){
				r.pixels[i]=0;
			}
			else r.pixels[i]=0xffffff;
		}
	}
}
