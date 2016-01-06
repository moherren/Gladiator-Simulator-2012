package com.mime.evolve;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.GlyphVector;
import java.io.File;
import java.io.IOException;

import com.mime.evolve.graphics.Render;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;
import com.mime.evolve.species.Species;

public class Tournament extends Game {
	static Player[] competetors;
	static Player[] newCompetetors = new Player[999];
	boolean started=false;
	private int redos=0;
	int displayStage=0;
	long nextStage=0;
	/** 1. Display fighter names with "vs." displayed between them
	 *  2. Move fighter names to their respective positions on the screen and fade into the arena
	 *  3. Display the word Fight!
	 *  4. The fight occurs
	 *  5. Death scene/display the words KO
	 *  6. Distribute money*/
	public final static long[] stageTimes=new long[]{
			200, 100,50,upperTimeLength,150,300
	};
	
	public Tournament(Player[] p) {
		super(true);		
		competetors = p;
		newCompetetors = new Player[p.length / 2];
		battleNumber = 0;
		newGame();
	}

	public Player[] tick(boolean key[]) {
			
		time++;
		
		// boolean left=key[KeyEvent.VK_A];
		// boolean right=key[KeyEvent.VK_D];
		// boolean up=key[KeyEvent.VK_W];
		// boolean down=key[KeyEvent.VK_S];
		// boolean shoot=key[KeyEvent.VK_TAB];
		if(time>=nextStage&&displayStage!=3){
			System.out.println(displayStage+" stage is done");
				displayStage++;
			if(displayStage==stageTimes.length){
				newGame();
			}
			else{
				
				nextStage+=stageTimes[displayStage];	
			}
		}

		if (competetors.length == 1) {
			System.out.println("Fight done");
			return  competetors;
		} else if(displayStage==3||displayStage==4){
			player1.tick(this);
			player2.tick(this);

			double dBP=Math.sqrt(Math.pow(player1.x-player2.x, 2)+Math.pow(player1.y-player2.y, 2));
			if(dBP<dBetweenPlayers&&(player1.species.projectile.meele||player2.species.projectile.meele)){
				dBetweenPlayers=dBP;
				resetCountdown(500);
			}
			
			if (Math.sqrt(Math.pow(player1.y - player2.y, 2)
					+ Math.pow(player1.x - player2.x, 2)) <= player1.size
					+ player2.size) {
				double dir = Math.atan2(player1.y - player2.y, player1.x
						- player2.x);
				player1.move(dir + Math.PI, 0.05 * player2.size);
				player2.move(dir, 0.05 * player1.size);
			}
			
			for (Projectile p : destroiedProjectiles) {
				projectiles.remove(p);
			}
			
			destroiedProjectiles.clear();
			for (Projectile p : projectiles) {
				p.tick();
			}
			
			

			if ((time - oldTime >= 1000&&redos<2&&(!species1.projectile.meele||!species2.projectile.meele))
					||(time - oldTime >= 2000&&redos<2)) {
				oldTime = time;
				redos++;
				newGame();
			}
			else if (time - oldTime >= 1000&&redos==2){
				System.out.println("Time up!");
				if(player2.fitness>player1.fitness)
					player1.damage(9999);
				else
					player2.damage(9999);
			}
		}
		else
			resetCountdown();
		return null;
	}

	public void endGame() {
		oldTime = time;
		redos=1;

		if (player1.health <= 0) {
			newCompetetors[battleNumber] = player2;
			Display.display.game.handle.giveWinnings(player2,player1);
		} else {
			newCompetetors[battleNumber] = player1;
			Display.display.game.handle.giveWinnings(player1,player2);
		}
		System.out.println("Winner " + newCompetetors[battleNumber].toString());
			battleNumber++;

		if (battleNumber >= newCompetetors.length) {
			System.out.println(competetors.length);
			battleNumber = 0;
			if(newCompetetors.length!=1){
				competetors = newCompetetors;
				newCompetetors = new Player[competetors.length / 2];
				newGame();
				Display.display.game.handle.takeAllBets(competetors);
				return;
			} 
			else{
				competetors = newCompetetors;
				return;
			}
		}
		displayStage=4;
		nextStage=time+stageTimes[4];
	}

	protected void newGame() {
		if(player1==null)
			displayStage=0;
		else if(player1.health<=0||player2.health<=0){
			displayStage=0;
		}
		else 
			displayStage=2;
		nextStage=(int) (time+stageTimes[displayStage]);
		
		dBetweenPlayers=10000;
		
		for (Projectile p : projectiles) {
			destroyProjectile(p);
		}
		if (competetors.length > 1){
		species1 = competetors[battleNumber * 2].species;
		player1 = new Player(11, Math.PI, species1,
				competetors[battleNumber * 2].DNA, this);
		species2 = competetors[battleNumber * 2 + 1].species;
		player2 = new Player(11, Math.PI, species2,
				competetors[battleNumber * 2 + 1].DNA, this);
		}
	}
	public int getDisplayStage(){
		return displayStage;
	}
	public int getDisplayTime(){
		int g=0;
		for(int i=0;i<displayStage;i++)
			g+=stageTimes[i];
		g+=nextStage-time;
		return g;
	}
}