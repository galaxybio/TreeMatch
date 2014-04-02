package twig.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
public class EncodingSet {
	private ArrayList<FiveTuple> set;
	
	//该集合属于节点node
	private PatternTreeNode node;
	
	public EncodingSet(PatternTreeNode node){
		set = new ArrayList<FiveTuple>();
		this.node = node;
	}
	
	//判断集合是否为空
	public boolean isEmpty(){
		return set.isEmpty();
	}
	//得到五元组集合
	public ArrayList<FiveTuple> getSet()
	{
		return set;
	}
	//得到五元组集合中的最大元素
	public XmlElement getMaxElement(){
		XmlElement maxElement = null;
		maxElement = this.set.get(0).getXmlElement();
		for(int i = 1;i<this.set.size();i++)
		{
			
			XmlElement elem_i =this.set.get(i).getXmlElement();
			Scheme scheme_i = elem_i.getScheme();
			if(scheme_i.isGreater(maxElement.getScheme()))
				maxElement = elem_i;			   
		}
		return maxElement;
	}

	//将潜在的返回元素放到该集合中的outputlist中
	public void addToOutputList(ArrayList<XmlElement> pelems,XmlElement elem){
		for(int i = 0; i< pelems.size(); ++i){
			XmlElement pelem = pelems.get(i);
			PatternTreeNode pnode = pelem.getPatternTreeNode();
			PatternTreeNode cnode = elem.getPatternTreeNode();
			if(cnode.getPreNode()!=null || cnode.getPostNode()!=null)
			{

				int precount=0,postcount=0;
//				for(int j=0;j<pnode.getChildrenNodes().size();j++)
//				{
//					PatternTreeNode child_j = pnode.getChildrenNodes().get(j);
//					if(child_j==cnode)
//					{
//					}
//				}

				for(int j=0;j<pnode.getChildrenNodes().size();j++)
				{
					PatternTreeNode child_j = pnode.getChildrenNodes().get(j);
					if(child_j==cnode.getPreNode())
					{
						precount =j;
					}
				}
				for(int j=0;j<pnode.getChildrenNodes().size();j++)
				{
					PatternTreeNode child_j = pnode.getChildrenNodes().get(j);
					if(child_j==cnode.getPostNode())
					{
						postcount =j;
					}
				}

				if(cnode.getPreNode()!=null && (cnode.getAxisType()==AxisType.FSIBLING || cnode.getPreNode().getAxisType()==AxisType.PSIBLING
						|| cnode.getAxisType()==AxisType.NOTFSIBLING || cnode.getPreNode().getAxisType()==AxisType.NOTPSIBLING))
				{ 
					if(cnode.getPreNode()!=null &&  pelem.minChild[precount]!=null &&pelem.maxChild[postcount]!=null 
							&& pelem.maxChild[postcount].getParent()!=null && elem.getScheme().isGreater(pelem.minChild[precount].getScheme())
							&&pelem.maxChild[postcount].getParent().isParentOf(elem))
					{

						pelems.get(i).addToOutputList(elem);

					}
				}

				else
				{
					if(cnode.getPreNode()!=null &&  pelem.minChild[precount]!=null && elem.getScheme().isGreater(pelem.minChild[precount].getScheme()))

					{
						pelems.get(i).addToOutputList(elem);       
					}
				}

				if(cnode.getPostNode()!=null && (cnode.getAxisType()==AxisType.PRECEDING || cnode.getPostNode().getAxisType()==AxisType.FSIBLING
						|| cnode.getAxisType()==AxisType.NOTPRECEDING || cnode.getPostNode().getAxisType()==AxisType.NOTFSIBLING))
				{ 	 
					if(cnode.getPostNode()!=null && pelem.maxChild[postcount]!=null && pelem.maxChild[postcount].getScheme().isGreater(elem.getScheme())
							&&pelem.maxChild[postcount].getParent().isParentOf(elem))
					{

						pelems.get(i).addToOutputList(elem);
					}
				}

				else
				{
					if(cnode.getPostNode()!=null && pelem.maxChild[postcount]!=null && pelem.maxChild[postcount].getScheme().isGreater(elem.getScheme()))		
					{

						pelems.get(i).addToOutputList(elem);
					}
				}

			}

			else
			{	
				pelems.get(i).addToOutputList(elem);
			}

		}
	}
	
