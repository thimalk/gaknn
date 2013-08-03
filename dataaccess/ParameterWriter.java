/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.dataaccess;
import gaknn.core.Attribute;
import gaknn.core.FastVector;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.io.BufferedWriter;
import java.io.FileWriter;




/**
 *
 * @author Niro
 */
public class ParameterWriter {
    double[] m_Weights;
    int k = 0;
    String m_FilePath;
    FastVector m_Attributes;
    
    final static String WEIGHT_TAG = "weights";
    final static String K_TAG = "k";
    final static String K_VALUE_TAG = "value";
    final static String PARAMETER_TAG = "parameters";
    public final static String FILE_EXTENSION = "prm";
    
    public ParameterWriter(FastVector attributes, String filePath){
        m_Attributes = attributes;
        m_FilePath = filePath;
    }
    
    public void Write(double[] weights, int k){
        BufferedWriter bufferedWriter = null;
        try
        {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            Element root = doc.createElement(PARAMETER_TAG);
            doc.appendChild(root);
            
            Element weightElement = doc.createElement(WEIGHT_TAG);
            root.appendChild(weightElement);
            
            ////////////////////////
            //Creating the XML tree

            for (int i=0; i<m_Attributes.size(); i++){
                Attribute attribute = (Attribute) m_Attributes.elementAt(i);
                if (!attribute.IsClassAttribute()){
                    Element wt = doc.createElement(attribute.Name());
                    weightElement.appendChild(wt);
                    Text txt = doc.createTextNode(String.valueOf(weights[i]));
                    wt.appendChild(txt);
                }
            }
            
            if (k>0){
                Element kElement = doc.createElement(K_TAG);
                root.appendChild(kElement);
                Element kValue = doc.createElement(K_VALUE_TAG);
                kElement.appendChild(kValue);
                Text txt = doc.createTextNode(String.valueOf(k));
                kValue.appendChild(txt);
            }

 
            /////////////////
            //Output the XML

            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            //trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();

            //print xml
            bufferedWriter = new BufferedWriter(new FileWriter(m_FilePath));
            bufferedWriter.write(xmlString);

        } catch (Exception e) {
            System.out.println(e);
        }finally {
            //Close the BufferedWriter
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

}
 

