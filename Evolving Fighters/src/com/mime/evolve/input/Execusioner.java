package com.mime.evolve.input;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import com.mime.evolve.Game;
import com.mime.evolve.sound.SoundHandler;
import com.mime.evolve.species.Species;
import com.mime.evolve.graphics.Render;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.projectiles.Projectile;
import com.mime.evolve.projectiles.SwingProjectile;

public class Execusioner extends Player{
	boolean inRing=false;
	final static double height=1.5;
	double walkingOld=0;
	public Execusioner(boolean[] DNA, Game game) {
		super(600, 0, -Math.PI/2.000, new ExecutorSpecies(), DNA, game);
		species.projectile=new WarAxe();
		size=19;
		power=1;
		speed=1;
		maxHealth=20;
		health=maxHealth;
		
	}
	
	public void tick(Game game){
		if(y<100&&!inRing)
			moveForward(1);
		else{
			species.tick(game, this);
			if(!inRing){
				inRing=true;
				SoundHandler.play(SoundHandler.EX2);
			}
		}
	}
	public void move(double direction,double distance){
		if(Math.sqrt(Math.pow(x-distance*Math.cos(direction)-600, 2)+Math.pow(y-400, 2))<=360-size||!inRing)
			x=x-distance*Math.cos(direction);
		if(Math.sqrt(Math.pow(x-600, 2)+Math.pow(y-400-distance*Math.sin(direction), 2))<=360-size||!inRing)
			y=y-distance*Math.sin(direction);
	}
public void drawAlive(Render2D r) {
		
		int newY=Render2D.visualY(y);
		double walking=(1+Math.cos(game.time/24.000))*3.0000;
			
		 if(this.walking==0)
			 walking=0;
		 Rectangle rec=new Rectangle();
		 rec.setBounds((int) x-size,(int)(newY-size*height-this.walking),(int)size*2,(int)(size*height));
		 int depth=(int) Render2D.visualY(y);
		 double[] eyeDir=new double[]{direction+Math.PI*0.125,direction-Math.PI*0.125};
		 int headX=(int) rec.getCenterX(),headY=rec.y;
		 
		 
		 if(Math.sin(eyeDir[0])>0){
			 double x=headX+Math.cos(eyeDir[0])*size;
			 double y=headY+Math.sin(eyeDir[0])*size*Render.hDisplacement;
			 for(int X=(int)x-1;X<(int)x+2;X++){
				 for(int Y=(int)y-1;Y<(int)y+2;Y++){
					 if(r.depthMap[X+Y*r.width]<depth&&!(X==(int)(x+1)&&Y==(int)(y-1))){
					 r.pixels[((int)X+(int)Y*r.width)]= 0xffffff;
					 r.depthMap[X+Y*r.width]=(int) depth;
					 }
				 }
			 }
		 }
		if(Math.sin(eyeDir[1])>0){
			 double x=headX+Math.cos(eyeDir[1])*size;
			 double y=headY+Math.sin(eyeDir[1])*size*Render.hDisplacement;
			 for(int X=(int)x-1;X<(int)x+2;X++){
				 for(int Y=(int)y-1;Y<(int)y+2;Y++){
					 if(r.depthMap[X+Y*r.width]<depth&&!(X==(int)(x-1)&&Y==(int)(y-1))){
						 r.pixels[((int)X+(int)Y*r.width)]= 0xffffff;
						 r.depthMap[X+Y*r.width]=(int) depth;
						 }
				 }
			 }
		}
		
		
		for(double i=0;i<Math.PI*2;i+=Math.PI/90.000){
				  int x=(int) (rec.getCenterX()+Math.cos(i)*size);
				  int y=(int) (rec.getMinY()+Math.sin(i)*size);
				  if(r.depthMap[x+y*r.width]<=depth){
						r.pixels[x+y*r.width]= 1;
						r.depthMap[x+y*r.width]=(int) depth;
					 }
			  }
		 for(int x=(int) (rec.x);x<(int)rec.getCenterX()+size;x++){
			 for(int y=(int) (rec.y-size);y<rec.y+size;y++){
				 if(Math.sqrt(Math.pow(rec.getCenterX()-x, 2)+Math.pow(rec.getMinY()-y, 2))<=size&&r.depthMap[x+y*r.width]<depth){
					r.pixels[x+y*r.width]= species.color;
					r.depthMap[x+y*r.width]=(int) depth;
				 }
			 }
		 }
		 
		 Render2D armor=species.getArmor();
		 for(int X=(int) 0;X<size*2;X++){
			 int x=rec.x+X;
			 for(int Y= 0;Y<size*height;Y++){
				 int y=Y+(int)(Math.sin(Math.acos((X-size)/(1.00*size)))*Render2D.hDisplacement*size)+rec.y;
				 int aX= (int) ((((X/(size*4.000))+((direction-Math.PI/2)%(Math.PI*2)/(Math.PI*2))))*armor.width%armor.width);
				 int aY= (int) ((Y/(size*height))*armor.height);
				 if(aX<0)
					 aX+=armor.width;
				 int color=armor.pixels[aX+aY*armor.width];
				 if(X==0||X==size*2-1||Y==(int)(size*height)-1)
					 color=1;
				 if(r.depthMap[x+y*r.width]<depth){
					r.pixels[x+y*r.width]= color;
					r.depthMap[x+y*r.width]=(int) depth;
				 }
			 }
		 }
		
	}

public void drawDead(Render2D r){
	int newY=Render2D.visualY(y);
	Rectangle rec=new Rectangle();
	rec.setBounds((int) x-size,(int)(newY-size*1.5-walking),(int)size*2,(int)(size*1.5));
	int depth=(int) Render2D.visualY(y);
	double dieingTime=100.000;
	double deathChange=Math.min(1,(game.getTime()-deathTime)/dieingTime);
	Render2D armor=species.getArmor();
	double direction=this.direction-Math.PI/2;
	double alias=Math.sin(Math.toRadians(48.6-90*deathChange));
	double oAlias=Math.cos(Math.toRadians(48.6-90*deathChange));
	double yMax=Math.abs(alias*size)+Math.abs(2*oAlias*size);
	
	double xInc=(Math.PI*2)/(8.000*size);
	for(double X=0;X<Math.PI*2;X+=xInc){
		for(int Y=0;Y<yMax;Y++){
			int x=(int) (Math.cos(X+direction)*size+this.x),y=(int) (Math.sin(direction+X)*alias*size+newY+Y-yMax);
			int aX=(int) (X/(2.000*Math.PI)*armor.width),aY=(int) (Y/yMax*armor.height);
			int color=armor.pixels[(aX)%armor.width+aY*armor.width];
			depth=(int) (newY+Math.sin(direction+X)*size);
			if(x>=size+this.x||x<=this.x-size||Y==0||Y+1>=yMax)
				color=1;
			else if(Math.sin(X+direction)<0){
				color=species.color;
				depth=(int) (newY-alias);
			}
			
			if(r.depthMap[x+y*r.width]<depth){
				r.pixels[x+y*r.width]=color;
				r.depthMap[x+y*r.width]=depth;
			}
		}
	}
	
	
	double[] eyeDir=new double[]{direction+Math.PI*0.125+Math.PI/2,direction-Math.PI*0.125+Math.PI/2};
	int headX=(int) rec.getCenterX(),headY=(int) (newY-yMax);
	depth=newY;

	for(double i=0;i<Math.PI*2;i+=Math.PI/90.000){
		  int x=(int) (rec.getCenterX()+Math.cos(i)*size);
		  int y=(int) ((newY-yMax)+Math.sin(i)*size);
		  if(r.depthMap[x+y*r.width]<=depth){
				r.pixels[x+y*r.width]= 1;
				r.depthMap[x+y*r.width]=(int) (depth+alias);
			 }
	  }
	for(int x=(int) (rec.x);x<(int)rec.getCenterX()+size;x++){
		for(int y=(int) (headY-size);y<headY+size;y++){
			if(Math.sqrt(Math.pow(rec.getCenterX()-x, 2)+Math.pow(headY-y, 2))<=size&&r.depthMap[x+y*r.width]<=depth){
				r.pixels[x+y*r.width]= species.color;
				r.depthMap[x+y*r.width]=(int) (depth+alias);
			}
	}
 }
	
	if(Math.sin(eyeDir[0])>0){
		 double x=headX+Math.cos(eyeDir[0])*size;
		 double y=headY+Math.sin(eyeDir[0])*size*alias;
		 for(int X=(int)x-1;X<(int)x+2;X++){
			 for(int Y=(int)y-1;Y<(int)y+2;Y++){
				 if(r.depthMap[X+Y*r.width]<=depth&&((X==Y)||(X==-Y))){
					 r.pixels[((int)X+(int)Y*r.width)]= 0xffffff;
					 r.depthMap[X+Y*r.width]=(int) (depth+alias);
				 }
			 }
		 }
	 }
	if(Math.sin(eyeDir[1])>0){
		 double x=headX+Math.cos(eyeDir[1])*size;
		 double y=headY+Math.sin(eyeDir[1])*size*alias;
		 for(int X=(int)x-1;X<(int)x+2;X++){
			 for(int Y=(int)y-1;Y<(int)y+2;Y++){
				 if(r.depthMap[X+Y*r.width]<=depth){
					 r.pixels[((int)X+(int)Y*r.width)]= 0xffffff;
					 r.depthMap[X+Y*r.width]=(int) (depth+alias);
					 }
			 	}
		 	}
		}
	}

