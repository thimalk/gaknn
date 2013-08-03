/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gaknn.dataaccess;

/**
 *
 * @author Niro
 */
import java.io.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class test {

    /**
     * Our goal is to create a DOM XML tree and then print the XML.
     */
    public static void main (String args[]) {
        new test();
    }

    public test() {
        
        BufferedWriter bufferedWriter = null;
        try {
            /////////////////////////////
            //Creating an empty XML Document

            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
           
            Element root = doc.createElement("param");
            doc.appendChild(root);
             
            Element weightElement = doc.createElement("weight");
            root.appendChild(weightElement);
            System.out.println("oooooooooo");
            ////////////////////////
            //Creating the XML tree

            for (int i=0; i<3; i++){
                Element wt = doc.createElement("wt" + i);
                weightElement.appendChild(wt);
                Text txt = doc.createTextNode(String.valueOf(i));
                wt.appendChild(txt);
            }
            

            //add a text element to the child

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
            System.out.println("Here's the xml:\n\n" + xmlString);
            bufferedWriter = new BufferedWriter(new FileWriter("I://test.tt"));
            bufferedWriter.write(xmlString);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
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
