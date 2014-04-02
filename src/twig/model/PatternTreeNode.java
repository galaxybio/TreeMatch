package twig.model;


import java.util.ArrayList;
import java.util.HashMap;

public class PatternTreeNode {
	//节点的标签名称
	private Label label;
	//节点的轴类型
	private AxisType axisType;
	//子孙节点
	private ArrayList<PatternTreeNode> descendantnodes;
	//孩子节点
	private ArrayList<PatternTreeNode> childrenNodes;
	//父亲节点
	private PatternTreeNode parent = null;
	//节点标号
	private int index;
	//最近祖先分支节点
	private PatternTreeNode nab = null;
	//最近子孙节点

	//该节点的标签流
	public LabelStream  buffer;
	//分支节点集合
	public EncodingSet  set;    
	public boolean flag;
	//是否最高分支节点
	private boolean istopnode;
	//前序节点
	private PatternTreeNode preNode;
	//后序节点
	private PatternTreeNode postNode;
	private boolean negflag = false;
	public boolean isbuffer = false;
	
	public void setTopBranchNode(boolean flag){
		this.istopnode = flag;
	}
	public boolean isTopBranchingNode(){
		return istopnode;
	}
	public void setIsNeg()
	{
		this.negflag=true;
	}


	public boolean getIsNeg()
	{
		return this.negflag;
	}
	public void setReturnNode()
	{
		flag = true;
	}
	public boolean isRetrunNode(){
		return flag;
	}

	public boolean isRoot(){//the index of root node is 1
		return index == 1? true: false;
	}

	public void setNAB(){
		if("doc" == this.parent.label.getString()){
			nab = this;
		}else{
			PatternTreeNode ancestor = parent;
			while(1 == ancestor.childrenNodes.size() && "doc" != ancestor.label.getString()){
				ancestor = ancestor.parent;
			}
			if(ancestor.getLabel().getString().equals("doc"))
				nab = this;
			else
			{
				if(ancestor.childrenNodes.size()>=2)
					nab = ancestor;
			}

		}
	}
	public PatternTreeNode  getNAB(){
		return nab;
	}

	public ArrayList<PatternTreeNode> getNDB(){
		ArrayList<PatternTreeNode> ndb = new ArrayList<PatternTreeNode>();
		int descendant_num = descendantnodes.size();
		for(int i=0; i < descendant_num; ++i){
			PatternTreeNode descendant = descendantnodes.get(i);
			while(1 == descendant.descendantnodes.size() && true != descendant.isLeaf()){
				descendant = descendant.descendantnodes.get(0);
			}
			ndb.add(descendant);
		}
		return ndb;
	}
	//是否为叶子节点
	public boolean isLeaf(){
		if(0 == descendantnodes.size()){
			return true;
		}else {
			return false;
		}
	}
	//是否为分支节点
	public boolean isBranching(){
		if(2 <= this.childrenNodes.size()){
			return true;
		}else {
			return false;
		}
	}
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ArrayList<PatternTreeNode> getDescendantNodes() {
		return descendantnodes;
	}


	public Label getLabel() {
		return label;
	}

	public AxisType getAxisType() {
		return axisType;
	}


	public PatternTreeNode getParent() {
		return parent;
	}


	
	public ArrayList<PatternTreeNode>  getTreeNodes(){
		ArrayList<PatternTreeNode> list = new ArrayList<PatternTreeNode>();
		int descendant_num = descendantnodes.size();
		for(int i=0;i<descendant_num;i++){
			list.addAll(descendantnodes.get(i).getTreeNodes());
		}
		list.add(this);
		return list;
	}
	public void setLabel(String label) {
		this.label = Label.createLabel(label);
	}

	public void setAxisType(AxisType axisType) {
		this.axisType = axisType;
	}

	public void setParent(PatternTreeNode parent) {
		this.parent = parent;
	}


	public void addPatternTreeNode(PatternTreeNode node) {
		descendantnodes.add(node);

	}
	public void addChildrenNode(PatternTreeNode node)
	{
		this.childrenNodes.add(node);
	}

	public ArrayList<PatternTreeNode> getChildrenNodes()
	{
		return childrenNodes;
	}

	public boolean isParentof(PatternTreeNode node)
	{
		if(this.childrenNodes.contains(node))
			return true;
		else {
			return false;
		}
	}


	public boolean isAncestorof(PatternTreeNode node)
	{
		if(this.descendantnodes.contains(node))
			return true;
		else 
			return false;
	}

