package com.mime.evolve.species;

import com.mime.evolve.Game;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;

public class RandomSpecies extends Species{

	public RandomSpecies(int color) {
		super(color);
		descriptor="erratic";
	}
	public void tick(Game game,Player user){
		int r=(int)((1-Math.sqrt(1-Game.rand.nextDouble()))*4);
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
		if(r%2==1){
			sitNum+=4;
		}	
		if(r>1){
			sitNum+=8;
		}
		
		user.execute(sitNum, enemy);
		if(user.direction<0)user.direction+=(Math.PI*2);
		sitNum--;
		sitNum/=Player.situations/4;
		 
	}
}
