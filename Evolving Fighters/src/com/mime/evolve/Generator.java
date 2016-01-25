package com.mime.evolve;

import java.util.ArrayList;

import com.mime.evolve.input.Player;

public class Generator{
	
	public class GenerateThread implements Runnable{
		int id,num;
		
		public GenerateThread(int id){
			this.num=id;
			addLoad(0);
		}
		
		public void run() {
			while(playersGen+2<=numOfPlayers){
			Game g=new Game();
			Player[] genedPlayers=null;
			double loadAmount=0;
			double change=0.125;
			id=playersGen/2;
				playersGen+=2;
				do{
					genedPlayers=g.tick(Display.display.input.key);
					if(g.getLoad()-loadAmount>change){
						addLoad(change);
						loadAmount+=change;
					}
				
					if(genedPlayers[0]!=null)
						System.out.print(id+", ");
				}while(genedPlayers[0]==null);
			addPlayer(genedPlayers,num);	
			}					
		}
	}
	private int numOfPlayers;
	private ArrayList<Player> players;
	private int numThreads;
	private int playersGen;
	Thread[] threads;
	GenerateThread[] generators;
	public boolean display=true;
	
	private double loadAmount;
	
	public Generator(int numPlayers,int numThreads,boolean display){
		numOfPlayers=numPlayers;
		players=new ArrayList<Player>();
		this.numThreads=numThreads;
		loadAmount=0;
		playersGen=0;
		this.display=display;
		try {
			generatePlayers();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void addLoad(double add) {
		loadAmount+=add*2/numOfPlayers;
		if(display)
		try{
		Display.display.lRender(Display.display.game,loadAmount);
		}
		catch(Exception e){
			
		}
	}
		
	public synchronized void addPlayer(Player[] p,int id){
		for(Player P:p)
		if(players.size()<numOfPlayers)
			players.add(P);
	}
	
	private void generatePlayers() throws InterruptedException{
			threads=new Thread[numThreads];
			generators=new GenerateThread[numThreads];
			for(int i=0;i<numThreads&&players.size()+i*2<numOfPlayers;i++){
				generators[i]=new GenerateThread(i);
				threads[i]=new Thread(generators[i]);
				threads[i].start();
			}
			for(int i=0;i<numThreads;i++){
				if(threads[i]!=null){
					threads[i].join();
				}
			}
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
}
