package com.example.excelconverter.entity;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;
import cn.afterturn.easypoi.excel.annotation.Excel;

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


    @Excel(name = "集团名称", fixedIndex = 0)
    private String groupName = null;

    @Excel(name = "债务主体", fixedIndex = 1)
    private String debtSubject = null;

    @Excel(name = "贷款银行/公司", fixedIndex = 2)
    private String loanCompanyOrBank = null;

    @Excel(name = "类别", fixedIndex = 3)
    private String type = null;

    @Excel(name = "本金", fixedIndex = 4)
    private String principal = null;
    @Excel(name = "利息", fixedIndex = 5)
    private String interest = null;
    @Excel(name = "本息合计", fixedIndex = 6)
    private String principalAndInterest = null;

    @Excel(name = "利息日期", /*groupName = "还款日期",*/ format = "yyyy年MM月dd日", fixedIndex = 7)
    private Date interestDate = null;
    @Excel(name = "本金日期", /*groupName = "还款日期",*/ format = "yyyy年MM月dd日", fixedIndex = 8)
    private Date principalDate = null;

}
