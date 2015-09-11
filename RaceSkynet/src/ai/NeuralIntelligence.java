package ai;

import java.util.ArrayList;

import neuralNet.NeuralNet;

public class NeuralIntelligence implements AI{

	NeuralNet net;
	private static int inputs=16,outputs=3,hiddenLayers=2,neuronsPerHiddenLayer=6;
	
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

}
