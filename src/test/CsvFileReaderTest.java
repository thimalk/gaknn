package test;

import static org.junit.Assert.*;

import java.io.IOException;

import gaknn.Gaknn;
import gaknn.core.Attribute;
import gaknn.core.FastVector;
import gaknn.core.Instance;
import gaknn.dataaccess.CsvFileReader;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CsvFileReaderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	

	@Test
	public void testReadHeader() throws IOException {
		CsvFileReader csv =new CsvFileReader("abalone_t.csv",Gaknn.CLASSIFIER_M);
		String head[]={"class",	"Length",	"Diameter",	"Height",	"Whole_Weight" ,	"Shucked_weight",	"Viscera_weight",	"Shell_weight",	"Rings"};

		csv.ReadHeader();
		for(int i=0;i<head.length;i++){
			Attribute a=(Attribute)csv.m_Attributes.elementAt(i);
			assertEquals("expected", head[i],a.Name() );
			
		}
		
		//fail("Not yet implemented");
	}
	
	
}
