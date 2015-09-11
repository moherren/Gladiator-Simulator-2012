package graphics;

import game.Game;
import ai.NeuralIntelligence;
import race.Car;

public class Render2D extends Render{
	
	public Render2D(int width, int height) {
		super(width, height);
	}
	public void render(){
		for(int i=0;i<pixels.length;i++){
			pixels[i]=0xffffff;
		}
		Game.getTrack().render(this);
	}

}
