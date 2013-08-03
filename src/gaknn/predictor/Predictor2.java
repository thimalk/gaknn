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
public class Predictor2 extends Predictor1{
    public Predictor2(AbstractSimilarity sim, Instance[] trSet){
        super(sim, trSet);
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

}
