/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.dataaccess;

import gaknn.core.Attribute;
import gaknn.core.FastVector;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Niro
 */
public class ParameterReader {
    static double[] m_Weights;
    static FastVector m_Attributes;
    static String m_FilePaath = "";
    
    final static String WEIGHT_PARAM = "weights";
    final static String K_PARAM = "k";
    final static String K_VALUE_NAME = "value";
    
    public ParameterReader(FastVector attributes, String filePath){
        m_Attributes = attributes;
        m_FilePaath = filePath;
    }
    
    public double[] ReadWeights(){
       m_Weights = new double[m_Attributes.size()];
        
        try {
            File file = new File(m_FilePaath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList weightNodes = doc.getElementsByTagName(WEIGHT_PARAM);
            Node weightNode = weightNodes.item(0);;
            Element nodeElement = (Element)weightNode;
            for (int i=0; i < m_Attributes.size(); i++){
                Attribute attribute = (Attribute) m_Attributes.elementAt(i);
                if (!attribute.IsClassAttribute())
                {   
                    //NodeList wtsNds = nodeElement.getElementsByTagName(attribute.Name());
                    String tagName = attribute.Name();
                    NodeList wtsNds = nodeElement.getElementsByTagName(tagName);
                    Element wtsElmnt = (Element) wtsNds.item(0);
                    NodeList fstWt = wtsElmnt.getChildNodes();
                    m_Weights[i] = Double.parseDouble(((Node) fstWt.item(0)).getNodeValue());      
                }
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    
        return m_Weights;
    } 
    
    
    public int ReadK(){
        int k = 1;
        try
        {
            File file = new File(m_FilePaath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList kNodes = doc.getElementsByTagName(K_PARAM);
            Node KNode = kNodes.item(0);
            Element nodeElement = (Element)KNode;
            NodeList kNds = nodeElement.getElementsByTagName(K_VALUE_NAME);
            Element kElmnt = (Element) kNds.item(0);
            NodeList fstK = kElmnt.getChildNodes();
            k = Integer.parseInt(((Node) fstK.item(0)).getNodeValue());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return k;
     }
    
   /*  public static void main(String argv[]) {
         m_FilePaath = "I:\\wts.xml";
         double[] wts = ReadWeights();
         
         for (int i=0; i<wts.length; i++){
             System.out.println(wts[i]);
         }
         
     }
*/
}
