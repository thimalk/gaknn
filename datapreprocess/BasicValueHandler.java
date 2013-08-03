/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.datapreprocess;

import gaknn.core.Attribute;

/**
 *
 * @author Niro
 */
public class BasicValueHandler extends ValueHandler{
    
    public double GetValue(int attrbuteType, int index, String sValue){
        double retValue = DefaultValue;

        switch (attrbuteType) {
        case  Attribute.STRING:
            retValue = DefaultValue;
        case Attribute.NOMINAL:
            Attribute attribute;
            attribute = (Attribute) m_Attributes.elementAt(index);
            retValue = attribute.IndexOfValue(sValue);
            
            if (retValue == -1)
                retValue = DefaultValue;
        }
        return retValue;
    }
    
    public double GetMissingValue(int attrbuteType, int index){
        return DefaultValue;
    }

}
