package ai;

import java.util.ArrayList;

import neuralNet.NeuralNet;

public class NeuralIntelligence implements AI{

	NeuralNet net;
	private static int inputs=18,outputs=3,hiddenLayers=2,neuronsPerHiddenLayer=7;
	public int fitness=0;
	
	public NeuralIntelligence(double[] weights){
		net=new NeuralNet(inputs,outputs,hiddenLayers,neuronsPerHiddenLayer);
		net.putWeights(weights);
	}
	
	public NeuralIntelligence(boolean random){
		net=new NeuralNet(inputs,outputs,hiddenLayers,neuronsPerHiddenLayer);
		if(random)
			net.randomize();
		else
			net.fill(0);
	}
	
	public ArrayList<Double> step(ArrayList<Double> input) {
		return net.update(input);
	}
	
	public NeuralNet getNeuralNet(){
		return net;
	}
	
	public void setFitness(int f){
		fitness=f;
	}
	
	public int getFitness(){
		return fitness;
	}
}
