package ${basePackage}.respone;

import ${basePackage}.model.${entityName};
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;


/**
* ${tableComment}
*
* @author ${author}
* @date ${createDate}
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class ${entityName}Vo extends ${entityName} {

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

  public static ${entityName}Vo to${entityName}Vo(${entityName} ${lowerFirstEntityName}) {
    ${entityName}Vo ${lowerFirstEntityName}Vo = new ${entityName}Vo();
    BeanUtils.copyProperties(${lowerFirstEntityName}, ${lowerFirstEntityName}Vo);
    return ${lowerFirstEntityName}Vo;
  }
}
