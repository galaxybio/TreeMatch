package twig.model;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import  org.xml.sax.helpers.AttributesImpl;

public class XmlElement {
	private Label label;
	private PatternTreeNode node =null;
	private Scheme scheme;
	private XmlElement parent;
	public int[] bitvector;
	public XmlElement[] minChild;
	public XmlElement[] maxChild;

	public boolean isnegelem = false;

	public boolean ishasneg =false; 
	private boolean isleafelem = false;
	public ArrayList<XmlElement> outputlist;
	private int childsize;
	private ArrayList<XmlElement>  ancestorandselfElements;
	private ArrayList<XmlElement>  childrenElements;
	private ArrayList<XmlElement> descendentsElements;
	public Scheme getScheme(){
		return this.scheme;
	}
	public ArrayList<Integer> getExtendDewey(){
		return this.scheme.getExtendDewey();
	}

	private String content;

	public Label getLabel() {
		return label;
	}

	public int getChildSize(){
		return this.childsize;
	}

	public void addChildSize(){
		this.childsize++;
	}

	public String getContent() {
		return content;
	}


	public void setIsleafElem()
	{
		if(this.getChildSize()==0)
			this.isleafelem = true;
	}
	public boolean getIsleafElem()
	{
		return this.isleafelem;
	}
	public void setContent(String content) {
		this.content = content;
	}


