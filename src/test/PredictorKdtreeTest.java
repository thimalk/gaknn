package test;

import static org.junit.Assert.*;
import gaknn.core.FastVector;
import gaknn.core.Instance;
import gaknn.core.Instances;
import gaknn.core.Pair;
import gaknn.dataaccess.ArffFileReader;
import gaknn.dataaccess.ParameterReader;
import gaknn.datapreprocess.BasicValueHandler;
import gaknn.predictor.Predictor;
import gaknn.predictor.Predictor1;
import gaknn.predictor.PredictorKdtree;
import gaknn.similarity.AbstractSimilarity;
import gaknn.similarity.BasicSimilarity;

import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PredictorKdtreeTest {
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
	public void testPredict1() {
		Predictor kdtreeP;
		try {
			ReadData("abalone_t.arff");
			int recNo = m_Data.Size();
	        m_TrainingSet=new Instance[10];
	        for (int i=0; i<recNo; i++){
	        
	            m_TrainingSet[i] = new Instance(m_Data.DataSet()[i]);
	            m_TrainingSet[i].SetClassIndex(m_Data.ClassIdList()[i]);
	        	
	        }
	        
			
			 ParameterReader paramReader = new ParameterReader(m_Attributes, "abalone_training.prm");
		    double []m_Weights = paramReader.ReadWeights();
		    double[] attVal={0.200,0.160,0.08,0.032,0.02,0.010,0.019,6};
			Instance instance =new Instance(attVal);
			 String[] ClassArray = m_Data.ClassArray();
			int k = paramReader.ReadK();
			 AbstractSimilarity simMeas = new BasicSimilarity(m_Attributes);
			 simMeas.SetWeights(m_Weights);
			kdtreeP=new PredictorKdtree(m_Data,simMeas); 
			 kdtreeP.setClassList(ClassArray);
		        kdtreeP.setK(3);
			kdtreeP.Predict(instance);
			assertEquals("expected", kdtreeP.Predict(instance), .660, .01);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//fail("Not yet implemented");
	}
	@Test
	public void testPredict2() {
		Predictor kdtreeP;
		try {
			ReadData("abalone_t.arff");
			int recNo = m_Data.Size();
	        m_TrainingSet=new Instance[10];
	        for (int i=0; i<recNo; i++){
	        
	            m_TrainingSet[i] = new Instance(m_Data.DataSet()[i]);
	            m_TrainingSet[i].SetClassIndex(m_Data.ClassIdList()[i]);
	        	
	        }
			
			 ParameterReader paramReader = new ParameterReader(m_Attributes, "abalone_training.prm");
		    double []m_Weights = paramReader.ReadWeights();
		    double[] attVal={0.200,0.160,0.08,0.032,0.02,0.010,0.019,6};
			Instance instance =new Instance(attVal);
			 String[] ClassArray = m_Data.ClassArray();
			int k = paramReader.ReadK();
			 AbstractSimilarity simMeas = new BasicSimilarity(m_Attributes);
			 simMeas.SetWeights(m_Weights);
			kdtreeP=new PredictorKdtree(m_Data,simMeas); 
			 kdtreeP.setClassList(ClassArray);
		        kdtreeP.setK(3);
			kdtreeP.Predict(instance);
			Pair pair =new Pair(0,.66);
			assertEquals("expected", kdtreeP.Predict(attVal).Value(),pair.Value(),.01);
			assertEquals("expected",kdtreeP.Predict(attVal).Index(), pair.Index());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    protected static Predictor CreatePredictor(String task,String type,AbstractSimilarity simMeas){
    	Predictor predictor=null;
    	if(type=="adhoc"){
    		if(task=="p"){
    			 int recNo = m_Data.Size();
    		        m_TrainingSet = new Instance[recNo];
    		        //ReadData(m_DataFilePath);
    		        
    		        
    		        for (int i=0; i<recNo; i++){
    		            m_TrainingSet[i] = new Instance(m_Data.DataSet()[i]);
    		            m_TrainingSet[i].SetClassIndex(m_Data.ClassIdList()[i]);
    		        }
    		}
    		predictor = new Predictor1(simMeas, m_TrainingSet);
    		
    	}
    	else if(type=="kdtree")
    		
    		if(task=="o"){
    			Instances inst=new Instances("traning", m_Attributes);
    			int recNo=m_TrainingSet.length;
    			for(int i=0;i<recNo;i++){
    				inst.AddElement(i, m_TrainingSet[i].GetElements(), m_TrainingSet[i].GetClassIndex());
    			}
    			predictor=new PredictorKdtree(inst, simMeas);
    			
    		}
    		else
    		predictor = new PredictorKdtree(m_Data, simMeas);
		return predictor;
    	
    }

	private static void ReadData(String filePath) throws IOException {
		try {
			if (filePath.length() == 0)
				throw new IOException("Missing file name");

			ArffFileReader dataFileReader = new ArffFileReader(filePath);
			dataFileReader.SetValueHandler(new BasicValueHandler());

			dataFileReader.ReadHeader();

			// dataFileReader.setSelectedAttributes(attr);
			dataFileReader.SetClassIndex(m_ClassAttribIndex);
			dataFileReader.CreateDataSet();

			dataFileReader.LoadData();

			m_Data = dataFileReader.GetData();
			m_Data.Compact();
			System.out.println("size       " + m_Data.Size());
			// m_Data.Normalize();

			// get class name
			// m_className=m_Data.Attribute(m_ClassAttribIndex).Name();
			m_Data.SetClassProperties();

			m_Attributes = m_Data.Attributes();
			dataFileReader = null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
