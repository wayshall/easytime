package org.onetwo.core.weak.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "unchecked", "serial" })
public class TreeModel implements Comparable<TreeModel>, Serializable {

	protected Serializable id;

	protected String name;

	protected List<TreeModel> children = new ArrayList<TreeModel>();

	protected Serializable parentId;
	
	protected Comparable sort;

	public TreeModel(Serializable id, String name) {
		this(id, name, null);
	}

	public TreeModel(Serializable id, String name, Serializable parentId) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		if(id instanceof Comparable)
			this.sort = (Comparable) id;
	}

	public List<TreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<TreeModel> children) {
		this.children = children;
	}

	public void addChild(TreeModel node) {
		this.children.add(node);
	}
	
	public TreeModel getChild(Object id){
		if(this.isLeafage())
			return null;
		for(TreeModel node : this.children){
			if(node.getId().equals(id))
				return node;
		}
		return null;
	}

	public Object getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Comparable getSort() {
		return (Comparable) sort;
	}

	public Object getParentId() {
		return parentId;
	}

	public boolean isLeafage() {
		return this.children.isEmpty();
	}

	@Override
	public int compareTo(TreeModel tree2) {
		return getSort().compareTo(tree2.getSort());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TreeModel other = (TreeModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
