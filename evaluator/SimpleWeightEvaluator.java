/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.evaluator;

import gaknn.core.Instance;
import gaknn.predictor.Predictor;

/**
 *
 * @author Niro
 */
public class SimpleWeightEvaluator extends Evaluator{
    
    public SimpleWeightEvaluator(Predictor predictor, Instance[] testSet){
        super(predictor, testSet);
     }
    
    
    public double Evaluate (){
        double fitness = 0.0;
        int testSetSize = m_TestSet.length;
        double fit = 0.0;
        
        for (int i=0; i<testSetSize; i++){
            fit += m_Predictor.Predict(m_TestSet[i]);
            fitness += (1 - fit)*(1 - fit);
        }
        
        if (fitness > Double.MAX_VALUE)
            fitness = Double.MAX_VALUE;
        else if (fitness < Double.MIN_VALUE)
            fitness = Double.MIN_VALUE;
        return Math.sqrt(fitness);
    }

}
