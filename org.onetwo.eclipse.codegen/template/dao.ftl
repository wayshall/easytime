package ${daoPackage};

import org.springframework.stereotype.Repository;

import ${modelPackage+"."+modelClassName};
import org.onetwo.common.base.BaseDao;

@Repository
public class ${daoClassName} extends BaseDao<${modelClassName}, ${table.primaryKey.javaType.simpleName}> {

}
