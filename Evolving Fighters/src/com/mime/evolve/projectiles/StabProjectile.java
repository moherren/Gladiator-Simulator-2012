package com.mime.evolve.projectiles;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import com.mime.evolve.Game;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.input.Player;

public class StabProjectile extends Projectile{

	ArrayList<Projectile> brothers;
	double dis;
	
	public StabProjectile(){
		reloadTime=25;
		bulletMax=0;
		endTime=10;
		size=4;
		damage=1.5;
		meele=true;
		speed=2;
		name="stab weapon";
	}
	public StabProjectile(double x, double y, double dir, Player target,Player owner,int size,double speed,long endTime,double damage,ArrayList<Projectile> brothers) {
		super(x, y, dir, target, owner,speed*owner.size);
		reloadTime=30;
		bulletMax=0;
		this.endTime=endTime;
		this.size=size;
		this.damage=damage;
		meele=true;
		endTime/=owner.power;
		this.brothers=brothers;
		dis=Point.distance(x, y, owner.x, owner.y);
	}
	public void draw(Render2D r){
		if(brothers.get(0).equals(this)){
			double Xs[]=new double[3];
			double Ys[]=new double[3];
			Projectile end=brothers.get(brothers.size()-1);
			Xs[0]=x+Math.cos(dir+Math.PI*3/4)*size*1.33;
			Ys[0]=Render2D.visualY(y)+Math.sin(dir+Math.PI*3/4)*size*1.33-owner.size*1.25;
			Xs[1]=x+Math.cos(dir-Math.PI*3/4)*size*1.33;
			Ys[1]=Render2D.visualY(y)+Math.sin(dir-Math.PI*3/4)*size*1.33-owner.size*1.25;
			Xs[2]=end.x+Math.cos(dir)*size;
			Ys[2]=Render2D.visualY(end.y)+Math.sin(dir)*size-owner.size*1.25;
			Path2D poly=new Path2D.Double();
			poly.moveTo(Xs[0], Ys[0]);
			for(int i=1;i<Xs.length;i++)
				poly.lineTo(Xs[i], Ys[i]);
			
			
			double oThickness=1;
			Rectangle rec=poly.getBounds();
			for(int x=(int) rec.getMinX();x<rec.getMaxX();x++){
				for(int y=(int) rec.getMinY();y<rec.getMaxY();y++){
					double depth=y+owner.size*0.75;
					if(x>=0&&x<r.width&&y>=0&&y<r.height)
					if(poly.contains(x,y)&&r.depthMap[x+y*r.width]<depth){
						r.depthMap[x+y*r.width]=(int) depth;
						r.pixels[x+y*r.width]=0x909090;
					}
					else if(poly.intersects(x-oThickness/3.00, y-oThickness/3.00,oThickness,oThickness)&&r.depthMap[x+y*r.width]<(int)depth){
						r.depthMap[x+y*r.width]=(int) depth;
						r.pixels[x+y*r.width]=0x1;
					}
				}
			}
		}
	}
	public void newProjectile(Player p,boolean[] gene){
		game=p.getGame();
		ArrayList<Projectile> brothers=new ArrayList<Projectile>();
		brothers.add(game.createProjectile(new StabProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed/10.000,endTime,damage,brothers)));
	}
	public void tick(){
		updatePosition();
		if(!meele)
			game.resetCountdown();
		if(Math.sqrt(Math.pow(x-target.x, 2)+Math.pow(y-target.y, 2))<=target.size+size&&damage!=0){
			
			target.damage(damage*owner.power);
			owner.fitness+=(damage*owner.power*1.000)/(target.maxHealth*1.000)*45;
			target.move(dir+Math.PI, damage*2);
			for(Projectile proj:brothers){
				proj.damage=0;
			}
		}
		if(startTime+endTime==game.time&&endTime!=0)game.destroyProjectile(this);
	}
	public void updatePosition(){
		//double nx=owner.x-Math.cos(owner.direction)*size,ny;
		dir=owner.direction;
		x=owner.x-Math.cos(owner.direction)*((startTime-game.time)*speed-owner.size-dis);
		y=owner.y-Math.sin(owner.direction)*((startTime-game.time)*speed-owner.size-dis);
	}
	public void willHit(){
		if(Player.rangeOfDirection(x, target.x, y, target.y, dir, 0, target.size+size)&&Point.distance(x, y, target.x, target.y)-size-target.size<=speed*endTime){
			//owner.fitness+=1;
			miss=false;
		}
	}
}
