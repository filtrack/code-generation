package ${serviceImplPackage!""};

import ${servicePackage!""}.${serviceName?cap_first!""};
import ${entityPackage!""}.${entityName?cap_first!""};
import ${mapperPackage!""}.${entityMapper?cap_first!""};
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* author ${author}
* description ${desc}
* date ${nowDate?string("yyyy-MM-dd")}
*/
@Service
public class ${className?cap_first!""} extends ServiceImpl<${entityMapper?cap_first!""}, ${entityName?cap_first!""}>  implements ${serviceName?cap_first!""} {

    final ${entityMapper?cap_first!""} ${entityMapper?uncap_first!""};
    public ${className?cap_first!""}(${entityMapper?cap_first!""} ${entityMapper?uncap_first!""}) {
        this.${entityMapper?uncap_first!""} = ${entityMapper?uncap_first!""};
    }

}

