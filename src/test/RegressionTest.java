package test;

import static org.junit.Assert.*;

import java.io.IOException;

import gaknn.core.FastVector;
import gaknn.core.Instance;
import gaknn.core.Instances;
import gaknn.dataaccess.ArffFileReader;
import gaknn.dataaccess.ParameterReader;
import gaknn.datapreprocess.BasicValueHandler;
import gaknn.predictor.Predictor;
import gaknn.predictor.Predictor1;
import gaknn.predictor.PredictorRegression;
import gaknn.similarity.AbstractSimilarity;
import gaknn.similarity.BasicSimilarity;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RegressionTest {
	static Instances m_Data;
	static int m_ClassAttribIndex = 0;
	static FastVector m_Attributes;
	private static Instance[] m_TrainingSet;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testPredictInstance() {
		 try {
			ReadData("abalone_regression.arff");
		
	        int recNo = m_Data.Size();
	        m_TrainingSet=new Instance[recNo];
	        for (int i=0; i<recNo; i++){
	            m_TrainingSet[i] = new Instance(m_Data.DataSet()[i]);
	            m_TrainingSet[i].SetClassIndex(m_Data.ClassIdList()[i]);
	        } 
	        
	        String[] ClassArray = m_Data.ClassArray();
	        AbstractSimilarity simMeas = new BasicSimilarity(m_Attributes);
	       
	        double[] attVal={0.160,0.08,0.032,0.02,0.010,0.019,6};
			
	        ParameterReader paramReader = new ParameterReader(m_Attributes, "abalone_training.prm");
	       
	        double[] m_Weights = paramReader.ReadWeights();
	        int k = paramReader.ReadK();
	        simMeas.SetWeights(m_Weights);
	        
	        Predictor predictor = new PredictorRegression(simMeas, m_TrainingSet);
	        System.out.println("fff");
	        predictor.setClassList(ClassArray);
	        predictor.setK(k);
	        
	        assertEquals("expected confidence", predictor.Predict(attVal).Value(),1-(.2-.195)/.2,.1);
	        assertEquals("expected value", predictor.Predict(attVal).indexValue(),.195,.1);
		 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		
	}

	@Test
	public void testPredictDoubleArray() {
		
		try {
			ReadData("abalone_regression.arff");
		
	        int recNo = m_Data.Size();
	        m_TrainingSet=new Instance[recNo];
	        for (int i=0; i<recNo; i++){
	            m_TrainingSet[i] = new Instance(m_Data.DataSet()[i]);
	            m_TrainingSet[i].SetClassIndex(m_Data.ClassIdList()[i]);
	        } 
	        
	        String[] ClassArray = m_Data.ClassArray();
	        AbstractSimilarity simMeas = new BasicSimilarity(m_Attributes);
	       
	        double[] attVal={0.160,0.08,0.032,0.02,0.010,0.019,6};
			Instance instance =new Instance(attVal);
			instance.SetClassIndex(.200);
	        ParameterReader paramReader = new ParameterReader(m_Attributes, "abalone_training.prm");
	       
	        double[] m_Weights = paramReader.ReadWeights();
	        int k = paramReader.ReadK();
	        simMeas.SetWeights(m_Weights);
	        
	        Predictor predictor = new PredictorRegression(simMeas, m_TrainingSet);
	        System.out.println("fff");
	        predictor.setClassList(ClassArray);
	        predictor.setK(k);
	        System.out.println(predictor.Predict(instance));
	        assertEquals("expected", predictor.Predict(instance),1-(.2-.195)/.2,.1);
		 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
            
            m_Data.SetClassProperties();

            m_Attributes = m_Data.Attributes();
            dataFileReader = null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        
    }

}
