package com.example.excelconverter.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.example.excelconverter.entity.DebtInfoImportDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Frank.Tang
 * @date 2023-11-06 11:35
 * @desc
 **/
@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UploadController {


    @PostMapping("/test")
    public String uploadServerFile(MultipartFile file, HttpServletRequest req) {
        if (file == null) {
            throw new RuntimeException("请选择文件后再上传");
        }

        readFile(file);

        return "上传成功: " + file.getOriginalFilename();
    }

    private List<DebtInfoImportDto> readFile(MultipartFile file) {
        ExcelImportResult<DebtInfoImportDto> result;

        try {
            ImportParams importParams = new ImportParams();
            importParams.setTitleRows(1);
            importParams.setHeadRows(2);
            result = ExcelImportUtil.importExcelMore(
                    file.getInputStream(),
                    DebtInfoImportDto.class,
                    importParams
            );

            List<DebtInfoImportDto> res = fillFileColum(result);

            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 填充第一列
     * @author Frank.Tang
     */
    private static List<DebtInfoImportDto> fillFileColum(ExcelImportResult<DebtInfoImportDto> result) {
        List<DebtInfoImportDto> res = result.getList();
        // 填充第一列
        for (int i = 0; i < res.size(); i++) {
            DebtInfoImportDto dto = res.get(i);
            String crtGroupName = dto.getGroupName();
            // 当前不为空
            if (crtGroupName != null) {
                // 从下一个开始
                for (int j = i+1; j < res.size(); j++) {
                    DebtInfoImportDto nxtDto = res.get(j);
                    if (nxtDto.getGroupName() != null) {
                        break;
                    }
                    nxtDto.setGroupName(crtGroupName);
                }
            }
        }
        return res;
    }
}
