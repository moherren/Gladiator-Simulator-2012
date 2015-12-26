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
		size=4;
		damage=1.75;
		meele=true;
		speed=0;
		name="sword";
		addedSpeed=-0.15;
	}
	public void newProjectile(Player p,boolean[] gene){
		game=p.getGame();
		ArrayList<Projectile> brothers=new ArrayList<Projectile>();
		brothers.add(game.createProjectile(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size,endTime,damage,range,brothers)));
		brothers.add(game.createProjectile(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size*3,endTime,damage,range,brothers)));
		brothers.add(game.createProjectile(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size+2,speed+size*5,endTime,damage,range,brothers)));
	}
}
