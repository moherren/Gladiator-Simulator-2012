package neuralNet;

public class NeuronLayer {

	public int numNeurons;
	public Neuron[] neurons;
	
	public NeuronLayer(int numNeurons,int inputsPerNeuron) {
		this.numNeurons=numNeurons;
		neurons=new Neuron[numNeurons];
		for(int i=0;i<neurons.length;i++){
			neurons[i]=new Neuron(inputsPerNeuron);
		}
	}

}
