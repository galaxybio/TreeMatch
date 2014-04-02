package twig.parse.xpath;

import java.util.HashMap;

import twig.model.*;


public class XpathParser {

	private int index = 1;

	private  HashMap<Label, PatternTreeNode> StringToNode;
	public XpathParser() {
	}

	public PatternTreeNode parseXpath(String Xpath) {

		if(Xpath.contains("[not following-sibling::"))
		{
			Xpath =Xpath.replace("[not following-sibling::","[!<");
		}
		if(Xpath.contains("/not following-sibling::"))
		{
			Xpath =Xpath.replace("/not following-sibling::","!<");
		}
		if(Xpath.contains("[not following::"))
		{
			Xpath =Xpath.replace("[not following::", "[!<<");
		} 
		if(Xpath.contains("/not following::"))
		{
			Xpath =Xpath.replace("/not following::","!<<");
		}
		if(Xpath.contains("[not preceding-sibling::"))
		{
			Xpath =Xpath.replace("[not preceding-sibling::","[!>");
		}
		if(Xpath.contains("/not preceding-sibling::"))
		{
			Xpath =Xpath.replace("/not preceding-sibling::", "!>");
		}
		if(Xpath.contains("[not preceding::"))
		{
			Xpath =Xpath.replace("[not preceding::", "[!>>");
		} 
		if(Xpath.contains("/not preceding::"))
		{
			Xpath =Xpath.replace("/not preceding::", "!>>");
		}

		if(Xpath.contains("[following-sibling::"))
		{
			Xpath = Xpath.replace("[following-sibling::", "[<");
		}
		if(Xpath.contains("/following-sibling::"))
		{
			Xpath = Xpath.replace("/following-sibling::", "<");
		}

		if(Xpath.contains("[following::"))
		{
			Xpath = Xpath.replace("[following::", "[<<");
		}

		if(Xpath.contains("/following::"))
		{
			Xpath = Xpath.replace("/following::", "<<");
		}
		if(Xpath.contains("[preceding-sibling::"))
		{
			Xpath = Xpath.replace("[preceding-sibling::", "[>");
		}

		if(Xpath.contains("/preceding-sibling::"))
		{
			Xpath = Xpath.replace("/preceding-sibling::", ">");
		}

		if(Xpath.contains("[preceding::"))
		{
			Xpath = Xpath.replace("[preceding::", "[>>");
		}

		if(Xpath.contains("/preceding::"))
		{
			Xpath = Xpath.replace("/preceding::", ">>");
		}
		if(Xpath.contains("not"))
		{
			Xpath = Xpath.replace("not ", "!");
		}

		if(Xpath.contains(".//"))
		{
			Xpath = Xpath.replace(".//", "//");
		}
		StringToNode = new HashMap<Label,PatternTreeNode>();
		StringBuffer xpath = new StringBuffer(Xpath);
		StringBuffer xpath_return = new StringBuffer(Xpath);

		xpath_return = getReturnXpath(xpath_return);

		String returnString =getReturnString(xpath_return);	
		final PatternTreeNode doc = new PatternTreeNode();
		doc.setLabel("doc");
		PatternTreeNode root = new PatternTreeNode();
		root.setParent(doc);
		getOneStep(xpath, root);
		setIndex(root);
		StringToNode.get(Label.createLabel(returnString)).setReturnNode();
		return root;
	}

	public PatternTreeNode getOneStep(StringBuffer xpath,PatternTreeNode onestep) {
		onestep.setAxisType(getAxisType(xpath));
		onestep.setLabel(getLabel(xpath));

		StringToNode.put( onestep.getLabel(),onestep);
		while (xpath.length() != 0) {
			StringBuffer subxpath = getSubXpath(xpath);   
			onestep.setSubpath(subxpath.toString());		
			if (subxpath.charAt(0) == '/' || subxpath.charAt(0) == '<' || subxpath.charAt(0) == '!' || subxpath.charAt(0) == '>') {
				PatternTreeNode substep = new PatternTreeNode();
				if(subxpath.charAt(0)!='<' && subxpath.charAt(0)!='>')
				{

					if(subxpath.charAt(0)=='!')
					{

						substep.setIsNeg();
						if(subxpath.charAt(1)=='>'||subxpath.charAt(1)=='<')
						{
							substep.setParent(onestep.getParent());
							onestep.getParent().addChildrenNode(substep);
							if(subxpath.charAt(1)=='<')
							{
								substep.setPreNode(onestep);
								onestep.setPostNode(substep);
							}
							else
							{
								onestep.setPreNode(substep);
								substep.setPostNode(onestep);
							}
							onestep.getParent().addPatternTreeNode(getOneStep(subxpath, substep));
						}
						else
						{

							substep.setParent(onestep);
							onestep.addChildrenNode(substep);
							onestep.addPatternTreeNode(getOneStep(subxpath, substep));	
						}
					}
					else
					{
						substep.setParent(onestep);
						onestep.addChildrenNode(substep);
						onestep.addPatternTreeNode(getOneStep(subxpath, substep));	
					}

				}
				else
				{
					substep.setParent(onestep.getParent());
					onestep.getParent().addChildrenNode(substep);
					if(subxpath.charAt(0)=='<')
					{
						substep.setPreNode(onestep);
						onestep.setPostNode(substep);
					}
					else
					{
						onestep.setPreNode(substep);
						substep.setPostNode(onestep);
					}
					onestep.getParent().addPatternTreeNode(getOneStep(subxpath, substep));

				}
			}else if(subxpath.charAt(0) == '@' ){
				String attribute = getAttribute(subxpath);
				onestep.addAttribute(attribute, subxpath);
			}
		}		
		return onestep;
	}

