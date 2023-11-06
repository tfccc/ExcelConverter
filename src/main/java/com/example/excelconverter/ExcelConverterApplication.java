package com.example.excelconverter;

import com.example.excelconverter.util.FileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExcelConverterApplication {

    public static void main(String[] args) {
        //启动web服务
        SpringApplication.run(ExcelConverterApplication.class, args);

        //打开网页
        FileUtil.browserOpenFile("/static/html/upload.html");
    }

}
