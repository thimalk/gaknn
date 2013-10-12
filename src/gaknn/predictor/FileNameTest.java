package gaknn.predictor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileNameTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetFilePath() {
		KNNPredictor knn=new KNNPredictor();
		String filePath="c:\\gaknn\\abalone_training.arff";
		System.out.println(filePath);
		System.out.println( knn.getExtensionRemovedPath(filePath));
		assertEquals("expected", "c:\\gaknn\\abalone_training", knn.getExtensionRemovedPath(filePath));
	}

}
