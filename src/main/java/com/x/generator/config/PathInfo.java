package com.x.generator.config;

import lombok.Data;

import java.io.File;

/**
 * @author lsx
 * @date 2024-07-13
 */
@Data
public class PathInfo {

    private String projectDir;

    private String baseInputPath;

    private String baseOutputPath;

    private String modelDir = "model" + File.separator + "v1";
    /**
     * 第一个.前的名称
     */
    private String entityTplName = "Entity";
    /**
     *
     */
    private String serviceTplDir = "service" + File.separator + "v1";
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
    private String serviceDir = "db" + File.separator + "service";
    /**
     *
     */
    private String serviceImplDir = joinPath(serviceDir, "impl");
    /**
     *
     */
    private String serviceNameFormat = "I%sService";
    /**
     *
     */
    private String serviceImplNameFormat = "%sService";
    /**
     *
     */
    private String mapperDir = "db" + File.separator + "dao";

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
