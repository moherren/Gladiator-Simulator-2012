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
		if(Player.rangeOfDirection(user.x, enemy.x, user.y, enemy.y, user.direction, user.broadCast,enemy.size)){
			sitNum++;
		}
		loop:for(Projectile proj:game.projectiles){
			if(Player.rangeOfDirection(user.x, proj.x, user.y, proj.y, user.direction, user.broadCast,proj.size)&&proj.target.equals(this)){
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
