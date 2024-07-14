package ${basePackage}.vo;

import ${basePackage}.model.${entityName};
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * ${tableComment}显示实体
 *
 * @author ${author}
 * @date ${createDate}
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ${entityName}Vo extends ${entityName} {

}