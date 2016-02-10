package com.mime.evolve.input;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Random;

import com.mime.evolve.Display;
import com.mime.evolve.Game;
import com.mime.evolve.Tournament;
import com.mime.evolve.graphics.Drawable;
import com.mime.evolve.graphics.Render;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.projectiles.Projectile;
import com.mime.evolve.sound.SoundHandler;
import com.mime.evolve.species.Species;

public class Player implements Drawable{
	public static int reactions=7,situations=16,traits=12;
	public static String dnaToString(boolean[] dna){
		String code="";
		for(boolean b:dna){
			if(b)
				code=code+"1";
			else
				code=code+"0";
		}
		return code;
	}
	public static boolean[] generateSequences(int length){
		boolean[] bool=new boolean[length];
		Random rand=Game.rand;
		for(int i=0;i<length;i++){
			Random newRand=new Random(rand.nextLong());
			bool[i]=newRand.nextBoolean();
		}
		return bool;
	}
	public static boolean preciseRangeOfDirection(double x1, double x2, double y1, double y2, double direction, double range){
		double angle2=Math.atan2(y2-y1, x2-x1);
		x2=x1+Math.cos(direction);
		y2=y1+Math.sin(direction);
		double angle1=Math.atan2(y2-y1, x2-x1);
		return (angle2-angle1>=-range&&angle2-angle1<=range);
	}
	public static boolean rangeOfDirection(double x1, double x2, double y1, double y2, double direction, double range,double targetSize){
		double angle2=Math.atan2(y2-y1, x2-x1);
		double range2=Math.atan2(targetSize, Point.distance(x1, y1, x2, y2));
		x2=x1+Math.cos(direction);
		y2=y1+Math.sin(direction);
		double angle1=Math.atan2(y2-y1, x2-x1);
		return (angle2-angle1>-range-range2&&angle2-angle1<range+range2);
	}
	public static boolean[] stringToDna(String code){
		boolean[] dna=new boolean[code.length()];
		for(int i=0;i<code.length();i++){
			if(code.charAt(i)=='1')
				dna[i]=true;
			else
				dna[i]=false;
		}
		return dna;
	}
	public double x=0,y=0,direction=0,broadCast=Math.PI/8,maxHealth=6,health=maxHealth,speed=0.2,power=0.6;
	public double maxCast=Math.PI/8,minCast=Double.MIN_VALUE,castRate=0.0250;
	public boolean right=true;
	public int size=11,fitness=0,bullets;
	public long lastShot=0;
	public long deathTime=0;
	
	public Species species;
	
	protected double walking=0;
	public boolean[] DNA=new boolean[reactions*situations+traits];
	Game game;
	Random rand=Game.rand;
	public Player(int size, double angle,Species species, boolean[] DNA,Game game){
		this.size=size;
		x=rand.nextInt(360-size);
		y=rand.nextInt((int) Math.sqrt(Math.pow(360-size, 2)-Math.pow(x,2)));
		if(rand.nextInt()%2==0){
			x*=-1;
		}
		if(rand.nextInt()%2==0){
			y*=-1;	
		}
		x+=600;
		y+=400;
		direction=angle;
		direction+=rand.nextDouble()+rand.nextInt();
		direction=Math.atan2(400-y,600-x);
		this.DNA=DNA;
		this.species=species;
		bullets=species.projectile.bulletMax;
		this.game=game;
		readTraits();
	}
	public Player(int size, double angle,Species species,Game game){
		this.size=size;
		
		Random rand=Game.rand;
		x=rand.nextInt(360-size);
		y=rand.nextInt((int) Math.sqrt(Math.pow(360-size, 2)-Math.pow(x,2)));
		if(rand.nextInt()%2==0){
			x*=-1;
		}
		if(rand.nextInt()%2==0){
			y*=-1;	
		}
		x+=600;
		y+=400;
		direction=angle;
		direction+=rand.nextDouble()+rand.nextInt();
		direction=Math.atan2( 400-y,600-x);
		DNA=generateSequences(situations*reactions+traits);
		this.species=species;
		bullets=species.projectile.bulletMax;
		this.game=game;
		readTraits();
	}
	public Player(int x,int y, double angle,Species species, boolean[] DNA,Game game){
		this.x=x;
		this.y=y;
		direction=angle;
		direction=Math.atan2(400-y,600-x);
		this.DNA=DNA;
		this.species=species;
		bullets=species.projectile.bulletMax;
		this.game=game;
		readTraits();
	}
	public Player(int x,int y, double angle,Species species,Game game){
		this.x=x;
		this.y=y;
		direction=angle;
		direction=Math.atan2(400-y,600-x);
		DNA=generateSequences(situations*reactions+traits);
		this.species=species;
		bullets=species.projectile.bulletMax;
		this.game=game;
		readTraits();
	}
	/**
	 * widenCsat() used to decrease the size of the player object's vision
	 */
	public void closeCast(){
		broadCast-=castRate;
		if(broadCast<minCast)
			broadCast=minCast;
	}
	public void damage(double d){
		if(game.getEnemy(this).health>0&&health>0){
			if(d==0)
				return;
			if((int)(health/4)!=(int)((health-d)/4))
				grunt();
			health-=d;
			game.resetCountdown();
			if(health<=0){
				game.endGame();
				if(deathTime==0)
					deathTime=game.getTime();
			}
		}
	}
	@Override
	public void draw(Render2D r){
		if(deathTime==0)
			drawAlive(r);
		else
			drawDead(r);
	}
	
