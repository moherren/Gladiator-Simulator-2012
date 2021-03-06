package com.mime.evolve.species;

import com.mime.evolve.Game;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;

public class EnemyVisionSpecies extends Species{

	public EnemyVisionSpecies(int color) {
		super(color);
		descriptor="meek";
	}
	public void tick(Game game,Player user){
		Player enemy=game.getEnemy(user);
		int sitNum=1;
		if(user.canSee(enemy)||user.canSee(game.execusioner)){
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
		if(Player.rangeOfDirection(enemy.x, user.x, enemy.y, user.y, enemy.direction, enemy.maxCast,user.size)){
			sitNum+=4;
		}
		if(Player.rangeOfDirection(enemy.x, user.x, enemy.y, user.y, user.direction, enemy.maxCast,user.size)){
			sitNum+=8;
		}
		
		user.execute(sitNum, enemy);
		if(user.direction<0)user.direction+=(Math.PI*2);
		 
	}
}
