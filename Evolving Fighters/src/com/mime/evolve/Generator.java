package com.mime.evolve;

import java.util.ArrayList;

import com.mime.evolve.input.Player;

public class Generator{
	
	public class GenerateThread implements Runnable{
		int id;
		
		public GenerateThread(int id){
			this.id=id;
		}
		
		public void run() {
			Game g=new Game();
			Player[] genedPlayers=null;
			boolean loaded=false;
			double loadAmount=0;
			do{
				genedPlayers=g.tick(Display.display.input.key);
				if(loadAmount!=g.getLoad()){
					addLoad(g.getLoad()-loadAmount);
					loadAmount=g.getLoad();
				}
				
				if(genedPlayers[0]!=null)
					System.out.println(id+" yay!");
			}
			while(genedPlayers[0]==null);
			for(Player p:genedPlayers){
				addPlayer(p);
			}
		}

		
	}
	private int numOfPlayers;
	private ArrayList<Player> players;
	private int numThreads;
	
	private double loadAmount;
	
	public Generator(int numPlayers,int numThreads){
		numOfPlayers=numPlayers;
		players=new ArrayList<Player>();
		this.numThreads=numThreads;
		loadAmount=0;
		try {
			generatePlayers();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void addLoad(double add) {
		loadAmount+=add*2/numOfPlayers;
		Display.display.lRender(Display.display.game,loadAmount/1.25);
	}
		
	public synchronized void addPlayer(Player p){
		if(players.size()<numOfPlayers)
			players.add(p);
	}
	
	private void generatePlayers() throws InterruptedException{
		while(players.size()<numOfPlayers){
			Thread[] threads=new Thread[numThreads];
			for(int i=0;i<numThreads&&players.size()+i*2<numOfPlayers;i++){
				threads[i]=new Thread(new GenerateThread(i+players.size()));
				threads[i].start();
			}
			for(int i=0;i<numThreads;i++){
				if(threads[i]!=null){
					threads[i].join();
				}
			}
		}
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
}
