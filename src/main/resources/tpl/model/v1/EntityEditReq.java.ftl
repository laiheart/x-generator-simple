package ${basePackage}.dto.${entityPackage};

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * ${tableComment}编辑更新实体
 *
 * @author ${author}
 * @date ${createDate}
 */
@Data
public class ${entityName}EditReq implements Serializable {

    private static final long serialVersionUID = 1L;
<#if hasPk>

    /**
    * ${pkFieldInfo.columnComment}
    */
    @Id
    @Column(name = "${pkFieldInfo.columnName}")
    @GeneratedValue(generator = "JDBC")
    private ${pkFieldInfo.typeName} ${pkFieldInfo.propertyName};
</#if>
<#list fieldInfos as field>

    /**
    * ${field.columnComment}
    */
    @Column(name = "${field.columnName}")
    private ${field.typeName} ${field.propertyName};
</#list>

}