/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.datapreprocess;

import gaknn.core.FastVector;

/**
 *
 * @author Niro
 */
public abstract class ValueHandler {
    protected static double DefaultValue = Double.NaN;
    protected FastVector m_Attributes;
    
    public void SetAttributes(FastVector attribues){
        m_Attributes = attribues;
    }
    
    public abstract double GetValue(int attrbuteType, int index, String sValue); 

    public abstract double GetMissingValue(int attrbuteType, int index);
    

}
