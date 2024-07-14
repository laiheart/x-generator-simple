package com.x.generator.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.x.generator.utils.Db2EntityDataTypeConvert;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lsx
 * @date 2024-07-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EntityMetaData extends CommonMetaData {
    private String ignorePrefix;
    private String entityName;
    private String entityPackage;

    private String tableName;
    private String tableComment;
    private FieldInfo pkFieldInfo;
    private boolean hasPk;
    List<FieldInfo> fieldInfos;

    public static EntityMetaData copyFromDbMetaData(DbMetaData dbMetaData) {
        //整体复制
        EntityMetaData entityMetaData = BeanUtil.copyProperties(dbMetaData, EntityMetaData.class);
        //复制主键实体
        FieldInfo pkFieldInfo = BeanUtil.copyProperties(dbMetaData.getPkFieldInfo(), FieldInfo.class);
        entityMetaData.setPkFieldInfo(pkFieldInfo);
        //复制其他字段信息
        List<FieldInfo> fieldInfos = BeanUtil.copyToList(dbMetaData.getFieldInfos(), FieldInfo.class);
        entityMetaData.setFieldInfos(fieldInfos);
        return entityMetaData;
    }

    private static void buildEntityName(EntityMetaData entityMetaData, String ignorePrefix) {
        entityMetaData.setIgnorePrefix(ignorePrefix);
        String tableName = entityMetaData.getTableName();
        if (ignorePrefix != null && entityMetaData.getTableName().startsWith(ignorePrefix)) {
            tableName = tableName.substring(ignorePrefix.length());
        }
        entityMetaData.setEntityName(StrUtil.upperFirst(StrUtil.toCamelCase(tableName)));
    }

    private static void buildFieldPropertyName(List<FieldInfo> fieldInfos) {
        for (FieldInfo fieldInfo : fieldInfos) {
            fieldInfo.setPropertyName(StrUtil.toCamelCase(fieldInfo.columnName));
        }
    }

    private static void db2EntityDataTypeConvert(List<FieldInfo> fieldInfos) {
        for (FieldInfo fieldInfo : fieldInfos) {
            String columnTypeName = fieldInfo.getTypeName();
            fieldInfo.setColumnTypeName(columnTypeName);
            String typeName = Db2EntityDataTypeConvert.db2EntityConvert(columnTypeName);
            fieldInfo.setTypeName(typeName);
        }
    }

    public static void buildFinal(EntityMetaData entityMetaData, String ignorePrefix) {
        EntityMetaData.buildEntityName(entityMetaData, ignorePrefix);
        List<FieldInfo> fieldInfos = entityMetaData.getFieldInfos();
        List<FieldInfo> all = new ArrayList<>(fieldInfos.size() + 1);
        all.addAll(fieldInfos);
        all.add(entityMetaData.getPkFieldInfo());
        EntityMetaData.buildFieldPropertyName(all);
        EntityMetaData.db2EntityDataTypeConvert(all);
        entityMetaData.setEntityPackage(entityMetaData.getEntityName().toLowerCase());
    }

    @Data
    public static class FieldInfo {
        private String columnName;
        private String columnTypeName;
        private String propertyName;
        private String typeName;
        private String columnComment;
    }
}
