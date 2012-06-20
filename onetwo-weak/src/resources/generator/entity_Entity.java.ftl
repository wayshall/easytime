<#ftl encoding="utf-8"/>
package ${modelPackage};

import org.onetwo.common.db.BaseEntity;

<#list importClasses as clz>
	<#lt>import ${clz};
</#list>

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/*****
 * ${table.comment?default("")}
 * @Entity
 */
<#assign uncapitalClassName = table.className?capitalize/>
@SuppressWarnings("serial")
@Entity
@Table(name="${table.name}")
public class ${modelClassName}<#if table.primaryKey??> extends BaseEntity<${table.primaryKey.javaType.simpleName}></#if>{
	
<#if table.primaryKey??>
	@Id
	<#if table.primaryKey.generatorId>
	@GeneratedValue(generator="${uncapitalClassName}Generator")
	@GenericGenerator(name="${uncapitalClassName}Generator", strategy="sequence", parameters={@Parameter(name="sequence", value="SEQ_${table.name?upper_case}")})
	</#if>
	protected ${table.primaryKey.javaType.simpleName} ${table.primaryKey.javaName};
<#else>
	该实体没有配置主键，请手动修改配置主键！
</#if>
	
<#list table.columnCollection as column>
  <#if !column.primaryKey>
	<#if column.dateType>
	
	@Temporal(TemporalType.${column.javaType.simpleName?upper_case})
	</#if>
	protected ${column.javaType.simpleName} ${column.javaName};
  </#if>
</#list>
	
	public ${modelClassName}(){
	}
	
<#if table.primaryKey??>
	public ${table.primaryKey.javaType.simpleName} ${table.primaryKey.readMethodName}() {
		return this.${table.primaryKey.javaName};
	}
	
	public void ${table.primaryKey.writeMethodName}(${table.primaryKey.javaType.simpleName} ${table.primaryKey.javaName}) {
		this.${table.primaryKey.javaName} = ${table.primaryKey.javaName};
	}
</#if>
	
<#list table.columnCollection as column>
  <#if !column.primaryKey>
	/*****
	 * ${column.comment?default("")}
	 * @return
	 */
	public ${column.javaType.simpleName} ${column.readMethodName}() {
		return this.${column.javaName};
	}
	
	public void ${column.writeMethodName}(${column.javaType.simpleName} ${column.javaName}) {
		this.${column.javaName} = ${column.javaName};
	}
  </#if>
</#list>
	
}
