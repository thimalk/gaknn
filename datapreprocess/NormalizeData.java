/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.datapreprocess;

import gaknn.core.Instances;

/**
 *
 * @author Niro
 */
public class NormalizeData {
    
    double[][] m_DataSet;
    
    public static double[][] Normalize(double[][] dataSet){
        double temparr[][] = dataSet;
         double maxValue = 0.0;
         double maxVlue[]= new double[dataSet[0].length];

         for (int j=0; j<maxVlue.length; j++){
             maxValue = 0.0;
             for (int i=0; i < dataSet.length; i++){
                 if (Math.abs(temparr[i][j]) > maxValue){
                     maxValue = temparr[i][j];
                 }
         }
             maxVlue[j]= Math.abs(maxValue);
         }
         for (int j=0; j<maxVlue.length; j++){
             if (maxVlue[j]> 0){
                 for (int i=0; i<dataSet.length; i++){
                    dataSet[i][j]= ((double)(temparr[i][j]/maxVlue[j]));
                 }
             }
        }
        return dataSet;
    }
    
}
