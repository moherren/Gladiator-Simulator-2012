package com.mime.evolve.projectiles;

import com.mime.evolve.Game;
import com.mime.evolve.graphics.Drawable;
import com.mime.evolve.graphics.Render;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.input.Player;

public class Projectile implements Drawable{
	public double x=0,y=0;
	public double dir=0,speed=2.5,vSpeed=0,hSpeed=0;
	public double damage=4, addedSpeed=0;
	public Player target;
	public int size=8;
	public Player owner;
	public boolean miss=true,meele=false;
	protected Game game;
	public long startTime=0,endTime=0;
	public int reloadTime=200,bulletMax=15;
	public String name="";
	
	public Projectile(){
		
	}
	public Projectile(double x,double y,double dir,Player target,Player owner,double speed){
		this.x=x;
		this.y=y;
		this.dir=dir;
		this.speed=speed;
		this.speed*=owner.power;
		vSpeed=Math.sin(dir)*this.speed;
		hSpeed=Math.cos(dir)*this.speed;
		this.target=target;
		this.owner=owner;
		game=owner.getGame();
		startTime=game.time;
		willHit();
		if(!owner.species.projectile.meele)
			game.resetCountdown();
	}
	@Override
	public void draw(Render2D r) {
		double depth=y;
		for(int y=-size;y<size*2;y++){
			for(int x=-size;x<size*2;x++){
				int xPix=(int)(x+this.x);
				int yPix=(int)(y+this.y-owner.size*Render.hDisplacement);
				if(xPix>=0&&xPix<r.width&&yPix>=0&&yPix<r.height)
					if(Math.sqrt(Math.pow(x, 2)*Render.hDisplacement+Math.pow(y, 2)*Render.vDisplacement)<=size&&r.depthMap[xPix+yPix*r.width]<depth){
						r.pixels[xPix+yPix*r.width]=0xff00ff;
						r.depthMap[xPix+yPix*r.width]=(int) depth;
					}
			}
		}
	}
	public void newProjectile(Player p,boolean[] gene){
		game.alterProjectiles(new Projectile(p.x,p.y,p.direction,game.getEnemy(p),p,speed),1);
	}
	public void tick(){
		updatePosition();
		if(Math.sqrt(Math.pow(x-target.x, 2)+Math.pow(y-target.y, 2))<=target.size+size){
			target.damage(damage*owner.power);
			owner.fitness+=(damage*owner.power*1.000)/target.maxHealth*45;
			target.move(dir+Math.PI, damage*2);
			game.alterProjectiles(this,0);
			damage=0;
		}
		if(game.executor!=null)
		if(Math.sqrt(Math.pow(x-game.executor.x, 2)+Math.pow(y-game.executor.y, 2))<=game.executor.size+size){
			game.executor.damage(damage*owner.power);
			//owner.fitness+=(damage*owner.power*1.000)/target.maxHealth*45;
			game.executor.move(dir+Math.PI, damage*2);
			game.alterProjectiles(this,0);
			damage=0;
		}
		if(Math.sqrt(Math.pow(x-600, 2)+Math.pow(y-400,2))>360){
			game.alterProjectiles(this,0);
		}
		if(startTime+endTime==game.time&&endTime!=0)game.alterProjectiles(this,0);
	}
	public void updatePosition(){
		x+=hSpeed;
		y+=vSpeed;
	}
	public void willHit(){
		if(Player.rangeOfDirection(x, target.x, y, target.y, dir, 0, target.size+size-1)){
				//miss=false;
			}
	}
}
