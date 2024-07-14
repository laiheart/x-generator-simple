package ${basePackage}.dto.${entityPackage};

import com.ection.platform.common.web.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * ${tableComment}查询实体
 *
 * @author ${author}
 * @date ${createDate}
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ${entityName}QueryReq extends BaseEntity {
<#list fieldInfos as field>

    /**
     * ${field.columnComment}
     */
    private ${field.typeName} ${field.propertyName};
</#list>

}