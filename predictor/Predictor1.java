/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.predictor;

import gaknn.core.Instance;
import gaknn.similarity.*;
import gaknn.core.Pair;
import java.util.Arrays;

/**
 *
 * @author Niro
 */
public class Predictor1 extends Predictor {
    
    public Predictor1(AbstractSimilarity sim, Instance[] trSet){
        super(sim, trSet);
    }
    
    public double Predict(Instance instance){
        int dataLength = trainSet.length;
        Pair[] simList = new Pair[dataLength];
        
        for (int i=0; i < dataLength; i++){
            double simValue = similarityMeasure.GetSimilarity(instance.GetElements(),trainSet[i].GetElements());
            simList[i] = new Pair(i,simValue);
        }
        
        Arrays.sort(simList);
        
        double[] vote = new double[m_ClassList.length];
	int ClassIndex = 0;
        
        for (int i=0; i<m_K; i++){
            int index = ((Pair)simList[i]).Index();
            ClassIndex = trainSet[index].GetClassIndex();
            vote[ClassIndex]+= ((Pair)simList[i]).Value();
        }
        
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
    
    public Pair Predict(double[] instance){
        Pair[] simList = new Pair[trainSet.length];
        int dataLength = trainSet.length;
        
        for (int i=0;i<dataLength;i++) { 
            double simValue = similarityMeasure.GetSimilarity(instance,trainSet[i].GetElements());
            simList[i] = new Pair(i,simValue);
        }
        
        Arrays.sort(simList);
        double[] vote = new double[m_ClassList.length];
        int ClassIndex = 0;
        
        for (int i=0; i<m_K; i++){
            int index = ((Pair)simList[i]).Index();
            ClassIndex = trainSet[index].GetClassIndex();
            vote[ClassIndex]+= ((Pair)simList[i]).Value();
        }
        
        int clsIndex = 0;
        for (int i=1; i<m_ClassList.length; i++){
            if (vote[clsIndex] < vote[i])
                clsIndex = i;
        }
        
        double confidence = CalculateClassConf(vote,clsIndex);
        
        return new Pair(clsIndex, confidence);
    }
    
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
