package genetics;

import java.util.ArrayList;
import java.util.Random;

import ai.NeuralIntelligence;
import race.Car;

public class Species {
	public final int MEMBERCOUNT=16;
	ArrayList<NeuralIntelligence> activeMembers=new ArrayList<NeuralIntelligence>();
	NeuralIntelligence elite=new NeuralIntelligence(true);
	int carsUsed=0,gen=0;
	
	public Species(){
		for(int i=0;i<MEMBERCOUNT;i++){
			activeMembers.add(new NeuralIntelligence(true));
		}
	}
	
	public void generate(){
		gen++;
		boolean winners=false;
		for(NeuralIntelligence ai:activeMembers){
			if(ai.getFitness()>elite.getFitness()){
				elite=ai;
				winners=true;
			}
		}
		if(!winners)
			activeMembers.add(elite);
		
		System.out.println("elite fitness = "+elite.fitness);
		
		Random rand=new Random();
		ArrayList<NeuralIntelligence> oldMembers=new ArrayList<NeuralIntelligence>();
		ArrayList<NeuralIntelligence> newMembers=new ArrayList<NeuralIntelligence>();
		for(NeuralIntelligence ai:activeMembers){
			for(int i=0;i<ai.fitness;i++){
				oldMembers.add(ai);
			}
		}

		for(int i=0;i<MEMBERCOUNT;i++){
			if(oldMembers.size()!=0){
				ArrayList<NeuralIntelligence> oMembers=(ArrayList<NeuralIntelligence>) oldMembers.clone();
				NeuralIntelligence Mom=oldMembers.get(rand.nextInt(oMembers.size()));
				for(int b=0;b<Mom.getFitness();b++){
					oMembers.remove(Mom);
				}
				NeuralIntelligence Dad;
				if(oMembers.size()!=0)
				Dad=oMembers.get(rand.nextInt(oMembers.size()));
				else
					Dad=new NeuralIntelligence(true);
				newMembers.add(mate(Mom,Dad));
			}
			else{
				newMembers.add(new NeuralIntelligence(true));
			}
		}
		carsUsed=0;
		activeMembers.clear();
		activeMembers.addAll(newMembers);
	}
	
	public static NeuralIntelligence mate(NeuralIntelligence Mom,NeuralIntelligence Dad){
		Random rand=new Random((long) (Math.random()*Long.MAX_VALUE));
		double[] m=Mom.getNeuralNet().getWeights(),d=Dad.getNeuralNet().getWeights();
		double[] weights=new double[m.length];
		for(int i=0;i<weights.length;i++){
			if(rand.nextBoolean())
				weights[i]=m[i];
			else
				weights[i]=d[i];
			if(rand.nextDouble()>0.95)
					weights[i]=rand.nextDouble()*2-1;
		}
		return new NeuralIntelligence(weights);
	}
	
	public NeuralIntelligence nextCar(){
		if(carsUsed==MEMBERCOUNT)
			generate();
		return activeMembers.get(carsUsed++);
	}
	
	public NeuralIntelligence getElite(){
		return elite;
	}
	
	public String toString(){
		return "carNum:"+carsUsed+" gen:"+gen+" elite:"+elite.fitness;
	}
}
