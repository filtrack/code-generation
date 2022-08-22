package ${mapperPackage!""};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${entityPackage!""}.${entityName?cap_first!""};
import org.apache.ibatis.annotations.Mapper;

/**
* author ${author}
* description ${desc}
* date ${nowDate?string("yyyy-MM-dd")}
*/
@Mapper
public interface ${className?cap_first!""}  extends BaseMapper<${entityName?cap_first!""}> {

}



