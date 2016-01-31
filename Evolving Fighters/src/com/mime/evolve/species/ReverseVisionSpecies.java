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
		if(Player.rangeOfDirection(user.x, enemy.x, user.y, enemy.y, user.direction+Math.PI, user.broadCast,enemy.size)){
			sitNum+=4;
		}
		loop:for(int i=0;i<game.projectiles.size();i++){
			Projectile proj=game.projectiles.get(i);
			if(proj!=null)
			if(Player.rangeOfDirection(user.x, proj.x, user.y, proj.y, user.direction+Math.PI, user.broadCast,proj.size)&&!proj.owner.equals(user)){
				sitNum+=8;
				break loop;
			}
		}
		
		user.execute(sitNum, enemy);
		if(user.direction<0)user.direction+=(Math.PI*2);
	}
}
