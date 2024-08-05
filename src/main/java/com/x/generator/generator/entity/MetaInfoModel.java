package com.x.generator.generator.entity;

import cn.hutool.core.util.StrUtil;
import com.x.generator.generator.utils.Db2EntityDataTypeConvert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lsx
 * @date 2024-07-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MetaInfoModel extends CommonMetaInfo {
    /**
     * 忽略表前缀名
     */
    private String ignorePrefix;
    /**
     * 表对应的实体名
     */
    private String entityName;
    /**
     * 实体包名
     */
    private String entityPackage;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表备注
     */
    private String tableComment;
    /**
     * 主键字段
     */
    private FieldInfo pkFieldInfo;
    /**
     * 非主键字段信息
     */
    List<FieldInfo> fieldInfos;

    /**
     * 设置实体名
     *
     * @param metaInfoModel
     * @param ignorePrefix
     */
    private static void buildEntityName(MetaInfoModel metaInfoModel, String ignorePrefix) {
        metaInfoModel.setIgnorePrefix(ignorePrefix);
        String tableName = metaInfoModel.getTableName();
        if (ignorePrefix != null && metaInfoModel.getTableName().startsWith(ignorePrefix)) {
            tableName = tableName.substring(ignorePrefix.length());
        }
        metaInfoModel.setEntityName(StrUtil.upperFirst(StrUtil.toCamelCase(tableName)));
    }

    /**
     * 构建实体属性名
     *
     * @param fieldInfos
     */
    private static void buildFieldPropertyName(List<FieldInfo> fieldInfos) {
        for (FieldInfo fieldInfo : fieldInfos) {
            fieldInfo.setPropertyName(StrUtil.toCamelCase(fieldInfo.columnName));
        }
    }

    /**
     * 数据库字段类型和实体属性类型转换
     *
     * @param fieldInfos
     */
    private static void db2EntityDataTypeConvert(List<FieldInfo> fieldInfos) {
        for (FieldInfo fieldInfo : fieldInfos) {
            String columnTypeName = fieldInfo.getColumnTypeName();
            String typeName = Db2EntityDataTypeConvert.db2EntityConvert(columnTypeName);
            fieldInfo.setTypeName(typeName);
        }
    }

    private static void modifyColumnDataType(List<FieldInfo> fieldInfos) {
        fieldInfos.forEach(info -> {
            String columnTypeName = info.getColumnTypeName().toLowerCase();
            if (StringUtils.equals("int", columnTypeName)) {
                info.setColumnTypeName("INTEGER");
            }
            if (StringUtils.equals("datetime",columnTypeName)) {
                info.setColumnTypeName("TIMESTAMP");
            }
        });
    }

    /**
     * 构建元信息模型完整信息
     *
     * @param metaInfoModel
     * @param ignorePrefix
     */
    public static void buildFinal(MetaInfoModel metaInfoModel, String ignorePrefix) {
        MetaInfoModel.buildEntityName(metaInfoModel, ignorePrefix);
        List<FieldInfo> fieldInfos = metaInfoModel.getFieldInfos();
        List<FieldInfo> all = new ArrayList<>(fieldInfos.size() + 1);
        all.addAll(fieldInfos);
        all.add(metaInfoModel.getPkFieldInfo());
        MetaInfoModel.buildFieldPropertyName(all);
        MetaInfoModel.db2EntityDataTypeConvert(all);
        MetaInfoModel.modifyColumnDataType(all);
        metaInfoModel.setEntityPackage(metaInfoModel.getEntityName().toLowerCase());
    }

    /**
     * 构建字段基本信息
     *
     * @param columnName
     * @param columnTypeName
     * @param columnComment
     * @return
     */
    public static FieldInfo buildFieldInfo(String columnName, String columnTypeName, String columnComment) {
        return new FieldInfo(columnName, columnTypeName, columnComment);
    }

    /**
     * 构建字段基本信息
     *
     * @param columnName
     * @return
     */
    public static FieldInfo buildFieldInfo(String columnName) {
        return new FieldInfo(columnName);
    }

    /**
     * 判断字段是否主键
     *
     * @param pkInfo
     * @param fieldInfo
     * @return
     */
    public static boolean isPkFieldInfo(FieldInfo pkInfo, FieldInfo fieldInfo) {
        if (pkInfo == null) return false;
        return pkInfo.columnName.equals(fieldInfo.columnName);
    }

    @Data
    @NoArgsConstructor
    public static class FieldInfo {
        private String columnName;
        private String columnTypeName;
        private String columnComment;
        private String propertyName;
        private String typeName;

        public FieldInfo(String columnName, String columnTypeName, String columnComment) {
            this.columnName = columnName;
            this.columnTypeName = columnTypeName;
            this.columnComment = columnComment;
        }

        public FieldInfo(String columnName) {
            this.columnName = columnName;
        }
    }
}
