package com.x.generator.generator.entity;

import lombok.Data;

import java.io.File;

/**
 * @author lsx
 * @date 2024-07-13
 */
@Data
public class FileInfo {

    private String projectDir;
    /**
     * 基础输入路径
     */
    private String baseInputPath;
    /**
     * 基础输出路径
     */
    private String baseOutputPath;

    /**
     * 模板文件实体名
     */
    private String entityName;

    //==================生成对应类的前缀报名
    /**
     *
     */
    private String controllerDir = "controller";
    /**
     *
     */
    private String businessDir = "business";
    /**
     *
     */
    private String serviceDir = joinPath("db", "service");
    /**
     *
     */
    private String serviceImplDir = joinPath(serviceDir, "impl");
    /**
     *
     */
    private String mapperDir = joinPath("db", "dao");
    /**
     *
     */
    private String mapperXmlDir = joinPath("db", "mapper");
    /**
     *
     */
    private String modelDir = "model";
    /**
     *
     */
    private String dtoDir = "dto";
    /**
     *
     */
    private String voDir = "vo";

    //============== 指定类名格式
    /**
     *
     */
    private String serviceNameFormat = "I%sService";
    /**
     *
     */
    private String serviceImplNameFormat = "%sService";

    {
        projectDir = System.getProperty("user.dir");
    }

    public static String joinPath(String parentPath, String... childPaths) {
        StringBuilder sb = new StringBuilder(parentPath);
        for (String childPath : childPaths) {
            sb.append(File.separator).append(childPath);
        }
        return sb.toString();
    }
}
