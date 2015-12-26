package com.mime.evolve.input;

import java.util.Hashtable;

public class User {
	public String name="";
	public int money=10,moneyWon=0;
	public Hashtable<String,Integer> bets=new Hashtable<String,Integer>();
	public User(String name) {
		this.name=name;
	}
	public Integer getBet(Player player){
		if(bets.containsKey(player.species.name))
			return bets.get(player.species.name);
		else
			return 0;
	}
	public int getBet(Player player, Player player2) {
		if(getBet(player)>0)
			return -getBet(player);
		return getBet(player2);
	}
	public String getName() {
		return name;
	}
	public void placeBet(Player player,Integer integer){
		bets.put(player.species.name, integer);
	}
	public void removeBet(Player p){
		bets.remove(p.species.name);
	}
	public void winMoney(int m){
		moneyWon+=m;
		money+=m;
	}
}
