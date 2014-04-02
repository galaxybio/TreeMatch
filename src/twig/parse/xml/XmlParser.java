package twig.parse.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.XMLReaderFactory;
import twig.model.*;
public class XmlParser extends DefaultHandler2 implements DeclHandler {
	private ArrayList<XmlElement> buffer;
	private ArrayList<XmlElement> ancestor;
	private Stack<XmlElement> stack;
	private PatternTreeNode root;
	StringBuilder content;
	public XmlParser(PatternTreeNode root) {
		this.stack = new Stack<XmlElement>();
		content = new StringBuilder();
		this.ancestor = new ArrayList<XmlElement>();
		this.buffer = new ArrayList<XmlElement>();
		this.root = root;
		new HashMap<String, String>();
	}

	public ArrayList<XmlElement> execute(String fileName) throws Exception{
		XMLReader reader = XMLReaderFactory.createXMLReader();
		reader.setContentHandler(this);/**this: NFAEngine*/
		reader.parse(fileName);
		return buffer;
	}
	public ArrayList<XmlElement> getXmlElements() {
		return buffer;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes){
		XmlElement newElem = null;
		Scheme scheme = null;
		XmlElement parent = null;

		if (stack.isEmpty()) {
			scheme = new Scheme();
			scheme.getExtendDewey().add(1);
			newElem = new XmlElement(localName,null,ancestor,attributes,scheme,root);
		}
		else 
		{  
			int y;
			parent = stack.peek();
			XmlElement preSibling = null;
			if (buffer.size() > 0) {
				preSibling = buffer.get(buffer.size() - 1);
				if (parent==preSibling)
					preSibling = null;		
			}
			if(preSibling == null)
			{
				y = 0;
			}
			else
			{
				while(preSibling.getExtendDewey().size()!=parent.getExtendDewey().size()+1)
				{
					preSibling=preSibling.getParent();
				}
				y = preSibling.getScheme().getLastComponent();
			}
			scheme = new Scheme(parent.getScheme(), y+1);			
			if(root.getPatternTreeNode(localName)==null && root.getTreeNodes().contains(root.getPatternTreeNode("*")))
			{
				newElem = new XmlElement(localName,parent,ancestor,attributes,scheme,root,"*");
			}
			else
			{
				newElem = new XmlElement(localName,parent,ancestor,attributes,scheme,root);
			}
			parent.addChildren(newElem);
		}
		buffer.add(newElem);
		if(root.getPatternTreeNode(localName)==null)
		{
			if(root.getTreeNodes().contains(root.getPatternTreeNode("*")))
				newElem.setPatternTreeNode(root.getPatternTreeNode("*"));
			else
				newElem.setPatternTreeNode(null);
		}
		else
			newElem.setPatternTreeNode(root.getPatternTreeNode(localName));
		if(root.getPatternTreeNode(localName)!=null)
		{
			root.getPatternTreeNode(localName).buffer.addXmlElement(newElem);
		}
		else
		{
			if(root.getTreeNodes().contains(root.getPatternTreeNode("*")))
			{
				root.getPatternTreeNode("*").buffer.addXmlElement(newElem);
			}
		}
		stack.push(newElem);
		ancestor.add(newElem);			
	}

	public void endElement(String uri, String localName, String qName){
		this.stack.pop();
		ancestor.remove(ancestor.size()-1);
	}


	public void characters(char[] ch, int start, int length){
		this.content.append(ch, start, length);
	}

	public void startDocument() throws SAXException {
	}

	public void endDocument() throws SAXException {
	}
}



