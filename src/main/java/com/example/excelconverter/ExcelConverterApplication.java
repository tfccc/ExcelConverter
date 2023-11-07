package com.example.excelconverter;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ExcelConverterApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ExcelConverterApplication.class);
        builder.headless(false).web(WebApplicationType.SERVLET).run(args);

        System.out.println("-----------------------服务启动成功-----------------------");
        System.out.println("-----------------------服务启动成功-----------------------");
        System.out.println("-----------------------服务启动成功-----------------------");
        //打开网页
        //FileUtil.browserOpenFile("/static/html/convert.html");
    }

}
