package gaknn.predictor;

import gaknn.core.Instance;
import gaknn.core.Instances;
import gaknn.core.Pair;
import gaknn.core.kdtree.KDTree;
import gaknn.similarity.AbstractSimilarity;

public class PredictorKdtree extends Predictor {
	KDTree kdTree;

	public PredictorKdtree(AbstractSimilarity sim, Instance[] trSet, Instances inst,double[] weights) {
		super(sim, trSet);
		kdTree=new KDTree(inst);
		kdTree.SetWeights(weights);
		try {
			kdTree.setInstances(inst);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("building error kd tree");
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}
	  /** get attribute values and find the majority class value confidence by finding k nearest neighbors form kd tree 
	  
	   *  find the k nearest neighbors from kd tree and find the vote for each class value and return the majority class value confidence. */
	@Override
	public double Predict(Instance instance) {
		Instances kNeighbours=new Instances(null, m_K);
		// TODO Auto-generated method stub
		double[] vote = new double[m_ClassList.length];
		int ClassIndex = 0;
		try {
		//  get the k nearest neighbors form kd tree in a form of instances
			kNeighbours=kdTree.kNearestNeighbours(instance, m_K);
			 for (int i=0; i<m_K; i++){
		            int index = kNeighbours.GetClassIndex();
		            ClassIndex = trainSet[index].GetClassIndex();
		            vote[ClassIndex]+= 1;
		        }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			e.printStackTrace();
		}
		 
			// for each k neighbors find class index and find its votes if one class value is present add 1 vote to that value
		        for (int i=0; i<m_K; i++){
		            int index = kNeighbours.GetClassIndex();
		            ClassIndex = trainSet[index].GetClassIndex();
		            vote[ClassIndex]+= 1;
		        }
		        
		        // get class value of the given instance and find its confidence with k nearest neighbor
		        int clsId = (int)instance.GetClassIndex();
		        double val = CalculateClassConf(vote,clsId);
			     
		        if (val < Double.MIN_VALUE)
		            val = 0.0;
		        else if (val > Double.MAX_VALUE)
		            val = Double.MAX_VALUE;
		        else if (Double.isNaN(val))
		            val = 0.0;

		        return val;
	}
	
	  /** get attribute values and find the majority class value by finding k nearest neighbors form kd tree 
	   * first create instance which have the attribute values
	   * then find the k nearest neighbors from kd tree and find the vote for each class value and return the majority class value and its confidence. */
	@Override
	public Pair Predict(double[] instance) {
		// TODO Auto-generated method stub
		Instance inst=new Instance(instance);
		double[] vote = new double[m_ClassList.length];
		int ClassIndex = 0;
		//Instances kNeighbours=new Instances();
		// TODO Auto-generated method stub
		try {
			//  get the k nearest neighbors form kd tree in a form of instances
			Instances kNeighbours=kdTree.kNearestNeighbours(inst, m_K);
			 for (int i=0; i<m_K; i++){
		            int index = kNeighbours.GetClassIndex();
		            ClassIndex = trainSet[index].GetClassIndex();
		            vote[ClassIndex]+= 1;
		        }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			e.printStackTrace();
		}
		 
		        // for each k neighbors find class index and find its votes if one class value is present add 1 vote to that value
		       
		        // find the largest vote and its index
		        int clsIndex = 0;
		        for (int i=1; i<m_ClassList.length; i++){
		            if (vote[clsIndex] < vote[i])
		                clsIndex = i;
		        }
		        // find confidence of the majority vote
		        double confidence = CalculateClassConf(vote,clsIndex);
		        
		        
		        return new Pair(clsIndex, confidence);
		
	}
	  /** find the confidence when votes for each class value and index of confidence need class value is given */
	 public double CalculateClassConf(double[] vote, int clsId){
	        double conf;
	        double totconf = 0.0;

	        for (int i=0; i<vote.length; i++){
	            totconf = vote[i] + totconf;
	        }

	        if (vote[clsId] > Double.MAX_VALUE)
	            conf = 1.0;
	        else
	            conf = (vote[clsId]/totconf);

	        return conf;
	    }

}
