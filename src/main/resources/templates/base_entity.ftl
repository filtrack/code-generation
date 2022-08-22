package ${entityPackage!""};


import java.io.Serializable;
import java.util.*;
<#if enableBaseEntity!false>
import ${entityPackage}.${baseEntityName};
</#if>
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
* author ${author}
* description ${desc}
* date ${nowDate?string("yyyy-MM-dd")}
*/
@Data
public class ${className?cap_first!""} implements Serializable {

<#list commonClns as field>
    <#if field.columnComment?? && field.columnComment !="">
    //${field.columnComment!""}
    </#if>
    <#if field.tabColumnName?? && field.tabColumnName !="">
    @TableField("${field.tabColumnName!""}")
    </#if>
    private ${field.columnType} ${field.lowerColumnName};

</#list>

}