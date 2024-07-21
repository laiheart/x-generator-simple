package ${basePackage}.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * ${tableComment}
 *
 * @author ${author}
 * @date ${createDate}
 */
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