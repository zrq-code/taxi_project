package com.mashibing.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 自动生成代码工具类
 */
public class MysqlGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/service_price?characterEncoding=utf-8&serverTimezone=GMT%2B8",
                        "root", "root").globalConfig(builder -> {
                    builder.author("rick zhou").fileOverride().outputDir("/Users/Rick_Zhou/IdeaProjects/online-taxi-public/service-price/src/main/java");
                })
                .packageConfig(builder -> {
                    builder.parent("com.mashibing").pathInfo(Collections
                            .singletonMap(OutputFile.mapperXml, "/Users/Rick_Zhou/IdeaProjects/online-taxi-public/service-price/src/main/java/com/mashibing/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude("price_rule");
                })
                .templateEngine(new FreemarkerTemplateEngine()).execute();
    }
}
