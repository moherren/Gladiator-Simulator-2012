package com.mime.evolve;

import java.util.ArrayList;
import java.util.Random;

import com.mime.evolve.graphics.Render;
import com.mime.evolve.input.Player;
import com.mime.evolve.level.Level;
import com.mime.evolve.level.Situation;
import com.mime.evolve.projectiles.Projectile;
import com.mime.evolve.projectiles.WepBowAndArrow;
import com.mime.evolve.projectiles.WepFlail;
import com.mime.evolve.projectiles.WepKnife;
import com.mime.evolve.projectiles.WepRapier;
import com.mime.evolve.projectiles.WepShortSword;
import com.mime.evolve.projectiles.WepSpear;
import com.mime.evolve.species.*;

public class Game {
	public static Random rand= new Random(Display.genSeed);
	public static int evolutionAmount=225;
	final public static int minEvolution=10;
	public static final Player[] noPlayers=new Player[]{null,null};
	
	
	public static final Player[] bots=new Player[]{
			new Player(0, 0,new Species(0,new WepBowAndArrow(),"bot"), 
			Player.stringToDna("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
			,new emptyGame()),
			
			new Player(0, 0,new WeaponReaderSpecies(0,new WepShortSword(),"bot"), 
			Player.stringToDna("1110011111011010011001101011110000100001000101111100111011100000100111100001001010001101100100000101101100011011000011110101")
			,new emptyGame()),
			
			new Player(0, 0,new Species(0,new WepBowAndArrow(),"bot"), 
			Player.stringToDna("0110010000010110010110101011011001000001010101011100101101100100000101010101110010110110010000010101010111001011000011110000")
			,new emptyGame()),
						
			new Player(0, 0,new ProximitySpecies(0,new WepSpear(),"bot"), 
			Player.stringToDna("0110010010010101010100100101101001010001011010010100010110100101000001101001010000011010010100000110100101000001000000000000")
			,new emptyGame()),
			
			new Player(0, 0,new Species(0,new WepRapier(),"bot"), 
			Player.stringToDna("1001010100010101100001010101100101010001010110000101010110010101000101011000010101011001010100010101100001010101000000000000")
			,new emptyGame()),
			
			new Player(0, 0,new Species(0,new WepFlail(),"bot"), 
			Player.stringToDna("1001010100010101100001010101100101010001010110000101010110010101000101011000010101011001010100010101100001010101000000000000")
			,new emptyGame()),
			
			new Player(0, 0,new ReverseVisionSpecies(0,new WepKnife(),"bot"), 
			Player.stringToDna("0110100101110100011111111111101001100011010100110010110000110011000010010111010011010011111100001110001010101011111111011101")
			,new emptyGame()),
	};
	
	
	public static final Player[] testBots=new Player[]{
		new Player(0, 0,new Species(0,new WepBowAndArrow(),"bot"), 
		Player.stringToDna("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
		,new emptyGame()),
		
		new Player(0, 0,new Species(0,new WepBowAndArrow(),"bot"), 
		Player.stringToDna("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
		,new emptyGame()),
		
		new Player(0, 0,new Species(0,new WepBowAndArrow(),"bot"), 
		Player.stringToDna("0110010000010110010110101011011001000001010101011100101101100100000101010101110010110110010000010101010111001011000011110000")
		,new emptyGame()),
					
		new Player(0, 0,new Species(0,new WepBowAndArrow(),"bot"), 
		Player.stringToDna("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
		,new emptyGame()),
				
		new Player(0, 0,new Species(0,new WepBowAndArrow(),"bot"), 
		Player.stringToDna("0110010000010110010110101011011001000001010101011100101101100100000101010101110010110110010000010101010111001011000011110000")
		,new emptyGame()),
		
		new Player(0, 0,new Species(0,new WepFlail(),"bot"), 
		Player.stringToDna("1001010100010101100001010101100101010001010110000101010110010101000101011000010101011001010100010101100001010101000000000000")
		,new emptyGame()),
		
		new Player(0, 0,new ReverseVisionSpecies(0,new WepKnife(),"bot"), 
		Player.stringToDna("0110100101110100011111111111101001100011010100110010110000110011000010010111010011010011111100001110001010101011111111011101")
		,new emptyGame()),
};
	
	
	public static boolean[] breed(boolean[] mom,boolean[] dad){
		Random rand=new Random();
		boolean chromosome;
		boolean[] dna=new boolean[Player.situations*Player.reactions+Player.traits];
		for(int x=0;x<Player.reactions*Player.situations+Player.traits;x++){
			if(rand.nextBoolean())
			chromosome=mom[x];
			else chromosome=dad[x];
			
			if(rand.nextInt((Player.reactions*Player.situations+Player.traits)*15)==0){
				chromosome=!chromosome;
			}
			dna[x]=chromosome;
		}
			
		return dna;
	}
	public static boolean[] breed(Species s){
		Random rand=new Random();
		ArrayList<boolean[]> ogp= (ArrayList<boolean[]>) s.oldGenePool.clone();
		boolean Mom=true,Dad=true;
		boolean[] mom;
		if(!ogp.isEmpty()){
			mom=ogp.get(rand.nextInt(ogp.size()));
		}
		else{
			mom=Player.generateSequences(Player.reactions*Player.situations+Player.traits);
			Mom=false;
		}
		for(boolean[] a:s.oldGenePool){
			if(a.equals(mom))
			ogp.remove(mom);
		}
		boolean[] dad;
		if(!ogp.isEmpty()){
			dad=ogp.get(rand.nextInt(ogp.size()));
		}
		else{
			dad=Player.generateSequences(Player.reactions*Player.situations+Player.traits);
			Dad=false;
		}
		return breed(mom,dad);
	}
	public static boolean[] mutate(boolean[] dNA2){;
		for(int x=0;x<Player.reactions*Player.situations+Player.traits;x++){
			if(rand.nextInt((Player.reactions*Player.situations+Player.traits)*20)==0){
				dNA2[x]=!dNA2[x];
			}
		}
		return dNA2;
	}
	public static Species newSpecies(int c){
		//return new Species(c);
		int seed=rand.nextInt(20);
		switch(seed){
		case 0: return new BulletProximitySpecies(c);
		case 1: return new ProximitySpecies(c);
		case 2: return new DualSightedSpecies(c);
		case 3: return new ReverseVisionSpecies(c);
		case 4: return new TimeOrientedSpecies(c);
		case 5: return new BroaderVisionSpecies(c);
		case 6: return new EnemyVisionSpecies(c);
		case 7: return new ReloadReaderSpecies(c);
		case 8:return newSpecies(c);
		case 9: return new ConstantVisionSpecies(c);
		case 10: return new VisionSizeSpecies(c);
		case 11: return new WeaponReaderSpecies(c);
		default:return newSpecies(c);
		}
	}
	public long time=0;
	public long oldTime=time;
	public final static long upperTimeLength=2500;
	public long upperTime=time+upperTimeLength;
	public Level level;
	public Species species1;
	public Species species2;
	public Species[] species={species1,species2,species1,species2};
	public ArrayList<Projectile> projectiles=new ArrayList<Projectile>();
	public ArrayList<Projectile> destroiedProjectiles=new ArrayList<Projectile>();
	
	short battleNumber=1,gen=1,fNum=0;
	
	Game tourny=null;
	public Player player1;
	public Player player2;
	double dBetweenPlayers=10000;
	public Game(){
		Random rand=new Random();
		//species=new Species[]{newSpecies(rand.nextInt((int) Math.pow(16,6))),newSpecies(rand.nextInt((int) Math.pow(16,6))),newSpecies(rand.nextInt((int) Math.pow(16,6))),newSpecies(rand.nextInt((int) Math.pow(16,6)))};
		species1=newSpecies(rand.nextInt((int) Math.pow(16,6)));
		species=new Species[]{species1,newSpecies(rand.nextInt((int) Math.pow(16,6))),newSpecies(rand.nextInt((int) Math.pow(16,6))),newSpecies(rand.nextInt((int) Math.pow(16,6)))};
		
		for(Species s:species){
			s.generateFights(this);
		}
		
		newGame();
		level=new Level(1200,800);
		for(int i=0;i<360;i++){
			double a=i/180.0000*Math.PI;
			double x = 600 + 360 * Math.cos(a);
			double y = 400 + Render.hDisplacement*360 * Math.sin(a);
			/*if(rand.nextInt(3)==0)
			level.addWall(x, y, 10, 10);*/
			level.addWall((int)x, (int)y, 5, 5);
		}
	}
	
	public Game(boolean b){
		
	}
	
	public void baseOpponet(Situation sit,int botNum,Species s){
		if(!s.projectile.meele){
			sit.setDNA(bots[botNum].DNA, false);
			sit.setSpecies(bots[botNum].species, false);
		}
		else
		{
			sit.setDNA(testBots[botNum].DNA, false);
			sit.setSpecies(testBots[botNum].species, false);
		}
	}
	
	public Projectile createProjectile(Projectile proj){
		projectiles.add(proj);
		return proj;
	}
	public void destroyProjectile(Projectile proj){
		destroiedProjectiles.add(proj);
	}
	public void endGame(){
		oldTime=time;
		
		if(player2.health<=0&&fNum<=11){
			fNum++;
		}
		else{
			species1.addToGenePool(player1);
			fNum=0;
			battleNumber++;
			species1=species[battleNumber%4];
		}
		
		
		//player2.species.addToGenePool(player2);
		
		if(battleNumber==16){
			battleNumber=0;
			gen++;
			for(Species s:species)
				s.transeferGenePool();
		}

		newGame();
	}
	
	public Player getEnemy(Player p){
		if(player1.equals(p)){
			return player2;
		}
			return player1;
	}
	public Species getEnemy(Species replaced) {
		if(replaced.equals(species2))
			return species1;
		else if(replaced.equals(species1))
			return species2;
		return null;
	}
	public double getLoad() {
		return (gen*1.00000)/evolutionAmount;
	}
	protected void newGame() {
		dBetweenPlayers=10000;
		boolean win=false;
		for(Projectile p:projectiles){
			destroyProjectile(p);
		}

		try{
		if(player2.health>0){
			player1=new Player(11,Math.PI,species1,breed(species1),this);
		}
		else
			win=true;
		
		}catch(NullPointerException n){
			player1=new Player(11,Math.PI,species1,this);
		}
		
		Player temp=player1;
		species1.fights[fNum].setDNA(player1.DNA, true);
		species1.fights[fNum].applySituation(this);
		
		if(win)
			player1.fitness+=temp.fitness;
	}
	public void resetCountdown(){
		oldTime=time;
		upperTime=time+upperTimeLength;
	}
	public void resetCountdown(int delay){
		oldTime=Math.max(time-delay,oldTime);
		oldTime=Math.min(upperTime, oldTime);
	}
	public Player[] tick(boolean key[]){
		time++;
		
		//boolean left=key[KeyEvent.VK_A];
		//boolean right=key[KeyEvent.VK_D];
		//boolean up=key[KeyEvent.VK_W];
		//boolean down=key[KeyEvent.VK_S];
		//boolean shoot=key[KeyEvent.VK_TAB];
		
		if (gen<evolutionAmount){
			player1.tick(this);
			player2.tick(this);
			
			double dBP=Math.sqrt(Math.pow(player1.x-player2.x, 2)+Math.pow(player1.y-player2.y, 2));
			if(dBP<dBetweenPlayers&&(player1.species.projectile.meele||player2.species.projectile.meele)){
				dBetweenPlayers=dBP;
				resetCountdown(500);
			}
			
			for(int i=0;i<destroiedProjectiles.size();i++){
				Projectile p=destroiedProjectiles.get(i);
				projectiles.remove(p);
			}
			destroiedProjectiles.clear();
			
			for(int i=0;i<projectiles.size();i++){
				try{
				projectiles.get(i).tick();
				}
				catch(NullPointerException n){
					
				}
			}
			
			if(Math.sqrt(Math.pow(player1.y-player2.y,2)+Math.pow(player1.x-player2.x, 2))<=player1.size+player2.size){
				double dir=Math.atan2(player1.y-player2.y, player1.x-player2.x);
				player1.move(dir+Math.PI, 0.05*player2.size);
				player2.move(dir, 0.05*player1.size);
			}
		
			if(time-oldTime>=1000) 
				endGame();
			
			if(player1.health<=0||player2.health<=0)
				endGame();
			
			return noPlayers;
		}
		else if(gen>evolutionAmount){
			//System.out.println("Tick Game");
			return tourny.tick(key);
		}
		else{
			Player[] p=new Player[4];
			for(int i=0;i<4;i++){
				p[i]=species[i].elite;
			}
			tourny=new HalfSorter(p);
			gen++;
			return noPlayers;
		}
	}
	public long getTime() {
		return time;
	}
}