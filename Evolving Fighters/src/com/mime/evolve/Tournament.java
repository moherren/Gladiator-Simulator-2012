package com.mime.evolve;

import com.mime.evolve.input.Execusioner;
import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;
import com.mime.evolve.sound.SoundHandler;

public class Tournament extends Game {
	Player[] competetors;
	Player[] newCompetetors = new Player[999];
	boolean started=false;
	private int redos=1;
	public static int displayStage=0;
	long nextStage=0;
	/** 1. Display fighter names with "vs." displayed between them
	 *  2. Move fighter names to their respective positions on the screen and fade into the arena
	 *  3. Display the word Fight!
	 *  4. The fight occurs
	 *  5. Death scene/display the words KO
	 *  6. Distribute money*/
	public final static long[] stageTimes=new long[]{
			200, 100,200,upperTimeLength*2,350,300,500
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
			boolean moneyWon=false;
			
			if(displayStage==6){
				
				if (player1.health <= 0) {
					if(Display.display.game.handle.giveWinnings(player2,player1))
						moneyWon=true;
				} else {
					if(Display.display.game.handle.giveWinnings(player1,player2))
						moneyWon=true;
				}

				}
			
			
			if(displayStage==stageTimes.length||(!moneyWon&&displayStage==6)){	
				if (battleNumber >= newCompetetors.length) {
					System.out.println(competetors.length);
					battleNumber = 0;
					if(newCompetetors.length!=1){
						competetors = newCompetetors;
						newCompetetors = new Player[competetors.length / 2];
						newGame();
						Display.display.game.handle.takeAllBets(competetors);
					} 	
					else{
						competetors = newCompetetors;
					}
				}
			
				newGame();
			}
			else{
				nextStage+=stageTimes[displayStage];
				}
			if(displayStage==2)
				SoundHandler.play(SoundHandler.ROUND_ONE);
			
			if(displayStage==5&&endGame=="KO")
				SoundHandler.play(SoundHandler.KO);
			else if(displayStage==5)
				SoundHandler.play(SoundHandler.EXECUTION);
			
			
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
			
			if(execusioner!=null){
				execusioner.tick(this);
				
				if (Math.sqrt(Math.pow(player1.y - execusioner.y, 2)
						+ Math.pow(player1.x - execusioner.x, 2)) <= player1.size
						+ execusioner.size) {
					double dir = Math.atan2(player1.y - execusioner.y, player1.x
							- execusioner.x);
					player1.move(dir + Math.PI, 0.05 * execusioner.size);
					execusioner.move(dir, 0.05 * player1.size);
				}
				if (Math.sqrt(Math.pow(execusioner.y - player2.y, 2)
						+ Math.pow(execusioner.x - player2.x, 2)) <= execusioner.size
						+ player2.size) {
					double dir = Math.atan2(execusioner.y - player2.y, execusioner.x
							- player2.x);
					execusioner.move(dir + Math.PI, 0.05 * player2.size);
					player2.move(dir, 0.05 * execusioner.size);
				}
			}
			
			for (Projectile p : destroiedProjectiles) {
				projectiles.remove(p);
			}
			
			destroiedProjectiles.clear();
			for(int i=0;i<projectiles.size();i++){
				Projectile proj=projectiles.get(i);
				if(proj!=null)
					proj.tick();
			}
			
			

			if ((time - oldTime >= 1000&&redos<2&&(!species1.projectile.meele||!species2.projectile.meele))
					||(time - oldTime >= 2000&&redos<2)) {
				oldTime = time;
				redos++;
				newGame();
			}
			else if (time - oldTime == 1000&&redos==2&&execusioner==null){
				System.out.println("Time up!");
				execusioner=new Execusioner(Player.stringToDna("0010010100010100100101000101001001010001010010010100010100100101000101001001010001010010010100010100100101000101000000000000")
						,this);
			}
		}
		else
			resetCountdown();
		return null;
	}

	public void endGame() {
		if (player1.health <= 0) {
			newCompetetors[battleNumber] = player2;
			} else {
			newCompetetors[battleNumber] = player1;
			}
		System.out.println("Winner " + newCompetetors[battleNumber].toString());
		
		oldTime = time;
		redos=1;
		battleNumber++;
		displayStage=4;
		nextStage=time+stageTimes[4];
	}

	protected void newGame() {
		for (Projectile p : destroiedProjectiles) {
			projectiles.remove(p);
		}
		
		destroiedProjectiles.clear();
		
		if(player1==null)
			displayStage=0;
		else if(player1.health<=0||player2.health<=0){
			displayStage=0;
		}
		else {
			displayStage=2;
			SoundHandler.play(SoundHandler.ROUND_TWO);
		}
		nextStage=(int) (time+stageTimes[displayStage]);
		
		dBetweenPlayers=10000;
		
		for (Projectile p : projectiles) {
			alterProjectiles(p,0);
		}
		
		execusioner=null;
		
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
	public int getRedos(){
		return redos;
	}
}