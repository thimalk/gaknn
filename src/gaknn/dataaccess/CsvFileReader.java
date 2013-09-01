package gaknn.dataaccess;

import gaknn.core.Attribute;
import gaknn.core.FastVector;
import gaknn.core.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvFileReader extends DataFileReader {
	// file extention
	public static String FILE_EXTENSION = "csv";

	/** Constant set for for missing values */
	public static char MISSING_VALUE = '?';

	

	public ArrayList<Boolean> getType = new ArrayList<Boolean>();
	StreamTokenizer m_Tokenizer;

	public CsvFileReader(String sDataFile) {
		// get the csv file path
		this.m_filePath = sDataFile;

	}

	// initializing the streamTokenizer
	protected void initTokenizer() {
		m_Tokenizer.resetSyntax();
		m_Tokenizer.whitespaceChars(0, ' ');
		m_Tokenizer.wordChars(' ' + 1, '\u00FF');
		m_Tokenizer.ordinaryChar(',');

		m_Tokenizer.eolIsSignificant(true);
	}

	protected void getFirstToken() throws IOException {
		while (m_Tokenizer.nextToken() == StreamTokenizer.TT_EOL) {
		}
		;
		if (m_Tokenizer.ttype == ',') {
			m_Tokenizer.nextToken();
			if (m_Tokenizer.ttype == ',') {
				m_Tokenizer.ttype = '?';
			}
		} else if ((m_Tokenizer.ttype == '\'') || (m_Tokenizer.ttype == '"'))
			m_Tokenizer.ttype = StreamTokenizer.TT_WORD;
		else if ((m_Tokenizer.ttype == StreamTokenizer.TT_WORD)
				&& (m_Tokenizer.sval.equals("?")))
			m_Tokenizer.ttype = '?';
	}

	protected void getNextToken() throws IOException {
		
		if (m_Tokenizer.nextToken() == StreamTokenizer.TT_EOF) {
			errorMessage("premature end of file");
		}
		
		//if token is ',' then find get next token 
		if (m_Tokenizer.ttype == ',') {
			m_Tokenizer.nextToken();
			// if the next token is also ',' then there is a missing value
			if (m_Tokenizer.ttype == ',') {
				m_Tokenizer.ttype = '?';
			}
		}

		else if ((m_Tokenizer.ttype == '\'') || (m_Tokenizer.ttype == '"')) {
			m_Tokenizer.ttype = StreamTokenizer.TT_WORD;
		} else if ((m_Tokenizer.ttype == StreamTokenizer.TT_WORD)
				&& (m_Tokenizer.sval.equals("?"))) {
			m_Tokenizer.ttype = '?';
		}
	}

	private void errorMessage(String msg) throws IOException {
		String str = msg + ", read " + m_Tokenizer.toString();
		if (m_Lines > 0) {
			int line = Integer.parseInt(str.replaceAll(".* line ", ""));
			str = str.replaceAll(" line .*", " line " + (m_Lines + line - 1));
		}
		throw new IOException(str);
	}

	protected void getLastToken(boolean endOfFileOk) throws IOException {
		if ((m_Tokenizer.nextToken() != StreamTokenizer.TT_EOL)
				&& ((m_Tokenizer.ttype != StreamTokenizer.TT_EOF) || !endOfFileOk)) {
			errorMessage("end of line expected");
		}
	}

	protected void readTillEOL() throws IOException {
		while (m_Tokenizer.nextToken() != StreamTokenizer.TT_EOL) {
		}
		;
		m_Tokenizer.pushBack();
	}
	/** method to read header read the first line of the csv file and read the attribure names */
	public void ReadHeader() throws IOException {

		if (m_filePath.length() == 0) {
			throw new IOException("Missing File");

		} else {
			
		
			FileReader fr = new FileReader(m_filePath);
			BufferedReader br = new BufferedReader(fr);
			m_Tokenizer = new StreamTokenizer(br);
			initTokenizer();
			m_Lines = 0;
			
			//csv to find the name of the file need to use the path file and remove the csv part
			if (this.m_filePath != null)
				m_RelationName = (this.m_filePath).replaceAll(
						"\\.[cC][sS][vV]$", "");

			// Create vectors to hold information temporarily.
			FastVector attributes = new FastVector();
			int numAttributes = 0;
			String attributeName;
			
			do {
				getNextToken();
				
				//check whether the token is in second line or token is eol
				if(m_Tokenizer.lineno()>1||m_Tokenizer.ttype==StreamTokenizer.TT_EOL){
					m_Tokenizer.pushBack();
					break;
				}
					//get attribute name and add it to the set of attributes
				attributeName = m_Tokenizer.sval;
				numAttributes++;
				attributes.addElement(new Attribute(attributeName, attributes
						.size(), numAttributes));
				
				//in csv we can't find the type of the attribute when read header. indicate need to find the type of the attribute
				getType.add(false);

			} while (true);

			// Check if any attributes have been declared.
			if (attributes.size() == 0) {
				errorMessage("no attributes declared");
			}
			m_Attributes = attributes;
		}
	}
	/** check whether the string is a date standard date need to be in this format dd/mm/yyyy check the length of the string and the '/' chars   */
	private boolean isDate(String astring) {
		if (astring.length() > 9 && astring.charAt(2) == '/'
				&& astring.charAt(5) == '/')
			return true;
		else
			return false;
	}

	@Override
	protected Instance ReadInstance() throws IOException {
		double[] instanceValues = new double[m_Data.NumAllAttributes()];
		int index = 0;
		int classIndex = 0;
		int numIndex = 0;
		int strIndex = 0;
		boolean bMissingValue;
		
		//if(m_Tokenizer.ttype!=StreamTokenizer.TT_EOL)
			//readTillEOL();
		
		// get the first token it need to take separately as it may have the eol token 
		getFirstToken();
		// if eof token found return null as data reading is finished.
		if(m_Tokenizer.ttype==StreamTokenizer.TT_EOF)
			return null;
		for (int i = 0; i < m_Data.NumAllAttributes(); i++) {

			// Get next token
			if (i > 0) {
				getNextToken();
			}
			
				bMissingValue = false;
				// Check if value is missing.
				
				if (m_Tokenizer.ttype == MISSING_VALUE)
					bMissingValue = true;
				else {
					// Check if token is valid.
					if (m_Tokenizer.ttype != StreamTokenizer.TT_WORD) {
						errorMessage("not a valid value");
					}
				}

				try {
					
					//check for numeric values check whether it string from streamtokenizer can convert into double
					double value;
					if (bMissingValue)
						value = Double.MAX_VALUE;
					else
						value = Double.valueOf(m_Tokenizer.sval);
					
					instanceValues[i] = value;
					//put the type numeric into the attribute
					if (!getType.get(i)) {
						m_Data.setType(i, Attribute.NUMERIC);
						getType.set(i, true);
						
					}
				// if value cannot covert to double it need to be date or nominal
				} catch (Exception e) {
						//check whether the value is of type date
					if (isDate(m_Tokenizer.sval)) {
						if (bMissingValue)
							instanceValues[i] = Double.MAX_VALUE;
						else
							try {
								instanceValues[i] = m_Data.Attribute(i)
										.ParseDate(m_Tokenizer.sval);
							} catch (ParseException e1) {
								errorMessage("unparseable date: "
										+ m_Tokenizer.sval);
							}
						
						//put the type date into the attribute
						if (!getType.get(i)) {
							m_Data.setType(i, Attribute.DATE);
							getType.set(i, true);
						}
						
						// check for nominal values
					} else {
						
						if (bMissingValue)
							instanceValues[i] = Double.MAX_VALUE;
						else{
							Attribute attr=(Attribute)m_Attributes.elementAt(i);
							
							if(attr.getValues()==null||attr.IndexOfValue(m_Tokenizer.sval)==-1){
								attr.addValue(m_Tokenizer.sval);
								m_Attributes.removeElementAt(i);
								m_Attributes.insertElementAt(attr, i);
							}
							instanceValues[i] = m_ValueHandler.GetValue(Attribute.NOMINAL, i, m_Tokenizer.sval);
						}
						//put type nominal into the attribute
						if (!getType.get(i)) {
							m_Data.setType(i, Attribute.NOMINAL);
							getType.set(i, true);
						}

					}

				}

		}
		
		
		// Add instance to dataset
		Instance inst = new Instance(instanceValues);
		// inst.setDataset(m_Data);
		inst.SetClassIndex(classIndex);
		return inst;
	}

}
