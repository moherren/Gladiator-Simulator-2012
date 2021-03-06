package com.mime.evolve.species;

import com.mime.evolve.Game;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;

public class DualSightedSpecies extends Species{

	public DualSightedSpecies(int color) {
		super(color);
		descriptor="superior";
	}
	public void tick(Game game,Player user){
		Player enemy=game.getEnemy(user);
		int sitNum=1;
		if(Player.rangeOfDirection(user.x, enemy.x, user.y, enemy.y, user.direction-user.broadCast/2, user.broadCast/2,enemy.size)){
			sitNum++;
		}
		else if(game.execusioner!=null)
		if(Player.rangeOfDirection(user.x, game.execusioner.x, user.y, game.execusioner.y, user.direction-user.broadCast/2, user.broadCast/2,game.execusioner.size)){
			sitNum++;
		}
		loop:for(int i=0;i<game.projectiles.size();i++){
			Projectile proj=game.projectiles.get(i);
			if(proj!=null)
			if(Player.rangeOfDirection(user.x, proj.x, user.y, proj.y, user.direction-user.broadCast/2, user.broadCast/2,proj.size)&&!proj.owner.equals(enemy)){
				sitNum+=2;
				break loop;
			}
		}
		if(Player.rangeOfDirection(user.x, enemy.x, user.y, enemy.y, user.direction+user.broadCast/2, user.broadCast/2,enemy.size)){
			sitNum+=4;
		}
		else if(game.execusioner!=null)
			if(Player.rangeOfDirection(user.x, enemy.x, user.y, enemy.y, user.direction+user.broadCast/2, user.broadCast/2,enemy.size)){
				sitNum+=4;
			}
		loop:for(int i=0;i<game.projectiles.size();i++){
			Projectile proj=game.projectiles.get(i);
			if(proj!=null)
			if(Player.rangeOfDirection(user.x, proj.x, user.y, proj.y, user.direction+user.broadCast/2, user.broadCast/2,proj.size)&&!proj.owner.equals(user)){
				sitNum+=8;
				break loop;
			}
		}
		
		user.execute(sitNum, enemy);
		if(user.direction<0)user.direction+=(Math.PI*2);
		 
	}
}