	public  ArrayList<PatternTreeNode> nodeToLeaf(PatternTreeNode node) {
		ArrayList<PatternTreeNode> nodes = new ArrayList<PatternTreeNode>();
		if (this.getLabel()!= node.getParent().getLabel()) {
			nodes.addAll(this.getParent().nodeToLeaf(node));
			nodes.add(this);
		}

		return nodes;
	}

	public PatternTreeNode() {
		descendantnodes = new ArrayList<PatternTreeNode>();
		childrenNodes = new ArrayList<PatternTreeNode>();
		buffer = new LabelStream(this);
		set = new EncodingSet(this);
		flag= false;
		preNode=null;
		postNode = null;
	}


	public void setPreNode(PatternTreeNode prenode)
	{
		this.preNode=prenode;

	}
	public PatternTreeNode getPreNode()
	{
		return preNode;
	}


	public void setPostNode(PatternTreeNode postnode)
	{
		this.postNode=postnode;
	}
	public PatternTreeNode getPostNode()
	{
		return postNode;
	}
	public String toString() {
		String str = toSelf();
		str = str + "  [ ";
		for (int i = 0; i < descendantnodes.size(); i++) {
			str = str + descendantnodes.get(i).toSelf() + ", ";
		}
		str = str + " ] ";
		return str + '\n';
	}

	public String toSelf() {
		String axis;
		if (this.axisType == AxisType.PC)
			axis = "/";
		else
		{
			if (this.axisType == AxisType.AD)
				axis = "//";
			else
			{
				if (this.axisType == AxisType.NOTPC)
					axis = "!/";
				else{
					if (this.axisType == AxisType.NOTAD)
						axis = "!//";
					else{


						if (this.axisType == AxisType.FSIBLING)
							axis = "<";
						else{
							if(this.axisType == AxisType.FOLLOWING)
								axis = "<<";
							else
							{
								if(this.axisType == AxisType.NOTFSIBLING)
									axis = "!<";
								else
								{
									if(this.axisType == AxisType.NOTFOLLOWING)
										axis = "!<<";
									else
									{
										if (this.axisType == AxisType.PSIBLING)
											axis = ">";
										else
										{
											if(this.axisType == AxisType.PRECEDING)
												axis = ">>";
											else{
												if(this.axisType == AxisType.NOTPSIBLING)
													axis = "!>";
												else
													axis = "!>>";
											}

										}

									}

								}

							}

						}

					}
				}
			}
		}
		boolean flag = isLeaf();
		String str = " axistype="+ axis + " labelname=" + label.getString() + " index="+ index + "" +
				" Isleaf="+flag + " IsreturnNode="+
				this.isRetrunNode()+ " isNegNode=" + this.getIsNeg();
		if(this.getPreNode()!=null)
		{
			str = str +" preNode=[";
			str+=this.getPreNode().getLabel()+"]";
		} 
		if(this.getPostNode()!=null)
		{
			str = str +" postNode=[";
			str+=this.getPostNode().getLabel()+"]";
		}
		return str;
	}

	public String toStringDesSibling() {
		String str = toString();
		int descendant_num = descendantnodes.size(); 
		for (int i = 0; i < descendant_num; i++) {
			str = str + descendantnodes.get(i).toStringDesSibling();
		}

		return str;
	}
	public String toStringParentSibling() {
		String str = toSelf();
		if (parent != null)
			str = str + " parent :[ " + parent.toSelf() + " ]";
		str = str + '\n';
		for (int i = 0; i < descendantnodes.size(); i++) {
			str = str + descendantnodes.get(i).toStringParentSibling();
		}

		return str;
	}
    
	public PatternTreeNode getPatternTreeNode(String label){
		if(this.label.getString().equals(label))
			return this;
		else{
			PatternTreeNode node;
			int descendant_num = this.descendantnodes.size();
			for(int i=0;i<descendant_num ;i++){
				node = descendantnodes.get(i).getPatternTreeNode(label);
				if(node!=null)
					return node;
			}
			return null;
		}
	}


	private String subpath = new String();
	public String getSubpath() {
		return subpath;
	}
	public void setSubpath(String subpath) {
		this.subpath = this.subpath+"["+subpath+"]";
	}
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	public void addAttribute(String key,Object value){
		attributes.put(key, value);
	}
	public Object getAttribute(String key){
		return attributes.get(key);
	}
}
