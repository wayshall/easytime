package org.onetwo.ext.xml;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.core.util.MyUtils;
import org.onetwo.core.util.SysUtils;

public class MenuXmlTest {
	

	@Test
	public void testReadFile(){
		String path = this.getClass().getResource("/menu_config.xml").getPath();
		Document doc = DomUtils.readXml(new File(path));
		XmlObjectParser p = new SimpleXmlObjectParser();
		Map map = new HashMap();
		map.put("debug", null);//根元素有那些属性
		map.put("popmenu", null);//根元素有那些属性
		p.setRoot(map);//设置根元素
		p.map("popmenus", HashMap.class, true);//根元素对应类型
		p.map("popmenu", MenuConfig.class, false);//映射各元素对应的类型
		doc.accept(p);
		Map root = (Map)p.getRoot();
		System.out.println(root.toString());
		List<MenuConfig> list = (List<MenuConfig>)MyUtils.asList(root.get("popmenu"));
//		list.add(0, null);
//		Assert.assertEquals(3, list.size());
		Assert.assertEquals("命令行", list.get(0).getName());
		Assert.assertEquals("cmd", list.get(0).getType());
		Assert.assertEquals("debug", root.get("debug"));
		System.out.println("printinfo_0: " + list.get(0).canPrintinfo());
		System.out.println("printinfo_1: " + list.get(1).canPrintinfo());
		System.out.println("printinfo_2: " + list.get(2).canPrintinfo());
		
		/*try {
			MenuConfig menuConfig = list.get(2);
			String impl = menuConfig.getImplementor();
			String[] strs = menuConfig.getStatments();
			for(String s : strs){
				String rs = SysUtils.exec(s);
				System.out.println("result: " + rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
