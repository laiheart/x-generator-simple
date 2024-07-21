package com.x.generator.generator;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.x.generator.config.GlobalConfig;
import com.x.generator.config.PathInfo;
import com.x.generator.constant.BaseConst;
import com.x.generator.entity.EntityMetaData;
import com.x.generator.core.EntityMetaDataBuilder;
import com.x.generator.entity.MetaDataContainer;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author lsx
 * @date 2024-07-13
 */
public class FileGenerator {

    public static void main(String[] args) throws TemplateException, IOException, IllegalAccessException, InterruptedException {
        generateFile();
    }

    public static void generateFile() throws TemplateException, IOException, IllegalAccessException {
        //配置信息
        GlobalConfig config = new GlobalConfig();
        //配置数据库等信息
        config.setDbUrl("jdbc:mysql://localhost:3306/xindada?useSSL=false&serverTimezone=UTC");
        config.setDbUser("root");
        config.setDbPassword("root");
        //需要生成的表
        config.setTableNames("user_answer", "app");
        config.setAuthor("lsx");
        String basePackage = "com.x.generator.generate";
        config.setBasePackage(basePackage);
        config.setTableIgnorePrefix("t_");
        // 获取整个项目的根路径
        String projectPath = System.getProperty("user.dir") + File.separator;
        PathInfo pathInfo = new PathInfo();
        // 模板文件基础路径
        pathInfo.setBaseInputPath(projectPath + "src/main/resources/tpl/");
        // 输出文件相对路径路径
        String baseDirectory = basePackage.replaceAll("\\.", "/");
        // 输出文件基础路径
        pathInfo.setBaseOutputPath(projectPath + "src/main/java/" + baseDirectory);
        // 实体类所在路径
        pathInfo.setModelTplDir("model/v1");
        // 实体类模板文件名
        pathInfo.setEntityTplName("Entity");
        // 各种层之间的包名
        pathInfo.setControllerDir("controller");
        pathInfo.setBusinessDir("business");
        // 生成文件
        doGenerate(pathInfo, config);
    }

