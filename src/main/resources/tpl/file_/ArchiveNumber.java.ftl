package ${basePackage}.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
* ${tableComment}
*
* @author ${author}
* @date ${createDate}
*/
@Schema(description = "转入档案编号表")
@Data
@Table(name = "${tableName}")
public class ${entityName} implements Serializable {

    private static final long serialVersionUID = 1L;
<#if pkFieldInfo??>

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