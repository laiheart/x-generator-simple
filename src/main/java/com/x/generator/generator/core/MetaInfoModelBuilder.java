package com.x.generator.generator.core;

import cn.hutool.core.util.StrUtil;
import com.x.generator.generator.config.GlobalConfig;
import com.x.generator.generator.constant.DbConst;
import com.x.generator.generator.entity.CommonMetaInfo;
import com.x.generator.generator.entity.MetaInfoModel;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lsx
 * @date 2024-07-13
 */
public class MetaInfoModelBuilder {

    public static List<CommonMetaInfo> build(GlobalConfig config) {
        //如果没有设置数据库名，则从dbUrl获取
        if (StringUtils.isBlank(config.getDbName())) {
            String dbUrl = config.getDbUrl();
            int begin = dbUrl.lastIndexOf("/") + 1;
            int end = dbUrl.indexOf("?") > 0 ? dbUrl.indexOf("?") : dbUrl.length();
            config.setDbName(dbUrl.substring(begin, end));
        }
        //数据库连接
        Connection conn = com.x.generator.generator.core.DbConnection.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPassword());
        List<CommonMetaInfo> list = new ArrayList<>(6);
        List<String> tableNames = config.getTableNames();
        //获取表字段信息
        for (String tableName : tableNames) {
            list.add(buildMetaInfoModel(conn, config, tableName));
        }
        com.x.generator.generator.core.DbConnection.close(conn);
        return list;
    }

    /**
     * 构建元数据模型
     *
     * @param conn
     * @param config
     * @param tableName
     * @return
     */
    private static MetaInfoModel buildMetaInfoModel(Connection conn, GlobalConfig config, String tableName) {
        String tableComment;
        MetaInfoModel.FieldInfo pkInfo = null;
        List<MetaInfoModel.FieldInfo> fieldInfoList;
        try {
            //获取指定表的名称
            // 3. 获取DatabaseMetaData对象
            DatabaseMetaData metaData = conn.getMetaData();
            // 4. 获取表信息和备注
            String catalog = null; // 数据库名，MySQL可以为null
            String schemaPattern = null; // schema模式名，MySQL中为数据库名
            String schema = null; // schema模式名，MySQL中为数据库名
            String tableNamePattern = tableName; // 表名模式
            String[] types = {"TABLE"};
            ResultSet tables = metaData.getTables(catalog, schemaPattern, tableNamePattern, types);
            tables.next();
            //表备注
            tableComment = tables.getString(DbConst.REMARKS);
            //获取不到表备注，则通过查询的方式获取【可能跟mysql版本有关】
            if (StringUtils.isBlank(tableComment)) {
                String format = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
                String sql = String.format(format, config.getDbName(), tableName);
                PreparedStatement state = conn.prepareStatement(sql);
                state.setString(1, config.getDbName());
                state.setString(2, tableName);
                ResultSet resultSet = state.executeQuery();
                if (resultSet.next()) {
                    tableComment = resultSet.getString("TABLE_COMMENT");
                }
            }

            // 获取主键信息，目前只支持单一主键
            ResultSet pkRs = conn.getMetaData().getPrimaryKeys(catalog, schema, tableName.toUpperCase());
            while (pkRs.next()) {
                String columnName = pkRs.getString(DbConst.COLUMN_NAME);
                pkInfo = MetaInfoModel.buildFieldInfo(columnName);
            }
            //获取指定表的列信息
            ResultSet colRs = conn.getMetaData().getColumns(catalog, schemaPattern, tableName.toUpperCase(), "%");
            fieldInfoList = new ArrayList<>(colRs.getFetchSize());
            while (colRs.next()) {
                String columnName = colRs.getString(DbConst.COLUMN_NAME);
                String columnTypeName = colRs.getString(DbConst.TYPE_NAME);
                String columnComment = colRs.getString(DbConst.REMARKS);
                MetaInfoModel.FieldInfo fieldInfo = MetaInfoModel.buildFieldInfo(columnName, columnTypeName, columnComment);
                //非主键字段加入到字段列表中
                if (MetaInfoModel.isPkFieldInfo(pkInfo, fieldInfo)) {
                    pkInfo = fieldInfo;
                } else {
                    fieldInfoList.add(fieldInfo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // 构建 ftl模板 模型数据
        MetaInfoModel metaInfoModel = new MetaInfoModel();
        String ignorePrefix = config.getTableIgnorePrefix();
        String author = config.getAuthor();
        String basePackage = config.getBasePackage();
        String createDate = config.getCreateDate();
        //忽略前缀字段
        metaInfoModel.setIgnorePrefix(ignorePrefix);
        //设置基础包
        metaInfoModel.setAuthor(author);
        metaInfoModel.setBasePackage(basePackage);
        metaInfoModel.setCreateDate(createDate);
        //设置表名
        metaInfoModel.setTableName(tableName);
        //设置表备注
        metaInfoModel.setTableComment(tableComment);
        //设置主键信息
        metaInfoModel.setPkFieldInfo(pkInfo);
        //设置非主键字段信息
        metaInfoModel.setFieldInfos(fieldInfoList);
        //构建完整数据模型
        MetaInfoModel.buildFinal(metaInfoModel, ignorePrefix);
        //设置实体变量名
        metaInfoModel.setLowerFirstEntityName(StrUtil.lowerFirst(metaInfoModel.getEntityName()));
        return metaInfoModel;
    }
}
