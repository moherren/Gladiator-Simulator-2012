package neuralNet;

public class Neuron {

	public int numInputs;
	public double[] weights;
	
	public Neuron(int inputs){
		numInputs=inputs+1;
		weights=new double[numInputs];
		for(int i=0;i<weights.length;i++){
			weights[i]=Math.random()*2-1;
		}
	}

}
