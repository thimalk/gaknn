package gaknn.dataaccess;
import gaknn.core.Instances;
import gaknn.core.Instance;
import gaknn.core.FastVector;
import gaknn.core.Attribute;
import java.io.IOException;

public class DataFileWriter {
	 /** the actual data */
    protected Pairs [] predictions;
    
    protected String m_filePath;
    
    public String m_RelationName;
    
    public FastVector m_Attributes;
    
    public void getAttributes(FastVector a_attributes){
    	m_Attributes=a_attributes;
    }


}
