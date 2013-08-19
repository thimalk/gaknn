package gaknn.dataaccess;

import gaknn.core.Attribute;
import gaknn.core.FastVector;
import gaknn.core.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

public class CsvFileReader extends DataFileReader {
	// file extention
	 public static String FILE_EXTENSION = "csv";
	 StreamTokenizer m_Tokenizer;
	public CsvFileReader (String sDataFile){
				// get the csv file path
		        this.m_filePath = sDataFile;
		    
	}
	// initializing  the streamTokenizer
	 protected void initTokenizer(){
	        m_Tokenizer.resetSyntax();         
	        m_Tokenizer.whitespaceChars(0, ' ');    
	        m_Tokenizer.wordChars(' '+1,'\u00FF');
	        m_Tokenizer.whitespaceChars(',',',');
	       
	        m_Tokenizer.eolIsSignificant(true);
	    }
	 protected void getFirstToken() throws IOException 
	    {
	        while (m_Tokenizer.nextToken() == StreamTokenizer.TT_EOL) {};
	        if(m_Tokenizer.ttype == ','){
	        	m_Tokenizer.nextToken();
	        	if(m_Tokenizer.ttype==','){
	        		m_Tokenizer.ttype='?';
	        	}
	        }
	        else if ((m_Tokenizer.ttype == '\'') ||(m_Tokenizer.ttype == '"')) 
	            m_Tokenizer.ttype = StreamTokenizer.TT_WORD;
	        else if ((m_Tokenizer.ttype == StreamTokenizer.TT_WORD) &&
	                        (m_Tokenizer.sval.equals("?")))
	            m_Tokenizer.ttype = '?';
	    }
	    
	    protected void getNextToken() throws IOException {
//	        if (m_Tokenizer.nextToken() == StreamTokenizer.TT_EOL) 
//	        {
//	             errorMessage("premature end of file");
//	        }
	        if (m_Tokenizer.nextToken() == StreamTokenizer.TT_EOF) 
	        {
	            errorMessage("premature end of file");
	        } 
	        if(m_Tokenizer.ttype == ','){
	        	m_Tokenizer.nextToken();
	        	if(m_Tokenizer.ttype==','){
	        		m_Tokenizer.ttype='?';
	        	}
	        }
	        	
	        else if ((m_Tokenizer.ttype == '\'') ||
	               (m_Tokenizer.ttype == '"')) 
	        {
	            m_Tokenizer.ttype = StreamTokenizer.TT_WORD;
	        } 
	        else if ((m_Tokenizer.ttype == StreamTokenizer.TT_WORD) &&
	               (m_Tokenizer.sval.equals("?")))
	        {
	            m_Tokenizer.ttype = '?';
	        }
	    }
	    
	    private void errorMessage(String msg) throws IOException 
	    {
	        String str = msg + ", read " + m_Tokenizer.toString();
	        if (m_Lines > 0) 
	        {
	            int line = Integer.parseInt(str.replaceAll(".* line ", ""));
	            str = str.replaceAll(" line .*", " line " + (m_Lines + line - 1));
	        }
	        throw new IOException(str);
	    }
	    
	    protected void getLastToken(boolean endOfFileOk) throws IOException 
	    {
	        if ((m_Tokenizer.nextToken() != StreamTokenizer.TT_EOL) &&
	            ((m_Tokenizer.ttype != StreamTokenizer.TT_EOF) || !endOfFileOk))
	        {
	            errorMessage("end of line expected");
	        }
	    }
	    
	    protected void readTillEOL() throws IOException {
	        while (m_Tokenizer.nextToken() != StreamTokenizer.TT_EOL) {};
	        m_Tokenizer.pushBack();
	    }
    public void ReadHeader() throws IOException {
        
        if (m_filePath.length() == 0){
            throw new IOException("Missing File");
            
        }
        else
        {
            FileReader fr = new FileReader(m_filePath); 
            BufferedReader br = new BufferedReader(fr);

             m_Tokenizer = new StreamTokenizer(br);
            initTokenizer();
            m_Lines = 0;
            
            if (this.m_filePath != null)
                m_RelationName = (this.m_filePath)
                    .replaceAll("\\.[cC][sS][vV]$", "");
            
           
            //Create vectors to hold information temporarily.
            FastVector attributes = new FastVector();
            int numAttributes=0;
            String attributeName;
            
            
            do{
            	getNextToken();
            	if(m_Tokenizer.lineno()>1||m_Tokenizer.ttype==StreamTokenizer.TT_EOL)
            		break;
            	attributeName=m_Tokenizer.sval;
            	numAttributes++;
            	attributes.addElement(new Attribute(attributeName, attributes.size(), numAttributes));
            	
            	
            }while(m_Tokenizer.lineno()==1);

              // Check if any attributes have been declared.
            if (attributes.size() == 0) {
                errorMessage("no attributes declared");
            }
            m_Attributes = attributes;
		}
	}

	@Override
	protected Instance ReadInstance() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
