package ${serviceImplPackage};

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ${modelPackage+"."+modelClassName};
import ${daoPackage+"."+daoClassName};
import ${servicePackage+"."+serviceClassName};

import org.onetwo.common.base.BaseServiceImpl;

@Service
public class ${serviceImplclassName} extends BaseServiceImpl<${modelClassName}, ${table.primaryKey.javaType.simpleName}> implements ${serviceClassName} {

	@Resource
	private ${daoClassName} dao;
	
	@Override
	public ${daoClassName} getDao(){
		return dao;
	}
}
