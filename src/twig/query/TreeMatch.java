package twig.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import twig.model.PatternTree;
import twig.model.PatternTreeNode;
import twig.model.XmlElement;
import twig.model.FiveTuple;
import twig.parse.xml.XmlParser;

import twig.parse.xpath.XpathParser;

public class TreeMatch {
	private PatternTree tree;
	private ArrayList<XmlElement> elements;
	private PatternTreeNode root;
	private boolean isfinalresult = true;
	private PatternTreeNode returnNode;
	private PatternTreeNode topbranchingnode;
	private ArrayList<PatternTreeNode> leafnodes;
	private PatternTreeNode author,sup;
	// 存放最终结果
	Set<XmlElement> finalresult = new HashSet<XmlElement>();
	public TreeMatch() {

	}

	// 主函数
	public void Execute(String xmlFile, String XPath) throws Exception {
		for (int i = 0; i < leafnodes.size(); ++i) {
			leafnodes.get(i).buffer.locateMatchLabel(root);
		}
		System.out
				.println("------------------------算法开始-----------------------");
		while (!end(tree)) {
			// 返回节点既不是分支节点，也不是叶子节点的情况
			if (!tree.getReturnNode().isLeaf()&& !tree.getReturnNode().isBranching()) {
				tree.setReturnNode(topbranchingnode);
				topbranchingnode.setReturnNode();
				this.isfinalresult = false;
			}

			// getNext入口
			PatternTreeNode fact = getNext_inproceedings(topbranchingnode);
			//PatternTreeNode fact = getNext(topbranchingnode);
			if (fact.isRetrunNode()) {
				ArrayList<XmlElement> pelems = this.MB(fact, fact.getNAB());
				fact.getNAB().set.addToOutputList(pelems,
						fact.buffer.getCurrent());
			} else {
				PatternTreeNode fact1 = fact;
				boolean flag = true;
				while (fact1.isBranching() || fact1.isLeaf()) {
					fact1 = fact1.getParent();

					if (fact1 == this.topbranchingnode) {
						flag = false;
						break;
					}
				}
				if (flag == true) {

					if (fact1 != this.topbranchingnode
							&& fact.getNAB().isAncestorof(fact1)) {
						ArrayList<XmlElement> fact1elems = new ArrayList<XmlElement>();
						fact1elems = MB(fact, fact1);
						for (int i = 0; i < fact1elems.size(); i++) {
							if (!fact1elems.get(i).satisfiy(fact.getNAB())
									|| !fact.getNAB().set.satisfyTreePattern(
											fact1elems.get(i),
											fact.buffer.getCurrent())) {
								fact1elems.remove(i);
								i--;
							} else {
								ArrayList<XmlElement> pelems = this.MB(fact,
										fact.getNAB());
								fact.getNAB().set.addToOutputList(pelems,
										fact1elems.get(i));
							}
						}

					}
				}
			}

			// 如果fact节点的标签流未到达表尾
			if (fact.buffer.getCurrent() != null) {
				// getmatchelems为fact节点匹配的父亲元素集合
				ArrayList<XmlElement> getmatchelems = fact.buffer.getCurrent()
						.getMatchElement(fact.getNAB());
				for (int i = 0; i < getmatchelems.size(); i++) {
					if (fact.getNAB().set.satisfyTreePattern(
							fact.buffer.getCurrent(), getmatchelems.get(i))) {
						if (i == 0) {
							finalresult.addAll(fact.getNAB().set
									.cleanSet(getmatchelems.get(0)));
						}
						fact.getNAB().set.updateSet(getmatchelems.get(i), fact);
					}
				}

			}
			// 判断fact的标签流是否向前移动
			if (fact.isbuffer == true) {
				fact.isbuffer = false;
			} else {
				fact.buffer.advance();
			}

			fact.buffer.locateMatchLabel(root);
		}
		System.out
				.println("------------------------算法结束------------------------");
	}

	// 输出结果集合
	public Set<XmlElement> Output_result() {
		finalresult.addAll(emptyAllSets(root));
		if (!isfinalresult) {
			ArrayList<XmlElement> tempresult = new ArrayList<XmlElement>(
					finalresult);
			finalresult.clear();
			ArrayList<XmlElement> resultlist = this.returnNode.buffer
					.getBuffer();

			for (int i = 0; i < tempresult.size(); i++) {
				for (int j = 0; j < resultlist.size(); j++) {
					if (returnNode.set.satisfyTreePattern(resultlist.get(j),
							tempresult.get(i))) {
						finalresult.add(resultlist.get(j));
					}
				}

			}
		}
		return finalresult;
	}

