package com.mime.evolve.graphics;

import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.Arrays;
import com.mime.evolve.Controller;
import com.mime.evolve.Display;
import com.mime.evolve.Tournament;
import com.mime.evolve.Victory;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;

public class Render2D extends Render{
	public static void drawLine(Render2D r,int depth,int color,double x1,double y1,double x2,double y2){
		double length=Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
		double slopeY=(y2-y1)/length,slopeX=(x2-x1)/length;
		
		for(int i=0;i<length;i++){
			int x=(int) (x1+slopeX*i);
			int y=(int) (y1+slopeY*i);
			if(r.depthMap[x+y*r.width]<=depth){
				r.pixels[x+y*r.width]=color;
				r.depthMap[x+y*r.width]=depth;
			}
		}
	}
	public static int literalY(double y){
		y-=Display.HEIGHT/2;
		y*=vDisplacement;
		y+=Display.HEIGHT/2;
		return (int) y;
	}
	public static int visualY(double y){
		y-=Display.HEIGHT/2;
		y*=hDisplacement;
		y+=Display.HEIGHT/2;
		return (int) y;
	}
	public int[] depthMap=new int[height*width];
	
	public static Render getBlankRender(int width,int height,int color){
		Render r=new Render(width,height);
		Arrays.fill(r.pixels, color);
		return r;
	}
	
	public Render2D(int width, int height) {
		super(width, height);
		Arrays.fill(pixels, -1);
	}
	
