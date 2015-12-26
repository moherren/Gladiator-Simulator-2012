package ai;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import game.Game;

public class ManualControl implements AI{

	public ArrayList<Double> step(ArrayList<Double> input) {
		boolean[] keys=Game.key;
		ArrayList<Double> list=new ArrayList<Double>();
		if(keys[KeyEvent.VK_UP])
			list.add(new Double(1));
		else if(keys[KeyEvent.VK_DOWN])
			list.add(new Double(-1));
		else
			list.add(new Double(0));
		
		if(keys[KeyEvent.VK_RIGHT])
			list.add(new Double(1));
		else if(keys[KeyEvent.VK_LEFT])
			list.add(new Double(-1));
		else
			list.add(new Double(0));
		
		return list;
	}

	public int getFitness() {

		return 0;
	}

	public void setFitness(int i) {
	}
}
