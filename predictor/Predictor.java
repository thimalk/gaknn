/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.predictor;

import gaknn.core.Instance;
import gaknn.similarity.*;
import gaknn.core.Pair;

/**
 *
 * @author Niro
 */
public abstract class Predictor {
    
    AbstractSimilarity similarityMeasure;
    Instance[] trainSet;
    
    public Predictor(AbstractSimilarity sim, Instance[] trSet){
        this.similarityMeasure = sim;
        this.trainSet = trSet;
    }
    public abstract double Predict(Instance instance); 

    public abstract Pair Predict(double[] instance);

    public void setClassList(String[] clsList){
            this.m_ClassList = clsList;
    }
    
    public void setK(int k){
        m_K = k;
    }
    
    public void setSimilarity(AbstractSimilarity similarity){
        similarityMeasure = similarity;
    }

    protected int m_K = 5;
    protected String[] m_ClassList;
}

