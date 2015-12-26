package com.mime.evolve;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.mime.evolve.graphics.Render;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.input.Player;
import com.mime.evolve.input.User;

class Confetti{
	double x,y;
	int color,startTime;
	double angle,torque,vx;
	Victory vic;
	public Confetti(double x,double y,Victory vic){
		this.x=x;
		this.y=y;
		this.color=randomColor();
		angle=Math.random()*2*Math.PI;
		torque=Math.random()*2-1;
		vx=Math.random()*6-3;
		this.vic=vic;
		startTime=vic.time;
	}
	
	public void draw(Render r){
		int time=vic.time-startTime;
		double y=time*0.1+this.y;
		double x=time*vx/0.1+this.x;
		double angle=this.angle+time*torque;
		double minX= (x-Math.sin(angle)*5),maxX= (Math.sin(angle)*5+x),slopeX=(maxX-minX)/10.00;
		double minY= (y-Math.cos(angle)*3),maxY= (Math.cos(angle)*3+y),slopeY=(maxY-minY)/6.00;
		for(double x1= minX;Math.abs(maxX-x1)>0.01;x1+=slopeX){
			for(double y1=minY;Math.abs(maxY-y1)>0.1;y1+=slopeY){
				if(x1>=0&&x1<1200&&y1>=0&&y1<800)
				r.pixels[(int)(x1)+(int)(y1)*r.width]=color;
			}
		}
		if(y>800||x<0||x>=1200){
			angle=Math.random()*2*Math.PI;
			torque=Math.random()*1.5-0.75;
			vx=Math.random()*4-2;
			startTime=vic.time;
			y=-5;
			x=Math.random()*1200;
			color=randomColor();
		}
	}
	public int randomColor(){
		int c=0;
		switch((int)(Math.random()*7)){
		case 0:
			c=0xff0000;
			break;
		case 1:
			c=0x00ff00;
			break;
		case 2:
			c=0x0000ff;
			break;
		case 4:
			c=0xffff00;
			break;
		case 5:
			c=0xff7f00;
			break;
		default:
			c=0x7F00FF;
			break;
		}
		return c;
	}
}

class GamblerName{
	String name;
	int position,money;
	Render2D r;
	int x,y;
	Victory vic;
	public GamblerName(String name,int position,int money,int y,Victory vic){
		this.name=Victory.position(position)+" "+name+" - "+money;
		this.position=position;
		this.money=money;
		this.vic=vic;
		
		r=new Render2D(1200,200);
		Arrays.fill(r.pixels, -1);
		FontRenderContext frc=new FontRenderContext(null,true,true);
		r.setFont(r.getFont().deriveFont(Font.BOLD, 45));
		GlyphVector gv=r.getFont().createGlyphVector(frc,this.name);
		int length=(int) (gv.getVisualBounds().getWidth()+10);
		this.x=600-length/2;
		this.y=y;
		if(position<4)
			r.drawWordArt(this.name, x, 100, position);
		else
			r.drawWordArt(this.name, x, 100,4);
	}
	
	public void draw(Render r){
		if(vic.time>vic.phase3+(vic.gamblers.size()-position)*500)
			r.draw(this.r, 0, y);
	}
}

class PlayerCreditName{
	double x,y;
	Player player;
	Victory vic;
	final float movingDistance=300*18;
	Render2D r=new Render2D(650,50);
	int length=1200,fontSize=26;
	public PlayerCreditName(double x,double y,Player player,Victory vic){
		this.x=x;
		this.y=y;
		this.player=player;
		this.vic=vic;
		player.x=100;
		player.y=100;
		player.direction=Math.PI/2;
		FontRenderContext frc=new FontRenderContext(null,true,true);
		
		while(length>600){
			fontSize--;
			r.setFont(r.getFont().deriveFont(0, fontSize));
			GlyphVector gv=r.getFont().createGlyphVector(frc, player.species.name);
			length=(int) (gv.getVisualBounds().getWidth());
		}
		r.drawString(player.species.name,(int)(x-length),fontSize,0xffffff);
	}
	
