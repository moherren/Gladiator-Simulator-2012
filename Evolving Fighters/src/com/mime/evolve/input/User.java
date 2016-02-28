package com.mime.evolve.input;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.Arrays;
import java.util.Hashtable;

import com.mime.evolve.graphics.Render2D;

public class User {
	public String name="";
	public int money=10,moneyWon=0,moneyBefore=money;
	public Hashtable<String,Integer> bets=new Hashtable<String,Integer>();
	Render2D nameArt,namePlate;
	CoinPile cp=new CoinPile(10,00,00);
	
	public User(String name) {
		this.name=name;
		
		nameArt=new Render2D(0,0);
		FontRenderContext frc=new FontRenderContext(null,true,true);
		nameArt.setFont(nameArt.getFont().deriveFont(Font.BOLD, 30));
		GlyphVector gv=nameArt.getFont().createGlyphVector(frc,this.name);
		int length=(int) (gv.getVisualBounds().getWidth()+10);
		nameArt=new Render2D(length+20,100);
		Arrays.fill(nameArt.pixels, -1);
		nameArt.setFont(nameArt.getFont().deriveFont(Font.BOLD, 30));

		nameArt.drawString(this.name, 10, 50,5);
	}
	
	public Integer getBet(Player player){
		if(bets.containsKey(player.species.name))
			return bets.get(player.species.name);
		else
			return new Integer(0);
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
		int m=getBet(p);
		
		if(m!=0){
			int maxTime=2000;
			int multiplier=Math.min(maxTime/m, 50);
			for(int i=0;i<m;i++){
				cp.removeCoin(i*multiplier);
			}
		}
		bets.remove(p.species.name);
	}
	
	public void winMoney(int m,Player p){
		moneyWon+=m;
		money+=m;
		
		if(m!=0){
			m/=2;
			int maxTime=2000;
			int multiplier=Math.min(maxTime/m, 50);
			for(int i=0;i<m;i++){
				cp.addCoin(i*multiplier);
			}
		}
		bets.remove(p.species.name);
	}
	
	public void renderName(Render2D r,int x,int y){
		r.draw(r, x, y);
	}
	
	public void renderNamePlate(Render2D r,int x,int y){
		r.background(x, y, r.width/2, r.height/4);
		r.draw(nameArt, x, y+50);
		cp.render(r, x+r.width/8*3, y+120);
	}
	
	public void updateCoins(){
		cp=new CoinPile(moneyBefore,0,0);
	}
}
