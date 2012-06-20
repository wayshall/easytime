package ${servicePackage};

import ${modelPackage+"."+modelClassName};

import org.onetwo.common.base.BaseService;

public interface ${serviceClassName} extends BaseService<${modelClassName}, ${table.primaryKey.javaType.simpleName}> {

}
