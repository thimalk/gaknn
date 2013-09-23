package gaknn.predictor;

import java.util.Arrays;

import gaknn.core.Instance;
import gaknn.core.Pair;
import gaknn.similarity.AbstractSimilarity;

public class  PredictorRegression extends Predictor  {

	public PredictorRegression(AbstractSimilarity sim, Instance[] trSet) {
		super(sim, trSet);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double Predict(Instance instance) {
		 int dataLength = trainSet.length;
	        Pair[] simList = new Pair[dataLength];
	        
	        for (int i=0; i < dataLength; i++){
	            double simValue = similarityMeasure.GetSimilarity(instance.GetElements(),trainSet[i].GetElements());
	            simList[i] = new Pair(i,simValue);
	        }
	        
	        Arrays.sort(simList);
	        double[] vote=new double[m_K];
		
	        double PredictedIndexValue=0;
	        for (int i=0; i<m_K; i++){
	            int index = ((Pair)simList[i]).Index();
	            vote[i]= trainSet[index].GetClassIndex();
	            PredictedIndexValue+=vote[i];
	        }
	        PredictedIndexValue/=m_K;
	        System.out.println("pred "+PredictedIndexValue);
	        double IdValue = (double)instance.GetClassIndex();
	        double val = CalculateClassConf(vote,IdValue);
		     
	        if (val < Double.MIN_VALUE)
	            val = 0.0;
	        else if (val > Double.MAX_VALUE)
	            val = Double.MAX_VALUE;
	        else if (Double.isNaN(val))
	            val = 0.0;

	        return val;
	}

	@Override
	public Pair Predict(double[] instance) {
	     Pair[] simList = new Pair[trainSet.length];
	        int dataLength = trainSet.length;
	        
	        for (int i=0;i<dataLength;i++) { 
	            double simValue = similarityMeasure.GetSimilarity(instance,trainSet[i].GetElements());
	            simList[i] = new Pair(i,simValue);
	        }
	        
	        Arrays.sort(simList);
	        double[] vote=new double[m_K];
			
	        double PredictedIndexValue=0;
	        for (int i=0; i<m_K; i++){
	            int index = ((Pair)simList[i]).Index();
	            vote[i]= trainSet[index].GetClassIndex();
	            PredictedIndexValue+=vote[i];
	        }
	        PredictedIndexValue/=m_K;
	        
	        double confidence = CalculateClassConf(vote,PredictedIndexValue);
	        
	        return new Pair(PredictedIndexValue, confidence);
	       
	}
    public double CalculateClassConf(double [] vote, double IdValue){
        double conf=0;
        for(int i=0;i<m_K;i++){
        	conf+=Math.abs(vote[i]-IdValue);
        }
        
        conf/=m_K;
       conf=1-(conf/IdValue);
       if(conf<0)
    	   conf=0;
     
        return conf;
    }

}
