package com.mime.evolve.input;

import java.util.ArrayList;

import com.mime.evolve.Game;
import com.mime.evolve.species.Species;
import com.mime.evolve.projectiles.Projectile;
import com.mime.evolve.projectiles.SwingProjectile;

public class Execusioner extends Player{
	boolean inRing=false;
	public Execusioner(boolean[] DNA, Game game) {
		super(600, 20, -Math.PI/2.000, new ExecutorSpecies(), DNA, game);
		species.projectile=new WarAxe();
		size=19;
		power=1;
		speed=1;
		maxHealth=20;
		health=maxHealth;
	}
	
	public void tick(Game game){
		if(y<100)
			moveForward(1);
		else{
			species.tick(game, this);
			inRing=true;
		}
	}
	public void move(double direction,double distance){
		if(Math.sqrt(Math.pow(x-distance*Math.cos(direction)-600, 2)+Math.pow(y-400, 2))<=360-size||!inRing)
			x=x-distance*Math.cos(direction);
		if(Math.sqrt(Math.pow(x-600, 2)+Math.pow(y-400-distance*Math.sin(direction), 2))<=360-size||!inRing)
			y=y-distance*Math.sin(direction);
	}
}
class WarAxe extends SwingProjectile{
	public WarAxe(){
	super();
	reloadTime=190;
	bulletMax=0;
	endTime=35;
	size=4;
	damage=7.5;
	meele=true;
	speed=6;
	name="war axe";
	addedSpeed=-0.4;
	range=Math.PI;
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
		brothers.add(game.alterProjectiles(new WarAxe(p.x,p.y,p.direction,game.getEnemy(p),p,size+2,speed+size*2,endTime,damage,range,brothers),1));
		brothers.add(game.alterProjectiles(new WarAxe(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size*3,endTime,damage,range,brothers),1));
	}
	public void tick(){
		updatePosition();
		if(Math.sqrt(Math.pow(x-game.player1.x, 2)+Math.pow(y-game.player1.y, 2))<=target.size+size&&damage!=0){
			game.player1.damage(damage*owner.power);
			owner.fitness+=(damage*owner.power*1.000)/game.player1.maxHealth*45;
			for(int i=0;i<brothers.size();i++){
				Projectile proj=brothers.get(i);
				if(proj!=null)
				proj.damage=0;
			}
			game.player1.move(dir+Math.PI, damage*2);
		}
		if(Math.sqrt(Math.pow(x-game.player2.x, 2)+Math.pow(y-game.player2.y, 2))<=game.player2.size+size&&damage!=0){
			game.player2.damage(damage*owner.power);
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