	public void checkCloseness(Render2D r){
		int y=(int) (this.y+movingDistance*vic.getTime()/vic.phase1);
		if(y<550&&y>350){
			Arrays.fill(r.depthMap, 0);
			Arrays.fill(r.pixels, 0);
			r.Player(player);
			if(y<400){
				r.shade(((400-y)/50.000), 0);
			}
			else if(y>500)
				r.shade(1-(550-y)/50.000, 0);
		}
			
	}
	
	public void draw(Render r){
		int y=(int) (this.y+movingDistance*vic.getTime()/vic.phase1);
		int x=(int) this.x;
		r.draw(this.r, 0, y);
	}
}

public class Victory {
	static Render2D winnerName=new Render2D(1200,200);
	public static String position(int pos){
		String string=""+pos;
		switch(pos%10){
		case 1:
			string=string+"st";
			break;
		case 2:
			string=string+"nd";
			break;
		case 3:
			string=string+"rd";
			break;
		default:
			string=string+"th";
			break;
		}
		return string;
	}
	ArrayList<Player> losers=new ArrayList<Player>();
	ArrayList<PlayerCreditName> names=new ArrayList<PlayerCreditName>();
	ArrayList<GamblerName> gamblers=new ArrayList<GamblerName>();
	ArrayList<Confetti> confetti=new ArrayList<Confetti>();
	Render2D deathWindow=new Render2D(200,200);
	Player winner;
	int time=0,startTime;
	final float phase1=45000,phase2=phase1+10000,phase3=phase2+500;
	
	int addedTime=0;

	public Victory(ArrayList<Player> people,ArrayList<User> users,Player winner){
		losers=people;
		losers.remove(winner);
		this.winner=winner;
		double y=-150;
		for(Player p:losers){
			if(!p.species.name.equals(winner.species.name)){
				names.add(new PlayerCreditName(600,y,p,this));
				y-=300;
			}
		}
		
		for(int i=0;i<users.size();i++){
			gamblers.add(new GamblerName(users.get(i).name,i+1,users.get(i).money,i*50+25,this));
		}
		
		Random random=new Random(0);
		for(int i=0;i<300;i++){
			confetti.add(new Confetti(random.nextInt(1200),-random.nextInt(400),this));
		}
		winnerName.setFont(winnerName.font.deriveFont(0,30));
		FontRenderContext frc=new FontRenderContext(null,true,true);
		winnerName.setFont(winnerName.getFont().deriveFont(0, 30));
		GlyphVector gv=winnerName.getFont().createGlyphVector(frc, winner.species.name);
		int length=(int) (gv.getVisualBounds().getWidth());
		winnerName.drawWordArt(winner.species.name, 600-length/2, 0, 4);
		
		startTime=(int) System.currentTimeMillis();
		//Game.player1=winner;
	}
	
	public float getTime() {
		return time;
	}
	
	public void render(Render2D r){
		if(time<phase1){
		Arrays.fill(r.pixels,1);
		r.draw(deathWindow, 600, 200);
		for(PlayerCreditName name:names)
			name.draw(r);
		}
		else {
			Arrays.fill(r.depthMap, 0);
			r.Arena();
			r.Player(winner);
			for(Confetti c:confetti){
				c.draw(r);
			}
			if(time>=phase2){
				r.draw(winnerName, 0, 350);
			}
			r.shade(Math.max(1-(time-phase1)/500,0), 1);
			
			if(time>=phase3){
				r.convertToGray(Math.min((time-phase2)/500.000,1));
				r.shade(Math.min((time-phase2)/1000.000,0.5), 1);
				for(GamblerName gn:gamblers)
					gn.draw(r);
			}
		}
	}
	
	public void step(boolean[] key){
		if(key[KeyEvent.VK_SPACE]){
			addedTime+=6;
		}
		time=(int) (System.currentTimeMillis()-startTime)+addedTime;
		if(time<phase1){
			for(PlayerCreditName n:names)
				n.checkCloseness(deathWindow);
		}
		else {
			winner.x=Display.WIDTH/2;
			winner.y=Math.max(Math.min(-750+(time-phase1)/8,Display.HEIGHT/2),winner.size*1.5);
			winner.direction=Math.PI/2;
		}
	}
}