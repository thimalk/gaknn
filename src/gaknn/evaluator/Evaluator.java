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


public abstract class Evaluator  {
    Instance[] m_TestSet;
    Predictor m_Predictor;

    public abstract double Evaluate ();
        
    public Evaluator(Predictor predictor, Instance[] testSet){
        this.m_Predictor = predictor;
        this.m_TestSet = testSet;
    }
    
    public void SetPredictor(Predictor predictor){
        m_Predictor = predictor;
    }
	
	//public abstract Pair PredictInstance(double[] intsnce);
}