package com.mime.evolve.species;

import java.awt.Point;
import com.mime.evolve.Game;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;

public class BulletProximitySpecies extends Species{

	public BulletProximitySpecies(int color) {
		super(color);
		descriptor="talented";
	}
	public void tick(Game game,Player user){
		Player enemy=game.getEnemy(user);
		boolean viewProject=false;
		double dis=720;
		int sitNum=1;
		if(user.canSee(enemy)||user.canSee(game.execusioner)){
			sitNum++;
		}
		for(int i=0;i<game.projectiles.size();i++){
			Projectile proj=game.projectiles.get(i);
			if(proj!=null)
			if(!proj.owner.equals(this)){
				if(user.canSee(proj)){
					viewProject=true;
				}
				double DIS=Point.distance(user.x, user.y, proj.x, proj.y);
				if(DIS<dis){
					dis=DIS;
				}
			}
		}
		if(viewProject)sitNum+=2;
		
		if(dis<144)sitNum+=0;
		else if(dis<288)sitNum+=4;
		else if(dis<432)sitNum+=8;
		else if(dis<576)sitNum+=12;
		user.execute(sitNum, enemy);
		if(user.direction<0)user.direction+=(Math.PI*2);
		 
	}
}