	public void moveForward(int i) {
		walking=(Math.cos(game.time/24.0)+1)*((speed+species.projectile.addedSpeed)*(5/8.00));
		move(direction+Math.PI,(Math.cos(game.time/24.0)+1)*((speed+species.projectile.addedSpeed)*(5/8.00)));
		if(walkingOld>0&&Math.sin(game.time/24.0)<0)
			SoundHandler.play(SoundHandler.STOMP,-11);
		walkingOld=Math.sin(game.time/24.0);
	}
	
	public void grunt(){
		SoundHandler.play(SoundHandler.EX1);
	}

class WarAxe extends SwingProjectile{
	public WarAxe(){
	super();
	reloadTime=190;
	bulletMax=0;
	endTime=35;
	size=8;
	damage=7.5;
	meele=true;
	speed=16;
	name="war axe";
	addedSpeed=-0.4;
	range=Math.PI*7/8.0;
	}
	
	public WarAxe(double x, double y, double direction, Player enemy, Player p,
			int size, double d, long endTime, double damage, double range,
			ArrayList<Projectile> brothers) {
		super(x,y,direction,enemy,p,size,d,endTime,damage,range,brothers);
	}

	public void newProjectile(Player p,boolean[] gene){
		game=p.getGame();
		ArrayList<Projectile> brothers=new ArrayList<Projectile>();
		brothers.add(game.alterProjectiles(new WarAxe(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size,endTime,damage,range,brothers),1));
		brothers.add(game.alterProjectiles(new WarAxe(p.x,p.y,p.direction,game.getEnemy(p),p,size+4,speed+size*2,endTime,damage,range,brothers),1));
		brothers.add(game.alterProjectiles(new WarAxe(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size*3,endTime,damage,range,brothers),1));
	}
	public void tick(){
		updatePosition();
		
		double swingFrac=1.0/3.0;
		if(game.time-startTime<this.endTime*swingFrac&&game.time-startTime+1>this.endTime*swingFrac)
			SoundHandler.play(SoundHandler.SWING,-10);
			
		if(Math.sqrt(Math.pow(x-game.player1.x, 2)+Math.pow(y-game.player1.y, 2))<=target.size+size&&damage!=0){
			game.player1.damage(damage*owner.power,this);
			owner.fitness+=(damage*owner.power*1.000)/game.player1.maxHealth*45;
			for(int i=0;i<brothers.size();i++){
				Projectile proj=brothers.get(i);
				if(proj!=null)
				proj.damage=0;
			}
			game.player1.move(dir+Math.PI, damage*2);
		}
		if(Math.sqrt(Math.pow(x-game.player2.x, 2)+Math.pow(y-game.player2.y, 2))<=game.player2.size+size&&damage!=0){
			game.player2.damage(damage*owner.power,this);
			//owner.fitness+=(damage*owner.power*1.000)/target.maxHealth*45;
			for(int i=0;i<brothers.size();i++){
				Projectile proj=brothers.get(i);
				if(proj!=null)
				proj.damage=0;
			}
			game.player2.move(dir+Math.PI, damage*2);
		}
		if(startTime+endTime==game.time&&endTime!=0)game.alterProjectiles(this,0);
	}
	
