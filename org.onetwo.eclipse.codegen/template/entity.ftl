package ${modelPackage};

<#list importClasses as clz>
	<#lt>import ${clz};
</#list>

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.onetwo.common.db.AbstractBaseEntity;


/*****
 * ${table.comment?default("")}
 * @Entity
 */
<#assign uncapitalClassName = table.className?capitalize/>
@SuppressWarnings("serial")
@Entity
@Table(name="${table.name}")
@SequenceGenerator(name="${modelClassName}Generator", sequenceName="SEQ_${table.name}")
public class ${modelClassName} extends AbstractBaseEntity<Long> {
	
<#list table.columnCollection as column>
  <#if column.javaName!='createTime' && column.javaName!='lastUpdateTime'>
	protected ${column.javaType.simpleName} ${column.javaName};
  </#if>
  
</#list>
	
	public ${modelClassName}(){
	}
	
<#list table.columnCollection as column>
  <#if column.javaName!='createTime' && column.javaName!='lastUpdateTime'>
	
	/*****
	 * ${column.comment?default("")}
	 * @return
	 */
<#if table.primaryKey??>
	@Id
	<#if table.primaryKey.generatorId>
	@GeneratedValue(generator="${uncapitalClassName}Generator")
	@GenericGenerator(name="${uncapitalClassName}Generator", strategy="sequence", parameters={@Parameter(name="sequence", value="SEQ_${table.name?upper_case}")})
	</#if>
<#else>
	该实体没有配置主键，请手动修改配置主键！
</#if>
	<#if column.dateType>
	@Temporal(TemporalType.${column.javaType.simpleName?upper_case})
	</#if>
	@Column(name="${column.name}")
	public ${column.javaType.simpleName} ${column.readMethodName}() {
		return this.${column.javaName};
	}
	
	public void ${column.writeMethodName}(${column.javaType.simpleName} ${column.javaName}) {
		this.${column.javaName} = ${column.javaName};
	}
  </#if>
</#list>
	
}
