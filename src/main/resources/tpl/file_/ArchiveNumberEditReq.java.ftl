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
@Schema(description = "转入档案编号表编辑更新实体")
@Data
public class ${entityName}EditReq implements Serializable {

    private static final long serialVersionUID = 1L;
<#if pkFieldInfo??>

    /**
     * ${pkFieldInfo.columnComment}
     */
    private ${pkFieldInfo.typeName} ${pkFieldInfo.propertyName};
</#if>
<#list fieldInfos as field>

    /**
     * ${field.columnComment}
     */
    private ${field.typeName} ${field.propertyName};
</#list>

  public static ${entityName} to${entityName}(${entityName}EditReq ${lowerFirstEntityName}EditReq) {
    ${entityName} ${lowerFirstEntityName} = new ${entityName}();
    BeanUtils.copyProperties(${lowerFirstEntityName}EditReq, ${lowerFirstEntityName});
    ${lowerFirstEntityName}.setUpdateDate(new Date());
    return ${lowerFirstEntityName};
  }
}
