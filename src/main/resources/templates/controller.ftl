package ${controllerPackage!""};

import ${servicePackage!""}.${serviceName?cap_first!""};
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* author ${author}
* description ${desc}
* date ${nowDate?string("yyyy-MM-dd")}
*/
@Slf4j
@RestController
@RequestMapping("/${entityName!""}")
public class ${className?cap_first!""} {

    final ${serviceName?cap_first!""} ${serviceName!""};
    public ${className?cap_first!""}(${serviceName?cap_first!""} ${serviceName!""}) {
        this.${serviceName!""} = ${serviceName!""};
    }

}


