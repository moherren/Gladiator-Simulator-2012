package com.mime.evolve.species;

import com.mime.evolve.Game;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;

public class ReverseVisionSpecies extends Species{

	public ReverseVisionSpecies(int color) {
		super(color);
		descriptor="suspicious";
	}
	
	public ReverseVisionSpecies(int color,Projectile proj,String name) {
		super(color,proj,name);
		descriptor="suspicious";
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
		if(Player.rangeOfDirection(user.x, enemy.x, user.y, enemy.y, user.direction+Math.PI, user.broadCast,enemy.size)){
			sitNum+=4;
		}
		loop:for(Projectile proj:game.projectiles){
			if(Player.rangeOfDirection(user.x, proj.x, user.y, proj.y, user.direction+Math.PI, user.broadCast,proj.size)&&proj.target.equals(this)){
				sitNum+=8;
				break loop;
			}
		}
		
		user.execute(sitNum, enemy);
		if(user.direction<0)user.direction+=(Math.PI*2);
	}
}
