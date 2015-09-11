package game;

import ai.AI;
import ai.NeuralIntelligence;
import race.Track;

public class Game {
	private static Track track;
	
	public static Track getTrack() {
		return track;
	}

	public Game(int width,int height){
		track=new Track(width,height,Track.getCircleTrack());
		track.createCar(new NeuralIntelligence(true));
		track.createCar(new NeuralIntelligence(true));
	}
}