	public void setParent(XmlElement pelem)
	{
		this.parent = pelem;
	}
//	public void setNegElem(PatternTreeNode node)
//	{
//		if(node.getAxisType()==AxisType.NOTAD || node.getAxisType() ==AxisType.NOTFOLLOWING|| node.getAxisType() ==AxisType.NOTPRECEDING)
//		{
//			if(this.getDescendentsElements().contains(node.buffer.getCurrent()))
//			{
//				this.ishasneg = true;
//			}
//		}
//		else
//		{
//			if(this.getChildren().contains(node.buffer.getCurrent()))
//			{
//				this.ishasneg = true;
//			}
//		}
//	}
	public XmlElement getParent()
	{
		return this.parent;
	}
	public String toString() {
		String starttag = new String("<" + this.label.getString() + ">");
		String endtag = new String("</" + this.label.getString() + ">");

		String regioncode = new String("(" + scheme.toString() + ")");
		return starttag + regioncode + endtag+"\n";
	}
	public XmlElement(String label,XmlElement parent,ArrayList<XmlElement> ancestor,Attributes attributes,Scheme scheme,PatternTreeNode root) {
		this.label = Label.createLabel(label);
		this.parent=parent;
		if(root.getPatternTreeNode(label)!=null)
		{
			this.node = root.getPatternTreeNode(label);
			int num = node.getDescendantNodes().size();
			bitvector= new int[num];
			minChild = new XmlElement[num];
			maxChild = new XmlElement[num];
			for(int i=0; i < num; ++i){
				bitvector[i] = 0;
				minChild[i] = null;
				maxChild[i] = null;
			}	
		}

		this.scheme = scheme;

		this.childrenElements = new ArrayList<XmlElement>();
		this.descendentsElements = new ArrayList<XmlElement>();
		outputlist = new ArrayList<XmlElement>();
		new AttributesImpl(attributes);
		if(null != ancestor){
			this.ancestorandselfElements = new ArrayList<XmlElement>(ancestor);
		}else{
			this.ancestorandselfElements = new ArrayList<XmlElement>();
		}
		this.ancestorandselfElements.add(this);
		this.content = new String();

	}
	public XmlElement(String label,XmlElement parent,ArrayList<XmlElement> ancestor,Attributes attributes,Scheme scheme,PatternTreeNode root,String star) {
		this.label = Label.createLabel(label);
		this.parent = parent;
		this.node = root.getPatternTreeNode(star);
		this.scheme = scheme;
		this.childrenElements = new ArrayList<XmlElement>();
		this.descendentsElements = new ArrayList<XmlElement>();
		int num = node.getDescendantNodes().size();

		bitvector= new int[num];
		minChild = new XmlElement[num];
		maxChild = new XmlElement[num];
		for(int i=0; i < num; ++i){
			bitvector[i] = 0;
			minChild[i] = null;
			maxChild[i] = null;
		}	
		outputlist = new ArrayList<XmlElement>();
		new AttributesImpl(attributes);
		if(null != ancestor){
			this.ancestorandselfElements = new ArrayList<XmlElement>(ancestor);
		}else{
			this.ancestorandselfElements = new ArrayList<XmlElement>();
		}
		this.ancestorandselfElements.add(this);
		this.content = new String();		
	}	
	public boolean issatisfy()
	{
		boolean flag = true;
		//if(!this.ishasneg)
		//{ 
		if(this.isAllBitVevtorOk())
		{		      
			PatternTreeNode node = this.getPatternTreeNode();
			int childnodenums =node.getChildrenNodes().size(); 
			int prenum=0,postnum=0;




			for(int j=0;j<childnodenums;j++)
			{
				if(node.getChildrenNodes().get(j).getPostNode()!=null || node.getChildrenNodes().get(j).getPreNode()!=null)
				{

					XmlElement minelem = this.minChild[j];
					//	if(minelem == null)
					//	{
					//	flag = false;
					//	break;
					//}
					XmlElement maxelem = this.maxChild[j];
					//	if(maxelem == null)
					//	{

					//	flag = false;
					//	break;
					//	}


					if(minelem!=null && minelem.getPatternTreeNode().getPreNode()!=null)
					{
						for(int k = 0;k<childnodenums;k++)
						{
							if(node.getChildrenNodes().get(k).getLabel().equals(minelem.getPatternTreeNode().getPreNode().getLabel()))
								prenum = k;
						}


						if(this.minChild[prenum]!=null)
						{
							if(!minelem.getScheme().isGreater(this.minChild[prenum].getScheme()))
							{

								flag = false;
								break;
							}
						}
						//else
							//{

							//	flag = false;
							//	break;
						//}
					}
					if(maxelem!=null && maxelem.getPatternTreeNode().getPostNode()!=null)
					{
						for(int k = 0;k<childnodenums;k++)
						{
							if(node.getChildrenNodes().get(k).getLabel().equals(minelem.getPatternTreeNode().getPostNode().getLabel()))
								postnum = k;
						}



						if(this.maxChild[postnum]!=null)
						{
							if(maxelem.getScheme().isGreater(this.maxChild[postnum].getScheme()))
							{

								flag = false;
								break;
							}
						}
						//else
							//{

							//	flag = false;
						//	break;
						//}
					}

				}
				else
					flag = true;

			}


		}
		else
		{

			flag = false;
		}
		//}
		//else
		//{
		//		flag = false;
		//	}

		return flag;

	}	

	public ArrayList<XmlElement> getDescendentsElements()
	{

		int child_num = this.getChildSize();
		for(int i = 0;i<child_num;i++)
		{
			XmlElement elem = this.getChildren().get(i);
			this.descendentsElements.add(elem);
			if(!elem.isleafelem)
			{
				elem.getDescendentsElements();
			}  	             
		}
		return this.descendentsElements;
	}


	public boolean isAllBitVevtorOk(){
		int num = this.bitvector.length;
		for(int i = 0; i< num;i++)
		{
			if(0 == this.bitvector[i])
				return false;
		}
		return true;
	}
	public void setPatternTreeNode(PatternTreeNode node){
		this.node = node;
	}

	public PatternTreeNode getPatternTreeNode(){
		return this.node;
	}

