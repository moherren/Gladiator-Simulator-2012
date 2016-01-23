package com.mime.evolve.projectiles;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import com.mime.evolve.Game;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.input.Player;

public class WepKnife extends StabProjectile{
	public WepKnife(){
		super();
		reloadTime=30;
		endTime=15;
		size=3;
		speed=1;
		damage=1.65;
		name="knife";
		addedSpeed=0.6;
	}
	public WepKnife(double x, double y, double dir, Player target,Player owner,int size,double speed,long endTime,double damage,ArrayList<Projectile> brothers) {
		super(x,y,dir,target,owner,size,speed,endTime,damage,brothers);
	}
	public void draw(Render2D r){
		if(brothers.get(0).equals(this)){
			int Xs[]=new int[4];
			int Ys[]=new int[4];
			Projectile end=brothers.get(brothers.size()-1);
			int newEndY=Render2D.visualY(end.y),newY=Render2D.visualY(y);
			Xs[0]=(int) (x+Math.cos(dir+Math.PI*3/4)*size*1.33);
			Ys[0]=(int) (newY+Math.sin(dir+Math.PI*3/4)*size*1.33-owner.size*0.75);
			Xs[1]=(int) (x+Math.cos(dir-Math.PI*3/4)*size*1.33);
			Ys[1]=(int) (newY+Math.sin(dir-Math.PI*3/4)*size*1.33-owner.size*0.75);
			Xs[2]=(int) (end.x+Math.cos(dir-Math.PI/4.00)*size/2);
			Ys[2]=(int) (newEndY+Math.sin(dir-Math.PI/4.00)*size/2-owner.size*0.75);
			Xs[3]=(int) (end.x+Math.cos(dir+Math.PI/4.00*5)*size/2);
			Ys[3]=(int) (newEndY+Math.sin(dir+Math.PI/4.00*5)*size/2-owner.size*0.75);
			Path2D poly=new Path2D.Double();
			poly.moveTo(Xs[0], Ys[0]);
			for(int i=1;i<Xs.length;i++)
				poly.lineTo(Xs[i], Ys[i]);
			
			double depth;
			double oThickness=1;
			Rectangle rec=poly.getBounds();
			for(int x=(int) rec.getMinX();x<rec.getMaxX();x++){
				for(int y=(int) rec.getMinY();y<rec.getMaxY();y++){
					depth=y;
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
		brothers.add(game.alterProjectiles(new WepKnife(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed/10.000,endTime,damage,brothers),1));
		brothers.add(game.alterProjectiles(new WepKnife(p.x+size*3,p.y,p.direction,game.getEnemy(p),p,size,speed/10.000,endTime,damage,brothers),1));
	}
	public void tick(){
		updatePosition();
		if(!meele)
			game.resetCountdown();
		if(Math.sqrt(Math.pow(x-target.x, 2)+Math.pow(y-target.y, 2))<=target.size+size&&damage!=0){
			Player enemy=game.getEnemy(owner);
			if(Player.rangeOfDirection(enemy.x, x, enemy.y, y, enemy.direction+Math.PI,enemy.maxCast, size)&&damage!=0){
				target.damage(damage*owner.power*3);
				owner.fitness+=(damage*owner.power*3.000)/target.maxHealth*45;
			}
			else{
				target.damage(damage*owner.power);
				owner.fitness+=(damage*owner.power*1.000)/target.maxHealth*45;
			}
			target.move(dir+Math.PI, damage*2);
			for(Projectile proj:brothers){
				proj.damage=0;
			}
		}
		if(startTime+endTime==game.time&&endTime!=0)game.alterProjectiles(this,0);
	}
}
