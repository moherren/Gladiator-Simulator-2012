package com.mime.evolve;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import com.mime.evolve.Game;
import com.mime.evolve.Generator;
import com.mime.evolve.Tournament;
import com.mime.evolve.input.GambleHandler;
import com.mime.evolve.input.Player;

public class Controller extends Game implements ActionListener{
	
	int gen=0;
	Player[] Competetors;
	ArrayList<Player> allCompetetors;
	GambleHandler handle=new GambleHandler(this);
	Tournament tourny;
	int loadCount=0;
	String ankur="trash";
	public static int intensity=2;
	
	public Player[] tick(boolean[] key){
		if(handle.usersEmpty()){
			handle.takeUsers();
			gen=1;
		}
		else if(Competetors==null){
			Generator gen=new Generator(16,intensity);
			ArrayList<Player> players=gen.getPlayers();
			Competetors=new Player[players.size()];
			for(int i=0;i<players.size();i++)
				Competetors[i]=players.get(i);
			allCompetetors=(ArrayList<Player>) players.clone();	
			this.gen=2;
			handle.takeAllBets(Competetors);
		}
		else if(tourny==null){
			tourny=new Tournament(Competetors);
			player1=tourny.player1;
			player2=tourny.player2;
			species1=tourny.species1;
			species2=tourny.species2;
			projectiles=tourny.projectiles;
		}
		else{
			player1=tourny.player1;
			player2=tourny.player2;
			species1=tourny.species1;
			species2=tourny.species2;
			Player[] result=tourny.tick(key);
			if(result!=null)
				if(result.length==1)
				return tourny.tick(key);
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}
	public int getDisplayStage(){
		return tourny.getDisplayStage();
	}
	public int getDisplayTime(){
		return tourny.getDisplayTime();
	}
}