	public void addToOutputList(XmlElement elem){

		outputlist.add(elem);
	}	
	public ArrayList<XmlElement> getOutputList()
	{
		return outputlist;
	}
//	public void setMinAndMaxChild(PatternTreeNode node)
//	{		
//		//以该元素对应的节点为根的子树节点
//		ArrayList<PatternTreeNode> nodes = this.getPatternTreeNode().getTreeNodes();
//		ArrayList<PatternTreeNode> leafnodes = new ArrayList<PatternTreeNode>(); 
//		//该子树的叶子节点集合
//		for(int i = 0;i<nodes.size();i++)
//		{
//			if(nodes.get(i).isLeaf())
//			{
//				leafnodes.add(nodes.get(i));
//			}
//		}
//
//		for(int j = 0;j<leafnodes.size();j++)
//		{
//			node = leafnodes.get(j);	
//			XmlElement  elem = null;
//			if(this.getChildren().contains(node.buffer.getCurrent()))
//			{
//				elem=node.buffer.getCurrent();
//			}   
//			else
//			{
//				for(int i = 0;i<this.getChildSize();i++)
//				{
//					if(this.getChildren().get(i).isAncestorOf(node.buffer.getCurrent()))
//						elem = this.getChildren().get(i);
//				}
//			}	//得到要设置的elem
//
//			if(elem!=null)
//			{
//				node = elem.getPatternTreeNode();
//				ArrayList<PatternTreeNode> childnodes = this.getPatternTreeNode().getChildrenNodes();
//				if(node.getPreNode()!=null)
//				{    	
//					int i,count=0,thiscount=0;
//					for(i =0;i<childnodes.size();i++)
//					{
//						if(childnodes.get(i)==node.getPreNode())
//						{
//							count = i;
//							break;
//						}
//
//					}
//					for(i =0;i<childnodes.size();i++)
//					{
//						if(childnodes.get(i)==node)
//						{
//							thiscount = i;
//							break;
//						}
//
//					}
//					boolean ok = false;
//
//					if(this.minChild[count]!=null)
//					{
//						if(this.minChild[thiscount]!=null)
//						{
//							if(this.minChild[thiscount].scheme.isGreater(elem.scheme))
//								this.minChild[thiscount]=elem;
//						}
//						else
//							this.minChild[thiscount]=elem;
//					}
//					else
//					{
//
//						ArrayList<XmlElement> preelems = new ArrayList<XmlElement>();
//						for(int k=0;k<this.getChildren().size();k++)
//						{
//							if(this.getChildren().get(k).getLabel().equals(node.getPreNode().getLabel()))
//								preelems.add(this.getChildren().get(k));
//						}
//
//
//						for(int k = 0 ;k<preelems.size();k++)
//						{
//							if(elem.scheme.isGreater(preelems.get(k).scheme))
//							{
//								ok=true;
//								break;
//							}
//
//						}
//
//						if(ok)
//						{
//
//							if(this.minChild[thiscount]!=null)
//							{
//								if(this.minChild[thiscount].scheme.isGreater(elem.scheme))
//									this.minChild[thiscount]=elem;
//							}
//							else
//								this.minChild[thiscount]=elem;
//						}
//					}
//
//
//					if(this.maxChild[count]!=null)
//					{
//						if(this.maxChild[thiscount]!=null)
//						{
//							if(!this.maxChild[thiscount].scheme.isGreater(elem.scheme))
//								this.maxChild[thiscount]=elem;
//						}
//						else
//							this.maxChild[thiscount]=elem;
//					}
//
//					else if(ok)
//					{
//						if(this.maxChild[thiscount]!=null)
//						{
//							if(!this.maxChild[thiscount].scheme.isGreater(elem.scheme))
//								this.maxChild[thiscount]=elem;
//						}
//						else
//							this.maxChild[thiscount]=elem;
//					}
//					else
//					{  
//						if(this.getPatternTreeNode().getChildrenNodes().get(count).buffer.getCurrent()!=null && node.buffer.getCurrent()!=null)
//						{
//							if(this.getPatternTreeNode().getChildrenNodes().get(count).buffer.getCurrent().scheme.isGreater(node.buffer.getCurrent().scheme))
//							{
//								node.buffer.advance();
//								node.isbuffer=true;
//							}
//						}
//
//					}
//
//				}
//
//
//				if(node.getPostNode()!=null)
//				{
//					int i,count=0,postcount=0;
//					for(i =0;i<childnodes.size();i++)
//					{
//						if(childnodes.get(i)==node)
//						{
//							count = i;
//							break;
//						}
//
//					}
//
//					for(i =0;i<childnodes.size();i++)
//					{
//						if(childnodes.get(i)==node.getPostNode())
//						{
//							postcount = i;
//							break;
//						}
//
//					}
//					if(this.minChild[count]!=null)
//					{
//						if(this.minChild[count].scheme.isGreater(elem.scheme))
//							this.minChild[count]=elem;
//					}
//					else
//						this.minChild[count]=elem;
//					if(this.maxChild[count]!=null && this.maxChild[postcount]!=null)
//					{						
//						if(!this.maxChild[count].scheme.isGreater(elem.scheme) && !elem.scheme.isGreater(this.maxChild[postcount].scheme))
//							this.maxChild[count]=elem;
//					}
//					else
//						this.maxChild[count]=elem;
//				}		
//			}
//		}
//	}
	public void setBitVector(PatternTreeNode node){
		ArrayList<XmlElement> currentElements = new ArrayList<XmlElement>();
		int num= this.getPatternTreeNode().getTreeNodes().size();
		for(int i = 0 ;i<num;i++)
		{
			PatternTreeNode node_i = this.getPatternTreeNode().getTreeNodes().get(i);
			if(node.isLeaf())
				currentElements.add(node_i.buffer.getCurrent());
		}

		int child_num = this.getPatternTreeNode().getChildrenNodes().size();
		for(int i = 0;i<child_num;i++)
		{
			for(int k =0;k<num;k++)
			{
				PatternTreeNode thisnode = this.getPatternTreeNode().getTreeNodes().get(k);
				int num_current = currentElements.size();
				for(int j=0;j<num_current;j++)
				{
					XmlElement elem_j = currentElements.get(j);
					if(thisnode!=null && elem_j!=null && thisnode.getLabel().equals(elem_j.getLabel()))
					{
						if(elem_j!=null) {
							if( this.getPatternTreeNode().set.satisfyTreePattern(this,elem_j))
							{ 

								this.bitvector[i]=1;
								break;
							}					
						}	
					}
				}
			}
		}
	}