	public void drawAlive(Render2D r) {
		
		int newY=Render2D.visualY(y);
		double walking=Math.abs(Math.sin((game.time%(Math.PI*64*Math.pow(speed, -1)))/32.000))*3.0000;
		 if(this.walking==0)
			 walking=0;
		 Rectangle rec=new Rectangle();
		 rec.setBounds((int) x-size,(int)(newY-size*1.5-walking),(int)size*2,(int)(size*1.5));
		 int depth=(int) Render2D.visualY(y);
		 double[] eyeDir=new double[]{direction+Math.PI*0.125,direction-Math.PI*0.125};
		 int headX=(int) rec.getCenterX(),headY=rec.y;
		 
		 
		 if(Math.sin(eyeDir[0])>0){
			 double x=headX+Math.cos(eyeDir[0])*size;
			 double y=headY+Math.sin(eyeDir[0])*size*Render.hDisplacement;
			 for(int X=(int)x-1;X<(int)x+2;X++){
				 for(int Y=(int)y-1;Y<(int)y+2;Y++){
					 if(r.depthMap[X+Y*r.width]<depth){
					 r.pixels[((int)X+(int)Y*r.width)]= 1;
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
					 if(r.depthMap[X+Y*r.width]<depth){
						 r.pixels[((int)X+(int)Y*r.width)]= 1;
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
					r.pixels[x+y*r.width]= species.skin;
					r.depthMap[x+y*r.width]=(int) depth;
				 }
			 }
		 }
		 
		 Render2D armor=species.getArmor();
		 for(int X=(int) 0;X<size*2;X++){
			 int x=rec.x+X;
			 for(int Y= 0;Y<size*1.5;Y++){
				 int y=Y+(int)(Math.sin(Math.acos((X-size)/(1.00*size)))*Render2D.hDisplacement*size)+rec.y;
				 int aX= (int) ((((X/(size*4.000))+((direction-Math.PI/2)%(Math.PI*2)/(Math.PI*2))))*armor.width%armor.width);
				 int aY= (int) ((Y/(size*1.5))*armor.height);
				 if(aX<0)
					 aX+=armor.width;
				 int color=armor.pixels[aX+aY*armor.width];
				 if(X==0||X==size*2-1||Y==(int)(size*1.5)-1)
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
					r.pixels[x+y*r.width]= species.skin;
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
						 r.pixels[((int)X+(int)Y*r.width)]= 1;
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
						 r.pixels[((int)X+(int)Y*r.width)]= 1;
						 r.depthMap[X+Y*r.width]=(int) (depth+alias);
						 }
				 }
			 }
		}
	}
	
	public void execute(int i,Player enemy) {
		if(deathTime==0){
		boolean[] gene=Arrays.copyOfRange(DNA, (i-1)*Player.reactions, i*Player.reactions);
		walking=0;
		boolean turning=false,walking=false;
		if(gene[0]&&!gene[1]){
			moveForward(1);
			walking=true;
		}
		if(!gene[0]&&gene[1]){
			moveBackward();
			walking=true;
		}
		
		if(gene[2]&&!gene[3]){
			turnLeft();
			turning=true;
		}
		else if(!gene[2]&&gene[3]){
			turnRight();
			turning=true;
		}
		
		if(gene[4])shoot(enemy,gene);
		
		if(gene[5]&&!gene[6])widenCast();
		else if(gene[6]&&!gene[5])closeCast();
		//else if(gene[5]&&gene[6])broadCast=maxCast;
		}
	}
	public Game getGame() {
		return game;
	}
	
