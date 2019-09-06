package cn.yiyang.common.utils;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.*;

/**
 * @ClassName CodeGenerator
 * @Description 基于mybatisplus, 用于生成代码
 * @Author Administrator
 * @Date 2019/1/7 10:24
 * @Version 1.0
 **/
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("wangyiyang");
        gc.setOpen(false);
        gc.setFileOverride(true);
        gc.setActiveRecord(true); // 开启orm模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML ColumnList
        gc.setDateType(DateType.ONLY_DATE);//设置MySql 的Date类型

        gc.setEntityName("%sEntity"); // 设置实体名称
        gc.setServiceName("%sService"); //设置Service接口层名称
        gc.setSwagger2(true); // 开启Swagger2
        gc.setIdType(IdType.AUTO); // 设置主键的生成策略(自增， uuid...)
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/demo_02?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT%2B8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName(scanner("模块名"));
        //******* 此处用于填写模块名 *********
        pc.setModuleName("demo");
        pc.setParent("cn.yiyang"); // 父包名
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                map.put("entityLombokModel",true); // 设置@Data
                map.put("restControllerStyle",true); // 设置@RestController
                this.setMap(map);
            }
        };

        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
         String templatePath = "/template/gen-template/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        templateConfig.setController("/template/gen-template/controller.java.vm");
        templateConfig.setEntity("/template/gen-template/entity.java.vm");
        templateConfig.setMapper("/template/gen-template/mapper.java.vm");
        templateConfig.setService("/template/gen-template/service.java.vm");
        templateConfig.setServiceImpl("/template/gen-template/serviceImpl.java.vm");

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel); // 数据库表映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel); // 数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
        strategy.setEntityLombokModel(true); //@Data
        strategy.setEntityBuilderModel(true); // @Builder
        strategy.setRestControllerStyle(true); // @RestController

        // 新增
        strategy.setVersionFieldName("version"); // 指定乐观锁名称
        strategy.setLogicDeleteFieldName("ban"); // 指定逻辑删除名称


        // *** 配置要生成代码的表 ***
        strategy.setInclude("table_1");
//        strategy.setControllerMappingHyphenStyle(true); // 驼峰转连字符 Controller层: userModel   ->  user-model
        strategy.setEntityTableFieldAnnotationEnable(true); // 是否生成实体时，生成字段注解
        strategy.setTablePrefix(pc.getModuleName() + "_"); //表前缀



        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }

}
