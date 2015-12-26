package com.mime.evolve;

import com.mime.evolve.input.Player;
import com.mime.evolve.level.Level;
import com.mime.evolve.projectiles.Projectile;
import com.mime.evolve.species.Species;

public class ContinousGame extends Game{
	public ContinousGame(){
		species=new Species[]{newSpecies(0xff0000),newSpecies(0x00ff00),newSpecies(0x0000ff),newSpecies(0x00ffff)};
		player1=new Player(12,Math.PI,species[0],this);
		player2=new Player(12,0,species[1],this);
		level=new Level(1200,800);
		for(int i=0;i<360;i++){
			double a=i/180.0000*Math.PI;
			double x = 600 + 360 * Math.cos(a);
			double y = 400 + 360 * Math.sin(a);
			/*if(rand.nextInt(3)==0)
			level.addWall(x, y, 10, 10);*/
			level.addWall((int)x, (int)y, 5, 5);
		}
	}
	public void endGame(){
		oldTime=time;
		
		if(player1.health<0){
			player1.fitness/=2;
			player2.fitness+=(int) player2.health*45/player2.maxHealth;
		}
		else if(player2.health<0){
			player2.fitness/=2;
			player1.fitness+=(int) player1.health*45/player1.maxHealth;
		}
		else{
		player1.fitness/=2;
		player2.fitness/=2;
		}
		
		species1.addToGenePool(player1);
		species2.addToGenePool(player2);
		battleNumber++;
		
		if(battleNumber==16){
			battleNumber=1;
			gen++;
			for(Species s:species){
				s.transeferGenePool();
			}
		}
		species2=species[(battleNumber/2)/4];
		species1=species[(battleNumber/2)%4];
		newGame();
	}
	protected void newGame() {
		
		dBetweenPlayers=10000;
		
		for(Projectile p:projectiles){
			destroyProjectile(p);
		}
		if(species1.oldGenePool.isEmpty()){
			player1=new Player(11,Math.PI,species1,this);
		}
		else{
			if(battleNumber-8>0){
				player1=new Player(11,Math.PI,species1,mutate(species1.elite.DNA),this);
			}
			else
				player1=new Player(11,Math.PI,species1,breed(species1),this);
		}
		if(species2.oldGenePool.isEmpty()){
			player2=new Player(11,Math.PI,species2,this);
		}
		else{
				player2=new Player(11,Math.PI,species2,breed(species2),this);
		}
	}
	public Player[] tick(boolean key[]){
		time++;
		
		//boolean left=key[KeyEvent.VK_A];
		//boolean right=key[KeyEvent.VK_D];
		//boolean up=key[KeyEvent.VK_W];
		//boolean down=key[KeyEvent.VK_S];
		//boolean shoot=key[KeyEvent.VK_TAB];
		
			player1.tick(this);
			player2.tick(this);
		
			double dBP=Math.sqrt(Math.pow(player1.x-player2.x, 2)+Math.pow(player1.y-player2.y, 2));
			if(dBP<dBetweenPlayers&&(player1.species.projectile.meele||player2.species.projectile.meele)){
				dBetweenPlayers=dBP;
				resetCountdown();
			}
			
			if(Math.sqrt(Math.pow(player1.y-player2.y,2)+Math.pow(player1.x-player2.x, 2))<=player1.size+player2.size){
				double dir=Math.atan2(player1.y-player2.y, player1.x-player2.x);
				player1.move(dir+Math.PI, 0.05*player2.size);
				player2.move(dir, 0.05*player1.size);
			}
		
			for(Projectile p:projectiles){
				p.tick();
			}
			for(Projectile p:destroiedProjectiles){
				projectiles.remove(p);
			}
			destroiedProjectiles.clear();
		
			if(time-oldTime>=1000) endGame();
			return null;
	}
}