package com.example.excelconverter.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.example.excelconverter.constant.StringConstants;
import com.example.excelconverter.entity.DebtInfoImportDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.scope.DummyScope;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        if (CollectionUtils.isEmpty(readData)) {
            return "没有读取到数据";
        }

        // 转为新的excel
        convert(readData);

        return "上传成功: " + file.getOriginalFilename();
    }


    /** 转为新的文件 **/
    private void convert(List<DebtInfoImportDto> data) {
        Map<String, List<DebtInfoImportDto>> byDebtSubject = data.stream()
                .collect(Collectors.groupingBy(DebtInfoImportDto::getDebtSubject));

        // 所有日期
        Set<String> dateSet = new HashSet<>();

        for (DebtInfoImportDto dto : data) {
            dateSet.add(dto.getInterestDate());
            dateSet.add(dto.getPrincipalDate());
        }

        // 列宽 (1时间+公司+1总计)
        int width = byDebtSubject.size() + 2;
        // 行数 (4表头+数据+1总计)
        int height = dateSet.size() + 5;

        // <公司，<日期，金额（本金+利息）>>
        Map<String, Map<String, BigDecimal>> collectiveMap = getCollectiveMap(byDebtSubject);

        // <公司, 总计>

        // <日期, 总计>

    }

    /**
     * 渲染map
     * <公司，<日期，金额(本金+利息)>>
     **/
    private Map<String, Map<String, BigDecimal>> getCollectiveMap(Map<String, List<DebtInfoImportDto>> byDebtSubject) {
        Map<String, Map<String, BigDecimal>> res = new HashMap<>();

        for (Map.Entry<String, List<DebtInfoImportDto>> nameAndDaMap : byDebtSubject.entrySet()) {
            // 债务主体
            String name = nameAndDaMap.getKey();
            // 债务主体对应的数据
            List<DebtInfoImportDto> list = nameAndDaMap.getValue();

            for (DebtInfoImportDto dto : list) {
                Map<String, BigDecimal> daMap = res.get(name);
                String principalDate = dto.getPrincipalDate();
                String interestDate = dto.getInterestDate();
                BigDecimal principal = dto.getPrincipal() == null ? BigDecimal.ZERO : dto.getPrincipal();
                BigDecimal interest = dto.getInterest() == null ? BigDecimal.ZERO : dto.getInterest();

                // 没有
                if (daMap == null) {
                    Map<String, BigDecimal> ndaMap = new HashMap<>();

                    ndaMap.put(interestDate, interest);
                    ndaMap.put(principalDate, principal);

                    ndaMap.remove(null);
                    res.put(name, ndaMap);
                }
                // 已有
                else {
                    if (interestDate != null && daMap.containsKey(interestDate)) {
                        daMap.put(interestDate, daMap.get(interestDate).add(interest));
                    } else {
                        daMap.put(interestDate, interest);
                    }
                    if (principalDate != null && daMap.containsKey(principalDate)) {
                        daMap.put(principalDate, daMap.get(principalDate).add(principal));
                    } else {
                        daMap.put(principalDate, principal);
                    }
                    daMap.remove(null);
                }
            }
        }
        return res;
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

            // 去掉部分数据
            result.getList().removeIf(o ->
                    StringUtils.isEmpty(o.getDebtSubject()) ||
                            o.getDebtSubject().contains(StringConstants.SPECIAL_STR_1) ||
                            o.getDebtSubject().contains(StringConstants.SPECIAL_STR_2) ||
                            o.getDebtSubject().contains(StringConstants.SPECIAL_STR_3)
            );

            return result.getList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