	public void draw(Render2D r){
		if(brothers.get(0).equals(this)){
			Projectile middle=brothers.get((brothers.size()-1)/2);
			Projectile end=brothers.get(brothers.size()-1);
			int newEndY=Render2D.visualY(end.y),newMiddleY=Render2D.visualY(middle.y),newY=Render2D.visualY(y);
			double[] Xs=new double[9];
			double[] Ys=new double[9];
			
			Xs[0]=middle.x+Math.cos(calcDirection-Math.PI/2.0)*middle.size;
			Ys[0]=newMiddleY+Math.sin(calcDirection-Math.PI/2.0)*middle.size*Render.hDisplacement;
			Xs[1]=middle.x+Math.cos(calcDirection-Math.PI*1/4.0)*middle.size;
			Ys[1]=newMiddleY+Math.sin(calcDirection-Math.PI*1/4.0)*middle.size*Render.hDisplacement;
			Xs[2]=end.x+Math.cos(calcDirection)*end.size;
			Ys[2]=newEndY+Math.sin(calcDirection)*end.size*Render.hDisplacement;
			Xs[3]=middle.x+Math.cos(calcDirection)*size;
			Ys[3]=newMiddleY+Math.sin(calcDirection)*size*Render.hDisplacement;
			Xs[4]=middle.x+Math.cos(calcDirection+Math.PI*3/8.0)*middle.size;
			Ys[4]=newMiddleY+Math.sin(calcDirection+Math.PI*3/8.0)*middle.size*Render.hDisplacement;
			Xs[5]=middle.x+Math.cos(calcDirection+Math.PI*5/8.0)*middle.size;
			Ys[5]=newMiddleY+Math.sin(calcDirection+Math.PI*5/8.0)*middle.size*Render.hDisplacement;
			Xs[6]=middle.x+Math.cos(calcDirection+Math.PI)*size;
			Ys[6]=newMiddleY+Math.sin(calcDirection+Math.PI)*size*Render.hDisplacement;
			Xs[7]=x+Math.cos(calcDirection+Math.PI)*size;
			Ys[7]=newY+Math.sin(calcDirection+Math.PI)*size*Render.hDisplacement;
			Xs[8]=middle.x+Math.cos(calcDirection-Math.PI*3/4.0)*middle.size;
			Ys[8]=newMiddleY+Math.sin(calcDirection-Math.PI*3/4.0)*middle.size*Render.hDisplacement;
			
			for(int n=0;n<Ys.length;n++){
				Ys[n]-=owner.size*Execusioner.height*5/6.0;
			}
			Path2D poly=new Path2D.Double();
			poly.moveTo(Xs[0], Ys[0]);
			for(int i=1;i<Xs.length;i++)
				poly.lineTo(Xs[i], Ys[i]);
			
			Xs=new double[4];
			Ys=new double[4];
			Xs[0]=middle.x+Math.cos(calcDirection)*size;
			Ys[0]=newMiddleY+Math.sin(calcDirection)*size*Render.hDisplacement;
			Xs[1]=Xs[0]+Math.cos(calcDirection+Math.PI)*middle.speed;
			Ys[1]=Ys[0]+Math.sin(calcDirection+Math.PI)*middle.speed*Render.hDisplacement;
			Xs[2]=Xs[1]+Math.cos(calcDirection+Math.PI/2.0)*3;
			Ys[2]=Ys[1]+Math.sin(calcDirection+Math.PI/2.0)*3*Render.hDisplacement;
			Xs[3]=Xs[0]+Math.cos(calcDirection+Math.PI/2.0)*3;
			Ys[3]=Ys[0]+Math.sin(calcDirection+Math.PI/2.0)*3*Render.hDisplacement;
			for(int n=0;n<Ys.length;n++){
				Ys[n]-=owner.size*Execusioner.height*5/6.0;
			}
			Path2D handle=new Path2D.Double();
			handle.moveTo(Xs[0], Ys[0]);
			for(int i=1;i<Xs.length;i++)
				handle.lineTo(Xs[i], Ys[i]);
			
			Rectangle rec=poly.getBounds().union(handle.getBounds());
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
						if(handle.contains(x, y)&&r.depthMap[x+y*r.width]<depth){
							r.depthMap[x+y*r.width]=depth;
							r.pixels[x+y*r.width]=0x964B00;
						}
						else if(handle.intersects(x-oThickness/3.00, y-oThickness/3.00,oThickness,oThickness)&&r.depthMap[x+y*r.width]<(int)depth){
							r.depthMap[x+y*r.width]=depth;
							r.pixels[x+y*r.width]=0x1;
						}
					}
				}
			}
		}
	}
}
}
class ExecutorSpecies extends Species{

	public ExecutorSpecies() {
		super(0x1c1c1c);
		generateArmor("Textures/execusionerArmor.png");
	}
	
	public void tick(Game game,Player user){
		int sitNum=1;
		if(user.canSee(game.player1)){
			sitNum++;
		}
		else if(user.canSee(game.player2)){
			sitNum++;
		}
		loop:for(int i=0;i<game.projectiles.size();i++){
			Projectile proj=game.projectiles.get(i);
			if(proj!=null)
			if(user.canSee(proj)&&!proj.owner.equals(user)){
				sitNum+=2;
				break loop;
			}
		}
		user.execute(sitNum, game.player1);
	}
}