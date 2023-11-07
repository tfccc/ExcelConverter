package com.example.excelconverter.esaypoi;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.*;

public class ExcelExportStyler implements IExcelExportStyler {
    private static final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");
    private static final short FONT_SIZE_12 = 12;
    private static final short FONT_SIZE_16 = 16;
    /**
     * 大标题样式
     */
    private CellStyle headerStyle;
    /**
     * 每列标题样式
     */
    private CellStyle titleStyle;
    /**
     * 数据行样式
     */
    private CellStyle rowStyles;

    public ExcelExportStyler(Workbook workbook) {
        this.init(workbook);
    }

    /**
     * 初始化样式
     *
     * @param workbook *
     */
    private void init(Workbook workbook) {
        this.headerStyle = initHeaderStyle(workbook);
        this.titleStyle = initTitleStyle(workbook);
        this.rowStyles = getBaseCellStyle(workbook);
    }

    /**
     * 大标题样式
     *
     * @param color *
     */
    @Override
    public CellStyle getHeaderStyle(short color) {
        return headerStyle;
    }

    /**
     * 每列标题样式
     *
     * @param color *
     */
    @Override
    public CellStyle getTitleStyle(short color) {
        return titleStyle;
    }

    /**
     * 数据行样式
     *
     * @param parity 可以用来表示奇偶行
     * @param entity 数据内容
     * @return 样式
     */
    @Override
    public CellStyle getStyles(boolean parity, ExcelExportEntity entity) {
        return rowStyles;
    }

    /**
     * 获取样式方法
     *
     * @param dataRow 数据行
     * @param obj     对象
     * @param data    数据
     */
    @Override
    public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
        return getStyles(true, entity);
    }

    /**
     * 模板使用的样式设置
     */
    @Override
    public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
        return null;
    }

    /**
     * 初始化--大标题样式
     *
     * @param workbook *
     */
    private CellStyle initHeaderStyle(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setFont(getFont(workbook, FONT_SIZE_16, false));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 初始化--每列标题样式
     *
     * @param workbook *
     */
    private CellStyle initTitleStyle(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setFont(getFont(workbook, FONT_SIZE_12, false));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 基础样式
     */
    private CellStyle getBaseCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setFont(getFont(workbook, FONT_SIZE_12, false));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //style.setWrapText(true);    // 设置自动换行
        return style;
    }

    /**
     * 字体样式
     * @param size   字体大小
     * @param isBold 是否加粗
     */
    private Font getFont(Workbook workbook, short size, boolean isBold) {
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setBold(isBold);
        font.setFontHeightInPoints(size);
        return font;
    }
}