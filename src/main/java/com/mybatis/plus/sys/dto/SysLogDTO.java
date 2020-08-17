package com.mybatis.plus.sys.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;

/**
 * description
 *
 * @author jinliang 2019/05/23 20:51
 */
@Data
public class SysLogDTO extends BaseRowModel {

    @ExcelProperty(value = "ID")
    private Long id;
    //用户名
    @ExcelProperty(value = "用户")
    private String username;
    //用户操作
    @ExcelProperty(value = "操作")
    private String operation;
    //请求方法
    @ExcelProperty(value = "方法")
    private String method;
    //请求参数
    @ExcelProperty(value = "参数")
    private String params;
    //执行时长(毫秒)
    @ExcelProperty(value = "时长")
    private Long time;
    //IP地址
    @ExcelProperty(value = "IP")
    private String ip;
    //创建时间
    @ExcelProperty(value = "创建时间")
    private Date createDate;
}
