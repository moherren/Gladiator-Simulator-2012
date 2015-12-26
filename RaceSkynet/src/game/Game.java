package game;

import ai.ManualControl;
import genetics.Species;
import graphics.Screen;
import race.Track;

public class Game {
	private static Track track;
	static Species[] species=new Species[]{new Species(),new Species()};
	static float time=0;
	static float nextRun=time+500;
	static float latestNextRun=time+100000;
	public static boolean[] key;
	
	public static Track getTrack() {
		return track;
	}
	
	public static void tick(boolean[] key){
		time++;
		Game.key=key;
		track.updatePosistions();
		Screen.centerX=Math.max((int) track.getCars().get(0).getPosition().x,Screen.width/2);	
		Screen.centerY=Math.max((int) track.getCars().get(0).getPosition().y,Screen.height/2);
		if(time>nextRun||time>latestNextRun)
			newGame();
		}
		

	public Game(int width,int height){
		track=new Track(width*3,height*3,Track.getSimpleTrack());
		track.createCar(species[0].nextCar());
		track.createCar(species[1].nextCar());
	}
	
	public static void delayEnd(){
		nextRun=time+600;
	}
	
	public static void newGame(){
		delayEnd();
		latestNextRun=time+20000;
		track.restartCar(0, Game.species[0].nextCar());
		track.restartCar(1, Game.species[1].nextCar());
	}
}
