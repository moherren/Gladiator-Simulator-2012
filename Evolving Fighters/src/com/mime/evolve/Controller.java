package com.mime.evolve;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.GlyphVector;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.mime.evolve.Game;
import com.mime.evolve.Generator;
import com.mime.evolve.Tournament;
import com.mime.evolve.graphics.Render;
import com.mime.evolve.graphics.Render2D;
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
	public Render2D vsArt;
	
	public Player[] tick(boolean[] key){
		if(handle.usersEmpty()){
			handle.takeUsers();
			gen=1;
		}
		else if(Competetors==null){
			Generator gen=new Generator(16,intensity);
			ArrayList<Player> players=gen.getPlayers();
			Competetors=new Player[players.size()];
			allCompetetors=new ArrayList<Player>();	
			for(int i=0;i<players.size();i++){
				Competetors[i]=players.get(i);
				allCompetetors.add(new Player(11,0,players.get(i).species,players.get(i).DNA,this));
			}
			this.gen=2;
			handle.takeAllBets(Competetors);
		}
		else if(tourny==null){
			try {
				vsArt=generateVs();
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
	public static Render2D generateVs() throws FontFormatException, IOException{
		Render2D vsArt;
		GlyphVector gv=Render.getGlyohVector("vs", Font.createFont(Font.TRUETYPE_FONT, new File("Constantine.ttf")).deriveFont(0,25));
		int width=(int) (gv.getVisualBounds().getWidth())+20;
		int height=(int) (gv.getVisualBounds().getHeight())+50;
		vsArt=new Render2D(width,height);
		Arrays.fill(vsArt.pixels,0);
		vsArt.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("Constantine.ttf")).deriveFont(0,25));
		vsArt.drawOutlinedWordArt("vs",10,(int) (gv.getVisualBounds().getHeight()+25), 4);
		return vsArt;
	}
}