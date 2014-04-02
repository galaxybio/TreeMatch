package twig.model;


public class FiveTuple {
	//该五元组的分支节点对应的元素
	private XmlElement elem;
	public FiveTuple(XmlElement elem){
		this.elem = elem;		
	}
	//判断bitvector值是否都为1,即判断是否满足子树查询
	public boolean isAllBitVevtorOk(){

		for(int i = 0; i< this.elem.bitvector.length;i++)
		{
			if(0 == this.elem.bitvector[i])
				return false;
		}
		return true;
	}
	public XmlElement getXmlElement(){
		return elem;
	}
	//public void update
}
