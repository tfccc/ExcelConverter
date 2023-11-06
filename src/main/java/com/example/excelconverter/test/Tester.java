package com.example.excelconverter.test;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Frank.Tang
 * @date 2023-11-06 10:36
 * @desc
 **/
public class Tester {

    public static void main(String[] args) {
        URL resourceUrl = Tester.class.getResource("/static/html/upload.html");
        if (resourceUrl == null) {
            throw new RuntimeException("文件未找到");
        }
        try {
            Desktop.getDesktop().browse(resourceUrl.toURI());
        } catch (IOException | URISyntaxException e) {
            System.out.println("文件打开失败");
            throw new RuntimeException(e);
        }
    }

}
