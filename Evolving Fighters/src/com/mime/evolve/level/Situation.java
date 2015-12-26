package com.mime.evolve.level;

import java.util.Random;

import com.mime.evolve.Game;
import com.mime.evolve.input.Player;
import com.mime.evolve.species.Species;

public class Situation {
	int x1,x2,y1,y2;
	Species species1,species2;
	double angle1,angle2;
	boolean[] dna1,dna2;
	Random rand=Game.rand;
	
	public Situation(){
		x1=rand.nextInt(360);
		y1=rand.nextInt((int) Math.sqrt(Math.pow(360, 2)-Math.pow(x1,2)));
		if(rand.nextInt()%2==0){
			x1*=-1;
		}
		if(rand.nextInt()%2==0){
			y1*=-1;	
		}
		x1+=600;
		y1+=400;
		
		x2=rand.nextInt(360);
		y2=rand.nextInt((int) Math.sqrt(Math.pow(360, 2)-Math.pow(x2,2)));
		if(rand.nextInt()%2==0){
			x2*=-1;
		}
		if(rand.nextInt()%2==0){
			y2*=-1;	
		}
		x2+=600;
		y2+=400;
		
		angle1=rand.nextDouble()*Math.PI*2;
		angle2=rand.nextDouble()*Math.PI*2;
	}
	
	public void applySituation(Game game){
		game.species1=species1;
		game.species2=species2;
		game.player1=new Player(x1,y1,angle1,species1,dna1,game);
		game.player2=new Player(x2,y2,angle2,species2,dna2,game);
	}
	
	public Species getSpecies(boolean first) {
		if(first)
			return species1;
		else
			return species2;
	}
	
	public void setDNA(boolean[] dna,boolean first){
		if(first)
			dna1=dna;
		else
			dna2=dna;
	}
	
	public void setPlayerLocation(int x,int y,boolean first){
		if(first){
			x1=x;
			y1=y;
		}
		else{
			x2=x;
			y2=y;
		}
	}

	public void setSpecies(Species species,boolean first){
		if(first)
			species1=species;
		else
			species2=species;
	}
	
}
