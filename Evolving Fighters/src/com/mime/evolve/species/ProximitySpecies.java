package com.mime.evolve.species;

import java.awt.Point;

import com.mime.evolve.Game;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;

public class ProximitySpecies extends Species{

	public ProximitySpecies(int color) {
		super(color);
		descriptor="on edge";
	}
	public ProximitySpecies(int color,Projectile proj,String name) {
		super(color,proj,name);
		descriptor="on edge";
	}
	public void tick(Game game,Player user){
		Player enemy=game.getEnemy(user);
		int sitNum=1;
		if(user.canSee(enemy)||user.canSee(game.executor)){
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
		double dis=Point.distance(user.x, user.y, enemy.x, enemy.y);
		if(dis%360>=180){
			sitNum+=4;
		}	
		if(dis>=360){
			sitNum+=8;
		}
		
		user.execute(sitNum, enemy);
		if(user.direction<0)user.direction+=(Math.PI*2);
		 
	}
}