    public static void doGenerate(PathInfo pathInfo, GlobalConfig config) throws IOException, TemplateException, IllegalAccessException {
        // new 出 Configuration 对象，参数为 FreeMarker 版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        // 指定模板文件所在的基础路径
        String baseInputPath = pathInfo.getBaseInputPath();
        File templateDir = new File(baseInputPath);
        configuration.setDirectoryForTemplateLoading(templateDir);

        // 设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");

        //构建所有模型信息
        List<MetaDataContainer> list = EntityMetaDataBuilder.build(config);

        //基础的输出路径
        String baseOutputPath = pathInfo.getBaseOutputPath();
        //model 模板路径
        String modelPath = pathInfo.getModelTplDir();
        //model 模板文件名前缀
        String entityTplName = pathInfo.getEntityTplName();
        for (MetaDataContainer container : list) {
            //实体类生成模板的model
            EntityMetaData entityMetaData = container.getEntity();
            String entityName = entityMetaData.getEntityName();
            String entityPackage = entityMetaData.getEntityPackage();
            Class<? extends MetaDataContainer> clz = container.getClass();
            //获取 实体元数据所有的字段及对应值
            Field[] fields = clz.getDeclaredFields();
            //实体类文件生成
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = StrUtil.upperFirst(field.getName());
                String outChildPath;
                String inputChildPath;
                if ("entity".equalsIgnoreCase(fieldName)) {
                    outChildPath = "model" + File.separator + entityName;
                    inputChildPath = entityTplName;
                } else if ("vo".equalsIgnoreCase(fieldName)) {
                    outChildPath = "vo" + File.separator + entityName + fieldName;
                    inputChildPath = entityTplName + fieldName;
                } else {
                    outChildPath = "dto" + File.separator + entityPackage + File.separator + entityName + fieldName;
                    inputChildPath = entityTplName + fieldName;
                }
                //输出文件路径
                String outputPath = getOutputPath(baseOutputPath, outChildPath);
                // 创建模板对象，加载指定模板
                String templateRelaPath = getTemplateRelaPath(modelPath,
                        inputChildPath);
                generateFile(templateRelaPath, outputPath, field.get(container), configuration);
            }
            //controller生成
            String serviceTplDir = pathInfo.getServiceTplDir();
            String outputPath = getOutputPath(baseOutputPath, PathInfo.joinPath(pathInfo.getControllerDir(), entityName + "Controller"));
            generateFile(getTemplateRelaPath(serviceTplDir, BaseConst.CONTROLLER_NAME), outputPath, entityMetaData, configuration);

            //business
            outputPath = getOutputPath(baseOutputPath, PathInfo.joinPath(pathInfo.getBusinessDir(), entityName + "Business"));
            generateFile(getTemplateRelaPath(serviceTplDir, BaseConst.BUSINESS_NAME), outputPath, entityMetaData, configuration);

            //service serviceImpl
            String serviceName = String.format(pathInfo.getServiceNameFormat(), entityName);
            outputPath = getOutputPath(baseOutputPath, PathInfo.joinPath(pathInfo.getServiceDir(), serviceName));
            generateFile(getTemplateRelaPath(serviceTplDir, BaseConst.SERVICE_NAME), outputPath, entityMetaData, configuration);
            String serviceImplName = String.format(pathInfo.getServiceImplNameFormat(), entityName);
            outputPath = getOutputPath(baseOutputPath, PathInfo.joinPath(pathInfo.getServiceImplDir(), serviceImplName));
            generateFile(getTemplateRelaPath(serviceTplDir, BaseConst.SERVICE_IMPL_NAME), outputPath, entityMetaData, configuration);

            //mapper
            outputPath = getOutputPath(baseOutputPath, PathInfo.joinPath(pathInfo.getMapperDir(), entityName + "Mapper"));
            generateFile(getTemplateRelaPath(serviceTplDir, BaseConst.MAPPER_NAME), outputPath, entityMetaData, configuration);

            //mapperXml
            outputPath = getXmlOutputPath(baseOutputPath, PathInfo.joinPath(pathInfo.getMapperXmlDir(), entityName + "Mapper"));
            generateFile(getXmlTemplateRelaPath(serviceTplDir, BaseConst.MAPPER_NAME), outputPath, entityMetaData, configuration);

        }
    }

    public static void generateFile(String templateInputRelaPath, String outputPath, Object model, Configuration configuration) {
        // 文件不存在则创建文件和父目录
        if (!FileUtil.exist(outputPath)) {
            FileUtil.touch(outputPath);
        }
        Writer out = null;
        try {
            out = new FileWriter(outputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 创建模板对象，加载指定模板
        Template template = null;
        try {
            template = configuration.getTemplate(templateInputRelaPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            template.process(model, out);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getOutputPath(String baseOutputPath, String childPath) {
        String path = baseOutputPath + File.separator + childPath + ".java";
        path = path.replaceAll("\\\\", "/");
        return path;
    }

    public static String getXmlOutputPath(String baseOutputPath, String childPath) {
        String path = baseOutputPath + File.separator + childPath + ".xml";
        path = path.replaceAll("\\\\", "/");
        return path;
    }

    public static String getTemplateRelaPath(String relativePath, String childPath) {
        String path = relativePath + File.separator + childPath + ".java.ftl";
        path = path.replaceAll("\\\\", "/");
        return path;
    }

    public static String getXmlTemplateRelaPath(String relativePath, String childPath) {
        String path = relativePath + File.separator + childPath + ".xml.ftl";
        path = path.replaceAll("\\\\", "/");
        return path;
    }

    private static void testTimer() throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new TimeTaskTest(), 1000);
        Thread.sleep(1000 * 10);
        timer.schedule(new TimeTaskTest(), 1000);
    }

    static class TimeTaskTest extends TimerTask {
        @Override
        public void run() {

        }
    }
}