	public void lowerFitness(int i){
		fitness-=i;
		if(fitness<0)
			fitness=0;
	}
	public void move(double direction,double distance){
		if(Math.sqrt(Math.pow(x-distance*Math.cos(direction)-600, 2)+Math.pow(y-400, 2))<=360-size)
			x=x-distance*Math.cos(direction);
		if(Math.sqrt(Math.pow(x-600, 2)+Math.pow(y-400-distance*Math.sin(direction), 2))<=360-size)
			y=y-distance*Math.sin(direction);
	}
	public void moveBackward() {
		walking-=((speed+species.projectile.addedSpeed)*(5/8.00));
		move(direction,((speed+species.projectile.addedSpeed)*(5/8.00)));
	}
	public void moveForward(int i) {
		walking+=((speed+species.projectile.addedSpeed)*(5/8.00));
		move(direction+Math.PI,((speed+species.projectile.addedSpeed)*(5/8.00)));
	}
	public void readTraits() {
		double healthBoost=2,speedBoost=0.2,powerBoost=0.1;
		for(int x=0;x<traits;x++){
			if(DNA[reactions*situations+x]){
				switch(x/4){
					case 0: speed+=speedBoost;
					break;
					case 1: maxHealth+=healthBoost;
					size++;
					break;
					case 2: power+=powerBoost;
					break;
				}
			}
			else{
				switch(x/4){
				case 0: 
					if(x%2==0){
						maxHealth+=healthBoost;
						size++;
					}
					else
						power+=powerBoost;
				break;
				case 1:
					if(x%2==0)
						power+=powerBoost;
					else
						speed+=speedBoost;
				break;
				case 2: 
					if(x%2==0)
						speed+=speedBoost;
					else{
						maxHealth+=healthBoost;
						size++;
					}
				break;
				}
			}
			health=maxHealth;
			
		}
	}
	
	public void shoot(Player enemy,boolean gene[]) {
		if((bullets>0||species.projectile.bulletMax==0)&&game.time-lastShot>=species.projectile.reloadTime){
		bullets--;
		lastShot=game.time;
		species.projectile.newProjectile(this,gene);
		}
	}
	public void tick(Game game){
		species.tick(game, this);
	}
	public String toString(){
		return ("name:"+species.name+", fitness-"+species.elite.fitness+", descriptor-"+species.descriptor+", weapon-"+species.projectile.name+", dna:"+dnaToString(DNA));
	}
	/**
	 * turnLeft() used to turn the player object's direction counter-clockwise
	 */
	public void turnLeft() {
		direction+=Math.PI*0.02*(speed+species.projectile.addedSpeed);
	}

	/**
	 * turnRight() used to turn the player object's direction clockwise
	 */
	public void turnRight() {
		direction+=-Math.PI*0.02*(speed+(species.projectile.addedSpeed));
	}

	/**
	 * widenCsat() used to increase the size of the player object's vision
	 */
	public void widenCast(){
		broadCast+=castRate;
		if(broadCast>maxCast)
			broadCast=maxCast;
	}
	
	public void grunt(){
		if(game instanceof Tournament)
		switch(Display.display.game.rand.nextInt(2)){
			case 0:
				SoundHandler.play(SoundHandler.GRUNT_ONE);
				break;
			case 1:
				SoundHandler.play(SoundHandler.GRUNT_TWO);
				break;
		}
	}
	
	public boolean canSee(double x,double y,double size){
		return Player.rangeOfDirection(this.x, x, this.y, y,direction, broadCast,size);
	}
	
	public boolean canSee(Player p){
		if(p==null)
			return false;
		return Player.rangeOfDirection(this.x, p.x, this.y, p.y,direction, broadCast,p.size);
	}
	
	public boolean canSee(Projectile p){
		if(p==null)
			return false;
		return Player.rangeOfDirection(this.x, p.x, this.y, p.y,direction, broadCast,p.size);
	}
}