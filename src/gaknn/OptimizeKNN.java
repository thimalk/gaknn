/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn;

import gaknn.core.Instance;
import gaknn.core.FastVector;
import gaknn.core.Instances;
import gaknn.core.InvalidClassIndexException;
import gaknn.core.Pair;
import gaknn.dataaccess.ArffFileReader;
import gaknn.dataaccess.ParameterReader;
import gaknn.dataaccess.ParameterWriter;
import gaknn.datapreprocess.BasicValueHandler;
import gaknn.evaluator.Evaluator;
import gaknn.evaluator.SimpleWeightEvaluator;
import gaknn.predictor.Predictor;
import gaknn.predictor.Predictor1;
import gaknn.similarity.*;


import java.io.IOException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.DoubleGene;
import org.jgap.impl.MutationOperator; 
import org.jgap.*;

/**
 *
 * @author Niro
 */
public class OptimizeKNN {
    
    private static int m_NumEvolutions = 1;
    private static int m_Population = 20;
    private static int m_Mutation = 100;
    private static double m_MinDoubleGeneVal= 0.0;
    private static double m_MaxDoubleGeneVal = 10.0;
    private static int m_MaxIntGeneVal = 10;
    
    
    private static String m_DataFilePath = "";
    private static String m_TestFilePath  = "";
    private static Instance[] m_TrainingSet;
    private static Instance[] m_TestSet;
    private static FastVector m_Attributes;
    private static Instances m_Data;
    
    // to get the class name
    private static String m_className;
    
    private static int k = 5;
    
    private static Instances m_TData;
    private static int genNo;
    private static boolean m_FindK = true;
    private static int m_ClassAttribIndex = -1;
    private static String m_ParameterFile;
    private static double[] m_Weights;
    private static String m_task = "p";
    
    public static void runOptimization()throws Exception{
        
        //ReadData(m_DataFilePath);
        String[] ClassArray = m_Data.ClassArray();
        
        Configuration conf = new DefaultConfiguration();
    	MutationOperator muteOp = new MutationOperator(conf, m_Mutation); 
    	conf.addGeneticOperator(muteOp);
        
        AbstractSimilarity simMeas = new BasicSimilarity(m_Attributes);
        
       
        Predictor predictor = new Predictor1(simMeas, m_TrainingSet);
        predictor.setClassList(ClassArray);
        Evaluator evaluator = new SimpleWeightEvaluator(predictor, m_TestSet);
    	
    	EvaluatePredictions fitFunc = new EvaluatePredictions(simMeas, predictor, evaluator);
    	//fitFunc.SetK(k);
        fitFunc.FindK(m_FindK);
        conf.setFitnessFunction(fitFunc);
        
        int numAttributes = m_Data.NumAttributes();
        Gene[] sampleGenes;
        
        
        if (m_FindK)
            sampleGenes = new Gene[numAttributes + 1];
        else
            sampleGenes = new Gene[numAttributes];
        
        int g;
        for (g=0; g<numAttributes; g++) sampleGenes[g] = new DoubleGene(conf, m_MinDoubleGeneVal, m_MaxDoubleGeneVal);
        
        sampleGenes[g] = new IntegerGene(conf,1,m_MaxIntGeneVal);
        
        IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sampleChromosome);
        conf.setPopulationSize(m_Population);
        Genotype population = Genotype.randomInitialGenotype( conf );
        
        IChromosome bestSolutionSoFarGlobal = sampleChromosome;
        double bestFitnessSoFarGlobal = 0.0;
        double[] weights = new double[numAttributes];
        
        for( int i = 0; i < m_NumEvolutions; i++ )
        { 
            genNo = 0;
            population.evolve();
            IChromosome bestSolutionSoFar = population.getFittestChromosome();
            
            double fit = 0.0;
            fit = bestSolutionSoFar.getFitnessValue() ;
            
            //System.out.println("gen#"+i+" Fit "+bestSolutionSoFar.getFitnessValue());
            g=0;
            while (g < numAttributes) {
            	weights[g] = (Double) bestSolutionSoFar.getGene( g ).getAllele();
            	
            	System.out.println("Weight value W" + g + ": " + weights[g]);
            	g++;
            }
            
            if (m_FindK)
            {
                k = (Integer) bestSolutionSoFar.getGene( g ).getAllele();
                System.out.println(" Value k: " + k);
            }
            

            if ( fit > bestFitnessSoFarGlobal)
            {
            	bestFitnessSoFarGlobal=fit;	
            	bestSolutionSoFarGlobal = (IChromosome) bestSolutionSoFar.clone();
            }
        }
        
        g=0;
        while (g < numAttributes) {
            weights[g] = (Double) bestSolutionSoFarGlobal.getGene( g ).getAllele();
            System.out.println("Weight value W" + g + ": " + weights[g]);
            g++;
        }
        
        if (m_FindK){
            k = (Integer) bestSolutionSoFarGlobal.getGene( g ).getAllele();
            System.out.println(" Value k: " + k);
        }
        
