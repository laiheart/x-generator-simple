package ${basePackage}.business;

import com.ection.platform.common.dto.ResultData;
import com.ection.platform.common.exception.BusinessException;
import ${basePackage}.db.service.${entityName}Service;
import ${basePackage}.dto.${entityPackage}.*;
import ${basePackage}.utils.ThrowUtils;
import ${basePackage}.vo.${entityName}Vo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ${author}
 * @date ${createDate}
 */
@Service
@Slf4j
public class ${entityName}Business {

    @Resource
    private ${entityName}Service ${lowerFirstEntityName}Service;

    public Long add(${entityName}AddReq ${lowerFirstEntityName}AddReq) {
        return ${lowerFirstEntityName}Service.insert(${lowerFirstEntityName}AddReq);
    }

    public List<${entityName}Vo> page(${entityName}QueryReq ${lowerFirstEntityName}QueryReq) {
        return ${lowerFirstEntityName}Service.page(${lowerFirstEntityName}QueryReq);
    }

    public ${entityName}Vo getById(Long id) {
        return ${lowerFirstEntityName}Service.getById(id);
    }

    public Integer edit(${entityName}EditReq ${lowerFirstEntityName}EditReq) {
        return ${lowerFirstEntityName}Service.edit(${lowerFirstEntityName}EditReq);
    }

}
