package com.mime.evolve.species;

import com.mime.evolve.Game;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;

public class ConstantVisionSpecies extends Species{

	public ConstantVisionSpecies(int color) {
		super(color);
		descriptor="diligent";
	}
	public void tick(Game game,Player user){
		Player enemy=game.getEnemy(user);
		boolean viewProj=false;
		int sitNum=1;
		if(Player.rangeOfDirection(user.x, enemy.x, user.y, enemy.y, user.direction, user.broadCast,enemy.size)){
			sitNum++;
		}
		else if(Player.rangeOfDirection(user.x, enemy.x, user.y, enemy.y, user.direction, user.maxCast,enemy.size)){
			sitNum+=4;
		}
		loop:for(int i=0;i<game.projectiles.size();i++){
			Projectile proj=game.projectiles.get(i);
			if(Player.rangeOfDirection(user.x, proj.x, user.y, proj.y, user.direction, user.broadCast,proj.size)&&proj.target.equals(this)){
				sitNum+=2;
				viewProj=true;
				break loop;
			}
		}
		
		if(viewProj)
			loop:for(int i=0;i<game.projectiles.size();i++){
				Projectile proj=game.projectiles.get(i);
			if(Player.rangeOfDirection(user.x, proj.x, user.y, proj.y, user.direction, user.maxCast,proj.size)&&proj.target.equals(enemy)){
				sitNum+=8;
				break loop;
			}
		}
		
		user.execute(sitNum, enemy);
		if(user.direction<0)user.direction+=(Math.PI*2);
		 
	}
}