	//判断两个元素是否满足模式树匹配
	public  boolean satisfyTreePattern(XmlElement eq, XmlElement eqi)
	{		
		if(eqi == null)
		{
			return false;
		}
		PatternTreeNode qiNode = eqi.getPatternTreeNode();
		if(this.node.getLabel() == eqi.getLabel()|| this.node.getLabel().getString().equals("*")||eqi.getLabel().getString().equals("*")) 
		{		
			if((eq.isAncestorOf(eqi)||eq.isParentOf(eqi))||(eqi.isAncestorOf(eq)||eqi.isParentOf(eq)))
				return true;
			else 
				return false;
		}
		else {
			if(node.isParentof(qiNode))
			{
				if(((qiNode.getAxisType()==AxisType.PC ||qiNode.getAxisType()==AxisType.FSIBLING || qiNode.getAxisType() ==AxisType.PSIBLING)  && eq.isParentOf(eqi))
						||((qiNode.getAxisType()==AxisType.AD ||qiNode.getAxisType()==AxisType.FOLLOWING || qiNode.getAxisType() ==AxisType.PRECEDING) && eq.isAncestorOf(eqi)))
					return true;
				else 
					return false;
			}

			else if(node.isAncestorof(qiNode))
			{
				ArrayList<PatternTreeNode> nodes = qiNode.nodeToLeaf(node);
				ArrayList<Label> elemnames = new ArrayList<Label>();

				for(int i=eqi.getScheme().getExtendDewey().size();i>0;i++)
				{
					elemnames.add(eqi.getLabel());
					eqi.getScheme().getExtendDewey().remove(i);
				}
				int index = 0;
				for(int i = 0;i<nodes.size();i++)
				{
					PatternTreeNode node_i = nodes.get(i);
					Label label_i = node_i.getLabel();
					AxisType axistype_i = node_i.getAxisType();
					if(label_i == elemnames.get(index))
					{
						if((axistype_i==AxisType.PC || axistype_i==AxisType.FSIBLING || axistype_i==AxisType.PSIBLING)&& label_i==elemnames.get(index+1))
						{
							index = index+1;
							continue;
						}
						else if (axistype_i==AxisType.AD || axistype_i==AxisType.FOLLOWING|| axistype_i==AxisType.PRECEDING)
						{
							while(index < elemnames.size())
							{
								Label label_next = nodes.get(i+1).getLabel();
								if(label_next==elemnames.get(index))
									break;
								else index++;
							}
							continue;
						}
					}

				}			
				if(index >=elemnames.size())
					return true;
				else 
					return false;
			}//else
		}//else
		return true;
	}
	//更新该集合
	public void updateSet(XmlElement elem,PatternTreeNode fact){
		FiveTuple tuple= new FiveTuple(elem);
		elem.setBitVector(fact);
		//if(fact.getIsNeg())
		//{
		//	elem.setNegElem(fact);
		//}
		//elem.setMinAndMaxChild(fact);
		set.add(tuple);
		if(!node.isRoot() && tuple.isAllBitVevtorOk()){
			updateAncestorSet(tuple);
		}
	}

	//清空该集合，满足输出条件返回输出结果
	public Set<XmlElement> cleanSet(XmlElement elem){
		//存放输出结果
		Set<XmlElement> finalresult = new HashSet<XmlElement>();
		int num=0;
		for(int i = 0;i<set.size();++i)
		{
			if(set.get(i).getXmlElement().issatisfy())
				num++;
		}		
		for(int i=0; i<set.size(); ++i){
			XmlElement elem_i = this.set.get(i).getXmlElement();
			if(elem_i.issatisfy())
			{	
				if(node.isRetrunNode()){	
					if(!elem.getPatternTreeNode().isLeaf()&& elem.issatisfy()|| elem.getPatternTreeNode().isLeaf())
					{
						ArrayList<XmlElement> pelems = elem_i.getMatchElement(node.getNAB());
						node.getNAB().set.addToOutputList(pelems,elem);
					}
				}	
				if(node.isTopBranchingNode()){
					if(1 == num){	

						if(set.get(0).getXmlElement().outputlist.size()!=0)
						{
							finalresult.addAll(set.get(0).getXmlElement().outputlist);
							set.get(0).getXmlElement().outputlist.clear();
						}
					}
					else{
						PatternTreeNode nab = elem_i.getPatternTreeNode().getNAB();
						for(int j = 0;j < nab.set.set.size();j++)
						{
							if(elem_i.outputlist!=null && j!=i)
							{  								
								nab.set.set.get(j).getXmlElement().outputlist.addAll(elem_i.outputlist);
								elem_i.outputlist.clear();
							}
						}
						num--;	
					}
				}
			}

		}
		set.clear();
		return finalresult;
	}

	//更新祖先集合
	private void updateAncestorSet(FiveTuple tuple){
		PatternTreeNode q = tuple.getXmlElement().getPatternTreeNode().getNAB();    
		int count = 0;
		//System.out.println(tuple.getXmlElement().getPatternTreeNode());
		int child_num = q.getChildrenNodes().size();
		for(int i = 0;i<child_num;i++)
		{
			if(q.getChildrenNodes().get(i).getLabel().equals(tuple.getXmlElement().getLabel()))
			{
				count = i;
			}
		}

		int set_num = q.set.set.size();
		for(int i=0; i<set_num; ++i){
			FiveTuple e = q.set.set.get(i);	
			XmlElement elem = e.getXmlElement();
			if(q.set.satisfyTreePattern(elem,tuple.getXmlElement()))
			{				

				elem.outputlist.addAll(tuple.getXmlElement().outputlist);
				if(elem.bitvector.length!=0)
				{
					if(elem.bitvector[count]==0)
					{
						elem.bitvector[count]=1;
						if(!q.isRoot()&&elem.issatisfy())
						{
							updateAncestorSet(q.set.set.get(i));
						}
					}
				}
			}
		}
	}
}