package test;

import static org.junit.Assert.*;
import gaknn.predictor.KNNPredictor;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class KNNPredictorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetfileExtenstion() {
		KNNPredictor knn=new KNNPredictor();
		String filePath="c:\\gheee\\dfke\\dfgg.arff";
		 int n=knn.getFileExtension(filePath);
		 System.out.println(n);
		filePath="c:\\gheee\\dfke\\dfgg.arff";
		 n=knn.getFileExtension(filePath);
		 assertEquals("expected", 1, n);
		 filePath="c:\\gheee\\dfke\\dfgg.csv";
		 n=knn.getFileExtension(filePath);
		 assertEquals("expected",2,n);
	}

}
