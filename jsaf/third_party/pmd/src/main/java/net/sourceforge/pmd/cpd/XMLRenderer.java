/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package net.sourceforge.pmd.cpd;

import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Philippe T'Seyen - original implementation
 * @author Romain Pelisse - javax.xml implementation 
 * 
 */
public final class XMLRenderer implements Renderer {

	private Document createDocument() {
    	try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			return parser.newDocument();
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private String xmlDocToString(Document doc) {
        try {
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.ENCODING, System.getProperty("file.encoding"));
	        StringWriter writer = new StringWriter();
	        transformer.transform(new DOMSource(doc), new StreamResult(writer));
	        return writer.getBuffer().toString().replaceAll("\n|\r", "");
        }  catch (TransformerException e) {
        	throw new IllegalStateException(e);
		}
	}
	
    public String render(Iterator<Match> matches) {
    	Document doc = createDocument();
		Element root = doc.createElement("pmd-cpd");
		doc.appendChild(root);

        Match match;
        while (matches.hasNext()) {
            match = matches.next();
            root.appendChild( addCodeSnippet(doc, addFilesToDuplicationElement(doc, createDuplicationElement(doc, match), match), match ) );
        }
        return xmlDocToString(doc);
    }
    
    private Element addFilesToDuplicationElement(Document doc, Element duplication, Match match) {
    	TokenEntry mark;
        for (Iterator<TokenEntry> iterator = match.iterator(); iterator.hasNext();) {
            mark = iterator.next();
            Element file = doc.createElement("file");
            file.setAttribute("line", String.valueOf(mark.getBeginLine()));
            file.setAttribute("path", mark.getTokenSrcID());
            duplication.appendChild(file);
        }
        return duplication;
    }

    private Element addCodeSnippet(Document doc, Element duplication, Match match) {
        String codeSnipet = match.getSourceCodeSlice();
        if (codeSnipet != null) {
        	Element codefragment = doc.createElement("codefragment");
            codefragment.appendChild(doc.createCDATASection(codeSnipet));
            duplication.appendChild(codefragment);
        }
        return duplication;
    }
    
    private Element createDuplicationElement(Document doc, Match match) {
        Element duplication = doc.createElement("duplication");
        duplication.setAttribute("lines", String.valueOf(match.getLineCount()));
        duplication.setAttribute("tokens", String.valueOf(match.getTokenCount()));
        return duplication;
    }
}
