package neuralNet;

import java.util.ArrayList;

public class NeuralNet {

	private int numInputs,numOutputs;
	private int numHiddenLayers;
	private int neuronsPerHiddenLayer;
	private NeuronLayer[] layers;
	
	public NeuralNet(int inputs,int outputs,int hiddenLayers,int neuronsPerHiddenLayer){
		numInputs=inputs;
		numOutputs=outputs;
		numHiddenLayers=hiddenLayers;
		this.neuronsPerHiddenLayer=neuronsPerHiddenLayer;
		createNet();
	}

	public void createNet(){
		layers=new NeuronLayer[numHiddenLayers];
		layers[0]=new NeuronLayer(neuronsPerHiddenLayer,numInputs);
		for(int i=1;i<numHiddenLayers;i++){
			layers[i]=new NeuronLayer(neuronsPerHiddenLayer,neuronsPerHiddenLayer);
		}
		layers[layers.length-1]=new NeuronLayer(numOutputs,neuronsPerHiddenLayer);
	}
	
	public double[] getWeights(){
		double[] weights=new double[getNumberOfWeights()];
		int i=0;
		for(NeuronLayer layer:layers){
			for(Neuron n:layer.neurons){
				for(double d:n.weights){
					weights[i]=d;
					i++;
				}
			}
		}
		return weights;
	}
	
	public int getNumberOfWeights(){
		int total=0;
		for(NeuronLayer layer:layers){
			for(Neuron n:layer.neurons){
				total+=n.weights.length;
			}
		}
		return total;
	}
	
	public void putWeights(double[] weights){
		if(weights.length!=getNumberOfWeights())
			return;
		int i=0;
		for(NeuronLayer layer:layers){
			for(Neuron n:layer.neurons){
				for(int j=0;j<n.weights.length;j++){
					n.weights[j]=weights[i];
					i++;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Double> update(ArrayList<Double> inputs){
		ArrayList<Double> outputs=new ArrayList<Double>();
		int weight=0;
		if(inputs.size()!=numInputs)
			return null;
		for(int i=0;i<layers.length;i++){
			if(i>0)
				inputs=(ArrayList<Double>)outputs.clone();
			outputs.clear();
			weight=0;
			for(int j=0;j<layers[i].numNeurons;j++){
				double totalInput=0;
				int numInput=layers[i].neurons[j].numInputs;
				for(int k=0;k<numInput-1;k++){
					totalInput+=layers[i].neurons[j].weights[k]*inputs.get(weight);
					weight++;
				}
				totalInput+=layers[i].neurons[j].weights[numInput-1]*-1;
				outputs.add(sigmoid(totalInput,1));
				weight=0;
			}
		}
		return outputs;
	}
	
	public static double sigmoid(double activation,double response){
		return 1/(1+Math.pow(Math.E, -activation/response));
	}
	
	public void randomize(){
		for(NeuronLayer layer:layers){
			for(Neuron n:layer.neurons){
				for(int j=0;j<n.weights.length;j++){
					n.weights[j]=Math.random()*2-1;
				}
			}
		}
	}

	public void fill(double filler) {
		for(NeuronLayer layer:layers){
			for(Neuron n:layer.neurons){
				for(int j=0;j<n.weights.length;j++){
					n.weights[j]=filler;
				}
			}
		}
		
	}
}
