package test.xquery;

import twig.model.PatternTree;
import twig.model.PatternTreeNode;
import twig.parse.xpath.XpathParser;


public class testxquery {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XpathParser xpathParser = new XpathParser();
		String str = null;
		String doc = null;
		

		
//		doc= "F:/Program/Myjava/case/22.xml";
//		 str = "//a[<<b]";
//		doc = "F:/Program/Myjava/xmark/xmark.xml";
		//str ="//text//bold<<keyword";
		//str ="//item[/location<<name<<payment]/description//keyword";
		//str ="//people[//city]//phone[<<creditcard][<<watches]" ;
		//str ="//text//bold[<<keyword[<<emph]]";
		//str ="//item/name[<<payment<<description]";		
		//str ="//people[//city]//phone[<<creditcard<<watches]";
		
		//str ="//item[2][/location]/description//keyword";
		//str ="//people//person[//address/zipcode][/profile/education][2]";
		//str ="//item[/location][2][//mailbox/mail//emph]/description//keyword[2]";		
//		str ="//item//bold<<keyword";
//		str ="//text//bold[3]<<keyword[3]";
		//str ="//item[2][/location<<name<<payment]/description//keyword";
		//str ="//text//bold[2][<<keyword[<<emph[2]][2]]";
		//str ="//item/name[<<payment<<description][1]"	;
		
		//doc = "F:/Program/Myjava/qizxopen-4.1/dblp.xml";		
//		//str ="//dblp/article[/title<<pages<<url<<ee]";
//		//str ="//dblp/inproceedings[/title]/ee<<author<<url";
//		str ="//dblp//article[//author]/ee<<cite";
//		
//		//str ="//dblp/inproceedings[3][/title]/author[2]";
//		//str ="//dblp/article[/author[1]][//title][2]//year" ;
//		//str ="//dblp/inproceedings[//cite][2][/title[1]]/author";
//		//str ="//dblp/article[3][/author][/title][/url][//ee]//year";
		//str ="//article[/volume][/cite[2]][2]//journal";
//		//str ="//dblp/article[/title<<pages[2]<<url<<ee[2]]";
//		//str ="//dblp/inproceedings[/title][/ee[2]<<author[2]<<url][2]";
//		//str ="//dblp/inproceedings[/title][/ee[2]<<author[2]<<url]";	
//		//str ="//dblp//article[//author[2]]/ee<<cite[3]";
		
		doc = "F:/Program/Myjava/qizxopen-4.1/treebank.xml";	
		str = "//NP[/S[//VP[//NN][//PP]/NP]]//VBN";
//		//str ="//S/VP/PP/IN[<<NP[/VBN]]";
//		//str ="//S/VP//_NONE_<<PP[/IN<<NP//VBN]";
//		str ="//S/VP//NP/CD<<PP[//NNP<<NNS]";
//		
//		//str ="//S/VP//PP[2][//NP/VBN]/IN";
//		//str ="//S/VP/PP[/NP/VBN][2][/IN]";
//		str ="//S/VP/PP[//NN][2][//NP[//CD[2]]/VBN]/IN";
//		str ="//S/VP/PP/IN[<<NP[/VBN[2]]]";
//		str ="//S/VP//_NONE_[2]<<PP[/IN<<NP//VBN]";
//		str ="//S/VP//NP/CD[2]<<PP[//NNP<<NNS]";	
		
		PatternTreeNode root = xpathParser.parseXpath(str); 
		PatternTree tree = new PatternTree(root);
		System.out.println(tree.toXQuery(doc));   
	}

}
