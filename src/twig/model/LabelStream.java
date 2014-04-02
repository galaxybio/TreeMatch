package twig.model;

import java.util.ArrayList;

public class LabelStream {
	//标签流元素
	private ArrayList<XmlElement> buffer;
	//标签流中对应元素的标号
	private int index;

	public LabelStream(PatternTreeNode node){
		this.index = 0;
		buffer = new ArrayList<XmlElement>();
	}
    
	//向该标签流中添加元素
	public void addXmlElement(XmlElement elem){
		buffer.add(elem);
	}

	//判断该标签中当前元素到根元素是否满足树模式
	public void locateMatchLabel(PatternTreeNode root){
		while(true)
		{
			if(buffer.size()==0 || this.getCurrent()==null || this.getCurrent().satisfiy(root))
				break;
			else
				this.advance();
		}
		return;
	}

	//取标签流中下一个元素
	public void advance(){
		index++;
	}
	//判断该标签流是否为空
	public boolean isEmpty(){
		if(index < buffer.size()){
			return false;
		}else{
			return true;
		}
	}

	//得到该标签流中的所有元素
	public ArrayList<XmlElement> getBuffer()
	{
		return buffer;
	}

	//得到标签流中的当前元素
	public XmlElement getCurrent(){

		if(index < buffer.size()){
			return buffer.get(index);			
		}else{
			return null;
		}
	}
}
