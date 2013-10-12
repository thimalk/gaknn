/**
 * 
 */
package gaknn.dataaccess;

import gaknn.core.FastVector;
import gaknn.core.Instance;
import gaknn.core.Instances;
import gaknn.datapreprocess.BasicValueHandler;

import java.io.IOException;

/**
 * class to read test file and training file in a given user format (csv, arff)
 * 
 * @author Thimal
 * 
 */
public class DataAccesser {
	 private String m_className;
	private  FastVector m_Attributes;
	private  int m_ClassAttribIndex=0;
	private String m_PredictorModel;
	
	public DataAccesser(int classAttibuteIndex,String method){
		m_ClassAttribIndex=classAttibuteIndex;
		m_PredictorModel=method;
		
	}
	 /**
     * method to get the file extension of file name
     * @param filePath
     * @return int file extension arff 1, csv 2 and any other -1
     */
    public  int getFileExtension(String filePath){
    	int arffIndex=filePath.indexOf("arff");
    	int csvIndex=filePath.indexOf("csv");
    	int lastIndex=filePath.length();

    	if(arffIndex!=-1&&filePath.substring(arffIndex,lastIndex).equalsIgnoreCase("arff"))
    		
    			return 1;
    	if(csvIndex!=-1&&filePath.substring(csvIndex,lastIndex).equalsIgnoreCase("csv"))
    		return 2;
    	return -1;

    	
    }
    /**
     * method to read the training dataset from csv or arff file and input to the Instances data structure 
     * @param filePath
     * @returnn Instances of data set
     * @throws IOException
     */
	public Instances ReadData(String filePath) throws IOException{
		Instances m_Data=null;
	        try
	        {
	        	
	            if (filePath.length()==0) throw new IOException("Missing file name");
	            DataFileReader dataFileReader;
	            if(getFileExtension(filePath)==1)
	            dataFileReader = new ArffFileReader(filePath);
	            else if(getFileExtension(filePath)==2)
	            	 dataFileReader = new CsvFileReader(filePath,m_PredictorModel);
	            else
	            	throw new IOException("Extension Error");
	            dataFileReader.SetValueHandler(new BasicValueHandler());

	            dataFileReader.ReadHeader();
	           
	            //dataFileReader.setSelectedAttributes(attr);
	            dataFileReader.SetClassIndex(m_ClassAttribIndex);
	            dataFileReader.CreateDataSet();
	            dataFileReader.LoadData();

	            m_Data = dataFileReader.GetData();
	            m_Data.Compact();
	            System .out.println("size       "+m_Data.Size());
	            //m_Data.Normalize();
	            
	            //get class name
	            m_className=m_Data.Attribute(m_ClassAttribIndex).Name();
	            m_Data.SetClassProperties();

	            m_Attributes = m_Data.Attributes();
	            dataFileReader = null;
	           
	        }
	        catch (Exception e)
	        {
	            System.out.println(e.getMessage());
	        }
	        return m_Data;
	        
	    }
	    
	/**
	 * method to read test data set and return the attribute values in 2-D double array
	 * @param testfile
	 * @return return 2-D double array of attribute values
	 * @throws IOException
	 */
	    public double[][] ReadTestData(String testfile)throws IOException{
	        if (testfile.length()==0) throw new IOException("Missing file name");
	        
	        Instances testdata;
	        DataFileReader dataFileReader;
            if(getFileExtension(testfile)==1)
            dataFileReader = new ArffFileReader(testfile);
            else if(getFileExtension(testfile)==2)
            	 dataFileReader = new CsvFileReader(testfile,m_PredictorModel);
            else
            	throw new IOException("Extension Error");
	        dataFileReader.SetValueHandler(new BasicValueHandler());
	        
	        dataFileReader.ReadHeader();
	        dataFileReader.SetClassIndex(m_ClassAttribIndex);
	    	
	    	//dataFileReader.setSelectedAttributes(attr);
	    	
	    	dataFileReader.CreateDataSet();
	     	dataFileReader.LoadData();
	    	
	    	testdata = dataFileReader.GetData();
	    	testdata.Compact();
	    	System .out.println("size       "+testdata.Size());
	    	//m_Data.Normalize();
	    	
	    	//m_Data.SetClassProperties();
	    	
	    	//m_Attributes = testdata.Attributes();
	    	dataFileReader = null;
	        return testdata.DataSet();
	    }
	    
	    /**
	     * method to set the attribute index of class value(class need to predict)
	     * @param classAttibuteIndex
	     */
	    public void setClassAttibuteIndex(int classAttibuteIndex){
	    	m_ClassAttribIndex=classAttibuteIndex;
	    }
	    /**
	     * method to get the class name of the attribute name need to predict
	     * @return String name of class attribute
	     */
	    public String getClassName(){
	    	return m_className;
	    }
	    /**
	     * method to get the set of attributes
	     * @return FastVector of set of attributes
	     */
	    public FastVector getAttibutes(){
	    	return m_Attributes;
	    }
	

}
