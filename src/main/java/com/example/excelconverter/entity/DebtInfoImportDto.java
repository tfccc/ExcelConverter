package com.example.excelconverter.entity;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;
import cn.afterturn.easypoi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Frank.Tang
 * @date 2023-11-06 15:50
 * @desc
 **/
@Data
public class DebtInfoImportDto implements IExcelDataModel, IExcelModel {

    /**
     * 行号
     */
    private int rowNum;
    /**
     * 错误消息
     */
    private String errorMsg;

    /** 本金+利息 */
    private BigDecimal total;


    @Excel(name = "债务主体", fixedIndex = 0)
    private String debtSubject;

    @Excel(name = "贷款银行/公司", fixedIndex = 1)
    private String loanCompanyOrBank;

    @Excel(name = "类别", fixedIndex = 2)
    private String type;

    @Excel(name = "合同金额", fixedIndex = 3)
    private String contactAmount;

    @Excel(name = "剩余未还", fixedIndex = 4)
    private String remainingUnReturned;

    @Excel(name = "本金", fixedIndex = 5)
    private BigDecimal principal;
    @Excel(name = "利息", fixedIndex = 6)
    private BigDecimal interest;
    @Excel(name = "本息合计", fixedIndex = 7)
    private BigDecimal principalAndInterest;

    @Excel(name = "本金日期", importFormat = "yyyy年MM月dd日",/*format = "yyyy年MM月dd日",*/ fixedIndex = 8)
    private String principalDate;
    @Excel(name = "利息日期", importFormat = "yyyy年MM月dd日",/*format = "yyyy年MM月dd日",*/ fixedIndex = 9)
    private String interestDate;

}
