package com.mime.evolve.species;


import com.mime.evolve.Game;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;

public class TimeOrientedSpecies extends Species{

	public TimeOrientedSpecies(int color) {
		super(color);
		descriptor="timely";
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
		if(game.time%2==1){
			sitNum+=4;
		}	
		if(game.time%4>1){
			sitNum+=8;
		}
		
		user.execute(sitNum, enemy);
		if(user.direction<0)user.direction+=(Math.PI*2);
		sitNum--;
		sitNum/=Player.situations/4;
	}
}
