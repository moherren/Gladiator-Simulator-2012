package com.mime.evolve.species;

import com.mime.evolve.Game;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;

public class VisionSizeSpecies extends Species{

	public VisionSizeSpecies(int color) {
		super(color);
		descriptor="humble";
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
			if(user.canSee(proj)&&proj.target.equals(user)){
				sitNum+=2;
				break loop;
			}
		}
		
		if(user.broadCast<user.minCast)sitNum+=4;
		else if(user.broadCast+user.maxCast/4.000*3<user.maxCast)sitNum+=8;
		else if(user.broadCast+user.maxCast/2.000<user.maxCast)sitNum+=12;
		else if(user.broadCast+user.maxCast/4.000<user.maxCast)sitNum+=16;
		
		user.execute(sitNum, enemy);
		if(user.direction<0)user.direction+=(Math.PI*2);
		 
	}
}
