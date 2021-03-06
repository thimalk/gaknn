package gaknn.dataaccess;

import gaknn.Gaknn;
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
/**
 * represent the class to get input from csv file
 *
 *@author Thimal Kempitiya
 */
public class CsvFileReader extends DataFileReader {
	/**
	 * keep the file extension
	 */
	public static String FILE_EXTENSION = "csv";
	
	private static String m_Method=Gaknn.CLASSIFIER_M;

	/** Constant set for for missing values */
	public static char MISSING_VALUE = '?';

	/** keep wheather the type of each attribute is find or not */
	public ArrayList<Boolean> getType = new ArrayList<Boolean>();
	/** StreamTokenizer to divide the file values  */
	StreamTokenizer m_Tokenizer;

	public CsvFileReader(String sDataFile,String method) {
		// get the csv file path
		this.m_filePath = sDataFile;
		this.m_Method=method;

	}

	/** initializing the streamTokenizer*/
	protected void initTokenizer() {
		m_Tokenizer.resetSyntax();
		m_Tokenizer.whitespaceChars(0, ' ');
		m_Tokenizer.wordChars(' ' + 1, '\u00FF');
		m_Tokenizer.ordinaryChar(',');

		m_Tokenizer.eolIsSignificant(true);
	}
	
	/** extract the first token from file*/
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

	/** extract next token from file*/
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

	/** throw the expection with custom message
	 * @param String message*/
	private void errorMessage(String msg) throws IOException {
		String str = msg + ", read " + m_Tokenizer.toString();
		if (m_Lines > 0) {
			int line = Integer.parseInt(str.replaceAll(".* line ", ""));
			str = str.replaceAll(" line .*", " line " + (m_Lines + line - 1));
		}
		throw new IOException(str);
	}

	/** get last token of file
	 * @param boolean whether to consider EOF or not */
	protected void getLastToken(boolean endOfFileOk) throws IOException {
		if ((m_Tokenizer.nextToken() != StreamTokenizer.TT_EOL)
				&& ((m_Tokenizer.ttype != StreamTokenizer.TT_EOF) || !endOfFileOk)) {
			errorMessage("end of line expected");
		}
	}
	/** read until EOL find */
	protected void readTillEOL() throws IOException {
		while (m_Tokenizer.nextToken() != StreamTokenizer.TT_EOL) {
		}
		;
		m_Tokenizer.pushBack();
	}
	/** method to read header read the first line of 
	 * the csv file and read the attribure names.
	 * */
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
	/** check whether the string is a date standard date need to be in this format dd/mm/yyyy 
	 * check the length of the string and the '/' chars.
	 * @param String string to check date format
	 * @return boolean true date format false not
	 * */
	private boolean isDate(String astring) {
		if (astring.length() > 9 && astring.charAt(2) == '/'
				&& astring.charAt(5) == '/')
			return true;
		else
			return false;
	}
	/** read each instance
	 * @return Instance instance keep the values*/

	@Override
	protected Instance ReadInstance() throws IOException {
		
		double[] instanceValues = new double[m_Data.NumAttributes()];
		int index = 0;
		double classIndex = 0;
		int numIndex = 0;
		int strIndex = 0;
		boolean bMissingValue;
		
		//if(m_Tokenizer.ttype!=StreamTokenizer.TT_EOL)
			//readTillEOL();
		
		// get the first token it need to take separately as it may have the eol token 
		getFirstToken();
		// if eof token found return null as data reading is finished.
		if(m_Tokenizer.ttype==StreamTokenizer.TT_EOF){
			
			return null;
		}
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
					

                    if (m_Data.Attribute(i).IsClassAttribute())
                    {
                        m_Data.SetClassIndex(i);
                        classIndex = value;
                        
                        if(m_Method==Gaknn.CLASSIFIER_M){
                        	Attribute attr=(Attribute)m_Attributes.elementAt(i);
							
							if(attr.getValues()==null||attr.IndexOfValue(m_Tokenizer.sval)==-1){
								attr.addValue(m_Tokenizer.sval);
								m_Attributes.removeElementAt(i);
								m_Attributes.insertElementAt(attr, i);
							}
                        }
                    }
                    else
                    {
					instanceValues[numIndex] = value;
					numIndex++;
                    }
					//put the type numeric into the attribute
					if (!getType.get(i)) {
						if(m_Method==Gaknn.CLASSIFIER_M)
							m_Data.setType(i, Attribute.NOMINAL);
						else
						m_Data.setType(i, Attribute.NUMERIC);
						getType.set(i, true);
						
					}
					
					
				// if value cannot covert to double it need to be date or nominal
				} catch (Exception e) {
						//check whether the value is of type date
					if (isDate(m_Tokenizer.sval)) {
						if (bMissingValue){
							instanceValues[numIndex] = Double.MAX_VALUE;
							numIndex++;
						}
						else
							try {
								instanceValues[numIndex] = m_Data.Attribute(i)
										.ParseDate(m_Tokenizer.sval);
								numIndex++;
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
						
							Attribute attr=(Attribute)m_Attributes.elementAt(i);
							
							if(attr.getValues()==null||attr.IndexOfValue(m_Tokenizer.sval)==-1){
								attr.addValue(m_Tokenizer.sval);
								m_Attributes.removeElementAt(i);
								m_Attributes.insertElementAt(attr, i);
							}
							if (bMissingValue)
		                        index = -1;
		                    else
		                        // Check if value appears in header.
		                        index = m_Data.Attribute(i).IndexOfValue(m_Tokenizer.sval);

		                    if (m_Data.Attribute(i).IsClassAttribute())
		                    {
		                        m_Data.SetClassIndex(i);
		                        classIndex = index;
		                        
		                    }
		                    else
		                    {
		                        if (bMissingValue)
		                            instanceValues[i] = m_ValueHandler.GetMissingValue(Attribute.NOMINAL, i); 
		                        else
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
