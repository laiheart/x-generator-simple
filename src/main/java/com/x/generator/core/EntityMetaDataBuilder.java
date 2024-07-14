package com.x.generator.core;

import cn.hutool.core.util.StrUtil;
import com.x.generator.config.GlobalConfig;
import com.x.generator.constant.DbConst;
import com.x.generator.entity.DbMetaData;
import com.x.generator.entity.EntityMetaData;
import com.x.generator.entity.MetaDataContainer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lsx
 * @date 2024-07-13
 */
public class EntityMetaDataBuilder {

    public static List<MetaDataContainer> build(GlobalConfig config) {
        Connection conn = DbConnection.getConnection();
        List<MetaDataContainer> list = new ArrayList<>(6);
        List<String> tableNames = config.getTableNames();
        for (String tableName : tableNames) {
            MetaDataContainer container = new MetaDataContainer();

            EntityMetaData entityMetaData = buildEntityMetaData(conn, tableName, config.getTableIgnorePrefix(), config.getAuthor(),
                    config.getBasePackage(), config.getCreateDate());
            //表对应实体
            container.setEntity(entityMetaData);
            list.add(container);
            //添加
            container.setAddReq(entityMetaData);
            //查询
            container.setQueryReq(entityMetaData);
            //编辑
            container.setEditReq(entityMetaData);
            //显示
            container.setVo(entityMetaData);
        }

        DbConnection.close(conn);
        return list;
    }

    private static EntityMetaData buildEntityMetaData(Connection conn, String tableName, String ignorePrefix,
                                                      String author, String basePackage, String createDate) {
        EntityMetaData entityMetaData;
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
            String tableComment = tables.getString(DbConst.REMARKS);

            //设置数据库元信息表名
            DbMetaData dbMetaData = new DbMetaData();
            dbMetaData.setTableName(tableName);
            dbMetaData.setTableComment(tableComment);

            ResultSet pkRs = conn.getMetaData().getPrimaryKeys(catalog, schema, tableName.toUpperCase());
            while (pkRs.next()) {
                String columnName = pkRs.getString(DbConst.COLUMN_NAME);
                dbMetaData.setPkFieldInfo(DbMetaData.buildFieldInfo(columnName, null, null));
            }
            ResultSet colRs = conn.getMetaData().getColumns(catalog, schemaPattern, tableName.toUpperCase(), "%");
            List<DbMetaData.FieldInfo> fieldInfos = new ArrayList<>(colRs.getRow());
            dbMetaData.setFieldInfos(fieldInfos);
            while (colRs.next()) {
                String columnName = colRs.getString(DbConst.COLUMN_NAME);
                String typeName = colRs.getString(DbConst.TYPE_NAME);
                String columnComment = colRs.getString(DbConst.REMARKS);
                DbMetaData.FieldInfo fieldInfo = DbMetaData.buildFieldInfo(columnName, typeName, columnComment);
                if (dbMetaData.isPkFieldInfo(fieldInfo)) {
                    dbMetaData.setPkFieldInfo(fieldInfo);
                } else {
                    fieldInfos.add(fieldInfo);
                }
            }
            entityMetaData = EntityMetaData.copyFromDbMetaData(dbMetaData);
            EntityMetaData.buildFinal(entityMetaData, ignorePrefix);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        entityMetaData.setBasePackage(basePackage);
        entityMetaData.setAuthor(author);
        entityMetaData.setCreateDate(createDate);
        entityMetaData.setLowerFirstEntityName(StrUtil.lowerFirst(entityMetaData.getEntityName()));
        return entityMetaData;
    }
}
