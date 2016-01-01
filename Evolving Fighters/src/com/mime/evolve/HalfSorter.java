package com.mime.evolve;

import com.mime.evolve.input.Player;
import com.mime.evolve.projectiles.Projectile;

public class HalfSorter extends Game {
	Player[] competetors;
	Player[] newCompetetors = new Player[999];
	int oLength;
	private int redos=0;

	public HalfSorter(Player[] p) {
		super(true);
		competetors = p;
		oLength=competetors.length;
		newCompetetors = new Player[p.length / 2];
		battleNumber = 0;
		newGame();
	}

	public void endGame() {
		if (competetors.length != oLength/2){
		oldTime = time;

		player1.fitness=player1.species.maxFit;
		player2.fitness=player2.species.maxFit;
		
		if (player1.health <= 0) {
			newCompetetors[battleNumber] = player2;
			System.out.println("Loser " + player1);
		} else {
			newCompetetors[battleNumber] = player1;
			System.out.println("Loser " + player2);
		}
		System.out.println("Winner " + newCompetetors[battleNumber].toString());
		
			battleNumber++;

		if (battleNumber >= newCompetetors.length) {
			System.out.println(competetors.length);
			battleNumber = 0;
			if(competetors.length!=1){
				competetors = newCompetetors;
				newCompetetors = new Player[competetors.length / 2];
			}
		}
		redos=1;
		newGame();
		}
	}

	protected void newGame() {
		
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

	public Player[] tick(boolean key[]) {
		time++;

		// boolean left=key[KeyEvent.VK_A];
		// boolean right=key[KeyEvent.VK_D];
		// boolean up=key[KeyEvent.VK_W];
		// boolean down=key[KeyEvent.VK_S];
		// boolean shoot=key[KeyEvent.VK_TAB];
		if(true){
			boolean change=true;
			while(change){
				change=false;
				for(int i=0;i<competetors.length-1;i++){
					if(competetors[i].fitness<competetors[i+1].fitness){
						Player holder=competetors[i];
						competetors[i]=competetors[i+1];
						competetors[i+1]=holder;
						change=true;
					}
				}
					
			}
			return new Player[]{
					competetors[0],competetors[1]
			};
		}
		else if (competetors.length == oLength/2) {
			for(Player p:competetors)
				p.fitness=p.species.maxFit;
			return  competetors;
		} else {
			player1.tick(this);
			player2.tick(this);

			double dBP=Math.sqrt(Math.pow(player1.x-player2.x, 2)+Math.pow(player1.y-player2.y, 2));
			if(dBP<dBetweenPlayers&&(player1.species.projectile.meele||player2.species.projectile.meele)){
				dBetweenPlayers=dBP;
				resetCountdown();
			}
			
			if (Math.sqrt(Math.pow(player1.y - player2.y, 2)
					+ Math.pow(player1.x - player2.x, 2)) <= player1.size
					+ player2.size) {
				double dir = Math.atan2(player1.y - player2.y, player1.x
						- player2.x);
				player1.move(dir + Math.PI, 0.05 * player2.size);
				player2.move(dir, 0.05 * player1.size);
			}

			for (Projectile p : projectiles) {
				p.tick();
			}
			for (Projectile p : destroiedProjectiles) {
				projectiles.remove(p);
			}
			destroiedProjectiles.clear();

			if ((time - oldTime >= 1000&&redos<3&&(!species1.projectile.meele||!species2.projectile.meele))
					||(time - oldTime >= 2000&&redos<3)) {
				oldTime = time;
				redos++;
				newGame();
			}
			else if (time - oldTime >= 1000&&redos==3){
				player1.damage(9999);
			}
			return new Player[]{null,null};
		}
	}
}
