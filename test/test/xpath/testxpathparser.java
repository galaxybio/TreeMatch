package test.xpath;
import twig.model.PatternTree;
import twig.model.PatternTreeNode;
import twig.parse.xpath.XpathParser;
public class testxpathparser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		XpathParser xpathParser = new XpathParser();
//		PatternTreeNode root = xpathParser.parseXpath("//K[/A[/B][/C][-1][/D][<<E[/F][//G][2][//H[/I][/J]]]]");
		//PatternTreeNode root = xpathParser.parseXpath("//A//B[//D][/E[//F]]//C");
		//PatternTreeNode root = xpathParser.parseXpath("//A/B[following-sibling::*[not .//D]/E]");
		//PatternTreeNode root = xpathParser.parseXpath("//A/B[following-sibling::C]");
		//PatternTreeNode root = xpathParser.parseXpath("//A[/B[following-sibling::C]]");
		//PatternTreeNode root = xpathParser.parseXpath("//*//A/following::B/C");
		//PatternTreeNode root = xpathParser.parseXpath("//A[.//B[not D[not E]]]/C/F/G");
		//PatternTreeNode root = xpathParser.parseXpath("//A[not .//B[.//E]][/C/F][not D[/G]]");
		//PatternTreeNode root = xpathParser.parseXpath("//*/B[not following::C[/D]]");
		//PatternTreeNode root = xpathParser.parseXpath("//test//bold/following-sibling::keyword");
		//PatternTreeNode root = xpathParser.parseXpath("//description/partilist/preceding-sibling::text");
		//PatternTreeNode root = xpathParser.parseXpath("//test//bold[following-sibling::keyword[following-sibling::emph]]");
		//PatternTreeNode root = xpathParser.parseXpath("//S[not .//ADJ]//MD");
		//PatternTreeNode root = xpathParser.parseXpath("//VP[/DT][not PP[not .//VBN]]/PRP_DOLLAR_");
		//PatternTreeNode root = xpathParser.parseXpath("//S/VP/PP/IN[following::NP[not VBN]]");
		//PatternTreeNode root = xpathParser.parseXpath("//A[//C[/D[following-sibling::E]]]/B");	
		//PatternTreeNode root = xpathParser.parseXpath("//A[//C[/D[following-sibling::E]]]/B");	
		PatternTreeNode root = xpathParser.parseXpath("//A[/B]//C[not D]");
		//PatternTreeNode root = xpathParser.parseXpath("//A[/C[/D[following-sibling::E]]]");
//		PatternTreeNode root = xpathParser.parseXpath("//A[/C][/D[2]]");
//		PatternTreeNode root = xpathParser.parseXpath("//a[<<b]");
//		PatternTreeNode root = xpathParser.parseXpath("//a[@id=34][@old=sube]//b[/c[@fafa='123124']]");
//		System.out.println(root.toStringDesSibling());   
//		System.out.println();   
//		System.out.println(root.toStringParentSibling());   
		PatternTree tree = new PatternTree(root);
		System.out.println(tree.getPatternTreeNodes());   
		//  E:/Program/Myjava/treebank/1122.xml   	
//		TwigFollowing twig = new TwigFollowing("//A//B//C","E:/Program/Myjava/case/7.xml");
//		twig.TwigList_Construct();
	}
}
