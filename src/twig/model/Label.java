package twig.model;

import java.util.HashSet;

public class Label {

	//标签名称
	private String str;
	//标签共享池
	private static HashSet<Label> labelpool = new HashSet<Label>();

	public Label(String str)
	{
		this.str = str;
	}
	public String getString()
	{
		return this.str;
	}
    //创建标签，如果共享池中有该标签名称，直接从中取，如果没有，则新创建一个标签
	public static Label createLabel(String str)
	{
		for(Label l:labelpool)
		{
			if(l.str.equals(str))
				return l;			
		}
		Label l = new Label(str);
		labelpool.add(l);
		return l;
	}
}