	public String getReturnString(StringBuffer xpath)
	{   
		int i ;
		for(i=xpath.length()-1;i>0;i--)
			if(xpath.charAt(i)=='/'||xpath.charAt(i)=='>'||xpath.charAt(i)=='<'||xpath.charAt(i)=='!')
				break;
		return xpath.substring(i+1);
	}


	public StringBuffer getReturnXpath(StringBuffer xpath)
	{
		int begin=0;
		int end= 0;
		int count=0;
		for(int i = 0;i<xpath.length();i++)
		{
			if(xpath.charAt(i)=='[')
			{
				begin = i;
				break;
			}
		}
		for(int i = 0;i<xpath.length();i++)
		{
			if(xpath.charAt(i)=='[')
			{
				count++;
			}
			if(xpath.charAt(i)==']')
				break;
		}


		for(int j = begin;j<xpath.length();j++)
		{
			if(xpath.charAt(j)==']')
			{
				count--;
			}
			if(count == 0)
			{
				end=j;
				break;
			}
		}

		xpath.delete(begin, end+1);
		for(int k=0;k<xpath.length();k++)
		{
			if(xpath.charAt(k)=='[' || xpath.charAt(k)==']')
				getReturnXpath(xpath);
		}
		return xpath;
	}

	public AxisType getAxisType(StringBuffer xpath) {
		if(xpath.charAt(0)=='!' && xpath.charAt(1)=='<'&&xpath.charAt(2)=='<')
		{
			xpath.delete(0, 3);
			return AxisType.NOTFOLLOWING;
		}
		if(xpath.charAt(0)=='!' && xpath.charAt(1)=='<'&&xpath.charAt(2)!='<')
		{
			xpath.delete(0, 2);
			return AxisType.NOTFSIBLING;
		}

		if(xpath.charAt(0)=='!' && xpath.charAt(1)=='>'&&xpath.charAt(2)=='>')
		{
			xpath.delete(0, 3);
			return AxisType.NOTPRECEDING;
		}
		if(xpath.charAt(0)=='!' && xpath.charAt(1)=='>'&&xpath.charAt(2)!='>')
		{
			xpath.delete(0, 2);
			return AxisType.NOTPSIBLING;
		}
		if(xpath.charAt(1)=='>')
		{
			xpath.delete(0, 2);
			return AxisType.PRECEDING;
		}
		if(xpath.charAt(1)=='<')
		{
			xpath.delete(0, 2);
			return AxisType.FOLLOWING;
		}

		if(xpath.charAt(0)=='!'&&xpath.charAt(1)=='/'&&xpath.charAt(2)=='/')
		{
			xpath.delete(0, 3);
			return AxisType.NOTAD;	
		}		
		else
		{
			if (xpath.charAt(0) == '<' || xpath.charAt(0)=='!' || xpath.charAt(0)=='>') 
			{

				if(xpath.charAt(0)=='<')
				{
					xpath.delete(0, 1);
					return AxisType.FSIBLING;
				}
				if(xpath.charAt(0)=='>')
				{
					xpath.delete(0, 1);
					return AxisType.PSIBLING;
				}
				else
				{
					xpath.delete(0, 1);
					return AxisType.NOTPC;
				}

			}
			else {
				if (xpath.charAt(1) == '/') {
					xpath.delete(0, 2);
					return AxisType.AD;
				} else {
					xpath.delete(0, 1);
					return AxisType.PC;
				}
			}
		}
	}

	public StringBuffer getSubXpath(StringBuffer xpath) {
		if (xpath.charAt(0) == '[') {
			int count = 1, i;

			for (i = 1; i < xpath.length(); i++) {
				if (xpath.charAt(i) == '[')
					count++;
				else if (xpath.charAt(i) == ']') {
					count--;
					if (count == 0) {
						break;
					}
				}
			}
			String str = xpath.substring(1, i);
			xpath.delete(0, i + 1);
			StringBuffer subpath = new StringBuffer(str);
			return subpath;
		} else {
			StringBuffer subpath = new StringBuffer(xpath.toString());
			xpath.setLength(0);
			return subpath;
		}
	}

	public String getLabel(StringBuffer xpath) {
		String label;
		int i = 0;
		while (i < xpath.length() && xpath.charAt(i) != '/'
				&& xpath.charAt(i) != '<' && xpath.charAt(i)!='>' && xpath.charAt(i)!='!' && xpath.charAt(i) != '[') {
			i++;
		}
		label = xpath.substring(0, i);
		xpath.delete(0, i);
		return label;
	}
	public String getAttribute(StringBuffer xpath){		
		String attribute;
		int i = 0;
		while (i < xpath.length() && xpath.charAt(i) != '=') {
			i++;
		}
		attribute = xpath.substring(1, i);
		xpath.delete(0, i);
		return attribute;		
	}




	private void setIndex(PatternTreeNode node) {
		node.setIndex(index);
		index++;
		for (int i = 0; i < node.getDescendantNodes().size(); i++) {
			setIndex(node.getDescendantNodes().get(i));
		}
	}
}