	// 解析文档，准备工作
	public void initData(String xmlFile, String XPath) throws Exception {
		XpathParser xpathParser = new XpathParser();
		root = xpathParser.parseXpath(XPath); // 解析得到根节点
		tree = new PatternTree(root); // 构造模式树
//		for(int i = 0;i<tree.getSize();i++){
//			
//			System.out.println(tree.getIndexofTreeNode(i));
//			tree.getIndexofTreeNode(i).setTreeNodes();
//		}
		tree.setNDBAndNAB();
		author = root.getPatternTreeNode("author");
		sup = root.getPatternTreeNode("sup");
		XmlParser xmlParser = new XmlParser(root);
		xmlParser.execute(xmlFile); // 解析文档树
		elements = xmlParser.getXmlElements(); // 得到文档树的所有元素
		topbranchingnode = tree.getRoot().getDescendantNodes().size() != 1 ? tree
				.getRoot() : tree.getRoot().getNDB().get(0);
		topbranchingnode.setTopBranchNode(true);
		this.returnNode = tree.getReturnNode();
		leafnodes = tree.getLeafNodes();
		

		System.out.println("文档树元素个数:" + elements.size());
		System.out.println("最高分支节点:"
				+ this.topbranchingnode.getLabel().getString());
		System.out.println("返回节点:" + this.returnNode.getLabel().getString());
	}

	// getNext原始函数
	public PatternTreeNode getNext(PatternTreeNode top) {
		if (top.isLeaf()) {
			return top;
		} else {
			ArrayList<XmlElement> ei = new ArrayList<XmlElement>();
			ArrayList<PatternTreeNode> fList = new ArrayList<PatternTreeNode>();
			ArrayList<XmlElement> mb = new ArrayList<XmlElement>();
			PatternTreeNode max = null;
			PatternTreeNode min = null;

			for (int i = 0; i < top.getNDB().size(); ++i) {
				PatternTreeNode ni = top.getNDB().get(i);
				PatternTreeNode fi = getNext(ni);
				if (fi != null) {
					fList.add(fi);
				}
				if (ni.isBranching() && ni.set.isEmpty()) {
					return fi;
				} else {
					ei.add(getE(ni));
				}
			}

			// 求最大节点
			if (ei.size() == 0) {
				max = null;
			} else {
				max = this.Max(ei);
			}
			
			//如果max为空，则返回fList中的第一个节点，该节点不能为返回节点标签流未到达表尾，如果fList为空，则返回空
			if (max == null) {
				return MaxIsNull_ReturnNode(fList);
			}
			
			else {
				for (int i = 0; i < top.getNDB().size(); ++i) {
					PatternTreeNode ni = top.getNDB().get(i);
					mb = MB(ni, top);
					if (null != mb) {
						if (false == getFlag(mb, max, ni)) {
							if (i < fList.size())
								return fList.get(i);
						}
					}
				}
			}
			//求最小节点
			min = Min(fList);
			UpdateMinNode(min, max, top);
			return min;
		}
	}//getNext

	
	
	public PatternTreeNode getNext_inproceedings(PatternTreeNode top) {
			ArrayList<XmlElement> ei = new ArrayList<XmlElement>();
			ArrayList<PatternTreeNode> fList = new ArrayList<PatternTreeNode>();
			ArrayList<XmlElement> mb = new ArrayList<XmlElement>();
			PatternTreeNode max = null;
			PatternTreeNode min = null;

				if (author != null) {
					fList.add(author);
				}
				ei.add(getE(author));
				if (sup != null) {
					fList.add(sup);
				}
				ei.add(getE(sup));

			// 求最大节点
			if (ei.size() == 0) {
				max = null;
			} else {
				max = this.Max(ei);
			}
			
			//如果max为空，则返回fList中的第一个节点，该节点不能为返回节点标签流未到达表尾，如果fList为空，则返回空
			if (max == null) {
				return MaxIsNull_ReturnNode(fList);
			}
			
			
			
			else {
					mb = MB(author, top);
					if (null != mb) {
						if (false == getFlag(mb, max, author)) {
							if (0 < fList.size())
								return fList.get(0);
						}
					}
					
					
					
					mb = MB(sup, top);
					if (null != mb) {
						if (false == getFlag(mb, max, sup)) {
							if (1 < fList.size())
								return fList.get(1);
						}
					}
				
				
			}
			//求最小节点
			min = Min(fList);
			UpdateMinNode(min, max, top);
			return min;
	}//getNext
	
	
	
	

	//如果max为空，则返回fList中的第一个节点，该节点不能为返回节点标签流未到达表尾，如果fList为空，则返回空
	public PatternTreeNode MaxIsNull_ReturnNode(ArrayList<PatternTreeNode> fList) {
		PatternTreeNode node = null;
		for (int i = 0; i < fList.size(); i++) {
			XmlElement current_elem = fList.get(i).buffer.getCurrent();
			if (current_elem == null) {
				fList.remove(i);
				i--;
			}
		}
		if (fList.size() == 1)
			node = fList.get(0);
		else {
			for (int i = 0; i < fList.size(); i++) {
				PatternTreeNode node_i = fList.get(i);
				if (!node_i.isRetrunNode()) {
					node = node_i;
					break;
				}
			}
		}
		return node;
	}
	
