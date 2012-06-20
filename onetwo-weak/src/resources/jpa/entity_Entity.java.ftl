package ${fullPackage};

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

import com.yooyo.zjk.BaseEntity;


/*****
 * ${table.comment?default("")}
 * @Entity
 */
<#assign uncapitalClassName = table.className?capitalize/>
@SuppressWarnings("serial")
@Entity
@Table(name="${table.name}")
@SequenceGenerator(name="${selfClassName}Generator", sequenceName="SEQ_${table.name}")
public class ${selfClassName} extends BaseEntity<Long> {
	
<#list table.columnCollection as column>
  <#if column.javaName!='createTime' && column.javaName!='lastUpdateTime'>
	protected ${column.javaType.simpleName} ${column.javaName};
  </#if>
  
</#list>
	
	public ${selfClassName}(){
	}
	
<#list table.columnCollection as column>
  <#if column.javaName!='createTime' && column.javaName!='lastUpdateTime'>
	
	/*****
	 * ${column.comment?default("")}
	 * @return
	 */
	<#if column.primaryKey>
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="${selfClassName}Generator")
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
