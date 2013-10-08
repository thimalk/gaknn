package gaknn;

import java.io.IOException;

import gaknn.core.Instances;
import gaknn.core.Pair;
import gaknn.dataaccess.ArffFileReader;
import gaknn.dataaccess.DataAccesser;
import gaknn.dataaccess.DataFileWriter;
import gaknn.dataaccess.ParameterWriter;
import gaknn.predictor.KNNPredictor;


/**
 * class genetic algorithm optimized k nearest neighbor algorithm
 * need to supply arguments
 *  
 * data file path 
 *  -datafile "path"
 * test file path
 * -testfile "path"
 * 
 * 
 * method to get the intitial arguments for the predictor
 * genetic algorithm need 
 * population  default value 20
 * -population "value"
 * number of evaluations  default value 1
 * -eval "value"
 * mutation default value 100
 * -mutation "value"
 * '
 * knn algorithm need
 * 
 * k value default value 5
 * -k "value"
 * parameter file which have the initial weights values default get the datafile +".prm"
 * -parm "path"
 * 
 *
 * @author Thimal
 *
 */
public class Gaknn {
	 /** The keyword used to denote the classifier predictor method */
    public final static String CLASSIFIER_M= "c";
    /** The keyword used to denote the regression predictor method */
    public final static String REGRESSION_M= "r";
    
	private static int m_classAttribIndex=0;
	static DataAccesser da;
	static KNNPredictor knn;
	
	
	private static String m_DataFilePath="train.csv";;
	private static String m_task="o";
	private static String m_TestFilePath="abalone_test.arff";;
	private static String m_className;
	private static Instances m_Data;
	private static String m_PredictorModel=CLASSIFIER_M;
	
	 
	/** Print a "usage message" that described possible command line options, 
     *  then exit.
     * @param message a specific error message to preface the usage message by.
     */
      protected static void Usage(String message)
      {
          System.err.println();
          System.err.println(message);
          System.err.println();
          System.err.println(
                );
      
      }
      
	 public static void ParseArguments(String[] argv){
	        int len = argv.length;
	        
	        /* parse the options */
	        for (int i=0; i<len; i++)
		{
	            /* try to get the various options */
	            if (argv[i].equals("-task"))
	            {
	                if (++i >= len || argv[i].startsWith("-")) 
	                    Usage("-task must have a value 'o' or 'p'");
	                   /* task */
	                    m_task = argv[i];
		    }
	            else if (argv[i].equals("-datafile")){
	                if (++i >= len || argv[i].startsWith("-")) 
	                    Usage("-datafile must have a valid data file");
	                    /* task */
	                    m_DataFilePath = argv[i];
	            }
	            else if (argv[i].equals("-testfile")){
	                if (++i >= len || argv[i].startsWith("-")) 
	                    Usage("-testfile must have a valid data file");
	                    /* task */
	                    m_TestFilePath = argv[i];
	            }
	            else if (argv[i].equals("-clsindex")){
	                if (++i >= len || argv[i].startsWith("-")) 
	                    Usage("-clsattrib must have the attribute index of the class attribute");
	                    /* task */
	                    m_classAttribIndex = Integer.parseInt(argv[i]);
	            }
	           
	           
	            
	          //initial weight values as xml file
	            else if (argv[i].equals("-model")){
	                if (++i >= len || argv[i].startsWith("-")) 
	                    Usage("-model must have a valid parameter file");
	                    /* task */
	                    m_PredictorModel = argv[i];
	            } 
	        }
	    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		knn=new KNNPredictor();
		knn.ParseArguments(args);
		ParseArguments(args);
		m_classAttribIndex=knn.getclassAttribIndex();
		m_PredictorModel=knn.getPredictorModel();
		da=new DataAccesser(m_classAttribIndex,m_PredictorModel);
		try {
			m_Data=da.ReadData(m_DataFilePath);
			knn.setData(m_Data);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
         
		try {
			if (m_task.equals("o"))
	         {
				knn.setTask(m_task);
				
				
			
			
			knn.setDataAccesser(da);
			System.out.println(m_Data.Size());
			
			
			knn.createTrainingdataSets();
			knn.runOptimization();
	         }else{
	        	 
	        	 m_Data=da.ReadData(m_DataFilePath);
	        	 
	        	 knn.setTask("p");
	        	
	                System.out.println("Read test data");
	                double[][] testSet = da.ReadTestData(m_TestFilePath);
	                long startTime = System.nanoTime();
	                Pair[] predictions = knn.PredictInstances(testSet);
	                long endTime = System.nanoTime();

	                long duration1 = endTime - startTime;
	                
	              
	                DataFileWriter dfr=new DataFileWriter(m_className, m_Data.ClassArray());
	                dfr.writeARFFOuput("abalone",predictions);
	                
	                knn.setPredictorType("kdtree");
	                startTime = System.nanoTime();
	                
	               predictions = knn.PredictInstances(testSet);
	                endTime = System.nanoTime();

	                long duration2 = endTime - startTime;
	                System.out.println("Duration ad hoc = "+duration1/100000+" ms");                
	                System.out.println("Duration kdtree = "+duration2/100000+" ms");
	                dfr.writeARFFOuput("abalonekdtree",predictions); 
	        	 
	         }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub

	}
	
	

}
