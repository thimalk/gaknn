/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.dataaccess;
import gaknn.datapreprocess.ValueHandler;
import gaknn.core.Instances;
import gaknn.core.Instance;
import gaknn.core.FastVector;
import gaknn.core.Attribute;
import java.io.IOException;

/**
 *
 * @author Niro
 */
public abstract class DataFileReader {
     /** the actual data */
    protected Instances m_Data;
    
    protected String m_filePath;
    
    public String m_RelationName;
    
    public FastVector m_Attributes;

    /** the number of lines read so far */
    protected int m_Lines;
    
    protected double[] m_Weights;
    
    protected int m_K;
    
    protected ValueHandler m_ValueHandler;
    
    public void LoadData() throws IOException{
    	if (m_filePath.length()== 0)
    		throw new IOException("Missing file");
    	else
    	{
            Instance inst;
            if (m_ValueHandler != null)
                m_ValueHandler.SetAttributes(m_Data.Attributes());

            
            int recCount = 0;

            while ((inst = ReadInstance()) != null) {
                m_Data.AddElement(recCount,inst.GetElements(), inst.GetClassIndex());
                recCount++;
            }
        }
    }
    
    public void SetClassIndex(int index){
        //this.m_Data.SetClassIndex(index);
        Attribute attr = (Attribute)this.m_Attributes.elementAt(index);
        attr.SetClassAttribute(true);
    }
    
    public void CreateDataSet(){
        m_Data = new Instances(m_RelationName, m_Attributes);
    }
    
    public void SetValueHandler(ValueHandler vh){
        m_ValueHandler = vh;
    }
    
    public void setSelectedAttributes(int[] attibutes){
        for (int i=0; i< attibutes.length; i++){
            Attribute attribute;
            attribute = (Attribute) m_Attributes.elementAt(i);

            if (attibutes[i] == 1)
                attribute.Selected(true);
            else
                attribute.Selected(false);
            
            m_Attributes.setElementAt(attribute,i);
        }
    }
    
    protected abstract Instance ReadInstance() throws IOException ;

    public Instances GetData(){
        return m_Data;
    }
    
    public double[] GetWeights(){
        return m_Weights;
    }
	    
    protected boolean IsNumber(String sVal){
        int intVal = 0;
        boolean ret = false;

        try {
            intVal = Integer.parseInt(sVal);
            ret =  true;
        } 
            catch (NumberFormatException nfe) {
            //	probably left empty
        }
        return ret;
    }
}
