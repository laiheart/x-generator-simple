package com.x.generator.config;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.util.*;

/**
 * @author lsx
 * @date 2024-07-13
 */
@Data
public class GlobalConfig {

    private String author;

    /**
     * 基础包名
     */
    private String basePackage;
    /**
     * 类日期
     */
    private String createDate;
    /**
     * 表名
     */
    private List<String> tableNames;
    /**
     * 表忽略前缀
     */
    private String tableIgnorePrefix;

    /**
     * 数据库连接信息
     */
    private String dbUrl;
    /**
     * 数据库用户名
     */
    private String dbUser;
    /**
     * 数据库密码
     */
    private String dbPassword;
    /**
     * 扩展信息
     */
    private Map<String, Object> extraInfo;

    {
        createDate = DateUtil.formatDate(new Date());
    }

    public void setTableNames(String... tableNames) {
        this.tableNames = new ArrayList<>();
        Collections.addAll(this.tableNames, tableNames);
    }
}
