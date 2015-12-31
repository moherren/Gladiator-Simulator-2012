package com.mime.evolve.species;

import com.mime.evolve.Game;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;
import com.mime.evolve.projectiles.WepShortSword;

public class WeaponReaderSpecies extends Species{

	public WeaponReaderSpecies(int color) {
		super(color);
		descriptor="intelligent";
	}
	public WeaponReaderSpecies(int i, Projectile proj, String string) {
		super(i,proj,string);
		descriptor="intelligent";
	}
	public void tick(Game game,Player user){
		Player enemy=game.getEnemy(user);
		int sitNum=1;
		if(Player.rangeOfDirection(user.x, enemy.x, user.y, enemy.y, user.direction, user.broadCast,enemy.size)){
			sitNum++;
		}
		loop:for(int i=0;i<game.projectiles.size();i++){
			Projectile proj=game.projectiles.get(i);
			if(Player.rangeOfDirection(user.x, proj.x, user.y, proj.y, user.direction, user.broadCast,proj.size)&&proj.target.equals(this)){
				sitNum+=2;
				break loop;
			}
		}
		if(!enemy.species.projectile.meele){
			sitNum+=4;
			
			if(enemy.bullets<enemy.species.projectile.bulletMax/3)
				sitNum+=4;
			else if(enemy.bullets<enemy.species.projectile.bulletMax*2/3)
				sitNum+=8;
			else if(enemy.bullets==0)
				sitNum+=12;
		}	
		
		user.execute(sitNum, enemy);
	}
}
