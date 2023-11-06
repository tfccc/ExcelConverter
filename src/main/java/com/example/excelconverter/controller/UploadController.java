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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        // 读取文件
        List<DebtInfoImportDto> readData = readFile(file);

        // 转为新的excel
        convert(readData);

        return "上传成功: " + file.getOriginalFilename();
    }


    /** 转为新的文件 **/
    private void convert(List<DebtInfoImportDto> data) {
        Map<String, List<DebtInfoImportDto>> byDebtSubject = data.stream()
                .collect(Collectors.groupingBy(DebtInfoImportDto::getDebtSubject));

        // 列宽 (时间+公司+总计)
        int width = byDebtSubject.size() + 2;

        // <日期，<名称，金额（本金+利息）>>
        HashMap<String, Map<String, BigDecimal>> dateAndAmountMap = new HashMap<>();

        for (Map.Entry<String, List<DebtInfoImportDto>> entry : byDebtSubject.entrySet()) {
            // 债务主体
            String name = entry.getKey();
            // 债务主体对应的数据
            List<DebtInfoImportDto> list = entry.getValue();


            for (DebtInfoImportDto dto : list) {

            }
        }

    }

    /** 读取文件数据 **/
    private List<DebtInfoImportDto> readFile(MultipartFile file) {
        ExcelImportResult<DebtInfoImportDto> result;

        try {
            ImportParams importParams = new ImportParams();
            importParams.setHeadRows(2);
            result = ExcelImportUtil.importExcelMore(
                    file.getInputStream(),
                    DebtInfoImportDto.class,
                    importParams
            );
            return result.getList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
