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
public class EuclideanSimilarity extends AbstractSimilarity{
    
    public EuclideanSimilarity(FastVector attributes){
        super(attributes);
    }
        
    public double GetSimilarity(double[] attrbuteSet1, double[] attrbuteSet2)
    {
	double simValue = 0.0;
	double dif = 0.0;
		
	for (int i=0; i<attrbuteSet1.length; i++){
            dif = m_Weights[i] * Math.sqrt(Math.abs(attrbuteSet1[i]*attrbuteSet1[i] - attrbuteSet2[i]*attrbuteSet2[i]));
            simValue = simValue + dif; 
	}
		
	simValue = 1/simValue;
	return simValue;
    }

}
