package com.mime.evolve;

import java.util.ArrayList;

import com.mime.evolve.input.Player;

public class Generator{
	
	public class GenerateThread implements Runnable{
		int id;
		
		public GenerateThread(int id){
			this.id=id;
			addLoad(0);
		}
		
		public void run() {
			Game g=new Game();
			Player[] genedPlayers=null;
			boolean loaded=false;
			double loadAmount=0;
			double change=0.125;
			do{
				genedPlayers=g.tick(Display.display.input.key);
				if(g.getLoad()-loadAmount>change){
					addLoad(change);
					loadAmount+=change;
					
				}
				
				if(genedPlayers[0]!=null)
					System.out.print("#");
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
		try{
		Display.display.lRender(Display.display.game,loadAmount);
		}
		catch(Exception e){
			
		}
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
