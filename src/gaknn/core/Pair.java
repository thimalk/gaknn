/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.core;

/**
 *
 * @author Niro
 */
public class Pair implements Comparable{

    public Pair(int i,double v){
        index=i;
        value=v;
    }
    //author thimal
    public Pair(double i,double v){
    	indexValue=i;
    	value=v;
    }
    
    int index; 
    double value;
    //author thimal
    double indexValue;
	   
    public int Index(){
        return index;
    }
    
    public double Value(){
        return value;
    }
    //author thimal
    public double indexValue(){
    	return indexValue;
    }
	   
    public int compareTo(Object pair) throws ClassCastException {
        if ((((Pair) pair).value) > this.value)
            return 1;
        else if ((((Pair) pair).value) == this.value)
            return 0;
        else
            return -1;
    }

}

