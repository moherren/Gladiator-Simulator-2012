package com.mime.evolve.graphics;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;

public class Render {
	public static final double vDisplacement=4/3.000,hDisplacement=Math.pow(vDisplacement,-1);
	public static GlyphVector getGlyohVector(String s,Font font){
		FontRenderContext frc=new FontRenderContext(null,true,true);
		return font.createGlyphVector(frc, s);
	}
	public static int mixColor(int base,int color,double d){
		int r = (base & 0xFF0000) >> 16;
    	int g = (base & 0xFF00) >> 8;
    	int b = (base & 0xFF);
    	r-=(r-((color & 0xFF0000) >> 16))*d;
    	g-=(g-((color & 0xFF00) >> 8))*d;
    	b-=(b-((color & 0xFF)))*d;
    	int rgb = r;
		rgb = (rgb << 8) + g;
		rgb = (rgb << 8) + b;
		return rgb;
	}
	public final int width;
	public final int height;
	
	public final int pixels[];
	public Font font = new JLabel().getFont().deriveFont(0, 24);
	
	public Render(int width,int height){
		this.width=width;
		this.height=height;
		this.pixels=new int[width*height];
		
		for(int x=0; x<width*height;x++){
			pixels[x]=0;
		}
		setFont("Constantine.ttf");
	}
	public void blur(int radius){
		for(int x=0;x<width;x++)
			blurColumn(radius,x);
		for(int y=0;y<height;y++)
			blurRow(radius,y);
	}
	public void blurColumn(int radius,int x){
		int Ys[]=new int[height];
		for(int y=0;y<height;y++){
			int min=y-radius;
			int max=y+radius;
			int reps=0;
			int tR=0;
			int tG=0;
			int tB=0;
			for(int ry1=min;ry1<max;ry1++){
				int ry=ry1;
				while(ry<0)
					ry+=height;
				while(ry>=height)
					ry-=height;
				reps++;
				int color=pixels[x+ry*width];
				tR += (color & 0xFF0000) >> 16;
    			tG += (color & 0xFF00) >> 8;
    			tB += (color & 0xFF);
			}
			tR/=reps;
			tG/=reps;
			tB/=reps;
			
			int rgb = tR;
			rgb = (rgb << 8) + tG;
			rgb = (rgb << 8) + tB;
			
			Ys[y]=rgb;
		}
		
		for(int y=0;y<height;y++){
			pixels[x+y*width]=Ys[y];
		}
	}
	
	public void blurRow(int radius,int y){
		int xs[]=new int[width];
		for(int x=0;x<width;x++){
			int min=x-radius;
			int max=x+radius;
			int reps=0;
			int tR=0;
			int tG=0;
			int tB=0;
			for(int rx1=min;rx1<max;rx1++){
				int rx=rx1;
				while(rx<0)
					rx+=width;
				while(rx>=width)
					rx-=width;
				reps++;
				int color=pixels[rx+y*width];
				tR += (color & 0xFF0000) >> 16;
    			tG += (color & 0xFF00) >> 8;
    			tB += (color & 0xFF);
			}
			tR/=reps;
			tG/=reps;
			tB/=reps;
			
			int rgb = tR;
			rgb = (rgb << 8) + tG;
			rgb = (rgb << 8) + tB;
			
			xs[x]=rgb;
		}
		
		for(int x=0;x<width;x++){
			pixels[x+y*width]=xs[x];
		}
	}
	
	public void convertToGray(double shadeAmount){
			
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				int hex=pixels[x+y*width];
				int r = (hex & 0xFF0000) >> 16;
		    	int g = (hex & 0xFF00) >> 8;
		    	int b = (hex & 0xFF);
		    	
		    	int shadeColor= (int) (r* 0.299);
		    	shadeColor += (int) (g*0.587);
				shadeColor += (int) (b*0.114);
				shadeColor = (shadeColor + (((shadeColor << 8) + shadeColor) << 8));
		    	
		    	r-=(r-((shadeColor & 0xFF0000) >> 16))*shadeAmount;
		    	g-=(g-((shadeColor & 0xFF00) >> 8))*shadeAmount;
		    	b-=(b-((shadeColor & 0xFF)))*shadeAmount;
		    	
		    	int rgb = r;
				rgb = (rgb << 8) + g;
				rgb = (rgb << 8) + b;
				pixels[x+y*width]=rgb;
			}
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
				if(alpha>0)
				pixels[xPix+yPix*width]=alpha;
			}
		}
	}
	
	public void drawOblique(Render render,int xOffSet,int yOffSet,double obliquity){
		for(int y=0; y<render.height;y++){
			int yPix=y+yOffSet;
			if (yPix<0||yPix>=height) continue;
			for(int x=0; x<render.width;x++){
				int xPix=x+xOffSet;
				if (xPix<0||xPix>=width) continue;
				int alpha=render.pixels[x+y*render.width];
				alpha=mixColor(pixels[xPix+yPix*width],alpha,obliquity);
				if(render.pixels[x+y*render.width]>0)
				pixels[xPix+yPix*width]=alpha;
			}
		}
	}
	
	public void drawString(String s,int x,int y,int color){
		FontRenderContext frc=new FontRenderContext(null,true,true);
		GlyphVector gv=font.createGlyphVector(frc, s);
		Shape shape=gv.getOutline(x, y);
		Rectangle2D rect=gv.getVisualBounds();
		if(!shape.intersects(0, 0, width, height))
			return;
		for(int X=Math.max(x, 0);X<Math.min(x+rect.getWidth()+50,width);X++){
			for(int Y=(int) Math.max(y-rect.getHeight(), 0);Y<Math.min(y+rect.getHeight()/2.000,height);Y++){
				if(shape.contains(X, Y))
					pixels[X+Y*width]=color;
			}
		}
	}
	
	public Font getFont(){
		return font;
	}
	
	public GlyphVector getGlyohVector(String s){
		FontRenderContext frc=new FontRenderContext(null,true,true);
		return font.createGlyphVector(frc, s);
	}
	public GlyphVector getGlyohVector(String s,Font font,double size){
		FontRenderContext frc=new FontRenderContext(null,true,true);
		return font.createGlyphVector(frc, s);
	}
	
	public void setFont(Font f){
		font=f;
	}
	
	public void setFont(String file){
		try {
			font=Font.createFont(Font.TRUETYPE_FONT, new File(file)).deriveFont(font.getStyle(),font.getSize());
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void shade(double shadeAmount,int shadeColor){
		if(shadeAmount==0)
			return;
			
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				int hex=pixels[x+y*width];
				int min=Math.min(1,hex);
				int r = (hex & 0xFF0000) >> 16;
		    	int g = (hex & 0xFF00) >> 8;
		    	int b = (hex & 0xFF);
		    	r-=(r-((shadeColor & 0xFF0000) >> 16))*shadeAmount;
		    	g-=(g-((shadeColor & 0xFF00) >> 8))*shadeAmount;
		    	b-=(b-((shadeColor & 0xFF)))*shadeAmount;
		    	
		    	int rgb = r;
				rgb = (rgb << 8) + g;
				rgb = (rgb << 8) + b;
				pixels[x+y*width]=Math.max(rgb,min);
			}
		}
	}
}
