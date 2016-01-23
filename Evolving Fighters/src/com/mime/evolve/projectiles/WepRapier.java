package com.mime.evolve.projectiles;

import java.util.ArrayList;

import com.mime.evolve.Game;
import com.mime.evolve.input.Player;

public class WepRapier extends SwingProjectile{
	public WepRapier(){
		super();
		reloadTime=35;
		bulletMax=0;
		endTime=15;
		size=3;
		damage=0.6;
		meele=true;
		speed=1;
		name="rapier";
	}
	public void newProjectile(Player p,boolean gene[]){
		game=p.getGame();
		ArrayList<Projectile> brothers=new ArrayList<Projectile>();
		double armLength=4;
		if(gene[2]||gene[3]){
			brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+armLength,endTime,damage,range,brothers),1));
			brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size*2+armLength,endTime,damage,range,brothers),1));
			brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size*4+armLength,endTime,damage,range,brothers),1));
			brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed+size*6+armLength,endTime,damage,range,brothers),1));
			brothers.add(game.alterProjectiles(new SwingProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size/2,speed+size*8+armLength,endTime,damage,range,brothers),1));
		}
		else{
			brothers.add(game.alterProjectiles(new StabProjectile(p.x,p.y,p.direction,game.getEnemy(p),p,size,speed/10.000,endTime,damage,brothers),1));
			brothers.add(game.alterProjectiles(new StabProjectile(p.x+size*2,p.y,p.direction,game.getEnemy(p),p,size,speed/10.000,endTime,damage,brothers),1));
			brothers.add(game.alterProjectiles(new StabProjectile(p.x+size*4,p.y,p.direction,game.getEnemy(p),p,size,speed/10.000,endTime,damage,brothers),1));
			brothers.add(game.alterProjectiles(new StabProjectile(p.x+size*6,p.y,p.direction,game.getEnemy(p),p,size,speed/10.000,endTime,damage,brothers),1));
			brothers.add(game.alterProjectiles(new StabProjectile(p.x+size*8,p.y,p.direction,game.getEnemy(p),p,size/2,speed/10.000,endTime,damage,brothers),1));
		}
	}
}
