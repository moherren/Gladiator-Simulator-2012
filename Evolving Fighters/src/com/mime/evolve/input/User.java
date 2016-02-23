package com.mime.evolve.input;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.Arrays;
import java.util.Hashtable;

import com.mime.evolve.graphics.Render2D;

public class User {
	public String name="";
	public int money=10,moneyWon=0;
	public Hashtable<String,Integer> bets=new Hashtable<String,Integer>();
	Render2D nameArt,namePlate;
	CoinPile cp=new CoinPile(money,0,0);
	
	public User(String name) {
		this.name=name;
		
		nameArt=new Render2D(0,0);
		FontRenderContext frc=new FontRenderContext(null,true,true);
		nameArt.setFont(nameArt.getFont().deriveFont(Font.BOLD, 30));
		GlyphVector gv=nameArt.getFont().createGlyphVector(frc,this.name);
		int length=(int) (gv.getVisualBounds().getWidth()+10);
		nameArt=new Render2D(length+20,150);
		Arrays.fill(nameArt.pixels, -1);
		nameArt.setFont(nameArt.getFont().deriveFont(Font.BOLD, 30));

		nameArt.drawWordArt(this.name, 10, 750,4);
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
	
	public void renderName(Render2D r,int x,int y){
		r.draw(r, x, y);
	}
	
	public void renderNamePlate(Render2D r,int x,int y){
		r.background(x, y, r.width, r.height/8);
		r.draw(nameArt, x+50, y+50);
		cp.render(r, x+r.width-100, y+50);
	}
}
