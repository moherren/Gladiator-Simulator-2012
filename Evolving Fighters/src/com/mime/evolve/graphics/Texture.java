package com.mime.evolve.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

public class Texture {
		
		public static Render gold=generateMetal(100,100,0xFFD700);
		public static Render silver=generateMetal(100,100,0xC0C0C0);
		public static Render bronze=generateMetal(100,100,0xCD7F32);
		public static Render stone=generateStone(200,200);
		public static Render background=createBackground();
		
		public static Render generateColoredStone(int width, int height, int color){
			Random random=new Random(0);
			Render render=new Render(width,height);
			
			for(int y=0;y<height;y++){
				for(int x=0;x<width;x++){
					int r = (color & 0xFF0000) >> 16;
		    		int g = (color & 0xFF00) >> 8;
		    		int b = (color & 0xFF);
					
					double mult=1;
					double range=0.2;
					if(random.nextBoolean())
						mult-=range*random.nextDouble();
					else
						mult+=range*random.nextDouble();
					r=(int) Math.max(0,Math.min(r*mult, 255));
					g=(int) Math.max(0,Math.min(g*mult, 255));
					b=(int) Math.max(0,Math.min(b*mult, 255));
					
					int rgb = r;
					rgb = (rgb << 8) + g;
					rgb = (rgb << 8) + b;
					render.pixels[x+y*width]=rgb;
				}
				render.blurRow(2, y);
			}
			
			for(int i=0;i<render.width;i++)
				render.blurColumn(3, i);

			return render;
		}
		
		public static Render createBackground(){
			Render t=loadBitmap("Textures/arena.png");
			Render r=Render2D.getBlankRender(t.width, t.height, 1);
			r.draw(t, 0, 0);
			return r;
		}
		
		public static Render generateMetal(int width, int height, int color){
			
			Random random=new Random(0);
			Render render=new Render(width,height);
			for(int y=0;y<height;y++){
				for(int x=0;x<width;x++){
					int r = (color & 0xFF0000) >> 16;
		    		int g = (color & 0xFF00) >> 8;
		    		int b = (color & 0xFF);
					
					double mult=1;
					double range=0.4;
					if(random.nextBoolean())
						mult-=range*random.nextDouble();
					else
						mult+=range*random.nextDouble();
					r=(int) Math.min(r*mult, 255);
					g=(int) Math.min(g*mult, 255);
					b=(int) Math.min(b*mult, 255);
					
					int rgb = r;
					rgb = (rgb << 8) + g;
					rgb = (rgb << 8) + b;
					render.pixels[x+y*width]=rgb;
				}
				render.blurRow(10, y);
			}
			for(int x=0;x<width;x++){
				double d=Math.abs(x-width/2)*0.5/width;
				for(int y=0;y<height;y++){
					render.pixels[x+y*width]=Render.mixColor(render.pixels[x+y*width], 0xffffff, d);
				}
			}
			return render;
		}
		
		public static Render generateStone(int width, int height){
			Random random=new Random(0);
			Render render=new Render(width,height);
			
			int color=0x999999;
			
			for(int y=0;y<height;y++){
				for(int x=0;x<width;x++){
					int r = (color & 0xFF0000) >> 16;
		    		int g = (color & 0xFF00) >> 8;
		    		int b = (color & 0xFF);
					
					double mult=1;
					double range=0.2;
					if(random.nextBoolean())
						mult-=range*random.nextDouble();
					else
						mult+=range*random.nextDouble();
					r=(int) Math.max(0,Math.min(r*mult, 255));
					g=(int) Math.max(0,Math.min(g*mult, 255));
					b=(int) Math.max(0,Math.min(b*mult, 255));
					
					int rgb = r;
					rgb = (rgb << 8) + g;
					rgb = (rgb << 8) + b;
					render.pixels[x+y*width]=rgb;
				}
				render.blurRow(2, y);
			}
			
			for(int i=0;i<render.width;i++)
				render.blurColumn(3, i);

			return render;
		}
		
		public static Render loadBitmap(String fileName){
			try{
				BufferedImage image=ImageIO.read(Texture.class.getResource(fileName));
				int width=image.getWidth();
				int height=image.getHeight();
				Render result=new Render(width,height);
				image.getRGB(0, 0,width, height, result.pixels,0,width);
				
				for(int x=0; x<width;x++){
					for(int y=0; y<height; y++){
						Color c=new Color(image.getRGB(x,y));
						int r=c.getRed();
						int b=c.getBlue();
						int g=c.getGreen();
						int rgb = r;
						rgb = (rgb << 8) + g;
						rgb = (rgb << 8) + b;
						result.pixels[x+y*width]=rgb;
					}
				}
				return result;
			}catch(Exception e){
				System.out.println("CRASH!");
				throw new RuntimeException(e);
				
			}
		}

		public static Render2D getSpriteSheet(Render r,int width,int height,int num){
			int columns=r.width/width,rows=r.height/height;
			if(num>=rows*columns)
				return null;
			Render2D sprite=new Render2D(width,height);
			
			for(int x=0;x<width;x++){
				int xPix=x+((num%columns)*width);
				for(int y=0;y<height;y++){
					int yPix=y+(num/columns);
					int alpha=r.pixels[xPix+yPix*r.width];
					sprite.pixels[x+y*sprite.width]=alpha;
				}
			}
			
			return sprite;
		}

		public static Render2D getSpriteSheet(Render r,int width,int height,int num,boolean flipped){
			int columns=r.width/width,rows=r.height/height;
			if(num>=rows*columns)
				return null;
			Render2D sprite=new Render2D(width,height);
			
			for(int x=0;x<width;x++){
				int xPix=x+((num%columns)*width);
				for(int y=0;y<height;y++){
					int yPix=y+(num/columns);
					int alpha=r.pixels[xPix+yPix*r.width];
					if(!flipped)
						sprite.pixels[x+y*sprite.width]=alpha;
					else
						sprite.pixels[sprite.width-1-x+y*sprite.width]=alpha;
				}
			}
			
			return sprite;
		} 
}
