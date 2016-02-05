package com.mime.evolve.projectiles;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import com.mime.evolve.Game;
import com.mime.evolve.graphics.Render;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.input.Player;

public class SwingProjectile extends Projectile{

	protected double range=Math.PI/2;
	protected ArrayList<Projectile> brothers;
	double calcDirection=0;
	
	public SwingProjectile(){
		reloadTime=45;
		bulletMax=0;
		endTime=10;
		size=4;
		damage=1.5;
		meele=true;
		speed=1.2;
		name="swing weapon";
	}
	public SwingProjectile(double x, double y, double dir, Player target,Player owner,int size,double speed,long endTime,double damage,double range,ArrayList<Projectile> brothers) {
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
		this.brothers=brothers;
	}
	public void draw(Render2D r){
		if(brothers.get(0).equals(this)){
			Projectile end=brothers.get(brothers.size()-1);
			int newEndY=Render2D.visualY(end.y),newY=Render2D.visualY(y);
			double[] Xs=new double[5];
			double[] Ys=new double[5];
			
			Xs[0]=x+Math.cos(calcDirection+Math.PI*3/4)*size;
			Ys[0]=newY+Math.sin(calcDirection+Math.PI*3/4)*size*Render.hDisplacement;
			Xs[1]=x+Math.cos(calcDirection-Math.PI*3/4)*size;
			Ys[1]=newY+Math.sin(calcDirection-Math.PI*3/4)*size*Render.hDisplacement;
			Xs[2]=end.x+Math.cos(calcDirection-Math.PI/2.000)*end.size;
			Ys[2]=newEndY+Math.sin(calcDirection-Math.PI/2.000)*end.size*Render.hDisplacement;
			Xs[3]=end.x+Math.cos(calcDirection)*end.size;
			Ys[3]=newEndY+Math.sin(calcDirection)*end.size*Render.hDisplacement;
			Xs[4]=end.x+Math.cos(calcDirection+Math.PI/2.000)*end.size;
			Ys[4]=newEndY+Math.sin(calcDirection+Math.PI/2.000)*end.size*Render.hDisplacement;
			for(int n=0;n<5;n++){
				Ys[n]-=owner.size*1.25;
			}
			Path2D poly=new Path2D.Double();
			poly.moveTo(Xs[0], Ys[0]);
			for(int i=1;i<Xs.length;i++)
				poly.lineTo(Xs[i], Ys[i]);
			Rectangle rec=poly.getBounds();
			int topX=(int) rec.getMaxX(),botX=(int) rec.getMinX(),topY=(int) rec.getMaxY(),botY=(int) rec.getMinY();
			int depth=(int) ((y+end.y)/2.00);
			
			double oThickness=1;
			rec.setFrameFromCenter(rec.getCenterX(), rec.getCenterY(), rec.getWidth()/2.00+1, rec.getHeight()/2.00+1);
			for(int x=botX;x<topX;x++){
				for(int y=botY;y<topY;y++){
					if(x>=0&&x<r.width&&y>=0&&y<r.height){
						depth=(int) (y)+(int)(owner.size*0.75);
						if(poly.contains(x,y)&&r.depthMap[x+y*r.width]<depth){
							r.depthMap[x+y*r.width]=depth;
							r.pixels[x+y*r.width]=0x909090;
						}
						else if(poly.intersects(x-oThickness/3.00, y-oThickness/3.00,oThickness,oThickness)&&r.depthMap[x+y*r.width]<(int)depth){
							r.depthMap[x+y*r.width]=depth;
							r.pixels[x+y*r.width]=0x1;
						}
					}
				}
			}
		}
	}
	public void newProjectile(Player p,boolean[] gene){
		game=p.getGame();
		ArrayList<Projectile> brothers=new ArrayList<Projectile>();
		brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed,endTime,damage,range,brothers),1));
		brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size*2,endTime,damage,range,brothers),1));
		brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size*4,endTime,damage,range,brothers),1));
	}
	public void tick(){
		updatePosition();
		if(Math.sqrt(Math.pow(x-target.x, 2)+Math.pow(y-target.y, 2))<=target.size+size&&damage!=0){
			target.damage(damage*owner.power);
			owner.fitness+=(damage*owner.power*1.000)/target.maxHealth*45;
			for(int i=0;i<brothers.size();i++){
				Projectile proj=brothers.get(i);
				if(proj!=null)
				proj.damage=0;
			}
			target.move(dir+Math.PI, damage*2);
		}
		if(game.executor!=null)
		if(Math.sqrt(Math.pow(x-game.executor.x, 2)+Math.pow(y-game.executor.y, 2))<=game.executor.size+size&&damage!=0){
			game.executor.damage(damage*owner.power);
			//owner.fitness+=(damage*owner.power*1.000)/target.maxHealth*45;
			for(int i=0;i<brothers.size();i++){
				Projectile proj=brothers.get(i);
				if(proj!=null)
				proj.damage=0;
			}
			game.executor.move(dir+Math.PI, damage*2);
		}
		if(startTime+endTime==game.time&&endTime!=0)game.alterProjectiles(this,0);
	}
	public void updatePosition(){
		calcDirection=owner.direction+(startTime-game.time)*range/endTime+range/2;
		x=owner.x+Math.cos(calcDirection)*speed;
		y=owner.y+Math.sin(calcDirection)*speed;
	}
	public void willHit(){
		if(Player.rangeOfDirection(x, target.x, y, target.y, dir, range, target.size+size)&&Point.distance(x, y, target.x, target.y)-size-target.size<=0&&Point.distance(x, y, target.x, target.y)+size+target.size>=0){
			//System.out.println("right on!");
			miss=false;
		}
	}
}
