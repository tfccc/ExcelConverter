package com.example.excelconverter.util;

import com.example.excelconverter.test.Tester;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Frank.Tang
 * @date 2023-11-06 10:55
 * @desc
 **/
public class FileUtil {


    /**
     * 用浏览器打开网页
     * @author Frank.Tang
     * @param filePath 文件路径 (resource目录下的相对路径)
     */
    public static void browserOpenFile(String filePath) {
        URL resourceUrl = Tester.class.getResource(filePath);
        if (resourceUrl == null) {
            throw new RuntimeException("文件未找到");
        }
        try {
            Desktop.getDesktop().browse(resourceUrl.toURI());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("文件打开失败");
        }
    }

}
