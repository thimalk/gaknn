/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.dataaccess;

import java.io.StreamTokenizer;
import java.io.IOException;
import java.text.ParseException;
import gaknn.core.Instance;
import gaknn.core.Attribute;
import java.io.FileReader;
import java.io.BufferedReader;
import gaknn.core.FastVector;
import gaknn.core.InvalidClassIndexException;

/**
 *
 * @author Niro
 */
public class ArffFileReader extends DataFileReader{
    public static String FILE_EXTENSION = "arff";
    
    public static char MISSING_VALUE = '?';
    
    /** The keyword used to denote the start of an arff header */
    public final static String ARFF_RELATION = "@relation";

    /** The keyword used to denote the start of the arff data section */
    public final static String ARFF_DATA = "@data";
    
    /** The keyword used to denote the start of an arff attribute declaration */
    public final static String ARFF_ATTRIBUTE = "@attribute";

    /** A keyword used to denote a numeric attribute */
    public final static String ARFF_ATTRIBUTE_INTEGER = "integer";

    /** A keyword used to denote a numeric attribute */
    public final static String ARFF_ATTRIBUTE_REAL = "real";

    /** A keyword used to denote a numeric attribute */
    public final static String ARFF_ATTRIBUTE_NUMERIC = "numeric";

    /** The keyword used to denote a string attribute */
    public final static String ARFF_ATTRIBUTE_STRING = "string";

    /** The keyword used to denote a date attribute */
    public final static String ARFF_ATTRIBUTE_DATE = "date";

   private StreamTokenizer m_Tokenizer;

    public ArffFileReader(String sDataFile)  {
        this.m_filePath = sDataFile;
    }
    
    

    protected void initTokenizer(){
        m_Tokenizer.resetSyntax();         
        m_Tokenizer.whitespaceChars(0, ' ');    
        m_Tokenizer.wordChars(' '+1,'\u00FF');
        m_Tokenizer.whitespaceChars(',',',');
        m_Tokenizer.commentChar('%');
        m_Tokenizer.quoteChar('"');
        m_Tokenizer.quoteChar('\'');
        m_Tokenizer.ordinaryChar('{');
        m_Tokenizer.ordinaryChar('}');
        m_Tokenizer.eolIsSignificant(true);
    }
    
    protected void getFirstToken() throws IOException 
    {
        while (m_Tokenizer.nextToken() == StreamTokenizer.TT_EOL) {};

        if ((m_Tokenizer.ttype == '\'') ||(m_Tokenizer.ttype == '"')) 
            m_Tokenizer.ttype = StreamTokenizer.TT_WORD;
        else if ((m_Tokenizer.ttype == StreamTokenizer.TT_WORD) &&
                        (m_Tokenizer.sval.equals("?")))
            m_Tokenizer.ttype = '?';
    }
    
