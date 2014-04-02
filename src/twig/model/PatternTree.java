package twig.model;

import java.util.ArrayList;
import java.util.HashMap;

public class PatternTree {
	//该模式树的根节点
	private PatternTreeNode root;
	//模式树中每个节点与各自标号的对应关系
	private HashMap<Integer, PatternTreeNode> nodes = new HashMap<Integer, PatternTreeNode>();
	//该模式树中的所有节点
	private ArrayList<PatternTreeNode> AllNodes = new ArrayList<PatternTreeNode>();
	//模式树中每个节点与各自标签名称的对应关系
	private HashMap<String, PatternTreeNode> nodesHashMap = new HashMap<String, PatternTreeNode>();
	//模式树中所有叶子节点
	private ArrayList<PatternTreeNode> leafnodes = new ArrayList<PatternTreeNode>();
	//返回节点
	private PatternTreeNode returnnode;
	//所有非叶子节点
	private ArrayList<PatternTreeNode> noleafnodes = new ArrayList<PatternTreeNode>();
	public PatternTree(PatternTreeNode root){
		ArrayList<PatternTreeNode>  nodesold = root.getTreeNodes();		
		for(int i=0;i<nodesold.size();i++){
			int index = nodesold.get(i).getIndex();
			nodes.put(index, nodesold.get(i));
			AllNodes.add(nodesold.get(i));
			if(nodesold.get(i).isRetrunNode())
				returnnode=nodesold.get(i);
			if(nodesold.get(i).isLeaf()){
				leafnodes.add(nodesold.get(i));
			}
		}
		noleafnodes = AllNodes;
		noleafnodes.removeAll(leafnodes);
		this.root = root;
	}
	
	public PatternTreeNode getRoot(){
		return root;
	}
	public void setReturnNode(PatternTreeNode node)
	{
		this.returnnode = node;
	}
	public int getSize(){
		return AllNodes.size();
	}

	public PatternTreeNode getReturnNode()
	{
		return returnnode;
	}

	public PatternTreeNode getIndexofTreeNode(int index){
		return nodes.get(index);
	}
	public HashMap<Integer, PatternTreeNode> getPatternTreeNodes(){
		return nodes;
	}
	public ArrayList<PatternTreeNode> getLeafNodes(){
		return leafnodes;
	}
	public ArrayList<PatternTreeNode> getNoLeafNodes()
	{

		return noleafnodes;
	}
	public PatternTreeNode getPatternTreeNode(String label){
		if(!nodesHashMap.containsKey(label)){
			nodesHashMap.put(label, root.getPatternTreeNode(label));
		}
		return nodesHashMap.get(label);
	}


    //设置每个节点的最近祖先节点和最近子孙节点
	public void setNDBAndNAB(){
		for(int i=1;i<=nodes.size();i++){
			nodes.get(i).setNAB();
		}		
	}



	public String toXQuery(String sour){
		String str = "<res>{count(\n";
		char s ='"';
		str = str +"for $"+root.getLabel()+" in doc("+s+sour+s+")//"+root.getLabel()+"\n";	
		PatternTreeNode node = null;
		PatternTreeNode subnode = null;
		String aixs = null;
		for(int i=1;i<=nodes.size();i++){
			node = nodes.get(i);
			for(int j=0;j<node.getDescendantNodes().size();j++){
				subnode = node.getDescendantNodes().get(j);
				if(subnode.getAxisType()== AxisType.PC)   aixs = "/";
				if(subnode.getAxisType()== AxisType.AD)   aixs = "//";
				if(subnode.getAxisType()== AxisType.FSIBLING)   aixs = "/following-sibling::";				
				str = str + "for $"+subnode.getLabel()+" in $"+node.getLabel()+aixs+subnode.getLabel()+"\n";
			}
		}
		for(int i=1;i<=nodes.size();i++){
			node = nodes.get(i);
			for(int j=0;j<node.getDescendantNodes().size();j++){
				subnode = node.getDescendantNodes().get(j);
			}
		}
		str = str + "return <r></r>)\n"+"}</res>";
		return str;
	}
}