	//算法中的第三个循环，更新min节点的祖先集合
	public void UpdateMinNode(PatternTreeNode min, PatternTreeNode max,
			PatternTreeNode top) {
		ArrayList<XmlElement> mb = MB(min, top);
		if (null != mb && mb.size()!=0) {
			//最大节点当前标签流中元素
			XmlElement current_maxElem = max.buffer.getCurrent();
			top.set.cleanSet(mb.get(0));
			for (int i = 0; i < mb.size(); ++i) {
				XmlElement mb_i =mb.get(i);
				if (mb_i.isAncestorOf(current_maxElem)) {
					top.set.updateSet(mb_i, min);
				}
			}
		}
	}
	
	//判断mb里的元素集合是否都是max的祖先 ，如果是为你true,否为false
	public boolean getFlag(ArrayList<XmlElement> mb, PatternTreeNode max,
			PatternTreeNode ni) {
		//最大节点当前标签流中元素
		XmlElement current_maxElem = max.buffer.getCurrent();
		for (int j = 0; j < mb.size(); ++j) {
			XmlElement mb_j =mb.get(j);
			if (!mb_j.isAncestorOf(current_maxElem)
					&& !ni.getLabel().equals("*")) {
				return false;
			}
		}
		return true;
	}

	//得到des节点对应的文档元素，如果是叶子节点则返回标签流中的当前元素，
	//如果是分支节点则返回该节点集合中的最大元素
	public XmlElement getE(PatternTreeNode des) {
		XmlElement e = null;
		if (des == null) {
			return null;
		}
		
		if (des.isLeaf() && des.buffer.getCurrent() == null) {
				return null;
		}
		else {
			e = des.buffer.getCurrent();
		}
		
		if (des.isBranching()) {
			e = des.set.getMaxElement(); 
		}
		
		return e;
	}

	public ArrayList<XmlElement> MB(PatternTreeNode des, PatternTreeNode anc) {
		XmlElement e = this.getE(des);
		if (null == e) {
			return null;
		} else {
			return e.getMatchElement(anc);
		}
	}

	//求list节点集合中最大的节点
	private PatternTreeNode Max(ArrayList<XmlElement> list) {
		XmlElement maxElement = null;
		PatternTreeNode max = null;
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size() - 1; i++) {
				
				XmlElement ielem = list.get(i);
				XmlElement nextelem = list.get(i+1);
				
				if (ielem != null && nextelem != null) {
					maxElement = ielem;
					if (nextelem.getScheme().isGreater(maxElement.getScheme())) {
						maxElement = nextelem;
					}
				}
			}
		}
		if (maxElement != null) {
			max = maxElement.getPatternTreeNode();
			if (max.buffer.getCurrent() == null || max.isRetrunNode()) {
				max = null;
			}
		} else {
			max = null;
		}
		return max;
	}

	//求list节点集合中最小的节点
	private PatternTreeNode Min(ArrayList<PatternTreeNode> list) {
		XmlElement minElement = null;
		if (!list.isEmpty()) {
			minElement = list.get(0).buffer.getCurrent();
			for (int i = 1; i < list.size(); i++) {
				PatternTreeNode list_i = list.get(i);
				XmlElement cur_ielem =list_i.buffer.getCurrent();
				
				if (cur_ielem != null && !list_i.isRetrunNode()) {
					if (minElement.getScheme().isGreater(cur_ielem.getScheme())) {
						minElement = list_i.buffer.getCurrent();
					}
				}
			}
		} else {
			return null;
		}
		return minElement.getPatternTreeNode();
	}

	//判断叶子节点的标签流是否都到达表尾
	private boolean end(PatternTree tree) {
		ArrayList<PatternTreeNode> leafnodes = tree.getLeafNodes();
		for (int i = 0; i < leafnodes.size(); i++) {
			PatternTreeNode leafnode = leafnodes.get(i);
			if (!leafnode.buffer.isEmpty())
				return false;
		}
		return true;
	}

	// 清空所有集合并返回最终结果
	public Set<XmlElement> emptyAllSets(PatternTreeNode node) {
		Set<XmlElement> finalres = new HashSet<XmlElement>();
		ArrayList<PatternTreeNode> childnodes = node.getChildrenNodes(); 
		ArrayList<FiveTuple> fivetuple = node.set.getSet();
		
		if (!node.isLeaf()) {
			for (int i = 0; i < childnodes.size(); i++) {
				PatternTreeNode childnode = childnodes.get(i);
				emptyAllSets(childnode);
			}
		}

		for (int i = 0; i < fivetuple.size(); i++) {
			FiveTuple tuple = fivetuple.get(i);
			finalres.addAll(node.set.cleanSet(tuple.getXmlElement()));
		}
		return finalres;
	}
}