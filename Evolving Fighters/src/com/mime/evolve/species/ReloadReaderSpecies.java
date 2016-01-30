package com.mime.evolve.species;

import com.mime.evolve.Game;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;

public class ReloadReaderSpecies extends Species{

	public ReloadReaderSpecies(int color) {
		super(color);
		descriptor="greedy";
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
		if(game.time-user.lastShot>=user.species.projectile.reloadTime){
			sitNum+=4;
		}
		if(game.time-game.getEnemy(user).lastShot>=user.species.projectile.reloadTime){
			sitNum+=8;
		}
		
		user.execute(sitNum, enemy);
		if(user.direction<0)user.direction+=(Math.PI*2);
		sitNum--;
		sitNum/=Player.situations/4;
		 
	}
}
