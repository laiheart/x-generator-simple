package ${basePackage}.request.${entityPackage};

import com.ection.platform.common.web.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* ${tableComment}
*
* @author ${author}
* @date ${createDate}
*/
@Schema(description = "转入档案编号表查询实体")
@EqualsAndHashCode(callSuper = true)
@Data
public class ${entityName}QueryReq extends BaseEntity {

    private static final long serialVersionUID = 1L;
<#list fieldInfos as field>

    /**
     * ${field.columnComment}
     */
    private ${field.typeName} ${field.propertyName};
</#list>

