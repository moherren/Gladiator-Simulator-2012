package com.mime.evolve.projectiles;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import com.mime.evolve.Display;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.input.Player;

public class WepSpear extends StabProjectile{
	public WepSpear(){
		super();
		reloadTime=80;
		bulletMax=0;
		endTime=20;
		size=4;
		damage=1.2;
		meele=true;
		speed=0.3;
		name="spear";
		dis=20;
	}
	public WepSpear(double x, double y, double direction, Player enemy,
			Player p, int size, double d, long endTime, double damage,
			ArrayList<Projectile> brothers) {
		super(x,y,direction,enemy,p,size,d,endTime,damage,brothers);
	}
	public void draw(Render2D r){
		if(brothers.indexOf(this)==0){
			Projectile end=brothers.get(brothers.size()-1);
			double newY=Render2D.visualY(y),newEndY=Render2D.visualY(end.y);
			
			int[] Xs=new int[4];
			int[] Ys=new int[4];
			Xs[0]=(int) (x+Math.cos(dir+Math.PI)*size);
			Xs[1]=(int) (x+Math.cos(dir+Math.PI/2)*size);
			Xs[2]=(int) (end.x+Math.cos(dir)*end.size);
			Xs[3]=(int) (x+Math.cos(dir-Math.PI/2)*size);
			Ys[0]=(int) (newY+Math.sin(dir+Math.PI)*size-owner.size*0.75);
			Ys[1]=(int) (newY+Math.sin(dir+Math.PI/2)*size-owner.size*0.75);
			Ys[2]=(int) (newEndY+Math.sin(dir)*end.size-owner.size*0.75);
			Ys[3]=(int) (newY+Math.sin(dir-Math.PI/2)*size-owner.size*0.75);
			Path2D head=new Path2D.Double();
			head.moveTo(Xs[0], Ys[0]);
			for(int i=1;i<Xs.length;i++)
				head.lineTo(Xs[i], Ys[i]);
			
			Xs[0]=(int) (x+Math.cos(dir+Math.PI/2)*size/2);
			Xs[1]=(int) (x+Math.cos(dir-Math.PI/2)*size/2);
			Xs[2]=(int) (Xs[1]+Math.cos(dir+Math.PI)*dis*2);
			Xs[3]=(int) (Xs[0]+Math.cos(dir+Math.PI)*dis*2);
			Ys[0]=(int) (newY+Math.sin(dir+Math.PI/2)*size/2*Render2D.hDisplacement-owner.size*0.75);
			Ys[1]=(int) (newY+Math.sin(dir-Math.PI/2)*size/2*Render2D.hDisplacement-owner.size*0.75);
			Ys[2]=(int) (Ys[1]+Math.sin(dir+Math.PI)*dis*2*Render2D.hDisplacement);
			Ys[3]=(int) (Ys[0]+Math.sin(dir+Math.PI)*dis*2*Render2D.hDisplacement);
			Path2D handle=new Path2D.Double();
			handle.moveTo(Xs[0], Ys[0]);
			for(int i=1;i<Xs.length;i++)
				handle.lineTo(Xs[i], Ys[i]);
			int handleDepth=(int) (handle.getBounds().getCenterY()+owner.size*0.75);
			int headDepth=(int) (head.getBounds().getCenterY()+owner.size*0.75);
			Rectangle rect=head.getBounds();
			rect.add(handle.getBounds());
			int botX=(int) Math.max(0, rect.getMinX());
			int botY=(int) Math.max(0, rect.getMinY());
			int topX=(int) Math.min(Display.WIDTH, rect.getMaxX());
			int topY=(int) Math.min(Display.HEIGHT, rect.getMaxY());
			double oThickness=1;
			
			for(int x=botX;x<topX;x++){
				for(int y=botY;y<topY;y++){
					handleDepth=(int) (y+owner.size*0.75);
					if(handle.contains(x, y)&&r.depthMap[x+y*r.width]<handleDepth){
						r.depthMap[x+y*r.width]=handleDepth;
						r.pixels[x+y*r.width]=0x964B00;
					}
					else if(handle.intersects(x-oThickness/3.00, y-oThickness/3.00,oThickness,oThickness)&&r.depthMap[x+y*r.width]<(int)handleDepth){
						r.depthMap[x+y*r.width]=handleDepth;
						r.pixels[x+y*r.width]=0x1;
					}
					if(head.contains(x, y)&&r.depthMap[x+y*r.width]<headDepth){
						r.depthMap[x+y*r.width]=handleDepth;
						r.pixels[x+y*r.width]=0x909090;
					}
					else if(head.intersects(x-oThickness/3.00, y-oThickness/3.00,oThickness,oThickness)&&r.depthMap[x+y*r.width]<(int)headDepth){
						r.depthMap[x+y*r.width]=headDepth;
						r.pixels[x+y*r.width]=0x1;
					}
				}
			}

		}
	}
	public void newProjectile(Player p,boolean gene[]){
		game=p.getGame();
		ArrayList<Projectile> brothers=new ArrayList<Projectile>();
		brothers.add(game.alterProjectiles(new WepSpear(p.x+dis,p.y,p.direction,game.getEnemy(p),p,size,speed,endTime,damage,brothers),1));
		brothers.add(game.alterProjectiles(new WepSpear(p.x+dis+size+size/2,p.y,p.direction,game.getEnemy(p),p,size-2,speed,endTime,damage,brothers),1));
	}
}
