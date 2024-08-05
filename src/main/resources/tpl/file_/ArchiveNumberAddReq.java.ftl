package ${basePackage}.request.${entityPackage};

import ${basePackage}.model.${entityName};
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
* ${tableComment}
*
* @author ${author}
* @date ${createDate}
*/
@Schema(description = "转入档案编号表添加实体")
@Data
public class ${entityName}AddReq implements Serializable {

    private static final long serialVersionUID = 1L;
<#list fieldInfos as field>

    /**
     * ${field.columnComment}
     */
    private ${field.typeName} ${field.propertyName};
</#list>

  public static ${entityName} to${entityName}(${entityName}AddReq ${lowerFirstEntityName}AddReq) {
    ${entityName} ${lowerFirstEntityName} = new ${entityName}();
    BeanUtils.copyProperties(${lowerFirstEntityName}AddReq, ${lowerFirstEntityName});
    ${lowerFirstEntityName}.setCreateDate(new Date());
    return ${lowerFirstEntityName};
  }

}
