package com.mime.evolve.projectiles;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import com.mime.evolve.Display;
import com.mime.evolve.Game;
import com.mime.evolve.graphics.Render;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.input.Player;

public class WepFlail extends Projectile{

	double range=Math.PI/2;
	double calcDirection=0;
	double X=0,Y=0;
	
	public WepFlail(){
		reloadTime=120;
		bulletMax=0;
		endTime=50;
		size=9;
		damage=5;
		meele=true;
		speed=10;
		name="flail";
		addedSpeed=-0.33;
	}
	public WepFlail(double x, double y, double dir, Player target,Player owner,int size,double speed,long endTime,double damage,double range) {
		super(x, y, dir, target, owner,speed);
		this.speed+=owner.size;
		reloadTime=30;
		bulletMax=0;
		this.endTime=endTime;
		this.size=size;
		this.damage=damage;
		meele=true;
		endTime/=owner.power;
		this.range=range;
	}
	public void draw(Render2D r){
			Ellipse2D ball;
			double oThickness=1;
			
			int newY=Render2D.visualY(y),newY2=Render2D.visualY(Y),newOwnerY=Render2D.visualY(owner.y);
			
			//marks out the points on the handle
			int[] Xs=new int[4];
			int[] Ys=new int[4];
			Xs[0]=(int)((owner.x+Math.cos(calcDirection)*(owner.size))+Math.cos(calcDirection+Math.PI*0.5)*2);
			Xs[1]=(int)((owner.x+Math.cos(calcDirection)*(owner.size))+Math.cos(calcDirection-Math.PI*0.5)*2);
			Xs[2]=(int)(X+Math.cos(calcDirection-Math.PI*0.5)*2);
			Xs[3]=(int)(X+Math.cos(calcDirection+Math.PI*0.5)*2);
			Ys[0]=(int)((newOwnerY+Math.sin(calcDirection)*(owner.size))+Math.sin(calcDirection+Math.PI*0.5)*2);
			Ys[1]=(int)((newOwnerY+Math.sin(calcDirection)*(owner.size))+Math.sin(calcDirection-Math.PI*0.5)*2);
			Ys[2]=(int)(newY2+Math.sin(calcDirection-Math.PI*0.5)*2*Render.hDisplacement);
			Ys[3]=(int)(newY2+Math.sin(calcDirection+Math.PI*0.5)*2*Render.hDisplacement);
			for(int n=0;n<4;n++){
				Ys[n]-=owner.size*0.75;
			}
			Path2D handle=new Path2D.Double();
			handle.moveTo(Xs[0], Ys[0]);
			for(int i=1;i<Xs.length;i++)
				handle.lineTo(Xs[i], Ys[i]);
			
			
			//marks out the points on the handle
			double chainDir=Math.atan2(newY2 - newY, X - x);
			Xs[0]=(int)(X+Math.cos(chainDir+Math.PI*0.5));
			Xs[1]=(int)(X+Math.cos(chainDir-Math.PI*0.5));
			Xs[2]=(int)(x+Math.cos(calcDirection-Math.PI*0.5));
			Xs[3]=(int)(x+Math.cos(calcDirection+Math.PI*0.5));
			Ys[0]=(int)(newY2+Math.sin(calcDirection+Math.PI*0.5)*Render.hDisplacement);
			Ys[1]=(int)(newY2+Math.sin(calcDirection-Math.PI*0.5)*Render.hDisplacement);
			Ys[2]=(int)(newY+Math.sin(calcDirection-Math.PI*0.5)*Render.hDisplacement);
			Ys[3]=(int)(newY+Math.sin(calcDirection+Math.PI*0.5)*Render.hDisplacement);
			for(int n=0;n<4;n++){
				Ys[n]-=owner.size*0.75;
			}
			Path2D chain=new Path2D.Double();
			chain.moveTo(Xs[0], Ys[0]);
			for(int i=1;i<Xs.length;i++)
				chain.lineTo(Xs[i], Ys[i]);
			
			ball=new Ellipse2D.Double(x-size, newY-size-owner.size*0.75,size,size);
			
			Rectangle rect=chain.getBounds().union(handle.getBounds()).union(ball.getBounds());
			int botX=(int) Math.max(0, rect.getMinX());
			int botY=(int) Math.max(0, rect.getMinY());
			int topX=(int) Math.min(Display.WIDTH, rect.getMaxX());
			int topY=(int) Math.min(Display.HEIGHT, rect.getMaxY());
			int handleDepth=(int) (handle.getBounds().getCenterY()),chainDepth=(int) ((chain.getBounds().getCenterY())),ballDepth=(int) (Render2D.visualY(y));
			
			//draws the handle
			for(int x=botX;x<topX;x++){
				for(int y=botY;y<topY;y++){
					if(x>=0&&x<r.width&&y>=0&&y<r.height){
						if(handle.contains(x,y)&&r.depthMap[x+y*r.width]<handleDepth){
							r.depthMap[x+y*r.width]=handleDepth;
							r.pixels[x+y*r.width]=0x964B00;
						}
						else if(handle.intersects(x-oThickness/3.00, y-oThickness/3.00,oThickness,oThickness)&&r.depthMap[x+y*r.width]<handleDepth){
							r.depthMap[x+y*r.width]=(int) handleDepth;
							r.pixels[x+y*r.width]=0x1;
						}
						
						if(chain.contains(x,y)&&r.depthMap[x+y*r.width]<chainDepth){
							r.depthMap[x+y*r.width]=chainDepth;
							r.pixels[x+y*r.width]=0x707070;
						}
						else if(chain.intersects(x-oThickness/3.00, y-oThickness/3.00,oThickness,oThickness)&&r.depthMap[x+y*r.width]<chainDepth){
							r.depthMap[x+y*r.width]=(int) chainDepth;
							r.pixels[x+y*r.width]=0x1;
						}
						
						if(ball.contains(x,y)&&r.depthMap[x+y*r.width]<ballDepth){
							r.depthMap[x+y*r.width]=ballDepth;
							r.pixels[x+y*r.width]=0x909090;
						}
						else if(ball.intersects(x-oThickness/3.00, y-oThickness/3.00,oThickness,oThickness)&&r.depthMap[x+y*r.width]<ballDepth){
							r.depthMap[x+y*r.width]=(int) ballDepth;
							r.pixels[x+y*r.width]=0x1;
						}
					}
				}
			}
	}
	public void newProjectile(Player p,boolean[] gene){
		game=p.getGame();
		game.alterProjectiles(new WepFlail(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed,endTime,damage,range),1);
	}
	public void tick(){
		updatePosition();
		if(!owner.species.projectile.meele)
			game.resetCountdown();
		if(Math.sqrt(Math.pow(x-target.x, 2)+Math.pow(y-target.y, 2))<=target.size+size&&damage!=0){
			target.damage(damage*owner.power);
			owner.fitness+=(damage*owner.power*1.000)/target.maxHealth*45;
			target.move(dir+Math.PI, damage*3.5);
			damage=0;
		}
		if(game.executor!=null)
		if(Math.sqrt(Math.pow(x-game.executor.x, 2)+Math.pow(y-game.executor.y, 2))<=game.executor.size+size&&damage!=0){
			game.executor.damage(damage*owner.power);
			owner.fitness+=(damage*owner.power*1.000)/target.maxHealth*45;
			game.executor.move(dir+Math.PI, damage*3.5);
			damage=0;
		}
		if(startTime+endTime==game.time)game.alterProjectiles(this,0);
	}
	public void updatePosition(){
		calcDirection=owner.direction+(startTime-game.time)*range/endTime+range/2;
		X=owner.x+Math.cos(calcDirection)*speed;
		Y=owner.y+Math.sin(calcDirection)*speed;
		double calcDirection=this.calcDirection+(startTime-game.time)*Math.PI/endTime+range/2;
		x=X+Math.cos(calcDirection)*speed*0.8;
		y=Y+Math.sin(calcDirection)*speed*0.8;
	}
	public void willHit(){
		if(Player.rangeOfDirection(x, target.x, y, target.y, dir, range, target.size+size)&&Point.distance(x, y, target.x, target.y)-size-target.size<=0&&Point.distance(x, y, target.x, target.y)+size+target.size>=0){
			//owner.fitness+=1;
			//System.out.println("right on!");
			miss=false;
		}
	}
}