	public ArrayList<XmlElement>  getMatchElement(PatternTreeNode node){

		ArrayList<XmlElement> result = new ArrayList<XmlElement>();

		int anc_num = this.ancestorandselfElements.size();
		for(int i =0;i<anc_num;i++)
		{
			XmlElement anc_elem_i = this.ancestorandselfElements.get(i);
			if(anc_elem_i.getLabel().equals(node.getLabel()))
				result.add(anc_elem_i);
		}

		for(int i = 0 ;i< result.size();i++)
		{
			XmlElement xmlelem = this;
			XmlElement res_i = result.get(i);
			if(res_i.isParentOf(xmlelem))	
			{
				if(!result.get(i).getPatternTreeNode().set.satisfyTreePattern(this,res_i))
				{
					result.remove(i);
					i--;
				}
			}

			else
			{
				boolean flag = true;
				XmlElement preelem = xmlelem;
				while(res_i!=xmlelem)
				{
					if(!xmlelem.getLabel().equals(xmlelem.getParent().getLabel()))
					{
						if(!res_i.getPatternTreeNode().set.satisfyTreePattern(xmlelem.getParent(),xmlelem))
						{
							flag = false;
							break;
						}
					}
					else
					{
						PatternTreeNode pre_node = preelem.getPatternTreeNode();
						
						if(pre_node!=null){
							AxisType pre_axistype = pre_node.getAxisType();
							if(pre_axistype == AxisType.PC || pre_axistype  == AxisType.FSIBLING ||
									pre_axistype  == AxisType.PSIBLING || pre_axistype  == AxisType.NOTFSIBLING||
											pre_axistype  == AxisType.NOTPSIBLING)
							{
								flag = false;
								break;
							}
						}
					}
					preelem = xmlelem;
					xmlelem = xmlelem.getParent();
				}
				if(flag == false)
				{
					result.remove(i);
					i--;
				}
				else
					continue;
			}
		}
		return result;
	}
	public void addChildren(XmlElement element)
	{
		childrenElements.add(element);
	}

