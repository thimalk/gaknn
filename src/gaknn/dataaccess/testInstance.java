package gaknn.dataaccess;

import static org.junit.Assert.*;

import java.io.IOException;

import gaknn.core.Attribute;
import gaknn.core.Instance;
import gaknn.datapreprocess.BasicValueHandler;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.ArrayComparisonFailure;

public class testInstance {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}
	@Test
	public void testReadHeader() throws IOException {
		CsvFileReader csv =new CsvFileReader("iris.csv");
		String head[]={"At1","At2","At3","At4","At5"};
		csv.ReadHeader();
		for(int i=0;i<head.length;i++){
			Attribute a=(Attribute)csv.m_Attributes.elementAt(i);
			assertEquals("expected", head[i],a.Name() );
			
		}
		
		//fail("Not yet implemented");
	}
	@Test
	public void test() throws ArrayComparisonFailure, IOException {
		double[] instanceValues={5.1,3.5,1.4,0.2};
		double delta =0.2;
		CsvFileReader csv =new CsvFileReader("iris.csv");
		BasicValueHandler valHandler=new BasicValueHandler();
		 //csv.SetValueHandler(new BasicValueHandler());
		//Instance inst = new Instance(instanceValues);
		csv.ReadHeader();
		csv.SetClassIndex(4);
		csv.CreateDataSet();
		valHandler.SetAttributes(csv.m_Data.Attributes());
		csv.SetValueHandler(valHandler);
		//csv.ReadInstance();
		assertArrayEquals("exepected", instanceValues, csv.ReadInstance().GetElements(),delta);
		//fail("Not yet implemented");
	}

}
