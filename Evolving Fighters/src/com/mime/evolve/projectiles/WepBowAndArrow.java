package com.mime.evolve.projectiles;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import com.mime.evolve.Game;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.input.Player;

public class WepBowAndArrow extends Projectile{
	final double shaftLength=20.5;
	public WepBowAndArrow(){
		size=1;
		damage=3.4;
		speed=3.5;
		reloadTime=350;
		bulletMax=10;
		name="bow and arrow";
	}
	public WepBowAndArrow(double x,double y,double dir,Player target,Player owner,double speed){
		super(x,y,dir,target,owner,speed);
		dir+=Math.tan(Game.rand.nextDouble()+Game.rand.nextInt())*owner.broadCast;
		size=4;
	}
	public void draw(Render2D r) {
		
		double sDepth=y+Math.sin(dir)*-(shaftLength/2.0);
		int newY=Render2D.visualY(y);
		
		
		int Xs[]=new int[3];
		int Ys[]=new int[3];
		Xs[0]=(int) (x+Math.cos(dir)*size);
		Ys[0]=(int) (newY+Math.sin(dir)*size);
		Xs[1]=(int) (x+Math.cos(dir+Math.PI*2/3)*size);
		Ys[1]=(int) (newY+Math.sin(dir+Math.PI*2/3)*size);
		Xs[2]=(int) (x+Math.cos(dir-Math.PI*2/3)*size);
		Ys[2]=(int) (newY+Math.sin(dir-Math.PI*2/3)*size);
		Path2D poly=new Path2D.Double();
		poly.moveTo(Xs[0], Ys[0]);
		for(int i=1;i<Xs.length;i++)
			poly.lineTo(Xs[i], Ys[i]);
		
		Xs=new int[4];
		Ys=new int[4];
		Xs[0]=(int) (x+Math.cos(dir+Math.PI/2)*size/3.0);
		Ys[0]=(int) (newY+Math.sin(dir+Math.PI/2)*size/3.0);
		Xs[1]=(int) (x+Math.cos(dir-Math.PI/2)*size/3.0);
		Ys[1]=(int) (newY+Math.sin(dir-Math.PI/2)*size/3.0);
		Xs[2]=(int) (Xs[1]+Math.cos(dir)*-(shaftLength));
		Ys[2]=(int) (Ys[1]+Math.sin(dir)*-(shaftLength));
		Xs[3]=(int) (Xs[0]+Math.cos(dir)*-(shaftLength));
		Ys[3]=(int) (Ys[0]+Math.sin(dir)*-(shaftLength));
		Path2D sPoly=new Path2D.Double();
		sPoly.moveTo(Xs[0], Ys[0]);
		for(int i=1;i<Xs.length;i++)
			sPoly.lineTo(Xs[i], Ys[i]);
		
		double oThickness=1;
		Rectangle2D rec=poly.getBounds().createUnion(sPoly.getBounds());
		for(int y=(int) rec.getMinY();y<rec.getMaxY();y++){
			for(int x=(int) rec.getMinX();x<rec.getMaxX();x++){
				int depth=(int) (y+owner.size*0.75);
				if(x>=0&&x<r.width&&y>=0&&y<r.height){
					if(poly.contains(x, y)&&r.depthMap[x+y*r.width]<depth){
						r.pixels[x+y*r.width]=0x606060;
						r.depthMap[x+y*r.width]=(int) depth;
					}
					else if(poly.intersects(x-oThickness/3.00, y-oThickness/3.00,oThickness,oThickness)&&r.depthMap[x+y*r.width]<(int)depth){
						r.depthMap[x+y*r.width]=(int)depth;
						r.pixels[x+y*r.width]=0x1;
					}
					if(sPoly.contains(x, y)&&r.depthMap[x+y*r.width]<depth){
						r.pixels[x+y*r.width]=0x964B00;
						r.depthMap[x+y*r.width]=(int) depth;
					}
					else if(sPoly.intersects(x-oThickness/3.00, y-oThickness/3.00,oThickness,oThickness)&&r.depthMap[x+y*r.width]<(int)depth){
						r.depthMap[x+y*r.width]=(int)depth;
						r.pixels[x+y*r.width]=0x1;
					}
				}
			}
		}
	}
	public void newProjectile(Player p,boolean[] gene){
		game=p.getGame();
		game.alterProjectiles(new WepBowAndArrow(p.x,p.y,p.direction+Math.tan((game.time%(Math.PI/2))-(Math.PI/4))*(Math.PI/26.000),game.getEnemy(p),p,speed),1);
	}
}
