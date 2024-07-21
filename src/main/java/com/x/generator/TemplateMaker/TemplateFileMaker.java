package com.x.generator.TemplateMaker;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 模板生成工具
 *
 * @author lsx
 * @date 2024-07-21
 */
public class TemplateFileMaker {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        Map<String, String> map = new HashMap<>(16);
        //日期
        String createDateRegex = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])";
        map.put(createDateRegex, "createDate");
        //作者
        map.put("lsx", "author");
        //基础包名
        map.put("com.ection.platform.photo.sticker", "basePackage");
        //实体名
        map.put("PhotoStickerFrameTpl", "entityName");
        //实体变量名
        map.put("photoStickerFrameTpl", "lowerFirstEntityName");
        //实体包名
        map.put("photostickerframetpl", "entityPackage");
        doMake(projectPath + "/src/main/resources/tpl/file",
                projectPath + "/src/main/resources/tpl/file_", map);
    }

    public static void doMake(String baseInputDir, String baseOutputDir, Map<String, String> keyPropertyMap) {
        File file = new File(baseInputDir);
        for (File item : Objects.requireNonNull(file.listFiles())) {
            if (item.isDirectory()) {
                String inputPath = new File(baseInputDir, item.getName()).getPath();
                String outputPath = new File(baseOutputDir, item.getName()).getPath();
                doMake(inputPath, outputPath, keyPropertyMap);
                continue;
            }
            String inputPath = item.getPath();
            //构建生成文件信息
            TemplateInfo info = new TemplateInfo();
            info.setKeyPropertyMap(keyPropertyMap);
            info.setInputPath(inputPath);
            String outputName = item.getName() + ".ftl";
            String outputPath = new File(baseOutputDir, outputName).getPath();
            info.setOutputPath(outputPath);
            doMake(info);
        }

    }

    public static void doMake(TemplateInfo... templateInfos) {
        for (TemplateInfo templateInfo : templateInfos) {
            String inputPath = templateInfo.getInputPath();
            String outputPath = templateInfo.getOutputPath();
            Map<String, String> map = templateInfo.getKeyPropertyMap();
            //替换后的模板文件
            String srcData = FileUtil.readUtf8String(inputPath);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String value = entry.getValue();
                entry.setValue(getTemplateVal(value));
            }
            String destData = replaceAll(srcData, map);
            File ouputFile = new File(outputPath);
            if (!ouputFile.exists()) {
                FileUtil.touch(ouputFile);
            }
            FileUtil.writeUtf8String(destData, ouputFile);
        }
    }

    private static String getTemplateVal(String property) {
        if (property.contains("\\$\\{")) {
            return property;
        }
        return String.format("\\$\\{%s\\}", property);
    }

    private static String replaceAll(String srcData, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            srcData = srcData.replaceAll(entry.getKey(), entry.getValue());
        }
        return srcData;
    }
}