	public ArrayList<XmlElement> getChildren()
	{
		return childrenElements;
	}
	public boolean isAncestorOf(XmlElement elem){
		if(elem == null)
			return false;
		else
			return scheme.isAncestorOf(elem.getScheme());
	}

	public boolean isParentOf(XmlElement elem){
		return scheme.isParentOf(elem.getScheme());
	}	



	public boolean satisfiy(PatternTreeNode node) {
		XmlElement elem = this;
		ArrayList<PatternTreeNode> nodes = this.getPatternTreeNode().nodeToLeaf(node);
		int node_num = nodes.size();
		if (node_num > scheme.getExtendDewey(). size())
			return false;
		ArrayList<Integer> encodeList = scheme.getExtendDewey();
		int level = encodeList.size();
		int index = node_num - 1;

		Scheme schm = new Scheme();
		for (Integer i : encodeList) {
			schm.getExtendDewey().add(i);
		}

		int encode_num = encodeList.size();
		for (int i = encode_num; i > 0; i--) {
			String str1 = nodes.get(index).getLabel().getString();
			String str2 = elem.getLabel().getString();

			if(str1.equals("*"))
				str1=str2;

			int schm_num = schm.getExtendDewey().size();
			if (str1.equals(str2)) {
				
				if (index < nodes.size() - 1) {
					AxisType axistype = nodes.get(index + 1).getAxisType();
					if (axistype == AxisType.PC || axistype == AxisType.FSIBLING ||
							axistype == AxisType.PSIBLING || axistype == AxisType.NOTFSIBLING||
									axistype == AxisType.NOTPSIBLING) {
						if (level - i != 1) {//
							i++;
							index = traceBack(nodes, index);
							if (index == -1 || index > schm_num)
								return false;
						} 
						else {
							level = i;
							index--;
							schm.getExtendDewey().remove(schm_num - 1);
							elem=elem.getParent();
						}

					}
					else if (axistype == AxisType.AD || axistype == AxisType.FOLLOWING 
							|| axistype == AxisType.PRECEDING || 
									axistype == AxisType.NOTFOLLOWING||axistype == AxisType.NOTPRECEDING) {
						level = i;
						index--;
						schm.getExtendDewey().remove(schm_num - 1);
						elem=elem.getParent();
					}
				}

				else {
					level = i;
					index--;
					schm.getExtendDewey().remove(schm_num - 1);
					elem=elem.getParent();

				}
			} 

			else {
				AxisType axistype = nodes.get(index + 1).getAxisType();
				if (index < node_num - 1 && (axistype == AxisType.PC || axistype == AxisType.FSIBLING||
						axistype == AxisType.PSIBLING || axistype == AxisType.NOTFSIBLING || 
								axistype == AxisType.NOTPSIBLING)) {
					i++;
					index = traceBack(nodes, index);
					if (index == -1 || index > schm_num)
						return false;
				} else {
					schm.getExtendDewey().remove(schm_num - 1);
					elem=elem.getParent();
				}
			}

			if (index < 0)
				return true;
		}

		return index == 0;
	}

	private int traceBack(ArrayList<PatternTreeNode> nodeList, int current) {
		int node_num = nodeList.size();
		for (int i = current; i < node_num - 1; i++) {
			AxisType axistype = nodeList.get(i + 1).getAxisType();
			if (axistype == AxisType.AD ||axistype == AxisType.FOLLOWING || 
					axistype == AxisType.PRECEDING || axistype == AxisType.NOTFOLLOWING ||
							axistype == AxisType.NOTPRECEDING) {
				return i;
			}
		}
		return -1;
	}


}
