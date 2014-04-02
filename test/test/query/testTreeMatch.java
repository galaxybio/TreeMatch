package test.query;

import twig.query.TreeMatch;

public class testTreeMatch {
	public static void main(String[] args) throws Exception {
		//TreeBank
		//String xmlfile="F:/Program/Myjava/treebank/treebank.xml";
		// AD PC 测试
		//String xpath="//S[//VP/IN][//NP]";
		//String xpath="//PP[/NP/VBN]/IN";
		//String xpath="//VP[//NN]/S";
		//String xpath = "//S[/ADJP][/PP[//NP]//IN]";
		//String xpath = "//NP[/S[//VP[//NN][//PP]/NP]]//VBN";
		//String xpath ="//S[/VP[//NP//JJ]/PP//NN]//V";
		// AD PC following-sibling:: 测试
		//String xpath = "//S/VP/PP/IN[following-sibling::NP[/VBN]]";
		//String xpath="//S/VP//_NONE_/following-sibling::PP[/IN/following-sibling::NP//VBN]";
		//String xpath="//S/VP//NP/CD/following-sibling::PP[//NNP/following-sibling::NNS]";
		//String xpath = "//S[//VBP][/NP[//NN]/JJ]/VP[//VBG]/VBN";
		//String xpath = "//S/VP[//JJ]/PP";
		
		//Xmark
		//String xmlfile = "F:/Program/Myjava/xmark/xmark.xml";
		// AD PC 测试
		//String xpath ="//item[//location]//description//keyword";
		//String xpath = "//people//person[//address//zipcode]//profile//education";
		//String xpath = "//item[//location][//mailbox//mail//emph]//description//keyword";
		//String xpath = "//item[/location]/description//keyword";
		//String xpath = "//people//person[//address/zipcode]/profile/education";
		//String xpath = "//person[//address//zipcode][//id]//profile[//age]//education";
		//String xpath = "//person[//address[/street]/city]/profile[//age]//business";
		//String xpath = "//person[/homepage]//profile[/education]//interest";
		// AD PC following-sibling::  测试
		//String xpath = "//item[/location/following-sibling::name/following-sibling::payment]/description//keyword";
		//String xpath ="//item//bold[following-sibling::keyword[following-sibling::emph]]";
		//String xpath = "//person[//homepage]//profile[/education]//interest";
		
		
		
		//dblp
	    String xmlfile="F:/Program/Myjava/dblp/dblp.xml";
		//String xmlfile="F:/Program/Myjava/test-use/book.xml";
		// AD PC 测试
		//Q7
		//String xpath ="//article[//cdrom]//author";
		//Q8
		String xpath = "//inproceedings[/author]//title/sup";
		//Q9
		//String xpath = "//inproceedings[/author][//title/sup]";
		//String xpath ="//dblp//inproceedings[//title]//author";
		//String xpath = "//dblp//article[//author][//title]//year";
		//String xpath ="//dblp[//inproceedings[//cite]]/article[/author]//year";
		// AD PC following-sibling:: 测试
		//String xpath = "//dblp/article[/title/following-sibling::pages/following-sibling::url/following-sibling::ee]";
		//String xpath ="//dblp/inproceedings/ee/following-sibling::author/following-sibling::url";
		//String xpath="//dblp//article[/author]/ee";
		//String xpath = "//books/book/metadata[/content]/title";
		
		//String xmlfile="test2.xml";
		//String xpath="//A//B";
		
		
		TreeMatch match = new TreeMatch();
		long time1 = System.currentTimeMillis();
		match.initData(xmlfile, xpath);
		long time2 = System.currentTimeMillis();
	    System.out.println("解析时间:"+(time2-time1));
		//System.out.println("最终结果 ：");
		//测试  pc ad *
		//System.out.println(match.Execute("test2.xml", "//A//*"));
		//System.out.println(match.Execute("test2.xml", "//A[/B]/C[/D]/E"));		
		//System.out.println(match.Execute("test2.xml", "//A//C[/D][/E]"));
		//System.out.println(match.Execute("bookstore.xml", "//bookstore/*[/price]"));      
		//System.out.println(match.Execute("bookstore1.xml", "//A/B[/D]/E"));
		//System.out.println(match.Execute("test3.xml", "//A//*"));		
		//测试 pc ad * < !
		//System.out.println(match.Execute("test2.xml", "//A[//C[/D/following-sibling::E]]"));
		//System.out.println(match.Execute("test2.xml", "//A/C[/E]"));
		//System.out.println(match.Execute("test2.xml", "//A/B/following-sibling::C/E"));
	    //System.out.println(match.Execute("test5.xml", "//A/B/following::E"));
	    //System.out.println(match.Execute("test6.xml", "//A[/F[/C[/D/following-sibling::E]]]"));
	    match.Execute(xmlfile, xpath);
	    long time3 = System.currentTimeMillis();
		System.out.println("算法时间:"+(time3-time2));
		System.out.println("返回结果的个数:"+match.Output_result().size());
		
	}	
}
 