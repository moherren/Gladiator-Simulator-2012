package com.mime.evolve.input;

import java.util.ArrayList;

import com.mime.evolve.graphics.Render;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.graphics.Texture;

public class CoinPile {
	//there is a max of 160
	int size;
	int x,y;
	int[][] stackCount=new int[9][9];
	ArrayList<Coin> coins=new ArrayList<Coin>();
	public CoinPile(int size,int x,int y) {
		this.size=size;
		this.x=x;
		this.y=y;
		//coins=new Coin[size];
		for(int i=0;i<size;i++){
			coins.add(createCoin(i,size,0));
		}
//		for(int X=0;X<9;X++)
//			for(int Y=0;Y<9;Y++){
//				for(int i=0;i<stackCount[X][Y];i++){
//					coins[X*9+Y+i*9*9]=createCoin(Y,X,i);
//				}
//			}
		
//		int factor=(int) Math.sqrt(size);
//		if(factor%2==0)
//			factor--;
//		int leftOver=0;
//		for(int c=1;c<=factor;c+=2)
//			leftOver+=Math.pow(c, 2);
//		int factorR=factor+1;
//		factorR/=2;
//		for(int i=0;i<81;i++){
//			int xC=i%9;
//			int yC=i/9;
//			int ring=Math.max(Math.abs(xC-4), Math.abs(yC-4))+1;
//			stackCount[xC][yC]=Math.max(0, factorR-ring);
//		}
//		System.out.println(Arrays.toString(stackCount));
	}

	public void render(Render2D r) {
		for(Coin c:coins){
			c.render(r);
		}
	}
	
	public void render(Render2D r,int x,int y) {
		for(Coin c:coins){
			c.render(r,x-this.x,y-this.y);
		}
	}
	
	public void addCoin(int delay){
		size++;
		coins.add(createCoin(size+1,size+1,delay));
	}
	
	public Coin createCoin(int num,int full,int delay){
		double circle=Math.PI;
		double length=(Math.log1p(num+1)*(Math.sinh(num%1.0)-1)/1.25);
		double x=(int)(Math.cos(num*1.6)*length);
		
		
		
		x*=Coin.size*2;
		x-=(Coin.size*2);
		x+=this.x;
		
		double y=(int)Math.abs((Math.sin(num*4.9)*length));
		y*=8;
		y-=(8)*4.5;
		y+=this.y;
		
		int z=0;
		for(Coin o:coins){
			if(Math.hypot(x-o.x, (y-o.y))<Coin.size&&z==o.z)
				z++;
		}
		return createCoin(x,y,z,delay);
	}
	
	public Coin createCoin(double x,double y,int height,int delay){
		return new Coin(x,y,height,System.currentTimeMillis()+delay);
	}

	public void removeCoin() {
		size--;
		coins.remove(coins.size()-1);
	}
}
class Coin{
		int x,y,z;
		long startTime;
		public static final double size=6;
		public static final Render sprite=Texture.loadBitmap("Textures/Coin.png");
		static final int tHeight=200,tLength=700,tTime=2000;
		
		public Coin(double x,double y,int z,long time) {
			this.x=(int) x;
			this.y=(int) y;
			this.z=(int) z;
			startTime=time;
		}

		public void render(Render2D r) {
			
			if(System.currentTimeMillis()-startTime>tTime)
				r.draw(Texture.getSpriteSheet(sprite, 12, 12, 0), x, y-z*3,y+z*4);
			else{
				float time=System.currentTimeMillis();
				int num=(int) (System.currentTimeMillis()/150%8);
				boolean flip=false;
				num%=4;
				if(num==3){
					num=1;
					flip=true;
				}
				
				double cha=(System.currentTimeMillis()-startTime)/(tTime*1.00);
				cha=1-cha;
				
				r.draw(Texture.getSpriteSheet(sprite,12,12,Math.abs(num),flip), (int)(x+tLength*cha), (int)(y-z*3-tHeight*Math.sin(cha*Math.PI)),y+z*4);
			}
		}
		public void render(Render2D r,int x,int y) {
			int tLength=-(this.x+x);
			if(this.x+x>r.width)
				tLength=r.width-(this.x+x);
				if(System.currentTimeMillis()>startTime){
					if(System.currentTimeMillis()-startTime>tTime)
						r.draw(Texture.getSpriteSheet(sprite, 12, 12, 0), this.x+x, this.y-z*3+y,this.y+z*4+y);
				else{
					float time=System.currentTimeMillis();
					int num=(int) (System.currentTimeMillis()/150%8);
					boolean flip=false;
					num%=4;
					if(num==3){
						num=1;
						flip=true;
					}
				
					double cha=(System.currentTimeMillis()-startTime)/(tTime*1.00);
					cha=1-cha;
				
					r.draw(Texture.getSpriteSheet(sprite,12,12,Math.abs(num),flip), (int)(this.x+tLength*cha)+x, (int)(this.y-z*3-tHeight*Math.sin(cha*Math.PI))+y,this.y+z*4+y);
				}
			}
		}
	}