package com.mime.evolve.projectiles;

import java.util.ArrayList;

import com.mime.evolve.Game;
import com.mime.evolve.input.Player;

public class WepShortSword extends SwingProjectile{
	public WepShortSword(){
		super();
		reloadTime=70;
		bulletMax=0;
		endTime=35;
		size=5;
		damage=1.95;
		meele=true;
		speed=0;
		name="sword";
		addedSpeed=0.2;
	}
	public void newProjectile(Player p,boolean[] gene){
		game=p.getGame();
		ArrayList<Projectile> brothers=new ArrayList<Projectile>();
		brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size,endTime,damage,range,brothers),1));
		brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size*3,endTime,damage,range,brothers),1));
		brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size+2,speed+size*5,endTime,damage,range,brothers),1));
	}
}
