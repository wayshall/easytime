package org.onetwo.core.util;

import java.util.Collection;

@SuppressWarnings("unchecked")
public class EasyList{
	
	public static interface Fun {
		public void exe(Object element, int index, Object...objects);
	}

	private Collection list;
	
	public EasyList(Collection list){
		this.list = list;
	}
	
	public void each(Fun block, Object...objects){
		if(list==null || list.isEmpty())
			return ;
		int index = 0;
		for(Object element : list){
			block.exe(element, index, objects);
			index++;
		}
	}
}
