package com.x.generator.entity;

import lombok.Data;

import java.util.List;

/**
 * @author lsx
 * @date 2024-07-13
 */
@Data
public class DbMetaData {
    private String tableName;
    private String tableComment;
    private FieldInfo pkFieldInfo;
    private boolean hasPk;
    List<FieldInfo> fieldInfos;

    public boolean isPkFieldInfo(FieldInfo fieldInfo) {
        if (pkFieldInfo == null || pkFieldInfo.getColumnName() == null) {
            return false;
        }
        if (pkFieldInfo.getColumnName().equals(fieldInfo.getColumnName())) {
            hasPk = true;
            return true;
        }
        return false;
    }

    public static FieldInfo buildFieldInfo(String columnName, String typeName, String columnComment) {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setColumnName(columnName);
        fieldInfo.setTypeName(typeName);
        fieldInfo.setColumnComment(columnComment);
        return fieldInfo;
    }

    @Data
    public static class FieldInfo {
        private String columnName = "";
        private String typeName = "";
        private String columnComment = "";
    }
}
