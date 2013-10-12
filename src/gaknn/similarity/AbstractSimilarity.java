/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.similarity;

import gaknn.core.FastVector;

/**
 *
 * @author Niro
 */
public abstract class AbstractSimilarity {
    public FastVector m_Attributes;
    static double[] m_Weights;
    
    public AbstractSimilarity(FastVector attributes){
        m_Attributes = attributes;
    }
    
    
    public void SetAttribute(FastVector attributes){
        m_Attributes = attributes;
    }
    
    public void SetWeights(double[] weights){
        m_Weights = weights;
    }
    
    public double[] getWeights(){
    	return m_Weights;
    }
    
    public abstract double GetSimilarity(double[] attrbuteSet1, double[] attrbuteSet2); 


}