	public void Arena(){
		draw(Texture.background,0,0);
	}
	public void background(){
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				int dRight=width-x,dBottom=height-y-30;
				int color=Texture.stone.pixels[(x%Texture.stone.width)+(y%Texture.stone.height)*Texture.stone.width];
				if(x<20&&((y>height/2&&x<dBottom)||(y<height/2&&x<y)||(y==height/2)))
					pixels[x+y*width]=Render.mixColor(color, 0, 0.20);
				else if(dRight<20&&((y>height/2&&dRight<dBottom)||(y<height/2&&dRight<y)||(y==height/2)))
					pixels[x+y*width]=Render.mixColor(color, 0, 0.50);
				else if(y<20)
					pixels[x+y*width]=Render.mixColor(color, 0, 0.30);
				else if(dBottom<20)
					pixels[x+y*width]=Render.mixColor(color, 0, 0.40);
				else
					pixels[x+y*width]=color;
			}
		}
	}
	public void background(int X,int Y,int width, int height){
		for(int x=X;x<width+X;x++){
			for(int y=Y;y<height+Y;y++){
				int dRight=width-x,dBottom=height-y-30;
				int color=Texture.stone.pixels[(x%Texture.stone.width)+(y%Texture.stone.height)*Texture.stone.width];
				if(x<20&&((y>height/2&&x<dBottom)||(y<height/2&&x<y)||(y==height/2)))
					pixels[x+y*width]=Render.mixColor(color, 0, 0.20);
				else if(dRight<20&&((y>height/2&&dRight<dBottom)||(y<height/2&&dRight<y)||(y==height/2)))
					pixels[x+y*width]=Render.mixColor(color, 0, 0.50);
				else if(y<20)
					pixels[x+y*width]=Render.mixColor(color, 0, 0.30);
				else if(dBottom<20)
					pixels[x+y*width]=Render.mixColor(color, 0, 0.40);
				else
					pixels[x+y*width]=color;
			}
		}
	}
	public void Blocks(){
		/*for(int i=0;i<blankGame.level.width*blankGame.level.height;i++){
			if(blankGame.level.wall[i]==true){
				pixels[i]=0;
			}
			else pixels[i]=0xffffff;
		}*/
	}
	public void displayFightInformation(Controller game){
		drawHealthBar(20,650,game.player1,true);
		drawHealthBar(width-20,650,game.player2,false);
		
		double dist=1-(game.getDisplayTime()-Tournament.stageTimes[0])/(Tournament.stageTimes[1]*1.000);
		
		if(game.getDisplayStage()==0)
			dist=0;
		else{
			dist=Math.max(dist, 0);
			dist=Math.min(dist, 1);
		}
		if(game.getDisplayStage()<=1){
			this.shade((1-dist)*0.85, 1);
			this.drawOblique(game.vsArt, 600-game.vsArt.width/2, 350-game.vsArt.height/2, 1-dist);
		}
		else
			dist=1;
		
		if(game.getDisplayStage()==2)
			draw(game.roundArt[game.getRedos()-1],600-game.roundArt[game.getRedos()-1].width/2,400-game.roundArt[game.getRedos()-1].height/2);
		else if(game.getDisplayStage()==5){
			if(game.endGame.equals("KO"))
				draw(game.koArt,600-game.koArt.width/2,400-game.koArt.height/2);
			else
				draw(game.execusionArt,600-game.execusionArt.width/2,400-game.execusionArt.height/2);
		}
		
		
			
	
		draw(game.species1.getNameArt(),(int)(400-380*dist),(int)(300*dist+400));
		draw(game.species2.getNameArt(),(int)(800+380*dist-game.species2.getNameArt().width),(int)(500*dist+200));
	}
	
	public void drawHealthBar(int x,int y,Player p,boolean leftToRight){
		drawHealthBar(this,x,y,p,leftToRight);
	}
	
	public static void drawHealthBar(Render r,int x,int y,Player p,boolean leftToRight){
		int width=220;
		int height=30;
		Render stone=Texture.stone;
		int line=x+5+(int) ((p.health/p.maxHealth)*(width-10));
		if(!leftToRight){
			line=x-5-(int) ((p.health/p.maxHealth)*(width-10));
			x-=width;
		}
		for(int X=0;X<width;X++){
			for(int Y=0;Y<height;Y++){
				int dRight=width-X,dBottom=height-Y;
				int color=stone.pixels[(X%stone.width)+(Y%stone.height)*stone.width];
				if(X<5&&((Y>height/2&&X<dBottom)||(Y<height/2&&X<Y)||(Y==height/2)))
					r.pixels[(X+x)+(Y+y)*r.width]=Render.mixColor(color, 0, 0.20);
				else if(dRight<5&&((Y>height/2&&dRight<dBottom)||(Y<height/2&&dRight<Y)||(Y==height/2)))
					r.pixels[(X+x)+(Y+y)*r.width]=Render.mixColor(color, 0, 0.50);
				else if(Y<5)
					r.pixels[(X+x)+(Y+y)*r.width]=Render.mixColor(color, 0, 0.30);
				else if(dBottom<5)
					r.pixels[(X+x)+(Y+y)*r.width]=Render.mixColor(color, 0, 0.40);
				else if((!leftToRight&&x+X<line)||(leftToRight&&x+X>line))
					r.pixels[(X+x)+(Y+y)*r.width]=0xff0000;
				else
					r.pixels[(X+x)+(Y+y)*r.width]=0x00ff00;

			}
		}
	}
	
	public void drawOutlinedWordArt(String s,int x,int y,int text){
		FontRenderContext frc=new FontRenderContext(null,true,true);
		GlyphVector gv=font.deriveFont(0, font.getSize()).createGlyphVector(frc, s);
		Area filler=new Area();
		filler.add(new Area(gv.getOutline(x, y)));
		Rectangle2D boundingBox=gv.getOutline(x, y).getBounds2D();
		Shape shape=gv.getOutline(x, y);
		filler.add(new Area(shape));
		
		Render r=new Render(100,100);
		
		switch(text){
		case 1:	
			r=Texture.gold;
			break;
		case 2:
			r=Texture.silver;
			break;
		case 3:
			r=Texture.bronze;
			break;
		case 4:
			r=Texture.stone;
			break;
		default:
			r=Texture.generateColoredStone(100, 100, text-4);
			break;
		}
		
		int minX=(int) boundingBox.getMinX(),minY=(int) boundingBox.getMinY();
		double rS=font.getSize()*(1.9/24.00);
		for(int X=(int) boundingBox.getMinX();X<boundingBox.getMaxX();X++){
			for(int Y=(int) boundingBox.getMinY();Y<boundingBox.getMaxY();Y++){
			if((X>width||X<0)||(Y>height||Y<0))
				continue;
			if(filler.contains(X,Y)){
				int up=Math.max(X, X+(Y-1)*width);
				int down=Math.min(X+(height-1)*width, X+(Y+1)*width);
				int left=Math.max(Y*width,(X-1)+Y*width);
				int right=Math.min((width-1)+Y*width,(X+1)+Y*width);
				
				pixels[X+Y*width]=r.pixels[X-minX+(Y-minY)*r.width];
				depthMap[X+Y*width]=Y;
				if(depthMap[right]<Y)
					pixels[right]=1;
				if(depthMap[left]<Y)
					pixels[left]=1;
				if(depthMap[down]<Y+1)
					pixels[down]=1;
				if(depthMap[up]<Y-1)
					pixels[up]=1;
			}
			}
		}
	}
	public void drawProjectile(Projectile p){
		p.draw(this);
	}
	public void drawWordArt(String s,int x,int y,int text){
		FontRenderContext frc=new FontRenderContext(null,true,true);
		GlyphVector gv=font.deriveFont(0, font.getSize()).createGlyphVector(frc, s);
		Area filler=new Area();
		filler.add(new Area(gv.getOutline(x, y)));
		Rectangle2D boundingBox=gv.getOutline(x, y).getBounds2D();
		Shape shape=gv.getOutline(x, y);
		filler.add(new Area(shape));
		
		Render r=new Render(100,100);
		
		switch(text){
		case 1:	
			r=Texture.gold;
			break;
		case 2:
			r=Texture.silver;
			break;
		case 3:
			r=Texture.bronze;
			break;
		case 4:
			r=Texture.stone;
			break;
		default:
			r=Texture.generateColoredStone(100, 100, text-4);
			break;
		}
		
		int minX=(int) boundingBox.getMinX(),minY=(int) boundingBox.getMinY();
		double rS=font.getSize()*(1.5/24.00);
		for(int X=(int) boundingBox.getMinX();X<boundingBox.getMaxX();X++){
			for(int Y=(int) boundingBox.getMinY();Y<boundingBox.getMaxY();Y++){
			if((X>width||X<0)||(Y>height||Y<0))
				continue;
			if(filler.contains(X, Y))
				pixels[X+Y*width]=r.pixels[X-minX+(Y-minY)*r.width];
			else if(filler.intersects(X-rS/3.00, Y-rS/3.00,rS/2,rS/2))
				pixels[X+Y*width]=Render.mixColor(r.pixels[X-minX+(Y-minY)*r.width],0,0.25);
			else if(filler.intersects(X-rS/3.00, Y,rS/2,rS/2))
				pixels[X+Y*width]=Render.mixColor(r.pixels[X-minX+(Y-minY)*r.width],0,0.5);
			else if(filler.intersects(X, Y-rS/3.00,rS/2,rS/2))
				pixels[X+Y*width]=Render.mixColor(r.pixels[X-minX+(Y-minY)*r.width],0,0.5);
			else if(filler.intersects(X, Y,rS/2,rS/2))
				pixels[X+Y*width]=Render.mixColor(r.pixels[X-minX+(Y-minY)*r.width],0,0.75);

			}
		}
	}
	public void loadingBar(double load){
		int barHeight=25,barWidth=300;
		int xOffSet=width/2-barWidth/2;
		int yOffSet=height/2-barHeight/2;
		for(int i=0;i<barHeight*(int)(barWidth*load);i++){
			int barLength=(int)(barWidth*load);
			int x=xOffSet+i%barLength,y=yOffSet+i/barLength;
			pixels[x+y*width]=0xff0000;
		}
		drawString("Loading...",550,385,1);
		DecimalFormat df2 = new DecimalFormat( "#0.00%" );
		drawString(df2.format(load),550,410,1);
	}
	
	public void Player(Player player1) {
		player1.draw(this);
	}
	
	public void winScreen(Victory vic){
		vic.render(this);
	}
	public void draw(Render render,int xOffSet,int yOffSet,int depth){
		for(int y=0; y<render.height;y++){
			int yPix=y+yOffSet;
			if (yPix<0||yPix>=height) continue;
			for(int x=0; x<render.width;x++){
				int xPix=x+xOffSet;
				if (xPix<0||xPix>=width) continue;
				int alpha=render.pixels[x+y*render.width];
				if(alpha>0&&depthMap[xPix+yPix*width]<=depth){
					pixels[xPix+yPix*width]=alpha;
					depthMap[xPix+yPix*width]=depth;
				}
			}
		}
		
	}
}
