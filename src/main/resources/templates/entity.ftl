package ${entityPackage!""};

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
* author ${author}
* description ${desc}
* date ${nowDate?string("yyyy-MM-dd")}
*/
<#if table.tableName?? && table.tableName !="">
@TableName("${table.tableName!""}")
</#if>
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ${table.lowerClassName?cap_first!""} <#if enableBaseEntity!false>extends BaseEntity<#else>implements  Serializable</#if> {

    private static final long serialVersionUID = 1L;

<#list table.columnInfoList as field>
    <#if field.columnComment?? && field.columnComment !="">
    //${field.columnComment!""}
    </#if>
    <#if field.tabColumnName?? && field.tabColumnName !="">
    @TableField("${field.tabColumnName!""}")
    </#if>
    private ${field.columnType} ${field.lowerColumnName};

</#list>

}