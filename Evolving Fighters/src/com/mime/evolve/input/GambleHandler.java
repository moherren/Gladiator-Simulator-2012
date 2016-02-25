package com.mime.evolve.input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import com.mime.evolve.Display;
import com.mime.evolve.sound.SoundHandler;

public class GambleHandler {
	private ArrayList<User> gamblers=new ArrayList<User>();
	private int currentGambler=0;
	Player[] players;
	
	boolean requestBets=false;
	ActionListener al;
	
	public GambleHandler(ActionListener al){
		this.al=al;
	}
	/*
	 * addUser is used to add a gambler to the pool of gamblers
	 */
	public void addUser(User user){
		gamblers.add(user);
	}
	/*
	 * finishedMakingUsers is used to accept the newly inputed gamblers
	 */
	public void finishMakingUsers(User[] users) {
		for(User u:users){
			addUser(u);
		}
		currentGambler=gamblers.size();
		if(requestBets){
			takeAllBets(players);
		}
	}
	/*
	 * finishedPlacingBets is used to tell the handler that a specific player has finished taking bets
	 */
	public void finishPlacingBet(User user){
		user.cp=new CoinPile(user.money,0,0);
		if(gamblers.size()>++currentGambler){
			takeBet(gamblers.get(currentGambler));
			
		}
		else{
			al.actionPerformed(new ActionEvent(this,0,"bets done"));
			//SoundHandler.playMusic(SoundHandler.SONG_ONE, 4);
			Display.returnCanvas();
		}
	}
	public ArrayList<User> getGamblers() {
		return gamblers;
	}
	public ArrayList<User> getOrderedList(){
		ArrayList<User> copy=(ArrayList<User>) gamblers.clone();
		while(!sortList(copy));
		return copy;
	}
	public void giveWinnings(Player winner,Player loser) {
		for(User u:gamblers){
			u.winMoney(u.getBet(winner)*2,winner);
			u.removeBet(loser);
		}
	}
	/*
	 * PlayerFinished is used to distribute winnings to those who bet on a specific player
	 */
	public void playerFinished(Player p,int round){
		
	}
	public boolean sortList(ArrayList<User> list){
		if(list.size()==1)
			return true;
		boolean inOrder=true;
		for(int i=0;i<list.size()-1;i++){
			if(list.get(i).moneyWon<list.get(i+1).moneyWon){
				inOrder=false;
				User u=list.get(i+1);
				list.remove(i+1);
				list.add(i,u);
			}
		}
		return inOrder;
	}
	/*
	 * takeAllBets is used to request for each gambler to input their bets
	 * The first argument is the player array used to choose to bet from
	 */
	public void takeAllBets(Player[] players){
		this.players=players;
		if(gamblers.size()!=0){
			currentGambler=0;
			takeBet(gamblers.get(currentGambler));
			currentGambler++;
			requestBets=false;
		}
		else
			requestBets=true;
	}
	/*
	 * takeBet is used to request a specific gambler to input their bets
	 */
	public void takeBet(User u){
		new Gambling(players,u,this);
	}
	/*
	 * takeUsers is used to create a new UserDefiner linked to this handler
	 */
	public void takeUsers(){
		new UserDefiner(this);
	}
	public boolean usersEmpty() {

		return gamblers.isEmpty();
	}
}