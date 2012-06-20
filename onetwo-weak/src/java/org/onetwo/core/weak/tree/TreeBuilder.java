package org.onetwo.core.weak.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.core.exception.ServiceException;


@SuppressWarnings("unchecked")
public class TreeBuilder<T> {

	private Map leafages = new LinkedHashMap();
	private List<TreeModel> tree = new ArrayList<TreeModel>();
	private Comparator comparator = null;
	
	public TreeBuilder(List<T> datas, TreeModelCreator treeNodeCreator) {
		this(datas, treeNodeCreator, null);
	}

	public TreeBuilder(List<T> datas, final TreeModelCreator<T> treeNodeCreator, Comparator comparator) {
		if(datas==null || datas.isEmpty())
			return ;
		
		this.comparator = comparator!=null?comparator:new Comparator<T>(){
			@Override
			public int compare(T o1, T o2) {
				TreeModel t1 = treeNodeCreator.createTreeModel(o1);
				TreeModel t2 = treeNodeCreator.createTreeModel(o2);
				return t1.compareTo(t2);
			}
		};
		Collections.sort(datas, this.comparator);

		for (T data : datas) {
			if (data == null)
				continue;
			TreeModel node = treeNodeCreator.createTreeModel(data);
			leafages.put(node.getId(), node);
		}
	}

	public List<TreeModel> buidTree() {
		if(leafages.isEmpty())
			return Collections.EMPTY_LIST;
		
		Collection<TreeModel> treeModels = (Collection<TreeModel>) leafages.values();
		
		for (TreeModel node : treeModels) {
			if (node.getParentId() == null || ( (node.getParentId() instanceof Number) && ((Number) node.getParentId()).longValue()<=0)) {
				tree.add(node);
			} else {
				TreeModel p = (TreeModel) leafages.get(node.getParentId());
				if (p == null)
					throw new ServiceException("build tree error: can't not find the node[" + node.getId() + ", "+node.getName()+"]'s parent node[" + node.getParentId() + "]");
				p.addChild(node);
			}
		}

		return tree;
	}
	
	public static void main(String[] args){
		
	}

}