        String parameterFileName = m_DataFilePath;
        parameterFileName.replaceFirst(ParameterWriter.FILE_EXTENSION,ArffFileReader.FILE_EXTENSION);
        int i = parameterFileName.lastIndexOf(ArffFileReader.FILE_EXTENSION);
        parameterFileName = parameterFileName.substring(0, i).concat(ParameterWriter.FILE_EXTENSION);
        //parameterFileName.replaceAll(ArffFileReader.FILE_EXTENSION,ParameterWriter.FILE_EXTENSION);
        ParameterWriter pWriter = new ParameterWriter(m_Attributes, parameterFileName);
        pWriter.Write(weights, k);
        pWriter = null;
    }
    
    private static void ReadData(String filePath) throws IOException{
        try
        {
            if (filePath.length()==0) throw new IOException("Missing file name");

            ArffFileReader dataFileReader = new ArffFileReader(filePath);
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
        
    }
    
    private static double[][] ReadTestData(String testfile)throws IOException{
        if (testfile.length()==0) throw new IOException("Missing file name");
        
        Instances testdata;
        ArffFileReader dataFileReader = new ArffFileReader(testfile);
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
    
    private Pair PredictInstance(double[] instance) throws IOException{
        ReadData(m_DataFilePath);
        int recNo = m_Data.Size();
        
        for (int i=0; i<recNo; i++){
            m_TrainingSet[i] = new Instance(m_Data.DataSet()[i]);
            m_TrainingSet[i].SetClassIndex(m_Data.ClassIdList()[i]);
        }
        
        String[] ClassArray = m_Data.ClassArray();
        AbstractSimilarity simMeas = new BasicSimilarity(m_Attributes);
        
        ParameterReader paramReader = new ParameterReader(m_Attributes, m_ParameterFile);
        m_Weights = paramReader.ReadWeights();
        k = paramReader.ReadK();
        simMeas.SetWeights(m_Weights);
                
        Predictor predictor = new Predictor1(simMeas, m_TrainingSet);
        predictor.setClassList(ClassArray);
        predictor.setK(k);
        

        return (predictor.Predict(instance));
    }
    
    private static  Pair[] PredictInstances(double[][] instances)throws IOException{
        
        Pair[] predictions = new Pair[instances.length];
        int recNo = m_Data.Size();
        m_TrainingSet = new Instance[recNo];
        
        //ReadData(m_DataFilePath);
        
        
        for (int i=0; i<recNo; i++){
            m_TrainingSet[i] = new Instance(m_Data.DataSet()[i]);
            m_TrainingSet[i].SetClassIndex(m_Data.ClassIdList()[i]);
        }
        
        String[] ClassArray = m_Data.ClassArray();
        AbstractSimilarity simMeas = new BasicSimilarity(m_Attributes);
       
        ParameterReader paramReader = new ParameterReader(m_Attributes, m_ParameterFile);
        m_Weights = paramReader.ReadWeights();
        k = paramReader.ReadK();
        simMeas.SetWeights(m_Weights);
                
        Predictor predictor = new Predictor1(simMeas, m_TrainingSet);
        predictor.setClassList(ClassArray);
        predictor.setK(k);

        for (int i=0; i < instances.length; i++){
            predictions[i] = predictor.Predict(instances[i]);
        }
        return predictions;
    }
    
    
    private static void createTrainingdataSets(){
    	int DataSize = m_Data.Size();
    	int TrainSize = (DataSize/3) * 2 + (DataSize % 3);
    	int TestSize = DataSize/3;
  	
    	m_TrainingSet = new Instance[TrainSize];
    	m_TestSet = new Instance[TestSize];
    	//System.out.println(m_TestSet[0].length);
    	
        int trIndex = 0;
	int tsIndex = 0;
		
        for (int i=1; i<=DataSize; i++){
            if ((i % 3)== 0 ){
                //System.out.println(m_TestSet[tsIndex].length);
                m_TestSet[tsIndex] = new Instance(m_Data.DataSet()[i-1]);
                m_TestSet[tsIndex].SetClassIndex(m_Data.ClassIdList()[i-1]);
                tsIndex++;			
            }
            else {
                //System.out.println(m_TestSet[tsIndex].length);
                m_TrainingSet[trIndex] = new Instance(m_Data.DataSet()[i-1]);
                m_TrainingSet[trIndex].SetClassIndex(m_Data.ClassIdList()[i-1]);
                trIndex++;			
            }
        }
   }
    
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
    
    protected static void ParseArguments(String[] argv){
        int len = argv.length;
        int i;
 
        /* parse the options */
        for (i=0; i<len; i++)
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
                    m_ClassAttribIndex = Integer.parseInt(argv[i]);
            }
            else if (argv[i].equals("-population")){
                if (++i >= len || argv[i].startsWith("-")) 
                    Usage("-population must have a valid integer value");
                    /* task */
                    m_Population = Integer.parseInt(argv[i]);
            }
            else if (argv[i].equals("-mutation")){
                if (++i >= len || argv[i].startsWith("-")) 
                    Usage("-mutation must have a valid integer value");
                    /* task */
                    m_Mutation = Integer.parseInt(argv[i]);
            }
            else if (argv[i].equals("-k")){
                if (++i >= len || argv[i].startsWith("-")) 
                    Usage("-k must have a valid integer value");
                    /* task */
                {
                    k = Integer.parseInt(argv[i]);
                    if (k<1)
                        m_FindK = false;
                }
            }
            else if (argv[i].equals("-params")){
                if (++i >= len || argv[i].startsWith("-")) 
                    Usage("-prams must have a valid parameter file");
                    /* task */
                    m_ParameterFile = argv[i];
            } 
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        ParseArguments(args);
        
        try 
        {
        	//o :optimizing the k value and the weight values
        	//t :training data
            if (m_task.equals("o"))
            {
                ReadData(m_DataFilePath);
                createTrainingdataSets();
                runOptimization();
            }
            else
            {
                ReadData(m_DataFilePath);
                double[][] testSet = ReadTestData(m_TestFilePath);
                Pair[] predictions = PredictInstances(testSet);

            }
        }
        catch (Exception e)
        {
            //e.getMessage();
            System.out.println(e.getMessage());
            e.printStackTrace();
             
        }
    
    }
       
}
