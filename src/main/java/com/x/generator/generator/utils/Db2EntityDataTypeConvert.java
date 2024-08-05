package com.x.generator.generator.utils;


/**
 * @author lsx
 * @date 2024-07-13
 */
public class Db2EntityDataTypeConvert {
    public static String db2EntityConvert(String fieldType) {
        String t = fieldType.toLowerCase();
        if (!t.contains("char") && !t.contains("text")) {
            if (t.contains("bigint")) {
                return "Long";
            } else if (t.contains("int")) {
                return "Integer";
            } else if (!t.contains("date") && !t.contains("time") && !t.contains("year")) {
                if (t.contains("text")) {
                    return "String";
                } else if (t.contains("bit")) {
                    return "Boolean";
                } else if (t.contains("decimal")) {
                    return "BigDecimal";
                } else if (t.contains("clob")) {
                    return "String";
                } else if (t.contains("blob")) {
                    return "String";
                } else if (t.contains("binary")) {
                    return "String";
                } else if (t.contains("float")) {
                    return "Float";
                } else if (t.contains("double")) {
                    return "Double";
                } else {
                    return "String";
                }
            } else {
                return "Date";
            }
        } else {
            return "String";
        }
    }

}