    protected void getNextToken() throws IOException {
        if (m_Tokenizer.nextToken() == StreamTokenizer.TT_EOL) 
        {
             errorMessage("premature end of file");
        }
        if (m_Tokenizer.ttype == StreamTokenizer.TT_EOF) 
        {
            errorMessage("premature end of file");
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
    
    protected Instance ReadInstance() throws IOException {
        double[] instanceValues = new double [m_Data.NumAttributes()];
        int index = 0;
        int classIndex = 0;
        int numIndex = 0;
        int strIndex = 0;
        boolean bMissingValue ;

        //	    Check if end of file reached.
        getFirstToken();
        if (m_Tokenizer.ttype == StreamTokenizer.TT_EOF) {
            return null;
        }
        // Get values for all attributes.
        for (int i = 0; i < m_Data.NumAllAttributes(); i++){

            // Get next token
            if (i > 0) {
                getNextToken();
            }
            
            if (m_Data.Attribute(i).Selected())
            {          
                // Check if value is missing.
                //   if  (m_Tokenizer.ttype == '?') {
                //   	instance.addElement(Instance.MISSING_VALUE);
                //   } else {
                bMissingValue = false;
                //Check if value is missing.
        
                if  (m_Tokenizer.ttype == MISSING_VALUE) 
                    bMissingValue = true;
                else
                {
                // Check if token is valid.
                    if (m_Tokenizer.ttype != StreamTokenizer.TT_WORD) {
                        errorMessage("not a valid value");
                    }
                }

                String attrName = m_Data.Attribute(i).Name();

                switch (m_Data.Attribute(i).Type()) {
                case Attribute.NOMINAL:
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
                            instanceValues[numIndex] = m_ValueHandler.GetMissingValue(Attribute.NOMINAL, i); 
                        else
                            instanceValues[numIndex] = m_ValueHandler.GetValue(Attribute.NOMINAL, i, m_Tokenizer.sval);
                        numIndex++;
                    }

                    break;
                case Attribute.NUMERIC:
                // Check if value is really a number.
                    try{
                        double value;

                        if (bMissingValue)
                            value = m_ValueHandler.GetMissingValue(Attribute.NUMERIC, i); 
                        else
                            value = Double.valueOf(m_Tokenizer.sval).doubleValue();

                        instanceValues[numIndex] = value;
                        numIndex++;
                    } catch (NumberFormatException e) {
                        errorMessage("number expected");
                    }
                    break;
                case Attribute.STRING:
                    if (bMissingValue)
                        instanceValues[numIndex] = m_ValueHandler.GetMissingValue(Attribute.STRING, i);
                    else
                        instanceValues[numIndex] = m_ValueHandler.GetValue(Attribute.STRING, i, m_Tokenizer.sval);
                    numIndex++;
                    break;
                case Attribute.DATE:
                    try {
                        if (bMissingValue)
                            instanceValues[numIndex] = m_ValueHandler.GetMissingValue(Attribute.DATE, i);
                        else
                            instanceValues[numIndex] = m_Data.Attribute(i).ParseDate(m_Tokenizer.sval);
                        numIndex++;

                        numIndex++;
                    } catch (ParseException e) {
                        errorMessage("unparseable date: " + m_Tokenizer.sval);
                    }
                    break;
                default:
                    errorMessage("unknown attribute type in column " + i);
                }
            }
        }
        // Add instance to dataset
        Instance inst = new Instance(instanceValues);
        // inst.setDataset(m_Data);
        inst.SetClassIndex(classIndex);
        return inst;
    }
    
    public void ReadHeader() throws IOException {
        
        if (m_filePath.length() == 0)
            throw new IOException("Missing File");
        else
        {
            FileReader fr = new FileReader(m_filePath); 
            BufferedReader br = new BufferedReader(fr);

            m_Tokenizer = new StreamTokenizer(br);
            initTokenizer();
            m_Lines = 0;

            // Get name of relation.
            getFirstToken();
            if (m_Tokenizer.ttype == StreamTokenizer.TT_EOF) {
              errorMessage("premature end of file");
            }
            
            if (ARFF_RELATION.equalsIgnoreCase(m_Tokenizer.sval)) {
                getNextToken();
                m_RelationName = m_Tokenizer.sval;
                getLastToken(false);
            } else {
                errorMessage("keyword " + ARFF_RELATION + " expected");
            }

            //Create vectors to hold information temporarily.
            FastVector attributes = new FastVector();

            //Get attribute declarations.
            getFirstToken();
            
            if (m_Tokenizer.ttype == StreamTokenizer.TT_EOF) 
                errorMessage("premature end of file");

            attributes = parseAttributes();
            // Check if data part follows. We can't easily check for EOL.
            if (!ARFF_DATA.equalsIgnoreCase(m_Tokenizer.sval)) {
                errorMessage("keyword " + ARFF_DATA + " expected");
            }

              // Check if any attributes have been declared.
            if (attributes.size() == 0) {
                errorMessage("no attributes declared");
            }
            m_Attributes = attributes;
        }
    }
    
    protected FastVector parseAttributes() throws IOException {
        String attributeName;
        FastVector attributes = new FastVector();
        FastVector attributeValues;
        int numAttributes = 0;

        while (ARFF_ATTRIBUTE.equalsIgnoreCase(m_Tokenizer.sval)) {

            // Get attribute name.
            getNextToken();
            attributeName = m_Tokenizer.sval;
            getNextToken();

        // Check if attribute is nominal.
            if (m_Tokenizer.ttype == StreamTokenizer.TT_WORD) {

                // Attribute is real, integer, or string.
                if (m_Tokenizer.sval.equalsIgnoreCase(ARFF_ATTRIBUTE_REAL) ||
                    m_Tokenizer.sval.equalsIgnoreCase(ARFF_ATTRIBUTE_INTEGER) ||
                    m_Tokenizer.sval.equalsIgnoreCase(ARFF_ATTRIBUTE_NUMERIC)) {
                    numAttributes++;
                    attributes.addElement(new Attribute(attributeName, attributes.size(), numAttributes));
                    readTillEOL();
                } else if (m_Tokenizer.sval.equalsIgnoreCase(ARFF_ATTRIBUTE_STRING)) {
                    numAttributes++;
                    attributes.addElement(new Attribute(attributeName, (FastVector)null,
                                    attributes.size(), numAttributes));
                    readTillEOL();
                } else if (m_Tokenizer.sval.equalsIgnoreCase(ARFF_ATTRIBUTE_DATE)) {
                  String format = null;
                  if (m_Tokenizer.nextToken() != StreamTokenizer.TT_EOL) {
                        if ((m_Tokenizer.ttype != StreamTokenizer.TT_WORD) &&
                            (m_Tokenizer.ttype != '\'') &&
                            (m_Tokenizer.ttype != '\"')) {
                            errorMessage("not a valid date format");
                        }
                        format = m_Tokenizer.sval;
                        readTillEOL();
                  } else {
                        m_Tokenizer.pushBack();
                  }
                  numAttributes++;
                  attributes.addElement(new Attribute(attributeName, format,
                                  attributes.size(), numAttributes));
                } else {
                      errorMessage("no valid attribute type or invalid "+
                            "enumeration");
                }
        } else {
            // Attribute is nominal.
            attributeValues = new FastVector();
            m_Tokenizer.pushBack();

            // Get values for nominal attribute.
            if (m_Tokenizer.nextToken() != '{') {
                errorMessage("{ expected at beginning of enumeration");
            }
            while (m_Tokenizer.nextToken() != '}') {
                if (m_Tokenizer.ttype == StreamTokenizer.TT_EOL) {
                    errorMessage("} expected at end of enumeration");
                } else {
                    attributeValues.addElement(m_Tokenizer.sval);
                }
            }

            Attribute attrb = new Attribute(attributeName, attributeValues,
                            attributes.size(), numAttributes);
            //System.out.println(attributeName);
           // if (attributeName.compareTo(Attribute.ARFF_ATTRIBUTE_CLASS) == 0){
            //    attrb.SetClassAttribute(true);
           // }
            //else
            numAttributes++;

            attributes.addElement(attrb);
        }
        
        getLastToken(false);
        getFirstToken();
        
        if (m_Tokenizer.ttype == StreamTokenizer.TT_EOF)
            errorMessage("premature end of file");
        }
        return attributes;
    }
    
    public void SetClassAttribute(String classAttribute) throws Exception{
       int index = -1;
       Attribute attribute;
       
       for (int i=0; i<m_Attributes.size(); i++){
           attribute = (Attribute)m_Attributes.elementAt(i);
           if (attribute.Name().equalsIgnoreCase(classAttribute)){
               index = i;
               attribute.SetClassAttribute(true);
           }
       }
       
       if (index > -1)
       {
           for (int i=index; i<m_Attributes.size(); i++){
               attribute = (Attribute)m_Attributes.elementAt(i);
               attribute.ValueIndex(i-1);
           }
       }
       else
           throw new Exception("Invalid attribute name for class " + classAttribute);
    }

}
