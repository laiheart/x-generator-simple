package ${basePackage}.db.service.impl;

import ${basePackage}.db.dao.${entityName}Mapper;
import ${basePackage}.db.service.I${entityName}Service;
import ${basePackage}.dto.${entityPackage}.${entityName}AddReq;
import ${basePackage}.dto.${entityPackage}.${entityName}EditReq;
import ${basePackage}.dto.${entityPackage}.${entityName}QueryReq;
import ${basePackage}.model.${entityName};
import ${basePackage}.vo.${entityName}Vo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ${author}
 * @Date ${createDate}
 */
@Service
public class ${entityName}Service implements I${entityName}Service {

    @Resource
    private ${entityName}Mapper ${lowerFirstEntityName}Mapper;

    @Override
    public Long insert(${entityName}AddReq ${lowerFirstEntityName}AddReq) {
        ${entityName} ${lowerFirstEntityName} = ${entityName}AddReq.to${entityName}(${lowerFirstEntityName}AddReq);
        ${lowerFirstEntityName}Mapper.insertSelective(${lowerFirstEntityName});
        return ${lowerFirstEntityName}.getId();
    }

    @Override
    public List<${entityName}Vo> page(${entityName}QueryReq ${lowerFirstEntityName}QueryReq) {
        return ${lowerFirstEntityName}Mapper.page(${lowerFirstEntityName}QueryReq);
    }

    @Override
    public ${entityName}Vo getById(Long id) {
        ${entityName} ${lowerFirstEntityName} = ${lowerFirstEntityName}Mapper.selectByPrimaryKey(id);
        return ${entityName}Vo.to${entityName}Vo(${lowerFirstEntityName});
    }

    @Override
    public Integer edit(${entityName}EditReq ${lowerFirstEntityName}EditReq) {
        ${entityName} ${lowerFirstEntityName} = ${entityName}EditReq.
                to${entityName}(${lowerFirstEntityName}EditReq);
        return ${lowerFirstEntityName}Mapper.updateByPrimaryKeySelective(${lowerFirstEntityName});
    }
}
