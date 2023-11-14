package com.example.excelconverter;

import com.example.excelconverter.constant.PathConstants;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;

import static com.example.excelconverter.constant.PathConstants.EXCEL_GEN_DEFAULT_PATH;

@SpringBootApplication
public class ExcelConverterApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ExcelConverterApplication.class);
        builder.headless(false).web(WebApplicationType.SERVLET).run(args);

        //创建文件夹
        File file = new File(EXCEL_GEN_DEFAULT_PATH);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                System.out.println("------------------------------" + EXCEL_GEN_DEFAULT_PATH + "，文件夹创建失败------------------------------");
                throw new RuntimeException("C:\\zExcelConverter\\，文件夹创建失败");
            } else {
                System.out.println("---------------------" + EXCEL_GEN_DEFAULT_PATH + "，文件夹创建成功，生成后的文件将放在此处---------------------");
            }
        }

        System.out.println("--------------------------------------------服务启动成功--------------------------------------------");
        System.out.println("--------------------------------------------服务启动成功--------------------------------------------");
        System.out.println("--------------------------------------------服务启动成功--------------------------------------------");
    }

}
