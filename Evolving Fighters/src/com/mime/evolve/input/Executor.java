package com.mime.evolve.input;

import java.util.ArrayList;

import com.mime.evolve.Game;
import com.mime.evolve.species.Species;
import com.mime.evolve.projectiles.Projectile;
import com.mime.evolve.projectiles.SwingProjectile;

public class Executor extends Player{
	boolean inRing=false;
	public Executor(boolean[] DNA, Game game) {
		super(600, 20, -Math.PI/2.000, new ExecutorSpecies(), DNA, game);
		species.projectile=new WarAxe();
		size=16;
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
	addedSpeed=-0.6;
	range=Math.PI;
	}
	
	public void newProjectile(Player p,boolean[] gene){
		game=p.getGame();
		ArrayList<Projectile> brothers=new ArrayList<Projectile>();
		brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size,endTime,damage,range,brothers),1));
		brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size+2,speed+size*2,endTime,damage,range,brothers),1));
		brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size*3,endTime,damage,range,brothers),1));
	}
}
class ExecutorSpecies extends Species{

	public ExecutorSpecies() {
		super(1);
	}
	
	public void tick(Game game,Player user){
		int sitNum=1;
		if(user.canSee(game.player1)||user.canSee(game.player2)){
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