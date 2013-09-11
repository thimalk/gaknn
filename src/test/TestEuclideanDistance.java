package test;

import static org.junit.Assert.*;

import java.io.IOException;

import gaknn.core.FastVector;
import gaknn.core.Instance;
import gaknn.core.Instances;
import gaknn.core.kdtree.EuclideanDistance;
import gaknn.dataaccess.ArffFileReader;
import gaknn.dataaccess.ParameterReader;
import gaknn.datapreprocess.BasicValueHandler;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestEuclideanDistance {
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
	public void testNormalDistance() {
		
		EuclideanDistance eD=new EuclideanDistance();
		try {
			ReadData("abalone_t.arff");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 ParameterReader paramReader = new ParameterReader(m_Attributes, "abalone_training.prm");
		    double []m_Weights = paramReader.ReadWeights();
		eD.setInstances(m_Data);
		eD.SetWeights(m_Weights);
		double[] fAttval={0.535,0.42,0.145,0.926,0.398,0.1965,0.25,17};
		Instance first=new Instance(fAttval);
		
		double[] sAttval={0.47,0.355,0.14,0.433,0.1525,0.095,0.152,12};
		Instance second=new Instance(sAttval);
		double expected=0;
		for(int i=0;i<fAttval.length;i++){
		double diff=(fAttval[i]-sAttval[i]);
		diff=diff*m_Weights[i];
		expected+=diff*diff;
		}
		expected=Math.sqrt(expected);
		assertEquals(expected, eD.distance(first, second),.01);
		//fail("Not yet implemented");
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
