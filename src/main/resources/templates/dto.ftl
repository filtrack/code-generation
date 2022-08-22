package ${dtoPackage!""};

import ${dtoPackage!""}.ReqPageDTO;
import java.util.Date;
import lombok.*;

/**
* author ${author}
* description ${desc}
* date ${nowDate?string("yyyy-MM-dd")}
*/
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ${className?cap_first!""} extends ReqPageDTO{

    private static final long serialVersionUID = 1L;

<#list table.allColumnInfoList as field>
    <#if field.columnComment?? && field.columnComment !="">
    //${field.columnComment!""}
    </#if>
    private ${field.columnType} ${field.lowerColumnName};
</#list>

}