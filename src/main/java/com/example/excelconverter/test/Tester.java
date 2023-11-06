package com.example.excelconverter.test;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.example.excelconverter.esaypoi.ExcelExportStyler;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Frank.Tang
 * @date 2023-11-06 10:36
 * @desc
 **/
public class Tester {

    public static void main(String[] args) {
        try {
            List<ExcelExportEntity> colList = new ArrayList<>();
            ExcelExportEntity colEntity = new ExcelExportEntity("时间", "date", 30);
            colList.add(colEntity);

            ExcelExportEntity deliColGroup = new ExcelExportEntity("债务主体", "debtSubject");
            List<ExcelExportEntity> deliColList = new ArrayList<>();
            // n列数据
            deliColList.add(new ExcelExportEntity("A公司", "A公司", 15));
            deliColList.add(new ExcelExportEntity("B公司", "B公司", 15));
            deliColList.add(new ExcelExportEntity("C公司", "C公司", 15));
            deliColList.add(new ExcelExportEntity("D公司", "D公司", 15));
            deliColList.add(new ExcelExportEntity("E公司", "E公司", 15));
            deliColList.add(new ExcelExportEntity("总计", "total", 15));
            deliColGroup.setList(deliColList);
            colList.add(deliColGroup);

            List<Map<String, Object>> list = new ArrayList<>();
            // 10行数据
            for (int i = 0; i < 10; i++) {
                Map<String, Object> valMap = new HashMap<>();

                valMap.put("date", "2023年11月6日" + i);

                List<Map<String, Object>> deliDetailList = new ArrayList<>();

                Map<String, Object> deliValMap = new HashMap<>();
                deliValMap.put("A公司", 500 + i);
                deliValMap.put("B公司", 600 + i);
                deliValMap.put("C公司", 2000 + i);
                deliValMap.put("D公司", 1150 + i);
                deliValMap.put("E公司", 1500 + i);
                deliValMap.put("total", 10000 + i);
                deliDetailList.add(deliValMap);

                valMap.put("debtSubject", deliDetailList);

                list.add(valMap);
            }

            ExportParams exportParams = new ExportParams("XX集团2023年11月还本付息汇总表", "XX集团2023年11月还本付息汇总表");
            //exportParams.setStyle(ExcelExportStyler.class);

            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, colList, list);
            Date date = new Date();
            FileOutputStream fos = new FileOutputStream(
                    "D:/价格分析表" +
                            date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds() +
                            ".xls"
            );
            workbook.write(fos);
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
