package ai;

import java.util.ArrayList;

public interface AI {
	
	public ArrayList<Double> step(ArrayList<Double> input);
	public int getFitness();
	public void setFitness(int i);
}
