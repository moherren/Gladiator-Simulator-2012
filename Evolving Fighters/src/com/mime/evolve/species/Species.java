package com.mime.evolve.species;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.mime.evolve.Game;
import com.mime.evolve.emptyGame;
import com.mime.evolve.graphics.Render;
import com.mime.evolve.graphics.Render2D;
import com.mime.evolve.graphics.Texture;
import com.mime.evolve.input.Player;
import com.mime.evolve.input.RandomNameGenerator;
import com.mime.evolve.level.Situation;
import com.mime.evolve.projectiles.*;

public class Species {
	public int reactions=0,situations=0,physicalTraits=0;
	public Projectile projectile=new WepBowAndArrow();
	public ArrayList<boolean[]> oldGenePool=new ArrayList<boolean[]>();
	private Hashtable<boolean[],Integer> genePool=new Hashtable<boolean[],Integer>();
	public Player elite=new Player(0,0,this,new emptyGame());
	public int color, skin=0xfdc14c, viewC=0x000000;
	public String descriptor="normal",name="No Name";
	public Situation[] fights=new Situation[13];
	public int maxFit=0;
	Render2D nameArt,armor; 
	public static int rWeapon=-1;
	public Species(int color){
		//this.skin=newSkin();
		this.color=color;
		
		do{
		name=RandomNameGenerator.generateName();
		}while(name.length()>=36);
		
		try {
			drawNameArt();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		generateArmor();
		projectile=createRandomProjectile();
		
	}
	public Species(int color,Projectile proj,String name){
		this.color=color;
		this.name=name;
		projectile=proj;
	}
	public void addToGenePool(Player p){
		if(p.fitness>maxFit){
			maxFit=p.fitness;
			elite=p;
		}
		boolean[] temp=p.DNA;
		int fitness=p.fitness;
		genePool.put(temp, fitness);
		return;
	}
	private void checkForElite(){
		Player p=new Player(0,0,this,new emptyGame());
		for(boolean[] i:genePool.keySet()){
			if(genePool.get(i)<p.fitness){
				p=new Player(0,0,this,i,new emptyGame());
			}
		}
		elite=p;
		return;
	}
	public void drawNameArt() throws FontFormatException, IOException{
		GlyphVector gv=Render.getGlyohVector(name, Font.createFont(Font.TRUETYPE_FONT, new File("Constantine.ttf")).deriveFont(0,25));
		int width=(int) (gv.getVisualBounds().getWidth())+20;
		int height=(int) (gv.getVisualBounds().getHeight())+50;
		nameArt=new Render2D(width,height);
		nameArt.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("Constantine.ttf")).deriveFont(0,25));
		nameArt.drawOutlinedWordArt(name,10,(int) (gv.getVisualBounds().getHeight()+25), color+4);
	}
	public void generateFights(Game game){
		ArrayList<Integer> botNum=new ArrayList<Integer>();
		for(int i=0;i<Game.bots.length-1;i++){
			botNum.add(new Integer(i));
		}
		Collections.shuffle(botNum);
		
		fights[0]=new Situation();
		game.baseOpponet(fights[0], 0,this);
		for(int i=0;i<(Game.bots.length-1)*2;i++){
			fights[i+1]=new Situation();
			game.baseOpponet(fights[i+1], botNum.get(i%(Game.bots.length-1))+1,this);
		}
		
		for(Situation sit:fights){
			sit.setSpecies(this, true);
		}
	}
	public int getFitness(boolean[] dna){
		int m=0;
		for(boolean[] b:oldGenePool)
			if(dna.equals(b))
				m++;
		return m;
	}
	
	public Render getNameArt(){
		return nameArt;
	}
	public void tick(Game game,Player user){
		Player enemy=game.getEnemy(user);
		int sitNum=1;
		if(Player.rangeOfDirection(user.x, enemy.x, user.y, enemy.y, user.direction, user.broadCast,enemy.size)){
			sitNum++;
		}
		loop:for(int i=0;i<game.projectiles.size();i++){
			Projectile proj=game.projectiles.get(i);
			if(proj!=null)
			if(Player.rangeOfDirection(user.x, proj.x, user.y, proj.y, user.direction, user.broadCast,proj.size)&&proj.target.equals(this)){
				sitNum+=2;
				break loop;
			}
		}
		
		/*if(user.broadCast>user.minCast&&viewEnemy)
			user.broadCast*=1-user.castRate;
		else if(!viewEnemy) user.broadCast=user.maxCast;*/
		user.execute(sitNum, enemy);
		//direction%=(Math.PI*2);
		if(user.direction<0)user.direction+=(Math.PI*2);
	}
	public void transeferGenePool(){
		oldGenePool.clear();
		Set<boolean[]> set=genePool.keySet();
		List<boolean[]> setStuff= new ArrayList<boolean[]>();
		setStuff.addAll(set);
		
		if(!genePool.contains(elite))
			genePool.put(elite.DNA,maxFit);

		for(boolean[] p:setStuff){
			for(int i=0;i<genePool.get(p);i++){
				oldGenePool.add(p);
			}
			if(!elite.DNA.equals(p)){
				genePool.remove(p);
			}
			else{
				
			}
		}
	}
	
	public void generateArmor(){
		String file;
		switch(Game.rand.nextInt(3)){
		case 0:file="Textures/armor1.png";
		break;
		case 1:file="Textures/armor2.png";
		break;
		default:file="Textures/armor3.png";
		break;
		}
		Render a=Texture.loadBitmap(file);
		armor=new Render2D(a.width,a.height);
		Arrays.fill(armor.pixels,color);
		armor.draw(a, 0, 0);
	}
	
	public Render2D getArmor(){
		return armor;
	}
	
	public static Projectile createRandomProjectile(){
		Random rand=new Random(Game.rand.nextLong());
		Projectile projectile;
		int seed=rand.nextInt(6);
		if(rWeapon>=0)
			seed=rWeapon;
		switch(seed){
		case 0:projectile=new WepShortSword();
		break;
		case 1:projectile=new WepBowAndArrow();
		break;
		case 2:projectile=new WepKnife();
		break;
		case 3:projectile=new WepRapier();
		break;
		case 4:projectile=new WepFlail();
		break;
		case 5:projectile=new WepSpear();
		break;
		default: projectile=createRandomProjectile();
		}
		return projectile;
	}
	
	private static int newSkin() {
		int skin=0xFFDFC4;
		switch(Game.rand.nextInt()%5){
		case 0:skin=0xffcd94;
			break;
		case 1:skin=0xffad60;
			break;
		case 2:skin=0xffe39f;
			break;
		case 3:skin=0x58422d;
			break;
		case 4:skin=0x3b2f27;
			break;
		case 5:skin=0xCE967C;
			break;
		case 6:skin=0xBA6C49;
			break;
		case 7:skin=0xF0C8C9;
			break;
		case 8:skin=0xB97C6D;
			break;
		case 9:skin=0xAD6452;
			break;
		case 10:skin=0xCB8442;
			break;
		case 11:skin=0x704139;
			break;
		case 12:skin=0x870400;
			break;
		case 13:skin=0x430000;
			break;
		}
		return skin;
	}
}
