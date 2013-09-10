package gaknn.dataaccess;
import gaknn.core.Instance;
import gaknn.core.FastVector;
import gaknn.core.Attribute;
import gaknn.core.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * Cass to write the predicted values to csv file or arff file
 * @author Thimal Kempitiya
 */
public class DataFileWriter {

    protected String m_filePath;
    
    public String m_RelationName;
    public static String PERCENTAGE="percentage";
    
    /** The keyword used to denote the start of an arff header */
    public final static String ARFF_RELATION = "@relation";

    /** The keyword used to denote the start of the arff data section */
    public final static String ARFF_DATA = "@data";
    
    /** The keyword used to denote the start of an arff attribute declaration */
    public final static String ARFF_ATTRIBUTE = "@attribute";

    /** A keyword used to denote a numeric attribute */
    public final static String ARFF_ATTRIBUTE_INTEGER = "integer";

    /** A keyword used to denote a numeric attribute */
    public final static String ARFF_ATTRIBUTE_REAL = "real";

    /** A keyword used to denote a numeric attribute */
    public final static String ARFF_ATTRIBUTE_NUMERIC = "numeric";

    /** The keyword used to denote a string attribute */
    public final static String ARFF_ATTRIBUTE_STRING = "string";

    /** The keyword used to denote a date attribute */
    public final static String ARFF_ATTRIBUTE_DATE = "date";
    protected String m_className;
    
    protected String[] m_classArray;
    protected Pair[] m_preictions; 
    
    
    public DataFileWriter(String className,String[]classArray,Pair[] predictions){
    	m_className=className;
    	m_preictions=predictions;
    	m_classArray=classArray;
    	
    }
    /** write the output to a csv format
     * 
     * @param String file name. */
    public void writeCSVOutput(String filename){
    	File file = new File(filename+".csv");
    	 try{
		file.createNewFile();

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		String header=m_className+","+PERCENTAGE;
		
			bw.write(header);
			bw.newLine();
			for(int i=0;i<m_preictions.length;i++){
				bw.write(m_classArray[m_preictions[i].Index()]+","+m_preictions[i].Value());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    /** write the output into a arff file
     * @param String file name. */
  public void writeARFFOuput(String filename){
	  File file = new File(filename+".arff");
 	 try{
		file.createNewFile();

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		PrintWriter pw=new PrintWriter(fw);
		BufferedWriter bw = new BufferedWriter(fw);
		
		//bw.write(ARFF_RELATION+" "+filename+" output\n");
		pw.println(ARFF_RELATION+" "+filename+" output");
		//bw.newLine();
		//bw.flush();
		pw.flush();
		//bw.write(ARFF_ATTRIBUTE+" "+m_className+" {");
		pw.print(ARFF_ATTRIBUTE+" "+m_className+" {");
		for(int i=0;i<m_classArray.length-1;i++){
			//bw.write(m_classArray[i]+",");
			pw.print(m_classArray[i]+",");
		}
		//bw.write(m_classArray[m_classArray.length-1]+"}\n");
		pw.println(m_classArray[m_classArray.length-1]+"}");
		//bw.newLine();
		bw.flush();
		//bw.write(ARFF_ATTRIBUTE+" "+PERCENTAGE+" "+ARFF_ATTRIBUTE_NUMERIC+"\n");
		pw.println(ARFF_ATTRIBUTE+" "+PERCENTAGE+" "+ARFF_ATTRIBUTE_NUMERIC);
		//bw.newLine();
		bw.flush();
		//bw.write(ARFF_DATA+"\n");
		pw.println(ARFF_DATA);
		//bw.newLine();
		//bw.flush();
		
			for(int i=0;i<m_preictions.length;i++){
				//bw.write(m_classArray[m_preictions[i].Index()]+","+m_preictions[i].Value()+"\n");
				pw.println(m_classArray[m_preictions[i].Index()]+","+m_preictions[i].Value());
				//bw.newLine();
				bw.flush();
			}
			bw.close();
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
   


}
