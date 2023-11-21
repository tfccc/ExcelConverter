package com.example.excelconverter.config;

import com.example.excelconverter.entity.ComResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 全局异常处理
 * @author Frank.Tang
 * @param
 * @return *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ComResult handleException(Exception e) {
        return ComResult.error(e.getMessage());
    }

}