package ${basePackage}.dto.${entityPackage};

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * ${tableComment}添加实体
 *
 * @author ${author}
 * @date ${createDate}
 */
@Data
public class ${entityName}AddReq implements Serializable {

    private static final long serialVersionUID = 1L;

<#list fieldInfos as field>

    /**
     * ${field.columnComment}
     */
    private ${field.typeName} ${field.propertyName};
</#list>

}