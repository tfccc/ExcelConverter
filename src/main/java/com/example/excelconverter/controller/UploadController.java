package com.example.excelconverter.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.example.excelconverter.constant.PathConstants;
import com.example.excelconverter.constant.StringConstants;
import com.example.excelconverter.entity.ComResult;
import com.example.excelconverter.entity.DebtInfoImportDto;
import com.example.excelconverter.esaypoi.ExcelExportStyler;
import com.example.excelconverter.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
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


    @PostMapping("/uploadAndConvert")
    public ComResult uploadServerFile(MultipartFile file, HttpServletRequest req) {
        // 没传文件
        if (file == null) {
            throw new RuntimeException("请先选择文件");
        }
        // 不是excel文件
        if (!file.getOriginalFilename().endsWith(".xls")
                && !file.getOriginalFilename().endsWith(".xlsx")) {
            throw new RuntimeException("仅支持xls/xlsx格式的文件");
        }

        // 读取文件
        List<DebtInfoImportDto> readData = readFile(file);

        // 转为新的excel
        convert(readData, file.getOriginalFilename());

        return ComResult.success("生成成功");
    }


    /** 转为新的文件 **/
    @SuppressWarnings("unchecked")
    private void convert(List<DebtInfoImportDto> data, String fileName) {
        Map<String, List<DebtInfoImportDto>> byDebtSubject = data.stream()
                .collect(Collectors.groupingBy(DebtInfoImportDto::getDebtSubject));

        // <公司，<日期，金额（本金+利息）>>
        Map<String, Map<String, BigDecimal>> collectiveMap = getCollectiveMap(byDebtSubject);

        Object[] twoMap = getTotalAmountByNameMap(collectiveMap);
        // <公司, 总计>
        Map<String, BigDecimal> totalByNameMap = (Map<String, BigDecimal>) twoMap[0];
        // <日期, 总计>
        Map<String, BigDecimal> totalByDateMap = (Map<String, BigDecimal>) twoMap[1];
        // 总计
        BigDecimal allTotal = totalByDateMap.values()
                .stream()
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        // 公司set
        Set<String> nameSet = totalByNameMap.keySet();

        // 构建单元格结构+单元格对应的数据
        Object[] sheetAndData = buildSheetAndData(collectiveMap, totalByNameMap, totalByDateMap, allTotal, nameSet);
        List<ExcelExportEntity> colList = (List<ExcelExportEntity>) sheetAndData[0];
        List<Map<String, Object>> valList = (List<Map<String, Object>>) sheetAndData[1];

        // xxxx年xx日
        DebtInfoImportDto oneData = data.get(0);
        String ym = getYearAndMonth(oneData.getInterestDate() == null ? oneData.getPrincipalDate() : oneData.getInterestDate());
        String titleAndSheetName = "XX集团" + ym + "还本付息汇总表";

        ExportParams exportParams = new ExportParams(titleAndSheetName, titleAndSheetName);
        exportParams.setFixedTitle(false);
        exportParams.setStyle(ExcelExportStyler.class);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, colList, valList);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(
                    PathConstants.EXCEL_GEN_DEFAULT_PATH + titleAndSheetName + "_" +
                            DateUtil.getDateStr(DateUtil.YEAR_MONTH_DAY_HOUR_0) +
                            (fileName.endsWith(".xls") ? ".xls" : ".xlsx")
            );
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException("文件生成过程中发生异常");
        }
    }

    /** 构建单元格结构+单元格对应的数据 **/
    private Object[] buildSheetAndData(Map<String, Map<String, BigDecimal>> collectiveMap,
                                       Map<String, BigDecimal> totalByNameMap, Map<String, BigDecimal> totalByDateMap,
                                       BigDecimal allTotal, Set<String> nameSet) {
        List<ExcelExportEntity> colList = new ArrayList<>();
        ExcelExportEntity colEntity = new ExcelExportEntity("时间", "date", 18);
        colList.add(colEntity);

        ExcelExportEntity deliColGroup = new ExcelExportEntity("债务主体", "debtSubject");
        List<ExcelExportEntity> deliColList = new ArrayList<>();
        // n列公司
        for (String name : nameSet) {
            ExcelExportEntity col = new ExcelExportEntity(name, name, 15);
            deliColList.add(col);
            deliColGroup.setList(deliColList);
        }
        // 最后一列总计
        ExcelExportEntity lastCol = new ExcelExportEntity("总计", "total", 15);
        deliColList.add(lastCol);
        colList.add(deliColGroup);

        // 表格填充值
        List<Map<String, Object>> valList = new ArrayList<>();

        /////////////////////// n行数据 ///////////////////////
        for (Map.Entry<String, BigDecimal> entry : totalByDateMap.entrySet()) {
            String date = entry.getKey();
            BigDecimal total = entry.getValue();

            Map<String, Object> valMap = new HashMap<>();
            List<Map<String, Object>> deliDetailList = new ArrayList<>();
            Map<String, Object> deliValMap = new HashMap<>();

            // put当前，公司对应的金额
            for (String name : nameSet) {
                Map<String, BigDecimal> dateAndAmoutMap = collectiveMap.get(name);
                if (dateAndAmoutMap == null) {
                    continue;
                }
                deliValMap.put(name, dateAndAmoutMap.get(date));
            }
            // put当前行的总计
            deliValMap.put("total", total);
            deliDetailList.add(deliValMap);
            // put当前行的日期
            valMap.put("date", date);
            valMap.put("debtSubject", deliDetailList);
            valList.add(valMap);
        }

        /////////////////////// 最后一行总计 ///////////////////////
        Map<String, Object> valMap = new HashMap<>();
        List<Map<String, Object>> deliDetailList = new ArrayList<>();
        Map<String, Object> deliValMap = new HashMap<>();

        // put最后一行公司对应的金额
        for (String name : nameSet) {
            deliValMap.put(name, totalByNameMap.get(name));
        }
        // put最后一行的总计
        deliValMap.put("total", allTotal);
        deliDetailList.add(deliValMap);
        // put最后一行的日期
        valMap.put("date", "总计");
        valMap.put("debtSubject", deliDetailList);
        valList.add(valMap);

        return new Object[]{colList, valList};
    }

    /**
     * 渲染map
     * Object[0] = <公司名，总金额>
     * Object[1] = <日期，总金额>
     **/
    private Object[] getTotalAmountByNameMap(Map<String, Map<String, BigDecimal>> collectiveMap) {
        Map<String, BigDecimal> nameMap = new TreeMap<>();
        Map<String, BigDecimal> dateMap = new TreeMap<>();

        for (Map.Entry<String, Map<String, BigDecimal>> entry : collectiveMap.entrySet()) {
            // 公司名
            String name = entry.getKey();
            // <日期，金额>
            Map<String, BigDecimal> value = entry.getValue();

            for (Map.Entry<String, BigDecimal> innerEntry : value.entrySet()) {
                // 日期
                String date = innerEntry.getKey();
                // 金额
                BigDecimal amount = innerEntry.getValue();
                // 有这个公司
                if (nameMap.containsKey(name)) {
                    nameMap.put(name, nameMap.get(name).add(amount));
                } else {
                    nameMap.put(name, amount);
                }
                // 有这个日期
                if (dateMap.containsKey(date)) {
                    dateMap.put(date, dateMap.get(date).add(amount));
                } else {
                    dateMap.put(date, amount);
                }
            }
        }
        return new Object[]{new LinkedHashMap<>(nameMap), new LinkedHashMap<>(dateMap)};
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
                    if (principalDate != null && principalDate.equals(interestDate)) {
                        ndaMap.put(principalDate, ndaMap.get(principalDate).add(principal));
                    } else {
                        ndaMap.put(principalDate, principal);
                    }

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
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(2);

        try {
            result = ExcelImportUtil.importExcelMore(
                    file.getInputStream(),
                    DebtInfoImportDto.class,
                    importParams
            );
        } catch (Exception e) {
            throw new RuntimeException("文件读取失败");
        }

        // 去掉部分数据
        List<DebtInfoImportDto> res = result.getList();

        res.removeIf(o -> StringUtils.isEmpty(o.getDebtSubject()) ||
                o.getDebtSubject().contains(StringConstants.SPECIAL_STR_1) ||
                o.getDebtSubject().contains(StringConstants.SPECIAL_STR_2) ||
                o.getDebtSubject().contains(StringConstants.SPECIAL_STR_3)
        );

        if (CollectionUtils.isEmpty(res)) {
            throw new RuntimeException("文件中没有读取到有效数据");
        }
        return res;
    }


    private final static String DATE_REGEX = "\\d+年\\d+月\\d+日";
    private final static Pattern DATE_PATTERN = Pattern.compile(DATE_REGEX);


    /** 2023年11月7日 提取 2023年11月  **/
    private static String getYearAndMonth(String date) {
        if (date == null || !DATE_PATTERN.matcher(date).matches()) {
            return null;
        }
        return date.split("\\d+日")[0];
    }

}
