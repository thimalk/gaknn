/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.similarity;
import gaknn.core.Attribute;
import gaknn.core.FastVector;

/**
 *
 * @author Niro
 */
public class BasicSimilarity extends AbstractSimilarity{
    
    public BasicSimilarity(FastVector attributes){
        super(attributes);
    }
    
    public double GetSimilarity(double[] attrbuteSet1, double[] attrbuteSet2){
        double simValue = 0.0;
        double dif = 0.0;
	int iWeightPos = 0;
	int numAttributes =attrbuteSet1.length;	
	for (int i=0; i<numAttributes; i++){
            Attribute attribute;
            attribute = (Attribute) m_Attributes.elementAt(i);
				
            switch (attribute.Type()){
		case Attribute.NOMINAL:
                    if (attrbuteSet1[i] == attrbuteSet2[i])
			dif = 0.0;
                    else
			dif = m_Weights[iWeightPos];
		default:
			dif = m_Weights[iWeightPos] * Math.abs(attrbuteSet1[i] - attrbuteSet2[i]);
            }
            iWeightPos++;
            simValue +=  dif;
	}
		
		
        if (simValue < Double.MIN_VALUE)
            simValue = Double.MAX_VALUE;
        else
            simValue = 1.0/simValue;

        if (Double.isInfinite(simValue))
            simValue = Double.MAX_VALUE;
		
        return simValue;
    }

}
